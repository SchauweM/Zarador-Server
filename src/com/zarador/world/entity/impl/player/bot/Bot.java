package com.zarador.world.entity.impl.player.bot;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import com.zarador.GameSettings;
import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Flag;
import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.Skill;
import com.zarador.net.PlayerSession;
import com.zarador.net.login.LoginDetailsMessage;
import com.zarador.net.login.LoginManager;
import com.zarador.util.NameUtils;
import com.zarador.world.World;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.player.*;
import com.zarador.world.entity.impl.player.bot.characteristics.BotCharacteristics;
import com.zarador.world.entity.impl.player.bot.characteristics.CombatCharacteristics;
import com.zarador.world.entity.impl.player.bot.pker.PkerBotTypes;

/**
 * Created by Ownerr on 09/03/2017.
 */

public class Bot extends Player {

	private final UUID uid = UUID.randomUUID();

	private final long generatedId = ThreadLocalRandom.current().nextLong();

	private Optional<Task> task = Optional.empty();

	private final BotCharacteristics characteristics;

	private final PkerBotTypes type;

	private int sleepTime;

	public boolean forceDharoksHit = false;

	public Bot(String username, String password,
			BotCharacteristics characteristics, PkerBotTypes type) {
		super(new PlayerSession(null));
		session.setPlayer(this);
		setUsername(username).setLongUsername(NameUtils.stringToLong(username))
				.setPassword(password).setHostAddress("Bot" + uid)
				.setComputerAddress("Bot" + uid).setSerialNumber(generatedId)
				.setMacAddress("Bot" + uid);
		this.characteristics = characteristics;
		this.type = type;
		equip();
		addFood();

	}

	private void addFood() {
		this.getInventory().add(new Item(15272, 28));
	}

	public void equip() {
		CombatCharacteristics c = characteristics.getCombat();
		CombatCharacteristics.CombatStyle primary = c.getPrimary();
		Map<Integer, Integer> map = c.of(primary).get();
		for (Map.Entry<Integer, Integer> s : map.entrySet()) {
			getEquipment().getItems()[s.getKey()] = new Item(s.getValue(), 1);
		}
		getEquipment().refreshItems();
		this.getUpdateFlag().flag(Flag.APPEARANCE);
	}

	public void login() {
		try {

			LoginDetailsMessage msg = new LoginDetailsMessage(getUsername(),
					getPassword(), "Bot " + uid, "Bot " + uid, "Bot" + uid,
					generatedId, GameSettings.client_version, (int) generatedId);

			LoginManager.startLogin(session, msg);
			// TODO
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			this.setPlayerLocked(false);
			TeleportHandler.teleportPlayer(this, new Position(3087, 3498, 0),
					getSpellbook().getTeleportType());
			BotTask ts = new BotTask(this);
			setTask(Optional.of(new Task() {

				@Override
				protected void execute() {
					if (sleepTime > 0) {
						sleepTime--;
					} else {
						sleepTime = ts.execute();
					}
				}

			}));
			task.ifPresent(TaskManager::submit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void botLogout() {
		task.ifPresent(Task::stop);
		PlayerHandler.handleLogout(this);
	}

	public double getHealthPercentage() {
		return (getCurrentHealth() / getMaximumHealth()) * 100;
	}

	public double getMaximumHealth() {

		return getSkillManager().getMaxLevel(Skill.CONSTITUTION);
	}

	public double getCurrentHealth() {

		return getConstitution();
		// return playerLevel[Stats.HITPOINTS];
	}

	public boolean inFight() {
		return target().isPresent();
	}

	public Optional<Player> target() {
		return World
				.getPlayers()
				.stream()
				.filter(Objects::nonNull)
				.filter(player -> player.getCombatBuilder().isAttacking())
				.filter(player -> player.getCombatBuilder().getVictim() == this)
				.findAny();

		// return Optional.ofNullable(this.getCombatBuilder().getLastAttacker())
		// .map(c -> (Player) c);
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	public void setTask(Optional<Task> task) {
		this.task = task;
	}

	public BotCharacteristics getCharacteristics() {
		return characteristics;
	}

	public PkerBotTypes getType() {
		return type;
	}
}