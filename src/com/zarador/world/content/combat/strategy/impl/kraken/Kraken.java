package com.zarador.world.content.combat.strategy.impl.kraken;

import com.zarador.model.Animation;
import com.zarador.model.Projectile;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.magic.CombatSpells;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class Kraken implements CombatStrategy {

	public boolean canAttack(Character entity, Character victim) {
		return true;
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		Player player = (Player)victim;
		NPC kraken = (NPC)entity;
		kraken.getCombatBuilder().setContainer(new CombatContainer(entity, player, 1, CombatType.MAGIC, true));
		new Projectile(entity, victim, 2705, 44, 3, 43, 31, 0);
		entity.performAnimation(new Animation(3991));
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return 5;
	}

	@Override
	public int attackDistance(Character entity) {
		return 30;
	}

	@Override
	public CombatType getCombatType() {
		return null;
	}
	
	public static void handleDeath(int playerPosition, Player player) {

	}

}