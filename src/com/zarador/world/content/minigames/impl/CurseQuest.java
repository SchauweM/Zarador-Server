package com.zarador.world.content.minigames.impl;

import com.zarador.world.entity.impl.player.Player;

public class CurseQuest {

	public static String getQuestTabPrefix(Player player) {
		// if (player.getMinigameAttributes().getCurseQuestAttributes()
		// .hasFinishedPart(0)
		// && player.getMinigameAttributes()
		// .getCurseQuestAttributes().getWavesCompleted() < 6) {
		// return "@yel@";
		// }
		if (player.getMinigameAttributes().getCurseQuestAttributes().getWavesCompleted() == 6) {
			return "@gre@";
		}
		return "@red@";
	}

	public static void openQuestLog(Player p) {
		for (int i = 8145; i < 8196; i++)
			p.getPacketSender().sendString(i, "");
		p.getPacketSender().sendInterface(8134);
		p.getPacketSender().sendString(8136, "Close window");
		p.getPacketSender().sendString(8144, "" + questTitle);
		p.getPacketSender().sendString(8145, "");
		int questIntroIndex = 0;
		for (int i = 8147; i < 8147 + questIntro.length; i++) {
			p.getPacketSender().sendString(i, "@dre@" + questIntro[questIntroIndex]);
			questIntroIndex++;
		}
		int questGuideIndex = 0;
		for (int i = 8147 + questIntro.length; i < 8147 + questIntro.length + questGuide.length; i++) {
			// if (!p.getMinigameAttributes().getCurseQuestAttributes()
			// .hasFinishedPart(questGuideIndex))
			p.getPacketSender().sendString(i, "" + questGuide[questGuideIndex]);
			// else
			p.getPacketSender().sendString(i, "@str@" + questGuide[questGuideIndex] + "");
			if (questGuideIndex == 2) {
				// if (p.getMinigameAttributes().getCurseQuestAttributes()
				// .getWavesCompleted() > 0
				// && !p.getMinigameAttributes()
				// .getCurseQuestAttributes()
				// .hasFinishedPart(questGuideIndex))
				p.getPacketSender().sendString(i, "@yel@" + questGuide[questGuideIndex]);
				if (p.getMinigameAttributes().getCurseQuestAttributes().getWavesCompleted() == 6)
					p.getPacketSender().sendString(i, "@str@" + questGuide[questGuideIndex] + "");
			}
			questGuideIndex++;
		}
		if (p.getMinigameAttributes().getCurseQuestAttributes().getWavesCompleted() == 6)
			p.getPacketSender().sendString(8147 + questIntro.length + questGuide.length, "@dre@Quest complete!");
	}

	private static final String questTitle = "Prayers of a Killer";
	private static final String[] questIntro = { "The Culinaromancer has returned and only you",
			"             can stop him!                  ", "", };
	private static final String[] questGuide = { "Talk to the Gypsy found in her tent in Varrock",
			"and agree to help her, Enter the portal.", "Defeat the following bosses:",
			"- Agrith-Na-Na",
			"- Flambeed",
			"- Karamel",
			"- Dessourt",
			"Gelatinnoth mother",
			"And finally... Defeat the Culinaromancer!" };

}
