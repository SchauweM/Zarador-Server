package org.scripts.kotlin.content.commands;

import com.zarador.model.StaffRights;
import com.zarador.model.container.impl.Shop;
import com.zarador.model.player.command.Command;
import com.zarador.world.entity.impl.player.Player;

public class OpenShop extends Command {

    public OpenShop(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if (args == null) {
            player.getPacketSender().sendMessage("Example usage: ::openshop-id");
        } else {
            int id  = Integer.parseInt(args[0]);
            Shop.ShopManager.getShops().get(id).open(player);
        }
    }
}
