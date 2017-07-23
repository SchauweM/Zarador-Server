package com.zarador.world.content;

import com.zarador.model.GroundItem;
import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.entity.impl.GroundItemManager;

/**
 * Handles The halloween Event
 * @author Jonny
 */
public class HalloweenEvent {

	public static int lastSpawn = 1600;

	public static Position[] EDGEVILLE_PUMPKIN_LOCATIONS = {
		new Position(3087, 3502, 0),
		new Position(3092, 3504, 0),
		new Position(3088, 3513, 0),
		new Position(3096, 3515, 0),
		new Position(3100, 3499, 0),
		new Position(3087, 3491, 0),
		new Position(3093, 3483, 0),
		new Position(3074, 3504, 0),
		new Position(3079, 3479, 0),
		new Position(3104, 3484, 0),
		new Position(3109, 3507, 0),
	};

	public static Position[] VARROCK_PUMPKIN_LOCATIONS = {
		new Position(3209, 3430, 0),
		new Position(3213, 3424, 0),
		new Position(3226, 3427, 0),
		new Position(3244, 3429, 0),
		new Position(3198, 3429, 0),
		new Position(3186, 3436, 0),
		new Position(3218, 3433, 0),
		new Position(3210, 3408, 0),
		new Position(3199, 3400, 0),
		new Position(3210, 3395, 0),
		new Position(3196, 3447, 0),
	};

	public static Position[] LUMBRIDGE_PUMPKIN_LOCATIONS = {
		new Position(3225, 3219, 0),
		new Position(3236, 3225, 0),
		new Position(3224, 3237, 0),
		new Position(3216, 3254, 0),
		new Position(3222, 3263, 0),
		new Position(3203, 3253, 0),
		new Position(3201, 3238, 0),
		new Position(3236, 3208, 0),
		new Position(3243, 3216, 0),
		new Position(3213, 3205, 0),
		new Position(3213, 3231, 0),
		new Position(3203, 3219, 0),
	};

	public static Position[] SKILLING_ZONE_LOW_PUMPKIN_LOCATIONS = {
		new Position(2802, 2790, 0),
		new Position(2797, 2791, 0),
		new Position(2793, 2784, 0),
		new Position(2796, 2779, 0),
		new Position(2807, 2777, 0),
		new Position(2790, 2777, 0),
		new Position(2784, 2786, 0),
		new Position(2789, 2793, 0),
		new Position(2808, 2793, 0),
	};

	public static Position[] ROCK_CRABS_PUMPKIN_LOCATIONS = {
		new Position(2686, 3723, 0),
		new Position(2682, 3729, 0),
		new Position(2672, 3728, 0),
		new Position(2673, 3714, 0),
		new Position(2663, 3715, 0),
		new Position(2687, 3715, 0),
		new Position(2673, 3705, 0),
		new Position(2661, 3704, 0),
	};

	/**
	 * Spawns random pumpkins all around the world.
	 */
	public static void spawnPumpkins() {
		if(World.getPlayers().size() == 0) {
			return;
		}
		if(lastSpawn == 1800) { //Every 30 minutes
			lastSpawn = 0;
			int random = Misc.inclusiveRandom(0, 4);
			Position[] position = null;
			if(random == 0) {
				position = EDGEVILLE_PUMPKIN_LOCATIONS;
				World.sendMessage("<icon=0><col=EE6800><shad=0>Pumpkins have spawned in Edgeville!");
			} else if(random == 1) {
				position = VARROCK_PUMPKIN_LOCATIONS;
				World.sendMessage("<icon=0><col=EE6800><shad=0>Pumpkins have spawned in Varrock!");
			} else if(random == 2) {
				position = LUMBRIDGE_PUMPKIN_LOCATIONS;
				World.sendMessage("<icon=0><col=EE6800><shad=0>Pumpkins have spawned in Lumbridge!");
			} else if(random == 3) {
				position = SKILLING_ZONE_LOW_PUMPKIN_LOCATIONS;
				World.sendMessage("<icon=0><col=EE6800><shad=0>Pumpkins have spawned at Skilling Zone (low)!");
			} else if(random == 4) {
				position = ROCK_CRABS_PUMPKIN_LOCATIONS;
				World.sendMessage("<icon=0><col=EE6800><shad=0>Pumpkins have spawned at Rock Crabs!");
			}
			if(position == null) {
				return;
			}
			for(int i = 0; i < position.length; i++) {
				GroundItemManager.spawnWorldItem(new GroundItem(new Item(1959, 1), position[i], "world", "0", false, 300, position[i].getZ() >= 0 && position[i].getZ() < 4 ? true : false, 80, Misc.random(0, Integer.MAX_VALUE)));
			}
		}
		lastSpawn++;
	}

}
