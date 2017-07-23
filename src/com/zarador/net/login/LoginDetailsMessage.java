package com.zarador.net.login;

import java.nio.channels.Channel;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.zarador.net.packet.Packet;

/**
 * The {@link Packet} implementation that contains data used for the final
 * portion of the login protocol.
 *
 * @author lare96 <http://github.org/lare96>
 */
public final class LoginDetailsMessage {

	/**
	 * The username of the player.
	 */
	private final String username;

	/**
	 * The password of the player.
	 */
	private final String password;

	/**
	 * The player's host address
	 */
	private final String host;

	/**
	 * The player's computer address.
	 */
	private final String computer_address;

	/**
	 * The player's client version.
	 */
	private final String clientVersion;

	/**
	 * The mac address.
	 */
	private final String mac_address;

	/**
	 * The unique serial.
	 */
	private final long serial;

	/**
	 * The player's client uid.
	 */
	private final int uid;

	/**
	 * Creates a new {@link LoginDetailsMessage}.
	 *
	 * @param ctx
	 *            the {@link ChannelHandlerContext} that holds our
	 *            {@link Channel} instance.
	 * @param username
	 *            the username of the player.
	 * @param password
	 *            the password of the player.
	 * @param encryptor
	 *            the encryptor for encrypting messages.
	 * @param decryptor
	 *            the decryptor for decrypting messages.
	 */
	public LoginDetailsMessage(String username, String password, String host, String cp_address, String mac_address,
			long serial, String clientVersion, int uid) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.computer_address = cp_address;
		this.mac_address = mac_address;
		this.serial = serial;
		this.clientVersion = clientVersion;
		this.uid = uid;
	}

	public long getSerial() {
		return serial;
	}

	public String getMacAddress() {
		return mac_address;
	}

	/**
	 * Gets the username of the player.
	 *
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the password of the player.
	 *
	 * @return the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the password of the player.
	 *
	 * @return the password.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Gets the player's serial number.
	 *
	 * @return the serial number.
	 */
	public String getComputerAddress() {
		return computer_address;
	}

	/**
	 * Gets the player's client version.
	 *
	 * @return the client version.
	 */
	public String getClientVersion() {
		return clientVersion;
	}

	/**
	 * Gets the player's client uid.
	 *
	 * @return the client's uid.
	 */
	public int getUid() {
		return uid;
	}
}
