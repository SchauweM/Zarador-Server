package com.zarador.world.entity.impl.player.bot;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.ThreadLocalRandom;

import com.zarador.model.Item;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.bot.characteristics.AggressivenessCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.BotCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.CombatCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.FoodCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.SpecialWeaponCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.VengeanceCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.CombatCharacteristics.CombatStyle;
import com.zarador.world.entity.impl.player.bot.characteristics.VengeanceCharacteristics.VengeanceTendency;
import com.zarador.world.entity.impl.player.bot.pker.PkerBotSets;
import com.zarador.world.entity.impl.player.bot.pker.PkerBotTypes;
import com.zarador.world.entity.impl.player.bot.util.EquipmentSlots;
import com.zarador.world.entity.impl.player.bot.util.NameGenerator;

public class BotBuilder {
	public int specialWeaponID;

	private int foodHeal = 57;

	public Bot buildRandom() {
		try {
			String name = NameGenerator.generateRandomName();

			boolean hasSpecWeapon = Misc.random(10) != 1; // 9 in 10 chance of
															// having a spec
															// weapon
			int specWep = from(5698, 1215, 11694, 4587, 1305);
			int specPercentage = Misc.random(30, 100);
			OptionalInt specDelay = Misc.random(3) == 1 ? OptionalInt.empty()
					: OptionalInt.of(Misc.random(2, 30));
			SpecialWeaponCharacteristics spec = new SpecialWeaponCharacteristics(
					specWep, specPercentage, specDelay);
			specialWeaponID = specWep;
			if (!hasSpecWeapon) {
				spec = new SpecialWeaponCharacteristics();
			}
			VengeanceCharacteristics veng = new VengeanceCharacteristics(
					EnumSet.allOf(VengeanceTendency.class));
			AggressivenessCharacteristics aggro = new AggressivenessCharacteristics(
					false, Misc.random(5, 15));

			PkerBotTypes type = PkerBotTypes.getRandom();
			PkerBotSets pkerSet = PkerBotSets.getRandomSet(type);
			// PkerBotTypes type = PkerBotTypes.MASTER;
			// PkerBotSets pkerSet = PkerBotSets.DHAROCKS_PKER;
			Map<CombatStyle, Map<Integer, Integer>> weapons = new HashMap<>();
			Map<Integer, Integer> rangeGear = new HashMap<>();
			rangeGear.put(EquipmentSlots.WEAPON, 9185); // Bronze crossbow
			rangeGear.put(EquipmentSlots.CHEST, 6322); // Snakeskin body

			Map<Integer, Integer> meleeGear = new HashMap<>();
			meleeGear.put(EquipmentSlots.HEAD,
					PkerBotSets.getRandomItem(pkerSet.getHead()));
			meleeGear.put(EquipmentSlots.WEAPON, 4151); // Abby whip
			meleeGear.put(EquipmentSlots.CHEST,
					PkerBotSets.getRandomItem(pkerSet.getBody()));
			meleeGear.put(EquipmentSlots.LEGS,
					PkerBotSets.getRandomItem(pkerSet.getLegs()));
			meleeGear.put(EquipmentSlots.NECKLACE,
					PkerBotSets.getRandomItem(pkerSet.getNecklace()));
			meleeGear.put(EquipmentSlots.FEET,
					PkerBotSets.getRandomItem(pkerSet.getFeet()));
			meleeGear.put(EquipmentSlots.HANDS,
					PkerBotSets.getRandomItem(pkerSet.getHands()));

			if (ItemDefinition.forId(meleeGear.get(EquipmentSlots.WEAPON))
					.isTwoHanded()) {
				meleeGear.put(EquipmentSlots.SHIELD,
						PkerBotSets.getRandomItem(pkerSet.getShield()));

			}
			Map<Integer, Integer> mageGear = new HashMap<>();
			mageGear.put(EquipmentSlots.HEAD, 16755); // Wizard hat
			mageGear.put(EquipmentSlots.FEET, 16931); // Wizard boots
			mageGear.put(EquipmentSlots.WEAPON, 17017); // Celestial Staff
			mageGear.put(EquipmentSlots.CHEST, 17237); // Celestial Staff
			mageGear.put(EquipmentSlots.LEGS, 16865); // Celestial Staff
			weapons.put(CombatStyle.MELEE, meleeGear);
			weapons.put(CombatStyle.RANGE, rangeGear);
			weapons.put(CombatStyle.MAGIC, mageGear);

			Map<Integer, Integer> map = new HashMap<>();
			map.put(EquipmentSlots.WEAPON,
					PkerBotSets.getRandomItem(pkerSet.getWeapon()));
			CombatCharacteristics weapon;// = new
											// CombatCharacteristics(CombatStyle.MELEE,
											// map);

			CombatStyle primary = CombatStyle.MELEE;
			weapon = new CombatCharacteristics(weapons, primary, 1,
					Optional.empty());
			OptionalInt backPercentage = Misc.random(5) != 4 ? OptionalInt
					.empty() : OptionalInt.of(Misc.random(50));

			int foodId = 15272;
			FoodCharacteristics food = new FoodCharacteristics(foodId, foodHeal);

			BotCharacteristics chars = new BotCharacteristics(false, 1,
					backPercentage, spec, weapon, veng, aggro, food);
			Bot bot = new Bot(name, "none", chars, type);

			bot.getEquipment().getItems()[Equipment.AMMUNITION_SLOT] = new Item(
					244, 1000);

			bot.getInventory().add(new Item(foodId, 10));
			if (pkerSet == PkerBotSets.DHAROCKS_PKER) {
				bot.getInventory().add(new Item(4718, 1));
			}
			if (primary == CombatStyle.MAGIC) {
				bot.getInventory().add(560, 1000);
				bot.getInventory().add(555, 4000);
				bot.getInventory().add(565, 1000);
			}

			if (type != PkerBotTypes.PURE)
				BotUtil.getMaxed(bot);
			else
				BotUtil.getPured(bot);

			return bot;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private int from(int... ids) {
		int random = ThreadLocalRandom.current().nextInt(ids.length);
		return ids[random];
	}

}
