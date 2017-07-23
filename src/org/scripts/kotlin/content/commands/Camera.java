package org.scripts.kotlin.content.commands;

import com.zarador.model.StaffRights;
import com.zarador.model.player.command.Command;
import com.zarador.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/28/2016.
 *
 * @author Seba
 */
public class Camera extends Command {

    public Camera(StaffRights staffRights) {
        super(staffRights);
    }

    @Override
    public void execute(Player player, String[] args, StaffRights privilege) {
        if(args.length < 3) {
            player.getPacketSender().sendMessage("You must use the command as ::camera-x-y-speed-angle");
            return;
        }
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int speed = Integer.parseInt(args[2]);
        int angle = Integer.parseInt(args[3]);
        player.getPacketSender().sendMessage("You have moved your camera to (x: "+x+", y: "+y+", speed: "+speed+", angle: "+angle+")");
        player.getPacketSender().sendCameraAngle(x, y, 0, speed, angle);
    }
}
