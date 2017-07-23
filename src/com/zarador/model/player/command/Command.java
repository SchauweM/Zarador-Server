package com.zarador.model.player.command;

import com.zarador.model.StaffRights;
import com.zarador.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/26/2016.
 *
 * @author Seba
 */
public abstract class Command {

    /**
     * Constructs our command
     * @param staffRights
     */
    public Command(StaffRights staffRights) {
        this.staffRights = staffRights;
    }

    /**
     * Executes the command's actions
     * @param player The player executing the command
     * @param args The args that the player passed threw
     * @param privilege The rights of the player.
     */
    public abstract void execute(Player player, String[] args, StaffRights privilege);

    /**
     * Returns the staff rights needed to execute the command.
     * @return
     */
    public StaffRights getStaffRights() {
        return staffRights;
    }

    /**
     * The player rights needed to execute this command
     */
    private final StaffRights staffRights;
}
