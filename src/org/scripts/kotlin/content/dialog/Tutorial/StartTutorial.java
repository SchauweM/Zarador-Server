package org.scripts.kotlin.content.dialog.Tutorial;

import org.scripts.kotlin.content.dialog.BankPin.BankPinTut;

import com.zarador.model.Flag;
import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.options.threeoption.ThreeOption;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.GameMode;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.net.packet.impl.EquipPacketListener;
import com.zarador.world.content.PlayerPanel;
import com.zarador.world.content.clan.ClanChatManager;
import com.zarador.world.entity.impl.player.Player;

public class StartTutorial extends Dialog {

    public Dialog dialog = this;
    public StartTutorial(Player player) {
        super(player);
        setEndState(9);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                ClanChatManager.join(getPlayer(), "argos");
                return Dialog.createNpc(DialogHandler.CALM, "Greetings, Welcome to Zarador!\\nWhat Game Mode would you like?");
            case 1:
                return Dialog.createOption(new ThreeOption(
                        "Play in @red@Knight@bla@ mode (@blu@500XP@bla@ Per Hit)",
                        "Play in @red@Realism@bla@ mode (@blu@10XP@bla@ Per Hit)",
                        "Play in @red@Ironman@bla@ mode (@blu@No trading & 50XP Per Hit@bla@)") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_3:
                                player.getGameModeAssistant().setGameMode(GameMode.KNIGHT);
                                PlayerPanel.refreshPanel(player);
                                //Equipment
                                player.getEquipment().set(Equipment.HEAD_SLOT, new Item(1153, 1));
                                player.getEquipment().set(Equipment.BODY_SLOT, new Item(1115, 1));
                                player.getEquipment().set(Equipment.LEG_SLOT, new Item(1067, 1));
                                player.getEquipment().set(Equipment.FEET_SLOT, new Item(3105, 1));
                                player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(1191, 1));
                                player.getEquipment().set(Equipment.HANDS_SLOT, new Item(11118, 1));
                                player.getEquipment().set(Equipment.RING_SLOT, new Item(2552, 1));
                                player.getEquipment().set(Equipment.CAPE_SLOT, new Item(1052, 1));
                                player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(1323, 1));
                                player.getEquipment().set(Equipment.AMULET_SLOT, new Item(1712, 1));
                                player.getEquipment().refreshItems();
                                player.getUpdateFlag().flag(Flag.APPEARANCE);
                                EquipPacketListener.resetWeapon(player);

                                //Inventory
                                player.getInventory().add(new Item(7947, 50), true);
                                player.getInventory().add(new Item(386, 25), true);
                                player.getInventory().add(new Item(2434, 2), true);
                                player.getInventory().add(new Item(841, 1), true);
                                player.getInventory().add(new Item(843, 1), true);
                                player.getInventory().add(new Item(884, 250), true);
                                player.getInventory().add(new Item(886, 100), true);
                                player.getInventory().add(new Item(1169, 1), true);
                                player.getInventory().add(new Item(1129, 1), true);
                                player.getInventory().add(new Item(1095, 1), true);
                                player.getInventory().add(new Item(1381, 1), true);
                                player.getInventory().add(new Item(577, 1), true);
                                player.getInventory().add(new Item(1011, 1), true);
                                player.getInventory().add(new Item(579, 1), true);
                                player.getInventory().add(new Item(558, 200), true);
                                player.getInventory().add(new Item(554, 200), true);
                                player.getInventory().add(new Item(555, 200), true);
                                player.getInventory().add(new Item(557, 200), true);
                                player.getInventory().add(new Item(562, 75), true);
                                player.getInventory().add(new Item(1333, 1), true);
                                player.getInventory().add(new Item(995, 300000), true);
                                player.getInventory().add(new Item(10586, 1), true);
                                player.getInventory().refreshItems();

                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_3:
                                player.getGameModeAssistant().setGameMode(GameMode.REALISM);
                                PlayerPanel.refreshPanel(player);
                                //Equipment
                                player.getEquipment().set(Equipment.HEAD_SLOT, new Item(1153, 1));
                                player.getEquipment().set(Equipment.BODY_SLOT, new Item(1115, 1));
                                player.getEquipment().set(Equipment.LEG_SLOT, new Item(1067, 1));
                                player.getEquipment().set(Equipment.FEET_SLOT, new Item(3105, 1));
                                player.getEquipment().set(Equipment.SHIELD_SLOT, new Item(1191, 1));
                                player.getEquipment().set(Equipment.HANDS_SLOT, new Item(11118, 1));
                                player.getEquipment().set(Equipment.RING_SLOT, new Item(2552, 1));
                                player.getEquipment().set(Equipment.CAPE_SLOT, new Item(1052, 1));
                                player.getEquipment().set(Equipment.WEAPON_SLOT, new Item(1323, 1));
                                player.getEquipment().set(Equipment.AMULET_SLOT, new Item(1712, 1));
                                player.getEquipment().refreshItems();
                                player.getUpdateFlag().flag(Flag.APPEARANCE);
                                EquipPacketListener.resetWeapon(player);

                                //Inventory
                                player.getInventory().add(new Item(7947, 50), true);
                                player.getInventory().add(new Item(386, 25), true);
                                player.getInventory().add(new Item(2434, 2), true);
                                player.getInventory().add(new Item(841, 1), true);
                                player.getInventory().add(new Item(843, 1), true);
                                player.getInventory().add(new Item(884, 250), true);
                                player.getInventory().add(new Item(886, 100), true);
                                player.getInventory().add(new Item(1169, 1), true);
                                player.getInventory().add(new Item(1129, 1), true);
                                player.getInventory().add(new Item(1095, 1), true);
                                player.getInventory().add(new Item(1381, 1), true);
                                player.getInventory().add(new Item(577, 1), true);
                                player.getInventory().add(new Item(1011, 1), true);
                                player.getInventory().add(new Item(579, 1), true);
                                player.getInventory().add(new Item(558, 200), true);
                                player.getInventory().add(new Item(554, 200), true);
                                player.getInventory().add(new Item(555, 200), true);
                                player.getInventory().add(new Item(557, 200), true);
                                player.getInventory().add(new Item(562, 75), true);
                                player.getInventory().add(new Item(1333, 1), true);
                                player.getInventory().add(new Item(995, 300000), true);
                                player.getInventory().add(new Item(16389, 1), true);
                                player.getInventory().refreshItems();

                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_3_OF_3:
                                player.getGameModeAssistant().setGameMode(GameMode.IRONMAN);
                                PlayerPanel.refreshPanel(player);
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 2:
                return Dialog.createPlayer(DialogHandler.HAPPY, "I would like to play as a "+getPlayer().getGameModeAssistant().getGameMode().getModeName()+".");
            case 3:
                return Dialog.createNpc(DialogHandler.CALM_CONTINUED, "A very interesting choice! Would you like to go through the Argos tutorial to help you learn how to make money and other sorts of stuff?");
            case 4:
                return Dialog.createOption(new TwoOption(
                        "Yes, take the tutorial.",
                        "No, skip the tutorial.") {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch(option) {
                            case OPTION_1_OF_2:
                                setState(5);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_2:
                                player.getPacketSender().sendInterfaceRemoval();
                                player.getDialog().sendDialog(new BankPinTut(player));
                                break;
                        }
                    }
                });
            case 5:
                getPlayer().moveTo(new Position(3182, 3676, 3));
                return Dialog.createNpc(DialogHandler.HAPPY, "We have an active Bounty Hunter with Revenants and Scorpia. There are also some resourace areas for skilling located inside the crator.");
            case 6:
                getPlayer().moveTo(new Position(3094, 3476, 0));
                return Dialog.createNpc(DialogHandler.HAPPY, "We give players every reason imaginable to want to be in the wilderness from skilling to killing wild bosses." +
                        " These coffins will take you close to those areas.");
            case 7:
                getPlayer().moveTo(new Position(2528, 3370, 0));
                return Dialog.createNpc(DialogHandler.HAPPY, "There are areas made just for skilling, this is the training grounds. You can train most skills here or by getting to them from this zone.");
            case 8:
                getPlayer().moveTo(new Position(3093, 3502, 0));
                return Dialog.createNpc(DialogHandler.HAPPY, "This is where the basic shops are located you can explore the rest of Argos by playing!");
            case 9:
                getPlayer().setNewPlayer(false);
                getPlayer().setPlayerLocked(false);
                return Dialog.createNpc(DialogHandler.HAPPY, "Have fun exploring Argos! If you need more help you can find me in Lumbridge.");
//                return Dialog.createOption(new TwoOption(
//                        "Setup Account Pin",
//                        "Setup Bank Pin") {
//                    @Override
//                    public void execute(Player player, OptionType option) {
//                        switch(option) {
//                            case OPTION_1_OF_2:
//                                player.getPacketSender().sendInterfaceRemoval();
//                                player.getDialog().sendDialog(new BankPinTut(player));
//                                break;
//                            case OPTION_2_OF_2:
//                                player.getPacketSender().sendInterfaceRemoval();
//                                player.getDialog().sendDialog(new BankPinTut(player));
//                                break;
//                        }
//                    }
//                });
        }
        return null;
    }
}
