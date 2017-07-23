package com.zarador.world.content.combat.strategy.impl;

import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.Locations;
import com.zarador.util.Misc;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class Zilyana implements CombatStrategy {

	public static String[] randomMessage = {
			"Death to the enemies of the light!", "Slay the evil ones!", "Saradomin lend me strength!",
			"By the power of Saradomin!", "Good will always triumph!", "Forward! Our allies are with us!",
			"Saradomin is with us!", "In the name of Saradomin!", "Attack! Find the Godsword!"
	};

	public static String getRandomMessage() {
		return randomMessage[(int) (Math.random() * randomMessage.length)];
	}
	private static final Animation attack_anim = new Animation(6967);

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer()
				&& ((Player) victim).getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		int speechChance = Misc.inclusiveRandom(1, 4);
		NPC zilyana = (NPC) entity;
		if (victim.getConstitution() <= 0) {
			return true;
		}
		if (Locations.goodDistance(zilyana.getPosition().copy(), victim.getPosition().copy(), 1)
				&& Misc.getRandom(5) <= 3) {
			zilyana.performAnimation(new Animation(zilyana.getDefinition().getAttackAnimation()));
			zilyana.getCombatBuilder().setContainer(new CombatContainer(zilyana, victim, 1, 1, CombatType.MELEE, true));
			if(speechChance == 1) {
				zilyana.forceChat(getRandomMessage());
			}
		} else {
			zilyana.performAnimation(attack_anim);
			zilyana.performGraphic(new Graphic(1220));
			zilyana.getCombatBuilder().setContainer(new CombatContainer(zilyana, victim, 2, 3, CombatType.MAGIC, true));
			zilyana.getCombatBuilder().setAttackTimer(7);
			if(speechChance == 1) {
				zilyana.forceChat(getRandomMessage());
			}
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 1;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
