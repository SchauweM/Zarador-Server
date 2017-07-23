package com.zarador.world.entity.impl.player.bot.segment;

import java.util.Objects;
import java.util.Optional;

import com.zarador.model.Flag;
import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.Locations.Location;
import com.zarador.model.action.distance.FollowMobileAction;
import com.zarador.world.World;
import com.zarador.world.content.combat.CombatFactory;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.Player;
import com.zarador.world.entity.impl.player.bot.Bot;
import com.zarador.world.entity.impl.player.bot.BotTask;
import com.zarador.world.entity.impl.player.bot.characteristics.BotCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.Spell;


public abstract class  BotSegment {

	protected final Bot bot;

	public BotSegment(Bot bot) {
		this.bot = bot;
//		equip(EquipmentSlots.WEAPON, 1321);
	}

	public abstract boolean canExec();

	public abstract int execute();

	protected int teleport() {
		teleport(3087, 3498);
		return 5;
	}

	public void teleport(int x, int y) {
		TeleportHandler.teleportPlayer(bot, new Position(x, y, 0), bot.getSpellbook().getTeleportType());
	}

	public BotCharacteristics chars() {
		return bot.getCharacteristics();
	}

	public boolean equipmentContains(int id) {
		return bot.getEquipment().contains(id);
	}

	public boolean inventoryContains(int id) {
		for (Item item : bot.getInventory().getItems()) {
			if (item != null) {
				if (item.getId() == id) {
					return true;
				}
			}
		}
		return false;
	}

	public Optional<Item> obtainEquipment(int slot) {
		return Optional.ofNullable(bot.getEquipment().get(slot));
	}

	public boolean equipmentFree(int slot) {
		return !obtainEquipment(slot).isPresent();
	}

	public void equip(int slot, int id) {
		Item contained = bot.getEquipment().get(slot);
		if (contained != null) {
			if (contained.getId() == id) {
				return;
			}
			addInvent(contained);
		}
		bot.getEquipment().getItems()[slot] = new Item(id, 1);
		bot.getUpdateFlag().flag(Flag.APPEARANCE);
	}

	public void addInvent(Item item) {
		bot.getInventory().add(item.getId(), item.getAmount());
	}

	public void addInvent(int id, int amount) {
		bot.getInventory().add(id, amount);
	}

	public void attack() {
		target().ifPresent(t -> {
			attack(t);
		});
	}
	
	public void attackMage(Spell spell) {
		target().ifPresent(t -> {
			attackMage(spell, t);
		});
	}
	
	public void attackMage(Spell spell, Player targ) {
		//TODO: Set spell to #SPELL
		attack(targ);
	}
	
	public void attack(Player attacked) {
		if (this.bot.getUsername().equalsIgnoreCase(BotTask.TESTING)) {
			System.out.println("Attempting to attack: " + attacked.getUsername());
		}
		if (!bot.getDragonSpear().elapsed(3000)) {
			bot.getPacketSender().sendMessage("You can't do that, you're stunned!");
			return;
		}
		if (bot.getCombatBuilder().getStrategy() == null) {
			bot.getCombatBuilder().determineStrategy();
		}
		if (CombatFactory.checkAttackDistance(bot, attacked)) {
			bot.getWalkingQueue().clear();
		}

		bot.getCombatBuilder().attack(attacked);
	}
	
	public void follow() {
		target().ifPresent(this::follow);
	}
	
	public void follow(Player other) {
		bot.getCombatBuilder().reset();
		bot.setEntityInteraction(other);
		bot.getActionQueue().addAction(new FollowMobileAction(bot, other));
	}

	public double getHealthPercentage() {
		return (getCurrentHealth() / getMaximumHealth()) * 100;
	}

	public double getMaximumHealth() {
		return bot.getMaximumHealth();
	}

	public double getCurrentHealth() {
		return bot.getConstitution();
	}

	public boolean inFight() {
		return target().isPresent();
	}

	public Optional<Player> target() {
		return World
				.getPlayers()
				.stream()
				.filter(Objects::nonNull)
				.filter(player -> Math.abs(player.getSkillManager()
						.getCombatLevel()
						- bot.getSkillManager().getCombatLevel()) <= 5)
		//		.filter(player -> player.getCombatBuilder().isAttacking())
				.filter(player -> player.getCombatBuilder().getVictim() == bot)
				.findAny();
	}

}
