package com.zarador.world.content;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.engine.task.impl.NPCRespawnTask;
import com.zarador.model.Locations;
import com.zarador.model.Position;
import com.zarador.model.RegionInstance;
import com.zarador.model.Locations.Location;
import com.zarador.model.RegionInstance.RegionInstanceType;
import com.zarador.world.World;
import com.zarador.world.content.clan.ClanChat;
import com.zarador.world.content.clan.ClanChatRank;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * Argos's bossing system Developed by High105
 **/

public class BossSystem {

	public enum Bosses {
		KBD(50), TD(8349), CORP(8133);

		private int npcId;

		private Bosses(int id) {
			npcId = id;
		}

		public int getBossID() {
			return npcId;
		}

	};

	/**
	 * @param player
	 * @param bossID
	 *            which boss is being spawned
	 * @param isSolo
	 *            if true (spawns just player) if false(spawns entire clan)
	 */
	public static void startInstance(Player player, int bossID, boolean isSolo) {
		if (!isSolo) {
			final ClanChat clan = player.getCurrentClanChat();
			player.moveTo(new Position(2861, 9640, player.getIndex() * 4));
			for (Player member : clan.getMembers()) {
				if (member != null) {
					ClanChatRank rank = clan.getRank(member);
					boolean move_in = false;
					int floor = member.getIndex() * 4;
					if (rank == ClanChatRank.OWNER || rank == ClanChatRank.STAFF) {
						move_in = true;
					}
					for (Player others : clan.getMembers()) {
						if (others != null) {
							if (move_in) {
								if (others.getLocation() == Location.BOSS_SYSTEM) {
									others.moveTo(new Position(2861, 9640, floor));
									others.setBossSolo(false);
								}
							}
						}
					}
				}
			}
		} else {
			player.moveTo(new Position(2861, 9640, player.getIndex() * 4));
		}
		player.setRegionInstance(new RegionInstance(player, RegionInstanceType.BOSS_SYSTEM));
		spawnBoss(player, bossID);

	}

	public static void leaveInstance(Player player) {
		if (!player.isBossSolo()) {
			Locations.Location.BOSS_SYSTEM.leave(player);
			final ClanChat clan = player.getCurrentClanChat();
			player.moveTo(new Position(2861, 9640, player.getIndex() * 4));
			for (Player member : clan.getMembers()) {
				if (member != null) {
					ClanChatRank rank = clan.getRank(member);
					boolean move_in = false;
					// int floor = member.getIndex() * 4;
					if (rank == ClanChatRank.OWNER || rank == ClanChatRank.STAFF) {
						move_in = true;
					}
					for (Player others : clan.getMembers()) {
						if (others != null) {
							if (move_in) {
								if (others.getLocation() == Location.BOSS_SYSTEM) {
									Locations.Location.BOSS_SYSTEM.leave(others);
								}
							}
						}
					}
				}
			}
		} else {
			Locations.Location.BOSS_SYSTEM.leave(player);
		}
	}

	public static void spawnBoss(final Player player, int bossID) {
		TaskManager.submit(new Task(2, player, false) {
			@Override
			public void execute() {
				if (player.getRegionInstance() == null || !player.isRegistered()
						|| player.getLocation() != Location.BOSS_SYSTEM) {
					stop();
					return;
				}
				player.setBossingSystem(true);
				NPC n = new NPC(bossID, new Position(2854, 9637, player.getPosition().getZ())).setSpawnedFor(player);
				World.register(n);
				player.getRegionInstance().getNpcsList().add(n);
				n.getCombatBuilder().attack(player);
				stop();
			}
		});
	}

	public static void bossKilled(final Player player, NPC n) {
		if (player.getRegionInstance() != null)
			player.getRegionInstance().getNpcsList().remove(n);
		World.deregister(n);
		TaskManager.submit(new NPCRespawnTask(n, 0));
		leaveInstance(player);
		player.setBossingSystem(false);
	}
}
