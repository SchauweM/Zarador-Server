package com.zarador.world.entity.impl.player.bot.segment;

import java.util.Map;
import java.util.Map.Entry;

import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.GraphicHeight;
import com.zarador.model.Skill;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.content.Consumables;
import com.zarador.world.content.combat.CombatFactory;
import com.zarador.world.content.combat.weapon.CombatSpecial;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.bot.Bot;
import com.zarador.world.entity.impl.player.bot.BotUtil;
import com.zarador.world.entity.impl.player.bot.characteristics.CombatCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.FoodCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.SpecialWeaponCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.Spell;
import com.zarador.world.entity.impl.player.bot.characteristics.VengeanceCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.CombatCharacteristics.CombatStyle;
import com.zarador.world.entity.impl.player.bot.characteristics.VengeanceCharacteristics.VengeanceTendency;
import com.zarador.world.entity.impl.player.bot.util.EquipmentSlots;

import java.util.Set;

public class DuringFightBotSegment extends BotSegment {

	private final SpecialWeaponCharacteristics s = chars().getSpecialWeapon();

	private int specDelay;

	private CombatStyle style;

	public DuringFightBotSegment(Bot bot) {
		super(bot);

		addInvent(9075, Integer.MAX_VALUE);
		addInvent(557, Integer.MAX_VALUE);
		addInvent(560, Integer.MAX_VALUE);

		addInvent(555, Integer.MAX_VALUE);
		addInvent(565, Integer.MAX_VALUE);
		addInvent(560, Integer.MAX_VALUE);

		style = chars().getCombat().getPrimary();
	}

	@Override
	public boolean canExec() {
		return inFight();
	}

	@Override
	public int execute() {
		if (specDelay > 0) {
			specDelay--;
		}

		Player other = target().get();

		if (!bot.getPosition().isWithinDistance(other.getPosition(), 3)) {
			follow();
		} else { //Idk
			if (style == CombatStyle.MAGIC) {
				attackMage(Spell.ICE_BARRAGE);
			} else {
				attack();
			}
		}

		restoration();

		venge();

		if (s.getWeaponId().isPresent()
				&& getOpponentHealthPercentage() <= s.getOpponentPercentage()
						.getAsInt() && hasEnoughSpecial()) {
			return handleSpecial();
		} else {
			handleEquip();
		}
		return 0;
	}

	private void handleEquip() {
		CombatCharacteristics c = chars().getCombat();
		if (bot.forceDharoksHit) {
			equip(EquipmentSlots.WEAPON, 4718); // Greataxe
			bot.forceDharoksHit = false;
		}
		if (CombatFactory.fullDharoks(bot) && getHealthPercentage() > 60) {
			equip(EquipmentSlots.WEAPON, c.getPrimaryWeapon());
		} else {
			if (getHealthPercentage() <= 60) {
				if (CombatFactory.fullDharoks(bot)) {
					equip(EquipmentSlots.WEAPON, 4718); // Greataxe
				}
			}
		}

		if (!c.getChangeTendency().isPresent() || c.getSize() == 1) {
			equip(EquipmentSlots.WEAPON, c.getPrimaryWeapon());
		} else { // We have multiple combat styles
			int random = Misc.random(1000);
			boolean change = random <= c.getChangeTendency().getAsInt();
			if (change) {
				Entry<CombatStyle, Map<Integer, Integer>> entry = c
						.getRandomEntry();
				this.style = entry.getKey();
				for (Entry<Integer, Integer> s : entry.getValue().entrySet()) {
					equip(s.getKey(), s.getValue());
				}
			}
		}
	}

	private void restoration() {
		try {
			FoodCharacteristics food = chars().getFood();
			if (food.getId().isPresent()) {
				if (getHealthPercentage() <= food.getEatPercentage().getAsInt()) {
					int id = food.getId().getAsInt();
					if (!bot.getInventory().contains(id)) {
						return;
					}
					int slot = BotUtil.findFirstSlot(bot, id);
					if (slot == -1) {
						return;
					}
					Consumables.isFood(bot, id, slot);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void venge() {
		try {
			VengeanceCharacteristics veng = chars().getVengeance();
			if (veng.isUsesVengeance() && !bot.hasVengeance()) {
				Set<VengeanceTendency> tendency = veng.getTendencySet();

				if (tendency.contains(VengeanceTendency.THIRTY_SECOND)) {
					castVeng();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void castVeng() {
		bot.performAnimation(new Animation(4410));
		bot.performGraphic(new Graphic(726, GraphicHeight.HIGH));
		bot.getLastVengeance().reset();
		bot.setHasVengeance(true);
	}

	private int handleSpecial() {
		int wantedWeapon = s.getWeaponId().getAsInt();
		int randomExtraDamage = Misc.random(3);
		if (randomExtraDamage == 1) {
			// bot.bonusSpecialDamage = true;
		}
		if (!equipmentContains(wantedWeapon)) {
			Map<Integer, Integer> entry = chars().getCombat()
					.of(chars().getCombat().getPrimary()).get();
			for (Entry<Integer, Integer> s : entry.entrySet()) {
				if (s.getKey() != EquipmentSlots.WEAPON)
					equip(s.getKey(), s.getValue());
			}
			equip(EquipmentSlots.WEAPON, wantedWeapon);
		} else {
			// bot.getSpecial().activateSpecial(wantedWeapon,
			// target().get().playerId);
			if (!s.getDelayBetween().isPresent()) {
				bot.setSpecialActivated(true);
				/**
				 * unsure how to get this to work.
				 */
				// bot.forcedChat("about to spec!");
				// System.out.println(""+bot.formattedName+" about to use special");
				/*
				 * if (bot.wearingDharoksArmour()){ bot.forceDharoksHit = true;
				 * bot.forcedChat("DHaxe Next!"); handleEquip();
				 * System.out.println(""+bot.formattedName+
				 * " wearing DH special enabled, force hit next"); return 10; }
				 */
				return 0;
			} else {
				int delayBetween = specDelay;// s.getDelayBetween().getAsInt();
				if (delayBetween > 0) {
					// bot.bonusSpecialDamage = false;
					handleEquip();
					return 0;
				} else {
					specDelay = s.getDelayBetween().getAsInt();
				}
			}
			return 0;
		}
		return 0;
	}

	public boolean hasEnoughSpecial() {
		if (!s.getWeaponId().isPresent()) {
			return false;
		}
		// ItemDefinition.forId(s.getWeaponId().getAsInt()).sp
		double requiredSpec = CombatSpecial.getSpecialAttack(s.getWeaponId()
				.getAsInt());
		boolean hasEnough = bot.getSpecialPercentage() >= requiredSpec;
		return hasEnough;
	}

	public double getOpponentHealthPercentage() {
		return (getOppCurrentHealth() / getOppMaximumHealth()) * 100;
	}

	public double getOppMaximumHealth() {
		return target().get().getSkillManager().getMaxLevel(Skill.CONSTITUTION);
	}

	public double getOppCurrentHealth() {
		return target().get().getConstitution();
	}
}
