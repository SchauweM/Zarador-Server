package com.zarador.model.player;

import com.zarador.model.Skill;
import com.zarador.util.Misc;
import com.zarador.world.content.PlayerPanel;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.skill.SkillManager;
import com.zarador.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 7/16/2016.
 *
 * @author Seba
 */
public class GameModeAssistant {

    /**
     * Constructs our gamemode assistant.
     * @param player
     */
    public GameModeAssistant(Player player) {
        this.player = player;
    }

    /**
     * Defines the player this assistant is for.
     */
    private final Player player;

    /**
     * Define what game mode the player is on. If they dont have one we will give them default by default.
     */
    private GameMode gameMode = GameMode.KNIGHT;

    /**
     * Returns the ordinal of the game mode.  We will use this for saving.
     * @return
     */
    public int ordinal() {
        return this.gameMode.ordinal();
    }

    /**
     * This is only used to set the game mode from a save.
     * @param gameMode
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Returns the formatted name of the gamemode.
     * @return
     */
    public String getModeName() {
        return Misc.formatText(this.gameMode.toString().toLowerCase().replace("_", ""));
    }

    /**
     * Returns the game mode's exp rate.
     * @return
     */
    public int getModeExpRate() {
        return this.gameMode.getModeExpRate();
    }

    /**
     * Returns the drop rate of the game mode.
     * @return
     */
    public double getMonsterDropRate() {
        return this.gameMode.getMonsterDropRate();
    }

    /**
     * Returns the special recovery rate of the game mode.
     * @return
     */
    public int getSpecialRecoveryRate() {
        return this.gameMode.getSpecialRecoveryRate();
    }

    /**
     * Returns the prayer drain rate for the player.
     * @return
     */
    public double getPrayerDrainRate() {
        return this.gameMode.getPrayerDrainRate();
    }

    /**
     * Returns the game mode of the player.
     * @return
     */
    public GameMode getGameMode() {
        return this.gameMode;
    }

    /**
     * Checks to see if the player is a iron man.
     * @return
     */
    public boolean isIronMan() {
        return this.gameMode == GameMode.IRONMAN;
    }

    /**
     * This will process a game mode switch.
     * @param player
     * @param achiev - do you reset their achievements?
     */
    public void resetStats(Player player, boolean achiev) {
        if (achiev) {
            /**
             * We will now reset all of the player's achievements.
             */
            for (int i = 0; i < Achievements.AchievementData.values().length; i++) {
                player.getAchievementAttributes().setCompletion(i, false);
            }
            for (int i = 0; i < player.getAchievementAttributes().getProgress().length; i ++) {
                player.getAchievementAttributes().setProgress(i, 0);
            }
            if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
                Achievements.finishAchievement(player, Achievements.AchievementData.DEFEAT_THE_CULINAROMANCER);
            }
            player.getPointsHandler().setAchievementPoints(0, false);
        }

        /**
         * Time to reset all of the players skills
         */
        for (int i = 0; i < SkillManager.MAX_SKILLS; i++) {
            Skill skill = Skill.values()[i];
            /**
             * We need to make sure they have the correct hp when they move to the lower ranks.
             */
            if (skill == Skill.CONSTITUTION) {
                player.getSkillManager().setMaxLevel(Skill.CONSTITUTION, 100);
                player.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, 100);
                player.getSkillManager().setExperience(Skill.CONSTITUTION, SkillManager.getExperienceForLevel(10));
                continue;
            }
            /**
             * We need to make sure they have the correct prayer level
             */
            if (skill == Skill.PRAYER) {
                player.getSkillManager().setMaxLevel(skill, 10);
                player.getSkillManager().setCurrentLevel(skill, 10);
                player.getSkillManager().setExperience(skill, 0);
                continue;
            }
            player.getSkillManager().setMaxLevel(skill, 1);
            player.getSkillManager().setCurrentLevel(skill, 1);
            player.getSkillManager().setExperience(skill, 0);
        }

        player.getSlayer().setSlayerMaster(null);
        player.getSlayer().setSlayerTask(null);
        player.getSlayer().setDuoSlayer(null);
        player.getSlayer().setAmountLeft(0);
        PlayerPanel.refreshPanel(player);
        Achievements.updateInterface(player);
        player.save();
    }

}