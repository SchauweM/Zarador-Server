package com.zarador.world.content;

import com.zarador.world.entity.impl.player.Player;

/**
 * Created by tanner on 8/31/2016.
 */
public class QuickChat {

    public static String[] quickChat = new String[] { "Attack", "Defence", "Strength", "Constitution",
    "Range", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
    "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting",
    "Construction", "Hunter", "Summoning", "Dungeoneering"};
    public static void handleButtons(Player player, int id) {
        if (!player.getQuickChat().elapsed(2500)) {
                if (!player.hasQC) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    player.hasQC = true;
                }
            return;
        }
        if (PlayerPunishment.isMuted(player.getUsername()) || PlayerPunishment.isIpMuted(player.getHostAddress())) {
            player.getPacketSender().sendMessage("You are muted and cannot chat.");
            return;
        }
        player.hasQC = false;
        if (id == 5 || id == 3) {
            player.forceChat("[Argos] My " + quickChat[id] + " level is "
                    + player.getSkillManager().getMaxLevel(id)/10 + ".");
            player.getQuickChat().reset();
            return;
        }
        player.forceChat("[Argos] My " + quickChat[id] + " level is "
                + player.getSkillManager().getMaxLevel(id) + ".");
        player.getQuickChat().reset();
    }
}
