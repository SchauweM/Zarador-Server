package org.scripts.kotlin.content.commands;

import com.zarador.model.StaffRights;
import com.zarador.model.player.command.Command;
import com.zarador.world.content.combat.strategy.impl.kraken.KrakenInstance;
import com.zarador.world.content.combat.strategy.impl.zulrah.Zulrah;
import com.zarador.world.entity.impl.player.Player;

public class ZulrahTeleport extends Command {

    public ZulrahTeleport(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
    	Zulrah.startZulrah(player);
    }
}
