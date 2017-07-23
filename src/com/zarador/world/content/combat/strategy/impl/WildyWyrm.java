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

public class WildyWyrm implements CombatStrategy {

	private static final Animation anim1 = new Animation(12791);
	private static final Graphic graphic1 = new Graphic(1211, GraphicHeight.MIDDLE);
	private static final Graphic graphic2 = new Graphic(390);

	@Override
	public boolean canAttack(Character entity, Character victim) {
		return victim.isPlayer();
	}

	@Override
	public CombatContainer attack(Character entity, Character victim) {
		return null;
	}

	@Override
	public boolean customContainerAttack(Character entity, Character victim) {
		NPC wildy_wyrm = (NPC) entity;
		if (victim.getConstitution() <= 0) {
			return true;
		}
		if (wildy_wyrm.isChargingAttack()) {
			return true;
		}
		Player target = (Player) victim;
		CombatType style = Misc.getRandom(8) >= 6
				&& Locations.goodDistance(wildy_wyrm.getPosition(), victim.getPosition(), 2) ? CombatType.MELEE
						: CombatType.MAGIC;
		if (style == CombatType.MELEE) {
			wildy_wyrm.performAnimation(new Animation(12791));
			wildy_wyrm.getCombatBuilder()
					.setContainer(new CombatContainer(wildy_wyrm, victim, 1, 1, CombatType.MELEE, true));
			int specialAttack = Misc.getRandom(4);
			if (specialAttack == 2) {
				int amountToDrain = Misc.getRandom(400);
				target.getPacketSender()
						.sendMessage("K'ril Tsutsaroth slams through your defence and steals some Prayer points..");
				if (amountToDrain > target.getSkillManager().getCurrentLevel(Skill.PRAYER)) {
					amountToDrain = target.getSkillManager().getCurrentLevel(Skill.PRAYER);
				}
				target.getSkillManager().setCurrentLevel(Skill.PRAYER,
						target.getSkillManager().getCurrentLevel(Skill.PRAYER) - amountToDrain);
				if (target.getSkillManager().getCurrentLevel(Skill.PRAYER) <= 0) {
					target.getPacketSender().sendMessage("You have run out of Prayer points!");
				}
			}
		} else {
			wildy_wyrm.performAnimation(anim1);
			wildy_wyrm.setChargingAttack(true);
			TaskManager.submit(new Task(2, target, false) {
				int tick = 0;

				@Override
				public void execute() {
					switch (tick) {
					case 0:
						for (Player t : Misc.getCombinedPlayerList(target)) {
							if (t == null || t.getLocation() != Location.WILDERNESS || t.isTeleporting())
								continue;
							if (t.getPosition().distanceToPoint(wildy_wyrm.getPosition().getX(),
									wildy_wyrm.getPosition().getY()) > 20)
								continue;
							new Projectile(wildy_wyrm, target, 311, 44, 3, 43, 43, 0).sendProjectile();
						}
						break;
					case 2:
						for (Player t : Misc.getCombinedPlayerList(target)) {
							if (t == null || t.getLocation() != Location.WILDERNESS)
								continue;
							target.performGraphic(new Graphic(376));
							wildy_wyrm.getCombatBuilder().setVictim(t);
							new CombatHit(wildy_wyrm.getCombatBuilder(),
									new CombatContainer(wildy_wyrm, t, 1, CombatType.MAGIC, true)).handleAttack();
						}
						wildy_wyrm.setChargingAttack(false);
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
