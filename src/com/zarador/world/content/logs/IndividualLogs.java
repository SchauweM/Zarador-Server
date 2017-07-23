package com.zarador.world.content.logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.zarador.GameSettings;
import com.zarador.world.entity.impl.player.Player;

/**
 * Writes data to the log files for individual logs
 * stored at ./logs/individual/type/player name.txt
 * @Author Jonny
 */
public class IndividualLogs {

    /**
     * Fetches system time and formats it appropriately
     *
     * @return Formatted time
     */
    private static String getTime() {
        Date getDate = new Date();
        String timeFormat = "M/d/yy hh:mma";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return "[" + sdf.format(getDate) + "]";
    }

    /**
     * Logs data into a certain log
     */
    public static void log(Player player, String type, String[] data) {
        if (!GameSettings.PLAYER_LOGGING) {
            return;
        }
        try {
            File file = new File("./logs/individual/" + player.getUsername() + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(type+" - ");
            bw.write(getTime()+" ");
            for(String logData: data) {
                if(data == null) {
                    continue;
                }
                bw.write(logData+", ");
            }
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}