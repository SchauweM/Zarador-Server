package com.zarador.model.player;

import org.scripts.kotlin.content.dialog.ChangeGameMode;
import org.scripts.kotlin.content.dialog.Rfd;
import org.scripts.kotlin.content.dialog.healers.Healers;
import org.scripts.kotlin.content.dialog.healers.HealersQuickOption;
import org.scripts.kotlin.content.dialog.npcs.*;

import com.zarador.GameSettings;
import com.zarador.model.*;
import com.zarador.model.container.impl.Shop;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.model.input.impl.PosSearchShop;
import com.zarador.net.packet.impl.UseItemPacketListener;
import com.zarador.world.content.*;
import com.zarador.world.content.combat.magic.CombatSpells;
import com.zarador.world.content.pos.PlayerOwnedShops;
import com.zarador.world.content.skill.impl.crafting.Tanning;
import com.zarador.world.content.skill.impl.fishing.Fishing;
import com.zarador.world.content.skill.impl.hunter.PuroPuro;
import com.zarador.world.content.skill.impl.mining.Mining;
import com.zarador.world.content.skill.impl.runecrafting.DesoSpan;
import com.zarador.world.content.skill.impl.slayer.SlayerDialog;
import com.zarador.world.content.skill.impl.slayer.SlayerMasters;
import com.zarador.world.content.skill.impl.summoning.BossPets;
import com.zarador.world.content.skill.impl.summoning.Summoning;
import com.zarador.world.content.skill.impl.summoning.SummoningData;
import com.zarador.world.content.skill.impl.thieving.ThievingManager;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 8/4/2016.
 *
 * @author Seba
 */
public final class ActionHandler {

    private final Player player;

    public ActionHandler(Player player) {
        this.player = player;
    }

    public void firstClickNpc(NPC npc) {
        if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
                && player.getBankPinAttributes().onDifferent(player)) {
            BankPin.init(player, false);
            return;
        }
        player.setEntityInteraction(npc);
        player.setNpcClickId(npc.getId());
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender().sendMessage("First click npc id: " + npc.getId());
        if (BossPets.talkTo(player, npc)) {
            player.getWalkingQueue().clear();
            return;
        }
        if (npc.getDialogue(player) != null) {
            player.getDialog().sendDialog(npc.getDialogue(player));
            return;
        }
        if (SummoningData.beastOfBurden(npc.getId())) {
            Summoning summoning = player.getSummoning();
            if (summoning.getBeastOfBurden() != null && summoning.getFamiliar() != null
                    && summoning.getFamiliar().getSummonNpc() != null
                    && summoning.getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
                summoning.store();
                player.getWalkingQueue().clear();
            } else {
                player.getPacketSender().sendMessage("That familiar is not yours!");
            }
            return;
        }
        switch (npc.getId()) {
            //pk sets
            case 2828:
                player.getDialog().sendDialog(new PkSetsDialogue(player));
                break;

            //Legendary donator shop
            case 590:
                if (player.getDonatorRights().ordinal() >= 3) {
                    player.getDialog().sendDialog(new Aemad(player));
                } else {
                    player.getPacketSender()
                            .sendMessage("You need to be an Legendary donator or higher, to access this shop!");
                }
                break;

            //game mode changer
            case 3642:
                player.getDialog().sendDialog(new ChangeGameMode(player));
                break;
            //sheep
            case 43:
                Sheep.shearSheep(player, npc);
                break;

            //mercant
            case 1282:
                player.getDialog().sendDialog(new Merchant(player));
                break;

            //merchant POS
            case 2593:
                player.getDialog().sendDialog(new POSMerchant(player));
                break;

            //tool leprichaun
            case 3021:
                player.getDialog().sendDialog(new ToolLeprechaun(player, 0));
                break;

            //Dungeoneering master (thok)
            case 9713:
                player.getDialog().sendDialog(new Thok(player, 0));
                break;

            //pumpkin pete
            case 12378:
                player.getDialog().sendDialog(new PumpkinPete(player, 0));
                break;

            //Santa
            case 8540:
                player.getDialog().sendDialog(new Santa(player));
                break;


            //Gambler
            case 2998:
                player.getDialog().sendDialog(new Gambler(player, 0, 1));
                break;

            //Runecrafting npc
            case 249:
                npc.performAnimation(CombatSpells.CONFUSE.getSpell().castAnimation().get());
                npc.forceChat("Off you go!");
                TeleportHandler.teleportPlayer(player, new Position(2601, 4770),
                        player.getSpellbook().getTeleportType());
                break;

            //Donovan
            case 4252:
                player.getDialog().sendDialog(new Donovan(player, 0));
                break;

            //Max npc
            case 2579:
                player.getDialog().sendDialog(new Max(player));
                break;

            //Ellis donator zone
            case 595:
                player.getDialog().sendDialog(new Ellis(player, 0));
                break;

            //Miner magnus
            case 1396:
                player.getDialog().sendDialog(new MinerMagnus(player, 0));
                break;

            //summoning
            case 6970:
                player.getDialog().sendDialog(new Pikkupstix(player, 0));
                break;
            //Rfd
            case 3385:
                if (player.getMinigameAttributes().getRecipeForDisasterAttributes()
                        .hasFinishedPart(0)
                        && player.getMinigameAttributes().getRecipeForDisasterAttributes()
                        .getWavesCompleted() < 6) {
                    player.getDialog().sendDialog(new Rfd(player, 2));
                    return;
                }
                if (player.getMinigameAttributes().getRecipeForDisasterAttributes()
                        .getWavesCompleted() == 6) {
                    player.getDialog().sendDialog(new Rfd(player, 6));
                    return;
                }
                player.getDialog().sendDialog(new Rfd(player, 0));
                break;

            //Warriors Guild
            case 650:
                Shop.ShopManager.getShops().get(31).open(player);
                break;

            //Aubury
            case 553:
                player.getDialog().sendDialog(new Aubury(player));
                break;

            //David skilling shop
            case 817:
                player.getDialog().sendDialog(new David(player));
                break;

            //Aleck hunter shop
            case 5110:
                player.getDialog().sendDialog(new Aleck(player, 0));
                break;

            //Martin fishing shop
            case 308:
                player.getDialog().sendDialog(new Martin(player));
                break;

            //Slayer masters
            case 401:
            case 402:
            case 403:
            case 404:
            case 405:
            case 490:
            case 9085:
                player.getDialog().sendDialog(new SlayerDialog(player, 0, null));
                break;

            //tokkul
            case 2622:
                Shop.ShopManager.getShops().get(17).open(player);
                break;

            //rc shop
            case 460:
                player.getDialog().sendDialog(new Frumscone(player));
                break;
            //farmer shop
            case 3917:
                Shop.ShopManager.getShops().get(25).open(player);
                break;

            //Bankers
            case 6362:
            case 494:
            case 4519:
            case 9710:
                player.getBank(0).open();
                break;

            //Oziach
            case 747:
                player.getDialog().sendDialog(new Oziach(player));
                break;

            //Duel arena healers
            case 959:
            case 960:
            case 961:
            case 962:
                player.getDialog().sendDialog(new Healers(player));
                break;

            //Talk to Horvik
            case 549:
                player.getDialog().sendDialog(new Horvik(player));
                break;

            //Talk to Zeke
            case 541:
                player.getDialog().sendDialog(new Zeke(player));
                break;

            //Talk to Zaff
            case 546:
                player.getDialog().sendDialog(new Zaff(player));
                break;

            //Talk to Lowe
            case 550:
                player.getDialog().sendDialog(new Lowe(player));
                break;

            //Talk to Head snow imp
            case 8542:
                player.getDialog().sendDialog(new HeadSnowImp(player));
                break;

            //Barrows brother
            case 572:
                player.getDialog().sendDialog(new BarrowsBrother(player, 0));
                break;

            //Talk to Giles
            case 2538:
                player.getDialog().sendDialog(new Giles(player));
                break;

            //Talk to Thessalia
            case 548:
                player.getDialog().sendDialog(new Thessalia(player));
                break;

            //Talk to Shopkeeper
            case 520:
                player.getDialog().sendDialog(new Shopkeeper(player));
                break;

            //Talk to Wise Old Man
            case 2253:
                player.getDialog().sendDialog(new WiseOldMan(player));
                break;

            //Talk to Bob
            case 519:
                player.getDialog().sendDialog(new Bob(player));
                break;

            //Talk to Sailor
            case 1304:
                player.getDialog().sendDialog(new Sailor(player, 0));
                break;

            //Talk to Argos guide
            case 945:
                player.getDialog().sendDialog(new ArgosGuide(player));
                break;

            //Agility penguin
            case 5428:
                player.getDialog().sendDialog(new AgilityPenguin(player, 0));
                break;

            //Master crafter
            case 805:
                player.getDialog().sendDialog(new MasterCrafter(player, 0));
                break;

            //Living rock minerals
            case 8839:
                Mining.startMining(player, new GameObject(30000, npc.getPosition()), npc);
                break;

            case 501:
                for (Item item : player.getInventory().getItems()) {
                    ItemDefinition def = ItemDefinition.forId(item.getId() + 1);
                    if (def.getName().toLowerCase().startsWith("raw")) {
                        if (def.isNoted()) {
                            int notedId = item.getId() + 1;
                            player.getInventory().delete(item.getId(), 1);
                            player.getInventory().add(notedId, 1);
                        }
                    }
                }
                player.getDialog().sendDialog(new QueenRevenant(player));
                break;

            case 8710:
            case 8707:
            case 8706:
            case 8705:
                EnergyHandler.rest(player);
                break;
            case 3789:
                player.getDialog().sendDialog(new VoidKnight(player));
                break;
            case 6055:
            case 6056:
            case 6057:
            case 6058:
            case 6059:
            case 6060:
            case 6061:
            case 6062:
            case 6063:
            case 6064:
            case 7903:
            case 1030:
                PuroPuro.catchImpling(player, npc);
                break;
            case 8022:
            case 8028:
                DesoSpan.siphon(player, npc);
                break;
            case 6807:
            case 6994:
            case 6995:
            case 6867:
            case 6868:
            case 6794:
            case 6795:
            case 6815:
            case 6816:
            case 6874:
            case 6873:
            case 3594:
            case 3590:
            case 3596:
                if (player.getSummoning().getFamiliar() == null
                        || player.getSummoning().getFamiliar().getSummonNpc() == null
                        || player.getSummoning().getFamiliar().getSummonNpc().getIndex() != npc.getIndex()) {
                    player.getPacketSender().sendMessage("That is not your familiar.");
                    return;
                }
                player.getSummoning().store();
                break;
            case 322:
            case 321:
            case 318:
            case 316:
            case 313:
            case 312:
            case 309:
            case 2724:
            case 3019:
            case 2722:
            case 2859:
                player.setEntityInteraction(npc);
                Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), false));
                break;
            case 2676:
                player.getPacketSender().sendInterface(3559);
                player.getAppearance().setCanChangeAppearance(true);
                break;
        }
        if (!(npc.getId() >= 8705 && npc.getId() <= 8710) && npc.getId() != 1396) {
            npc.setPositionToFace(player.getPosition());
        }
        player.setPositionToFace(npc.getPosition());
    }

    public void secondClickNpc(NPC npc) {
        if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
                && player.getBankPinAttributes().onDifferent(player)) {
            BankPin.init(player, false);
            return;
        }
        player.setEntityInteraction(npc);
        player.setNpcClickId(npc.getId());
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender().sendConsoleMessage("Second click npc id: " + npc.getId());
        if (BossPets.pickup(player, npc)) {
            player.getWalkingQueue().clear();
            return;
        }
        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in NPCOptionPacketListener: " + npc.getId() + " -
            // SECOND_CLICK_OPCODE");
        }
        switch (npc.getId()) {
            //Open player owned stores item search
            case 2593:
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

            //Untradeable buy back store
            case 970:
                //player.openUnTradeableShop(player, player.itemToBuyBack);
                break;
            //Agility penguin
            case 5428:
                player.getDialog().sendDialog(new AgilityPenguin(player, 5));
                break;

            //void knight shop
            case 3789:
                Shop.ShopManager.getShops().get(47).open(player);
                break;

            //Legendary donator shop
            case 590:
                if (player.getDonatorRights().ordinal() >= 3) {
                    Shop.ShopManager.getShops().get(49).open(player);
                } else {
                    player.getPacketSender()
                            .sendMessage("You need to be an Legendary donator or higher, to access this shop!");
                }
                break;

            //Travel with Sailor
            case 1304:
                player.getDialog().sendDialog(new Sailor(player, 1));
                break;

            //Thok dungeoneering master
            case 9713:
                Shop.ShopManager.getShops().get(46).open(player);
                break;

            //Ellis donator shop
            case 595:
                Shop.ShopManager.getShops().get(45).open(player);
                break;

            //Gambler
            case 2998:
                Shop.ShopManager.getShops().get(7).open(player);
                break;

            //mercant
            case 1282:
                Shop.ShopManager.getShops().get(0).open(player);
                break;
            //summoning
            case 6970:
                player.getDialog().sendDialog(new Pikkupstix(player, 1));
                break;

            //tool leprichaun
            case 3021:
                for (Item item : player.getInventory().getItems()) {
                    Item noted = UseItemPacketListener.getNotedHarvest(player, item);
                    if (noted == null) {

                    } else {
                        int amount = player.getInventory().getAmount(item.getId());
                        player.getInventory().delete(new Item(item.getId(), amount));
                        player.getInventory().add(new Item(noted.getId(), amount));
                    }
                }
                player.getPacketSender().sendMessage("You exchange your harvested crops for their noted form.");
                player.getDialog().sendDialog(new ToolLeprechaun(player, 2));
                break;

            //Aubury
            case 553:
                Shop.ShopManager.getShops().get(29).open(player);
                break;

            //David skilling shop
            case 817:
                if (player.getGameModeAssistant().getGameMode() == GameMode.IRONMAN) {
                    Shop.ShopManager.getShops().get(40).open(player);
                } else {
                    Shop.ShopManager.getShops().get(30).open(player);
                }
                break;

            //Aleck hunter shop
            case 5110:
                Shop.ShopManager.getShops().get(39).open(player);
                break;

            //Miner magnus shop
            case 1396:
                Shop.ShopManager.getShops().get(44).open(player);
                break;

            //Martin fishing shop
            case 308:
                Shop.ShopManager.getShops().get(38).open(player);
                break;

            //Slayer masters
            case 401:
            case 402:
            case 403:
            case 404:
            case 405:
            case 490:
            case 9085:
                if (player.getSlayer().hasMasterRequirements(SlayerMasters.forNpcId(npc.getId()))) {
                    player.getSlayer().assignSlayerTask(SlayerMasters.forNpcId(npc.getId()), false);
                } else {
                    player.getDialog().sendDialog(new SlayerDialog(player, 9, null));
                }
                break;

            //tokkul
            case 2622:
                Shop.ShopManager.getShops().get(17).open(player);
                break;

            //Bankers
            case 494:
            case 4519:
            case 6362:
            case 9710:
                player.getBank(0).open();
                break;

            //Oziach
            case 747:
                Artifacts.sellArtifacts(player);
                break;
            //Duel arena healers
            case 959:
            case 960:
            case 961:
            case 962:
                player.getDialog().sendDialog(new HealersQuickOption(player));
                break;

            //Horvik's armour shop
            case 549:
                Shop.ShopManager.getShops().get(1).open(player);
                break;

            //Zeke's weapon shop
            case 541:
                Shop.ShopManager.getShops().get(2).open(player);
                break;

            //Zaff's magic shop
            case 546:
                Shop.ShopManager.getShops().get(5).open(player);
                break;

            //Lowe's range shop
            case 550:
                Shop.ShopManager.getShops().get(4).open(player);
                break;

            //Head snow imp's shop
            case 8542:
                Shop.ShopManager.getShops().get(24).open(player);
                break;

            //Giles's supply shop
            case 2538:
                Shop.ShopManager.getShops().get(3).open(player);
                break;

            //Thessalia's clothes store
            case 548:
                Shop.ShopManager.getShops().get(20).open(player);
                break;

            //Shopkeeper's general store
            case 520:
                Shop.ShopManager.getShops().get(0).open(player);
                break;

            case 1:
            case 2:
            case 3:
            case 7:
            case 4:
            case 9:
            case 1714:
            case 1715:
            case 18:
            case 23:
            case 32:
            case 26:
            case 20:
            case 2234:
            case 21:
            case 34:
            case 1307:
            case 1305:
            case 1306:
            case 1311:
            case 1310:
            case 1308:
            case 1314:
                ThievingManager.initMobData(player, ThievingManager.forMobData(npc.getId()));
                break;
            case 6874:
            case 6873:
                if (player.getSummoning().getFamiliar() == null
                        || player.getSummoning().getFamiliar().getSummonNpc() == null
                        || player.getSummoning().getFamiliar().getSummonNpc().getIndex() != npc.getIndex()) {
                    player.getPacketSender().sendMessage("That is not your familiar.");
                    return;
                }
                player.getSummoning().store();
                break;
            case 462:
                npc.performAnimation(CombatSpells.CONFUSE.getSpell().castAnimation().get());
                npc.forceChat("Off you go!");
                TeleportHandler.teleportPlayer(player, new Position(2911, 4832),
                        player.getSpellbook().getTeleportType());
                break;
            case 4657:

                break;
            case 805:
                Tanning.selectionInterface(player);
                break;
            case 318:
            case 316:
            case 313:
            case 312:
                player.setEntityInteraction(npc);
                Fishing.setupFishing(player, Fishing.forSpot(npc.getId(), true));
                break;
        }
        if (npc.getId() != 1396) {
            npc.setPositionToFace(player.getPosition());
        }
        player.setPositionToFace(npc.getPosition());
    }

    public void thirdClickNpc(NPC npc) {
        if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
                && player.getBankPinAttributes().onDifferent(player)) {
            BankPin.init(player, false);
            return;
        }
        player.setEntityInteraction(npc).setPositionToFace(npc.getPosition().copy());
        if (npc.getId() != 1396) {
            npc.setPositionToFace(player.getPosition());
        }
        player.setNpcClickId(npc.getId());
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender().sendMessage("Third click npc id: " + npc.getId());

        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in NPCOptionPacketListener: " + npc.getId() + " -
            // THIRD_CLICK_OPCODE");
        }
        switch (npc.getId()) {

            //Open your pos store
            case 2593:
                if (!GameSettings.POS_ENABLED) {
                    player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                    return;
                }
                if (player.getGameModeAssistant().isIronMan()) {
                    player.getPacketSender().sendMessage("Ironmen can't use the player owned shops!");
                    return;
                }
                player.getPacketSender().sendString(41900, "");
                PlayerOwnedShops.openShop(player.getUsername(), player);
                player.setPlayerOwnedShopping(true);
                break;

            //tool leprichaun
            case 3021:
                player.getDialog().sendDialog(new ToolLeprechaun(player, 3));
                break;

            //Slayer masters
            case 401:
            case 402:
            case 403:
            case 404:
            case 405:
            case 490:
            case 9085:
                Shop.ShopManager.getShops().get(32).open(player);
                break;

            //Agility penguin
            case 5428:
                player.getDialog().sendDialog(new AgilityPenguin(player, 4));
                break;

            //Aubury
            case 553:
                npc.performAnimation(CombatSpells.CONFUSE.getSpell().castAnimation().get());
                npc.forceChat("Off you go!");
                TeleportHandler.teleportPlayer(player, new Position(2911, 4832, 0), player.getSpellbook().getTeleportType());
                break;

            //Aleck hunter shop
            case 5110:
                player.getDialog().sendDialog(new Aleck(player, 2));
                break;

            case 961:
                //Stat restore npc here
                break;
        }
        if (npc.getId() != 1396) {
            npc.setPositionToFace(player.getPosition());
        }
        player.setPositionToFace(npc.getPosition());
    }

    public void fourthClickNpc(NPC npc) {
        if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
                && player.getBankPinAttributes().onDifferent(player)) {
            BankPin.init(player, false);
            return;
        }
        player.setEntityInteraction(npc);
        player.setNpcClickId(npc.getId());
        if (player.getStaffRights().isDeveloper(player))
            player.getPacketSender().sendMessage("Fourth click npc id: " + npc.getId());

        if (GameSettings.DEBUG_MODE) {
            // PlayerLogs.log(player, "" + player.getUsername()
            // + " in NPCOptionPacketListener: " + npc.getId() + " -
            // FOURTH_CLICK_OPCODE");
        }
        switch (npc.getId()) {
            //Open someones POS
            case 2593:
                if (!GameSettings.POS_ENABLED) {
                    player.getPacketSender().sendMessage("Player owned shops have been disabled.");
                    return;
                }
                player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
                player.setInputHandling(new PosSearchShop());
                break;

            //Slayer masters
            case 401:
            case 402:
            case 403:
            case 404:
            case 405:
            case 490:
            case 9085:
//                if (player.getGameModeAssistant().isIronMan()) {
//                    Shop.ShopManager.getShops().get(33).open(player);
//                } else {
                    Shop.ShopManager.getShops().get(15).open(player);
//                }
                break;
            case 2217:
                player.getPacketSender().sendEnterInputPrompt("Enter the name of a player's shop:");
                break;
            case 4657:
                if (!player.getDonatorRights().isDonator()) {
                    player.getPacketSender().sendMessage("You need to be a member to teleport to this zone.")
                            .sendMessage("To become a member, visit zarador.net and purchase a scroll.");
                    return;
                }
                TeleportHandler.teleportPlayer(player, new Position(3424, 2919),
                        player.getSpellbook().getTeleportType());
                break;
            case 705:
                Shop.ShopManager.getShops().get(7).open(player);
                break;
            case 2253:
                Shop.ShopManager.getShops().get(9).open(player);
                break;
        }

        if (npc.getId() != 1396) {
            npc.setPositionToFace(player.getPosition());
        }
        player.setPositionToFace(npc.getPosition());
    }

}
