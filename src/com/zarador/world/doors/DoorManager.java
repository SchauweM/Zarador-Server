package com.zarador.world.doors;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.zarador.model.GameObject;
import com.zarador.model.HashedPosition;
import com.zarador.model.Position;
import com.zarador.world.content.CustomObjects;

/**
 *
 * @author relex lawl
 */
public final class DoorManager {
		
	public static void init() throws IOException {
		GLOBAL_DOORS.clear();

		int amount = 0;
		final long startup = System.currentTimeMillis();
		
		final FileReader reader = new FileReader("./data/def/doors/doors.json");
		final Door[] doors = new GsonBuilder().setPrettyPrinting().create().fromJson(reader, Door[].class);
		
		System.out.println("Loading global door configurations...");
		
		for (Door door : doors) {
			GLOBAL_DOORS.put(door.getOpenState().getPosition(), door);
			GLOBAL_DOORS.put(door.getClosedState().getPosition(), door);
			amount++;
		}
		
		reader.close();
		System.out.println("Loaded " + amount + " door configurations in " + (System.currentTimeMillis() - startup) + "ms");

		isDoor(new GameObject(1516, new Position(2445, 3089))); //Castle wars door
		isDoor(new GameObject(1519, new Position(2445, 3090))); //Castle wars door
		isDoor(new GameObject(11622, new Position(3078, 3435))); //Barbarian village door
		isDoor(new GameObject(11621, new Position(3079, 3435))); //Barbarian village door
		isDoor(new GameObject(1516, new Position(2508, 3859))); //Donator zone door
		isDoor(new GameObject(1519, new Position(2508, 3860))); //Donator zone door
	}
	
	public static boolean isDoor(GameObject gameObject) {
		Door door = GLOBAL_DOORS.get(new HashedPosition(gameObject.getPosition()));
		
		if (door != null) {
			GameObject newDoor = null;
			boolean removeClipping = false;
			
			if (gameObject.getId() == door.getOpenState().getId()) {
				//the door is opened and we are going to close it
				newDoor = new GameObject(door.getClosedState().getId(), door.getClosedState().getDirection(),
						gameObject.getType(), door.getClosedState().getPosition());
				for (GameObject object : CustomObjects.REMOVE_OBJECTS) {
					if (newDoor.getId() == object.getId() && newDoor.getPosition().getX() == object.getPosition().getX() && newDoor.getPosition().getY() == object.getPosition().getY()) {
						CustomObjects.REMOVE_OBJECTS.remove(object);
					}
				}
			} else if (gameObject.getId() == door.getClosedState().getId()) {
				//the door is closed and we are going to open it
				newDoor = new GameObject(door.getOpenState().getId(), door.getOpenState().getDirection(),
						gameObject.getType(), door.getOpenState().getPosition());
				removeClipping = true;
				CustomObjects.REMOVE_OBJECTS.add(gameObject);
			}
			
			if (newDoor != null) {
				if (removeClipping || newDoor.getId() == 1517 || newDoor.getId() == 1520) {
					//these doors aren't meant to be kept open, so they are clipped
					//even while open, so we're manually removing the clipping
					//RegionClipping.removeClipping(newDoor);
					//RegionClipping.removeClipping(gameObject);
				}
				gameObject.setType(0);
				newDoor.setType(0);
				CustomObjects.deleteGlobalObject(gameObject);
				CustomObjects.spawnGlobalObject(newDoor);
			}
			
			return true;
		}
		
		return false;
	}
	
	public static final Map<HashedPosition, Door> GLOBAL_DOORS = new HashMap<>();
}