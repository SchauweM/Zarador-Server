package com.zarador.world.content.combat.strategy.impl;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.GraphicHeight;
import com.zarador.model.Locations;
import com.zarador.model.Projectile;
import com.zarador.model.Skill;
import com.zarador.model.Locations.Location;
import com.zarador.util.Misc;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.HitQueue.CombatHit;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class Tsutsuroth implements CombatStrategy {

	public static String[] randomMessage = {
			"Attack them, you dogs!", "Forward!", "Death to Saradomin's dogs!", "Kill them, you cowards!", "The Dark One will have their souls!",
			"Zamorak curse them!", "Rend them limb from limb!", "No retreat!", "Flay them all!"
	};

	public static String getRandomMessage() {
		return randomMessage[(int) (Math.random() * randomMessage.length)];
	}

	private static final Animation anim1 = new Animation(6947);
	private static final Graphic graphic1 = new Graphic(1211, GraphicHeight.MIDDLE);
	private static final Graphic graphic2 = new Graphic(390);

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer() && ((Player) victim).getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		int speechChance = Misc.inclusiveRandom(1, 4);
		NPC tsutsuroth = (NPC) entity;
		if (victim.getConstitution() <= 0) {
			return true;
		}
		if (tsutsuroth.isChargingAttack()) {
			return true;
		}
		Player target = (Player) victim;
		CombatType style = Misc.getRandom(8) >= 6
				&& Locations.goodDistance(tsutsuroth.getPosition(), victim.getPosition(), 2) ? CombatType.MELEE
						: CombatType.MAGIC;
		if (style == CombatType.MELEE) {
			tsutsuroth.performAnimation(new Animation(6945));
			tsutsuroth.getCombatBuilder()
					.setContainer(new CombatContainer(tsutsuroth, victim, 1, 1, CombatType.MELEE, true));
			if(speechChance == 1) {
				tsutsuroth.forceChat(getRandomMessage());
			}
			int specialAttack = Misc.getRandom(4);
			if (specialAttack == 2) {
				int amountToDrain = Misc.getRandom(400);
				target.getPacketSender()
						.sendMessage("K'ril Tsutsaroth slams through your defence and steals some Prayer points..");
				if (amountToDrain > target.getSkillManager().getCurrentLevel(Skill.PRAYER)) {
					amountToDrain = target.getSkillManager().getCurrentLevel(Skill.PRAYER);
					tsutsuroth.forceChat("YARRRRRRR!");
				}
				target.getSkillManager().setCurrentLevel(Skill.PRAYER,
						target.getSkillManager().getCurrentLevel(Skill.PRAYER) - amountToDrain);
				if (target.getSkillManager().getCurrentLevel(Skill.PRAYER) <= 0) {
					target.getPacketSender().sendMessage("You have run out of Prayer points!");
					tsutsuroth.forceChat("YARRRRRRR!");
				}
			}
		} else {
			tsutsuroth.performAnimation(anim1);
			tsutsuroth.setChargingAttack(true);
			TaskManager.submit(new Task(2, target, false) {
				int tick = 0;

				@Override
				public void execute() {
					switch (tick) {
					case 0:
						for (Player t : Misc.getCombinedPlayerList(target)) {
							if (t == null || t.getLocation() != Location.GODWARS_DUNGEON || t.isTeleporting())
								continue;
							if (t.getPosition().distanceToPoint(tsutsuroth.getPosition().getX(),
									tsutsuroth.getPosition().getY()) > 20)
								continue;
							new Projectile(tsutsuroth, target, graphic1.getId(), 44, 3, 43, 43, 0).sendProjectile();
						}
						break;
					case 2:
						for (Player t : Misc.getCombinedPlayerList(target)) {
							if (t == null || t.getLocation() != Location.GODWARS_DUNGEON)
								continue;
							target.performGraphic(graphic2);
							tsutsuroth.getCombatBuilder().setVictim(t);
							new CombatHit(tsutsuroth.getCombatBuilder(),
									new CombatContainer(tsutsuroth, t, 1, CombatType.MAGIC, true)).handleAttack();
						}
						tsutsuroth.setChargingAttack(false);
						stop();
						break;
					}
					tick++;
				}
			});
		}
		return true;
	}

	@Override
	public int attackDelay(Character entity) {
		return entity.getAttackSpeed();
	}

	@Override
	public int attackDistance(Character entity) {
		return 3;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MIXED;
	}
}
