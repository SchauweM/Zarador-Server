package com.zarador.world.content.diversions.hourly;

import org.scripts.kotlin.content.dialog.npcs.ShootingStarAlien;

import com.zarador.GameSettings;
import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.*;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.util.MathUtil;
import com.zarador.util.Misc;
import com.zarador.util.NameUtils;
import com.zarador.world.World;
import com.zarador.world.content.CustomObjects;
import com.zarador.world.content.diversions.Diversion;
import com.zarador.world.content.skill.impl.mining.MiningData;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles Chaos Shooting Stars
 * @author relex, Jonny
 */

public final class ShootingStar extends GameObject implements HourlyDiversion, Diversion {

	public ShootingStar(int id, int direction, int type, Position position) {
		super(id, direction, type, position);
	}
	
	private Stage stage;

	private Location location = getRandomLocation();
	
	private int minerals;
	
	private int ticks;
	
	public Location getLocation() {
		return location;
	}

	/**
	 * Checks if the shooting star has already crashed.
	 * @return
	 */
	public boolean alreadyCrashed() {
		int ticksLeft = SPAWN_TIMER - ticks;
		return ticksLeft <= 0;
	}

	public String getTimeToCrash() {
		int ticksLeft = SPAWN_TIMER - ticks;
		if (ticksLeft <= 0)
			return "the shooting star has already crashed!";
		
		return "the next shooting star will crash in " + MathUtil.getTimeForTicks(ticksLeft) + ".";
	}
	
	@Override
	public void diversionPulse() {
		if (stage == null) {
			ticks++;
			if (ticks >= SPAWN_TIMER || (!GameSettings.STARTED_SERVER && ticks >= 50)) {
				GameSettings.STARTED_SERVER = true;
				stage = Stage.values()[0];
				setPosition(location.getPosition());
				
				//edgeMeteora = new NPC(4515, new Position(3102, 3492), Direction.NORTH) {

				//};
				
				//varrockMeteora = new NPC(4515, new Position(3220, 3436), Direction.SOUTH) {

				//};
				CustomObjects.spawnGlobalObject(this);
				//World.register(varrockMeteora);
				World.sendMessage("<icon=0><shad=FF8C38>A shooting star has just crashed " + location.getClue() + "!");
			}
		}
	}

	@Override
	public void forcedEnd() {
		stage = null;
		location = getRandomLocation();
		ticks = 0;
		minerals = 0;
		setId(Stage.values()[0].getObjectId());
		//World.deregister(edgeMeteora);
		//World.deregister(varrockMeteora);
		CustomObjects.deleteGlobalObject(this);
		HourlyDiversionManager.chooseNextDiversion();
	}

	@Override
	public void setTimer(int timer) {
		this.ticks = (SPAWN_TIMER - timer);
	}

	@Override
	public boolean hasMenuAction(final Player player, int option) {
		if (stage == null)
			return true;
		if (option == 1) {
			if (player.getDiversion() == this)
				return true;
			if (player.getSkillManager().getCurrentLevel(Skill.MINING) < stage.getLevelRequirement(player)) {
				player.getPacketSender().sendMessage("You need a mining level of " + stage.getLevelRequirement(player) + " to mine this layer of the crashed star!");
				return true;
			}
			final MiningData.Pickaxe pickaxe = MiningData.forPick(MiningData.getPickaxe(player));
			if (pickaxe == null) {
				player.getPacketSender().sendMessage("You do not have a pickaxe which you have the required level to use.");
				return true;
			}			
			if (player.getInventory().isFull()
					&& !player.getInventory().contains(STARDUST)) {
				player.getPacketSender().sendMessage("Not enough space in your inventory.");
				return true;
			}
			if (player.getDiversionTask() != null) {
				player.getDiversionTask().stop();
			}
			player.setDiversion(this);
			player.performAnimation(new Animation(pickaxe.getAnim()));
						
			final Task task = new Task() {
				int stardustPossesed = player.getInventory().getAmount(STARDUST);
				int animationTick = 4;
				int ticksForReward = pickaxe.ordinal() + MathUtil.random(stage.getMiningTimerModifier());
				@Override
				public void execute() {
					if (stage == null || player.getDiversion() != ShootingStar.this) {
						player.performAnimation(new Animation(65535));
						stop();
						return;
					}
					if (player.getSkillManager().getCurrentLevel(Skill.MINING) < stage.getLevelRequirement(player)) {
						player.getPacketSender().sendMessage("You need a mining level of " + stage.getLevelRequirement(player) + " to mine this layer of the crashed star!");
						stop();
						return;
					}
					animationTick--;
					ticksForReward--;
					
					if (animationTick <= 0) {
						player.performAnimation(new Animation(pickaxe.getAnim()));
						animationTick = 4;
					}
					if (ticksForReward <= 0) {
						ticksForReward = pickaxe.ordinal() + MathUtil.random(stage.getMiningTimerModifier());
						if (stardustPossesed + 1 <= STARDUST_LIMIT) {
							stardustPossesed++;
							player.getInventory().add(new Item(STARDUST, 1));
							player.getPacketSender().sendMessage("You manage to collect some minerals.");
							if(player.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 20786 || player.getInventory().contains(20786)) {
								player.getInventory().add(new Item(STARDUST, 1));
								stardustPossesed++;
								minerals++;
							}
						}
						if(player.getLocation() == Locations.Location.WILDERNESS) {
							player.getSkillManager().addSkillExperience(Skill.MINING, ((double) stage.getExperience() * 1.25));
						} else {
							player.getSkillManager().addSkillExperience(Skill.MINING, stage.getExperience());
						}
						minerals++;
						
						if (minerals >= stage.getMinerals()) {
							if (stage.ordinal() + 1 < Stage.values().length) {
								final Stage newStage = Stage.values()[stage.ordinal() + 1];
								CustomObjects.deleteGlobalObject(ShootingStar.this);
								setId(newStage.getObjectId());
								stage = newStage;
								minerals = 0;
								CustomObjects.spawnGlobalObject(ShootingStar.this);
							} else {
								forcedEnd();
								spawnAlien();
							}
						}
					}
				}
			};
			player.setDiversionTask(task);
			TaskManager.submit(task);
		} else if (option == 2) {
			int progress = (minerals  * 100) / stage.getMinerals();
			player.getPacketSender().sendMessage("This star is currently in its " + stage.getName() + " stage. This layer is " + progress + "% destroyed.");
		}
		return true;
	}
		
	private static Location getRandomLocation() {
		final Location[] locations = Location.values();
		return locations[MathUtil.random(locations.length - 1)];
	}

	private void spawnAlien() {
		final Alien alien = new Alien(8091, getPosition(), Direction.NORTH, -1);
		World.register(alien);
		
		TaskManager.submit(new Task(180) {
			int ticks = 0;
			@Override
			public void execute() {		
				ticks++;
				if (ticks == 1) {
					alien.setForcedChat("Good bye and thank you, humans!");

					alien.performAnimation(new Animation(10354));
					
					
					for (int x = -1; x < 2; x++) {
						for (int y = -1; y < 2; y++) {
							World.sendStillGraphic(1621, 0, getPosition().copy().add(x, y));
						}
					}
				} else if (ticks == 2) {
					World.deregister(alien);
					stop();
				}
			}
		});
	}
	
	public static List<Item> getRewards(Player player) {
		int stardust = player.getInventory().getAmount(STARDUST);
		if (stardust <= 0)
			return null;
		
		if (stardust > STARDUST_LIMIT)
			stardust = STARDUST_LIMIT;
		
		final List<Item> rewards = new ArrayList<>();

		rewards.add(new Item(995, 1500000));
		rewards.add(new Item(2358, 30));

		if(Misc.inclusiveRandom(1, 2) == 1) {
			rewards.add(new Item(2364, Misc.inclusiveRandom(5, 10)));
		} else {
			rewards.add(new Item(452, Misc.inclusiveRandom(5, 10)));
		}

		if(Misc.inclusiveRandom(1, 2) == 1) {
			rewards.add(new Item(450, Misc.inclusiveRandom(5, 10)));
		} else {
			rewards.add(new Item(2362, Misc.inclusiveRandom(5, 10)));
		}

		if(Misc.inclusiveRandom(1, 2) == 1) {
			rewards.add(new Item(1632, Misc.inclusiveRandom(5, 10)));
		} else {
			rewards.add(new Item(1618, Misc.inclusiveRandom(5, 10)));
		}

		rewards.add(new Item(9075, Misc.inclusiveRandom(50, 100)));

		return rewards;
	}

	public static ShootingStar getInstance() {
		return INSTANCE;
	}
	
	private NPC edgeMeteora;
	
	private NPC varrockMeteora;
	
	private static final ShootingStar INSTANCE = new ShootingStar(38660, 0, 10, new Position(0, 0));
	
	public static final int STARDUST = 13727;
	
	public static final int STARDUST_LIMIT = 20000;
	
	private static final int SPAWN_TIMER = 2400;
	
	private static final int COSMIC_RUNES = 564;
	
	private static final int ASTRAL_RUNES = 9075;
	
	private static final int GOLD_ORES = 445;
	
	private enum Stage {	
		NINTH(38660, 90, 15, 210, 10),
		
		EIGTH(38661, 80, 25, 145, 9),
		
		SEVENTH(38662, 70, 40, 114, 8),
		
		SIXTH(38663, 60, 80, 71, 7),
		
		FIFTH(38664, 50, 175, 47, 6),
		
		FOURTH(38665, 40, 250, 32, 5),

		THIRD(38666, 30, 439, 29, 4),
		
		SECOND(38667, 20, 700, 25, 3),
		
		FIRST(38668, 10, 1200, 14, 2);
		
		private Stage(int objectId, int levelRequirement, int minerals,
				int experience, int miningTimerModifier) {
			this.objectId = objectId;
			this.levelRequirement = levelRequirement;
			this.minerals = minerals;
			this.experience = experience;
			this.miningTimerModifier = miningTimerModifier;
			this.name = toString().toLowerCase();
		}
		
		private final int objectId;
		
		private final int levelRequirement;
		
		private final int minerals;
		
		private final int experience;
		
		private final int miningTimerModifier;
		
		private final String name;
		
		public int getObjectId() {
			return objectId;
		}
		
		public int getLevelRequirement(Player player) {
			return levelRequirement;
		}
		
		public int getMinerals() {
			return minerals;
		}
		
		public int getExperience() {
			return experience;
		}
		
		public int getMiningTimerModifier() {
			return miningTimerModifier;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public enum Location {
		FALADOR_EAST_BANK(new Position(3030, 3348), "near Falador east bank", "Falador"),
		RIMMINGTON_MINE(new Position(2980, 3236), "in the Rimmington mine", "Rimmington"),
		SHILO_VILLAGE_MINE(new Position(2827, 2995), "near the Shilo Village mine", "Shilo Village"),
		KELDAGRIM_ENTRANCE(new Position(2723, 3702), "near the Keldagrim entrance", "Keldragrim"),
		LUNAR_ISLE_MINE(new Position(2139, 3937), "near the Lunar Isle mine", "Lunar Isle"),
		DONATOR_ZONE_MINE(new Position(2534, 3885), "near the Donator Zone mine", "Donator Zone"),
		NORTH_OF_NEITIZNOT(new Position(2374, 3832), "north of Neitiznot", "Neitiznot"),
		ARDOUGNE_EAST_MINE(new Position(2701, 3330), "in the Ardougne east mine", "Ardougne"),
		YANILLE_BANK(new Position(2603, 3085), "near Yanille bank", "Yanille"),
		AL_KHARID_BANK(new Position(3278, 3174), "near Al Kharid bank", "Al Kharid"),
		AL_KHARID_MINE(new Position(3299, 3288), "in the Al Kharid mining site", "Al Kharid"),
		DUEL_ARENA(new Position(3350, 3272), "at the Duel Arena", "Duel Arena"),
		VARROCK_EAST_BANK(new Position(3258, 3408), "near Varrock-east bank", "Varrock"),
		SOUTH_WEST_VARROCK(new Position(3175, 3376), "near the Varrock-west mine", "Varrock"),
		CANIFIS(new Position(3500, 3483), "north of Canifis pub", "Canifis"),
		GNOME_AGILITY_COURSE(new Position(2480, 3434), "in the Gnome Agility Course", "Gnome Agility Course"),
		EDGEVILLE_BANK(new Position(3094, 3485), "south of Edgville bank", "Edgeville"),
		NORTH_EDGEVILLE_MINE(new Position(3109, 3570), "at Edgeville north mining site", "Edgeville"),
		KBD_ENTRANCE(new Position(3011, 3846), "near the King Black Dragon entrance", "King Black Dragon"),
		PIRATES_HIDEOUT(new Position(3045, 3940), "near the Pirates' hideout", "Pirates' hideout"),
		MAGE_BANK(new Position(3091, 3962), "north of the Mage Bank lever", "Mage Bank"),
		LUMBRIDGE_SWAMP(new Position(3238, 3174), "east of Lumbridge Swamp", "Lumbridge Swamp");

		private Location(Position position, String clue, String name) {
			this.position = position;
			this.name = name;
			this.clue = clue;
		}
		
		private Location(Position position, String clue) {
			this.position = position;
			this.name = NameUtils.capitalizeWords(toString().toLowerCase().replaceAll("_", " "));
			this.clue = clue;
		}
		
		private final Position position;
		
		private final String name;

		private String clue;
		
		public Position getPosition() {
			return position;
		}
		
		public String getName() {
			return name;
		}

		public String getClue() { return clue; }
	}
	
	private static final class Alien extends NPC {
		
		public Alien(int id, Position position, Direction direction,
				int respawnTimer) {
			super(id, position, direction);

		}

		@Override
		public Dialog getDialogue(Player player) {
			return new ShootingStarAlien(player);
		}
	}
}