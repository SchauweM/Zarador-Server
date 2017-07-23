package com.zarador.net.packet.impl;

import org.scripts.kotlin.content.dialog.ClanChatDialogue;
import org.scripts.kotlin.content.dialog.Report;
import org.scripts.kotlin.content.dialog.teleports.*;

import com.zarador.GameSettings;
import com.zarador.model.Gender;
import com.zarador.model.Position;
import com.zarador.model.Locations.Location;
import com.zarador.model.container.impl.Bank;
import com.zarador.model.container.impl.Bank.BankSearchAttributes;
import com.zarador.model.definitions.WeaponInterfaces.WeaponInterface;
import com.zarador.model.input.impl.*;
import com.zarador.model.options.Option;
import com.zarador.model.player.GameMode;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.*;
import com.zarador.world.content.Sounds.Sound;
import com.zarador.world.content.achievements.*;
import com.zarador.world.content.clan.ClanChat;
import com.zarador.world.content.clan.ClanChatManager;
import com.zarador.world.content.combat.magic.Autocasting;
import com.zarador.world.content.combat.magic.MagicSpells;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.content.combat.weapon.CombatSpecial;
import com.zarador.world.content.combat.weapon.FightType;
import com.zarador.world.content.minigames.impl.*;
import com.zarador.world.content.pos.PlayerOwnedShops;
import com.zarador.world.content.pos.PosDetails;
import com.zarador.world.content.pos.PosFeaturedShops;
import com.zarador.world.content.skill.ChatboxInterfaceSkillAction;
import com.zarador.world.content.skill.Enchanting;
import com.zarador.world.content.skill.impl.crafting.LeatherMaking;
import com.zarador.world.content.skill.impl.crafting.Tanning;
import com.zarador.world.content.skill.impl.fletching.Fletching;
import com.zarador.world.content.skill.impl.herblore.IngridientsBook;
import com.zarador.world.content.skill.impl.smithing.SmithingData;
import com.zarador.world.content.skill.impl.summoning.PouchMaking;
import com.zarador.world.content.skill.impl.summoning.SummoningTab;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.content.wells.WellOfGoodness;
import com.zarador.world.entity.impl.player.Player;

import java.util.ArrayList;

/**
 * This packet listener manages a button that the player has clicked upon.
 *
 * @author Gabriel Hannason
 */

public class ButtonClickPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {

        int id = packet.readUnsignedShort();

        if (player.getStaffRights().isDeveloper(player)) {
            player.getPacketSender().sendMessage("Clicked button: " + id);
        }
        
        /*
         * Player Owned Shops
         * Search List
         */
        if (id <= 41474 && id >= 41870) {
            PosDetails pd = PosItemSearch.forId(id, player);
            if (pd != null) {
                if (PlayerPunishment.isPlayerBanned(pd.getOwner())) {
                    player.getPacketSender().sendMessage("This player is banned!");
                } else {
                    player.getPacketSender().sendString(41900, "Back to Search Selection");
                    PlayerOwnedShops.openShop(pd.getOwner(), player);
                }
            } else {
                player.getPacketSender().sendMessage("This result didn't return a offer!");
            }
        }

        if (checkOptionContainer(player, id)) {
            return;
        }

        if (checkHandlers(player, id))
            return;

        if (Enchanting.enchantButtons(player, id)) {
            return;
        }

        if (PosFeaturedShops.handlePosButtons(player, id)) {
            return;
        }

        switch (id) {
            case 65216:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 12464:
                player.setRequestingAssistance(!player.isRequestAssistance());
                if (player.isRequestAssistance()) {
                    player.getPacketSender().sendConfig(427, 0);
                } else {
                    player.getPacketSender().sendConfig(427, 1);
                }
                break;
                
            /*
             * Player Owned Shops
             * 
             */
            case 41890:
                if (player.getShop() != null && player.getShop().id == 46 && player.isShopping()) {
                    int experience;
                    if(player.getGameModeAssistant().getGameMode() == GameMode.REALISM || player.getGameModeAssistant().getGameMode() == GameMode.IRONMAN) {
                        experience = 10;
                    } else {
                        experience = 100;
                    }
                    player.setInputHandling(new BuyDungExperience());
                    player.getPacketSender().sendEnterAmountPrompt("How much experience would you like to buy? 1 token = "+experience+" exp");
                } else if(player.isPlayerOwnedShopping()) {
                    if (player.getGameModeAssistant().isIronMan()) {
                        player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
                        return;
                    }
                    player.setPlayerOwnedShopping(false);
                    PlayerOwnedShops.openItemSearch(player, false);
                }
            break;
            
            /*
             * Quest List Interface
             * Well buttons
             */
            case 55101:
                if(WellOfGoodness.isActive("exp")) {
                    player.getPacketSender().sendMessage("This well is active for " + WellOfGoodness.getMinutesRemaining("exp") + " more minutes.");
	            } else {
	                player.getPacketSender().sendMessage("This well is not active");
	            }
                break;
            case 55102:
                if(WellOfGoodness.isActive("drops")) {
                    player.getPacketSender().sendMessage("This well is active for " + WellOfGoodness.getMinutesRemaining("drops") + " more minutes.");
                } else {
                    player.getPacketSender().sendMessage("This well is not active");
                }
                break;
            case 55103:
                if(WellOfGoodness.isActive("pkp")) {
                    player.getPacketSender().sendMessage("This well is active for " + WellOfGoodness.getMinutesRemaining("pkp") + " more minutes.");
                } else {
                    player.getPacketSender().sendMessage("This well is not active");
                }
                break;   
            /*
             * Kill Tracker
             */
            case 55105:
            	KillsTracker.open(player, 0);
            	break;
            /*
             * Drop Log    
             */
            case 55106:
                DropLog.open(player, 0);
                break;
            case 35253:
                if (player.isKillsTrackerOpen()) {
                    KillsTracker.open(player, 0);
                } else if (player.isDropLogOpen()) {
                    DropLog.open(player, 0);
                }
                break;
            case 35254:
                if (player.isKillsTrackerOpen()) {
                    KillsTracker.open(player, 1);
                } else if (player.isDropLogOpen()) {
                    DropLog.open(player, 1);
                }
                break;
            /*
             *	Drop Generator     
             */
            case 55107:
                player.setInputHandling(new DropGeneratorNpcId());
                player.getPacketSender().sendEnterAmountPrompt("Enter a npc id:");
                break;
            /*
             * Staff Online    
             */
            case 55074:
                ArrayList<Player> staffOnline = World.getStaffOnline();
                for (Player staff: staffOnline) {
                    player.getPacketSender().sendMessage("<img=" + staff.getStaffRights().getCrown() + "> " + staff.getUsername());
                }
                break;
            case 55071:
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55200);
                break;
            /*
             * Players Online    
             */
            case 55073:
                int tlNeeded = 149;
                if (player.getSkillManager().getTotalLevel() < tlNeeded) {
                    player.getPacketSender().sendMessage(
                            "You cannot view the players online until you have over " + (tlNeeded + 1) + " total level.");
                } else {
                    PlayersOnlineInterface.showInterface(player);
                }
                break;
                
            /*
             * PK - Target
             */
            case 55120:
            	if (player.getPlayerKillingAttributes().getTarget() != null) {
            		int my_x = player.getPosition().getX();
            		int other_x = player.getPlayerKillingAttributes().getTarget().getPosition().getX();
            		int wild_lvl = player.getPlayerKillingAttributes().getTarget().getWildernessLevel();
            		int steps_x = 0;
            		String direction = "";
            		boolean on_eachother = false;
            		if (my_x > other_x) {
            			steps_x = my_x - other_x;
            			direction = "West";
            		} else if (my_x == other_x) {
            			on_eachother = true;
            		} else {
            			steps_x = other_x - my_x;
            			direction = "East";
            		}
            		if (player.getPlayerKillingAttributes().getTarget().getLocation() == Location.WILDERNESS) {
            			if (on_eachother) {
            				player.getPacketSender().sendMessage(
            						"Your target '" + player.getPlayerKillingAttributes().getTarget().getUsername()
            						+ "' is directly on you in level " + wild_lvl + " wilderness.");
            			} else {
            				player.getPacketSender()
            				.sendMessage("Your target '"
            						+ player.getPlayerKillingAttributes().getTarget().getUsername() + "' is "
            						+ steps_x + " steps " + direction + " in level " + wild_lvl + " wilderness.");
            			}
            		} else {
            			player.getPacketSender().sendMessage("Your target is not in the wilderness!");
            		}
            	} else {
            		player.getPacketSender().sendMessage("You currently do not have a target!");
            	}
            	break;
            case 55005:
            	if (player.isKillsTrackerOpen()) {
            		player.setKillsTrackerOpen(false);
            		player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55065);
            		PlayerPanel.refreshPanel(player);
            	}
            	break;
                
           /*
            * End Quest Interface 	
            */
            	
           /*
            * Dungeoneering
            */            
            case 26250:
                player.setInputHandling(new JoinDungeoneeringParty());
                player.getPacketSender().sendEnterInputPrompt("What dungeoneering party do you want to join?");
                break;
            //Close tab
            case 26226:
                player.getPacketSender().sendDungeoneeringTabIcon(false);
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55065);
                break;
                
            /*
             * Report button
             */
            case 1314:
                player.getDialog().sendDialog(new Report(player));
                break;
             
            /*
             * Teleport Interface
             */
            case 60652: //Close Button
                player.getPacketSender().sendInterfaceRemoval();
                break;
                
            //Cities
            case 60602:
            case 1174: //Modern Magic
            case 13079: //Ancients Magick
            case 30250: //Lunars Spells
                TeleportingInterface.showInterface(player, "cities");
                break;
            case 8299:
                if(player.newPlayer()) {
                    return;
                }
                TeleportingInterface.showInterface(player, "cities");
                break;
            
            //Skilling Areas
            case 60605:
            case 1540: //Moderns Magic
            case 13045: // Ancient Magick
            case 30083: //Lunars Spells
                TeleportingInterface.showInterface(player, "skilling");
                break;
            
            //Minigames
            case 1167: //Moderns Magic
            case 13087: //Ancients Magick
            case 30106: //Lunars Spells
            case 60608:
                TeleportingInterface.showInterface(player, "minigames");
                break;

            //Monsters
            case 60611:
            case 1164: //Moderns Magic
            case 13035: //Ancients Magick
            case 30064: //Lunars Spells
                TeleportingInterface.showInterface(player, "monsters");
                break;

            //Wilderness Areas
            case 1170: //Moderns Magic
            case 13095: //Ancients Magick
            case 30226: //Lunars Spells
                player.getDialog().sendDialog(new WildernessAreas(player));
                break;

            //Dungeons
            case 60614:
            case 1541: //Moderns Magic
            case 13069: //Ancients Magick
            case 30138: //Lunars Spells
                TeleportingInterface.showInterface(player, "dungeons");
                break;

            //Bosses
            case 60617:
            case 7455: //Moderns Magic
            case 13053: //Ancients Magick
            case 30266: //Lunars Spells
                TeleportingInterface.showInterface(player, "bosses");
                break;

            case 13061: //Ancients
                TeleportHandler.teleportPlayer(player, new Position(3164, 3482, 0), player.getSpellbook().getTeleportType());
                break;
                
           /*
            * End Teleport Interface
            */

//            case -10425:
//                player.getPacketSender().sendTab(GameSettings.OPTIONS_TAB);
//                PlayerPanel.refreshPanel(player);
//                break;
//            case -10423:
//                player.setTourneyToggle(!player.tourney_toggle);
//                PlayerPanel.refreshPanel(player);
//                break;
            case 41400:
                player.getDueling().setLastDuelRules(player);
                break;
//            case -10424:
//                player.getPacketSender().sendTab(GameSettings.OPTIONS_TAB);
//                PlayerPanel.refreshPanel(player);
//                break;
            case 39166:
                player.getPacketSender().sendMessage("Coming soon...");
                break;
            case 38082:
            case 38008:
            case 5384:
            case 18721:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 45384:
                for (int j = 0; j < player.getInventory().getItems().length; j++) {
                    player.getTrading().tradeItem(player.getInventory().get(j).getId(),
                            player.getInventory().get(j).getAmount(), j);
                }
                break;
            case 1036:
                EnergyHandler.rest(player);
                break;
            case 39160:
            case 14012:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 14176:
                player.setUntradeableDropItem(null);
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 14175:
                player.getPacketSender().sendInterfaceRemoval();
                if (player.getUntradeableDropItem() != null
                        && player.getInventory().contains(player.getUntradeableDropItem().getId())) {
                    if(player.getDungeoneering().isDungItem(player.getUntradeableDropItem().getDefinition().getName())) {
                        player.getDungeoneering().unBindItem(player.getUntradeableDropItem());
                        player.getPacketSender().sendMessage("You have unbinded the item "+player.getUntradeableDropItem().getDefinition().getName()+". You now have "+player.getDungeoneering().getBindSlotsOpen()+" bind slots open.");
                    } else {
                        player.getPacketSender().sendMessage("Your item vanishes as it hits the floor.");
                    }
                    player.getInventory().delete(player.getUntradeableDropItem());
                    Sounds.sendSound(player, Sound.DROP_ITEM);
                }
                player.setUntradeableDropItem(null);
                break;
            case 1013:
                player.getSkillManager().setTotalGainedExp(0);
                break;
//            case -10426:
//                player.setYellToggle(!player.yell_toggle);
//                PlayerPanel.refreshPanel(player);
//                break;
            
            case 55206:
                player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55065);
                break;
            case 55207:
                RecipeForDisaster.openQuestLog(player);
                break;
            case 55208:
                Nomad.openQuestLog(player);
                break;
            case 350:
                player.getPacketSender()
                        .sendMessage("To autocast a spell, please right-click it and choose the autocast option.")
                        .sendTab(GameSettings.MAGIC_TAB).sendConfig(108, player.getAutocastSpell() == null ? 3 : 1);
                break;
            case 29335:
                if(player.getInterfaceId() > 0) {
                    player.getPacketSender().sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                player.getDialog().sendDialog(new ClanChatDialogue(player));
                break;
            case 29455:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                ClanChatManager.toggleLootShare(player);
                break;

            //Home Teleport
            case 30000:
            case 1195:
                int random = Misc.random(1);
                switch(random) {
                    case 0:
                        TeleportHandler.teleportPlayer(player, new Position(3087, 3502, 0), player.getSpellbook().getTeleportType());
                        break;
                    case 1:
                        TeleportHandler.teleportPlayer(player, new Position(3086, 3502, 0), player.getSpellbook().getTeleportType());
                        break;
                }
                Achievements.finishAchievement(player, Achievements.AchievementData.TELEPORT_HOME);
                break;

            //Ape atoll teleport
            case 18470:
                TeleportHandler.teleportPlayer(player, new Position(2765, 2785, 0), player.getSpellbook().getTeleportType());
                break;

            case 1159: // Bones to Bananas
            case 15877:// Bones to peaches
            case 30306:
                MagicSpells.handleMagicSpells(player, id);
                break;
            case 10001:
                if (player.getInterfaceId() == -1) {
                    Consumables.handleHealAction(player);
                } else {
                    player.getPacketSender().sendMessage("You cannot heal yourself right now.");
                }
                break;
            case 18025:
                if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
                    PrayerHandler.deactivatePrayer(player, PrayerHandler.AUGURY);
                } else {
                    PrayerHandler.activatePrayer(player, PrayerHandler.AUGURY);
                }
                break;
            case 18018:
                if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
                    PrayerHandler.deactivatePrayer(player, PrayerHandler.RIGOUR);
                } else {
                    PrayerHandler.activatePrayer(player, PrayerHandler.RIGOUR);
                }
                break;
            case 10000:
            case 950:
                if (player.getInterfaceId() < 0)
                    player.getPacketSender().sendInterface(40030);
                else
                    player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
                break;
            case 3546:
            case 3420:
                if (System.currentTimeMillis() - player.getTrading().lastAction <= 300)
                    return;
                player.getTrading().lastAction = System.currentTimeMillis();
                if (player.getTrading().inTrade()) {
                    player.getTrading().acceptTrade(id == 3546 ? 2 : 1);
                } else {
                    player.getPacketSender().sendInterfaceRemoval();
                }
                break;
            case 26003:
            case 10162:
            case 47267:
            case 15210:
            case 26300:
                player.getPacketSender().sendInterfaceRemoval();
                break;
            case 841:
                IngridientsBook.readBook(player, player.getCurrentBookPage() + 2, true);
                break;
            case 839:
                IngridientsBook.readBook(player, player.getCurrentBookPage() - 2, true);
                break;
            case 14922:
                player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
                break;
            case 14921:
                player.getPacketSender().sendMessage("Please visit the forums and ask for help in the support section.");
                break;
            case 5294:
                player.getPacketSender().sendClientRightClickRemoval().sendInterfaceRemoval();
                //TODO: Re-add account pin system
                //player.getDialog().sendDialog(player.getBankPinAttributes().hasBankPin() ? new DeletePin(player) : new BankPinDial(player));
                break;
            case 15002:
            case 27653:
                if (!player.busy() && !player.getCombatBuilder().isBeingAttacked()) {
                    player.getSkillManager().stopSkilling();
                    player.getPriceChecker().open();
                } else {
                    player.getPacketSender().sendMessage("You cannot open this right now.");
                }
                break;
            case 2735:
            case 1511:
                if (player.getSummoning().getBeastOfBurden() != null) {
                    player.getSummoning().toInventory();
                    player.getPacketSender().sendInterfaceRemoval();
                } else {
                    player.getPacketSender().sendMessage("You do not have a familiar who can hold items.");
                }
                break;
            case 54038:
            case 54035:
            case 54032:
            case 54029:
            case 1020:
            case 1021:
            case 1019:
            case 1018:
                if (id == 1020 || id == -11504)
                    SummoningTab.renewFamiliar(player);
                else if (id == 1019 || id == -11501)
                    SummoningTab.callFollower(player);
                else if (id == 1021 || id == -11498)
                    SummoningTab.handleDismiss(player, true);
                else if (id == -11507)
                    player.getSummoning().toInventory();
                else if (id == 1018)
                    player.getSummoning().toInventory();
                break;
            case 2799:
            case 2798:
            case 1747:
            case 1748:
            case 8890:
            case 8886:
            case 8875:
            case 8871:
            case 8894:
                ChatboxInterfaceSkillAction.handleChatboxInterfaceButtons(player, id);
                break;
            case 14873:
            case 14874:
            case 14875:
            case 14876:
            case 14877:
            case 14878:
            case 14879:
            case 14880:
            case 14881:
            case 14882:
                BankPin.clickedButton(player, id);
                break;
            case 27005:
            case 22012:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                Bank.depositItems(player, id == 27005 ? player.getEquipment() : player.getInventory(), false);
                break;
            case 27023:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                if (player.getSummoning().getBeastOfBurden() == null) {
                    player.getPacketSender().sendMessage("You do not have a familiar which can hold items.");
                    return;
                }
                Bank.depositItems(player, player.getSummoning().getBeastOfBurden(), false);
                break;
            case 22008:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                player.setNoteWithdrawal(!player.withdrawAsNote());
                break;
            case 21000:
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                player.setSwapMode(false);
                player.getPacketSender().sendConfig(304, 0).sendMessage("This feature is coming soon!");
                // player.setSwapMode(!player.swapMode());
                break;
            case 27009:
                MoneyPouch.toBank(player);
                break;
            case 27014:
            case 27015:
            case 27016:
            case 27017:
            case 27018:
            case 27019:
            case 27020:
            case 27021:
            case 27022:
                if (!player.isBanking())
                    return;
                if (player.getBankSearchingAttributes().isSearchingBank())
                    BankSearchAttributes.stopSearch(player, true);
                int bankId = id - 27014;
                boolean empty = bankId > 0 ? Bank.isEmpty(player.getBank(bankId)) : false;
                if (!empty || bankId == 0) {
                    player.setCurrentBankTab(bankId);
                    player.getPacketSender().sendString(5385, "scrollreset");
                    player.getPacketSender().sendString(27002, Integer.toString(player.getCurrentBankTab()));
                    player.getPacketSender().sendString(27000, "1");
                    player.getBank(bankId).open();
                } else
                    player.getPacketSender().sendMessage("To create a new tab, please drag an item here.");
                break;
            case 22004:
                if (!player.isBanking())
                    return;
                if (!player.getBankSearchingAttributes().isSearchingBank()) {
                    player.getBankSearchingAttributes().setSearchingBank(true);
                    player.setInputHandling(new EnterSyntaxToBankSearchFor());
                    player.getPacketSender().sendEnterInputPrompt("What would you like to search for?");
                } else {
                    BankSearchAttributes.stopSearch(player, true);
                }
                break;
            case 22845:
            case 24115:
            case 24010:
            case 24041:
            case 150:
                player.setAutoRetaliate(!player.isAutoRetaliate());
                break;
            case 29332:
                ClanChat clan = player.getCurrentClanChat();
                if (clan == null) {
                    player.getPacketSender().sendMessage("You are not in a clanchat channel.");
                    return;
                }
                ClanChatManager.leave(player, false);
                player.setClanChatName(null);
                break;
            case 29329:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                player.setInputHandling(new EnterClanChatToJoin());
                player.getPacketSender().sendEnterInputPrompt("Enter the name of the clanchat channel you wish to join:");
                break;
            case 19158:
            case 152:
                if (player.getRunEnergy() <= 1) {
                    player.getPacketSender().sendMessage("You do not have enough energy to do this.");
                    player.getWalkingQueue().setRunningToggled(false);
                } else {
                    player.getWalkingQueue().setRunningToggled(!player.getWalkingQueue().isRunning());
                }
                player.getPacketSender().sendRunStatus();
                break;
            case 55114:
            case 39167:
            case 27658:
            case 15004:
                player.setExperienceLocked(!player.experienceLocked());
                String type = player.experienceLocked() ? "locked" : "unlocked";
                player.getPacketSender().sendMessage("Your experience is now " + type + ".");
                PlayerPanel.refreshPanel(player);
                break;
            case 27651:
            case 21341:
            case 15001:
                if (player.getInterfaceId() == -1) {
                    player.getSkillManager().stopSkilling();
                    BonusManager.update(player);
                    player.getPacketSender().sendInterface(21172);
                } else
                    player.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
                break;
            case 15003:
            case 27654:
                if (player.getInterfaceId() > 0) {
                    player.getPacketSender()
                            .sendMessage("Please close the interface you have open before opening another one.");
                    return;
                }
                player.getSkillManager().stopSkilling();
                ItemsKeptOnDeath.sendInterface(player);
                break;
            case 2458: // Logout
                if (player.logout()) {
                    World.getPlayers().remove(player);
                }
                break;
            // case 10003:
            case 29138:
            case 29038:
            case 29063:
            case 29113:
            case 29163:
            case 29188:
            case 29213:
            case 29238:
            case 30007:
            case 48023:
            case 33033:
            case 30108:
            case 7473:
            case 7562:
            case 7487:
            case 7788:
            case 8298:
            case 8481:
            case 7612:
            case 7587:
            case 7662:
            case 7462:
            case 7548:
            case 7687:
            case 7537:
            case 12322:
            case 7637:
            case 12311:
            case 10003:
            case 8003:
                CombatSpecial.activate(player);
                break;
            case 1772: // shortbow & longbow
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_ACCURATE);
                }
                break;
            case 1771:
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_RAPID);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_RAPID);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_RAPID);
                }
                break;
            case 1770:
                if (player.getWeapon() == WeaponInterface.SHORTBOW) {
                    player.setFightType(FightType.SHORTBOW_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.LONGBOW) {
                    player.setFightType(FightType.LONGBOW_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
                    player.setFightType(FightType.CROSSBOW_LONGRANGE);
                }
                break;
            case 2282: // dagger & sword
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_STAB);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_STAB);
                }
                break;
            case 2285:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_LUNGE);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_LUNGE);
                }
                break;
            case 2284:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_SLASH);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_SLASH);
                }
                break;
            case 2283:
                if (player.getWeapon() == WeaponInterface.DAGGER) {
                    player.setFightType(FightType.DAGGER_BLOCK);
                } else if (player.getWeapon() == WeaponInterface.SWORD) {
                    player.setFightType(FightType.SWORD_BLOCK);
                }
                break;
            case 2429: // scimitar & longsword
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_CHOP);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_CHOP);
                }
                break;
            case 2432:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_SLASH);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_SLASH);
                }
                break;
            case 2431:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_LUNGE);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_LUNGE);
                }
                break;
            case 2430:
                if (player.getWeapon() == WeaponInterface.SCIMITAR) {
                    player.setFightType(FightType.SCIMITAR_BLOCK);
                } else if (player.getWeapon() == WeaponInterface.LONGSWORD) {
                    player.setFightType(FightType.LONGSWORD_BLOCK);
                }
                break;
            case 3802: // mace
                player.setFightType(FightType.MACE_POUND);
                break;
            case 3805:
                player.setFightType(FightType.MACE_PUMMEL);
                break;
            case 3804:
                player.setFightType(FightType.MACE_SPIKE);
                break;
            case 3699:
                player.getAppearance().setGender(Gender.FEMALE);
                break;
            case 3698:
                player.getAppearance().setGender(Gender.MALE);
                break;
            case 3803:
                player.setFightType(FightType.MACE_BLOCK);
                break;
            case 55077:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] My title is: None.");
                player.getQuickChat().reset();
                break;
            case 55078:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] I have played for: " + Misc.getMinutesPlayed(player) + " minutes.");
                player.getQuickChat().reset();
                break;
            case 55079:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] My game mode is: "+player.getGameModeAssistant().getModeName()+".");
                player.getQuickChat().reset();
                break;
            case 55080:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] My donator rank is: "+player.getDonatorRights().getTitle()+".");
                player.getQuickChat().reset();
                break;
            case 55081:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] I have "+player.getPointsHandler().getPkPoints()+" pk points.");
                player.getQuickChat().reset();
                break;
            case 55082:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] I have "+player.getPointsHandler().getVotingPoints()+" vote points.");
                player.getQuickChat().reset();
                break;
            case 55083:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] I have "+player.getPointsHandler().getSlayerPoints()+" slayer points.");
                player.getQuickChat().reset();
                break;
            case 55084:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] I have "+player.getPointsHandler().getCommendations()+" commendations.");
                player.getQuickChat().reset();
                break;
            case 55085:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] I have "+player.getPointsHandler().getDungeoneeringTokens()+" dungeoneering tokens.");
                player.getQuickChat().reset();
                break;
            case 55087:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                int kdr = 0;
                if (player.getPlayerKillingAttributes().getPlayerDeaths() > 0) {
                    kdr = player.getPlayerKillingAttributes().getPlayerKills() / player.getPlayerKillingAttributes().getPlayerDeaths();
                } else {
                    kdr = player.getPlayerKillingAttributes().getPlayerKills();
                }
                player.forceChat("[Argos] My Kill/Death ration is: "+kdr+".");
                player.getQuickChat().reset();
                break;
            case 55088:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] I have a total of "+player.getPlayerKillingAttributes().getPlayerKills()+" kills.");
                player.getQuickChat().reset();
                break;
            case 55089:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] I have a total of "+player.getPlayerKillingAttributes().getPlayerDeaths()+" deaths.");
                player.getQuickChat().reset();
                break;
            case 55090:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] My killstreak is: "+player.getPlayerKillingAttributes().getPlayerKillStreak()+".");
                player.getQuickChat().reset();
                break;
            case 55091:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] I have won "+player.getDueling().arenaStats[0]+" duels.");
                player.getQuickChat().reset();
                break;
            case 55092:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
                player.forceChat("[Argos] I have lost "+player.getDueling().arenaStats[1]+" duels.");
                player.getQuickChat().reset();
                break;
            case 55095:
                if (!player.getQuickChat().elapsed(2500)) {
                    player.getPacketSender().sendMessage("Please wait 3 seconds before using the quick chat again.");
                    return;
                }
//                if (player.getSlayer().getSlayerTask() == SlayerTasks.NO_TASK) {
//                    player.forceChat("[Argos] I currently do not have a slayer task.");
//                } else {
//                    player.forceChat("[Argos] My slayer task is: "+ Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " "))+"s.");
//                }
//                player.forceChat("[Argos] My slayer task is: "+ Misc.formatText(player.getSlayer().getSlayerMaster().toString().toLowerCase().replaceAll("_", " "))+".");
//                player.getQuickChat().reset();
                break;

            case 4454: // knife, thrownaxe, dart & javelin
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_ACCURATE);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_ACCURATE);
                }
                break;
            case 4453:
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_RAPID);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_RAPID);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_RAPID);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_RAPID);
                }
                break;
            case 4452:
                if (player.getWeapon() == WeaponInterface.KNIFE) {
                    player.setFightType(FightType.KNIFE_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.THROWNAXE) {
                    player.setFightType(FightType.THROWNAXE_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.DART) {
                    player.setFightType(FightType.DART_LONGRANGE);
                } else if (player.getWeapon() == WeaponInterface.JAVELIN) {
                    player.setFightType(FightType.JAVELIN_LONGRANGE);
                }
                break;
            case 4685: // spear
                player.setFightType(FightType.SPEAR_LUNGE);
                break;
            case 4688:
                player.setFightType(FightType.SPEAR_SWIPE);
                break;
            case 4687:
                player.setFightType(FightType.SPEAR_POUND);
                break;
            case 4686:
                player.setFightType(FightType.SPEAR_BLOCK);
                break;
            case 4711: // 2h sword
                player.setFightType(FightType.TWOHANDEDSWORD_CHOP);
                break;
            case 4714:
                player.setFightType(FightType.TWOHANDEDSWORD_SLASH);
                break;
            case 4713:
                player.setFightType(FightType.TWOHANDEDSWORD_SMASH);
                break;
            case 4712:
                player.setFightType(FightType.TWOHANDEDSWORD_BLOCK);
                break;
            case 5576: // pickaxe
                player.setFightType(FightType.PICKAXE_SPIKE);
                break;
            case 5579:
                player.setFightType(FightType.PICKAXE_IMPALE);
                break;
            case 5578:
                player.setFightType(FightType.PICKAXE_SMASH);
                break;
            case 5577:
                player.setFightType(FightType.PICKAXE_BLOCK);
                break;
            case 7768: // claws
                player.setFightType(FightType.CLAWS_CHOP);
                break;
            case 7771:
                player.setFightType(FightType.CLAWS_SLASH);
                break;
            case 7770:
                player.setFightType(FightType.CLAWS_LUNGE);
                break;
            case 7769:
                player.setFightType(FightType.CLAWS_BLOCK);
                break;
            case 8466: // halberd
                player.setFightType(FightType.HALBERD_JAB);
                break;
            case 8468:
                player.setFightType(FightType.HALBERD_SWIPE);
                break;
            case 8467:
                player.setFightType(FightType.HALBERD_FEND);
                break;
            case 5862: // unarmed
                player.setFightType(FightType.UNARMED_PUNCH);
                break;
            case 5861:
                player.setFightType(FightType.UNARMED_KICK);
                break;
            case 5860:
                player.setFightType(FightType.UNARMED_BLOCK);
                break;
            case 12298: // whip
                player.setFightType(FightType.WHIP_FLICK);
                break;
            case 12297:
                player.setFightType(FightType.WHIP_LASH);
                break;
            case 12296:
                player.setFightType(FightType.WHIP_DEFLECT);
                break;
            case 336: // staff
                player.setFightType(FightType.STAFF_BASH);
                break;
            case 335:
                player.setFightType(FightType.STAFF_POUND);
                break;
            case 334:
                player.setFightType(FightType.STAFF_FOCUS);
                break;
            case 433: // warhammer
                player.setFightType(FightType.WARHAMMER_POUND);
                break;
            case 432:
                player.setFightType(FightType.WARHAMMER_PUMMEL);
                break;
            case 431:
                player.setFightType(FightType.WARHAMMER_BLOCK);
                break;
            case 782: // scythe
                player.setFightType(FightType.SCYTHE_REAP);
                break;
            case 784:
                player.setFightType(FightType.SCYTHE_CHOP);
                break;
            case 785:
                player.setFightType(FightType.SCYTHE_JAB);
                break;
            case 783:
                player.setFightType(FightType.SCYTHE_BLOCK);
                break;
            case 1704: // battle axe
                player.setFightType(FightType.BATTLEAXE_CHOP);
                break;
            case 1707:
                player.setFightType(FightType.BATTLEAXE_HACK);
                break;
            case 1706:
                player.setFightType(FightType.BATTLEAXE_SMASH);
                break;
            case 1705:
                player.setFightType(FightType.BATTLEAXE_BLOCK);
                break;
            case 37006:
            	AchievementsInterface.showInterface(player, "skilling");
            	break;
            case 37011:
            	AchievementsInterface.showInterface(player, "combat");
            	break;
            case 37016:
            	AchievementsInterface.showInterface(player, "distractions");
            	break;
        }
    }

    private boolean checkOptionContainer(Player player, int id) {
        switch (id) {
            case 2461: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_1_OF_2))
                    return true;
                break;
            }
            case 2462: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_2_OF_2))
                    return true;
                break;
            }
            case 2471: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_1_OF_3))
                    return true;
                break;
            }
            case 2472: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_2_OF_3))
                    return true;
                break;
            }
            case 2473: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_3_OF_3))
                    return true;
                break;
            }
            case 2482: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_1_OF_4))
                    return true;
                break;
            }
            case 2483: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_2_OF_4))
                    return true;
                break;
            }
            case 2484: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_3_OF_4))
                    return true;
                break;
            }
            case 2485: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_4_OF_4))
                    return true;
                break;
            }
            case 2494: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_1_OF_5))
                    return true;
                break;
            }
            case 2495: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_2_OF_5))
                    return true;
                break;
            }
            case 2496: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_3_OF_5))
                    return true;
                break;
            }
            case 2497: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_4_OF_5))
                    return true;
                break;
            }
            case 2498: {
                if (player.getOptionContainer().handle(Option.OptionType.OPTION_5_OF_5))
                    return true;
                break;
            }
            case 8654:
                QuickChat.handleButtons(player, 0);
                break;
            case 8655:
                QuickChat.handleButtons(player, 3);
                break;
            case 8656:
                QuickChat.handleButtons(player, 14);
                break;
            case 8657:
                QuickChat.handleButtons(player, 2);
                break;
            case 8658:
                QuickChat.handleButtons(player, 16);
                break;
            case 8659:
                QuickChat.handleButtons(player, 13);
                break;
            case 8660:
                QuickChat.handleButtons(player, 1);
                break;
            case 8861:
                QuickChat.handleButtons(player, 15);
                break;
            case 8662:
                QuickChat.handleButtons(player, 10);
                break;
            case 8663:
                QuickChat.handleButtons(player, 4);
                break;
            case 8664:
                QuickChat.handleButtons(player, 17);
                break;
            case 8665:
                QuickChat.handleButtons(player, 7);
                break;
            case 8666:
                QuickChat.handleButtons(player, 5);
                break;
            case 8667:
                QuickChat.handleButtons(player, 12);
                break;
            case 8668:
                QuickChat.handleButtons(player, 11);
                break;
            case 8669:
                QuickChat.handleButtons(player, 6);
                break;
            case 8670:
                QuickChat.handleButtons(player, 9);
                break;
            case 8671:
                QuickChat.handleButtons(player, 8);
                break;
            case 8672:
                QuickChat.handleButtons(player, 20);
                break;
            case 12162:
                QuickChat.handleButtons(player, 18);
                break;
            case 13928:
                QuickChat.handleButtons(player, 19);
                break;
            case 28177: //construction
//                QuickChat.handleButtons(player, 0);
                break;
            case 28178:
                QuickChat.handleButtons(player, 22);
                break;
            case 28179:
                QuickChat.handleButtons(player, 23);
                break;
            case 28180:
                QuickChat.handleButtons(player, 24);
                break;

        }

        return false;
    }

    private boolean checkHandlers(Player player, int id) {
        if (player.isPlayerLocked() && id != 2458 && (id < 14873 && id > 14882)) {
            return true;
        }
        if (Titles.handleButtons(player, id)) {
            return true;
        }
        if (Sounds.handleButton(player, id)) {
            return true;
        }
        if (PrayerHandler.isButton(id)) {
            PrayerHandler.togglePrayerWithActionButton(player, id);
            return true;
        }
        if (CurseHandler.isButton(player, id)) {
            return true;
        }
        if (TeleportingInterface.isButton(player, id)) {
            return true;
        }
        if (Autocasting.handleAutocast(player, id)) {
            return true;
        }
        if (SmithingData.handleButtons(player, id)) {
            return true;
        }
        if (PouchMaking.pouchInterface(player, id)) {
            return true;
        }
        if (Fletching.fletchingButton(player, id)) {
            return true;
        }
        if (LeatherMaking.handleButton(player, id) || Tanning.handleButton(player, id)) {
            return true;
        }
        if (Emotes.doEmote(player, id)) {
            return true;
        }
        if (PestControl.handleInterface(player, id)) {
            return true;
        }
        if (player.getLocation() == Location.DUEL_ARENA && Dueling.handleDuelingButtons(player, id)) {
            return true;
        }
        if (ExperienceLamps.handleButton(player, id)) {
            return true;
        }
        if (PlayersOnlineInterface.handleButton(player, id)) {
            return true;
        }
        if (PlayerOwnedShops.posButtons(player, id)) {
            return true;
        }
        if (ClanChatManager.handleClanChatSetupButton(player, id)) {
            return true;
        }
        return false;
    }

    public static final int OPCODE = 185;
}
