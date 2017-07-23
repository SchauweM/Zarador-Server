package com.zarador.model.npc.drops;

import java.util.ArrayList;
import java.util.List;

import com.zarador.model.Item;
import com.zarador.world.World;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/15/2016.
 *
 * @author Seba
 */
public class LootAnnouncement {

    /**
     * Holds our array list of items that can be announced. Grabbed from the old drop system.
     */
    private static final List<Integer> ITEMS;

    /**
     * This is the list of items that will be announced if someone gets one.
     */
    private static final int[] CAN_ANNOUNCE = {13262, 3271, 13271, 20997, 11613, 21472, 21473, 21474, 21475, 21476,
			21369, 6643, 6640, 6642, 6641, 13247,
            21114, 21113, 21112, 21111, 21110, 12703, 1543, 1545, 1546,
            21074, 21077, 21075, 21076, 21078, 21079, 1547, 1548, 6571, 14484, 4224, 11702, 11704, 11706, 12926,
            21107, 11905, 21109, 10887, 11708, 11704, 11724, 11726, 11728, 11718, 11720, 11722, 11730, 11716, 14876,
            11286, 13427, 6731, 6733, 6737, 6735, 4151, 21372, 2513, 15259, 13902, 13890, 13884, 13861, 13858, 13864,
            13905, 13887, 13893, 13899, 13873, 13879, 13876, 13870, 6571, 18349, 18351, 18353, 18355, 18357,
            13750, 13748, 13746, 13752, 11335, 15486, 13870, 13873, 13876, 13884, 13890, 13896,
            13902, 13858, 13861, 13864, 13867, 11995, 11996, 11997, 11978, 12001, 12002, 12003, 12004, 12005, 12006,
            11990, 11991, 11992, 11993, 11994, 11989, 11988, 11987, 11986, 11985, 11984, 11983, 11982, 11981, 11979,
            13659, 11235, 13754, 20000, 20001, 20002, 15103, 15104, 15105, 15106, 21000, 21001, 21002, 21003, 21004,
            21005, 21006, 21007, 1053, 1830, 1055, 1057, 1831, 1832, 1834, 8000, 21008, 21009, 21010, 21011, 21012,
            21013, 21014, 21015, 21016, 21017, 6564, 6576, 21018, 21019, 21020, 21021, 21022, 21023, 21024, 21025,
            21026, 21027, 21028, 21029, 15017, 21030, 21031, 21032, 21033, 21034, 21035, 21036, 21037, 21038, 21039,
            21040, 21041, 21042, 10330, 10332, 10334, 10336, 10338, 10340, 10342, 10344, 10346, 10348, 10350, 10352,
            12006, 21038, 13000, 13001, 13002, 13003, 13004, 13005, 13006, 13007, 13008, 20171, 21089, 21090, 21103,
            21091, 21092, 21100, 21101, 15259, 15241, 13576, 21104, 19672, 19673, 19674, 21111, 21112, 21113, 11694,
            14004, 14005, 14006, 14007, 21136, 21144, 21148, 19672, 19673, 19674, 21140, 20135, 20139, 20143, 20147, 20151, 20155,
            20159, 20163, 20167, 19780, 15126, 2572,
            12954, 12816, 11943, 13178, 13247, 12921, 12940, 12939, 12643, 12644, 12645, 12649, 12650, 12651, 12652, 12653, 11995, 12654, 12655, 13181, 13178, 13179, 13177, 13225, 12648, 13322, 13320, 13321, 13247
    };

    /**
     * Puts our list of items into our array list for future reading.
     */
    static {
        ITEMS = new ArrayList<>();
        for (int i : CAN_ANNOUNCE) {
            ITEMS.add(i);
        }
    }

    /**
     * Checks to see if the item should get an announcement then processes if it can.
     * @param playerName
     * @param item
     * @param npcName
     */
    public void sendAnnouncment(String playerName, Item item, String npcName) {
        if (!ITEMS.contains(item.getId())) {
            return;
        }
        World.sendMessage("<icon=1><shad=FF8C38>[News] " + playerName + " has received a " + item.getDefinition().getName() + " from a " + npcName);
    }

    /**
     * This is used for {@link com.zarador.world.content.DropLog}
     * @param item
     * @return
     */
    public boolean isAnnouncable(int item) {
        return ITEMS.contains(item);
    }

}
