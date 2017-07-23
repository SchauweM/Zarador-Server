package com.zarador.world.entity.impl.player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/6/2016.
 *
 * @author Seba
 */
public class PlayerTimers {

    public PlayerTimers(Player player) {
        this.player = player;
    }

    /**
     * The player the timers are for.
     */
    private Player player;

    /**
     * How long the player is jailed for.
     */
    private int jailTicks = -1;

    /**
     * How long the player cannot yell for.
     */
    private int yellTicks = -1;

    /**
     * How long do the npc's have before they start targeting your ass.
     */
    private int aggressiveDelay = 0;

    /**
     * Gets how long the player is jailed for
     * @return
     */
    public int getJailTicks() {
        return jailTicks;
    }

    /**
     * Sets how long the player is jailed for.
     * @param jailTicks
     */
    public void setJailTicks(int jailTicks) {
        this.jailTicks = jailTicks;
    }

    /**
     * Gets how long the player cannot yell.
     * @return
     */
    public int getYellTicks() {
        return yellTicks;
    }

    /**
     * Sets how long the player cannot yell for.
     * @param yellTicks
     */
    public void setYellTicks(int yellTicks) {
        this.yellTicks = yellTicks;
    }

    /**
     * Gets the aggressive delay for the player
     *
     * @return {@link Integer}
     */
    public int getAggressiveDelay() {
        return aggressiveDelay;
    }

    /**
     * Sets the aggressive timer for the player.
     *
     * @param aggressiveDelay {@link Integer} The time we want to set the timer to.
     */
    public void setAggressiveDelay(int aggressiveDelay) {
        this.aggressiveDelay = aggressiveDelay;
    }

    public void process() {
        if (player.isJailed()) {
            if (this.jailTicks == 0) {
                player.getPacketSender().sendMessage("Your jail sentence has ended! You are now free to teleport.");
            }
            this.jailTicks--;
        }

        if (player.isYellMute()) {
            if (this.yellTicks == 0) {
                player.getPacketSender().sendMessage("You yell mute has ended.  You can now yell again.");
            }
            this.yellTicks--;
        }

        if (this.aggressiveDelay > 0) {
            aggressiveDelay--;
        }
    }
}
