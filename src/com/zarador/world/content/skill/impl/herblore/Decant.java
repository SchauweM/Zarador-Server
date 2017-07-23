package com.zarador.world.content.skill.impl.herblore;

import com.zarador.world.entity.impl.player.Player;

/**
 * Created by High105 on 11/12/2016.
 */
public class Decant {
    
    public static void decantPotion(Player player) {
        for(Potion pot : Potion.values()) {
            int full = pot.getFullId();
            int half = pot.getHalfId();
            int quarter = pot.getQuarterId();
            int threeQuarters = pot.getThreeQuartersId();
            int totalNeeded = 0;
            int leftover = 0;
            int emptyVials = 0;
            if(player.getInventory().contains(threeQuarters)) {
                totalNeeded += (3 * player.getInventory().getAmount(threeQuarters));
                emptyVials += player.getInventory().getAmount(threeQuarters);
                player.getInventory().delete(threeQuarters, player.getInventory().getAmount(threeQuarters));
            }
            if(player.getInventory().contains(half)) {
                totalNeeded += (2 * player.getInventory().getAmount(half));
                emptyVials += player.getInventory().getAmount(half);
                player.getInventory().delete(half, player.getInventory().getAmount(half));
            }
            if(player.getInventory().contains(quarter)) {
                totalNeeded += (1 * player.getInventory().getAmount(quarter));
                emptyVials += player.getInventory().getAmount(quarter);
                player.getInventory().delete(quarter, player.getInventory().getAmount(quarter));
            }
            if(totalNeeded > 0) {
                if(emptyVials < 4) {
                    player.getPacketSender().sendMessage("You have nothing to decant.");
                    return;
                }
                if(player.getInventory().contains(quarter)) {
                    player.getInventory().delete(quarter, player.getInventory().getAmount(quarter));
                }
                if(player.getInventory().contains(half)) {
                    player.getInventory().delete(half, player.getInventory().getAmount(half));
                }
                if(player.getInventory().contains(threeQuarters)) {
                    player.getInventory().delete(threeQuarters, player.getInventory().getAmount(threeQuarters));
                }
                if(totalNeeded >= 4)
                    player.getInventory().add(full, totalNeeded / 4);
                else if(totalNeeded == 3)
                    player.getInventory().add(threeQuarters, 1);
                else if(totalNeeded == 2)
                    player.getInventory().add(half, 1);
                else if(totalNeeded == 1)
                    player.getInventory().add(quarter, 1);
                if((totalNeeded % 4) != 0) {
                    emptyVials -= 1;
                    leftover = totalNeeded % 4;
                    if(leftover == 3)
                        player.getInventory().add(threeQuarters, 1);
                    else if(leftover == 2)
                        player.getInventory().add(half, 1);
                    else if(leftover == 1)
                        player.getInventory().add(quarter, 1);
                }
                emptyVials -= (totalNeeded / 4);
                player.getInventory().add(229, emptyVials);
            }
        }
    }
    public static void decantFlask(Player player) {
        for(Potion flask : Potion.values()) {
            int oneDose = flask.getFlaskOne();
            int twoDose = flask.getFlaskTwo();
            int threeDose = flask.getFlaskThree();
            int fourDose = flask.getFlaskFour();
            int fiveDose = flask.getFlaskFive();
            int sixDose = flask.getFlaskSix();
            int totalNeeded = 0;
            int leftover = 0;
            int emptyVials = 0;
            if(player.getInventory().contains(fiveDose)) {
                totalNeeded += (3 * player.getInventory().getAmount(fiveDose));
                emptyVials += player.getInventory().getAmount(fiveDose);
                player.getInventory().delete(fiveDose, player.getInventory().getAmount(fiveDose));
            }
            if(player.getInventory().contains(fourDose)) {
                totalNeeded += (2 * player.getInventory().getAmount(fourDose));
                emptyVials += player.getInventory().getAmount(fourDose);
                player.getInventory().delete(fourDose, player.getInventory().getAmount(fourDose));
            }
            if(player.getInventory().contains(threeDose)) {
                totalNeeded += (1 * player.getInventory().getAmount(threeDose));
                emptyVials += player.getInventory().getAmount(threeDose);
                player.getInventory().delete(threeDose, player.getInventory().getAmount(threeDose));
            }
            if(player.getInventory().contains(twoDose)) {
                totalNeeded += (1 * player.getInventory().getAmount(twoDose));
                emptyVials += player.getInventory().getAmount(twoDose);
                player.getInventory().delete(twoDose, player.getInventory().getAmount(twoDose));
            }
            if(player.getInventory().contains(oneDose)) {
                totalNeeded += (1 * player.getInventory().getAmount(oneDose));
                emptyVials += player.getInventory().getAmount(oneDose);
                player.getInventory().delete(threeDose, player.getInventory().getAmount(oneDose));
            }
            if(totalNeeded > 0) {
                if(emptyVials < 6) {
                    player.getPacketSender().sendMessage("You have nothing to decant.");
                    return;
                }
                if(player.getInventory().contains(oneDose)) {
                    player.getInventory().delete(oneDose, player.getInventory().getAmount(oneDose));
                }
                if(player.getInventory().contains(twoDose)) {
                    player.getInventory().delete(twoDose, player.getInventory().getAmount(twoDose));
                }
                if(player.getInventory().contains(threeDose)) {
                    player.getInventory().delete(threeDose, player.getInventory().getAmount(threeDose));
                }
                if(player.getInventory().contains(fourDose)) {
                    player.getInventory().delete(fourDose, player.getInventory().getAmount(fourDose));
                }
                if(player.getInventory().contains(fiveDose)) {
                    player.getInventory().delete(fiveDose, player.getInventory().getAmount(fiveDose));
                }
                if(totalNeeded >= 6)
                    player.getInventory().add(sixDose, totalNeeded / 6);
                else if(totalNeeded == 5)
                    player.getInventory().add(fiveDose, 1);
                else if(totalNeeded == 4)
                    player.getInventory().add(fourDose, 1);
                else if(totalNeeded == 3)
                    player.getInventory().add(threeDose, 1);
                else if(totalNeeded == 2)
                    player.getInventory().add(twoDose, 1);
                else if(totalNeeded == 1)
                    player.getInventory().add(oneDose, 1);
                if((totalNeeded % 6) != 0) {
//                    emptyVials -= 1;
                    leftover = totalNeeded % 6;
                    if(leftover == 5)
                        player.getInventory().add(fiveDose, 1);
                    else if(leftover == 4)
                        player.getInventory().add(fourDose, 1);
                    else if(leftover == 3)
                        player.getInventory().add(threeDose, 1);
                    else if(leftover == 2)
                        player.getInventory().add(twoDose, 1);
                    else if(leftover == 1)
                        player.getInventory().add(oneDose, 1);
                }
//                emptyVials -= (totalNeeded / 4);
//                player.getInventory().add(229, emptyVials);
            }
        }
    }
}
