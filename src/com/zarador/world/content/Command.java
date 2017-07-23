package com.zarador.world.content;

import com.zarador.GameSettings;
import com.zarador.world.entity.impl.player.Player;

public class Command {

	public static void open(Player player) {
		try {
			/* SHOW THE INTERFACE */
			player.setKillsTrackerOpen(true);
			resetInterface(player);
			player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, 55000);
			player.getPacketSender().sendString(55001, "@or2@  Commands");
			player.getPacketSender().sendString(55020, "@or2@ Player Commands");
			player.getPacketSender().sendString(55021, "@or3@ - @whi@ ::auth [auth]");
			player.getPacketSender().sendString(55022, "@or3@ - @whi@ ::save");
			player.getPacketSender().sendString(55023, "@or3@ - @whi@ ::attacks");
			player.getPacketSender().sendString(55024, "@or3@ - @whi@ ::empty");
			player.getPacketSender().sendString(55025, "@or3@ - @whi@ ::bosses");
			player.getPacketSender().sendString(55026, "@or3@ - @whi@ ::changepass");
			player.getPacketSender().sendString(55027, "@or3@ - @whi@ ::droplist");
			player.getPacketSender().sendString(55028, "@or3@ - @whi@ ::edge");
			player.getPacketSender().sendString(55029, "@or3@ - @whi@ ::stuck");
			player.getPacketSender().sendString(55030, "@or3@ - @whi@ ::teamspeak");
			player.getPacketSender().sendString(55031, "@or3@ - @whi@ ::skull");
			player.getPacketSender().sendString(55032, "@or3@ - @whi@ ::gamble");
			player.getPacketSender().sendString(55033, "@or3@ - @whi@ ::forumrank");
			player.getPacketSender().sendString(55034, "@or2@ Server Support Commands");
			player.getPacketSender().sendString(55035, "@or3@ - @whi@ ::staffzone");
			player.getPacketSender().sendString(55036, "@or3@ - @whi@ ::jail [player]");
			player.getPacketSender().sendString(55037, "@or3@ - @whi@ ::unjail [player]");
			player.getPacketSender().sendString(55038, "@or3@ - @whi@ ::teleto [player]");
			player.getPacketSender().sendString(55039, "@or3@ - @whi@ ::movehome [player]");
			player.getPacketSender().sendString(55040, "@or3@ - @whi@ ::mute [player]");
			player.getPacketSender().sendString(55041, "@or3@ - @whi@ ::unmute [player]");
			player.getPacketSender().sendString(55042, "@or3@ - @whi@ ::kick [player]");
			player.getPacketSender().sendString(55043, "@or3@ - @whi@ ::scan [player]");
			player.getPacketSender().sendString(55044, "@or3@ - @whi@ ::host [player]");
			player.getPacketSender().sendString(55045, "@or2@ Moderator Commands");
			player.getPacketSender().sendString(55046, "@or3@ - @whi@ ::teletome [player]");
			player.getPacketSender().sendString(55047, "@or3@ - @whi@ ::ipmute [player]");
			player.getPacketSender().sendString(55048, "@or3@ - @whi@ ::ban [player]");
			player.getPacketSender().sendString(55049, "@or3@ - @whi@ ::unban [player]");
			player.getPacketSender().sendString(55050, "@or3@ - @whi@ ::ipban [player]");
			player.getPacketSender().sendString(55051, "@or3@ - @whi@ ::silenceyell [player]");
			player.getPacketSender().sendString(55052, "@or3@ - @whi@ ::unsilenceyell [player]");
			player.getPacketSender().sendString(55053, "@or3@ - @whi@ ::massban [player]");
			player.getPacketSender().sendString(55054, "@or3@ - @whi@ ::unmassban [player]");
			player.getPacketSender().sendString(55055, "@or2@ Administrator Commands");
			player.getPacketSender().sendString(55056, "@or3@ - @whi@ ::serialban [player]");
			player.getPacketSender().sendString(55057, "@or3@ - @whi@ ::unserialban [player]");
			player.getPacketSender().sendString(55058, "@or3@ - @whi@ ::gold [player]");
			player.getPacketSender().sendString(55059, "@or3@ - @whi@ ::globalyell");
			/*
			 * player.getPacketSender().sendString(55054, "@or3@ - @whi@ ");
			 * player.getPacketSender().sendString(55055, "@or3@ - @whi@ ");
			 * player.getPacketSender().sendString(55056, "@or3@ - @whi@ ");
			 * player.getPacketSender().sendString(55057, "@or3@ - @whi@ ");
			 * player.getPacketSender().sendString(55058, "@or3@ - @whi@ ");
			 * player.getPacketSender().sendString(55059, "@or3@ - @whi@ ");
			 * player.getPacketSender().sendString(55060, "@or3@ - @whi@ ");
			 * player.getPacketSender().sendString(55061, "@or3@ - @whi@ ");
			 * player.getPacketSender().sendString(55062, "@or3@ - @whi@ ");
			 * player.getPacketSender().sendString(55063, "@or3@ - @whi@ ");
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void resetInterface(Player player) {
		for (int i = 55020; i < 55064; i++) {
			player.getPacketSender().sendString(i, "");
		}
	}

}
