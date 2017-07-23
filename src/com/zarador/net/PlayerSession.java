package com.zarador.net;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jboss.netty.channel.Channel;

import com.zarador.GameSettings;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketBuilder;
import com.zarador.net.packet.PacketConstants;
import com.zarador.net.packet.PacketListener;
import com.zarador.net.packet.codec.PacketDecoder;
import com.zarador.net.packet.impl.PlayerRelationPacketListener;
import com.zarador.world.entity.impl.player.Player;

/**
 * The session handler dedicated to a player that will handle input and output
 * operations.
 *
 * @author lare96 <http://github.com/lare96>
 * @author blakeman8192
 */
public final class PlayerSession {

	/**
	 * The queue of messages that will be handled on the next sequence.
	 */
	private final Queue<Packet> prioritizedMessageQueue = new ConcurrentLinkedQueue<>();
	private final Queue<Packet> messageQueue = new ConcurrentLinkedQueue<>();

	/**
	 * The channel that will manage the connection for this player.
	 */
	private final Channel channel;

	/**
	 * The player I/O operations will be executed for.
	 */
	private Player player;

	/**
	 * The current state of this I/O session.
	 */
	private SessionState state = SessionState.CONNECTED;

	/**
	 * Creates a new {@link PlayerSession}.
	 *
	 */
	public PlayerSession(Channel channel) {
		this.channel = channel;
	}

	/**
	 * Queues the {@code msg} for this session to be encoded and sent to the
	 * client.
	 *
	 * @param msg
	 *            the message to queue.
	 */
	public void queueMessage(PacketBuilder msg) {
		if (channel == null) {
			return;
		}
		
		try {
			if (!channel.isOpen())
				return;
			channel.write(msg.toPacket());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Processes all of the queued messages from the {@link PacketDecoder} by
	 * polling the internal queue, and then handling them via the
	 * handleInputMessage.
	 */
	public void handleQueuedMessages() {
		handlePrioritizedMessageQueue();
		Packet msg;
		while ((msg = messageQueue.poll()) != null) {
			handleInputMessage(msg);
		}
	}

	public void handlePrioritizedMessageQueue() {
		Packet msg;
		while ((msg = prioritizedMessageQueue.poll()) != null) {
			handleInputMessage(msg);
		}
	}

	/**
	 * Handles an incoming message.
	 *
	 * @param msg
	 *            The message to handle.
	 */
	public void handleInputMessage(Packet msg) {
		try {
			int op = msg.getOpcode();

			PacketListener listener = PacketConstants.PACKETS[op];
			if (op != 11 && op != 60 && op != 5 && op != 12 && op != 103 && op != 230 && op != 4 && op != 98
					&& op != 164 && op != 248 && op != 104 && !(listener instanceof PlayerRelationPacketListener)) {
				if (msg.getLength() != PacketConstants.MESSAGE_SIZES[op]) {
					//System.out.println("Ouch " + op);
					//System.out.println(op + " Length: " + msg.getLength());
					return;
				}
			}
			long start = System.currentTimeMillis();
			listener.handleMessage(player, msg);
			long time = System.currentTimeMillis() - start;
			if (time > 50) {
				msg.setTime(time);
				//GameServer.getPanel().addPacket(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Uses state-machine to handle upstream messages from Netty.
	 *
	 * @param msg
	 *            the message to handle.
	 */
	public void handleIncomingMessage(Packet msg) {
		if (messageQueue.size() <= GameSettings.DECODE_LIMIT) {
			if (msg.prioritize()) {
				prioritizedMessageQueue.add(msg);
			} else {
				messageQueue.add(msg);
			}
		}
	}

	public void clearMessages() {
		messageQueue.clear();
	}

	/**
	 * Gets the player I/O operations will be executed for.
	 *
	 * @return the player I/O operations.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the current state of this I/O session.
	 *
	 * @return the current state.
	 */
	public SessionState getState() {
		return state;
	}

	/**
	 * Sets the value for {@link PlayerSession#state}.
	 *
	 * @param state
	 *            the new value to set.
	 */
	public void setState(SessionState state) {
		this.state = state;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
