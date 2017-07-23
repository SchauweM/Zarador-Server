package com.zarador.world.content.logs;

import com.zarador.world.entity.impl.player.Player;

/**
 * Distributes logs to seperate & individual logs
 * @Author Jonny
 */
public class Logs {

    public static void log(Player player, String type, String[] data) {
        SeperateLogs.log(player, type, data);
        IndividualLogs.log(player, type, data);
    }
}
