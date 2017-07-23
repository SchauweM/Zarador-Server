package com.zarador.world.content.skill.impl.dungeoneering;

import com.zarador.GameServer;
import com.zarador.GameSettings;
import com.zarador.model.GroundItem;
import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.content.skill.impl.dungeoneering.floors.Floor1;
import com.zarador.world.entity.impl.GroundItemManager;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class Dungeoneering {

    /**
     * Tells you what stage you are on in your Dungeon
     */
    public enum DungeonStage {
        DEFAULT(),
        ENTERED(),
        SPAWNED_BOSS(),
        KILLED_BOSS(),

        DungeonStage() {

        }
    }

    private Player player;
    private Floor floor;
    private int kills;
    private int deaths;
    private Dungeoneering.DungeonStage dungeonStage = Dungeoneering.DungeonStage.DEFAULT;
    private Player[] party = {
            null,
            null,
            null,
            null,
    };
    private Item[] bind = {
            null,
            null,
            null,
            null,
            null,
    };

    /**
     * Obtain what dungeon stage you are on
     *
     * @return
     */
    public Dungeoneering.DungeonStage getDungeonStage() {
        return this.dungeonStage;
    }

    /**
     * Get the player for the dungeon
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the whole dungeoneering party
     * @return
     */
    public Player[] getParty() {
        return this.party;
    }

    /**
     * Get your players binded items
     * @return
     */
    public Item[] getBindedItems() {
        return this.bind;
    }

    public void setBind(Item[] items) {
        this.bind = items;
    }

    /**
     * Bind a dungeoneering item
     * @param toBind
     */
    public void bindItem(Item toBind) {
        int i = 0;
        boolean successfulBind = false;
        for(Item item: getBindedItems()) {
            if(item == null) {
                int bindedID = getBindedID(toBind);
                this.bind[i] = new Item(bindedID, 1);
                successfulBind = true;
                player.getInventory().delete(toBind);
                player.getInventory().add(bindedID, 1);
                break;
            }
            i++;
        }
        if(successfulBind) {
            player.getPacketSender().sendMessage("You have successfully binded the item "+toBind.getDefinition().getName()+" and can bind "+getBindSlotsOpen()+" more items.");
        } else {
            player.getPacketSender().sendMessage("You can't bind any more items! Destroy a item to bind another one.");
        }
    }

    /**
     * Removes a binded item.
     * @param toUnBind
     */
    public void unBindItem(Item toUnBind) {
        int index = 0;
        for(Item item: getBindedItems()) {
            if(item != null) {
                if(item.getId() == toUnBind.getId()) {
                    this.bind[index] = null;
                    break;
                }
            }
            index++;
        }
    }

    /**
     * Finds how many items you can currently bind.
     * @return
     */
    public int getBindSlotsOpen() {
        int amountOpen = 0;
        for(Item item: getBindedItems()) {
            if(item == null) {
                amountOpen++;
            }
        }
        return amountOpen;
    }

    /**
     * Checks if the item is a dungeoneering equipment/weapon.
     * @param item
     * @return
     */
    public static boolean isDungItem(String name) {
        name = name.toLowerCase();
        if(name.contains("novite")) {
            return true;
        } else if(name.contains("bathus")) {
            return true;
        } else if(name.contains("marmaros")) {
            return true;
        } else if(name.contains("kratonite")) {
            return true;
        } else if(name.contains("fractite")) {
            return true;
        } else if(name.contains("zephyrium")) {
            return true;
        } else if(name.contains("argonite")) {
            return true;
        } else if(name.contains("katagon")) {
            return true;
        } else if(name.contains("gorgonite")) {
            return true;
        } else if(name.contains("promethium")) {
            return true;
        } else if(name.contains("primal")) {
            return true;
        }
        return false;
    }

    /**
     * Gets the item id of a binded dungeoneering item.
     * @param item
     * @return
     */
    public int getBindedID(Item item) {
        for (int i = 0; i < ItemDefinition.getMaxAmountOfItems() - 1; i++) {
            if (ItemDefinition.forId(i).getName().toLowerCase().equalsIgnoreCase(item.getDefinition().getName().toLowerCase() + " (b)")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds all binded items to a players inventory.
     */
    public void giveBindedItems() {
        for(Item item: getBindedItems()) {
            if(item != null) {
                if(item.getId() > 0) {
                    if(isDungItem(item.getDefinition().getName())) {
                        player.getInventory().add(item, true);
                    }
                }
            }
        }
    }

    /**
     * Set someone to a certain party slot
     * @param index
     * @param other
     */
    public void setParty(int index, Player other) {
        this.party[index] = other;
    }

    /**
     * Add a player to the dungeoneering party
     * @param other
     */
    public void addToParty(Player other) {
        for(int i = 0; i < getParty().length; i++) {
            if(getParty()[i] == null) {
                setParty(i, other);
                break;
            }
        }
    }

    /**
     * Check if the player is currently doing dungeoneering.
     * @return
     */
    public boolean isDoingDung() {
        if(getDungeonStage() == DungeonStage.DEFAULT) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check if the dungeoneering party is full
     * @return
     */
    public boolean isPartyFull() {
        for(int i = 0; i < getParty().length; i++) {
            if(getParty()[i] == null) {
               return false;
            }
        }
        return true;
    }

    /**
     * Get a person in your party
     * @param index
     * @return
     */
    public Player getParty(int index) {
        return this.party[index];
    }

    /**
     * Get the amount of people in the party.
     * @return
     */
    public int getAmountInParty() {
        int amountInParty = 0;
        for(int i = 0; i < getParty().length; i++) {
            if(getParty()[i] !=  null) {
                amountInParty++;
            }
        }
        return amountInParty;
    }

    /**
     * Updates the party interface
     */
    public void updatePartyInterface() {
        int startString = 26236;
        for(int i = 0; i < getParty().length; i++) {
            //player.getPacketSender().sendString(26236, getParty);
            //26236++;
        }
    }

    /**
     * Get your current dungeoneering floor
     * @return
     */
    public Floor getFloor() {
        return this.floor;
    }

    /**
     * Gets how many kills your party has
     * @return
     */
    public int getKills() {
        return this.kills;
    }

    /**
     * Gets how many deaths your party has.
     * @return
     */
    public int getDeaths() {
        return this.deaths;
    }

    /**
     * Set a new dungeoneering floor
     * @param floor
     */
    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    /**
     * Sets your new dungeon stage.
     *
     * @param stage
     */
    public void setDungeonStage(Dungeoneering.DungeonStage stage) {
        this.dungeonStage = stage;
    }

    /**
     * Sets X amount of kills for your party.
     * @param kills
     */
    public void setKills(int kills) {
        this.kills = kills;
        updateInterface();
    }

    /**
     * Add X amount of kills for your party.
     * @param kills
     */
    public void addKills(int kills) {
        this.kills += kills;
        updateInterface();
    }

    /**
     * Sets X amount of deaths for your party.
     * @param deaths
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
        updateInterface();
    }

    /**
     * Add X amount of deaths for your party.
     * @param deaths
     */
    public void addDeaths(int deaths) {
        this.deaths += deaths;
        updateInterface();
    }

    public Dungeoneering(Player player) {
        this.player = player;
        this.floor = new Floor1(player);
        this.kills = 0;
        this.deaths = 0;
        this.party = new Player[] {
                null,
                null,
                null,
                null,
        };
        this.bind = new Item[] {
                null,
                null,
                null,
                null,
                null,
        };
    }

    /**
     * Change your dungeoneering floor
     */
    public void changeFloor() {
        if(getFloor().getFloorId() == 1) {
            setFloor(new Floor1(player));
        }
        player.getPacketSender().sendString(26240, ""+getFloor().getFloorId());
    }

    /**
     * When using the open party interface tab option.
     * This will change your tab interface and do all the setters.
     */
    public void showTab() {
        player.getPacketSender().sendDungeoneeringTabIcon(true);
        player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 26224);
        player.getPacketSender().sendTab(GameSettings.QUESTS_TAB);
        player.getPacketSender().sendString(26240, ""+getFloor().getFloorId());
    }

    /**
     * Leave the exit room.
     */
    public void leaveExitRoom() {
        player.moveTo(new Position(3457, 3725, 0));
        player.getDungeoneering().setDungeonStage(DungeonStage.DEFAULT);
    }

    /**
     * Update the kills/deaths interface
     */
    public void updateInterface() {
        player.getPacketSender().sendString(37508, "Solo kills: "+getKills());
        player.getPacketSender().sendString(37509, "Solo deaths: "+getDeaths());
    }

    /**
     * Tells you if you have the required amount of kills
     * to proceed onto the boss room.
     * @return
     */
    public boolean hasRequiredKills() {
        if(GameSettings.DEVELOPER_MODE) {
            return true;
        }
        return getKills() >= getFloor().getRequiredKills();
    }

    /**
     * Check if you can enter the dungeon
     * @return
     */
    public boolean canEnterDungeon() {
        if(GameServer.isUpdating()) {
            player.getPacketSender().sendMessage("You can't enter a dungeon while an update is happening!");
            return false;
        }
        String plrCannotEnter = null;
        if(player.getSummoning().getFamiliar() != null) {
            player.getPacketSender().sendMessage("You must dismiss your familiar before being allowed to enter a dungeon.");
            return false;
        }
        for(Item t : player.getEquipment().getItems()) {
            if(t != null && t.getId() > 0) {
                plrCannotEnter = player.getUsername();
            }
        }
        for(Item t : player.getInventory().getItems()) {
            if(t != null && t.getId() > 0) {
                plrCannotEnter = player.getUsername();
            }
        }
        if(plrCannotEnter != null) {
            player.getPacketSender().sendMessage("Your team cannot enter the dungeon because "+plrCannotEnter+" hasn't banked").sendMessage("all of their items.");
            return false;
        }
        return true;
    }

    /**
     * Adds a ring of kinship to a players bank
     * upon login as long as they do not already
     * have one.
     */
    public void addRingOfKinship() {
        boolean needsRing = true;
        for(Item t : player.getEquipment().getItems()) {
            if(t != null && t.getId() > 0) {
                if(t.getId() == 15707) {
                    needsRing = false;
                }
            }
        }
        for(Item t : player.getInventory().getItems()) {
            if(t != null && t.getId() > 0) {
                if(t.getId() == 15707) {
                    needsRing = false;
                }
            }
        }
        for (int i = 0; i < player.getBanks().length; i++) {
            if (player.getBank(i) != null) {
                for(Item t : player.getBank(i).getItems()) {
                    if(t != null && t.getId() > 0) {
                        if(t.getId() == 15707) {
                            needsRing = false;
                        }
                    }
                }
            }
        }
        if(needsRing) {
            player.getBank(0).add(15707, 1);
        }
    }

    /**
     * Gets the floor you are currently on for dungeoneering
     * @return
     */
    public int getZ() {
        return this.getPlayer().getIndex() * 4;
    }

    public Item[] MINION_REWARDS = {
        new Item(18173, 1),
        new Item(18173, 1),
        new Item(18159, 2),
        new Item(2436, 1),
        new Item(2440, 1),
        new Item(2442, 1),
        new Item(2444, 1),
        new Item(3040, 1),
        new Item(18165, 1),
        new Item(18165, 1),
        new Item(2434, 1)
    };

    public Item[] RARE_MINION_REWARDS = {
        //Zephyrium
        new Item(15763, 1),
        new Item(16283, 1),
        new Item(16305, 1),
        new Item(16349, 1),
        new Item(16371, 1),
        new Item(16393, 1),
        new Item(16415, 1),
        new Item(16657, 1),
        new Item(16679, 1),
        new Item(16701, 1),
        new Item(16723, 1),
        new Item(16797, 1),
        new Item(16899, 1),
        new Item(16945, 1),
        new Item(17029, 1),
        new Item(17103, 1),
        new Item(17249, 1),
        new Item(17351, 1),

        //Fractite
        new Item(15761, 1),
        new Item(16281, 1),
        new Item(16303, 1),
        new Item(16347, 1),
        new Item(16369, 1),
        new Item(16391, 1),
        new Item(16413, 1),
        new Item(16655, 1),
        new Item(16677, 1),
        new Item(16699, 1),
        new Item(16721, 1),
        new Item(16789, 1),
        new Item(16897, 1),
        new Item(16943, 1),
        new Item(16961, 1),
        new Item(16973, 1),
        new Item(17027, 1),
        new Item(17095, 1),
        new Item(17247, 1),
        new Item(17349, 1),

        //Kratonite
        new Item(15759, 1),
        new Item(16279, 1),
        new Item(16301, 1),
        new Item(16345, 1),
        new Item(16367, 1),
        new Item(16411, 1),
        new Item(16411, 1),
        new Item(16653, 1),
        new Item(16675, 1),
        new Item(16697, 1),
        new Item(16719, 1),
        new Item(16781, 1),
        new Item(16895, 1),
        new Item(16941, 1),
        new Item(17025, 1),
        new Item(17087, 1),
        new Item(17245, 1),
        new Item(17347, 1),

        //Marmaros
        new Item(15757, 1),
        new Item(16277, 1),
        new Item(16299, 1),
        new Item(16343, 1),
        new Item(16365, 1),
        new Item(16387, 1),
        new Item(16409, 1),
        new Item(16651, 1),
        new Item(16673, 1),
        new Item(16695, 1),
        new Item(16717, 1),
        new Item(16773, 1),
        new Item(16893, 1),
        new Item(16939, 1),
        new Item(17023, 1),
        new Item(17079, 1),
        new Item(17243, 1),
        new Item(17345, 1),

    };

    /**
     * Gives the reward for the dungeoneering minions
     */
    public void handleMinionReward(NPC npc) {
        //Bones
        GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526, 1), npc.getPosition(), player.getUsername(), false, 150, false, 200));

        //Random reward
        GroundItemManager.spawnGroundItem(player, new GroundItem(MINION_REWARDS[Misc.inclusiveRandom(MINION_REWARDS.length - 1)], npc.getPosition(), player.getUsername(), false, 150, false, 200));

        //Rare reward
        int rareChance = Misc.random(10);
        if(rareChance == 1) {
            GroundItemManager.spawnGroundItem(player, new GroundItem(RARE_MINION_REWARDS[Misc.inclusiveRandom(RARE_MINION_REWARDS.length - 1)], npc.getPosition(), player.getUsername(), false, 150, false, 200));
        }

        //War key
        int chance = Misc.random(player.getDungeoneering().getFloor().getRequiredKills() * 2);
        if(chance == 1) {
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(18236, 1), npc.getPosition(), player.getUsername(), false, 150, false, 200));
        }
    }

    public Item[] STARTER_ITEMS = {
         new Item(18173, 5),
         new Item(18165, 2),
         new Item(16939, 1),
         new Item(1215, 1),
    };

    /**
     * Add all starter items when going into a dungeon.
     */
    public void giveStarterItems() {
        for(Item item: STARTER_ITEMS) {
            player.getInventory().add(item, true);
        }
    }

}
