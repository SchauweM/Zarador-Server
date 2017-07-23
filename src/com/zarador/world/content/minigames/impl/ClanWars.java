package com.zarador.world.content.minigames.impl;

import com.zarador.model.Position;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.minigames.impl.Dueling.DuelRule;
import com.zarador.world.entity.impl.player.Player;

public class ClanWars {

	Player player;

	public ClanWars(Player player) {
		this.player = player;
	}

	public enum Rules {
		// combat rules
		COMBAT_MELEE(false), COMBAT_MAGE(false), COMBAT_RANGE(false), COMBAT_PRAYER(false), COMBAT_SUMMONING(
				false), COMBAT_FOOD(false), COMBAT_POTS(false),

		// time limit
		NO_LIMIT(1000), FIVE_MINUTES(5), TEN_MINUTES(10), THIRTY_MINUTES(30),

		// kills
		FIRST_25(25, true), FIRST_50(50, true), FIRST_75(75, true), FIRST_100(100, true), LAST_TEAM_STADING(50, false),

		// Arenas
		ARENA1(3333, 3333, 0), ARENA2(3333, 3333, 0), ARENA3(3333, 3333, 0), ARENA4(3333, 3333, 0);

		private int x, y, z;

		private Rules(int a, int b, int c) {
			x = a;
			y = b;
			z = c;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}

		private int kills;
		private boolean respawns;

		private Rules(int kill, boolean res) {
			kills = kill;
			respawns = res;
		}

		public int getKills() {
			return kills;
		}

		public boolean getRespawn() {
			return respawns;
		}

		private int time;

		private Rules(int t) {
			time = t;
		}

		public int getTime() {
			return time;
		}

		private boolean toggle;

		private Rules(boolean tog) {
			toggle = tog;
		}

		public boolean getToggle() {
			return toggle;
		}

	}

	public boolean[] selectedRules = new boolean[Rules.values().length];
	public int duelingWith = -1;

	public void selectRule(DuelRule duelRule) {
		final Player playerToChallenge = World.getPlayers().get(duelingWith);
		if (playerToChallenge == null)
			return;
		if (player.getInterfaceId() != 6575)
			return;
		int index = duelRule.ordinal();
		boolean alreadySet = selectedRules[duelRule.ordinal()];
		boolean slotOccupied = duelRule.getEquipmentSlot() > 0
				? player.getEquipment().getItems()[duelRule.getEquipmentSlot()].getId() > 0
						|| playerToChallenge.getEquipment().getItems()[duelRule.getEquipmentSlot()].getId() > 0
				: false;
		if (duelRule == DuelRule.NO_SHIELD) {
			if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() > 0
					&& ItemDefinition.forId(player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId())
							.isTwoHanded()
					|| ItemDefinition.forId(playerToChallenge.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId())
							.isTwoHanded())
				slotOccupied = true;
		}
		int spaceRequired = slotOccupied ? duelRule.getInventorySpaceReq() : 0;
		for (int i = 10; i < this.selectedRules.length; i++) {
			if (selectedRules[i]) {
				DuelRule rule = DuelRule.forId(i);
				if (rule.getEquipmentSlot() > 0)
					if (player.getEquipment().getItems()[rule.getEquipmentSlot()].getId() > 0
							|| playerToChallenge.getEquipment().getItems()[rule.getEquipmentSlot()].getId() > 0)
						spaceRequired += rule.getInventorySpaceReq();
			}
		}
		if (!alreadySet && player.getInventory().getFreeSlots() < spaceRequired) {
			player.getPacketSender().sendMessage("You do not have enough free inventory space to set this rule.");
			return;
		}
		if (!alreadySet && playerToChallenge.getInventory().getFreeSlots() < spaceRequired) {
			player.getPacketSender().sendMessage("" + playerToChallenge.getUsername()
					+ " does not have enough inventory space for this rule to be set.");
			return;
		}
		if (!player.getDueling().selectedDuelRules[index]) {
			player.getDueling().selectedDuelRules[index] = true;
			player.getDueling().duelConfig += duelRule.getConfigId();
		} else {
			player.getDueling().selectedDuelRules[index] = false;
			player.getDueling().duelConfig -= duelRule.getConfigId();
		}
		player.getPacketSender().sendToggle(286, player.getDueling().duelConfig);
		playerToChallenge.getDueling().duelConfig = player.getDueling().duelConfig;
		playerToChallenge.getDueling().selectedDuelRules[index] = player.getDueling().selectedDuelRules[index];
		playerToChallenge.getPacketSender().sendToggle(286, playerToChallenge.getDueling().duelConfig);
		player.getPacketSender().sendString(6684, "");
		if (selectedRules[DuelRule.OBSTACLES.ordinal()]) {
			if (selectedRules[DuelRule.NO_MOVEMENT.ordinal()]) {
				Position duelTele = new Position(3366 + Misc.getRandom(12), 3246 + Misc.getRandom(6), 0);
				player.getDueling().duelTelePos = duelTele;
				playerToChallenge.getDueling().duelTelePos = player.getDueling().duelTelePos.copy();
				playerToChallenge.getDueling().duelTelePos.setX(player.getDueling().duelTelePos.getX() - 1);
			}
		} else {
			if (selectedRules[DuelRule.NO_MOVEMENT.ordinal()]) {
				Position duelTele = new Position(3335 + Misc.getRandom(12), 3246 + Misc.getRandom(6), 0);
				player.getDueling().duelTelePos = duelTele;
				playerToChallenge.getDueling().duelTelePos = player.getDueling().duelTelePos.copy();
				playerToChallenge.getDueling().duelTelePos.setX(player.getDueling().duelTelePos.getX() - 1);
			}
		}

		if (duelRule == DuelRule.LOCK_WEAPON && selectedRules[duelRule.ordinal()]) {
			player.getPacketSender()
					.sendMessage(
							"@red@Warning! The rule 'Same Weapon' has been enabled. You will not be able to change")
					.sendMessage("@red@weapon during the duel!");
			playerToChallenge.getPacketSender()
					.sendMessage(
							"@red@Warning! The rule 'Same Weapon' has been enabled. You will not be able to change")
					.sendMessage("@red@weapon during the duel!");
		}
	}

	public static boolean handleDuelingButtons(final Player player, int button) {
		if (DuelRule.forButtonId(button) != null) {
			player.getDueling().selectRule(DuelRule.forButtonId(button));
			return true;
		} else {
			if (player.getDueling().duelingWith < 0)
				return false;
			final Player playerToDuel = World.getPlayers().get(player.getDueling().duelingWith);
			switch (button) {
			case 31081:

				break;
			case 31082:

				break;
			case 6674:
				if (!player.getDueling().inDuelScreen)
					return true;
				if (playerToDuel == null)
					return true;
				if (player.getDueling().selectedDuelRules[DuelRule.NO_MELEE.ordinal()]
						&& player.getDueling().selectedDuelRules[DuelRule.NO_RANGED.ordinal()]
						&& player.getDueling().selectedDuelRules[DuelRule.NO_MAGIC.ordinal()]) {
					player.getPacketSender()
							.sendMessage("You won't be able to attack the other player with the current rules.");
					return true;
				}
				player.getDueling().duelingStatus = 2;
				if (player.getDueling().duelingStatus == 2) {
					player.getPacketSender().sendString(6684, "Waiting for other player...");
					playerToDuel.getPacketSender().sendString(6684, "Other player has accepted.");
				}
				if (playerToDuel.getDueling().duelingStatus == 2) {
					playerToDuel.getPacketSender().sendString(6684, "Waiting for other player...");
					player.getPacketSender().sendString(6684, "Other player has accepted.");
				}
				if (player.getDueling().duelingStatus == 2 && playerToDuel.getDueling().duelingStatus == 2) {
					player.getDueling().duelingStatus = 3;
					playerToDuel.getDueling().duelingStatus = 3;
					playerToDuel.getDueling().confirmDuel();
					player.getDueling().confirmDuel();
				}
				return true;
			case 6520:
				player.getDueling().duelingStatus = 4;
				if (playerToDuel.getDueling().duelingStatus == 4 && player.getDueling().duelingStatus == 4) {
					player.getDueling().startDuel();
					playerToDuel.getDueling().startDuel();
				} else {
					player.getPacketSender().sendString(6571, "Waiting for other player...");
					playerToDuel.getPacketSender().sendString(6571, "Other player has accepted");
				}
				return true;
			}
		}
		return false;
	}

}
