package com.zarador.world.content.skill.impl.slayer;

import org.scripts.kotlin.content.dialog.Slayer.ResetTask;
import org.scripts.kotlin.content.dialog.teleports.jewlery.SlayerRing;

import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.model.Skill;
import com.zarador.model.definitions.NpcDefinition;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.PlayerPanel;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.content.wells.WellOfGoodness;
import com.zarador.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The Slayer Skill
 * @Author Jonny
 */

public class Slayer {

    Player player;
    private SlayerMasters slayerMaster;
    private SlayerTasks slayerTask;
    private int amountLeft;
    private int slayerStreak;
    private String duoPlayer;
    private int givenDuo = 0;
    private boolean onlyBossDuradel = false;

    public Slayer(Player player) {
        this.player = player;
    }

    /**
     * Check how many times you have given a duo assignment
     * @return
     */
    public int getDuoTimes() {
        return this.givenDuo;
    }

    /**
     * Set an amount of times you have done duo.
     * @param amount
     */
    public void setDuoTimes(int amount) {
        this.givenDuo = amount;
    }

    /**
     * Add duo times onto your current duo times.
     * @param amount
     */
    public void addDuoTimes(int amount) {
        this.givenDuo += amount;
    }

    /**
     * Get the slayer master that is assigned to the
     * players slayer task.
     * @return
     */
    public SlayerMasters getSlayerMaster() {
        return this.slayerMaster;
    }

    /**
     * Set the player a new slayer master depending on
     * what slayer task he is currently on.
     * @param slayerMaster
     */
    public void setSlayerMaster(SlayerMasters slayerMaster) {
        this.slayerMaster = slayerMaster;
    }

    /**
     * Get the slayer task that the player is currently on.
     * @return
     */
    public SlayerTasks getSlayerTask() {
        return this.slayerTask;
    }

    /**
     * Gets the total amount left on your slayer task.
     * @return
     */
    public int getAmountLeft() {
        return this.amountLeft;
    }

    /**
     * Sets the total amount left to kill on your slayer task.
     * @param amountLeft
     */
    public void setAmountLeft(int amountLeft) {
        this.amountLeft = amountLeft;
    }

    /**
     * Set the player a new slayer task.
     * @param slayerTask
     */
    public void setSlayerTask(SlayerTasks slayerTask) {
        this.slayerTask = slayerTask;
    }

    /**
     * Checks if you have the correct requirements to get
     * a slayer task from a certain slayer master.
     * @param slayerMaster
     * @return
     */
    public boolean hasMasterRequirements(SlayerMasters slayerMaster) {
        if(player.getSkillManager().getCombatLevel() >= slayerMaster.getCombatLevelRequirement()) {
            return true;
        }
        return false;
    }

    /**
     * Assigns a slayer task for your player
     * @param slayerMaster
     */
    public void assignSlayerTask(SlayerMasters slayerMaster, boolean bypass) {
        if(!bypass) {
            if (player.getSlayer().getSlayerTask() != null) {
                player.setNpcClickId(slayerMaster.getNpcId());
                player.getDialog().sendDialog(new SlayerDialog(player, 2, null));
                return;
            }
        }
        ArrayList<SlayerTasks> possibleTasks = new ArrayList<>();
        for(SlayerTasks slayerTasks: SlayerTasks.values()) {
            if(slayerTasks.getSlayerMaster() == slayerMaster || (slayerTasks.getSlayerMaster().ordinal() == slayerMaster.ordinal() - 1 && !player.getSlayer().isOnlyBossDuradel()) && slayerMaster != SlayerMasters.KURADAL) {
                if(!possibleTasks.contains(slayerTasks)) {
                    if (player.getSkillManager().getCurrentLevel(Skill.SLAYER) >= NpcDefinition.forId(slayerTasks.getNpcId(0)).getSlayerLevel()) {
                        possibleTasks.add(slayerTasks);
                    }
                }
            }
        }
        Collections.shuffle(possibleTasks);
        SlayerTasks myTask = possibleTasks.get(0);
        player.getSlayer().setSlayerMaster(slayerMaster);
        player.getSlayer().setSlayerTask(myTask);
        player.getSlayer().setAmountLeft(Misc.inclusiveRandom(myTask.getMinimumAmount(), myTask.getMaximumAmount()));
        player.getSlayer().setDuoSlayer(null);
        player.setNpcClickId(slayerMaster.getNpcId());
        player.getDialog().sendDialog(new SlayerDialog(player, 3, null));
        Achievements.finishAchievement(player, Achievements.AchievementData.GET_SLAYER_TASK);
        PlayerPanel.refreshPanel(player);
    }

    /**
     * Gets the name of your current slayer task
     * @return
     */
    public String getTaskName() {
        return this.getSlayerTask().getName();
    }

    /**
     * Gets the name of the player you are doing
     * duo slayer with.
     * @return
     */
    public String getDuoSlayerName() {
        return this.duoPlayer;
    }

    /**
     * Set a duo slayer partner name for your character.
     * @param name
     */
    public void setDuoSlayer(String name) {
        if(name == null) {
            if(this.duoPlayer != null) {
                Player partner = World.getPlayerByName(this.duoPlayer);
                if(partner != null) {
                    partner.getPacketSender().sendMessage("Your partner has left the duo slayer assignment.");
                    partner.getSlayer().resetDuoPlayer();
                }
            }
        }
        this.duoPlayer = name;
    }

    /**
     * Completely resets the duo partners slayer task.
     */
    public void resetDuoPlayer() {
        this.duoPlayer = null;
    }

    /**
     * Prompts a user with a duo slayer option
     * @param other
     */
    public void duoSlayerOption(Player other) {
        if(other == null) {
            player.getPacketSender().sendMessage("This player is currently offline!");
            return;
        }
        if(getSlayerTask() == null || getSlayerMaster() == null) {
            player.getPacketSender().sendMessage("You need a slayer task in order to assign a duo task with someone.");
            return;
        }
        if(other.getSlayer().getSlayerTask() != null || other.getSlayer().getSlayerMaster() != null) {
            player.getPacketSender().sendMessage("This player already has a slayer assignment.");
            return;
        }
        if(!other.getSlayer().hasMasterRequirements(player.getSlayer().getSlayerMaster())) {
            player.getPacketSender().sendMessage("This player does not have the combat level requirement for your slayer task.");
            return;
        }
        if(other.getSlayer().getDuoSlayerName() != null) {
            player.getPacketSender().sendMessage("This player is already on a duo slayer assignment.");
            return;
        }
        if(player.getSlayer().getDuoSlayerName() != null) {
            player.getPacketSender().sendMessage("You are already on a duo slayer assignment with somebody.");
            return;
        }
        if(player.getSlayer().getDuoTimes() >= 1) {
            player.getPacketSender().sendMessage("You can only send a duo slayer assignment once per task.");
            return;
        }
        if(other.getSlayer().getDuoTimes() >= 1) {
            player.getPacketSender().sendMessage("The other player has already initiated a duo assignment for his task.");
            return;
        }
        other.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
        other.getDialog().sendDialog(new SlayerDialog(other, 11, player));
    }

    /**
     * If the player accepts the duo slayer invitation
     * then they will now get your current slayer assignment.
     */
    public void assignDuoTask(Player other) {
        this.setSlayerTask(other.getSlayer().getSlayerTask());
        this.setSlayerMaster(other.getSlayer().getSlayerMaster());
        this.setAmountLeft(other.getSlayer().getAmountLeft());
        this.setDuoSlayer(other.getUsername());
        this.addDuoTimes(1);
        other.getSlayer().setDuoSlayer(player.getUsername());
        other.getSlayer().addDuoTimes(1);
        player.getDialog().sendDialog(new SlayerDialog(player, 13, null));
        PlayerPanel.refreshPanel(player);
    }

    /**
     * Opens the slayer dialog to check how many
     * monsters you have left to kill before your
     * task is over.
     */
    public void checkAmountLeft() {
        player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
        player.getDialog().sendDialog(new SlayerDialog(player, 5, null));
    }

    /**
     * Opens the slayer dailog to check if your master
     * has any recommendations for your current task.
     */
    public void checkRecommendations() {
        player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
        player.getDialog().sendDialog(new SlayerDialog(player, 6, null));
    }

    /**
     * Gives you an easier slayer task
     * @param slayerMaster
     */
    public void giveEasierTask(SlayerMasters slayerMaster) {
        if(player.getSlayer().getSlayerMaster().ordinal() > slayerMaster.ordinal()) {
            player.getSlayer().assignSlayerTask(slayerMaster, true);
        } else {
            player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
            player.getDialog().sendDialog(new SlayerDialog(player, 7, null));
        }
    }

    /**
     * Check if you killed a slayer npc,
     * if you did reward yourself!
     * @param npcId
     */
    public void killedNpc(int npcId) {
        if(player.getSlayer().getSlayerTask() == null) {
            return;
        }
        for(int i = 0; i < player.getSlayer().getSlayerTask().getNpcIds().length; i++) {
            if(player.getSlayer().getSlayerTask().getNpcId(i) == npcId) {
                player.getSlayer().decrementAmountLeft(1, i, false);
                break;
            }
        }
    }

    /**
     * Decrements a certain amount from the amount
     * of kills you have left to kill on your task.
     * @param decrement
     */
    public void decrementAmountLeft(int decrement, int taskIndex, boolean duo) {
        if(duo) {
            double experience = player.getSlayer().getSlayerTask().getExperience(taskIndex);
            if(WellOfGoodness.isActive("exp")) {
                experience *= 1.3;
            }
            experience *= player.getGameModeAssistant().getModeExpRate();
            experience /= 2;
            player.getSkillManager().addExactExperience(Skill.SLAYER, experience, true);
        } else {
            player.getSkillManager().addSkillExperience(Skill.SLAYER, player.getSlayer().getSlayerTask().getExperience(taskIndex));
        }
        this.amountLeft -= decrement;
        if(this.getDuoSlayerName() != null) {
            Player partner = World.getPlayerByName(this.getDuoSlayerName());
            if(partner != null) {
                if(!duo) {
                    if(partner.getSlayer().getSlayerTask().ordinal() == player.getSlayer().getSlayerTask().ordinal()) {
                        if(partner.getSlayer().getDuoSlayerName().equals(player.getUsername()) && player.getSlayer().getDuoSlayerName().equals(partner.getUsername())) {
                            if(partner.getPosition().isWithinDistance(player.getPosition(), 64)) {
                                partner.getSlayer().decrementAmountLeft(decrement, taskIndex, true);
                            }
                        }
                    }
                }
            }
        }
        if(this.amountLeft == 0) {
            if (slayerMaster == SlayerMasters.DURADEL) {
                Achievements.finishAchievement(player, Achievements.AchievementData.COMPLETE_AN_ELITE_SLAYER_TASK);
            }
            if (slayerMaster == SlayerMasters.NIEVE) {
                Achievements.finishAchievement(player, Achievements.AchievementData.COMPLETE_A_HARD_SLAYER_TASK);
            }
            player.getSlayer().addSlayerStreak(1);
            player.getPointsHandler().setSlayerPoints(this.getPointsToGive(), true);
            player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
            player.getDialog().sendDialog(new SlayerDialog(player, 8, null));
            player.getSlayer().setSlayerMaster(null);
            player.getSlayer().setSlayerTask(null);
            player.getSlayer().setDuoTimes(0);
            player.getSlayer().setDuoSlayer(null);
            player.getSlayer().setAmountLeft(0);
        } else if(this.amountLeft == 10 || this.amountLeft == 25 || this.amountLeft == 50 || this.amountLeft == 100 || this.amountLeft == 200) {
            player.setNpcClickId(player.getSlayer().getSlayerMaster().getNpcId());
            player.getPacketSender().sendMessage("You have "+this.amountLeft+" "+player.getSlayer().getTaskName()+"s left on your current Slayer Task.");
        }
        PlayerPanel.refreshPanel(player);
    }

    public void handleSlayerRingTP(int itemId) {
        if (!player.getClickDelay().elapsed(4500))
            return;
        if (player.getWalkingQueue().isLockMovement())
            return;
        SlayerTasks task = getSlayerTask();
        if (task == null)
            return;
        Position slayerTaskPos = new Position(task.getTaskPos().getX(),
                task.getTaskPos().getY(), task.getTaskPos().getZ());
        if (!TeleportHandler.checkReqs(player, slayerTaskPos))
            return;
        if (task.isWild()) {
            player.getDialog().sendDialog(new SlayerRing(player, slayerTaskPos));
        } else {
            TeleportHandler.teleportPlayer(player, slayerTaskPos, player.getSpellbook().getTeleportType());
        }
        Item slayerRing = new Item(itemId);
        player.getInventory().delete(slayerRing);
        if (slayerRing.getId() < 13288)
            player.getInventory().add(slayerRing.getId() + 1, 1);
        else
            player.getPacketSender().sendMessage("Your Ring of Slaying crumbles to dust.");
    }

    public void resetTask() {
        if (player.getSlayer().getTaskName() == null) {
            return;
        }
        if (player.getPointsHandler().getSlayerPoints() > 4) {
            player.getDialog().sendDialog(new ResetTask(player));
        } else {
            player.getPacketSender().sendInterfaceRemoval();
            player.getPacketSender().sendMessage("You do not have enough slayer points to reset your task.");
        }
    }

    /**
     * Get the current slayer streak that you are on
     * @return
     */
    public int getSlayerStreak() {
        return this.slayerStreak;
    }

    /**
     * Add onto your current slayer streak.
     * @param i
     */
    public void addSlayerStreak(int i) {
        this.slayerStreak += i;
    }

    /**
     * Sets a slayer streak
     * @param i
     */
    public void setSlayerStreak(int i) {
        this.slayerStreak = i;
    }

    /**
     * Reset your current slayer streak.
     */
    public void resetSlayerStreak() {
        this.slayerStreak = 0;
    }

    public int getPointsToGive() {
        for(int i = 1; i <= 100; i++) {
            if (this.slayerStreak == 10 * i && 10 * i != 50) {
                return player.getSlayer().getSlayerMaster().getTenTaskBonus();
            } else if (this.slayerStreak == 50 * i)
                return player.getSlayer().getSlayerMaster().getFiftyTaskBonus();
        }
        return player.getSlayer().getSlayerMaster().getPointsPerTask();
    }

    /**
     * Tells you if you are on a slayer task
     * depending on a npc id
     * @param npcId
     * @return
     */
    public boolean onSlayerTask(int npcId) {
        for(int i = 0; i <= this.getSlayerTask().getNpcIds().length; i++) {
            if(this.getSlayerTask().getNpcId(i) == npcId) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnlyBossDuradel() {
        return onlyBossDuradel;
    }

    public void setOnlyBossDuradel(boolean onlyBossDuradel) {
        this.onlyBossDuradel = onlyBossDuradel;
    }
}