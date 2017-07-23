package org.scripts.kotlin.content.dialog;

import com.zarador.model.DonatorRights;
import com.zarador.model.StaffRights;
import com.zarador.model.options.fiveoption.FiveOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.World;
import com.zarador.world.content.AccountTools;
import com.zarador.world.content.PlayerPanel;
import com.zarador.world.entity.impl.player.Player;

public class GiveDonatorDialog extends Dialog {

    public Dialog dialog = this;
    public String username;

    public GiveDonatorDialog(Player player, String username) {
        super(player);
        setEndState(0);
        this.username = username;
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createOption(new FiveOption(
                        "Premium",
                        "Extreme",
                        "Legendary",
                        "Uber",
                        "Platinum") {
                    DonatorRights rights;
                    Player other;
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_5:
                                if(player.getStaffRights() != StaffRights.OWNER && player.getStaffRights() != StaffRights.MANAGER && player.getStaffRights() != StaffRights.ADMINISTRATOR) {
                                    return;
                                }
                                rights = DonatorRights.PREMIUM;
                                other = World.getPlayerByName(username);
                                if(other == null) {
                                    AccountTools.setDonator(player, username, rights, new Player(null));
                                } else {
                                    other.setDonatorRights(rights);
                                    player.setAmountDonated(player.getAmountDonated() + 10);
                                    PlayerPanel.refreshPanel(other);
                                    other.save();
                                    player.getPacketSender().sendMessage("You have given the rank "+rights.getTitle()+" to "+username+".");
                                    other.getPacketSender().sendMessage("You have been given the rank "+rights.getTitle()+" by "+player.getUsername()+".");
                                }
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                            case OPTION_2_OF_5:
                                if(player.getStaffRights() != StaffRights.OWNER && player.getStaffRights() != StaffRights.MANAGER && player.getStaffRights() != StaffRights.ADMINISTRATOR) {
                                    return;
                                }
                                rights = DonatorRights.EXTREME;
                                other = World.getPlayerByName(username);
                                if(other == null) {
                                    AccountTools.setDonator(player, username, rights, new Player(null));
                                } else {
                                    other.setDonatorRights(rights);
                                    player.setAmountDonated(player.getAmountDonated() + 50);
                                    PlayerPanel.refreshPanel(other);
                                    other.save();
                                    player.getPacketSender().sendMessage("You have given the rank "+rights.getTitle()+" to "+username+".");
                                    other.getPacketSender().sendMessage("You have been given the rank "+rights.getTitle()+" by "+player.getUsername()+".");
                                }
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                            case OPTION_3_OF_5:
                                if(player.getStaffRights() != StaffRights.OWNER && player.getStaffRights() != StaffRights.MANAGER && player.getStaffRights() != StaffRights.ADMINISTRATOR) {
                                    return;
                                }
                                rights = DonatorRights.LEGENDARY;
                                other = World.getPlayerByName(username);
                                if(other == null) {
                                    AccountTools.setDonator(player, username, rights, new Player(null));
                                } else {
                                    other.setDonatorRights(rights);
                                    player.setAmountDonated(player.getAmountDonated() + 150);
                                    PlayerPanel.refreshPanel(other);
                                    other.save();
                                    player.getPacketSender().sendMessage("You have given the rank "+rights.getTitle()+" to "+username+".");
                                    other.getPacketSender().sendMessage("You have been given the rank "+rights.getTitle()+" by "+player.getUsername()+".");
                                }
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                            case OPTION_4_OF_5:
                                if(player.getStaffRights() != StaffRights.OWNER && player.getStaffRights() != StaffRights.MANAGER && player.getStaffRights() != StaffRights.ADMINISTRATOR) {
                                    return;
                                }
                                rights = DonatorRights.UBER;
                                other = World.getPlayerByName(username);
                                if(other == null) {
                                    AccountTools.setDonator(player, username, rights, new Player(null));
                                } else {
                                    other.setDonatorRights(rights);
                                    player.setAmountDonated(player.getAmountDonated() + 500);
                                    PlayerPanel.refreshPanel(other);
                                    other.save();
                                    player.getPacketSender().sendMessage("You have given the rank "+rights.getTitle()+" to "+username+".");
                                    other.getPacketSender().sendMessage("You have been given the rank "+rights.getTitle()+" by "+player.getUsername()+".");
                                }
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                            case OPTION_5_OF_5:
                                if(player.getStaffRights() != StaffRights.OWNER && player.getStaffRights() != StaffRights.MANAGER && player.getStaffRights() != StaffRights.ADMINISTRATOR) {
                                    return;
                                }
                                rights = DonatorRights.PLATINUM;
                                other = World.getPlayerByName(username);
                                if(other == null) {
                                    AccountTools.setDonator(player, username, rights, new Player(null));
                                } else {
                                    other.setDonatorRights(rights);
                                    player.setAmountDonated(player.getAmountDonated() + 1000);
                                    PlayerPanel.refreshPanel(other);
                                    other.save();
                                    player.getPacketSender().sendMessage("You have given the rank "+rights.getTitle()+" to "+username+".");
                                    other.getPacketSender().sendMessage("You have been given the rank "+rights.getTitle()+" by "+player.getUsername()+".");
                                }
                                player.getPacketSender().sendInterfaceRemoval();
                                break;
                        }
                    }
                });
        }
        return null;
    }
}
