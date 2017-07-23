package com.zarador.model.input.impl;

import com.zarador.model.input.EnterAmount;
import com.zarador.world.entity.impl.player.Player;

public class EnterAmountOfPosOfferPrice extends EnterAmount {

    private int item_amount;
    private int slot;

    public EnterAmountOfPosOfferPrice(int slot) {
        this.slot = slot;
    }

    public EnterAmountOfPosOfferPrice(int slot, int item_amount) {
        this.slot = slot;
        this.item_amount = item_amount;
    }

    @Override
    public void handleAmount(Player player, long price) {
        if (price <= 0 || price >= Long.MAX_VALUE) {
            player.getPacketSender().sendMessage("Please enter a valid price.");
            return;
        }
        if (price > 100000000000L) {
            player.getPacketSender().sendMessage("You can only put a item in your shop under 100B.");
            return;
        }
        player.getPlayerOwnedShop().sellItem(player, getSlot(), getItemAmount(), price);
    }

    public int getItemAmount() {
        return item_amount;
    }

    @Override
    public int getSlot() {
        return slot;
    }

}