package com.zarador.world.content.clan;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.util.Stopwatch;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

/**
 * An instance of a clanchat channel, holding all fields.
 *
 * @author Gabriel Hannason
 */
public class ClanChat {

	public ClanChat(Player owner, String name, int index) {
		this.owner = owner;
		this.name = name;
		this.index = index;
		this.ownerName = owner.getUsername();
	}

	public ClanChat(String ownerName, String name, int index) {
		this.owner = World.getPlayerByName(ownerName);
		this.ownerName = ownerName;
		this.name = name;
		this.index = index;
	}

	private String name;
	private Player owner;
	private String ownerName;
	private final int index;
	private boolean lootShare;
	private Stopwatch lastAction = new Stopwatch();

	private ClanChatRank[] rankRequirement = new ClanChatRank[4];
	private CopyOnWriteArrayList<Player> members = new CopyOnWriteArrayList<Player>();
	private CopyOnWriteArrayList<String> bannedNames = new CopyOnWriteArrayList<String>();
	private Map<String, ClanChatRank> rankedNames = new HashMap<String, ClanChatRank>();

	public Player getOwner() {
		return owner;
	}

	public ClanChat setOwner(Player owner) {
		this.owner = owner;
		return this;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public ClanChat setName(String name) {
		this.name = name;
		return this;
	}

	public boolean getLootShare() {
		return lootShare;
	}

	public void setLootShare(boolean lootShare) {
		this.lootShare = lootShare;
	}

	public Stopwatch getLastAction() {
		return lastAction;
	}

	public ClanChat addMember(Player member) {
		if (members.contains(member)) {
			// members.remove(member);
		}
		members.add(member);
		return this;
	}

	public ClanChat removeMember(String name) {
		for (int i = 0; i < members.size(); i++) {
			Player member = members.get(i);
			if (member == null)
				continue;
			if (member.getUsername().equals(name)) {
				members.remove(i);
				break;
			}
		}
		return this;
	}

	public ClanChatRank getRank(Player player) {
		return rankedNames.get(player.getUsername());
	}

	public ClanChat giveRank(Player player, ClanChatRank rank) {
		rankedNames.put(player.getUsername(), rank);
		return this;
	}

	public CopyOnWriteArrayList<Player> getMembers() {
		return members;
	}

	public Map<String, ClanChatRank> getRankedNames() {
		return rankedNames;
	}

	public CopyOnWriteArrayList<String> getBannedNames() {
		return bannedNames;
	}

	public void addBannedName(String name) {
		if (!bannedNames.contains(name)) {
			bannedNames.add(name);
			TaskManager.submit(new Task(1) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick == 2000) { // 20 minutes
						stop();
						return;
					}
					tick++;
				}

				@Override
				public void stop() {
					setEventRunning(false);
					bannedNames.remove(name);
				}
			});
		}
	}

	public boolean isBanned(String name) {
		return bannedNames.contains(name);
	}

	public ClanChatRank[] getRankRequirement() {
		return rankRequirement;
	}

	public ClanChat setRankRequirements(int index, ClanChatRank rankRequirement) {
		this.rankRequirement[index] = rankRequirement;
		return this;
	}

	/**
	 * This array contains all the rank requirements needed to be met in order
	 * to perform certain actions in this clan chat; see
	 * {@link #RANK_REQUIRED_TO_ENTER}, {@link #RANK_REQUIRED_TO_KICK},
	 * {@link #RANK_REQUIRED_TO_TALK} and {@link #RANK_REQUIRED_TO_BAN}.
	 */
	private final int[] rankReqs = { -1, -1, ClanChatRank.CAPTAIN.ordinal(), ClanChatRank.CAPTAIN.ordinal() };

	/**
	 * Sets the designated {@link #rankRequirement} with said index.
	 *
	 * @param index
	 *            The index of the rank requirement to edit.
	 * @param rankRequirement
	 *            The new rank value.
	 * @return The ClanChat instance.
	 */
	public ClanChat setRankReqs(int index, int rankRequirement) {
		this.rankReqs[index] = rankRequirement;
		return this;
	}

	/**
	 * Gets the array of rank requirements in order to do certain actions in the
	 * {@link ClanChat}.
	 *
	 * @return The {@link #rankRequirement} array.
	 */
	public int[] getRankReqs() {
		return rankReqs;
	}

	public static final int RANK_REQUIRED_TO_ENTER = 0, RANK_REQUIRED_TO_KICK = 1, RANK_REQUIRED_TO_TALK = 2,
			RANK_REQUIRED_TO_BAN = 3;

}
