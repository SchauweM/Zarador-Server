package com.zarador.world.content.skill.impl.dungeoneering.floors;

import com.zarador.model.*;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.WeaponAnimations;
import com.zarador.model.definitions.WeaponInterfaces;
import com.zarador.model.player.GameMode;
import com.zarador.net.packet.impl.EquipPacketListener;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.BonusManager;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zarador.world.content.skill.impl.dungeoneering.Floor;
import com.zarador.world.entity.impl.GroundItemManager;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles the first dungeoneering floor
 * @Author Jonny
 */
public class Floor1 extends Floor {

    private Player player;
    private NPC boss;

    public Floor1(Player player) {
        this.player = player;
        this.boss = getBosses()[Misc.inclusiveRandom(getBosses().length - 1)];
        this.boss.setDungeoneeringNpc(true);
        this.boss.setWalkEnabled(true);
        this.boss.setWalkingDistance(2);
    }

    private NPC[] minions = new NPC[] {
            //Battlemages
            new NPC(912, new Position(3285, 9170)),
            new NPC(912, new Position(3311, 9174)),
            new NPC(912, new Position(3308, 9188)),

            //Dark wizards
            new NPC(174, new Position(3299, 9170)),
            new NPC(174, new Position(3303, 9171)),
            new NPC(174, new Position(3306, 9180)),

            //Zombies
            new NPC(75, new Position(3300, 9176)),
            new NPC(75, new Position(3297, 9178)),

            //Skeletons
            new NPC(93, new Position(3285, 9179)),

            //Ghosts
            new NPC(103, new Position(3278, 9177)),
            new NPC(103, new Position(3277, 9183)),
            new NPC(103, new Position(3280, 9188)),
            new NPC(103, new Position(3288, 9188)),

            //Giant spiders
            new NPC(60, new Position(3300, 9203)),
            new NPC(60, new Position(3288, 9202)),

            //Poison spiders
            new NPC(134, new Position(3281, 9203)),
            new NPC(134, new Position(3307, 9203)),
    };

    private NPC[] bosses = new NPC[] {
            //Astea Frostweb
            new NPC(9992, new Position(3282, 9195)),
            //Icy bones
            new NPC(10051, new Position(3282, 9195)),
    };

    @Override
    public void enterFloor() {
        if(!player.getDungeoneering().canEnterDungeon()) {
            return;
        }
        player.getDungeoneering().setDungeonStage(Dungeoneering.DungeonStage.ENTERED);
        for(int i = 0; i < getMinions().length; i++) {
            getMinions()[i].setWalkEnabled(true);
            getMinions()[i].setWalkingDistance(2);
            getMinions()[i].setDungeoneeringNpc(true);
            getMinions()[i].getPosition().setZ(player.getIndex() * 4);
            getMinions()[i].setConstitution(getMinions()[i].getDefaultConstitution());

            World.register(getMinions()[i]);
        }
        player.getPacketSender().sendMessage("Welcome to Dungeoneering floor "+getFloorId()+".");
        player.moveTo(getStartPosition());
        player.getDungeoneering().updateInterface();
        player.getDungeoneering().giveStarterItems();
        player.getDungeoneering().giveBindedItems();
    }

    @Override
    public void leaveFloor() {
        if(player.isUsingChest()) {
            player.getPacketSender().sendMessage("You must wait until you finish using the chest to leave.");
            return;
        }
        player.moveTo(new Position(3464 - Misc.inclusiveRandom(0, 7), 3722 - Misc.inclusiveRandom(0, 4), 0));
        for(int i = 0; i < getMinions().length; i++) {
            World.deregister(getMinions()[i]);
        }
        World.deregister(this.getBoss());
        //player.setRegionInstance(null);
        player.getClickDelay().reset();
        player.getEquipment().resetItems().refreshItems();
        player.getInventory().resetItems().refreshItems();
        PrayerHandler.deactivateAll(player);
        CurseHandler.deactivateAll(player);
        player.getEquipment().refreshItems();
        player.getUpdateFlag().flag(Flag.APPEARANCE);
        EquipPacketListener.resetWeapon(player);
        WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
        BonusManager.update(player);
        for(Skill skill : Skill.values())
            player.getSkillManager().setCurrentLevel(skill, player.getSkillManager().getMaxLevel(skill));
        player.getSkillManager().stopSkilling();
        player.getPacketSender().sendClientRightClickRemoval();
        player.getDungeoneering().setKills(0);
        player.getDungeoneering().setDeaths(0);
        player.getDungeoneering().setDungeonStage(Dungeoneering.DungeonStage.DEFAULT);
    }

    @Override
    public void fightBoss(GameObject gameObject) {
        Position spawnPosition = null;
        if(gameObject.getPosition().equals(3280, 9199)) { //west
            if(player.getPosition().getY() == 9200) {
                player.moveTo(new Position(3280, 9199, player.getDungeoneering().getZ()));
                spawnPosition = new Position(3281, 9195, player.getDungeoneering().getZ());
            } else {
                player.moveTo(new Position(3280, 9200, player.getDungeoneering().getZ()));
            }
        } else if(gameObject.getPosition().equals(3306, 9199)) { //east
            if(player.getPosition().getY() == 9200) {
                player.moveTo(new Position(3306, 9199, player.getDungeoneering().getZ()));
                spawnPosition = new Position(3305, 9195, player.getDungeoneering().getZ());
            } else {
                player.moveTo(new Position(3306, 9200, player.getDungeoneering().getZ()));
            }
        }
        if(spawnPosition != null) {
            if(player.getDungeoneering().getDungeonStage() == Dungeoneering.DungeonStage.ENTERED) {
                this.boss.setPosition(spawnPosition);
                this.boss.setConstitution(this.boss.getDefaultConstitution());
                World.register(this.boss);
                player.getDungeoneering().setDungeonStage(Dungeoneering.DungeonStage.SPAWNED_BOSS);
            }
        }
    }

    @Override
    public NPC[] getMinions() {
        return this.minions;
    }

    @Override
    public NPC[] getBosses() {
        return this.bosses;
    }

    @Override
    public int getFloorId() {
        return 1;
    }

    @Override
    public Position getStartPosition() {
        return new Position(3277, 9171, player.getIndex() * 4);
    }

    @Override
    public int getRequiredKills() {
        return 10;
    }

    @Override
    public NPC getBoss() {
        return this.boss;
    }

    @Override
    public void killedBoss() {
        //Bones
        GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(526, 1), getBoss().getPosition(), player.getUsername(), false, 150, false, 200));

        //War key
        GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(18236, 1), getBoss().getPosition(), player.getUsername(), false, 150, false, 200));

        player.getDungeoneering().setDungeonStage(Dungeoneering.DungeonStage.KILLED_BOSS);
    }

    @Override
    public void completeFloor() {
        if(player.isUsingChest()) {
            player.getPacketSender().sendMessage("You must wait until you finish using the chest to leave.");
            return;
        }

        int tokens = player.getDungeoneering().getKills() * 500;
        if(player.getDungeoneering().getDungeonStage() == Dungeoneering.DungeonStage.KILLED_BOSS) {
            tokens += 700;
        }
        tokens += Misc.inclusiveRandom(100, 300);
        int experience = tokens;

        double tokenBoost = tokens * player.getDonatorRights().getDungTokenBoost();

        player.getPointsHandler().setDungeoneeringTokens(tokens + (int) tokenBoost, true);

        if(player.getGameModeAssistant().getGameMode() == GameMode.KNIGHT) {
            player.getSkillManager().addSkillExperience(Skill.DUNGEONEERING, experience / 3);
        } else {
            player.getSkillManager().addSkillExperience(Skill.DUNGEONEERING, experience);
        }

        player.getPacketSender().sendMessage("You have received "+Misc.format(tokens + (int) tokenBoost)+" dungeoneering tokens for completing floor 1.");

        Achievements.doProgress(player, Achievements.AchievementData.COMPLETE_5_DUNG_FLOORS);
        Achievements.doProgress(player, Achievements.AchievementData.COMPLETE_50_DUNG_FLOORS);
        Achievements.finishAchievement(player, Achievements.AchievementData.COMPLETE_DUNG_FLOOR);

        leaveFloor();
    }

}
