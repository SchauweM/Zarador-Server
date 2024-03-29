package com.zarador.world.content.combat.strategy.impl;

import com.zarador.util.Misc;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.magic.CombatSpells;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.content.minigames.impl.Dueling;
import com.zarador.world.content.minigames.impl.Dueling.DuelRule;
import com.zarador.world.entity.Entity;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * The default combat strategy assigned to an {@link Entity} during a magic
 * based combat session.
 *
 * @author lare96
 */
public class DefaultMagicCombatStrategy implements CombatStrategy {

	@Override
	public boolean canAttack(Character entity, Character victim) {

		// Npcs don't need to be checked.
		if (entity.isNpc()) {
			if (victim.isPlayer()) {
				Player p = (Player) victim;
				if (Nex.nexMinion(((NPC) entity).getId())) {
					if (!p.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
						return false;
					}
					return true;
				}
			}
			return true;
		}

		// Create the player instance.
		Player player = (Player) entity;

		if (Dueling.checkRule(player, DuelRule.NO_MAGIC)) {
			player.getPacketSender().sendMessage("Magic-attacks have been turned off in this duel!");
			player.getCombatBuilder().reset();
			return false;
		}
		// We can't attack without a spell.
		if (player.getCastSpell() == null)
			player.setCastSpell(player.getAutocastSpell());

		if (player.getCastSpell() == null) {
			return false;
		}

		// Check the cast using the spell implementation.
		return player.getCastSpell().canCast(player, true);
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {

		if (entity.isPlayer()) {
			Player player = (Player) entity;
			player.prepareSpell(player.getCastSpell(), victim);
			if (player.isAutocast() && player.getAutocastSpell() != null)
				player.setCastSpell(player.getAutocastSpell());
			player.setPreviousCastSpell(player.getCastSpell());
			player.getWalkingQueue().clear();
		} else if (entity.isNpc()) {
			NPC npc = (NPC) entity;

			switch (npc.getId()) {
			case 133: // cobra
				npc.prepareSpell(Misc.randomElement(new CombatSpells[] { CombatSpells.ICE_BARRAGE,
						CombatSpells.ICE_BLITZ, CombatSpells.BLOOD_BARRAGE }).getSpell(), victim);
				break;
			case 912: // zammy battle mage
				npc.prepareSpell(Misc.randomElement(new CombatSpells[] { CombatSpells.FIRE_WAVE,
						CombatSpells.FLAMES_OF_ZAMORAK, CombatSpells.BLOOD_BARRAGE, CombatSpells.ICE_BARRAGE }).getSpell(), victim);
				break;
			case 913: // sara battle mage
				npc.prepareSpell(Misc.randomElement(new CombatSpells[] { CombatSpells.WATER_WAVE,
						CombatSpells.SARADOMIN_STRIKE, CombatSpells.ICE_BARRAGE }).getSpell(), victim);
				break;
			case 914: // guthix battle mage
				npc.prepareSpell(Misc.randomElement(new CombatSpells[] { CombatSpells.EARTH_WAVE,
						CombatSpells.CLAWS_OF_GUTHIX, CombatSpells.ICE_BARRAGE }).getSpell(), victim);
				break;
			case 13:
			case 172:
			case 174:
				npc.prepareSpell(Misc.randomElement(new CombatSpells[] { CombatSpells.WEAKEN, CombatSpells.FIRE_STRIKE,
						CombatSpells.EARTH_STRIKE, CombatSpells.WATER_STRIKE }).getSpell(), victim);
				break;
			case 2025:
			case 1643:
				npc.prepareSpell(Misc.randomElement(
						new CombatSpells[] { CombatSpells.FIRE_WAVE, CombatSpells.EARTH_WAVE, CombatSpells.WATER_WAVE })
						.getSpell(), victim);
				break;
			case 3495:
				npc.prepareSpell(Misc.randomElement(new CombatSpells[] { CombatSpells.SMOKE_BLITZ,
						CombatSpells.ICE_BLITZ, CombatSpells.ICE_BURST, CombatSpells.SHADOW_BARRAGE }).getSpell(),
						victim);
				break;
			case 3496:
				npc.prepareSpell(Misc.randomElement(new CombatSpells[] { CombatSpells.BLOOD_BARRAGE,
						CombatSpells.BLOOD_BURST, CombatSpells.BLOOD_BLITZ, CombatSpells.BLOOD_RUSH }).getSpell(),
						victim);
				break;
			case 3491:
				npc.prepareSpell(Misc.randomElement(new CombatSpells[] { CombatSpells.ICE_BARRAGE,
						CombatSpells.ICE_BLITZ, CombatSpells.ICE_BURST, CombatSpells.ICE_RUSH }).getSpell(), victim);
				break;
			case 13454:
				npc.prepareSpell(CombatSpells.ICE_BLITZ.getSpell(), victim);
				break;
			case 13453:
				npc.prepareSpell(CombatSpells.BLOOD_BURST.getSpell(), victim);
				break;
			case 13452:
				npc.prepareSpell(CombatSpells.SHADOW_BARRAGE.getSpell(), victim);
				break;
			case 13451:
				npc.prepareSpell(CombatSpells.SHADOW_BURST.getSpell(), victim);
				break;
			case 2896:
				npc.prepareSpell(CombatSpells.WATER_STRIKE.getSpell(), victim);
				break;
			case 6254:
				npc.prepareSpell(CombatSpells.WIND_WAVE.getSpell(), victim);
				break;
			case 6257:
				npc.prepareSpell(CombatSpells.WATER_WAVE.getSpell(), victim);
				break;
			case 6278:
				npc.prepareSpell(CombatSpells.SHADOW_RUSH.getSpell(), victim);
				break;
			case 6221:
				npc.prepareSpell(CombatSpells.FIRE_BLAST.getSpell(), victim);
				break;
			}

			if (npc.getCurrentlyCasting() == null)
				npc.prepareSpell(CombatSpells.WIND_STRIKE.getSpell(), victim);
		}

		if (entity.getCurrentlyCasting().maximumHit() == -1) {
			return new CombatContainer(entity, victim, CombatType.MAGIC, true);
		}

		return new CombatContainer(entity, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		int distance = 8;
		if (entity.isNpc()) {
			switch (((NPC) entity).getId()) {
			case 2896:
			case 13451:
			case 13452:
			case 13453:
			case 13454:
				distance = 40;
				break;
			}
		}
		return distance;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		return false;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}
