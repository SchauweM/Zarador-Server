package com.zarador.world.content.minigames.impl;

import com.zarador.world.content.PlayerPanel;
import com.zarador.world.entity.impl.player.Player;

/**
 * @author High105
 */
public class ClawQuest {

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
			if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 0)
				p.getPacketSender().sendString(i + 3, "" + questGuide[questGuideIndex]);
			else
				p.getPacketSender().sendString(i + 3, "@str@" + questGuide[questGuideIndex] + "");
			questGuideIndex++;
		}
		p.getPacketSender().sendString(8150, "Skill Requirements");
		if (p.getSkillManager().getMaxLevel(17) < 50)
			p.getPacketSender().sendString(8151, "Thieving: 50");
		else
			p.getPacketSender().sendString(8151, "@str@Thieving: 50");
		if (p.getSkillManager().getMaxLevel(10) < 90)
			p.getPacketSender().sendString(8152, "Fishing: 91");
		else
			p.getPacketSender().sendString(8152, "@str@Fishing: 91");
		if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 1) {
			p.getPacketSender().sendString(8155, "The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "He said something about her being in a pasture and");
			p.getPacketSender().sendString(8158, "along the river bank.");
		} else if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 2) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "He requested me to come back to him with whatever I found.");
		} else if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 3) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "I need to talk to Denath and convince him to agree to");
			p.getPacketSender().sendString(8161, "help the king. Then inform the king when he says yes.");
		} else if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 4) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "@str@I need to talk to Denath and convince him to agree to");
			p.getPacketSender().sendString(8161, "@str@help the king. Then inform the king when he says yes.");
			p.getPacketSender().sendString(8162, "I need to bring Denath 100 noted rocktails before he");
			p.getPacketSender().sendString(8163, "agrees to help the king. Then I must tell the king that");
			p.getPacketSender().sendString(8164, "Denath has agreed to help.");
		} else if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 5) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "@str@I need to talk to the GUY and convince him to agree to");
			p.getPacketSender().sendString(8161, "@str@help the king. Then inform the king when he says yes.");
			p.getPacketSender().sendString(8162, "@str@I need to bring Denath 100 noted rocktails before he");
			p.getPacketSender().sendString(8163, "@str@agrees to help the king. Then I must tell the king that");
			p.getPacketSender().sendString(8164, "@str@Denath has agreed to help.");
			p.getPacketSender().sendString(8165, "I must let the king know that Denath will help us.");
		} else if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 6) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "@str@I need to talk to the GUY and convince him to agree to");
			p.getPacketSender().sendString(8161, "@str@help the king. Then inform the king when he says yes.");
			p.getPacketSender().sendString(8162, "@str@I need to bring Denath 100 noted rocktails before he");
			p.getPacketSender().sendString(8163, "@str@agrees to help the king. Then I must tell the king that");
			p.getPacketSender().sendString(8164, "@str@Denath has agreed to help.");
			p.getPacketSender().sendString(8165, "@str@I must let the king know that Denath will help us.");
			p.getPacketSender().sendString(8166, "I must collect some samples from the banana tree's");
			p.getPacketSender().sendString(8167, "from ape toll and bring it to Denath.");
		} else if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 7) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "@str@I need to talk to the GUY and convince him to agree to");
			p.getPacketSender().sendString(8161, "@str@help the king. Then inform the king when he says yes.");
			p.getPacketSender().sendString(8162, "@str@I need to bring Denath 100 noted rocktails before he");
			p.getPacketSender().sendString(8163, "@str@agrees to help the king. Then I must tell the king that");
			p.getPacketSender().sendString(8164, "@str@Denath has agreed to help.");
			p.getPacketSender().sendString(8165, "@str@I must let the king know that Denath will help us.");
			p.getPacketSender().sendString(8166, "@str@I must collect some samples from the banana tree's");
			p.getPacketSender().sendString(8167, "@str@from ape toll and bring it to Denath.");
			p.getPacketSender().sendString(8168, "I should show the King what I helped Denath make.");
		} else if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 8) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "@str@I need to talk to the GUY and convince him to agree to");
			p.getPacketSender().sendString(8161, "@str@help the king. Then inform the king when he says yes.");
			p.getPacketSender().sendString(8162, "@str@I need to bring Denath 100 noted rocktails before he");
			p.getPacketSender().sendString(8163, "@str@agrees to help the king. Then I must tell the king that");
			p.getPacketSender().sendString(8164, "@str@Denath has agreed to help.");
			p.getPacketSender().sendString(8165, "@str@I must let the king know that Denath will help us.");
			p.getPacketSender().sendString(8166, "@str@I must collect some samples from the banana tree's");
			p.getPacketSender().sendString(8167, "@str@from ape toll and bring it to Denath.");
			p.getPacketSender().sendString(8168, "@str@I should show the King what I helped Denath make.");
			p.getPacketSender().sendString(8169, "The king promised me wealth and power if I help him out");
			p.getPacketSender().sendString(8170, "he also said I can keep anything I find on this new");
			p.getPacketSender().sendString(8171, "adventure into the khazard fight arena.");
			p.getPacketSender().sendString(8172, "He told me if I killed The Shaikahan I would");
			p.getPacketSender().sendString(8173, "be given my reward. He also said to bring my bravery");
			p.getPacketSender().sendString(8174, "potion and if I needed another to bring Denath more samples.");
			p.getPacketSender().sendString(8175, "I will need the Sinister key the king gives me when I ");
			p.getPacketSender().sendString(8176, "teleport. The key is what will give me access to the arena.");
		} else if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 9) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "@str@I need to talk to the GUY and convince him to agree to");
			p.getPacketSender().sendString(8161, "@str@help the king. Then inform the king when he says yes.");
			p.getPacketSender().sendString(8162, "@str@I need to bring Denath 100 noted rocktails before he");
			p.getPacketSender().sendString(8163, "@str@agrees to help the king. Then I must tell the king that");
			p.getPacketSender().sendString(8164, "@str@Denath has agreed to help.");
			p.getPacketSender().sendString(8165, "@str@I must let the king know that Denath will help us.");
			p.getPacketSender().sendString(8166, "@str@I must collect some samples from the banana tree's");
			p.getPacketSender().sendString(8167, "@str@from ape toll and bring it to Denath.");
			p.getPacketSender().sendString(8168, "@str@I should show the King what I helped Denath make.");
			p.getPacketSender().sendString(8169, "@str@The king promised me wealth and power if I help him out");
			p.getPacketSender().sendString(8170, "@str@he also said I can keep anything I find on this new");
			p.getPacketSender().sendString(8171, "@str@adventure into the khazard fight arena.");
			p.getPacketSender().sendString(8172, "@str@He told me if I killed The Shaikahan I would");
			p.getPacketSender().sendString(8173, "@str@be given my reward. He also said to bring my bravery");
			p.getPacketSender().sendString(8174, "@str@potion and if I needed another to bring Denath more samples.");
			p.getPacketSender().sendString(8175, "@str@I will need the Sinister key the king gives me when I ");
			p.getPacketSender().sendString(8176, "@str@teleport. The key is what will give me access to the arena.");
			p.getPacketSender().sendString(8177, "The Shaikahan's killed I need to tell King Healthorg and bring");
			p.getPacketSender().sendString(8178, "him the certificate to get my reward!");
		} else if (p.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 10) {
			p.getPacketSender().sendString(8155, "@str@The king wants me to go north of the yaks training");
			p.getPacketSender().sendString(8156, "@str@area and find Phingspet. She has something of his.");
			p.getPacketSender().sendString(8157, "@str@He said something about her being in a cow pasture and");
			p.getPacketSender().sendString(8158, "@str@along the river.");
			p.getPacketSender().sendString(8159, "@str@He requested me to come back to him with whatever I found.");
			p.getPacketSender().sendString(8160, "@str@I need to talk to the GUY and convince him to agree to");
			p.getPacketSender().sendString(8161, "@str@help the king. Then inform the king when he says yes.");
			p.getPacketSender().sendString(8162, "@str@I need to bring Denath 100 noted rocktails before he");
			p.getPacketSender().sendString(8163, "@str@agrees to help the king. Then I must tell the king that");
			p.getPacketSender().sendString(8164, "@str@Denath has agreed to help.");
			p.getPacketSender().sendString(8165, "@str@I must let the king know that Denath will help us.");
			p.getPacketSender().sendString(8166, "@str@I must collect some samples from the banana tree's");
			p.getPacketSender().sendString(8167, "@str@from ape toll and bring it to Denath.");
			p.getPacketSender().sendString(8168, "@str@I should show the King what I helped Denath make.");
			p.getPacketSender().sendString(8169, "@str@The king promised me wealth and power if I help him out");
			p.getPacketSender().sendString(8170, "@str@he also said I can keep anything I find on this new");
			p.getPacketSender().sendString(8171, "@str@adventure into the khazard fight arena.");
			p.getPacketSender().sendString(8172, "@str@He told me if I killed The Shaikahan I would");
			p.getPacketSender().sendString(8173, "@str@be given my reward. He also said to bring my bravery");
			p.getPacketSender().sendString(8174, "@str@potion and if I needed another to bring Denath more samples.");
			p.getPacketSender().sendString(8175, "@str@I will need the Sinister key the king gives me when I ");
			p.getPacketSender().sendString(8176, "@str@teleport. The key is what will give me access to the arena.");
			p.getPacketSender().sendString(8177, "@str@The Shaikahan's killed I need to tell King Healthorg and bring");
			p.getPacketSender().sendString(8178, "@str@him the certificate to get my reward!");
			p.getPacketSender().sendString(8179, "@dre@Quest complete!");
		}
	}

	public static void giveReward(Player p) {
		// p.getPacketSender().sendInterface(297);
		p.getPacketSender().sendInterface(12140);
		p.getPacketSender().sendString(12144, "You have completed: The King's Task");
		p.getPacketSender().sendString(12150, "Fishbowl Helmet");
		p.getPacketSender().sendString(12151, "Diving Apparatus");
		p.getPacketSender().sendString(12152, "You can now always teleport by");
		p.getPacketSender().sendString(12153, "using the kings teleport option");
		p.getPacketSender().sendString(12154, "");
		p.getPacketSender().sendString(12155, "");
		p.getPacketSender().sendString(12147, "2");
		p.getInventory().add(7534, 1);
		p.getInventory().add(7535, 1);
		p.addQuestPoints(2);
		PlayerPanel.refreshPanel(p);
	}

	public static String getQuestTabPrefix(Player player) {
		if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() != 0
				&& player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() < 10) {
			return "@yel@";
		} else if (player.getMinigameAttributes().getClawQuestAttributes().getQuestParts() == 10) {
			return "@gre@";
		}
		return "@red@";
	}

	private static final String questTitle = "The King's Task";
	private static final String[] questIntro = { "King Healthorg needs your help.",
			"He has a task that needs done that only the strongest", "warriors can complete.", };
	private static final String[] questGuide = { "Help the King and he will reward you greatly.",
			"he can be found in the varrock castle." };
}
