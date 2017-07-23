package com.zarador.world.content.combat.strategy.impl;

import com.zarador.model.*;
import com.zarador.util.Misc;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.HitQueue.CombatHit;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * @Author Jonny
 * Handles the combat mechanics for Chaos Fanatic
 */

public class ChaosFanatic implements CombatStrategy {

	private static final Animation ATTACK_ANIMATION = new Animation(811);

	private static final int RED_GFX = 554;

	private static final int GREEN_GFX = 551;

	private static final String[] MESSAGES = {
			"BURN!",
			"WEUGH!",
			"Develish Oxen Roll!",
			"All your wilderness are belong to them!",
			"AhehHeheuhHhahueHuUEehEahAH",
			"I shall call him squidgy and he shall be my squidgy!"};

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character target) {
		NPC attacker = (NPC) entity;
		if (attacker.getConstitution() <= 0) {
			return true;
		}
		Player victim = (Player) target;
		CombatType style = CombatType.MAGIC;
		int clientSpeed;
		int gfxDelay;
		int hitDelay;
		switch (Misc.random(3)) {
			case 3:
				style = CombatType.GREEN_BOMB;
				break;
		}
		Position firstLocation = victim.getPosition();
		Position secondLocation = victim.getPosition().transform(1, 1, 0);
		Position thirdLocation = victim.getPosition().transform(-1, -1, 0);

		switch (style) {
			case MAGIC:
				attacker.performAnimation(ATTACK_ANIMATION);
				if (attacker.getPosition().isWithinDistance(victim.getPosition(), 1)) {
					clientSpeed = 50;
					gfxDelay = 60;
				} else if (attacker.getPosition().isWithinDistance(victim.getPosition(), 5)) {
					clientSpeed = 70;
					gfxDelay = 80;
				} else if (attacker.getPosition().isWithinDistance(victim.getPosition(), 8)) {
					clientSpeed = 90;
					gfxDelay = 100;
				} else {
					clientSpeed = 110;
					gfxDelay = 120;
				}
				hitDelay = (gfxDelay / 20) - 1;
				new Projectile(attacker, target, RED_GFX, 45, clientSpeed / 5, 43, 35, 50).sendProjectile();
				break;
			case GREEN_BOMB:
				attacker.performAnimation(ATTACK_ANIMATION);
				if (attacker.getPosition().isWithinDistance(victim.getPosition(), 1)) {
					clientSpeed = 70;
					gfxDelay = 80;
				} else if (attacker.getPosition().isWithinDistance(victim.getPosition(), 5)) {
					clientSpeed = 90;
					gfxDelay = 100;
				} else if (attacker.getPosition().isWithinDistance(victim.getPosition(), 8)) {
					clientSpeed = 110;
					gfxDelay = 120;
				} else {
					clientSpeed = 130;
					gfxDelay = 140;
				}
                target.dealDamage(null, new Hit(Misc.random(25, 100), Hitmask.DARK_GREEN, CombatIcon.NONE));
                new Projectile(attacker, target, GREEN_GFX, 45, clientSpeed / 5, 43, 35, 50).sendProjectile();
				break;
			default:
				gfxDelay = 0;
				break;
		}
        attacker.forceChat(MESSAGES[(int) (Math.random() * MESSAGES.length)]);
        new CombatHit(attacker.getCombatBuilder(), new CombatContainer(attacker, victim, 1, CombatType.MAGIC, true)).handleAttack();
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return 2;
	}

	@Override
	public int attackDistance(Character entity) {
		return 6;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}
}
