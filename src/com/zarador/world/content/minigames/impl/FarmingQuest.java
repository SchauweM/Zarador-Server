package com.zarador.world.content.minigames.impl;

import com.zarador.model.Skill;
import com.zarador.world.content.PlayerPanel;
import com.zarador.world.entity.impl.player.Player;

/**
 * @author High105
 */
public class FarmingQuest {

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
			if (p.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 0)
				p.getPacketSender().sendString(i + 4, "" + questGuide[questGuideIndex]);
			else
				p.getPacketSender().sendString(i + 4, "@str@" + questGuide[questGuideIndex] + "");
			questGuideIndex++;
		}
		p.getPacketSender().sendString(8150, "Skill Requirements");
		if (p.getSkillManager().getMaxLevel(17) < 70)
			p.getPacketSender().sendString(8151, "Thieving: 70");
		else
			p.getPacketSender().sendString(8151, "@str@Thieving: 70");
		if (p.getSkillManager().getMaxLevel(8) < 60)
			p.getPacketSender().sendString(8152, "Woodcutting: 60");
		else
			p.getPacketSender().sendString(8152, "@str@Woodcutting: 60");
		if (p.getSkillManager().getMaxLevel(19) < 60)
			p.getPacketSender().sendString(8153, "Farming: 60");
		else
			p.getPacketSender().sendString(8153, "@str@Farming: 60");
		if (p.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 1) {
			p.getPacketSender().sendString(8156, "Vanessa has agreed to help me with my farming ability");
			p.getPacketSender().sendString(8157, "if I help her gather the following items: a pair of ");
			p.getPacketSender().sendString(8158, "secateurs and a dramen branch. I can talk to her");
			p.getPacketSender().sendString(8159, "for information on how to get those items. I must");
			p.getPacketSender().sendString(8160, "speak to Vanessa with the items in my inventory.");
		} else if (p.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 2) {
			p.getPacketSender().sendString(8156, "@str@Vanessa has agreed to help me with my farming ability");
			p.getPacketSender().sendString(8157, "@str@if I help her gather the following items: a pair of ");
			p.getPacketSender().sendString(8158, "@str@secateurs and a dramen branch. I can talk to her");
			p.getPacketSender().sendString(8159, "@str@for information on how to get those items. I must");
			p.getPacketSender().sendString(8160, "@str@speak to Vanessa with the items in my inventory.");
			p.getPacketSender().sendString(8161, "Now that Vanessa has the secateurs and a dramen branch");
			p.getPacketSender().sendString(8162, "I need to put 100 weeds in the compost bin to charge");
			p.getPacketSender().sendString(8163, "the magic secateurs.");
		} else if (p.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 3) {
			p.getPacketSender().sendString(8156, "@str@Vanessa has agreed to help me with my farming ability");
			p.getPacketSender().sendString(8157, "@str@if I help her gather the following items: a pair of ");
			p.getPacketSender().sendString(8158, "@str@secateurs and a dramen branch. I can talk to her");
			p.getPacketSender().sendString(8159, "@str@for information on how to get those items. I must");
			p.getPacketSender().sendString(8160, "@str@speak to Vanessa with the items in my inventory.");
			p.getPacketSender().sendString(8161, "@str@Now that Vanessa has the secateurs and a dramen branch");
			p.getPacketSender().sendString(8162, "@str@I need to put 100 weeds in the compost bin to charge");
			p.getPacketSender().sendString(8163, "@str@the magic secateurs.");
			p.getPacketSender().sendString(8164, "I should inform Vanessa I've filled the compost bin.");
		} else if (p.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() == 4) {
			p.getPacketSender().sendString(8156, "@str@Vanessa has agreed to help me with my farming ability");
			p.getPacketSender().sendString(8157, "@str@if I help her gather the following items: a pair of ");
			p.getPacketSender().sendString(8158, "@str@secateurs and a dramen branch. I can talk to her");
			p.getPacketSender().sendString(8159, "@str@for information on how to get those items. I must");
			p.getPacketSender().sendString(8160, "@str@speak to Vanessa with the items in my inventory.");
			p.getPacketSender().sendString(8161, "@str@Now that Vanessa has the secateurs and a dramen branch");
			p.getPacketSender().sendString(8162, "@str@I need to put 100 weeds in the compost bin to charge");
			p.getPacketSender().sendString(8163, "@str@the magic secateurs.");
			p.getPacketSender().sendString(8164, "@str@I should inform Vanessa I've filled the compost bin.");
			p.getPacketSender().sendString(8165, "@dre@Quest complete!");
		}
	}

	public static void giveReward(Player p) {
		p.getPacketSender().sendInterface(12140);
		p.getPacketSender().sendString(12144, "You have completed: Farmer's Expedition");
		p.getPacketSender().sendString(12150, "A pair of magic secateurs");
		p.getPacketSender().sendString(12151, "Using Magic Secateurs gives 20% more crops");
		p.getPacketSender().sendString(12152, "50k farming xp");
		p.getPacketSender().sendString(12153, "");
		p.getPacketSender().sendString(12154, "");
		p.getPacketSender().sendString(12155, "");
		p.getPacketSender().sendString(12147, "3");
		p.getInventory().add(7409, 1);
		p.getSkillManager().addExactExperience(Skill.FARMING, 50000, false);
		p.addQuestPoints(3);
		PlayerPanel.refreshPanel(p);
	}

	public static String getQuestTabPrefix(Player player) {
		if (player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() >= 4) {
			return "@gre@";
		} else if (player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() != 0
				&& player.getMinigameAttributes().getFarmQuestAttributes().getQuestParts() < 10) {
			return "@yel@";
		} else {
			return "@red@";
		}
	}

	private static final String questTitle = "Farmer's Expedition";
	private static final String[] questIntro = { "Vanessa is said to have the power to make farming",
			"abilities better and stronger. I just need to", "gather the needed supplies for her.", };
	private static final String[] questGuide = { "I can start this quest by speaking with Vanessa in",
			"the Catherby farming shop." };
}
