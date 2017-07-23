package com.zarador.engine.task.impl;

import com.zarador.model.Locations;
import com.zarador.model.Position;
import com.zarador.world.entity.impl.player.Player;

/**
 * Represents a movement action for a game character.
 * 
 * @author Gabriel Hannason
 */

public class WalkToTask {

	public interface FinalizedMovementTask {
		public void execute();
	}

	/**
	 * The WalkToTask constructor.
	 * 
	 * @param entity
	 *            The associated game character.
	 * @param destination
	 *            The destination the game character will move to.
	 * @param finalizedTask
	 *            The task a player must execute upon reaching said destination.
	 */
	public WalkToTask(Player entity, Position destination, int distance, FinalizedMovementTask finalizedTask) {
		this.player = entity;
		this.destination = destination;
		this.finalizedTask = finalizedTask;
		this.distance = distance;
	}

	private int distance = -1;

	/**
	 * The associated game character.
	 */
	private final Player player;

	/**
	 * The destination the game character will move to.
	 */
	private Position destination;

	/**
	 * The task a player must execute upon reaching said destination.
	 */
	private final FinalizedMovementTask finalizedTask;

	/**
	 * Executes the action if distance is correct
	 */
	public void tick() {
		if (player == null)
			return;
		if (!player.isRegistered()) {
			player.setWalkToTask(null);
			return;
		}
		if (player.getWalkToTask() != null && player.getWalkToTask() == this) {
			if (player.isTeleporting() || player.getConstitution() <= 0 || destination == null) {
				player.setWalkToTask(null);
				return;
			}
			//System.out.println(""+destination.toString() +", "+distance);
			//This tells you if you don't want to use that object on that position
			if((player.getPosition().getX() == 2688 && player.getPosition().getY() == 3707)
				|| (player.getPosition().getX() == 2800 && player.getPosition().getY() == 10134)
					|| (player.getPosition().getX() == 3102 && player.getPosition().getY() == 2958)
					|| (player.getPosition().getX() == 3101 && player.getPosition().getY() == 2958)
					|| (player.getPosition().getX() == 3100 && player.getPosition().getY() == 2958)
					|| (player.getPosition().getX() == 3104 && player.getPosition().getY() == 2958)
					|| (player.getPosition().getX() == 3103 && player.getPosition().getY() == 2958)
					|| (player.getPosition().getX() == 3165 && player.getPosition().getY() == 3685)
					|| (player.getPosition().getX() == 3164 && player.getPosition().getY() == 3685)
					|| (player.getPosition().getX() == 3163 && player.getPosition().getY() == 3685)
					|| (player.getPosition().getX() == 3165 && player.getPosition().getY() == 3696)
					|| (player.getPosition().getX() == 3164 && player.getPosition().getY() == 3696)
					|| (player.getPosition().getX() == 3163 && player.getPosition().getY() == 3696)

					//Steps you don't want to click the object on
					&& (!player.getPosition().equals(3046, 3927))
					&& (!player.getPosition().equals(3047, 3927))
					&& (!player.getPosition().equals(3048, 3927))
					&& (!player.getPosition().equals(3046, 3926))
					&& (!player.getPosition().equals(3046, 3925))
					&& (!player.getPosition().equals(3046, 3924))
					&& (!player.getPosition().equals(3046, 3923))

					) {
				finalizedTask.execute();
				player.setEntityInteraction(null);
				player.setWalkToTask(null);
			} else {
				if (Locations.goodDistance(player.getPosition().getX(), player.getPosition().getY(), destination.getX(),
						destination.getY(), distance) || destination.equals(player.getPosition())) {
					if(
						(player.getPosition().getX() == 3156 && player.getPosition().getY() == 3696) ||
						(player.getPosition().getX() == 3162 && player.getPosition().getY() == 3696) ||
						(player.getPosition().getX() == 3161 && player.getPosition().getY() == 3696) ||
						(player.getPosition().getX() == 3160 && player.getPosition().getY() == 3696) ||
						(player.getPosition().getX() == 3159 && player.getPosition().getY() == 3696) ||
						(player.getPosition().getX() == 3158 && player.getPosition().getY() == 3696) ||
						(player.getPosition().getX() == 3157 && player.getPosition().getY() == 3696) ||
						(player.getPosition().getX() == 3156 && player.getPosition().getY() == 3696) ||
						(player.getPosition().getX() == 3155 && player.getPosition().getY() == 3696)
								//Steps you don't want to click the object on
								|| (player.getPosition().equals(3046, 3927))
								|| (player.getPosition().equals(3047, 3927))
								|| (player.getPosition().equals(3048, 3927))
								|| (player.getPosition().equals(3046, 3926))
								|| (player.getPosition().equals(3046, 3925))
								|| (player.getPosition().equals(3046, 3924))
								|| (player.getPosition().equals(3046, 3923))

							) {

					} else {
						finalizedTask.execute();
						player.setEntityInteraction(null);
						player.setWalkToTask(null);
					}
				}
			}
		}
	}
}
