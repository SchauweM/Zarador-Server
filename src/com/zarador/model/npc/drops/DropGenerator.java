package com.zarador.model.npc.drops;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.zarador.model.Item;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.definitions.NpcDefinition;
import com.zarador.world.entity.impl.player.Player;

/**
 * Generates a certain drop * x
 * @author Jonny
 */
public class DropGenerator {

    private int rarity = 0;

    private int totalRare = 0;
    private int totalVeryRare = 0;
    private int totalEpic = 0;

    private int npcId;

    private ArrayList<Integer> rareDrops = new ArrayList<>();
    private ArrayList<Integer> epicDrops = new ArrayList<>();

    /**
     * Generates a interface that displays x amount of rare/very rare/epic drops.
     * @param player
     * @param amount
     * @param npcId
     */
    public void generate(Player player, int amount, int npcId) {
        if(amount > 1000) {
            player.getPacketSender().sendMessage("You can only do 1,000 drops at a time.");
            return;
        }
        if(amount <= 0) {
            player.getPacketSender().sendMessage("You must enter a valid number to generate a drop.");
            return;
        }
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getInstance(locale);

        this.rarity = 0;

        //Reset
        setTotalRare(0);
        setTotalVeryRare(0);
        setTotalEpic(0);

        //Erases all random stuff on the interface
        for (int i = 42508; i < 42600; i++)
            player.getPacketSender().sendString(i, "");

        //Erase all items on interface
        for (int i = 0; i < 20; i++)
            player.getPacketSender().sendItemOnInterface(42603 + i, -1, 0, 0);

        this.rareDrops = new ArrayList<>();
        this.epicDrops = new ArrayList<>();

        LootTable table = LootSystem.tables.get(npcId);

        if(table == null) {
            player.getPacketSender().sendString(42508, "Doesn't exist");
            player.getPacketSender().sendMessage("<col=ff0000>The npc id "+npcId+" does not have a drop table.");
            return;
        }
        setNpcId(npcId);
        for (int i = 0; i < amount; i++) {
            Item roll = LootSystem.rollDrop(player, null, table.getSortedLoot(), npcId, true);
            if(roll == null) {
                continue;
            }
            if(getRarity() == 3) {
                incrementTotalRare(1);
            } else if(getRarity() == 4) {
                incrementTotalVeryRare(1);
            } else if(getRarity() == 5) {
                incrementTotalEpic(1);
            }
            setRarity(-1);
        }

        //Sets the default interface stuff
        player.getPacketSender().sendInterface(42499);

        player.getPacketSender().sendString(42508, NpcDefinition.forId(npcId).getName());
        player.getPacketSender().sendString(42509, "@or1@Boost: "+LootSystem.getExtraChance(player));
        player.getPacketSender().sendString(42510, "@or1@Kills: "+currencyFormatter.format(amount));

        player.getPacketSender().sendString(42512, "@yel@Epic: ("+getTotalEpic()+")");

        //Epic drops
        ArrayList<Integer> completed = new ArrayList<>();
        for(int i = 0; i < getEpicDrops().size(); i++) {
            if(getEpicDrops().get(i) == null) {
                continue;
            }
            int itemId = (Integer) getEpicDrops().get(i);
            player.getPacketSender().sendString(42513 + i, "@yel@"+ItemDefinition.forId(itemId).getName());
            if(42613 + i > 42622) {
                break;
            }
            if(completed.contains(itemId)) {
                continue;
            }

            completed.add(itemId);
            player.getPacketSender().sendItemOnInterface(42613 + completed.indexOf(itemId), itemId, 0, getTotalInArrayList(itemId, getEpicDrops()));
        }

        player.getPacketSender().sendString(42513 + getTotalEpic() + 1, "@mds@Very Rare: ("+getTotalVeryRare()+")");

        //Very Rare Drops
        completed = new ArrayList<>();
        for(int i = 0; i < getRareDrops().size(); i++) {
            if(getRareDrops().get(i) == null) {
                continue;
            }
            int itemId = (Integer) getRareDrops().get(i);
            player.getPacketSender().sendString(42513 + getTotalEpic() + i + 2, "@mds@"+ItemDefinition.forId(itemId).getName());
            if(42603 + i > 42612) {
                break;
            }
            if(completed.contains(itemId)) {
                continue;
            }
            completed.add(itemId);
            player.getPacketSender().sendItemOnInterface(42603 + completed.indexOf(itemId), itemId, 0, getTotalInArrayList(itemId, getRareDrops()));
        }
    }

    /**
     * Checks the chance of this item on a npc drop table. (1/x)
     * @param itemId
     */
    public void checkChance(Player player, int itemId) {
        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getInstance(locale);

        LootTable table = LootSystem.tables.get(getNpcId());
        if(table != null) {
            for(int i = 0; i < getEpicDrops().size(); i++) {
                if(table.getSortedLoot()[5] == null) {
                    break;
                }
                if(getEpicDrops().get(i).equals(itemId)) {
                    player.getPacketSender().sendMessage("The chance of "+ItemDefinition.forId(itemId).getName()+" dropping in the epic drop table is (1/"+currencyFormatter.format(getKillsWithBoost(player, NpcDefinition.forId(getNpcId()).getEpicRare()) * table.getSortedLoot()[5].length)+").");
                    break;
                }
            }
            for(int i = 0; i < getRareDrops().size(); i++) {
                if(table.getSortedLoot()[4] == null) {
                    break;
                }
                if(getRareDrops().get(i).equals(itemId)) {
                    player.getPacketSender().sendMessage("The chance of "+ItemDefinition.forId(itemId).getName()+" dropping in the rare drop table is (1/"+currencyFormatter.format(getKillsWithBoost(player, NpcDefinition.forId(getNpcId()).getVeryRare()) * table.getSortedLoot()[4].length)+").");
                    break;
                }
            }
        }
    }

    /**
     * Get the current npc id for the drop table
     * @return
     */
    public int getNpcId() {
        return this.npcId;
    }

    /**
     * Set the npc id for the drop table
     * @param npcId
     */
    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }

    /**
     * Gets the total amount of kills generated with your drop rate boost.
     * @param player
     * @param kills
     * @return
     */
    public int getKillsWithBoost(Player player, int kills) {
        return kills - (int) (kills * LootSystem.getExtraChance(player));
    }


    /**
     * Get the total amount of a id inside of the array list.
     * @param id
     * @return
     */
    public int getTotalInArrayList(int id, ArrayList arrayList) {
        int total = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if(arrayList.get(i).equals(id)) {
                total++;
            }
        }
        return total;
    }

    /**
     * Gets the array list for rare items
     * @return
     */
    public ArrayList getRareDrops() {
        return this.rareDrops;
    }

    /**
     * Gets the array list for rare items
     * @return
     */
    public ArrayList getEpicDrops() {
        return this.epicDrops;
    }

    /**
     * Gets the rarity of your current drop.
     * @return
     */
    public int getRarity() {
        return this.rarity;
    }

    /**
     * Sets the rarity of your current drop.
     * @param rarity
     */
    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    /**
     * Gets the total rare drops generated.
     * @return
     */
    public int getTotalRare() {
        return this.totalRare;
    }

    /**
     * Sets total rare drops generated
     * @param total
     */
    public void setTotalRare(int total) {
        this.totalRare = total;
    }

    /**
     * Increments the total amount of rare drops by a value
     * @param amount
     */
    public void incrementTotalRare(int amount) {
        this.totalRare += amount;
    }

    /**
     * Gets the total very rare drops generated.
     * @return
     */
    public int getTotalVeryRare() {
        return this.totalVeryRare;
    }

    /**
     * Sets total very rare drops generated
     * @param total
     */
    public void setTotalVeryRare(int total) {
        this.totalVeryRare = total;
    }

    /**
     * Increments the total amount of very rare drops by a value
     * @param amount
     */
    public void incrementTotalVeryRare(int amount) {
        this.totalVeryRare += amount;
    }

    /**
     * Gets the total epic drops generated.
     * @return
     */
    public int getTotalEpic() {
        return this.totalEpic;
    }

    /**
     * Get the total rare drops in general.
     * @return
     */
    public int getTotalDrops() {
        return this.totalRare + this.totalVeryRare + this.totalEpic;
    }

    /**
     * Sets total epic drops generated
     * @param total
     */
    public void setTotalEpic(int total) {
        this.totalEpic = total;
    }

    /**
     * Increments the total amount of epic drops by a value
     * @param amount
     */
    public void incrementTotalEpic(int amount) {
        this.totalEpic += amount;
    }
}
