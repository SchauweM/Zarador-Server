package com.zarador.model;

import java.util.ArrayList;
import java.util.List;

import com.zarador.util.Misc;
import com.zarador.util.NameUtils;
import com.zarador.world.World;
import com.zarador.world.content.PlayerPunishment;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.clan.ClanChatManager;
import com.zarador.world.entity.impl.player.Player;

/**
 * This file represents a player's relation with other world entities, this
 * manages adding and removing friends who we can chat with and also adding and
 * removing ignored players who will not be able to message us or see us online.
 *
 * @author relex lawl Redone a bit by Gabbe
 */

public class PlayerRelations {

	/**
	 * The player's current friend status, checks if others will be able to see
	 * them online.
	 */
	private PrivateChatStatus status = PrivateChatStatus.ON;

	/**
	 * This map contains the player's friends list.
	 */
	private List<Long> friendList = new ArrayList<Long>(200);

	/**
	 * This map contains the player's ignore list.
	 */
	private List<Long> ignoreList = new ArrayList<Long>(100);

	/**
	 * The player's current private message index.
	 */
	private int privateMessageId = 1;

	/**
	 * Gets the current private message index.
	 * 
	 * @return The current private message index + 1.
	 */
	public int getPrivateMessageId() {
		return privateMessageId++;
	}

	/**
	 * Sets the current private message index.
	 * 
	 * @param privateMessageId
	 *            The new private message index value.
	 * @return The PlayerRelations instance.
	 */
	public PlayerRelations setPrivateMessageId(int privateMessageId) {
		this.privateMessageId = privateMessageId;
		return this;
	}

	public PlayerRelations setStatus(PrivateChatStatus status, boolean update) {
		this.status = status;
		if (update)
			updateLists(true, 0);
		return this;
	}

	public PrivateChatStatus getStatus() {
		return this.status;
	}

	/**
	 * Gets the player's friend list.
	 * 
	 * @return The player's friends.
	 */
	public List<Long> getFriendList() {
		return friendList;
	}

	/**
	 * Gets the player's ignore list.
	 * 
	 * @return The player's ignore list.
	 */
	public List<Long> getIgnoreList() {
		return ignoreList;
	}

	/**
	 * Updates the player's friend list.
	 * 
	 * @param online
	 *            If <code>true</code>, the players who have this player added,
	 *            will be sent the notification this player has logged in.
	 * @return The PlayerRelations instance.
	 */
	public PlayerRelations updateLists(boolean online, int sendmessage) {
		if (status == PrivateChatStatus.OFF)
			online = false;
		player.getPacketSender().sendFriendStatus(2);
		/*
		 * for (Player players : World.getPlayers()) { if(players == null)
		 * continue; boolean temporaryOnlineStatus = online; if
		 * (players.getRelations().friendList.contains(player.getLongUsername())
		 * ) { if (status.equals(PrivateChatStatus.FRIENDS_ONLY) &&
		 * !friendList.contains(players.getLongUsername()) ||
		 * status.equals(PrivateChatStatus.OFF) ||
		 * ignoreList.contains(players.getLongUsername())) {
		 * temporaryOnlineStatus = false; }
		 * players.getPacketSender().sendFriend(player.getLongUsername(),
		 * temporaryOnlineStatus ? 1 : 0); } boolean tempOn = true; if
		 * (player.getRelations().friendList.contains(players.getLongUsername())
		 * ) { if
		 * (players.getRelations().status.equals(PrivateChatStatus.FRIENDS_ONLY)
		 * && !players.getRelations().getFriendList().contains(player.
		 * getLongUsername()) ||
		 * players.getRelations().status.equals(PrivateChatStatus.OFF) ||
		 * players.getRelations().getIgnoreList().contains(player.
		 * getLongUsername())) { tempOn = false; }
		 * player.getPacketSender().sendFriend(players.getLongUsername(), tempOn
		 * ? 1 : 0); } }
		 */
		for (long list : friendList) {
			player.getPacketSender().sendFriend(list, World.getPlayerForLongName(list) != null ? 1 : 0, sendmessage);
		}
		for (Player other : World.getPlayers()) {
			if (other != null && other.getRelations().getFriendList().contains(player.getLongUsername())) {
				other.getPacketSender().sendFriend(player.getLongUsername(), online ? 1 : 0, 0);
			}
		}
		return this;
	}

	public void sendStatus() {
		int privateChat = status == PrivateChatStatus.OFF ? 2 : status == PrivateChatStatus.FRIENDS_ONLY ? 1 : 0;
		player.getPacketSender().sendChatOptions(0, privateChat, 0);
	}

	public void sendFriends() {
		for (int i = 0; i < player.getRelations().getFriendList().size(); i++) {
			player.getPacketSender().sendFriend(player.getRelations().getFriendList().get(i), 0, 1);
		}
	}

	public PlayerRelations onLogin(Player player) {
		player.getPacketSender().sendIgnoreList();
		sendFriends();
		sendStatus();
		return this;
	}

	/**
	 * Adds a player to the associated-player's friend list.
	 * 
	 * @param username
	 *            The user name of the player to add to friend list.
	 */
	public void addFriend(Long username) {
		String name = NameUtils.longToString(username);
		if (friendList.size() >= 200) {
			player.getPacketSender().sendMessage("Your friend list is full, the max you can have is 200!");
			return;
		}
		if (ignoreList.contains(username)) {
			player.getPacketSender().sendMessage("Please remove " + name + " from your ignore list first.");
			return;
		}
		if (friendList.contains(username)) {
			player.getPacketSender().sendMessage(name + " is already on your friends list!");
		} else {
			friendList.add(username);
			// sendFriends();
			updateLists(true, 0);
			Player friend = World.getPlayerByName(name);
			if (friend != null) {
				friend.getRelations().updateLists(true, 0);
				if (player.getCurrentClanChat() != null) {
					ClanChatManager.checkFriendsRank(friend, player.getCurrentClanChat(), true);
				}
			}
			Achievements.finishAchievement(player, Achievements.AchievementData.ADD_FRIEND);
		}
		player.getPacketSender().sendString(5067,
				"Friends List (" + player.getRelations().getFriendList().size() + "/200)");
	}

	/*
	 * Checks if a player is friend with someone.
	 */
	public boolean isFriendWith(String player) {
		return friendList.contains(NameUtils.stringToLong(player));
	}

	/**
	 * Deletes a friend from the associated-player's friends list.
	 * 
	 * @param username
	 *            The user name of the friend to delete.
	 */
	public void deleteFriend(Long username) {
		if (friendList.contains(username)) {
			friendList.remove(username);
			if (!status.equals(PrivateChatStatus.ON)) {
				Player ignored = World.getPlayerByName(NameUtils.longToString(username));
				if (ignored != null)
					ignored.getRelations().updateLists(false, 0);
				updateLists(false, 0);
			}
		} else {
			player.getPacketSender().sendMessage("This player is not on your friends list!");
		}
		player.getPacketSender().sendString(5067,
				"Friends List (" + player.getRelations().getFriendList().size() + "/200)");
	}

	/**
	 * Adds a player to the associated-player's ignore list.
	 * 
	 * @param username
	 *            The user name of the player to add to ignore list.
	 */
	public void addIgnore(Long username) {
		String name = NameUtils.longToString(username);
		if (ignoreList.size() >= 100) {
			player.getPacketSender().sendMessage("Your ignore list is full!");
			return;
		}
		if (friendList.contains(username)) {
			player.getPacketSender().sendMessage("Please remove " + name + " from your friend list first.");
			return;
		}
		if (ignoreList.contains(username)) {
			player.getPacketSender().sendMessage(name + " is already on your ignore list!");
		} else {
			ignoreList.add(username);
			player.getPacketSender().sendIgnoreList();
			updateLists(true, 0);
			Player ignored = World.getPlayerByName(name);
			if (ignored != null)
				ignored.getRelations().updateLists(false, 0);
		}
	}

	/**
	 * Deletes an ignored player from the associated-player's ignore list.
	 * 
	 * @param username
	 *            The user name of the ignored player to delete from ignore
	 *            list.
	 */
	public void deleteIgnore(Long username) {
		if (ignoreList.contains(username)) {
			ignoreList.remove(username);
			player.getPacketSender().sendIgnoreList();
			updateLists(true, 0);
			if (status.equals(PrivateChatStatus.ON)) {
				Player ignored = World.getPlayerByName(NameUtils.longToString(username));
				if (ignored != null)
					ignored.getRelations().updateLists(true, 0);
			}
		} else {
			player.getPacketSender().sendMessage("This player is not on your ignore list!");
		}
	}

	/**
	 * Sends a private message to {@code friend}.
	 * 
	 * @param friend
	 *            The player to private message.
	 * @param message
	 *            The message being sent in bytes.
	 * @param size
	 *            The size of the message.
	 */
	public void message(Player friend, byte[] message, int size) {
		int tlNeeded = 149;
		if (PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())) {
			player.getPacketSender().sendMessage("You are muted and cannot chat.");
			return;
		}
		if (this.player.getSkillManager().getTotalLevel() < tlNeeded && !friend.getStaffRights().isStaff()) {
			this.player.getPacketSender().sendMessage(
					"You cannot use the private message until you have over " + (tlNeeded + 1) + " total level.");
			this.player.getPacketSender().sendMessage(
					"You can only pm the staff currently online, if you need something. Have fun training!");
			return;
		}
		if (friend == null || message == null) {
			this.player.getPacketSender().sendMessage("This player is currently offline.");
			return;
		}
		if (friend.getRelations().status.equals(PrivateChatStatus.FRIENDS_ONLY)
				&& !friend.getRelations().friendList.contains(player.getLongUsername())
				|| friend.getRelations().status.equals(PrivateChatStatus.OFF)) {
			this.player.getPacketSender().sendMessage("This player is currently offline.");
			return;
		}
		if (status == PrivateChatStatus.OFF) {
			setStatus(PrivateChatStatus.FRIENDS_ONLY, true);
		}
		if (Misc.blockedWord(Misc.textUnpack(message, size).replaceAll(";", "."))) {
			// Logs.write_data(this.player.getUsername()+ ".txt", "advertisers",
			// "Player was caught saying
			// in private message: "+Misc.textUnpack(message,
			// size).replaceAll(";", ".")+"");
			return;
		}
		friend.getPacketSender().sendPrivateMessage(player.getLongUsername(), player.getStaffRights(), message, size,
				player);
		// Logs.write_data(friend.getUsername()+ ".txt", "private_messages",
		// "Recieved from
		// "+this.player.getUsername()+": " + Misc.textUnpack(message,
		// size).replaceAll(";", "."));
		// Logs.write_data(this.player.getUsername()+ ".txt",
		// "private_messages", "Sent to
		// "+friend.getUsername()+": " + Misc.textUnpack(message,
		// size).replaceAll(";", "."));
	}

	/**
	 * Represents a player's friends list status, whether others will be able to
	 * see them online or not.
	 */
	public static enum PrivateChatStatus {
		ON(990), FRIENDS_ONLY(991), OFF(992);

		PrivateChatStatus(int actionId) {
			this.actionId = actionId;
		}

		private int actionId;

		public int getActionId() {
			return this.actionId;
		}

		public static PrivateChatStatus forIndex(int i) {
			for (PrivateChatStatus status : PrivateChatStatus.values()) {
				if (status != null && status.ordinal() == i)
					return status;
			}
			return ON;
		}

		public static PrivateChatStatus forActionId(int id) {
			for (PrivateChatStatus status : PrivateChatStatus.values()) {
				if (status != null && status.getActionId() == id)
					return status;
			}
			return ON;
		}
	}

	/**
	 * The PlayerRelations constructor.
	 * 
	 * @param player
	 *            The associated-player.
	 */
	public PlayerRelations(Player player) {
		this.player = player;
	}

	/**
	 * The associated player.
	 */
	private Player player;
}
