package com.zarador.world.entity.impl.player;

import com.zarador.GameSettings;
import com.zarador.model.Locations;
import com.zarador.model.Skill;
import com.zarador.model.Locations.Location;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.LoyaltyProgramme;
import com.zarador.world.content.combat.pvp.BountyHunter;
import com.zarador.world.entity.impl.GroundItemManager;

public class PlayerProcess {

	/*
	 * The player (owner) of this instance
	 */
	private Player player;

	/*
	 * The loyalty tick, once this reaches 6, the player will be given loyalty
	 * points. 6 equals 3.6 seconds.
	 */
	private int loyaltyTick;

	/*
	 * The double xp tick, once this reaches 2 the player will have 1 second removed
	 * off their double xp. 2 equals 1 second.
	 */
	private int doubleXPTick;

	/*
	 * The timer tick, once this reaches 2, the player's total play time will be
	 * updated. 2 equals 1.2 seconds.
	 */
	private int timerTick;

	/*
	 * Makes sure ground items are spawned on height change
	 */
	private int previousHeight;

	public PlayerProcess(Player player) {
		this.player = player;
		this.previousHeight = player.getPosition().getZ();
	}

	public void sequence() {
		// player.getPacketSender().sendToggle(286, 1024 + 512 + 4096 + 2);
		/** COMBAT **/
		player.getCombatBuilder().process();

		if (player.getLocation() == Location.WILDERNESS) {
			boolean continue_method = true;
			boolean continue_lower_stats = false;
			if (player.getStaffRights().isManagement()) {
				continue_method = false;
			}
			if (continue_method) {
				if (player.getSkillManager().getCurrentLevel(Skill.ATTACK) > 118) {
					player.getSkillManager().setCurrentLevel(Skill.ATTACK, 118);
					continue_lower_stats = true;
				}
				if (player.getSkillManager().getCurrentLevel(Skill.STRENGTH) > 118) {
					player.getSkillManager().setCurrentLevel(Skill.STRENGTH, 118);
					continue_lower_stats = true;
				}
				if (player.getSkillManager().getCurrentLevel(Skill.DEFENCE) > 118) {
					player.getSkillManager().setCurrentLevel(Skill.DEFENCE, 118);
					continue_lower_stats = true;
				}
				if (continue_lower_stats) {
					player.getPacketSender().sendMessage("Your stats have been lowered for entering the wilderness.");
				}
			}
		}

		/** MISC **/

		if (previousHeight != player.getPosition().getZ()) {
			GroundItemManager.handleRegionChange(player);
			previousHeight = player.getPosition().getZ();
		}

		if (player.getRunEnergy() < 100) {
			player.setRunEnergy(player.getRunEnergy() + 0.25F);
		}
		if (player.getRunEnergy() <= 96 && player.isResting()) {
			player.setRunEnergy(player.getRunEnergy() + 2);
		}

		if (!player.isInActive()) {
			if (loyaltyTick >= 6) {
				LoyaltyProgramme.incrementPoints(player);
				loyaltyTick = 0;
			}
			loyaltyTick++;
		}

		if(player.getDoubleXP() > 0) {
			if (!player.isInActive() && !GameSettings.DOUBLE_EXP) {
				if (doubleXPTick >= 2) {
					player.decrementDoubleXP(1);
					doubleXPTick = 0;
				}
				doubleXPTick++;
			}
		}

		if (timerTick >= 1) {
			player.getPacketSender().sendString(55067, " Server Time: " + Misc.getCurrentServerTime());
			player.getPacketSender().sendString(55073, "   >- Players online: @cha@"+ (World.getPlayers().size() + GameSettings.fakePlayerCount));
			player.getPacketSender().sendString(55074, "   >- Staff online: @cha@"+ World.staffOnline());
			player.getPacketSender().sendString(55075, "   >- Wilderness: @cha@"+ Locations.PLAYERS_IN_WILD);
			player.getPacketSender().sendString(55078, "   >- Play Time: @cha@" + Misc.getTimePlayed(player.getTotalPlayTime() + (player.getRecordedLogin().elapsed())) + "");
			timerTick = 0;
		}
		timerTick++;

		player.getPlayerTimers().process();

		BountyHunter.sequence(player);

		/*
		 * if (player.getRegionInstance() != null &&
		 * (player.getRegionInstance().getType() ==
		 * RegionInstanceType.CONSTRUCTION_HOUSE ||
		 * player.getRegionInstance().getType() ==
		 * RegionInstanceType.CONSTRUCTION_DUNGEON)) { ((House)
		 * player.getRegionInstance()).process(); }
		 */
	}
}
