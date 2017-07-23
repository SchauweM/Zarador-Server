package com.zarador.world.content;

import com.zarador.model.Animation;
import com.zarador.model.GameObject;
import com.zarador.model.Position;
import com.zarador.util.Misc;
import com.zarador.util.Stopwatch;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

public class EvilTrees {

	private static final int TIME = 4000000;
	public static final int MAX_CUT_AMOUNT = 500;

	private static Stopwatch timer = new Stopwatch().reset();
	public static EvilTree SPAWNED_TREE = null;
	private static LocationData LAST_LOCATION = null;

	public static class EvilTree {

		public EvilTree(GameObject treeObject, LocationData treeLocation) {
			this.treeObject = treeObject;
			this.treeLocation = treeLocation;
		}

		private GameObject treeObject;
		private LocationData treeLocation;

		public GameObject getTreeObject() {
			return treeObject;
		}

		public LocationData getTreeLocation() {
			return treeLocation;
		}
	}

	public enum LocationData {

		LOCATION_1(new Position(3107, 3506), "To the right of the Edgeville bank", "Edgeville"),
		LOCATION_2(new Position(3087, 3541), "at the Wilderness ditch, level 3", "Wilderness"),
		LOCATION_3(new Position(2470, 5166), "@ the Enterence of TzHaar dungeon", "TzHaar"),
		LOCATION_4(new Position(3362, 3285), "behind the Duel Arena", "Duel Arena"),
		LOCATION_5(new Position(2928, 3453), "at the enterence of Taverley", "Taverley"),;

		LocationData(Position spawnPos, String clue, String playerPanelFrame) {
			this.spawnPos = spawnPos;
			this.clue = clue;
			this.playerPanelFrame = playerPanelFrame;
		}

		private Position spawnPos;
		private String clue;
		public String playerPanelFrame;
	}

	public static LocationData getRandom() {
		LocationData tree = LocationData.values()[Misc.getRandom(LocationData.values().length - 1)];
		return tree;
	}

	public static void sequence() {
		if (SPAWNED_TREE == null) {
			if (timer.elapsed(TIME)) {
				LocationData locationData = getRandom();
				if (LAST_LOCATION != null) {
					if (locationData == LAST_LOCATION) {
						locationData = getRandom();
					}
				}
				LAST_LOCATION = locationData;
				SPAWNED_TREE = new EvilTree(new GameObject(11434, locationData.spawnPos), locationData);
				CustomObjects.spawnGlobalObject(SPAWNED_TREE.treeObject);
				World.sendMessage("<icon=0><shad=FF8C38>The evil tree has grown " + locationData.clue + "!");
				World.getPlayers().forEach(p -> p.getPacketSender().sendString(39163,
						"@or2@Evil Tree: @yel@" + EvilTrees.SPAWNED_TREE.getTreeLocation().playerPanelFrame + ""));
				timer.reset();
			}
		} else {
			if (SPAWNED_TREE.treeObject.getCutAmount() >= MAX_CUT_AMOUNT) {
				despawn(false);
				timer.reset();
			}
		}
	}

	public static void despawn(boolean respawn) {
		if (respawn) {
			timer.reset(0);
		} else {
			timer.reset();
		}
		if (SPAWNED_TREE != null) {
			for (Player p : World.getPlayers()) {
				if (p == null) {
					continue;
				}
				if (p.getInteractingObject() != null
						&& p.getInteractingObject().getId() == SPAWNED_TREE.treeObject.getId()) {
					p.performAnimation(new Animation(65535));
					p.getPacketSender().sendClientRightClickRemoval();
					p.getSkillManager().stopSkilling();
					p.getPacketSender().sendMessage("The tree has been wrkt.");
				}
			}
			CustomObjects.deleteGlobalObject(SPAWNED_TREE.treeObject);
			SPAWNED_TREE = null;
		}
	}
}
