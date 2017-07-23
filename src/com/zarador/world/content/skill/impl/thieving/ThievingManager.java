package com.zarador.world.content.skill.impl.thieving;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.*;
import com.zarador.util.Misc;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.entity.impl.player.Player;

public class ThievingManager {

    private enum MobData {

        MAN(new int[] {1, 2, 3, 4}, 1, new int[] {995}, 30, 8),
        FARMER(new int[] {7}, 10, new int[] {995, 313, 314}, 15, 14.5),
        HAM_FEMALE(new int[] {1715}, 15, new int[] {4298, 4300, 4302, 4304, 4308, 4310}, 1, 18.5),
        HAM_MALE(new int[] {1714}, 20, new int[] {4298, 4300, 4302, 4304, 4308, 4310}, 1, 22.5),
        ALKHARID_WARRIOR(new int[] {18}, 25, new int[] {995}, 100, 26),
        MASTER_FARMER(new int[] {2234}, 38, new int[] {5291, 5291, 5291, 5293, 5293, 5294, 5294, 5294, 5294, 5295, 5296, 5297, 5298, 5300, 5303, 5304, 5291, 5291, 5291, 5291, 5293, 5294, 5294, 5291, 5291, 5292, 5292, 5292}, Misc.random(1, 4), 43),
        ARDOUGNE_KNIGHT(new int[] {26, 23}, 55, new int[] {995}, Misc.getRandom(100), 84.3),
        BANDIT(new int[] {1880}, 53, new int[] {995}, Misc.getRandom(5000), 79.5),
        FREMENNIK_CITZEN(new int[] {1307, 1305, 1306, 1311, 1310, 1308, 1314}, 45, new int[] {995}, Misc.getRandom(200), 65),
        GUARD(new int[] {9, 32}, 40, new int[] {995}, 150, 46.8),
        HERO(new int[] {21}, 80, new int[] {995}, Misc.getRandom(800), 275),
        PALADIN(new int[] {20}, 70, new int[] {995}, Misc.getRandom(500), 151.75),
        VILLAGER(new int[] {1888}, 45, new int[] {995}, Misc.getRandom(2000), 65),
        MASTER_FISHER(new int[] {308}, 72, new int[] {25, 313, 378, 372, 384, 15271, 3143}, Misc.getRandom(5), 165),
        WATCHMAN(new int[] {34}, 65, new int[] {995}, Misc.getRandom(50), 138);

        private final int[] items;
        private final int[] npcId;
        private final int requirements;
        private final int amount;
        private final double xp;

        MobData(int[] npcId, int requirements, int[] items, int amount, double xp) {
            this.npcId = npcId;
            this.requirements = requirements;
            this.items = items;
            this.amount = amount;
            this.xp = xp;
        }

        public int getAmount() {
            return amount;
        }

        public int[] getItems() {
            return items;
        }

        public int[] getNpcId() {
            return npcId;
        }

        public int getRequirements() {
            return requirements;
        }

        public double getXP() {
            return xp;
        }
    }

    public static MobData forMobData(int mobId) {
        for (MobData mobData : MobData.values()) {
            for (int id : mobData.getNpcId()) {
                if (id == mobId) {
                    if (mobData != null) {
                        return mobData;
                    }
                }
            }
        }

        return null;
    }

    public static void initMobData(Player player, MobData data) {
        if (player.getInventory().getFreeSlots() == 0) {
            return;
        }
        if (player.getSkillManager().getCurrentLevel(Skill.THIEVING) < data.getRequirements()) {
            return;
        }

        if (player.getSkillManager().getCurrentLevel(Skill.THIEVING) >= data.getRequirements()) {
            if (player.busy()) {
                return;
            }
            if ((System.currentTimeMillis() - player.lastThieve) < 1500) {
                return;
            }
            player.lastThieve = System.currentTimeMillis();
            TaskManager.submit(new Task() {

                int cycle = 0;
                int chance = (2 * player.getSkillManager().getCurrentLevel(Skill.THIEVING)) - data.getRequirements();
                boolean failed = false;

                @Override
                public void execute() {
                    if (player == null) {
                        stop();
                        return;
                    }

                    if (cycle == 0) {
                        if (Misc.getRandom(chance) == 1) {
                            player.getPacketSender().sendMessage("You have been stunned.");
                            player.performGraphic(new Graphic(80));
                            player.performAnimation(new Animation(65535));
                            player.dealDamage(null, new Hit(Misc.random(10, 50), Hitmask.LIGHT_YELLOW, CombatIcon.NONE));
                            failed = true;
                        } else {
                            player.getSkillManager().addSkillExperience(Skill.THIEVING, data.getXP());
                            player.performAnimation(new Animation(881));
                            player.getInventory().add(new Item(data.getItems()[Misc.getRandom(data.getItems().length - 1)], Misc.getRandom(data.getAmount())));
                            player.getPacketSender().sendMessage("You manage to steal some loot.");
                            Achievements.doProgress(player, Achievements.AchievementData.PICK_POCKET_150_TIMES);
                        }
                    } else if (cycle == (failed ? 5 : 3)) {
                        stop();
                    }

                    cycle++;
                }
            });
        }
    }

}
