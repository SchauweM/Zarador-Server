package com.zarador.model.input.impl;
import com.zarador.model.input.EnterAmount;
import com.zarador.world.content.skill.impl.runecrafting.DustOfArmadyl;
import com.zarador.world.entity.impl.player.Player;

public class GrindArmadylRune extends EnterAmount {

    public void handleAmount(Player player, long value) {
        int amount = (int) value;
        if (value > Integer.MAX_VALUE) {
            amount = Integer.MAX_VALUE;
        }
        DustOfArmadyl.grindRunes(player, amount);
    }

}
