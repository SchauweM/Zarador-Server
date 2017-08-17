package com.zarador.model;

import com.zarador.GameSettings;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.BossSystem;
import com.zarador.world.content.combat.CombatFactory;
import com.zarador.world.content.combat.pvp.BountyHunter;
import com.zarador.world.content.minigames.impl.*;
import com.zarador.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zarador.world.entity.Entity;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class Locations {

    public static void login(Player player) {
        player.setLocation(Location.getLocation(player));
        player.getLocation().login(player);
        player.getLocation().enter(player);
    }

    public static void logout(Player player) {
        player.getLocation().logout(player);
        if (player.getRegionInstance() != null)
            player.getRegionInstance().destruct();
        if (player.getLocation() != Location.GODWARS_DUNGEON) {
            player.getLocation().leave(player);
        }
    }

    public static int PLAYERS_IN_WILD;
    public static int PLAYERS_IN_DUEL_ARENA;

    public enum Location {
        //boolean multi, boolean summonAllowed, boolean followingAllowed,
        //boolean cannonAllowed, boolean firemakingAllowed, boolean aidingAllowed
        TORMENTED_DEMONS(new int[]{2570, 2620}, new int[]{5701, 5750}, true, true, true, false, false, true) {
        },
        SHILO(new int[]{2757, 2909}, new int[]{2939, 3010}, false, true, true, false, false, true) {
        },
        CONSTRUCTION(new int[]{1880, 1951}, new int[]{5080, 5151}, false, true, true, false, true, true) {
            @Override
            public boolean canTeleport(Player player) {
                return true;
            }

            @Override
            public void login(Player player) {
                player.moveTo(new Position(3086, 3502));
            }

            @Override
            public void logout(Player player) {
                player.moveTo(new Position(3086, 3502));
            }
        },
        BANDITS(new int[]{3154, 3192}, new int[]{2961, 2999}, true, true, true, false, true, true) {
        },
        MEMBER_ZONE(new int[]{3415, 3435}, new int[]{2900, 2926}, false, true, true, false, false, true) {
        },
        VARROCK(new int[]{3167, 3272}, new int[]{3263, 3504}, false, true, true, true, true, true) {
        },
        APE_ATOLL_SKILLING(new int[]{2786, 2811}, new int[]{2769, 2808}, false, true, true, false, true, true) {
        },
        GANODERMIC_BEAST(new int[]{2230, 2280}, new int[]{3175, 3220}, true, true, true, false, true, true) {
        },
        TREASURE_ISLAND(new int[]{3010, 3075}, new int[]{2870, 2950}, true, true, true, false, true, true) {
        },
        GAMBLE(new int[]{2430, 2493}, new int[]{3055, 3139}, false, true, true, true, true, true) {
        },
        KURADALS_DUNGEON(new int[]{1595, 1673}, new int[]{5240, 5328}, true, true, true, true, false, true) {
        },
        BANK(new int[]{3090, 3099, 3089, 3090, 3248, 3258, 3179, 3191, 2944, 2948, 2942, 2948, 2944, 2950, 3008, 3019,
                3017, 3022, 3203, 3213, 3212, 3215, 3215, 3220, 3220, 3227, 3227, 3230, 3226, 3228, 3227, 3229},
                new int[]{3487, 3500, 3492, 3498, 3413, 3428, 3432, 3448, 3365, 3374, 3367, 3374, 3365, 3370, 3352,
                        3359, 3352, 3357, 3200, 3237, 3200, 3235, 3202, 3235, 3202, 3229, 3208, 3226, 3230, 3211, 3208,
                        3226},
                false, true, true, false, false, true) {
        },
        EDGEVILLE(new int[]{3073, 3134}, new int[]{3457, 3518}, false, true, true, false, false, true) {
        },
        LUMBRIDGE(new int[]{3200, 3229}, new int[]{3198, 3236}, false, true, true, true, true, true) {
        },
        DONATOR_ZONE(new int[]{2489, 2636}, new int[]{3827, 3933}, false, true, true, true, true, true) {
            @Override
            public void process(Player player) {
                if (!player.getDonatorRights().isDonator() && !player.newPlayer()) {
                    player.moveTo(new Position(3086, 3502));
                }
            }
        },
        SLASH_BASH(new int[]{2504, 2561}, new int[]{9401, 9473}, true, true, true, true, true, true) {
        },
        BANDOS_AVATAR(new int[]{2877, 2928}, new int[]{4734, 4787}, true, true, true, true, true, true) {
        },
        KALPHITE_QUEEN(new int[]{3464, 3500}, new int[]{9478, 9523}, true, true, true, true, true, true) {
        },
        PHOENIX(new int[]{2824, 2862}, new int[]{9545, 9594}, true, true, true, true, true, true) {
        },
        // BANDIT_CAMP(new int[]{3020, 3150, 3055, 3195}, new int[]{3684, 3711,
        // 2958, 3003}, true, true,
        // true, true, true, true) {
        // },
        ROCK_CRABS(new int[]{2663, 2729}, new int[]{3691, 3730}, true, true, true, true, true, true) {
        },
        BORK(new int[]{3060, 3140}, new int[]{2950, 3050}, true, true, true, false, true, true) {
        },
        ARMOURED_ZOMBIES(new int[]{3077, 3132}, new int[]{9657, 9680}, true, true, true, true, true, true) {
        },
        CORPOREAL_BEAST(new int[]{2879, 2962}, new int[]{4368, 4406}, true, true, true, false, true, true) {
        },
        DAGANNOTH_DUNGEON(new int[]{2886, 2938}, new int[]{4431, 4477}, true, true, true, false, true, true) {
        },
        JAIL(new int[]{1967, 1982}, new int[]{4997, 5013}, false, false, false, false, false, false) {
            @Override
            public void process(Player player) {
                if (player.isJailed() && player.getLocation() != Location.JAIL) {
                    player.getPacketSender().sendMessage("What are you doing out of your cell? Get back in there!");
                }
            }

            @Override
            public boolean canTeleport(Player player) {
                if (player.isJailed()) {
                    player.getPacketSender().sendMessage("You cannot teleport out of jail... What're you thinking?");
                    return false;
                }
                return true;
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                if (player.isJailed()) {
                    player.getPacketSender().sendMessage("You cannot attack players while you are jailed.");
                    player.getWalkingQueue().clear();
                    return false;
                }
                return false;
            }
        },
        // Location(int[] x, int[] y, boolean multi, boolean summonAllowed,
        // boolean followingAllowed,
        // boolean cannonAllowed, boolean firemakingAllowed, boolean
        // aidingAllowed) {
        WILDKEY_ZONE(new int[]{3352, 3390}, new int[]{3870, 3905}, true, true, true, false, true, true) {
            @Override
            public void process(Player player) {
                player.setWildernessLevel(60);
                player.getPacketSender().sendString(25352, "" + player.getWildernessLevel());
                player.getPacketSender().sendString(25355, "Levels: " + CombatFactory.getLevelDifference(player, false)
                        + " - " + CombatFactory.getLevelDifference(player, true));
                BountyHunter.process(player);
                player.getPacketSender().sendInteractionOption("Attack", 2, true);
                player.getPacketSender().sendWalkableInterface(25347);
                player.getPacketSender().sendString(19000,
                        "Combat level: " + player.getSkillManager().getCombatLevel());
                player.getUpdateFlag().flag(Flag.APPEARANCE);
            }

            @Override
            public boolean canTeleport(Player player) {
                if (player.getWildernessLevel() > 20) {
                    if (player.getStaffRights() == StaffRights.OWNER || player.getStaffRights() == StaffRights.MANAGER || player.getStaffRights() == StaffRights.ADMINISTRATOR) {
                        player.getPacketSender()
                                .sendMessage("@red@You've teleported out of deep Wilderness, logs have been written.");
                        return true;
                    }
                    player.getPacketSender().sendMessage("Teleport spells are blocked in this level of Wilderness.");
                    player.getPacketSender()
                            .sendMessage("You must be below level 20 of Wilderness to use teleportation spells.");
                    return false;
                }
                return true;
            }

            @Override
            public void login(Player player) {
                player.performGraphic(new Graphic(2000, 8));
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                int combatDifference = CombatFactory.combatLevelDifference(player.getSkillManager().getCombatLevel(),
                        target.getSkillManager().getCombatLevel());
                if (combatDifference > player.getWildernessLevel() || combatDifference > target.getWildernessLevel()) {
                    player.getPacketSender()
                            .sendMessage("Your combat level difference is too great to attack that player here.");
                    player.getWalkingQueue().clear();
                    return false;
                }
                if (target.getLocation() != Location.WILDKEY_ZONE) {
                    player.getPacketSender()
                            .sendMessage("That player cannot be attacked, because they are not in the Wilderness.");
                    player.getWalkingQueue().clear();
                    return false;
                }
                return true;
            }
        },
        // 3653
        SHREKS_HUT(new int[]{3190, 3215}, new int[]{3154, 3178}, true, true, true, false, true, true) {
            @Override
            public void enter(Player player) {
                player.getPacketSender().sendMessage("There is a presences coming from inside this shack.");
            }
        },
        // 3653
        WILDERNESS(
                new int[]{2940, 3392, 2986, 3012, 3650, 3653, 3012, 3059, 3008, 3070, 2250, 2295, 2760,
                        2805, 2830, 2885, 2505, 2550},
                new int[]{3525, 3968, 10338, 10366, 3457, 3472, 10303, 10351, 10235, 10300, 4675, 4729,
                        10120, 10180, 10105, 10150, 4760, 4795},
                false, true, true, true, true, true) {
            @Override
            public void process(Player player) {
                int x = player.getPosition().getX();
                int y = player.getPosition().getY();
                boolean ghostTown = x >= 3650 && y <= 3517;
                boolean inKBD = x >= 2250 && x <= 2295 && y >= 4675 && y <= 4729;
                boolean chaosTempleDungeon1 = x >= 2760 && x <= 2805 && y >= 10120 && y <= 10180;
                boolean chaosTempleDungeon2 = x >= 2830 && x <= 2885 && y >= 10105 && y <= 10150;
                boolean notInTown = y > 3507 && x < 3681;
                boolean inVenenatis = x >= 2505 && x <= 2550 && y >= 4760 && y <= 4795;
                boolean safeSpot = x == 3650 && y == 3472;
                if (inKBD) {
                    player.setWildernessLevel(20);
                } else if (chaosTempleDungeon1 || chaosTempleDungeon2) {
                    player.setWildernessLevel(12);
                } else if (inVenenatis) {
                    player.setWildernessLevel(15);
                } else {
                    player.setWildernessLevel(((((y > 6400 ? y - 6400 : y) - 3520) / 8) + 1));
                }
                player.getPacketSender().sendString(25352, "" + player.getWildernessLevel());
                player.getPacketSender().sendString(25355, "Levels: " + CombatFactory.getLevelDifference(player, false)
                        + " - " + CombatFactory.getLevelDifference(player, true));
                BountyHunter.process(player);
            }

            @Override
            public void leave(Player player) {
                if (player.getLocation() != this) {
                    player.getPacketSender().sendString(19000,
                            "Combat level: " + player.getSkillManager().getCombatLevel());
                    player.getUpdateFlag().flag(Flag.APPEARANCE);
                }
                PLAYERS_IN_WILD--;
            }

            @Override
            public void enter(Player player) {
                player.getPacketSender().sendInteractionOption("Attack", 2, true);
                player.getPacketSender().sendWalkableInterface(25347);
                player.getPacketSender().sendString(19000,
                        "Combat level: " + player.getSkillManager().getCombatLevel());
                player.getUpdateFlag().flag(Flag.APPEARANCE);
                PLAYERS_IN_WILD++;
            }

            @Override
            public boolean canTeleport(Player player) {
                if (player.getWildernessLevel() > 20) {
                    if (player.getStaffRights().isManagement()) {
                        player.getPacketSender()
                                .sendMessage("@red@You've teleported out of deep Wilderness, logs have been written.");
                        return true;
                    }
                    player.getPacketSender().sendMessage("Teleport spells are blocked in this level of Wilderness.");
                    player.getPacketSender()
                            .sendMessage("You must be below level 20 of Wilderness to use teleportation spells.");
                    return false;
                }
                return true;
            }

            @Override
            public void login(Player player) {
                player.performGraphic(new Graphic(2000, 8));
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                int combatDifference = CombatFactory.combatLevelDifference(player.getSkillManager().getCombatLevel(),
                        target.getSkillManager().getCombatLevel());
                if (combatDifference > player.getWildernessLevel() || combatDifference > target.getWildernessLevel()) {
                    player.getPacketSender()
                            .sendMessage("Your combat level difference is too great to attack that player here.");
                    player.getWalkingQueue().clear();
                    return false;
                }
                if (target.getLocation() != Location.WILDERNESS) {
                    player.getPacketSender()
                            .sendMessage("That player cannot be attacked, because they are not in the Wilderness.");
                    player.getWalkingQueue().clear();
                    return false;
                }
                /*
				 * if(Misc.getMinutesPlayed(player) < 20) {
				 * player.getPacketSender().sendMessage(
				 * "You must have played for at least 20 minutes in order to attack someone."
				 * ); return false; } if(Misc.getMinutesPlayed(target) < 20) {
				 * player.getPacketSender().sendMessage(
				 * "This player is a new player and can therefore not be attacked yet."
				 * ); return false; }
				 */
                return true;
            }
        },
        ZULRAH_CLOUD_ONE(new int[] { 2262, 2264 }, new int[] { 3074, 3076 }, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
				return true;
			}

			@Override
			public void logout(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}

			@Override
			public void onDeath(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}
		},
		ZULRAH_CLOUD_TWO(new int[] { 2262, 2264 }, new int[] { 3072, 3074 }, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
				return true;
			}

			@Override
			public void logout(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}

			@Override
			public void onDeath(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}
		},
		ZULRAH_CLOUD_THREE(new int[] { 2262, 2264 }, new int[] { 3069, 3071 }, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
				return true;
			}

			@Override
			public void logout(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}

			@Override
			public void onDeath(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}
		},
		ZULRAH_CLOUD_FOUR(new int[] { 2265, 2267 }, new int[] { 3068, 3070 }, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
				return true;
			}

			@Override
			public void logout(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}

			@Override
			public void onDeath(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}
		},
		ZULRAH_CLOUD_FIVE(new int[] { 2268, 2270 }, new int[] { 3068, 3070 }, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
				return true;
			}

			@Override
			public void logout(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}

			@Override
			public void onDeath(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}
		},
		ZULRAH_CLOUD_SIX(new int[] { 2271, 2273 }, new int[] { 3069, 3071 }, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
				return true;
			}

			@Override
			public void logout(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}

			@Override
			public void onDeath(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}
		},
		ZULRAH_CLOUD_SEVEN(new int[] { 2272, 2274 }, new int[] { 3072, 3074 }, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
				return true;
			}

			@Override
			public void logout(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}

			@Override
			public void onDeath(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}
		},
		ZULRAH_CLOUD_EIGHT(new int[] { 2265, 2264 }, new int[] { 3069, 3071 }, true, true, true, true, true, true) {
			@Override
			public boolean canTeleport(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
				return true;
			}

			@Override
			public void logout(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}

			@Override
			public void onDeath(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}
		},
		ZULRAH(new int[] { 2257, 2281 }, new int[] { 3063, 3083 }, true, true, true, true, true, true) {

			@Override
			public boolean canTeleport(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
				return true;
			}

			@Override
			public void logout(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}

			@Override
			public void onDeath(Player player) {
				player.getRegionInstance().getNpcsList().forEach(npc -> {
					World.deregister(npc);
				});
			}

		},
        BARROWS(new int[]{3520, 3598, 3543, 3584, 3543, 3560}, new int[]{9653, 9750, 3265, 3314, 9685, 9702},
                false, true, true, true, true, true) {
            @Override
            public void process(Player player) {
                if (player.getWalkableInterfaceId() != 37200)
                    player.getPacketSender().sendWalkableInterface(37200);
            }

            @Override
            public boolean canTeleport(Player player) {
                return true;
            }

            @Override
            public void logout(Player player) {

            }

            @Override
            public boolean handleKilledNPC(Player killer, NPC npc) {
                Barrows.killBarrowsNpc(killer, npc, true);
                return true;
            }
        },
        PEST_CONTROL_GAME(new int[]{2624, 2690}, new int[]{2550, 2619}, true, true, true, false, true, true) {
            @Override
            public void process(Player player) {
                if (player.getWalkableInterfaceId() != 21100)
                    player.getPacketSender().sendWalkableInterface(21100);
            }

            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender()
                        .sendMessage("Teleport spells are blocked on this island. Wait for the game to finish!");
                return false;
            }

            @Override
            public void leave(Player player) {
                PestControl.leave(player, true);
            }

            @Override
            public void logout(Player player) {
                PestControl.leave(player, true);
            }

            @Override
            public boolean handleKilledNPC(Player killer, NPC n) {
                return true;
            }

            @Override
            public void onDeath(Player player) {
                player.moveTo(new Position(2657, 2612, 0));
            }
        },
        PEST_CONTROL_BOAT(new int[]{2660, 2663}, new int[]{2638, 2643}, false, false, false, false, false, true) {
            @Override
            public void process(Player player) {
                if (player.getWalkableInterfaceId() != 21005)
                    player.getPacketSender().sendWalkableInterface(21005);
            }

            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender().sendMessage("You must leave the boat before teleporting.");
                return false;
            }

            @Override
            public void leave(Player player) {
                if (player.getLocation() != PEST_CONTROL_GAME) {
                    PestControl.leave(player, true);
                }
            }

            @Override
            public void logout(Player player) {
                PestControl.leave(player, true);
            }
        },
        SOULWARS(new int[]{-1, -1}, new int[]{-1, -1}, true, true, true, false, true, true) {
            @Override
            public void process(Player player) {

            }

            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender()
                        .sendMessage("If you wish to leave, you must use the portal in your team's lobby.");
                return false;
            }

            @Override
            public void logout(Player player) {

            }

            @Override
            public void onDeath(Player player) {

            }

            @Override
            public boolean handleKilledNPC(Player killer, NPC npc) {

                return false;
            }

        },
        SOULWARS_WAIT(new int[]{-1, -1}, new int[]{-1, -1}, false, false, false, false, false, true) {
            @Override
            public void process(Player player) {
            }

            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender().sendMessage("You must leave the waiting room before being able to teleport.");
                return false;
            }

            @Override
            public void logout(Player player) {
            }
        },
        FIGHT_CAVES(new int[]{2360, 2445}, new int[]{5045, 5125}, true, true, false, false, false, false) {
            @Override
            public void process(Player player) {
            }

            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender().sendMessage(
                        "Teleport spells are blocked here. If you'd like to leave, use the north-east exit.");
                return false;
            }

            @Override
            public void login(Player player) {
            }

            @Override
            public void leave(Player player) {
                player.getCombatBuilder().reset(true);
                if (player.getRegionInstance() != null) {
                    player.getRegionInstance().destruct();
                }
                player.moveTo(new Position(2439, 5169));
            }

            @Override
            public void onDeath(Player player) {
                FightCave.leaveCave(player, true);
            }

            @Override
            public boolean handleKilledNPC(Player killer, NPC npc) {
                FightCave.handleJadDeath(killer, npc);
                return true;
            }
        },
        ZULRAH_PIT(new int[]{2257, 2281}, new int[]{3063, 3084}, false, true, true, true, true, true) {
            @Override
            public void process(Player player) {
            }

            @Override
            public boolean canTeleport(Player player) {
                return true;
            }

            @Override
            public void login(Player player) {
            }

            @Override
            public void leave(Player player) {
                player.getCombatBuilder().reset(true);
                if (player.getRegionInstance() != null) {
                    player.getRegionInstance().destruct();
                }
            }

            @Override
            public void onDeath(Player player) {

            }

            @Override
            public boolean handleKilledNPC(Player killer, NPC npc) {

                return true;
            }
        },
        INSIDE_MAGE_BANK(new int[]{2525, 2555}, new int[]{4706, 4728}, true, true, true, true, true, true) {
        },
        OUTSIDE_MAGE_BANK(new int[]{3085, 3101}, new int[]{3947, 3961}, true, true, true, true, true, true) {
        },
        CERBERUS_CAVE(new int[]{1223, 1262}, new int[]{1226, 1280}, false, true, true, true, true, true) {
        },
        SKOTIZO(new int[]{2525, 2601}, new int[]{4915, 4990}, true, true, true, false, true, true) {
        },
        BOSS_SYSTEM(new int[]{2845, 2865}, new int[]{9626, 9650}, true, true, true, false, false, false) {
            @Override
            public void process(Player player) {
            }

            @Override
            public boolean canTeleport(Player player) {
                return true;
            }

            @Override
            public void login(Player player) {
            }

            @Override
            public void leave(Player player) {
                player.getCombatBuilder().reset(true);
                if (player.getRegionInstance() != null) {
                    player.getRegionInstance().destruct();
                }
                player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), 0));
            }

            @Override
            public void onDeath(Player player) {
                leave(player);
            }

            @Override
            public boolean handleKilledNPC(Player killer, NPC npc) {
                BossSystem.bossKilled(killer, npc);
                return true;
            }

        },
        GRAVEYARD(new int[]{3485, 3517}, new int[]{3559, 3580}, true, true, false, true, false, false) {
            @Override
            public void process(Player player) {
            }

            @Override
            public boolean canTeleport(Player player) {
                if (player.getMinigameAttributes().getGraveyardAttributes().hasEntered()) {
                    player.getPacketSender().sendInterfaceRemoval()
                            .sendMessage("A spell teleports you out of the graveyard.");
                    Graveyard.leave(player);
                    return false;
                }
                return true;
            }

            @Override
            public void logout(Player player) {
                if (player.getMinigameAttributes().getGraveyardAttributes().hasEntered()) {
                    Graveyard.leave(player);
                }
            }

            @Override
            public boolean handleKilledNPC(Player killer, NPC npc) {
                return killer.getMinigameAttributes().getGraveyardAttributes().hasEntered()
                        && Graveyard.handleDeath(killer, npc);
            }

            @Override
            public void onDeath(Player player) {
                Graveyard.leave(player);
            }
        },
        FIGHT_PITS(new int[]{2370, 2425}, new int[]{5133, 5167}, true, true, true, false, false, true) {
            @Override
            public void process(Player player) {
                if (FightPit.inFightPits(player)) {
                    FightPit.updateGame(player);
                    if (player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK)
                        player.getPacketSender().sendInteractionOption("Attack", 2, true);
                }
            }

            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender().sendMessage(
                        "Teleport spells are blocked here. If you'd like to leave, use the northern exit.");
                return false;
            }

            @Override
            public void logout(Player player) {
                FightPit.removePlayer(player, "leave game");
            }

            @Override
            public void leave(Player player) {
                onDeath(player);
            }

            @Override
            public void onDeath(Player player) {
                if (FightPit.getState(player) != null) {
                    FightPit.removePlayer(player, "death");
                }
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                String state1 = FightPit.getState(player);
                String state2 = FightPit.getState(target);
                return state1 != null && state1.equals("PLAYING") && state2 != null && state2.equals("PLAYING");
            }
        },
        FIGHT_PITS_WAIT_ROOM(new int[]{2393, 2404}, new int[]{5168, 5176}, false, false, false, false, false,
                true) {
            @Override
            public void process(Player player) {
                FightPit.updateWaitingRoom(player);
            }

            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender().sendMessage(
                        "Teleport spells are blocked here. If you'd like to leave, use the northern exit.");
                return false;
            }

            @Override
            public void logout(Player player) {
                FightPit.removePlayer(player, "leave room");
            }

            @Override
            public void leave(Player player) {
                if (player.getLocation() != FIGHT_PITS) {
                    FightPit.removePlayer(player, "leave room");
                }
            }

        },
        DUEL_ARENA(new int[]{3322, 3394, 3311, 3323, 3331, 3391}, new int[]{3195, 3291, 3223, 3248, 3242, 3260},
                false, false, false, false, false, false) {
            @Override
            public void process(Player player) {
                if (player.getWalkableInterfaceId() != 201)
                    player.getPacketSender().sendWalkableInterface(201);
                if (player.getDueling().duelingStatus == 0) {
                    if (player.getPlayerInteractingOption() != PlayerInteractingOption.CHALLENGE)
                        player.getPacketSender().sendInteractionOption("Challenge", 2, false);
                } else if (player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK)
                    player.getPacketSender().sendInteractionOption("Attack", 2, true);
            }

            @Override
            public void enter(Player player) {
                PLAYERS_IN_DUEL_ARENA++;
                player.getPacketSender().sendMessage(
                        "<img=4> <col=996633>Warning! Do not stake items which you are not willing to lose.");
            }

            @Override
            public boolean canTeleport(Player player) {
                if (player.getDueling().duelingStatus == 5) {
                    player.getPacketSender().sendMessage("To forfiet a duel, run to the west and use the trapdoor.");
                    return false;
                }
                return true;
            }

            @Override
            public void logout(Player player) {
                boolean dc = false;
                if (player.getDueling().inDuelScreen && player.getDueling().duelingStatus != 5) {
                    player.getDueling().declineDuel(player.getDueling().duelingWith > 0 ? true : false);
                } else if (player.getDueling().duelingStatus == 5) {
                    if (player.getDueling().duelingWith > -1) {
                        Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
                        if (duelEnemy != null) {
                            duelEnemy.getDueling().duelVictory();
                        } else {
                            dc = true;
                        }
                    }
                }
                //player.moveTo(new Position(3368, 3268));
                if (dc) {
                    World.getPlayers().remove(player);
                }
            }

            @Override
            public void leave(Player player) {
                if (player.getDueling().duelingStatus == 5) {
                    onDeath(player);
                }
                PLAYERS_IN_DUEL_ARENA--;
            }

            @Override
            public void onDeath(Player player) {
                if (player.getDueling().duelingStatus == 5) {
                    if (player.getDueling().duelingWith > -1) {
                        Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
                        if (duelEnemy != null) {
                            duelEnemy.getDueling().duelVictory();
                        }
                    }
                    player.getDueling().arenaStats[1]++;
                    player.getDueling().reset();
                }
                player.moveTo(new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3)));
                player.getDueling().reset();
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                if (target.getIndex() != player.getDueling().duelingWith) {
                    player.getPacketSender().sendMessage("That player is not your opponent!");
                    return false;
                }
                if (player.getDueling().timer != -1) {
                    player.getPacketSender().sendMessage("You cannot attack yet!");
                    return false;
                }
                return player.getDueling().duelingStatus == 5 && target.getDueling().duelingStatus == 5;
            }
        },
        GODWARS_DUNGEON(new int[]{2800, 2950, 2858, 2943}, new int[]{5200, 5400, 5180, 5230}, true, true, true,
                false, true, true) {
            @Override
            public void process(Player player) {
                int x = player.getPosition().getX();
                int y = player.getPosition().getY();
                boolean inNex = x >= 2900 && x <= 2940 && y >= 5187 && y <= 5220;
                boolean inSara = x >= 2900 && x <= 2940 && y >= 5255 && y <= 5305;
                boolean inSaraLobby = x >= 2909 && x <= 2940 && y >= 5255 && y <= 5276;
                if (player.getWalkableInterfaceId() != 16210)
                    player.getPacketSender().sendWalkableInterface(16210);
            }

            @Override
            public void enter(Player player) {

            }

            @Override
            public boolean canTeleport(Player player) {
                return true;
            }

            @Override
            public void onDeath(Player player) {
                leave(player);
            }

            @Override
            public void leave(Player p) {
                p.getMinigameAttributes().getGodwarsDungeonAttributes().setAltarDelay(0).setHasEnteredRoom(false);
            }

            @Override
            public boolean handleKilledNPC(Player killer, NPC n) {
                int index = -1;
                int npc = n.getId();
                if (npc == 6246 || npc == 6229 || npc == 6230 || npc == 6231 || npc == 6227 || npc == 6625
                        || npc == 6223 || npc == 6222) // Armadyl
                    index = 0;
                else if (npc == 102 || npc == 3583 || npc == 115 || npc == 113 || npc == 6273 || npc == 6276
                        || npc == 6277 || npc == 6288 || npc == 6265 || npc == 6263 || npc == 6261 || npc == 6260) // Bandos
                    index = 1;
                else if (npc == 6258 || npc == 6259 || npc == 6254 || npc == 6255 || npc == 6257 || npc == 6256
                        || npc == 6252 || npc == 6250 || npc == 6240 || npc == 6247) // Saradomin
                    index = 2;
                else if (npc == 10216 || npc == 6216 || npc == 1220 || npc == 6007 || npc == 6219 || npc == 6220
                        || npc == 6221 || npc == 49 || npc == 4418 || npc == 6208 || npc == 6206 || npc == 6204
                        || npc == 6203) // Zamorak
                    index = 3;
                if (index != -1) {
                    if (npc == 6221) {
                        //Double point npc
                        killer.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[2]++;
                        killer.getPacketSender().sendString((16216 + index),
                                "" + killer.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index]);
                    }
                    killer.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index]++;
                    killer.getPacketSender().sendString((16216 + index),
                            "" + killer.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index]);
                }
                return false;
            }
        },
        NOMAD(new int[]{3342, 3377}, new int[]{5839, 5877}, true, true, false, true, false, true) {
            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender().sendMessage(
                        "Teleport spells are blocked here. If you'd like to leave, use the southern exit.");
                return false;
            }

            @Override
            public void leave(Player player) {
                if (player.getRegionInstance() != null)
                    player.getRegionInstance().destruct();
                player.moveTo(new Position(1890, 3177));
                player.restart();
            }

            @Override
            public boolean handleKilledNPC(Player killer, NPC npc) {
                if (npc.getId() == 8528) {
                    Nomad.endFight(killer, true);
                    return true;
                }
                return false;
            }
        },
        RECIPE_FOR_DISASTER(new int[]{1885, 1913}, new int[]{5340, 5369}, true, true, false, false, false,
                false) {
            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender()
                        .sendMessage("Teleport spells are blocked here. If you'd like to leave, use a portal.");
                return false;
            }

            @Override
            public boolean handleKilledNPC(Player killer, NPC npc) {
                RecipeForDisaster.handleNPCDeath(killer, npc);
                return true;
            }

            @Override
            public void leave(Player player) {
                if (player.getRegionInstance() != null)
                    player.getRegionInstance().destruct();
                player.moveTo(new Position(3078, 3492));
            }

            @Override
            public void onDeath(Player player) {
                leave(player);
            }
        },
        FREE_FOR_ALL_ARENA(new int[]{2755, 2876}, new int[]{5512, 5627}, true, true, true, false, false, true) {
            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender()
                        .sendMessage("Teleport spells are blocked here, if you wish to teleport, use the portal.");
                return false;
            }

            @Override
            public void onDeath(Player player) {
                player.moveTo(new Position(2815, 5511));
                player.setConstitution(player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) + player.getEquipment().getBoost());
            }

            @Override
            public boolean canAttack(Player player, Player target) {
                if (target.getLocation() != FREE_FOR_ALL_ARENA) {
                    player.getPacketSender().sendMessage("That player has not entered the dangerous zone yet.");
                    player.getWalkingQueue().clear();
                    return false;
                }
                return true;
            }

            @Override
            public void enter(Player player) {
                if (player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK) {
                    player.getPacketSender().sendInteractionOption("Attack", 2, true);
                }
            }

        },
        FREE_FOR_ALL_WAIT(new int[]{2755, 2876}, new int[]{5507, 5627}, false, false, true, false, false, true) {
            @Override
            public boolean canTeleport(Player player) {
                player.getPacketSender()
                        .sendMessage("Teleport spells are blocked here, if you wish to teleport, use the portal.");
                return false;
            }

            @Override
            public void onDeath(Player player) {
                player.moveTo(new Position(2815, 5511));
            }
        },
        WARRIORS_GUILD(new int[]{2833, 2879}, new int[]{3531, 3559}, false, true, true, false, false, true) {
            @Override
            public void logout(Player player) {
                if (player.getMinigameAttributes().getWarriorsGuildAttributes().enteredTokenRoom()) {
                    player.moveTo(new Position(2844, 3540, 2));
                }
            }

            @Override
            public void leave(Player player) {
                player.getMinigameAttributes().getWarriorsGuildAttributes().setEnteredTokenRoom(false);
            }
        },
        PURO_PURO(new int[]{2556, 2630}, new int[]{4281, 4354}, false, true, true, false, false, true) {
        },

        /**
         * The city of Dungeoneering
         *
         * @Dungeoneering
         */
        DAEMONHEIM(new int[]{3393, 3515}, new int[]{3612, 3773}, true, false, true, false, true, true) {
            @Override
            public void leave(Player player) {
                player.getPacketSender().sendDungeoneeringTabIcon(false);
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55065);
            }
        },
        DUNGEONEERING_FLOOR_1(new int[]{3268, 3316}, new int[]{9161, 9210}, true, false, true, false, true, true) {
            @Override
            public void leave(Player player) {
                player.getPacketSender().sendDungeoneeringTabIcon(false);
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55065);
                player.getDungeoneering().getFloor().leaveFloor();
            }

            @Override
            public boolean canTeleport(Player player) {
                return false;
            }

            @Override
            public void login(Player player) {
                player.getPacketSender().sendDungeoneeringTabIcon(false);
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55065);
                player.getDungeoneering().getFloor().leaveFloor();
            }

            @Override
            public void logout(Player player) {
                player.getDungeoneering().getFloor().leaveFloor();
            }

            @Override
            public void onDeath(Player player) {
                player.getDungeoneering().addDeaths(1);
            }

            @Override
            public void process(Player player) {
                if (player.getDungeoneering().getDungeonStage() != Dungeoneering.DungeonStage.DEFAULT) {
                    if (player.getWalkableInterfaceId() != 37500) {
                        player.getPacketSender().sendWalkableInterface(37500);
                    }
                } else if (player.getWalkableInterfaceId() == 37500) {
                    player.getPacketSender().sendWalkableInterface(-1);
                }
            }

            @Override
            public boolean handleKilledNPC(Player player, NPC killedNpc) {
                if (killedNpc.getId() == player.getDungeoneering().getFloor().getBoss().getId()) {
                    player.getDungeoneering().setDungeonStage(Dungeoneering.DungeonStage.KILLED_BOSS);
                    player.getDungeoneering().getFloor().killedBoss();
                    return true;
                }
                if (player.getDungeoneering().getDungeonStage() != Dungeoneering.DungeonStage.DEFAULT) {
                    player.getDungeoneering().addKills(1);
                    player.getDungeoneering().handleMinionReward(killedNpc);
                    return true;
                }
                return false;
            }
        },
        FLESH_CRAWLERS(new int[]{2033, 2049}, new int[]{5178, 5197}, false, true, true, false, true, true) {
        },
        AGILITY_WILDERNESS_COURSE(new int[]{2989, 3009}, new int[]{3916, 3966}, false, true, true, false, false,
                false) {
        },
        EZONE_DONOR(new int[]{3342, 3385}, new int[]{9618, 9661}, false, true, true, false, false, false) {
        },
        MONSTER_DONOR(new int[]{2306, 2360}, new int[]{9769, 9918}, true, true, true, false, false, false) {
            @Override
            public void process(Player player) {
                if (!player.getDonatorRights().isDonator() && !player.newPlayer()) {
                    player.moveTo(new Position(3086, 3502));
                }
            }
        },
        KRAKEN(new int[]{3680, 3712}, new int[]{5791, 5824}, true, false, true, false, false, true) {
            @Override
            public boolean canTeleport(Player player) {
                player.resetKraken();
                return true;
            }

            @Override
            public void logout(Player player) {
                player.resetKraken();
            }

            @Override
            public void onDeath(Player player) {
                player.resetKraken();
            }

            @Override
            public void leave(Player player) {
                player.resetKraken();
            }
        },
        DEFAULT(null, null, false, true, true, true, true, true) {
        };

        Location(int[] x, int[] y, boolean multi, boolean summonAllowed, boolean followingAllowed,
                 boolean cannonAllowed, boolean firemakingAllowed, boolean aidingAllowed) {
            this.x = x;
            this.y = y;
            this.multi = multi;
            this.summonAllowed = summonAllowed;
            this.followingAllowed = followingAllowed;
            this.cannonAllowed = cannonAllowed;
            this.firemakingAllowed = firemakingAllowed;
            this.aidingAllowed = aidingAllowed;
        }

        private int[] x, y;
        private boolean multi;
        private boolean summonAllowed;
        private boolean followingAllowed;
        private boolean cannonAllowed;
        private boolean firemakingAllowed;
        private boolean aidingAllowed;

        public int[] getX() {
            return x;
        }

        public int[] getY() {
            return y;
        }

        public static boolean inMulti(Character gc) {
            int x = gc.getPosition().getX(), y = gc.getPosition().getY();
            boolean agilityWild = x >= 2989 && x <= 3009 && y >= 3916 && y <= 3966;
            if (gc.getLocation() == WILDERNESS) {
                if (x >= 3250 && x <= 3302 && y >= 3905 && y <= 3925 || x >= 3020 && x <= 3055 && y >= 3684 && y <= 3711
                        || x >= 3150 && x <= 3195 && y >= 2958 && y <= 3003
                        || x >= 3645 && x <= 3715 && y >= 3454 && y <= 3550
                        || x > 3193 && x < 3279 && y > 9272 && y < 9343
                        || x >= 3136 && x <= 3327 && y >= 3519 && y <= 3607
                        || x >= 3125 && x <= 3327 && y >= 3648 && y <= 3860
                        || x >= 3200 && x <= 3390 && y >= 3840 && y <= 3967
                        || x >= 2992 && x <= 3007 && y >= 3912 && y <= 3967 && !agilityWild
                        || x >= 2946 && x <= 2959 && y >= 3816 && y <= 3831
                        || x >= 3008 && x <= 3199 && y >= 3856 && y <= 3903
                        || x >= 3008 && x <= 3071 && y >= 3600 && y <= 3711
                        || x >= 3072 && x <= 3327 && y >= 3608 && y <= 3647
                        || x >= 2624 && x <= 2690 && y >= 2550 && y <= 2619
                        || x >= 2371 && x <= 2422 && y >= 5062 && y <= 5117
                        || x >= 2896 && x <= 2927 && y >= 3595 && y <= 3630
                        || x >= 2892 && x <= 2932 && y >= 4435 && y <= 4464
                        || x >= 2256 && x <= 2287 && y >= 4680 && y <= 4711
                        || x >= 2250 && x <= 2295 && y >= 4675 && y <= 4729 // in
                        // kbd
                        || x >= 2516 && x <= 2595 && y >= 4926 && y <= 5003 // gwd
                        || x >= 3008 && x <= 3070 && y >= 10235 && y <= 10300 // kbd
                        // poison
                        // spiders
                        || x >= 2560 && x <= 2630 && y >= 5710 && y <= 5753
                        || x >= 2505 && x <= 2550 && y >= 4760 && y <= 4795)
                    return true;
            } else {
                if (gc.getLocation() != WILDERNESS) {
                    // rock crabs
                    if (x >= 2656 && x <= 2732 && y >= 3711 && y <= 3742
                            || x >= 2885 && x <= 2920 && y >= 4375 && y <= 4415
                            //tds
                            || x >= 2570 && x <= 2620 && y >= 5701 && y <= 5750) {
                        return true;
                    }
                }
            }
            return gc.getLocation().multi;
        }

        public boolean isSummoningAllowed() {
            return summonAllowed;
        }

        public boolean isFollowingAllowed() {
            return followingAllowed;
        }

        public boolean isCannonAllowed() {
            return cannonAllowed;
        }

        public boolean isFiremakingAllowed() {
            return firemakingAllowed;
        }

        public boolean isAidingAllowed() {
            return aidingAllowed;
        }

        public static Location getLocation(Entity gc) {
            for (Location location : Location.values()) {
                if (location != Location.DEFAULT)
                    if (inLocation(gc, location))
                        return location;
            }
            return Location.DEFAULT;
        }

        public static boolean inLocation(Entity gc, Location location) {
            if (location == Location.DEFAULT) {
                if (getLocation(gc) == Location.DEFAULT)
                    return true;
                else
                    return false;
            }
			/*
			 * if(gc instanceof Player) { Player p = (Player)gc; if(location ==
			 * Location.TRAWLER_GAME) { String state =
			 * FishingTrawler.getState(p); return (state != null &&
			 * state.equals("PLAYING")); } else if(location ==
			 * FIGHT_PITS_WAIT_ROOM || location == FIGHT_PITS) { String state =
			 * FightPit.getState(p), needed = (location == FIGHT_PITS_WAIT_ROOM)
			 * ? "WAITING" : "PLAYING"; return (state != null &&
			 * state.equals(needed)); } else if(location == Location.SOULWARS) {
			 * return (SoulWars.redTeam.contains(p) ||
			 * SoulWars.blueTeam.contains(p) && SoulWars.gameRunning); } else
			 * if(location == Location.SOULWARS_WAIT) { return
			 * SoulWars.isWithin(SoulWars.BLUE_LOBBY, p) ||
			 * SoulWars.isWithin(SoulWars.RED_LOBBY, p); } }
			 */
            return inLocation(gc.getPosition().getX(), gc.getPosition().getY(), location);
        }

        public static boolean inLocation(int x, int y, Location location) {
            int checks = location.getX().length - 1;
            for (int i = 0; i <= checks; i += 2) {
                if (x >= location.getX()[i] && x <= location.getX()[i + 1]) {
                    if (y >= location.getY()[i] && y <= location.getY()[i + 1]) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void process(Player player) {

        }

        public boolean canTeleport(Player player) {
            return true;
        }

        public void login(Player player) {

        }

        public void enter(Player player) {

        }

        public void leave(Player player) {

        }

        public void logout(Player player) {

        }

        public void onDeath(Player player) {

        }

        public boolean handleKilledNPC(Player killer, NPC npc) {
            return false;
        }

        public boolean canAttack(Player player, Player target) {
            return false;
        }

        /**
         * SHOULD AN ENTITY FOLLOW ANOTHER ENTITY NO MATTER THE DISTANCE BETWEEN
         * THEM?
         **/
        public static boolean ignoreFollowDistance(Character character) {
            Location location = character.getLocation();
            return location == Location.FIGHT_CAVES || location == Location.RECIPE_FOR_DISASTER
                    || location == Location.NOMAD;
        }
    }

    public static void process(Character gc) {
        Location newLocation = Location.getLocation(gc);
        if (gc.getLocation() == newLocation) {
            if (gc.isPlayer()) {
                Player player = (Player) gc;
                gc.getLocation().process(player);
                if (Location.inMulti(player)) {
                    if (player.getMultiIcon() != 1) {
                        player.getPacketSender().sendMultiIcon(1);
                    }
                } else if (player.getMultiIcon() == 1) {
                    player.getPacketSender().sendMultiIcon(0);
                }
            }
        } else {
            Location prev = gc.getLocation();
            if (gc.isPlayer()) {
                Player player = (Player) gc;
                if (player.getMultiIcon() > 0)
                    player.getPacketSender().sendMultiIcon(0);
                if (player.getWalkableInterfaceId() > 0 && player.getWalkableInterfaceId() != 37400
                        && player.getWalkableInterfaceId() != 50000)
                    player.getPacketSender().sendWalkableInterface(-1);
                if (player.getPlayerInteractingOption() != PlayerInteractingOption.NONE)
                    player.getPacketSender().sendInteractionOption("null", 2, true);
            }
            gc.setLocation(newLocation);
            if (gc.isPlayer()) {
                prev.leave(((Player) gc));
                gc.getLocation().enter(((Player) gc));
            }
        }
    }

    public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
        if (playerX == objectX && playerY == objectY)
            return true;
        for (int i = 0; i <= distance; i++) {
            for (int j = 0; j <= distance; j++) {
                if ((objectX + i) == playerX
                        && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } else if ((objectX - i) == playerX
                        && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                } else if (objectX == playerX
                        && ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean goodDistance(Position pos1, Position pos2, int distanceReq) {
        if (pos1.getZ() != pos2.getZ())
            return false;
        return goodDistance(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY(), distanceReq);
    }

    public static int distanceTo(Position position, Position destination, int size) {
        final int x = position.getX();
        final int y = position.getY();
        final int otherX = destination.getX();
        final int otherY = destination.getY();
        int distX, distY;
        if (x < otherX)
            distX = otherX - x;
        else if (x > otherX + size)
            distX = x - (otherX + size);
        else
            distX = 0;
        if (y < otherY)
            distY = otherY - y;
        else if (y > otherY + size)
            distY = y - (otherY + size);
        else
            distY = 0;
        if (distX == distY)
            return distX + 1;
        return distX > distY ? distX : distY;
    }
}
