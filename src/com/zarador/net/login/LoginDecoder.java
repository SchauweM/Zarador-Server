package com.zarador.net.login;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.zarador.GameSettings;
import com.zarador.net.PlayerSession;
import com.zarador.net.packet.PacketBuilder;
import com.zarador.net.packet.codec.PacketDecoder;
import com.zarador.net.packet.codec.PacketEncoder;
import com.zarador.net.security.IsaacRandom;
import com.zarador.util.Misc;
import com.zarador.util.NameUtils;
import com.zarador.world.entity.impl.player.Player;

/**
 * the login requests.
 *
 * @author Gabriel Hannason
 */
public final class LoginDecoder extends FrameDecoder {

	private static final ConnectionThrottler throttler = new ConnectionThrottler(5);
	private static final int CONNECTED = 0;
	private static final int LOGGING_IN = 1;
	private int state = CONNECTED;
	private long seed;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (!channel.isConnected()) {
			return null;
		}
		final String ip_address = ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress();
		switch (state) {
		case CONNECTED:
			if (buffer.readableBytes() < 1) {
				return null;
			}
			int request = buffer.readUnsignedByte();
			if (request != 14) {
				System.out.println("Invalid login request: " + request);
				channel.close();
				return null;
			}
			boolean connectionAllowed = throttler.verifyConnection(ip_address);
			if (!connectionAllowed) {
				channel.close();
				return null;
			}
			seed = new SecureRandom().nextLong();
			channel.write(new PacketBuilder().put((byte) 0).putLong(seed).toPacket());
			state = LOGGING_IN;
			return null;
		case LOGGING_IN:
			if (buffer.readableBytes() < 2) {
				System.out.println("no readable bytes");
				return null;
			}
			int loginType = buffer.readByte();
			if (loginType != 16 && loginType != 18) {
				System.out.println("Invalid login type: " + loginType);
				channel.close();
				return null;
			}
			int blockLength = buffer.readByte() & 0xff;
			if (buffer.readableBytes() < blockLength) {
				channel.close();
				return null;
			}
			int magicId = buffer.readUnsignedByte();
			if (magicId != 0xFF) {
				System.out.println("Invalid magic id! magicId: " + magicId);
				channel.close();
				return null;
			}
			int clientVersion = buffer.readShort();
			int memory = buffer.readByte();
			if (memory != 0 && memory != 1) {
				System.out.println("Unhandled memory byte value");
				channel.close();
				return null;
			}
			/*
			 * int[] archiveCrcs = new int[9]; for (int i = 0; i < 9; i++) {
			 * archiveCrcs[i] = buffer.readInt(); }
			 */
			int length = buffer.readUnsignedByte();
			/**
			 * Our RSA components.
			 */
			ChannelBuffer rsaBuffer = buffer.readBytes(length);
			BigInteger bigInteger = new BigInteger(rsaBuffer.array());
			bigInteger = bigInteger.modPow(GameSettings.RSA_EXPONENT, GameSettings.RSA_MODULUS);
			rsaBuffer = ChannelBuffers.wrappedBuffer(bigInteger.toByteArray());
			int securityId = rsaBuffer.readByte();
			if (securityId != 10) {
				System.out.println("securityId id is not 10. It is " + securityId);
				channel.close();
				return null;
			}
			long clientSeed = rsaBuffer.readLong();
			long seedReceived = rsaBuffer.readLong();
			if (seedReceived != seed) {
				System.out.println("Unhandled seed read: [seed, seedReceived] : [" + seed + ", " + seedReceived + "]");
				channel.close();
				return null;
			}
			int[] seed = new int[4];
			seed[0] = (int) (clientSeed >> 32);
			seed[1] = (int) clientSeed;
			seed[2] = (int) (this.seed >> 32);
			seed[3] = (int) this.seed;
			IsaacRandom decodingRandom = new IsaacRandom(seed);
			for (int i = 0; i < seed.length; i++) {
				seed[i] += 50;
			}
			int uid = rsaBuffer.readInt();
			String username = Misc.readString(rsaBuffer);
			String password = Misc.readString(rsaBuffer);
			String client_version = Misc.readString(rsaBuffer);
			String computer_address = Misc.readString(rsaBuffer);
			String mac_address = "not-set";
			long serial = -1L;
			try {
				if (Double.parseDouble(client_version) >= 1.00) {
					mac_address = Misc.readString(rsaBuffer);
					serial = rsaBuffer.readLong();
				}
			} catch (NumberFormatException e) {
				System.err.println("Invalid client version: " + client_version + ", player: " + username);
			}

			if (username.length() > 12 || password.length() > 20) {
				System.out.println("Username or password length too long");
				return null;
			}
			username = Misc.formatText(username.toLowerCase());
			channel.getPipeline().replace("encoder", "encoder", new PacketEncoder(new IsaacRandom(seed)));
			channel.getPipeline().replace("decoder", "decoder", new PacketDecoder(decodingRandom));
			return login(channel, new LoginDetailsMessage(username, password, ip_address, computer_address, mac_address,
					serial, client_version, uid));
		}
		return null;
	}

	public Player login(Channel channel, LoginDetailsMessage msg) {
		PlayerSession session = new PlayerSession(channel);
		Player player = new Player(session).setUsername(msg.getUsername())
				.setLongUsername(NameUtils.stringToLong(msg.getUsername())).setPassword(msg.getPassword())
				.setHostAddress(msg.getHost()).setComputerAddress(msg.getComputerAddress())
				.setSerialNumber(msg.getSerial()).setMacAddress(msg.getMacAddress());
		// CharacterConversion.convert(channel);
		session.setPlayer(player);
		LoginManager.startLogin(session, msg);
		return player;
	}

}
