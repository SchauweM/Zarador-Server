package com.zarador.world.content.combat.strategy.impl.zulrah;

import com.zarador.engine.task.Task;
import com.zarador.model.CombatIcon;
import com.zarador.model.GameObject;
import com.zarador.model.Hit;
import com.zarador.model.Hitmask;
import com.zarador.model.Position;
import com.zarador.util.Misc;
import com.zarador.model.Locations.Location;
import com.zarador.world.World;
import com.zarador.world.entity.impl.player.Player;

public class ZulrahToxicCloud extends Task {

	final Player player;
	
	public ZulrahToxicCloud(Player player) {
		this.player = player;
	}
	
	@Override
	protected void execute() {
		for(int i = 0; i < Zulrah.TOXIC_CLOUD_AREAS.length; i++) {
			if(Location.inLocation(player, Zulrah.TOXIC_CLOUD_AREAS[i])) {
				if(World.objectExists(new GameObject(Zulrah.TOXIC_CLOUD_ID, new Position(Zulrah.TOXIC_CLOUD_LOCATIONS[i][0],
						Zulrah.TOXIC_CLOUD_LOCATIONS[i][1], player.getPosition().getZ())))) {
					player.dealDamage(new Hit(Misc.getRandom(4)*10, Hitmask.DARK_PURPLE, CombatIcon.NONE));
				}
			}
		}
		
	}

}
