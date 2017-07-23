package org.scripts.kotlin.content.dialog.Effigys;

import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogHandler;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.util.Misc;
import com.zarador.world.content.Effigies;
import com.zarador.world.entity.impl.player.Player;

public class CleanEffigy extends Dialog {

    public Dialog dialog = this;
    int x = 0;
    public CleanEffigy(Player player, int x) {
        super(player);
        this.x = x;
        setEndState(2);
    }

    final String name = Misc.formatText(ItemDefinition.forId(x).getName());
    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return Dialog.createStatement(DialogHandler.CALM, "You clean off the dust off of the Ancient effigy.",
                        "The relic begins to make some sort of weird noises.",
                        "I think there may be something inside here.");
            case 1:
                switch (x) {
                    case 18778:
                        return Dialog.createStatement(DialogHandler.CALM, "This will require at least a level of 91 in one of the two",
                                "skills to investigate. After investigation it becomes nourished,",
                                "rewarding 15,000 experience in the skill used.");
                    case 18779:
                        return Dialog.createStatement(DialogHandler.CALM, "This will require at least a level of 93 in one of the two",
                                "skills to investigate. After investigation it becomes sated,",
                                "rewarding 30,000 experience in the skill used.");
                    case 18780:
                        return Dialog.createStatement(DialogHandler.CALM, "This will require at least a level of 95 in one of the two",
                                "skills to investigate. After investigation it becomes gordged,",
                                "rewarding 45,000 experience in the skill used.");
                    case 18781:
                        return Dialog.createStatement(DialogHandler.CALM, "This will require at least a level of 97 in one of the two",
                                "skills to investigate. After investigation it provides 60,000 ",
                                "experience in the skill used and, then crumbles to dust,",
                                "leaving behind a dragonkin lamp.");
                }
            case 2:
                int r = 1 + Misc.getRandom(6);
                boolean newEffigy = getPlayer().getEffigy() == 0;
                if (!newEffigy) {
                    r = getPlayer().getEffigy();
                } else {
                    getPlayer().setEffigy(r);
                }
                switch (r) {
                    case 1:
                        return Dialog.createOption(new TwoOption(
                                "Crafting",
                                "Agility") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_2:
                                        Effigies.openEffigy(player, 12);
                                        break;
                                    case OPTION_2_OF_2:
                                        Effigies.openEffigy(player, 16);
                                        break;
                                }
                            }
                        });
                    case 2:
                        return Dialog.createOption(new TwoOption(
                                "Runecrafting",
                                "Thieving") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_2:
                                        Effigies.openEffigy(player, 20);
                                        break;
                                    case OPTION_2_OF_2:
                                        Effigies.openEffigy(player, 17);
                                        break;
                                }
                            }
                        });
                    case 3:
                        return Dialog.createOption(new TwoOption(
                                "Cooking",
                                "Firemaking") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_2:
                                        Effigies.openEffigy(player, 7);
                                        break;
                                    case OPTION_2_OF_2:
                                        Effigies.openEffigy(player, 11);
                                        break;
                                }
                            }
                        });
                    case 4:
                        return Dialog.createOption(new TwoOption(
                                "Farming",
                                "Fishing") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_2:
                                        Effigies.openEffigy(player, 19);
                                        break;
                                    case OPTION_2_OF_2:
                                        Effigies.openEffigy(player, 10);
                                        break;
                                }
                            }
                        });
                    case 5:
                        return Dialog.createOption(new TwoOption(
                                "Fletching",
                                "Woodcutting") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_2:
                                        Effigies.openEffigy(player, 9);
                                        break;
                                    case OPTION_2_OF_2:
                                        Effigies.openEffigy(player, 8);
                                        break;
                                }
                            }
                        });
                    case 6:
                        return Dialog.createOption(new TwoOption(
                                "Herblore",
                                "Prayer") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_2:
                                        Effigies.openEffigy(player, 15);
                                        break;
                                    case OPTION_2_OF_2:
                                        Effigies.openEffigy(player, 5);
                                        break;
                                }
                            }
                        });
                    case 7:
                        return Dialog.createOption(new TwoOption(
                                "Smithing",
                                "Mining") {
                            @Override
                            public void execute(Player player, OptionType option) {
                                switch(option) {
                                    case OPTION_1_OF_2:
                                        Effigies.openEffigy(player, 13);
                                        break;
                                    case OPTION_2_OF_2:
                                        Effigies.openEffigy(player, 14);
                                        break;
                                }
                            }
                        });
                }

        }
        return null;
    }
}
