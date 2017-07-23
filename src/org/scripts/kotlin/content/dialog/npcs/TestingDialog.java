package org.scripts.kotlin.content.dialog.npcs;

import com.zarador.model.options.twooption.TwoOption;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.model.player.dialog.DialogMessage;
import com.zarador.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/28/2016.
 *
 * @author Seba
 */
public class TestingDialog extends Dialog {

    private Dialog dialog = this;

    public TestingDialog(Player player) {
        super(player);
        setEndState(2);
    }

    @Override
    public DialogMessage getMessage() {
        switch (getState()) {
            case 0:
                return createNpc("Hello folks, how are you doing today?  Keep in mind we have a text wrapper so this will be converted into the lines needed.");
            case 1:
                return createOption(new TwoOption(
                        "Go to the next dialog",
                        "Go back to the beginning."
                ) {
                    @Override
                    public void execute(Player player, OptionType option) {
                        switch (option) {
                            case OPTION_1_OF_2:
                                setState(2);
                                player.getDialog().sendDialog(dialog);
                                break;
                            case OPTION_2_OF_2:
                                setState(0);
                                player.getDialog().sendDialog(dialog);
                                break;
                        }
                    }
                });
            case 2:
                return createNpc("This is the end dialog of this chat.");
        }

        return null;
    }
}
