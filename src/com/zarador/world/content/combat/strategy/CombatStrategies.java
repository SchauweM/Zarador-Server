package com.zarador.world.content.combat.strategy;

import java.util.HashMap;
import java.util.Map;

import com.zarador.world.content.combat.strategy.impl.*;
import com.zarador.world.content.combat.strategy.impl.kraken.Kraken;
import com.zarador.world.content.combat.strategy.impl.kraken.Tentacles;

public class CombatStrategies {

	private static final DefaultMeleeCombatStrategy defaultMeleeCombatStrategy = new DefaultMeleeCombatStrategy();
	private static final DefaultMagicCombatStrategy defaultMagicCombatStrategy = new DefaultMagicCombatStrategy();
	private static final DefaultRangedCombatStrategy defaultRangedCombatStrategy = new DefaultRangedCombatStrategy();
	private static final Map<Integer, CombatStrategy> STRATEGIES = new HashMap<Integer, CombatStrategy>();

	public static void init() {
		DefaultMagicCombatStrategy defaultMagicStrategy = new DefaultMagicCombatStrategy();
		STRATEGIES.put(6618, new CrazyArcheologist());
		STRATEGIES.put(6619, new ChaosFanatic());
		STRATEGIES.put(502, new Kraken());
		STRATEGIES.put(5535, new Tentacles());
		STRATEGIES.put(912, defaultMagicStrategy);
		STRATEGIES.put(913, defaultMagicStrategy);
		STRATEGIES.put(914, defaultMagicStrategy);
		STRATEGIES.put(13, defaultMagicStrategy);
		STRATEGIES.put(172, defaultMagicStrategy);
		STRATEGIES.put(174, defaultMagicStrategy);
		STRATEGIES.put(2025, defaultMagicStrategy);
		STRATEGIES.put(3495, defaultMagicStrategy);
		STRATEGIES.put(3496, defaultMagicStrategy);
		STRATEGIES.put(3491, defaultMagicStrategy);
		STRATEGIES.put(13451, defaultMagicStrategy);
		STRATEGIES.put(13452, defaultMagicStrategy);
		STRATEGIES.put(13453, defaultMagicStrategy);
		STRATEGIES.put(13454, defaultMagicStrategy);
		STRATEGIES.put(1643, defaultMagicStrategy);
		STRATEGIES.put(6254, defaultMagicStrategy);
		STRATEGIES.put(6257, defaultMagicStrategy);
		STRATEGIES.put(6278, defaultMagicStrategy);
		STRATEGIES.put(6221, defaultMagicStrategy);
		STRATEGIES.put(9992, defaultMagicStrategy);

		DefaultRangedCombatStrategy defaultRangedStrategy = new DefaultRangedCombatStrategy();
		STRATEGIES.put(688, defaultRangedStrategy);
		STRATEGIES.put(2028, defaultRangedStrategy);
		STRATEGIES.put(6220, defaultRangedStrategy);
		STRATEGIES.put(6256, defaultRangedStrategy);
		STRATEGIES.put(6276, defaultRangedStrategy);
		STRATEGIES.put(6252, defaultRangedStrategy);
		STRATEGIES.put(27, defaultRangedStrategy);

		STRATEGIES.put(8776, new DwarvenHandCannoneer());
		STRATEGIES.put(8771, new ChaosDwogre());
		STRATEGIES.put(2745, new Jad());
		STRATEGIES.put(8528, new Nomad());
		STRATEGIES.put(8349, new TormentedDemon());
		STRATEGIES.put(2054, new ChaosElemental());
		STRATEGIES.put(4540, new BandosAvatar());
		STRATEGIES.put(5871, new Bork());
		STRATEGIES.put(8133, new CorporealBeast());
		STRATEGIES.put(5866, new Cerberus());
		STRATEGIES.put(13447, new Nex());
		STRATEGIES.put(6368, new ZamorakianMage());
		STRATEGIES.put(2896, new Spinolyp());
		STRATEGIES.put(2881, new DagannothSupreme());
		STRATEGIES.put(2882, new DagannothPrime());
		STRATEGIES.put(6260, new Graardor());
		STRATEGIES.put(8597, new AvatarOfCreation());
		STRATEGIES.put(9911, new HarLakkRiftsplitter());
		STRATEGIES.put(11751, new Necrolord());
		STRATEGIES.put(6263, new Steelwill());
		STRATEGIES.put(6265, new Grimspike());
		STRATEGIES.put(6222, new KreeArra());
		STRATEGIES.put(6223, new WingmanSkree());
		STRATEGIES.put(6225, new Geerin());
		STRATEGIES.put(6203, new Tsutsuroth());
		STRATEGIES.put(6208, new Kreeyath());
		STRATEGIES.put(6206, new Gritch());
		STRATEGIES.put(6247, new Zilyana());
		STRATEGIES.put(6250, new Growler());
		STRATEGIES.put(1382, new Glacor());
		STRATEGIES.put(9939, new PlaneFreezer());
		STRATEGIES.put(2000, new Venenatis());
		STRATEGIES.put(7286, new Skotizo());
		STRATEGIES.put(5886, new AbyssalSire());
		STRATEGIES.put(3334, new WildyWyrm());
		STRATEGIES.put(2001, new Scorpia());
		STRATEGIES.put(133, defaultMagicStrategy);
		STRATEGIES.put(1857, new AvatarOfMagic());
		STRATEGIES.put(1854, new AvatarOfRange());
		STRATEGIES.put(9766, new Sagittare());
		
		Dragon dragonStrategy = new Dragon();
		STRATEGIES.put(50, dragonStrategy);
		STRATEGIES.put(4000, dragonStrategy);
		STRATEGIES.put(941, dragonStrategy);
		STRATEGIES.put(55, dragonStrategy);
		STRATEGIES.put(53, dragonStrategy);
		STRATEGIES.put(54, dragonStrategy);
		STRATEGIES.put(51, dragonStrategy);
		STRATEGIES.put(10775, dragonStrategy);
		STRATEGIES.put(6593, dragonStrategy);
		STRATEGIES.put(1590, dragonStrategy);
		STRATEGIES.put(1591, dragonStrategy);
		STRATEGIES.put(1592, dragonStrategy);
		STRATEGIES.put(5362, dragonStrategy);
		STRATEGIES.put(5363, dragonStrategy);
		STRATEGIES.put(3068, dragonStrategy);
		STRATEGIES.put(3069, dragonStrategy);
		STRATEGIES.put(6611, new Vetion());

		Aviansie aviansieStrategy = new Aviansie();
		STRATEGIES.put(6246, aviansieStrategy);
		STRATEGIES.put(6230, aviansieStrategy);
		STRATEGIES.put(6231, aviansieStrategy);

		KalphiteQueen kalphiteQueenStrategy = new KalphiteQueen();
		STRATEGIES.put(1158, kalphiteQueenStrategy);
		STRATEGIES.put(1160, kalphiteQueenStrategy);

		Shrek shrekStrat = new Shrek();
		STRATEGIES.put(5872, shrekStrat);

		Revenant revenantStrategy = new Revenant();
		STRATEGIES.put(13465, revenantStrategy);
		STRATEGIES.put(13469, revenantStrategy);
		STRATEGIES.put(13474, revenantStrategy);
		STRATEGIES.put(13478, revenantStrategy);
		STRATEGIES.put(13479, revenantStrategy);
	}

	public static CombatStrategy getStrategy(int npc) {
		if (STRATEGIES.get(npc) != null) {
			return STRATEGIES.get(npc);
		}
		return defaultMeleeCombatStrategy;
	}

	public static CombatStrategy getDefaultMeleeStrategy() {
		return defaultMeleeCombatStrategy;
	}

	public static CombatStrategy getDefaultMagicStrategy() {
		return defaultMagicCombatStrategy;
	}

	public static CombatStrategy getDefaultRangedStrategy() {
		return defaultRangedCombatStrategy;
	}
}
