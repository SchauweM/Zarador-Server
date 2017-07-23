package com.zarador.world.content.skill.impl.thieving;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.zarador.model.Animation;
import com.zarador.model.Item;
import com.zarador.model.Skill;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.entity.impl.player.Player;

public class Thieving {
    private final Player player;

    public Thieving(Player player) {
        this.player = Objects.requireNonNull(player);
    }

    public boolean stealFromStall(ThievingStall stall) {
        if (stall == null) {
            return false;
        } else if ((System.currentTimeMillis() - player.lastThieve) < 2500) {
            return false;
        } else if (player.getInventory().getFreeSlots() == 0) {
            player.getPacketSender().sendMessage("You do not have enough inventory space to steal from this stall!");
            return false;
        } else if (player.getSkillManager().getMaxLevel(Skill.THIEVING) < stall.getRequiredLevel()) {
            player.getPacketSender().sendMessage("You need a thieving level of " + stall.getRequiredLevel() + " to steal from this stall.");
            return false;
        }

        player.getSkillManager().addSkillExperience(Skill.THIEVING, stall.getExperience());
        player.lastThieve = System.currentTimeMillis();
        player.performAnimation(new Animation(832));

        // Achievement call
        //if(stall == ThievingStall.SCIMITAR_STALL) {
            Achievements.doProgress(player, Achievements.AchievementData.STEAL_2000_TIMES);
       // }

        // Loot generation code
        List<WeightedItem> weightedItems = Arrays.asList(stall.getStallRewards().getRewards());
        int hitSlot = Misc.getRandom(stall.getStallRewards().getWeightSum());
        int weight = 0;

        for (WeightedItem reward : weightedItems) {
            weight += reward.getWeight();

            if (weight > hitSlot) {
                int randomAmount = Misc.inclusiveRandom(1, reward.getAmount());
                sendStealNotification(reward.getId(), randomAmount);
                if(reward.getId() == 995) {
                    player.getInventory().add((reward.getId()), randomAmount);
                } else {
                    player.getInventory().add((reward.getId()), reward.getId() == 995 ? reward.getAmount() : randomAmount);
                }
                return true;
            }
        }

        player.getInventory().add(new Item(1, 1));


        return false;
    }

    private void sendStealNotification(int id, int amount) {
        ItemDefinition definition = ItemDefinition.forId(id);
        String name = definition.getName().toLowerCase();
        StringBuilder bldr = new StringBuilder("You steal ");

        if (amount == 1) {
            if (name.endsWith("s"))
                bldr.append("a pair of");
            else if (Misc.isVowel(name.charAt(0)))
                bldr.append("an");
            else
                bldr.append("some");
        } else if (amount > 1)
            bldr.append("some");
        else
            throw new AssertionError("negative item amount: " + amount);

        bldr.append(' ').append(name);

        if (amount > 1 && !name.endsWith("s"))
            bldr.append('s');

        player.getPacketSender().sendMessage(bldr.append(".").toString());
    }

}
