package com.zarador.net.packet.impl;

import org.scripts.kotlin.content.dialog.*;
import org.scripts.kotlin.content.dialog.Well.DonateToWellDial;
import org.scripts.kotlin.content.dialog.Well.Well;
import org.scripts.kotlin.content.dialog.npcs.SmallSacks;
import org.scripts.kotlin.content.dialog.teleports.EdgevilleCoffins;

import com.zarador.GameSettings;
import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.engine.task.impl.WalkToTask;
import com.zarador.engine.task.impl.WalkToTask.FinalizedMovementTask;
import com.zarador.model.*;
import com.zarador.model.Locations.Location;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.GameObjectDefinition;
import com.zarador.model.input.impl.EnterAmountOfLogsToAdd;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.util.Misc;
import com.zarador.world.ChaosTunnelHandler;
import com.zarador.world.World;
import com.zarador.world.content.*;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.chests.CrystalChest;
import com.zarador.world.content.chests.TreasureChest;
import com.zarador.world.content.chests.WarChest;
import com.zarador.world.content.combat.magic.Autocasting;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.content.combat.range.DwarfMultiCannon;
import com.zarador.world.content.combat.weapon.CombatSpecial;
import com.zarador.world.content.diversions.hourly.ShootingStar;
import com.zarador.world.content.minigames.impl.*;
import com.zarador.world.content.minigames.impl.Dueling.DuelRule;
import com.zarador.world.content.pos.PlayerOwnedShops;
import com.zarador.world.content.skill.Enchanting;
import com.zarador.world.content.skill.impl.agility.Agility;
import com.zarador.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.zarador.world.content.skill.impl.farming.Farming;
import com.zarador.world.content.skill.impl.fishing.Fishing;
import com.zarador.world.content.skill.impl.fishing.Fishing.Spot;
import com.zarador.world.content.skill.impl.hunter.Hunter;
import com.zarador.world.content.skill.impl.hunter.PuroPuro;
import com.zarador.world.content.skill.impl.mining.Mining;
import com.zarador.world.content.skill.impl.mining.MiningData;
import com.zarador.world.content.skill.impl.mining.Prospecting;
import com.zarador.world.content.skill.impl.runecrafting.Runecrafting;
import com.zarador.world.content.skill.impl.runecrafting.RunecraftingData;
import com.zarador.world.content.skill.impl.smithing.EquipmentMaking;
import com.zarador.world.content.skill.impl.smithing.Smelting;
import com.zarador.world.content.skill.impl.summoning.BossPets;
import com.zarador.world.content.skill.impl.thieving.ThievingStall;
import com.zarador.world.content.skill.impl.woodcutting.Woodcutting;
import com.zarador.world.content.skill.impl.woodcutting.WoodcuttingData;
import com.zarador.world.content.skill.impl.woodcutting.WoodcuttingData.Hatchet;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.content.transportation.TeleportType;
import com.zarador.world.doors.DoorManager;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * This packet listener is called when a player clicked on a game object.
 *
 * @author relex lawl
 */

public class ObjectActionPacketListener implements PacketListener {

    /**
     * The PacketListener logger to debug information and print out errors.
     */
    // private final static Logger logger =
    // Logger.getLogger(ObjectActionPacketListener.class);
    private static void firstClick(final Player player, Packet packet) {
        final int x = packet.readLEShortA();
        final int id = packet.readUnsignedShort();
        final int y = packet.readUnsignedShortA();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (id > 0 && id != 2465 && id != 5959 && !World.objectExists(gameObject)) {
            player.getPacketSender().sendMessage("Something has gone wrong, please report this! " + x + ", " + y + ", id: " + id + ".");
            return;
        }
        int distanceX = (player.getPosition().getX() - position.getX());
        int distanceY = (player.getPosition().getY() - position.getY());
        if (distanceX < 0)
            distanceX = -(distanceX);
        if (distanceY < 0)
            distanceY = -(distanceY);
        int size = distanceX > distanceY ? GameObjectDefinition.forId(id).getSizeX()
                : GameObjectDefinition.forId(id).getSizeY();

        if (size <= 0)
            size = 1;
        gameObject.setSize(size);
        if (player.getWalkingQueue().isLockMovement())
            return;

        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in ObjectActionPacketListener: " + id + " -
            // FIRST_CLICK_OPCODE");
        }

        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender().sendConsoleMessage(
                    "First click object id; [id, position, direction] : [" + id + ", " + gameObject.getRotation() + ", " + position.toString() + "]");

        if (!player.getDragonSpear().elapsed(3000)) {
            player.getPacketSender().sendMessage("You are stunned!");
            return;
        }
        //Fixes barb agility course exploit
        if (gameObject.getId() == 2282) {
            position.set(2551, 3554, 0);
        }
        if(gameObject != null) {
            if(gameObject.getDefinition() != null) {
                if(gameObject.getDefinition().getName() != null) {
                    if (gameObject.getDefinition().getName().equalsIgnoreCase("ladder") || gameObject.getDefinition().getName().equalsIgnoreCase("staircase")) {
                        if (player.isFrozen()) {
                            player.getPacketSender().sendMessage("You can't use this ladder because you are frozen.");
                            return;
                        }
                    }
                }
            }
        }
        player.setInteractingObject(gameObject)
                .setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
                    @Override
                    public void execute() {
                        if(gameObject.getId() != 4767) {
                            player.setPositionToFace(gameObject.getPosition());
                        }
                        if (WoodcuttingData.Trees.forId(id) != null) {
                            Woodcutting.cutWood(player, gameObject, false);
                            return;
                        }
                        if (MiningData.forRock(gameObject.getId()) != null) {
                            Mining.startMining(player, gameObject, null);
                            return;
                        }
                        if (Farming.isGameObject(player, gameObject, 1))
                            return;
                        if (DoorManager.isDoor(gameObject)) {
//							if(gameObject.getName().toLowerCase().contains("gate")) {
//								Region region = World.loadRegion(x, y);
//								int positionX = gameObject.getPosition().getX();
//								int positionY = gameObject.getPosition().getY();
//								GameObject doubleDoor = region.gateAtPosition("gate", positionX, positionY);
//								if(doubleDoor != null) {
//									DoorManager.isDoor(doubleDoor);
//								}
//							}
                            return;
                        }
                        if (Runecrafting.runecraftingAltar(player, gameObject.getId())) {
                            RunecraftingData.RuneData rune = RunecraftingData.RuneData.forId(gameObject.getId());
                            if (rune == null)
                                return;
                            Runecrafting.craftRunes(player, rune);
                            return;
                        }
                        if (Agility.handleObject(player, gameObject)) {
                            return;
                        }
                        if (Barrows.handleObject(player, gameObject)) {
                            return;
                        }
                        if (ChaosTunnelHandler.handleObjects(player, gameObject)) {
                            return;
                        }
                        if (player.getLocation() == Location.WILDERNESS
                                && WildernessObelisks.handleObelisk(gameObject.getId())) {
                            return;
                        }
                        switch (id) {
                            //camelot stairs up
                            case 1725:
                                player.moveTo(new Position(player.getPosition().getX(), 3513, 1));
                                break;
                            //camelot stairs down
                            case 1726:
                                player.moveTo(new Position(player.getPosition().getX(), 3509, 0));
                                break;
                            case 1747: // camelot ladder up
                                player.performAnimation(new Animation(828));
                                player.delayedMoveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()+1), 2);
                                break;
                            case 1746: // camelot ladder down
                                player.performAnimation(new Animation(827));
                                player.delayedMoveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ()-1), 2);
                                break;
                            //Crystal Ball (Bolt Enchanting)
                            case 589:
                                Enchanting.update_interface(player);
                            break;
                            case 30205:
                                player.getDialog().sendDialog(new ChooseScoreboard(player));
                                break;
                            case 28089:
                                if (!GameSettings.POS_ENABLED) {
                                    player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                                    return;
                                }
                                if (player.getGameModeAssistant().isIronMan()) {
                                    player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
                                    return;
                                }
                                PlayerOwnedShops.openItemSearch(player, true);
                                //player.setPlayerOwnedShopping(true);
                                break;

                            //Kuradels dungeon
                            case 47236:
                                break;

                            //Construction portal
                            case 15477:

                                break;
                            //rune ess
                            case 2491:
                                if(player.getInteractingObject() != null) {
                                    if (player.getSkillManager().getMaxLevel(Skill.MINING) <= 21) {
                                        Mining.startMining(player, new GameObject(24444, player.getInteractingObject().getPosition()), null);
                                    } else {
                                        Mining.startMining(player, new GameObject(24445, player.getInteractingObject().getPosition()), null);
                                    }
                                }
                                break;
                            case 47758:
                            case 47760:
                            case 47762:
                                //Presents.pickUpPresent(player, gameObject);
                                break;
                            case 2492:
                                player.moveTo(new Position(3253, 3400, 0));
                                break;
                            case 2477:
                                if(player.getDungeoneering().getDungeonStage() == Dungeoneering.DungeonStage.KILLED_BOSS) {
                                    player.getDungeoneering().getFloor().completeFloor();
                                } else {
                                    player.getPacketSender().sendMessage("You are not quite done with your dungeon yet!");
                                }
                                break;
                            case 38660:
                            case 38661:
                            case 38662:
                            case 38663:
                            case 38664:
                            case 38665:
                            case 38666:
                            case 38667:
                            case 38668:
                                ShootingStar.getInstance().hasMenuAction(player, 1);
                                break;
                            //Dungeoneering
                            case 48496:
                                player.getDialog().sendDialog(new EnterDungeon(player));
                                break;
                            case 6645:
                                if(player.getDungeoneering().getDungeonStage() == Dungeoneering.DungeonStage.DEFAULT) {
                                    player.getPacketSender().sendMessage("You are currently not dungeoneering.");
                                    return;
                                }
                                player.getDialog().sendDialog(new LeaveDungeon(player));
                                break;
                            case 52847:
                                if(player.getPosition().getY() >= 3725) {
                                    player.getPacketSender().sendMessage("You are currently not dungeoneering.");
                                    return;
                                }
                                player.getDungeoneering().leaveExitRoom();
                                break;
                            case 6643:
                                if(!player.getDungeoneering().hasRequiredKills()) {
                                    player.getPacketSender().sendMessage("You need atleast "+player.getDungeoneering().getFloor().getRequiredKills()+" kills to fight the boss.");
                                    return;
                                }
                                player.getDungeoneering().getFloor().fightBoss(gameObject);
                                break;
                            //corp lair
                            case 37929:
                                player.performAnimation(new Animation(844));
                                TaskManager.submit(new Task(1, player, true) {
                                    int tick = 1;

                                    @Override
                                    public void execute() {
                                        tick++;
                                        if (tick == 3) {
                                            stop();
                                        }
                                    }

                                    @Override
                                    public void stop() {
                                        setEventRunning(false);
                                        if (player.getPosition().getX() < 2919) {
                                            player.moveTo(new Position(2921, 4384, 0));
                                            player.setDirection(Direction.EAST);
                                            player.getPacketSender().sendMessage("You have awoken the beast.");
                                        } else {
                                            player.moveTo(new Position(2917, 4384, 0));
                                            player.setDirection(Direction.WEST);
                                        }
                                    }
                                });
                                break;
                            //runecrafting portals
                            case 2465:
                            case 2466:
                            case 2467:
                            case 2468:
                            case 2469:
                            case 2470:
                            case 2474:
                            case 2475:
                                player.moveTo(new Position(3093, 3496, 0));
                                break;
                            //Giant Crystal
                            case 62:
                                Emotes.doEmote(player, 666);
                                TaskManager.submit(new Task(1, player, true) {
                                    int tick = 1;

                                    @Override
                                    public void execute() {
                                        tick++;
                                        if (tick == 7) {
                                            player.performGraphic(new Graphic(191));
                                        }
                                        if (tick == 8) {
                                            stop();
                                        }
                                    }

                                    @Override
                                    public void stop() {
                                        setEventRunning(false);
                                        player.moveTo(new Position(2595, 4772, 0));
                                        player.getPacketSender().sendMessage("<img=4> To get started with Runecrafting, buy a talisman and use the locate option on it.");
                                    }
                                });
                                break;
                            //Well of Goodness
                            case 884:
                                player.setNpcClickId(945);
                                player.getDialog().sendDialog(new Well(player));
                                break;
                            //Edgeville coffins
                            case 398:
                                player.setNpcClickId(945);
                                player.getDialog().sendDialog(new EdgevilleCoffins(player));
                                break;
                            //Bounty hunter caves
                            case 28119:
                                player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY() - 11, 0));
                                break;
                            case 28121:
                                player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY() + 11, 0));
                                break;
                            //Bount hunter portal
                            case 42219:
                                player.setNpcClickId(945);
                                player.getDialog().sendDialog(new BountyPortal(player));
                                break;

                            case 2406:
                                if (!player.getClickDelay().elapsed(3000))
                                    return;
                                player.getPacketSender().sendMessage(
                                        "kicks: " + player.getMinigameAttributes().getShrek1Attributes().getDoorKicks());
                                if (player.getMinigameAttributes().getShrek1Attributes().getQuestParts() == 1) {
                                    if (player.getMinigameAttributes().getShrek1Attributes().getDoorKicks() == 0) {
                                        player.setDirection(Direction.EAST);
                                        player.performAnimation(new Animation(2555));
                                        player.getPacketSender()
                                                .sendMessage("You hear a very intimidating voice from inside yelling.");
                                        player.getMinigameAttributes().getShrek1Attributes().setDoorKicks(1);
                                    } else if (player.getMinigameAttributes().getShrek1Attributes().getDoorKicks() == 1) {
                                        player.setDirection(Direction.EAST);
                                        player.performAnimation(new Animation(2555));
                                        // spawn shrek
                                        TaskManager.submit(new Task(2, player, false) {
                                            @Override
                                            public void execute() {
                                                NPC n = new NPC(5872, new Position(3201, 3169, player.getPosition().getZ()))
                                                        .setSpawnedFor(player);
                                                World.register(n);
                                                n.getCombatBuilder().attack(player);
                                                stop();
                                            }
                                        });
                                        // player.getMinigameAttributes().getShrek1Attributes().setDoorKicks(2);
                                    }
                                } else {
                                    player.getPacketSender().sendMessage("Nothing interesting happens.");
                                }
                                // ok so player kicks door once - shrek says leave
                                // dont come back or else...
                                // if player kicks door again he spawns - they fight
                                // - 100% drop ogres head
                                player.getClickDelay().reset();
                                break;
                            case 9299:
                                if (player.getSkillManager().getMaxLevel(Skill.AGILITY) < 50) {
                                    player.getPacketSender()
                                            .sendMessage("You need an agility of 50 or higher to use this shortcut.");
                                    return;
                                }
                                if (player.getPosition().getY() > 3190) {
                                    player.setDirection(Direction.SOUTH);
                                    player.performAnimation(new Animation(2240));
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            tick++;
                                            if (tick == 4) {
                                                stop();
                                            }
                                        }

                                        @Override
                                        public void stop() {
                                            setEventRunning(false);
                                            player.moveTo(new Position(3240, 3190, 0));
                                            player.getPacketSender().sendMessage("You squeeze through the fence.");
                                        }
                                    });
                                } else {
                                    player.setDirection(Direction.NORTH);
                                    player.performAnimation(new Animation(2240));
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            tick++;
                                            if (tick == 4) {
                                                stop();
                                            }
                                        }

                                        @Override
                                        public void stop() {
                                            setEventRunning(false);
                                            player.moveTo(new Position(3240, 3191, 0));
                                            player.getPacketSender().sendMessage("You squeeze through the fence.");
                                        }
                                    });
                                }
                                break;
                            case 23271:
                                if (player.getPosition().getY() == 3520 || player.getPosition().getY() == 3519) {
                                    player.performAnimation(new Animation(6132));
                                    TaskManager.submit(new Task(1, player, false) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            if (tick == 3) {
                                                player.moveTo(new Position(player.getPosition().getX(), 3523, 0));
                                                stop();
                                            }
                                            tick++;
                                        }

                                        @Override
                                        public void stop() {

                                        }

                                    });
                                } else if (player.getPosition().getY() == 3523 || player.getPosition().getY() == 3524) {
                                    player.performAnimation(new Animation(6132));
                                    TaskManager.submit(new Task(1, player, false) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            if (tick == 3) {
                                                player.moveTo(new Position(player.getPosition().getX(), 3520, 0));
                                                stop();
                                            }
                                            tick++;
                                        }

                                        @Override
                                        public void stop() {
                                        }
                                    });
                                }
                                break;
                            case 21772:
                                player.moveTo(new Position(3236, 3458, 0));
                                break;
                            case 10309:
                                player.performAnimation(new Animation(828));
                                TaskManager.submit(new Task(1, player, true) {
                                    int tick = 1;

                                    @Override
                                    public void execute() {
                                        tick++;
                                        if (tick == 4) {
                                            stop();
                                        }
                                    }

                                    @Override
                                    public void stop() {
                                        setEventRunning(false);
                                        player.moveTo(new Position(2658, 3492, 0));
                                    }
                                });
                                break;
                            case 1754:
                                player.performAnimation(new Animation(827));
                                TaskManager.submit(new Task(1, player, true) {
                                    int tick = 1;

                                    @Override
                                    public void execute() {
                                        tick++;
                                        if (tick == 4) {
                                            stop();
                                        }
                                    }

                                    @Override
                                    public void stop() {
                                        setEventRunning(false);
                                        player.moveTo(new Position(2962, 9650, 0));
                                    }
                                });
                                break;
                            case 1734:
                                if (player.getPosition().getX() == 3045 && player.getPosition().getY() == 10323) {
                                    player.moveTo(new Position(3045, 3927, 0));
                                } else if (player.getPosition().getX() == 3044 && player.getPosition().getY() == 10323) {
                                    player.moveTo(new Position(3044, 3927, 0));
                                } else {
                                    player.getPacketSender().sendMessage("I can't climb them from here.");

                                }
                                break;
                            case 1733:
                                if (player.getPosition().getX() == 3045 || player.getPosition().getY() == 3927) {
                                    player.moveTo(new Position(3045, 10323, 0));
                                } else if (player.getPosition().getX() == 3044 || player.getPosition().getY() == 3927) {
                                    player.moveTo(new Position(3044, 10323, 0));
                                } else {
                                    player.getPacketSender().sendMessage("I can't climb them from here.");
                                }
                                break;
                            case 51:
                                player.getPacketSender().sendMessage("There is no way I could squeeze through that...");
                                break;
                            case 52:
                            case 53:
                                if (player.getPosition().getY() < 3470) {
                                    player.moveTo(
                                            new Position(player.getPosition().getX(), player.getPosition().getY() + 1));
                                } else if (player.getPosition().getY() > 3469) {
                                    player.moveTo(
                                            new Position(player.getPosition().getX(), player.getPosition().getY() - 1));
                                }
                                break;
                            case 99:
                                if (player.getInventory().contains(1843)) {
                                    player.moveTo(
                                            new Position(player.getPosition().getX(), player.getPosition().getY() - 1, 0));
                                } else {
                                    player.getPacketSender()
                                            .sendMessage("The door is locked. I must need some sort of key to get in.");
                                }
                                break;
                            case 2932:
                                player.moveTo(new Position(2600, 3157, 0));
                                player.performAnimation(new Animation(2306));
                                TaskManager.submit(new Task(1, player, true) {
                                    int tick = 1;

                                    @Override
                                    public void execute() {
                                        tick++;
                                        if (tick == 6) {
                                        } else if (tick >= 10) {
                                            stop();
                                        }
                                    }

                                    @Override
                                    public void stop() {
                                        setEventRunning(false);
                                        player.moveTo(new Position(player.getPosition().getX() - 1,
                                                player.getPosition().getY(), player.getPosition().getZ()));
                                        player.getPacketSender().sendMessage(
                                                "You smash open the barrel by jumping on it, a lion appeared!");
                                        NPC n = new NPC(1172, new Position(player.getPosition().getX(),
                                                player.getPosition().getY() + 2, player.getPosition().getZ()))
                                                .setSpawnedFor(player);
                                        World.register(n);
                                    }
                                });
                                break;
                            case 21764:
                            case 2654:
                                if (player.getDonatorRights().ordinal() > 2) {
                                    if (!player.getSpecTimer().elapsed(120000)) {
                                        player.getPacketSender()
                                                .sendMessage("You can only restore your special attack every 2 minutes.");
                                        return;
                                    }
                                    player.performAnimation(new Animation(1327));
                                    player.setSpecialPercentage(100);
                                    CombatSpecial.updateBar(player);
                                    int max = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION);
                                    player.setConstitution(max);
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            player.getSkillManager().getMaxLevel(Skill.PRAYER));
                                    player.setPoisonDamage(0);
                                    player.setVenomDamage(0);
                                    player.getPacketSender().sendConstitutionOrbPoison(false);
                                    player.getPacketSender().sendConstitutionOrbVenom(false);
                                    player.getPacketSender().sendMessage(
                                            "<img=10><col=570057><shad=0> You take a drink from the fountain... and feel revived!");
                                    player.getSpecTimer().reset();
                                } else {
                                    if (!player.getSpecTimer().elapsed(9000000)) {
                                        player.getPacketSender()
                                                .sendMessage("You can only restore your special attack every 15 minutes.");
                                        return;
                                    }
                                    player.performAnimation(new Animation(1327));
                                    player.setSpecialPercentage(100);
                                    CombatSpecial.updateBar(player);
                                    int max = player.getSkillManager().getMaxLevel(Skill.CONSTITUTION);
                                    player.setConstitution(max);
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            player.getSkillManager().getMaxLevel(Skill.PRAYER));
                                    player.setPoisonDamage(0);
                                    player.setVenomDamage(0);
                                    player.getPacketSender().sendConstitutionOrbPoison(false);
                                    player.getPacketSender().sendConstitutionOrbVenom(false);
                                    player.getPacketSender().sendMessage(
                                            "<icon=2><shad=ff0000> You take a drink from the fountain... and feel revived!");
                                    player.getSpecTimer().reset();
                                }
                                break;
                            case 81:
                                if (player.getPosition().getX() == 2584) {
                                    if (player.getInventory().contains(993)) {
                                        player.performAnimation(new Animation(1820));
                                        TaskManager.submit(new Task(1, player, true) {
                                            int tick = 1;

                                            @Override
                                            public void execute() {
                                                tick++;
                                                if (tick == 2) {
                                                } else if (tick >= 5) {
                                                    player.moveTo(new Position(player.getPosition().getX() + 1,
                                                            player.getPosition().getY(), 0));
                                                    stop();
                                                }
                                            }

                                            @Override
                                            public void stop() {
                                                setEventRunning(false);
                                                player.getPacketSender()
                                                        .sendMessage("You use the key to get through the door.");
                                            }
                                        });
                                    }
                                }
                                break;
                            case 82:
                                if (player.getPosition().getX() == 2606 || player.getPosition().getX() == 2607) {
                                    if (player.getInventory().contains(993)) {
                                        player.performAnimation(new Animation(1820));
                                        TaskManager.submit(new Task(1, player, true) {
                                            int tick = 1;

                                            @Override
                                            public void execute() {
                                                tick++;
                                                if (tick == 2) {
                                                } else if (tick >= 5) {
                                                    player.moveTo(new Position(player.getPosition().getX(),
                                                            player.getPosition().getY() + 2, 0));
                                                    stop();
                                                }
                                            }

                                            @Override
                                            public void stop() {
                                                setEventRunning(false);
                                                player.getPacketSender()
                                                        .sendMessage("You use the key to get through the door.");
                                            }
                                        });
                                    }
                                }
                                break;
                            case 4754:
                            case 4749:
                                if (player.getMinigameAttributes().getClawQuestAttributes().getSamples() <= 50) {
                                    if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() >= 6) {
                                        player.performAnimation(new Animation(2290));
                                        TaskManager.submit(new Task(1, player, true) {
                                            int tick = 1;

                                            @Override
                                            public void execute() {
                                                tick++;
                                                if (tick == 2) {
                                                    player.getMinigameAttributes().getClawQuestAttributes().addSamples(1);
                                                } else if (tick >= 6) {
                                                    stop();
                                                }
                                            }

                                            @Override
                                            public void stop() {
                                                setEventRunning(false);
                                                player.getPacketSender()
                                                        .sendMessage("You have collected "
                                                                + player.getMinigameAttributes().getClawQuestAttributes()
                                                                .getSamples()
                                                                + " of "
                                                                + player.getMinigameAttributes()
                                                                .getClawQuestAttributes().SAMPLES_NEEDED
                                                                + " samples needed.");
                                            }
                                        });
                                    } else {
                                        player.getPacketSender().sendMessage("Nothing interesting happened.");
                                    }
                                }
                                break;
                            case 12987:
                                player.getPacketSender()
                                        .sendMessage("There is another way out of this stable. The gate is broken!");
                                break;
                            case 12982:
                                if (player.getPosition().getY() == 3275) {
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            tick++;
                                            player.performAnimation(new Animation(828));
                                            if (tick == 3) {
                                                player.moveTo(new Position(player.getPosition().getX(),
                                                        player.getPosition().getY() + 2));
                                            } else if (tick >= 4) {
                                                stop();
                                            }
                                        }

                                        @Override
                                        public void stop() {
                                            setEventRunning(false);
                                            player.getPacketSender().sendMessage("You jump over the stile.");
                                        }
                                    });
                                } else {
                                    player.getPacketSender().sendMessage("You failed to climb over, please try again.");
                                }
                                break;
                            case 3565:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) >= 50) {

                                    if (player.getLocation() == Location.BORK && player.getPosition().getY() < 2973) {
                                        TaskManager.submit(new Task(1, player, true) {
                                            int tick = 1;

                                            @Override
                                            public void execute() {
                                                tick++;
                                                player.performAnimation(new Animation(769));
                                                if (tick == 3) {
                                                    player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY() + 3));
                                                } else if (tick >= 4) {
                                                    stop();
                                                }
                                            }

                                            @Override
                                            public void stop() {
                                                setEventRunning(false);
                                                Agility.addExperience(player, 13);
                                                player.getPacketSender().sendMessage("You jump over the wall.");
                                            }
                                        });
                                    } else if (player.getLocation() == Location.BORK && player.getPosition().getY() >= 2973) {
                                        TaskManager.submit(new Task(1, player, true) {
                                            int tick = 1;

                                            @Override
                                            public void execute() {
                                                tick++;
                                                player.performAnimation(new Animation(769));
                                                if (tick == 3) {
                                                    player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY() - 3));
                                                } else if (tick >= 4) {
                                                    stop();
                                                }
                                            }

                                            @Override
                                            public void stop() {
                                                setEventRunning(false);
                                                Agility.addExperience(player, 13);
                                                player.getPacketSender().sendMessage("You jump over the wall.");
                                            }
                                        });
                                    } else if (player.getPosition().getX() <= 3349) {
                                        TaskManager.submit(new Task(1, player, true) {
                                            int tick = 1;

                                            @Override
                                            public void execute() {
                                                tick++;
                                                player.performAnimation(new Animation(769));
                                                if (tick == 3) {
                                                    player.moveTo(new Position(3352, player.getPosition().getY()));
                                                } else if (tick >= 4) {
                                                    stop();
                                                }
                                            }

                                            @Override
                                            public void stop() {
                                                setEventRunning(false);
                                                Agility.addExperience(player, 13);
                                                player.getPacketSender().sendMessage("You jump over the wall.");
                                            }
                                        });
                                    } else {
                                        player.getPacketSender().sendMessage(
                                                "You need an Agility level of at least 50 to get past this obstacle.");
                                        player.getPacketSender().sendMessage("or an wilderness key!");
                                    }
                                } else {
                                    player.getPacketSender().sendMessage("You need 50 agility in order to cross this...");
                                }
                                break;
                            case 5259:
                                if (player.getTeleblockTimer() > 0) {
                                    player.getPacketSender().sendMessage("You are teleblocked");
                                    return;
                                }
                                if (player.getPosition().getX() >= 3653) { // :)
                                    player.moveTo(new Position(3652, player.getPosition().getY()));
                                } else {
                                    player.getPacketSender().sendInterfaceRemoval();
                                    player.moveTo(new Position(3653, player.getPosition().getY()));
                                }
                                break;
                            case 38700:
                                player.moveTo(new Position(3085, 3512));
                                break;
                            case 1765:// ladder down to posion spider KBD
                                player.performAnimation(new Animation(827));
                                TaskManager.submit(new Task(1, player, true) {
                                    int tick = 1;

                                    @Override
                                    public void execute() {
                                        tick++;
                                        if (tick == 4) {
                                            stop();
                                        }
                                    }

                                    @Override
                                    public void stop() {
                                        setEventRunning(false);
                                        player.moveTo(new Position(3069, 10255, 0));
                                    }
                                });
                                break;
                            case 2795: // lever KBD
                                if (player.getTeleblockTimer() > 0) {
                                    player.getPacketSender()
                                            .sendMessage("A magical spell is blocking you from teleporting.");
                                    return;
                                }
                                if (player.getPosition().getY() >= 10252) {
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            tick++;
                                            player.performAnimation(new Animation(2140));
                                            if (tick >= 2) {
                                                stop();
                                            }
                                        }

                                        @Override
                                        public void stop() {
                                            setEventRunning(false);
                                            TeleportHandler.teleportPlayer(player, new Position(2273, 4681, 0),
                                                    TeleportType.LEVER);
                                        }
                                    });
                                } else {
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            tick++;
                                            player.performAnimation(new Animation(2140));
                                            if (tick >= 2) {
                                                stop();
                                            }
                                        }

                                        @Override
                                        public void stop() {
                                            setEventRunning(false);
                                            TeleportHandler.teleportPlayer(player, new Position(3066, 10254),
                                                    TeleportType.LEVER);
                                        }
                                    });
                                }
                                break;
                            case 1766:// poison spider ladder KBD
                                if (player.getTeleblockTimer() > 0) {
                                    player.getPacketSender()
                                            .sendMessage("A magical spell is blocking you from teleporting.");
                                    return;
                                }
                                player.performAnimation(new Animation(828));
                                TaskManager.submit(new Task(1, player, true) {
                                    int tick = 1;

                                    @Override
                                    public void execute() {
                                        tick++;
                                        if (tick == 4) {
                                            stop();
                                        }
                                    }

                                    @Override
                                    public void stop() {
                                        setEventRunning(false);
                                        player.moveTo(new Position(3017, 3850, 0));
                                    }
                                });
                                break;
                            case 9312: // Grand Exchange Underwall Tunnel
                                Position position = new Position(3164, 3484, 0);
                                break;
//							case 2465:
//								if (player.getLocation() == Location.EDGEVILLE) {
//									player.getPacketSender().sendMessage(
//											"<img=4> @blu@Welcome to the free-for-all arena! You will not lose any items on death here.");
//									player.moveTo(new Position(2815, 5511));
//								} else {
//									player.getPacketSender()
//											.sendMessage("The portal does not seem to be functioning properly.");
//								}
//								break;
                            case 7353:
                                player.moveTo(new Position(2439, 4956, player.getPosition().getZ()));
                                break;
                            case 7321:
                                player.moveTo(new Position(2452, 4944, player.getPosition().getZ()));
                                break;
                            case 7322:
                                player.moveTo(new Position(2455, 4964, player.getPosition().getZ()));
                                break;
                            case 7315:
                                player.moveTo(new Position(2447, 4956, player.getPosition().getZ()));
                                break;
                            case 7316:
                                player.moveTo(new Position(2471, 4956, player.getPosition().getZ()));
                                break;
                            case 7318:
                                player.moveTo(new Position(2464, 4963, player.getPosition().getZ()));
                                break;
                            case 7324:
                                player.moveTo(new Position(2481, 4956, player.getPosition().getZ()));
                                break;

                            case 7319:
                                if (gameObject.getPosition().getX() == 2481 && gameObject.getPosition().getY() == 4956)
                                    player.moveTo(new Position(2467, 4940, player.getPosition().getZ()));
                                break;

                            case 11356:
                                if (!player.getDonatorRights().isDonator()) {
                                    player.getPacketSender().sendMessage("You are not a donator... Get out of here!");
                                    player.moveTo(new Position(3087, 3502, 0));
                                    return;
                                }
                                player.moveTo(new Position(2860, 9741));
                                player.getPacketSender().sendMessage("You step through the portal..");
                                break;
                            case 47180:
                                if (player.getDonatorRights().ordinal() >= 3) {
                                    player.getDialog().sendDialog(new PortalDevice(player, 0));
                                } else {
                                    player.getPacketSender().sendMessage("You need to be an Legendary Donator to use this.");
                                }
                                break;
                            case 10091:
                            case 8702:
                                if (gameObject.getId() == 8702) {
                                    if (player.getDonatorRights().ordinal() < 2) {
                                        player.getPacketSender()
                                                .sendMessage("You must be at least a Super Donator to use this.");
                                        return;
                                    }
                                }
                                Fishing.setupFishing(player, Spot.ROCKTAIL);
                                break;
                            case 9319:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 61 or higher to climb this");
                                    return;
                                }
                                if (player.getPosition().getZ() == 0)
                                    player.moveTo(new Position(3422, 3549, 1));
                                else if (player.getPosition().getZ() == 1) {
                                    if (gameObject.getPosition().getX() == 3447)
                                        player.moveTo(new Position(3447, 3575, 2));
                                    else
                                        player.moveTo(new Position(3447, 3575, 0));
                                }
                                break;

                            case 9320:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 61) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 61 or higher to climb this");
                                    return;
                                }
                                if (player.getPosition().getZ() == 1)
                                    player.moveTo(new Position(3422, 3549, 0));
                                else if (player.getPosition().getZ() == 0)
                                    player.moveTo(new Position(3447, 3575, 1));
                                else if (player.getPosition().getZ() == 2)
                                    player.moveTo(new Position(3447, 3575, 1));
                                player.performAnimation(new Animation(828));
                                break;
                            case 2274:
                                if (player.getTeleblockTimer() > 0) {
                                    player.getPacketSender().sendMessage("You are teleblocked, don't die, noob.");
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 2912 && gameObject.getPosition().getY() == 5300) {
                                    player.moveTo(new Position(2914, 5300, 1));
                                } else if (gameObject.getPosition().getX() == 2914
                                        && gameObject.getPosition().getY() == 5300) {
                                    player.moveTo(new Position(2912, 5300, 2));
                                } else if (gameObject.getPosition().getX() == 2919
                                        && gameObject.getPosition().getY() == 5276) {
                                    player.moveTo(new Position(2918, 5274));
                                } else if (gameObject.getPosition().getX() == 2918
                                        && gameObject.getPosition().getY() == 5274) {
                                    player.moveTo(new Position(2919, 5276, 1));
                                } else if (gameObject.getPosition().getX() == 3001
                                        && gameObject.getPosition().getY() == 3931
                                        || gameObject.getPosition().getX() == 3652
                                        && gameObject.getPosition().getY() == 3488) {
                                    player.moveTo(GameSettings.DEFAULT_POSITION_EDGEVILLE.copy());
                                    player.getPacketSender().sendMessage("The portal teleports you to Edgeville.");
                                }
                                break;
                            case 7836:
                            case 7837:
                            case 7808:
                            case 7818:
                                int amt = player.getInventory().getAmount(6055);
                                if (amt > 0) {
                                    player.getInventory().delete(6055, amt);
                                    player.getPacketSender().sendMessage("You put the weed in the compost bin.");
                                    player.getSkillManager().addSkillExperience(Skill.FARMING, 20 * amt);
                                    if (player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 2) {
                                        player.getMinigameAttributes().getFarmQuestAttributes().addProduce(amt);
                                        player.getPacketSender()
                                                .sendMessage("You now have added " + player.getMinigameAttributes()
                                                        .getFarmQuestAttributes().getProduce()
                                                        + "/100 weeds to the compost bin.");
                                        if (player.getMinigameAttributes().getFarmQuestAttributes().getProduce() > 99) {
                                            player.getMinigameAttributes().getFarmQuestAttributes().setQuestParts(3);
                                        }
                                    }
                                } else {
                                    player.getPacketSender().sendMessage("You do not have any weeds in your inventory.");
                                }
                                break;
                            case 9706:
                                if (player.getTeleblockTimer() > 0) {
                                    player.getPacketSender()
                                            .sendMessage("A magical spell is blocking you from teleporting.");
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 3104 && gameObject.getPosition().getY() == 3956) {
                                    player.setDirection(Direction.WEST);
                                    TeleportHandler.teleportPlayer(player, new Position(3105, 3951), TeleportType.LEVER);
                                } else {
                                    player.setDirection(Direction.WEST);
                                    TeleportHandler.teleportPlayer(player, new Position(3105, 3951), TeleportType.LEVER);
                                }
                                break;
                            case 9707:
                                if (player.getTeleblockTimer() > 0) {
                                    player.getPacketSender()
                                            .sendMessage("A magical spell is blocking you from teleporting.");
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 3105 && gameObject.getPosition().getY() == 3952) {
                                    player.setDirection(Direction.NORTH);
                                    TeleportHandler.teleportPlayer(player, new Position(3105, 3956), TeleportType.LEVER);
                                }
                                break;
                            case 5960: // Levers
                                if (player.getTeleblockTimer() > 0) {
                                    player.getPacketSender()
                                            .sendMessage("A magical spell is blocking you from teleporting.");
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 2539 && gameObject.getPosition().getY() == 4712) {
                                    player.setDirection(Direction.SOUTH);
                                    TeleportHandler.teleportPlayer(player, new Position(3090, 3956), TeleportType.LEVER);
                                } else if (gameObject.getPosition().getX() == 3067
                                        && gameObject.getPosition().getY() == 10253) {
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            tick++;
                                            player.performAnimation(new Animation(2140));
                                            if (tick >= 2) {
                                                stop();
                                            }
                                        }

                                        @Override
                                        public void stop() {
                                            setEventRunning(false);
                                            TeleportHandler.teleportPlayer(player, new Position(2272, 4680, 0),
                                                    TeleportType.LEVER);
                                        }
                                    });
                                } else if (gameObject.getPosition().getX() == 2272
                                        && gameObject.getPosition().getY() == 4680) {
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            tick++;
                                            player.performAnimation(new Animation(2140));
                                            if (tick >= 2) {
                                                stop();
                                            }
                                        }

                                        @Override
                                        public void stop() {
                                            setEventRunning(false);
                                            TeleportHandler.teleportPlayer(player, new Position(3067, 10253),
                                                    TeleportType.LEVER);
                                        }
                                    });
                                }
                                break;
                            case 5959:
                                if (player.getTeleblockTimer() > 0) {
                                    player.getPacketSender().sendMessage("A magical spell is blocking you from teleporting.");
                                    return;
                                }
                                if(player.getPosition().equals(3089,3956) || player.getPosition().equals(3089,3957) || player.getPosition().equals(3089,3955)) {
                                    return;
                                }
                                if (gameObject.getPosition().getX() == 3090 && gameObject.getPosition().getY() == 3956) {
                                    if(!player.getPosition().equals(3090,3956)) {
                                        return;
                                    }
                                    int x = 3090;
                                    int y = 3956;
                                    TaskManager.submit(new Task(1, player, false) {
                                        int ticks = 0;

                                        @Override
                                        public void execute() {
                                            ticks++;
                                            player.getWalkingQueue().walkStep(x,y);
                                            if (ticks >= 3)
                                                player.setDirection(Direction.WEST);
                                                TeleportHandler.teleportPlayer(player, new Position(2539, 4712), TeleportType.LEVER);
                                                stop();
                                        }

                                    });
                                } else if (gameObject.getPosition().getX() == 3090 && gameObject.getPosition().getY() == 3474) {
                                    if(!player.getPosition().equals(3090,3956)) {
                                        return;
                                    }
                                    player.setDirection(Direction.WEST);
                                    TeleportHandler.teleportPlayer(player, new Position(3154, 3923), TeleportType.LEVER);
                                } else if (player.getPosition().getX() == 3090 && player.getPosition().getY() >= 3957) {
                                    if(!player.getPosition().equals(3090,3956)) {
                                        return;
                                    }
                                    player.setDirection(Direction.SOUTH);
                                    TeleportHandler.teleportPlayer(player, new Position(2539, 4712), TeleportType.LEVER);
                                } else if (player.getPosition().getX() == 3090 && player.getPosition().getY() <= 3955) {
                                    if(!player.getPosition().equals(3090,3956)) {
                                        return;
                                    }
                                    player.setDirection(Direction.NORTH);
                                    TeleportHandler.teleportPlayer(player, new Position(2539, 4712), TeleportType.LEVER);
                                } else if (player.getPosition().getX() == 3153 && player.getPosition().getY() <= 3923) {
                                    player.setDirection(Direction.WEST);
                                    TeleportHandler.teleportPlayer(player, new Position(2561, 3311), TeleportType.LEVER);
                                } else if (gameObject.getPosition().equals(2561, 3311)) {
                                    player.setDirection(Direction.WEST);
                                    TeleportHandler.teleportPlayer(player, new Position(3154, 3923), TeleportType.LEVER);
                                } else if (player.getPosition().getX() == 2561 && player.getPosition().getY() <= 3311) {
                                    player.setDirection(Direction.WEST);
                                    TeleportHandler.teleportPlayer(player, new Position(3153, 3923), TeleportType.LEVER);
                                } else if (gameObject.getPosition().equals(3153, 3923)) {
                                    player.setDirection(Direction.WEST);
                                    TeleportHandler.teleportPlayer(player, new Position(2562, 3311), TeleportType.LEVER);
                                }
                                break;
                            case 5096:
                                if (gameObject.getPosition().getX() == 2644 && gameObject.getPosition().getY() == 9593)
                                    player.moveTo(new Position(2649, 9591));
                                break;

                            case 5094:
                                if (gameObject.getPosition().getX() == 2648 && gameObject.getPosition().getY() == 9592)
                                    player.moveTo(new Position(2643, 9594, 2));
                                break;

                            case 5098:
                                if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9511)
                                    player.moveTo(new Position(2637, 9517));
                                break;

                            case 5097:
                                if (gameObject.getPosition().getX() == 2635 && gameObject.getPosition().getY() == 9514)
                                    player.moveTo(new Position(2636, 9510, 2));
                                break;
                            case 26428:
                            case 26426:
                            case 26425:
                            case 26427:
                                String bossRoom = "Armadyl";
                                boolean leaveRoom = player.getPosition().getY() > 5295;
                                int index = 0;
                                Position movePos = new Position(2839, !leaveRoom ? 5296 : 5295, 2);
                                if (id == 26425) {
                                    bossRoom = "Bandos";
                                    leaveRoom = player.getPosition().getX() > 2863;
                                    index = 1;
                                    movePos = new Position(!leaveRoom ? 2864 : 2863, 5354, 2);
                                } else if (id == 26427) {
                                    bossRoom = "Saradomin";
                                    leaveRoom = player.getPosition().getX() < 2908;
                                    index = 2;
                                    movePos = new Position(leaveRoom ? 2908 : 2907, 5265, 0);
                                } else if (id == 26428) {
                                    bossRoom = "Zamorak";
                                    leaveRoom = player.getPosition().getY() <= 5331;
                                    index = 3;
                                    movePos = new Position(2925, leaveRoom ? 5332 : 5331, 2);
                                }
                                int killcount = 10;
                                switch (player.getDonatorRights()) {
                                    case PREMIUM:
                                        killcount = 8;
                                        break;
                                    case EXTREME:
                                        killcount = 5;
                                        break;
                                    case LEGENDARY:
                                        killcount = 0;
                                        break;
                                    case UBER:
                                        killcount = 0;
                                        break;
                                    case PLATINUM:
                                        killcount = 0;
                                        break;
                                }
                                switch (index) {
                                    case 0:
                                        if (BossPets.hasPet(player, BossPets.BossPet.PET_KREE_ARRA)) {
                                            killcount = 0;
                                        }
                                        break;
                                    case 1:
                                        if (BossPets.hasPet(player, BossPets.BossPet.PET_GENERAL_GRAARDOR)) {
                                            killcount = 0;
                                        }
                                        break;
                                    case 2:
                                        if (BossPets.hasPet(player, BossPets.BossPet.PET_ZILYANA)) {
                                            killcount = 0;
                                        }
                                        break;
                                    case 3:
                                        if (BossPets.hasPet(player, BossPets.BossPet.PET_KRIL_TSUTSAROTH)) {
                                            killcount = 0;
                                        }
                                        break;
                                }
                                if (!leaveRoom && (!player.getStaffRights().isManagement() && player.getMinigameAttributes()
                                        .getGodwarsDungeonAttributes().getKillcount()[index] < killcount)) {
                                    player.getPacketSender().sendMessage("You need " + Misc.anOrA(bossRoom) + " " + bossRoom
                                            + " killcount of at least " + killcount + " to enter this room.");
                                    return;
                                }
                                player.moveTo(movePos);
                                player.getMinigameAttributes().getGodwarsDungeonAttributes()
                                        .setHasEnteredRoom(leaveRoom ? false : true);
                                player.getMinigameAttributes().getGodwarsDungeonAttributes().getKillcount()[index] = 0;
                                player.getPacketSender().sendString(16216 + index, "0");
                                break;
                            case 26289:
                            case 26286:
                            case 26288:
                            case 26287:
                                if (System.currentTimeMillis() - player.getMinigameAttributes()
                                        .getGodwarsDungeonAttributes().getAltarDelay() < 600000) {
                                    player.getPacketSender().sendMessage("");
                                    player.getPacketSender()
                                            .sendMessage("You can only pray at a God's altar once every 10 minutes.");
                                    player.getPacketSender().sendMessage("You must wait another "
                                            + (int) ((600 - (System.currentTimeMillis() - player.getMinigameAttributes()
                                            .getGodwarsDungeonAttributes().getAltarDelay()) * 0.001))
                                            + " seconds before being able to do this again.");
                                    return;
                                }
                                int itemCount = id == 26289 ? Equipment.getItemCount(player, "Bandos", false)
                                        : id == 26286 ? Equipment.getItemCount(player, "Zamorak", false)
                                        : id == 26288 ? Equipment.getItemCount(player, "Armadyl", false)
                                        : id == 26287 ? Equipment.getItemCount(player, "Saradomin", false)
                                        : 0;
                                int toRestore = player.getSkillManager().getMaxLevel(Skill.PRAYER) + (itemCount * 10);
                                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) >= toRestore) {
                                    player.getPacketSender()
                                            .sendMessage("You do not need to recharge your Prayer points at the moment.");
                                    return;
                                }
                                player.performAnimation(new Animation(645));
                                player.getSkillManager().setCurrentLevel(Skill.PRAYER, toRestore);
                                player.getMinigameAttributes().getGodwarsDungeonAttributes()
                                        .setAltarDelay(System.currentTimeMillis());
                                break;
                            case 2873:
                                player.performAnimation(new Animation(645));
                                player.getPacketSender().sendMessage("You pray to Saradomin and recieve a holy cape...");
                                player.getInventory().add(new Item(2412, 1));
                                break;
                            case 2875:
                                player.performAnimation(new Animation(645));
                                player.getPacketSender().sendMessage("You pray to Guthix and recieve a holy cape...");
                                player.getInventory().add(new Item(2413, 1));
                                break;
                            case 2874:
                                player.performAnimation(new Animation(645));
                                player.getPacketSender().sendMessage("You pray to Zamorak and recieve a holy cape...");
                                player.getInventory().add(new Item(2414, 1));
                                break;
                            case 16044:
                                if (player.getPosition().getY() < 3875) {
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            tick++;
                                            player.performAnimation(new Animation(804));
                                            if (tick == 4) {
                                                stop();
                                            } else if (tick >= 6) {
                                                stop();
                                            }
                                        }

                                        @Override
                                        public void stop() {
                                            setEventRunning(false);
                                            player.moveTo(new Position(player.getPosition().getX(),
                                                    player.getPosition().getY() + 2));
                                            player.getPacketSender().sendMessage("You teleport through the portal.");
                                            player.getPacketSender()
                                                    .sendMessage("You can only leave this zone by talking to Sir Tinley.");
                                        }
                                    });
                                } else {
                                    player.getPacketSender()
                                            .sendMessage("You cannot leave through this portal, talk to Sir Tinley.");
                                }
                                break;
                            case 23093:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 70) {
                                    player.getPacketSender().sendMessage(
                                            "You need an Agility level of at least 70 to go through this portal.");
                                    return;
                                }
                                if (!player.getClickDelay().elapsed(2000))
                                    return;
                                int plrHeight = player.getPosition().getZ();
                                if (plrHeight == 2)
                                    player.moveTo(new Position(2914, 5300, 1));
                                else if (plrHeight == 1) {
                                    int x = gameObject.getPosition().getX();
                                    int y = gameObject.getPosition().getY();
                                    if (x == 2914 && y == 5300)
                                        player.moveTo(new Position(2912, 5299, 2));
                                    else if (x == 2920 && y == 5276)
                                        player.moveTo(new Position(2920, 5274, 0));
                                } else if (plrHeight == 0)
                                    player.moveTo(new Position(2920, 5276, 1));
                                player.getClickDelay().reset();
                                break;
                            case 26439:
                                if (player.getSkillManager().getMaxLevel(Skill.CONSTITUTION) <= 700) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Constitution level of at least 70 to swim across.");
                                    return;
                                }
                                if (!player.getClickDelay().elapsed(1000))
                                    return;
                                if (player.isCrossingObstacle())
                                    return;
                                final String startMessage = "You jump into the icy cold water..";
                                final String endMessage = "You climb out of the water safely.";
                                final int jumpGFX = 68;
                                final int jumpAnimation = 772;
                                player.setSkillAnimation(773);
                                player.setCrossingObstacle(true);
                                player.getUpdateFlag().flag(Flag.APPEARANCE);
                                player.performAnimation(new Animation(3067));
                                final boolean goBack2 = player.getPosition().getY() >= 5344;
                                player.getPacketSender().sendMessage(startMessage);
                                player.moveTo(new Position(2885, !goBack2 ? 5335 : 5342, 2));
                                player.setDirection(goBack2 ? Direction.SOUTH : Direction.NORTH);
                                player.performGraphic(new Graphic(jumpGFX));
                                player.performAnimation(new Animation(jumpAnimation));
                                TaskManager.submit(new Task(1, player, false) {
                                    int ticks = 0;

                                    @Override
                                    public void execute() {
                                        ticks++;
                                        player.getWalkingQueue().walkStep(0, goBack2 ? -1 : 1);
                                        if (ticks >= 10)
                                            stop();
                                    }

                                    @Override
                                    public void stop() {
                                        player.setSkillAnimation(-1);
                                        player.setCrossingObstacle(false);
                                        player.getUpdateFlag().flag(Flag.APPEARANCE);
                                        player.getPacketSender().sendMessage(endMessage);
                                        player.moveTo(
                                                new Position(2885, player.getPosition().getY() < 5340 ? 5333 : 5345, 2));
                                        setEventRunning(false);
                                    }
                                });
                                player.getClickDelay().reset((System.currentTimeMillis() + 9000));
                                break;
                            case 26384:
                                if (player.isCrossingObstacle())
                                    return;
                                if (!player.getInventory().contains(2347)) {
                                    player.getPacketSender()
                                            .sendMessage("You need to have a hammer to bang on the door with.");
                                    return;
                                }
                                player.setCrossingObstacle(true);
                                final boolean goBack = player.getPosition().getX() <= 2850;
                                player.performAnimation(new Animation(377));
                                TaskManager.submit(new Task(2, player, false) {
                                    @Override
                                    public void execute() {
                                        player.moveTo(new Position(goBack ? 2851 : 2850, 5333, 2));
                                        player.setCrossingObstacle(false);
                                        stop();
                                    }
                                });
                                break;
                            case 26303:
                                if (!player.getClickDelay().elapsed(1200))
                                    return;
                                if (player.getSkillManager().getCurrentLevel(Skill.RANGED) < 70)
                                    player.getPacketSender()
                                            .sendMessage("You need a Ranged level of at least 70 to swing across here.");
                                else if (!player.getInventory().contains(9419)) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Mithril grapple to swing across here.");
                                    return;
                                } else {
                                    player.performAnimation(new Animation(789));
                                    TaskManager.submit(new Task(2, player, false) {
                                        @Override
                                        public void execute() {
                                            player.getPacketSender().sendMessage(
                                                    "You throw your Mithril grapple over the pillar and move across.");
                                            player.moveTo(new Position(2871,
                                                    player.getPosition().getY() <= 5270 ? 5279 : 5269, 2));
                                            stop();
                                        }
                                    });
                                    player.getClickDelay().reset();
                                }
                                break;
                            case 4493:
                                if (player.getPosition().getX() >= 3432) {
                                    player.moveTo(new Position(3433, 3538, 1));
                                }
                                break;
                            case 4494:
                                player.moveTo(new Position(3438, 3538, 0));
                                break;
                            case 4495:
                                player.moveTo(new Position(3417, 3541, 2));
                                break;
                            case 4496:
                                player.moveTo(new Position(3412, 3541, 1));
                                break;
                            case 25339:
                            case 25340:
                                player.moveTo(new Position(1778, 5346, player.getPosition().getZ() == 0 ? 1 : 0));
                                break;
                            case 10229:
                            case 10230:
                                boolean up = id == 10229;
                                player.performAnimation(new Animation(up ? 828 : 827));
                                player.getPacketSender().sendMessage("You climb " + (up ? "up" : "down") + " the ladder..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(up ? new Position(1912, 4367) : new Position(2900, 4449));
                                        stop();
                                    }
                                });
                                break;
                            case 1568:
                                player.moveTo(new Position(3097, 9868));
                                break;
                            case 5103: // Brimhaven vines
                            case 5104:
                            case 5105:
                            case 5106:
                            case 5107:
                                if (!player.getClickDelay().elapsed(4000))
                                    return;
                                if (player.getSkillManager().getCurrentLevel(Skill.WOODCUTTING) < 30) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Woodcutting level of at least 30 to do this.");
                                    return;
                                }
                                if (WoodcuttingData.getHatchet(player) < 0) {
                                    player.getPacketSender().sendMessage(
                                            "You do not have a hatchet which you have the required Woodcutting level to use.");
                                    return;
                                }
                                final Hatchet axe = Hatchet.forId(WoodcuttingData.getHatchet(player));
                                player.performAnimation(new Animation(axe.getAnim()));
                                gameObject.setRotation(-1);
                                TaskManager.submit(new Task(3 + Misc.getRandom(4), player, false) {
                                    @Override
                                    protected void execute() {
                                        if (player.moving) {
                                            stop();
                                            return;
                                        }
                                        int x = 0;
                                        int y = 0;
                                        if (player.getPosition().getX() == 2689 && player.getPosition().getY() == 9564) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2691
                                                && player.getPosition().getY() == 9564) {
                                            x = -2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2683
                                                && player.getPosition().getY() == 9568) {
                                            x = 0;
                                            y = 2;
                                        } else if (player.getPosition().getX() == 2683
                                                && player.getPosition().getY() == 9570) {
                                            x = 0;
                                            y = -2;
                                        } else if (player.getPosition().getX() == 2674
                                                && player.getPosition().getY() == 9479) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2676
                                                && player.getPosition().getY() == 9479) {
                                            x = -2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2693
                                                && player.getPosition().getY() == 9482) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2672
                                                && player.getPosition().getY() == 9499) {
                                            x = 2;
                                            y = 0;
                                        } else if (player.getPosition().getX() == 2674
                                                && player.getPosition().getY() == 9499) {
                                            x = -2;
                                            y = 0;
                                        }
                                        CustomObjects.objectRespawnTask(player,
                                                new GameObject(-1, gameObject.getPosition().copy()), gameObject, 10);
                                        player.getPacketSender().sendMessage("You chop down the vines..");
                                        player.getSkillManager().addSkillExperience(Skill.WOODCUTTING, 45);
                                        player.performAnimation(new Animation(65535));
                                        player.getWalkingQueue().walkStep(x, y);
                                        stop();
                                    }
                                });
                                player.getClickDelay().reset();
                                break;

                            case 29942:
                                if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player.getSkillManager()
                                        .getMaxLevel(Skill.SUMMONING)) {
                                    player.getPacketSender()
                                            .sendMessage("You do not need to recharge your Summoning points right now.");
                                    return;
                                }
                                player.performGraphic(new Graphic(1517));
                                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                                        player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
                                player.getPacketSender().sendString(18045,
                                        " " + player.getSkillManager().getCurrentLevel(Skill.SUMMONING) + "/"
                                                + player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                                player.getPacketSender().sendMessage("You recharge your Summoning points.");
                                break;
                            case 57225:
                                if (!player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
                                    player.moveTo(new Position(2911, 5204));
                                    player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(true);
                                } else {
                                    player.moveTo(new Position(2906, 5204));
                                    player.getMinigameAttributes().getGodwarsDungeonAttributes().setHasEnteredRoom(false);
                                }
                                break;
                            case 9294:
                                if (player.getSkillManager().getCurrentLevel(Skill.AGILITY) < 80) {
                                    player.getPacketSender()
                                            .sendMessage("You need an Agility level of at least 80 to use this shortcut.");
                                    return;
                                }
                                player.performAnimation(new Animation(769));
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(
                                                new Position(player.getPosition().getX() >= 2880 ? 2878 : 2880, 9813));
                                        stop();
                                    }
                                });
                                break;
                            case 9293:
                                boolean back = player.getPosition().getX() > 2888;
                                player.moveTo(back ? new Position(2886, 9799) : new Position(2891, 9799));
                                break;
                            case 2320:
                                back = player.getPosition().getY() == 9969 || player.getPosition().getY() == 9970;
                                player.moveTo(back ? new Position(3120, 9963) : new Position(3120, 9969));
                                break;
                            case 1755:
                                player.performAnimation(new Animation(828));
                                player.getPacketSender().sendMessage("You climb the stairs..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        if (gameObject.getPosition().getX() == 2547
                                                && gameObject.getPosition().getY() == 9951) {
                                            player.moveTo(new Position(2548, 3551));
                                        } else if (gameObject.getPosition().getX() == 3005
                                                && gameObject.getPosition().getY() == 10363) {
                                            player.moveTo(new Position(3005, 3962));
                                        } else if (gameObject.getPosition().getX() == 3084
                                                && gameObject.getPosition().getY() == 9672) {
                                            player.moveTo(new Position(3117, 3244));
                                        } else if (gameObject.getPosition().getX() == 3097
                                                && gameObject.getPosition().getY() == 9867) {
                                            player.moveTo(new Position(3096, 3468));
                                        }
                                        stop();
                                    }
                                });
                                break;
                            case 1738:
                                player.performAnimation(new Animation(828));
                                player.getPacketSender().sendMessage("You climb the stairs..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        if (player.getPosition().getX() > 3204 || player.getLocation() == Location.LUMBRIDGE) {
                                            if (player.getPosition().getX() < 3207)
                                                player.moveTo(new Position(player.getPosition().getX(),
                                                        player.getPosition().getY(), 1));
                                        } else if (player.getLocation() != Location.WARRIORS_GUILD
                                                && player.getPosition().getZ() == 0) {
                                            player.moveTo(new Position(2729, 3462, 1));
                                        } else {
                                            player.moveTo(new Position(2840, 3539, 2));
                                        }
                                        stop();
                                    }
                                });
                                break;
                            case 5110:
                                player.moveTo(new Position(2647, 9557));
                                player.getPacketSender().sendMessage("You pass the stones..");
                                break;
                            case 5111:
                                player.moveTo(new Position(2649, 9562));
                                player.getPacketSender().sendMessage("You pass the stones..");
                                break;
                            case 6434:
                                player.performAnimation(new Animation(827));
                                player.getPacketSender().sendMessage("You enter the trapdoor..");
                                TaskManager.submit(new Task(1, player, false) {
                                    @Override
                                    protected void execute() {
                                        player.moveTo(new Position(3085, 9672));
                                        stop();
                                    }
                                });
                                break;
                            case 19187:
                            case 19175:
                                Hunter.dismantle(player, gameObject);
                                break;
                            case 25029:
                                PuroPuro.goThroughWheat(player, gameObject);
                                break;
                            case 47976:
                                Nomad.endFight(player, false);
                                break;
                            case 2182:
                                if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                                    player.getPacketSender()
                                            .sendMessage("You have no business with this chest. Talk to the Gypsy first!");
                                    return;
                                }
                                RecipeForDisaster.openRFDShop(player);
                                break;
                            case 12356:
                                if (!player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0)) {
                                    player.getPacketSender()
                                            .sendMessage("You have no business with this portal. Talk to the Gypsy first!");
                                    return;
                                }
                                if (player.getPosition().getZ() > 0) {
                                    RecipeForDisaster.leave(player);
                                } else {
                                    player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(1,
                                            true);
                                    RecipeForDisaster.enter(player);
                                }
                                break;
                            case 9369:
                                if (player.getPosition().getY() > 5175) {
                                    FightPit.addPlayer(player);
                                } else {
                                    FightPit.removePlayer(player, "leave room");
                                }
                                break;
                            case 9368:
                                if (player.getPosition().getY() < 5169) {
                                    FightPit.removePlayer(player, "leave game");
                                }
                                break;
                            case 9357:
                                FightCave.leaveCave(player, false);
                                break;
                            case 9356:
                                FightCave.enterCave(player);
                                break;
                            case 6704:
                                player.moveTo(new Position(3577, 3282, 0));
                                break;
                            case 5013:
                                player.moveTo(new Position(2838, 10124, 0));
                                break;
                            case 5998:
                                player.moveTo(new Position(2799, 10134, 0));
                                break;
                            case 6706:
                                player.moveTo(new Position(3554, 3283, 0));
                                break;
                            case 6705:
                                player.moveTo(new Position(3566, 3275, 0));
                                break;
                            case 6702:
                                player.moveTo(new Position(3564, 3289, 0));
                                break;
                            case 6703:
                                player.moveTo(new Position(3574, 3298, 0));
                                break;
                            case 6707:
                                player.moveTo(new Position(3556, 3298, 0));
                                break;
                            case 3203:
                                if (player.getLocation() == Location.DUEL_ARENA && player.getDueling().duelingStatus == 5) {
                                    if (player.getDueling().timer >= 0) {
                                        player.getPacketSender()
                                                .sendMessage("You cannot forfeit before the duel has started.");
                                        return;
                                    }
                                    if (Dueling.checkRule(player, DuelRule.NO_FORFEIT)) {
                                        player.getPacketSender().sendMessage("Forfeiting has been disabled in this duel.");
                                        return;
                                    }
                                    player.getCombatBuilder().reset(true);
                                    if (player.getDueling().duelingWith > -1) {
                                        Player duelEnemy = World.getPlayers().get(player.getDueling().duelingWith);
                                        if (duelEnemy == null)
                                            return;
                                        duelEnemy.getCombatBuilder().reset(true);
                                        duelEnemy.getWalkingQueue().clear();
                                        duelEnemy.getDueling().duelVictory();
                                    }
                                    player.moveTo(new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3), 0));
                                    player.getDueling().reset();
                                    player.getCombatBuilder().reset(true);
                                    player.restart();
                                }
                                break;
                            case 14315:
                                PestControl.boardBoat(player);
                                break;
                            case 14314:
                                if (player.getLocation() == Location.PEST_CONTROL_BOAT) {
                                    player.getLocation().leave(player);
                                }
                                break;
                            case 1739:
                                player.getDialog().sendDialog(new LumbyStairs(player));
                                break;
                            case 1740:
                                player.moveTo(new Position(player.getPosition().getX(),
                                        player.getPosition().getY(), player.getPosition().getZ() - 1));
                                break;
                            case 15638:
                                player.moveTo(new Position(2840, 3539, 0));
                                break;
                            case 15644:
                            case 15641:
                                switch (player.getPosition().getZ()) {
                                    case 0:
                                        player.moveTo(new Position(2855, player.getPosition().getY() >= 3546 ? 3545 : 3546));
                                        break;
                                    case 2:
                                        if (player.getPosition().getX() == 2846) {
                                            int requiredAmount = 50;
                                            if(player.getDonatorRights() != DonatorRights.PLAYER) {
                                                requiredAmount = 25;
                                            }
                                            if (player.getInventory().getAmount(8851) < requiredAmount) {
                                                player.getPacketSender()
                                                        .sendMessage("You need at least "+requiredAmount+" tokens to enter this area.");
                                                return;
                                            }
                                            player.moveTo(new Position(2847, player.getPosition().getY(), 2));
                                            WarriorsGuild.handleTokenRemoval(player);
                                        } else if (player.getPosition().getX() == 2847) {
                                            WarriorsGuild.resetCyclopsCombat(player);
                                            player.moveTo(new Position(2846, player.getPosition().getY(), 2));
                                            player.getMinigameAttributes().getWarriorsGuildAttributes()
                                                    .setEnteredTokenRoom(false);
                                        }
                                        break;
                                }
                                break;
                            case 15653:
                                if (player.getSkillManager().getCurrentLevel(Skill.ATTACK) + player.getSkillManager().getCurrentLevel(Skill.STRENGTH) < 130) {
                                    player.getPacketSender().sendMessage("A true warrior requires a total of 130 Strength and Attack.");
                                    return;
                                }
                                if (player.getPosition().getX() == 2877) {
                                    player.moveTo(new Position(2876, 3546, 0));
                                } else {
                                    player.moveTo(new Position(2877, 3546, 0));
                                }
                                break;
                            case 28714:
                                player.performAnimation(new Animation(828));
                                player.delayedMoveTo(new Position(2806, 2785), 2);
                                break;
                            case 1756:
                                player.performAnimation(new Animation(827));
                                if(gameObject.getPosition().equals(3097, 3468)) {
                                    player.delayedMoveTo(new Position(3096, 9867), 2);
                                } else {
                                    player.delayedMoveTo(new Position(2209, 5348), 2);
                                }
                                player.performAnimation(new Animation(827));
                                break;
                            case 2268:
                                player.performAnimation(new Animation(828));
                                player.delayedMoveTo(new Position(3229, 3610), 2);
                                break;

                            case 19191:
                            case 19189:
                            case 19180:
                            case 19184:
                            case 19182:
                            case 19178:
                            case 19186:
                                Hunter.lootTrap(player, gameObject);
                                break;
                            case 28716:
                                if (!player.busy()) {
                                    player.getSkillManager().updateSkill(Skill.SUMMONING);
                                    player.getPacketSender().sendInterface(63471);
                                } else
                                    player.getPacketSender()
                                            .sendMessage("Please finish what you're doing before opening this.");
                                break;
                            case 6:
                                DwarfCannon cannon = player.getCannon();
                                if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                                    player.getPacketSender().sendMessage("This is not your cannon!");
                                } else {
                                    DwarfMultiCannon.startFiringCannon(player, cannon);
                                }
                                break;
                            case 2:
                                if(gameObject.getPosition().equals(2792, 2771)) {
                                    player.moveTo(new Position(2384, 4706));
                                } else if(gameObject.getPosition().equals(2383, 4704)) {
                                    player.moveTo(new Position(2794, 2773));
                                } else if(gameObject.getPosition().equals(2509, 3874)) {
                                    player.moveTo(new Position(2336, 9794));
                                }
                                player.getPacketSender().sendMessage("You walk through the entrance..");
                                break;
                            case 3223:
                                if(gameObject.getPosition().equals(2336, 9793)) {
                                    player.moveTo(new Position(2510, 3873));
                                }
                                player.getPacketSender().sendMessage("You walk through the entrance..");
                                break;
                            case 2026:
                            case 2028:
                            case 2029:
                            case 2030:
                            case 2031:
                                player.setEntityInteraction(gameObject);
                                Fishing.setupFishing(player, Fishing.forSpot(gameObject.getId(), false));
                                return;
                            case 12692:
                            case 2783:
                            case 4306:
                                player.setInteractingObject(gameObject);
                                EquipmentMaking.handleAnvil(player);
                                break;
                            case 41687:
                            case 2732:
                            case 4767:
                                EnterAmountOfLogsToAdd.openInterface(player);
                                break;
                            case 409:
                            case 27661:
                            case 2640:
                            case 36972:
                                player.performAnimation(new Animation(645));
                                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
                                        .getMaxLevel(Skill.PRAYER)) {
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
                                    player.getPacketSender().sendMessage("You recharge your Prayer points.");
                                }
                                break;
                            case 8749:
                                boolean restore = player.getSpecialPercentage() < 100;
                                if (restore) {
                                    player.setSpecialPercentage(100);
                                    CombatSpecial.updateBar(player);
                                    player.getPacketSender().sendMessage("Your special attack energy has been restored.");
                                }
                                for (Skill skill : Skill.values()) {
                                    int increase = skill != Skill.PRAYER && skill != Skill.CONSTITUTION
                                            && skill != Skill.SUMMONING ? 19 : 0;
                                    if (player.getSkillManager().getCurrentLevel(
                                            skill) < (player.getSkillManager().getMaxLevel(skill) + increase))
                                        player.getSkillManager().setCurrentLevel(skill,
                                                (player.getSkillManager().getMaxLevel(skill) + increase));
                                }
                                player.performGraphic(new Graphic(1302));
                                player.getPacketSender().sendMessage("Your stats have received a major buff.");
                                break;
                            case 4859:
                                player.performAnimation(new Animation(645));
                                if (player.getSkillManager().getCurrentLevel(Skill.PRAYER) < player.getSkillManager()
                                        .getMaxLevel(Skill.PRAYER)) {
                                    player.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            player.getSkillManager().getMaxLevel(Skill.PRAYER), true);
                                    player.getPacketSender().sendMessage("You recharge your Prayer points.");
                                }
                                break;
                            case 411:
                                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 30) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Defence level of at least 30 to use this altar.");
                                    return;
                                }
                                player.performAnimation(new Animation(645));
                                if (player.getPrayerbook() == Prayerbook.NORMAL) {
                                    player.getPacketSender()
                                            .sendMessage("You sense a surge of power flow through your body!");
                                    player.setPrayerbook(Prayerbook.CURSES);
                                    Achievements.finishAchievement(player, Achievements.AchievementData.SWITCH_PRAYBOOK);
                                } else {
                                    player.getPacketSender()
                                            .sendMessage("You sense a surge of purity flow through your body!");
                                    player.setPrayerbook(Prayerbook.NORMAL);
                                    Achievements.finishAchievement(player, Achievements.AchievementData.SWITCH_PRAYBOOK);
                                }
                                player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB,
                                        player.getPrayerbook().getInterfaceId());
                                PrayerHandler.deactivateAll(player);
                                CurseHandler.deactivateAll(player);
                                break;
                            case 2515:
                                if (player.getLocation() == Location.ROCK_CRABS) {
                                    player.performAnimation(new Animation(828));
                                    player.getPacketSender().sendString(1, "ZULRAHFADE");
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            if (tick == 2) {
                                                player.moveTo(new Position(2690, 3706, 0));
                                            }
                                            if (tick == 5) {
                                                player.moveTo(new Position(2691, 3771, 0));
                                            }
                                            if (tick == 8) {
                                                stop();
                                            }
                                            tick++;
                                        }

                                        @Override
                                        public void stop() {
                                            player.moveTo(new Position(3102, 2959, 0));
                                        }
                                    });
                                } else {
                                    player.performAnimation(new Animation(828));
                                    player.getPacketSender().sendString(1, "ZULRAHFADE");
                                    TaskManager.submit(new Task(1, player, true) {
                                        int tick = 1;

                                        @Override
                                        public void execute() {
                                            if (tick == 2) {
                                                player.moveTo(new Position(3102, 2956, 0));
                                            }
                                            if (tick == 5) {
                                                player.moveTo(new Position(2691, 3771, 0));
                                            }
                                            if (tick == 8) {
                                                stop();
                                            }
                                            tick++;
                                        }

                                        @Override
                                        public void stop() {
                                            player.moveTo(new Position(2688, 3706, 0));
                                        }
                                    });
                                }
                                break;
                            case 6552:
                                player.performAnimation(new Animation(645));
                                player.setSpellbook(player.getSpellbook() == MagicSpellbook.ANCIENT ? MagicSpellbook.NORMAL
                                        : MagicSpellbook.ANCIENT);
                                player.getPacketSender()
                                        .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                                        .sendMessage("You feel a sense of energy as your spellbook changes.");
                                Autocasting.resetAutocast(player, true);
                                Achievements.finishAchievement(player, Achievements.AchievementData.SWITCH_SPELLBOOK);
                                break;
                            case 410:
                                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
                                    player.getPacketSender()
                                            .sendMessage("You need a Defence level of at least 40 to use this altar.");
                                    return;
                                }
                                player.performAnimation(new Animation(645));
                                player.setSpellbook(player.getSpellbook() == MagicSpellbook.LUNAR ? MagicSpellbook.NORMAL
                                        : MagicSpellbook.LUNAR);
                                player.getPacketSender()
                                        .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                                        .sendMessage("Your magic spellbook is changed..");
                                ;
                                Autocasting.resetAutocast(player, true);
                                Achievements.finishAchievement(player, Achievements.AchievementData.SWITCH_SPELLBOOK);
                                break;
                            case 2878:
                                player.moveTo(new Position(2509, 4689));
                                break;
                            case 2879:
                                player.moveTo(new Position(2542, 4718));
                                break;
                            case 172:
                                CrystalChest.handleChest(player, gameObject);
                                break;
                            case 2403:
                                WarChest.handleChest(player, gameObject);
                                break;
                            case 10621:
                            case 18804:
                            case 24204:
                            case 29577:
                                TreasureChest.handleChest(player, gameObject);
                                break;
                            case 6910:
                            case 4483:
                            case 3193:
                            case 2213:
                            case 11758:
                            case 6084:
                            case 10517:
                            case 14367:
                            case 42192:
                            case 26972:
                            case 11402:
                            case 26969:
                            case 75:
                            case 2497:
                            case 16700:
                            case 21301:
                            case 36786:
                            case 2995:
                            case 45079:
                            case 27663:
                                player.getBank(player.getCurrentBankTab()).open();
                                break;
                            case 45091:
                                if(player.getInventory().getAmount(444) + player.getInventory().getAmount(445) == 0 && player.getInventory().getAmount(453) + player.getInventory().getAmount(454) == 0) {
                                    player.getPacketSender().sendMessage("<col=ff0000>Small sacks needs gold or coal ore in order to turn them into minerals.");
                                    return;
                                }
                                player.getDialog().sendDialog(new SmallSacks(player));
                                break;
                            case 21304:
                                player.getDialog().sendDialog(new Spin(player));
                                break;
                            case 21514: //ladder up at neiznot
                                player.performAnimation(new Animation(828));
                                TaskManager.submit(new Task(1, player, true) {
                                    int tick = 1;

                                    @Override
                                    public void execute() {
                                        tick++;
                                        if (tick == 4) {
                                            stop();
                                        }
                                    }

                                    @Override
                                    public void stop() {
                                        setEventRunning(false);
                                        if (player.getPosition().getY() < 3805) {
                                            player.moveTo(new Position(2329, 3802, 1));
                                        } else {
                                            player.getPacketSender().sendMessage("You cannot climb this ladder.");
                                        }
                                    }
                                });
                                break;
                            case 21515:// ladder down neitznot
                                player.performAnimation(new Animation(827));
                                TaskManager.submit(new Task(1, player, true) {
                                    int tick = 1;

                                    @Override
                                    public void execute() {
                                        tick++;
                                        if (tick == 4) {
                                            stop();
                                        }
                                    }

                                    @Override
                                    public void stop() {
                                        setEventRunning(false);
                                        player.moveTo(new Position(player.getPosition().getX() + 1, player.getPosition().getY(), 0));
                                    }
                                });
                                break;
                        }
                    }
                }));
    }

    private static void secondClick(final Player player, Packet packet) {
        final int id = packet.readLEShortA();
        final int y = packet.readLEShort();
        final int x = packet.readUnsignedShortA();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (id > 0 && id != 6 && id != 2213 && !World.objectExists(gameObject) && id != 4706) {
            player.getPacketSender().sendMessage("An error occured. Error code: " + id)
                    .sendMessage("Please report the error to a staff member.");
            return;
        }
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender()
                    .sendConsoleMessage("Second click object id; [id, position] : [" + id + ", " + position.toString() + "]");
        if(gameObject.getId() != 4767) {
            player.setPositionToFace(gameObject.getPosition());
        }
        int distanceX = (player.getPosition().getX() - position.getX());
        int distanceY = (player.getPosition().getY() - position.getY());
        if (distanceX < 0)
            distanceX = -(distanceX);
        if (distanceY < 0)
            distanceY = -(distanceY);
        int size = distanceX > distanceY ? distanceX : distanceY;
        gameObject.setSize(size);
        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in ObjectActionPacketListener: " + gameObject.getId() + " -
            // SECOND_CLICK");
        }
        if (!player.getDragonSpear().elapsed(3000)) {
            player.getPacketSender().sendMessage("You are stunned!");
            return;
        }
        player.setInteractingObject(gameObject)
                .setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
                    @Override
                    public void execute() {
                        int ran = 0;
                        if (MiningData.forRock(gameObject.getId()) != null) {
                            Prospecting.prospectOre(player, id);
                            return;
                        }
                        if (player.getThieving().stealFromStall(ThievingStall.forId(id)))
                            return;
                        if (Farming.isGameObject(player, gameObject, 2))
                            return;
                        if (Agility.handleObject(player, gameObject)) {
                            return;
                        }
                        switch (gameObject.getId()) {
                            case 38660:
                            case 38661:
                            case 38662:
                            case 38663:
                            case 38664:
                            case 38665:
                            case 38666:
                            case 38667:
                            case 38668:
                                ShootingStar.getInstance().hasMenuAction(player, 2);
                                break;

                            case 17010:
                                if (player.getSkillManager().getMaxLevel(Skill.DEFENCE) < 40) {
                                    player.getPacketSender().sendMessage("You need a Defence level of at least 40 to use this altar.");
                                    return;
                                }
                                player.performAnimation(new Animation(645));
                                player.setSpellbook(player.getSpellbook() == MagicSpellbook.LUNAR ? MagicSpellbook.NORMAL
                                        : MagicSpellbook.LUNAR);
                                player.getPacketSender()
                                        .sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId())
                                        .sendMessage("Your magic spellbook is changed..");
                                Autocasting.resetAutocast(player, true);
                                Achievements.finishAchievement(player, Achievements.AchievementData.SWITCH_SPELLBOOK);
                                break;
                            case 2274:
                                player.setRevsWarning(true);
                                player.getPacketSender().sendMessage("You have re-enabled the revs warning toggle.");
                                break;
                            case 6910:
                            case 4483:
                            case 3193:
                            case 2213:
                            case 6084:
                            case 10517:
                            case 11758:
                            case 14367:
                            case 42192:
                            case 26972:
                            case 11402:
                            case 26969:
                            case 75:
                            case 36786:
                            case 27663:
                                player.getBank(player.getCurrentBankTab()).open();
                                break;
                            case 884:
                                player.setNpcClickId(945);
                                player.getDialog().sendDialog(new DonateToWellDial(player));
                                break;
                            case 28716:
                                if (player.getSkillManager().getCurrentLevel(Skill.SUMMONING) == player.getSkillManager()
                                        .getMaxLevel(Skill.SUMMONING)) {
                                    player.getPacketSender()
                                            .sendMessage("You do not need to recharge your Summoning points right now.");
                                    return;
                                }
                                player.performGraphic(new Graphic(1517));
                                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                                        player.getSkillManager().getMaxLevel(Skill.SUMMONING), true);
                                player.getPacketSender().sendString(18045,
                                        " " + player.getSkillManager().getCurrentLevel(Skill.SUMMONING) + "/"
                                                + player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                                player.getPacketSender().sendMessage("You recharge your Summoning points.");
                                break;
                            case 2646:
                            case 312:
                                if (!player.getClickDelay().elapsed(1200))
                                    return;
                                if (player.getInventory().isFull()) {
                                    player.getPacketSender().sendMessage("You don't have enough free inventory space.");
                                    return;
                                }
                                String type = gameObject.getId() == 312 ? "Potato" : "Flax";
                                player.performAnimation(new Animation(827));
                                player.getInventory().add(gameObject.getId() == 312 ? 1942 : 1779, 1);
                                player.getPacketSender().sendMessage("You pick some " + type + "..");
                                gameObject.setPickAmount(gameObject.getPickAmount() + 1);
                                if (Misc.getRandom(3) == 1 && gameObject.getPickAmount() >= 1
                                        || gameObject.getPickAmount() >= 6) {
                                    player.getPacketSender().sendClientRightClickRemoval();
                                    gameObject.setPickAmount(0);
                                    CustomObjects.globalObjectRespawnTask(new GameObject(-1, gameObject.getPosition()),
                                            gameObject, 10);
                                }
                                player.getClickDelay().reset();
                                break;
                            case 2644:
                                player.getDialog().sendDialog(new Spin(player));
                                break;
                            case 6:
                                DwarfCannon cannon = player.getCannon();
                                if (cannon == null || cannon.getOwnerIndex() != player.getIndex()) {
                                    player.getPacketSender().sendMessage("This is not your cannon!");
                                } else {
                                    DwarfMultiCannon.pickupCannon(player, cannon, false);
                                }
                                break;
                            case 6189:
                            case 26814:
                            case 11666:
                            case 26300:
                            case 21303:
                                Smelting.openInterface(player);
                                break;
                            case 2152:
                                player.performAnimation(new Animation(8502));
                                player.performGraphic(new Graphic(1308));
                                player.getSkillManager().setCurrentLevel(Skill.SUMMONING,
                                        player.getSkillManager().getMaxLevel(Skill.SUMMONING));
                                player.getPacketSender().sendMessage("You renew your Summoning points.");
                                break;
                        }
                    }
                }));
    }

    private static void thirdClick(Player player, Packet packet) {
        final int id = packet.readUnsignedShortA();
        final int y = packet.readUnsignedShortA();
        final int x = packet.readShort();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (id > 0 && id != 6 && !World.objectExists(gameObject)) {
            // player.getPacketSender().sendMessage("An error occured.
            // Errorcode: "+id).sendMessage("Please report the error to a
            // staffmember.");
            return;
        }
        if(gameObject.getId() != 4767) {
            player.setPositionToFace(gameObject.getPosition());
        }
        int distanceX = (player.getPosition().getX() - position.getX());
        int distanceY = (player.getPosition().getY() - position.getY());
        if (distanceX < 0)
            distanceX = -(distanceX);
        if (distanceY < 0)
            distanceY = -(distanceY);
        int size = distanceX > distanceY ? distanceX : distanceY;
        gameObject.setSize(size);
        player.setInteractingObject(gameObject);
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender()
                    .sendMessage("Third click object id; [id, position] : [" + id + ", " + position.toString() + "]");
        player.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {
                if (Farming.isGameObject(player, gameObject, 3))
                    return;
                switch (id) {
                }
            }
        }));
    }

    private static void fourthClick(Player player, Packet packet) {
        final int id = packet.readUnsignedShortA();
        final int y = packet.readUnsignedShortA();
        final int x = packet.readShort();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (id > 0 && id != 6 && !World.objectExists(gameObject)) {
            // player.getPacketSender().sendMessage("An error occured.
            // Errorcode: "+id).sendMessage("Please report the error to a
            // staffmember.");
            return;
        }
        if(gameObject.getId() != 4767) {
            player.setPositionToFace(gameObject.getPosition());
        }
        int distanceX = (player.getPosition().getX() - position.getX());
        int distanceY = (player.getPosition().getY() - position.getY());
        if (distanceX < 0)
            distanceX = -(distanceX);
        if (distanceY < 0)
            distanceY = -(distanceY);
        int size = distanceX > distanceY ? distanceX : distanceY;
        gameObject.setSize(size);
        player.setInteractingObject(gameObject);
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender()
                    .sendMessage("Fourth click object id; [id, position] : [" + id + ", " + position.toString() + "]");
        player.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {
                if (Farming.isGameObject(player, gameObject, 4))
                    return;

                switch (id) {
                }
            }
        }));
    }

    private static void fifthClick(final Player player, Packet packet) {
        final int id = packet.readUnsignedShortA();
        final int y = packet.readUnsignedShortA();
        final int x = packet.readShort();
        final Position position = new Position(x, y, player.getPosition().getZ());
        final GameObject gameObject = new GameObject(id, position);
        if (id > 0 && !World.objectExists(gameObject)) {
            player.getPacketSender().sendMessage("An error occured. Error code: " + id).sendMessage("Please report the error to a staffmember.");
            return;
        }
        if (!player.getDragonSpear().elapsed(3000)) {
            player.getPacketSender().sendMessage("You are stunned!");
            return;
        }
        if(gameObject.getId() != 4767) {
            player.setPositionToFace(gameObject.getPosition());
        }
        int distanceX = (player.getPosition().getX() - position.getX());
        int distanceY = (player.getPosition().getY() - position.getY());
        if (distanceX < 0)
            distanceX = -(distanceX);
        if (distanceY < 0)
            distanceY = -(distanceY);
        int size = distanceX > distanceY ? distanceX : distanceY;
        gameObject.setSize(size);
        player.setInteractingObject(gameObject);
        player.setWalkToTask(new WalkToTask(player, position, gameObject.getSize(), new FinalizedMovementTask() {
            @Override
            public void execute() {
                if (Farming.isGameObject(player, gameObject, 5))
                    return;
                switch (id) {
                }
            }
        }));
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.isTeleporting() || player.isPlayerLocked() || player.getWalkingQueue().isLockMovement()) {
            return;
        }
        switch (packet.getOpcode()) {
            case FIRST_CLICK:
                firstClick(player, packet);
                break;
            case SECOND_CLICK:
                secondClick(player, packet);
                break;
            case THIRD_CLICK:
                thirdClick(player, packet);
                break;
            case FOURTH_CLICK:
                fourthClick(player, packet);
                break;
            case FIFTH_CLICK:
                fifthClick(player, packet);
                break;
        }
    }

    public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 234,
            FIFTH_CLICK = 228;
}