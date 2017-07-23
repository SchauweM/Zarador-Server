package com.zarador.model.player.dialog;

import com.zarador.model.Animation;
import com.zarador.model.definitions.NpcDefinition;
import com.zarador.world.entity.impl.player.Player;

public final class DialogHandler {
	private final Player player;

	public DialogHandler(Player player) {
		this.player = player;
	}
    
	public static final int HAPPY = 9760, CALM = 9805, JOYOUS_TALK = 9840, GLANCE_DOWN = 9832, HAPPY_AND_SURPRISED_TALK = 9742, CALM_CONTINUED = 590, CONTENT = 9803, EVIL = 592, EVIL_CONTINUED = 593, DELIGHTED_EVIL = 594, ANNOYED = 595, DISTRESSED = 596, DISTRESSED_CONTINUED = 597, NEAR_TEARS = 598, SAD = 599, DISORIENTED_LEFT = 600, DISORIENTED_RIGHT = 601, UNINTERESTED = 602, SLEEPY = 603, PLAIN_EVIL = 604, LAUGHING = 605, LONGER_LAUGHING = 606, LONGER_LAUGHING_2 = 607, LAUGHING_2 = 608, EVIL_LAUGH_SHORT = 609, SLIGHTLY_SAD = 9773, VERY_SAD = 9768, OTHER = 612, NEAR_TEARS_2 = 9765, ANGRY_1 = 9781, ANGRY_2 = 615, ANGRY_3 = 9789, ANGRY_4 = 9785, SCARED = 9777;

	private static boolean canSendDialog(Player player) {
		if(player.newPlayer()) {
			return true;
		}
		if (player.isTeleporting()) {
			return false;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
			return false;
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
			return false;
		}
		return true;
	}

	public void sendDialog(Dialog dialog) {
		if (dialog == null) {
			return;
		}
		DialogMessage message = dialog.getMessage();
		if (message == null) {
			return;
		}
		if (!canSendDialog(this.player)) {
			return;
		}
		if (message.getLines() != null || message.getType() == DialogType.OPTION) {
			switch (message.getType()) {
				case PLAYER:
					this.sendPlayerChat(message.getDialogueAnimation(), message.getLines());
					break;
				case NPC:
					this.sendNpcChat(message.getDialogueAnimation(), message.getLines());
					break;
				case STATEMENT:
					this.sendStatement(message.getLines());
					break;
				case OPTION:
					this.player.getOptionContainer().display(message.getOption());
					break;
			}
		}
		if (message.getType() == DialogType.EMPTY) {
			player.currentDialog = null;
			return;
		}
		if (dialog.finished()) {
			player.currentDialog = null;
		} else {
			player.currentDialog = dialog;
		}
	}

	private void sendOption(String s, String s1) {
		if (player.isTeleporting()) {
			return;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
		}
		player.getPacketSender().sendString("Select an Option", 2470);
		player.getPacketSender().sendString(s, 2471);
		player.getPacketSender().sendString(s1, 2472);
		player.getPacketSender().sendString("Click here to continue", 2473);
		player.getPacketSender().sendChatboxInterface(13758);
	}

	public void sendOption2(String s, String s1) {
		if (player.isTeleporting()) {
			return;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
		}
        player.getPacketSender().sendString("Select an Option", 2460);
        player.getPacketSender().sendString(s, 2461);
        player.getPacketSender().sendString(s1, 2462);
        player.getPacketSender().sendChatboxInterface(2459);
    }

	public void sendOption3(String s, String s1, String s2) {
		if (player.isTeleporting()) {
			return;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
		}
		player.getPacketSender().sendString(2460, "Select an Option");
		player.getPacketSender().sendString(s, 2471);
		player.getPacketSender().sendString(s1, 2472);
		player.getPacketSender().sendString(s2, 2473);
		player.getPacketSender().sendChatboxInterface(2469);
	}

	public void sendOption4(String s, String s1, String s2, String s3) {
		if (player.isTeleporting()) {
			return;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
		}
		player.getPacketSender().sendString("Select an Option", 2481);
		player.getPacketSender().sendString(s, 2482);
		player.getPacketSender().sendString(s1, 2483);
		player.getPacketSender().sendString(s2, 2484);
		player.getPacketSender().sendString(s3, 2485);
		player.getPacketSender().sendChatboxInterface(2480);
	}

	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		if (player.isTeleporting()) {
			return;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
		}
		player.getPacketSender().sendString("Select an Option", 2493);
		player.getPacketSender().sendString(s, 2494);
		player.getPacketSender().sendString(s1, 2495);
		player.getPacketSender().sendString(s2, 2496);
		player.getPacketSender().sendString(s3, 2497);
		player.getPacketSender().sendString(s4, 2498);
		player.getPacketSender().sendChatboxInterface(2492);
	}

	/**
	 * NPC dialogue.
	 */
	public void sendNpcChat(String[] lines, int emotion) {
		switch (lines.length) {
			case 1:
				this.sendNpcChat(lines[0], emotion);
				break;
			case 2:
				this.sendNpcChat(lines[0], lines[1], emotion);
				break;
			case 3:
				this.sendNpcChat(lines[0], lines[1], lines[2], emotion);
				break;
			case 4:
				this.sendNpcChat(lines[0], lines[1], lines[2], lines[3], emotion);
				break;
		}
	}

	/**
	 * NPC dialogue.
	 */
	public void sendNpcChat(int dialogueAnimation, String... lines) {
		int emotion = DialogHandler.HAPPY;
		this.sendNpcChat(lines, emotion);
	}

	/**
	 * NPC dialogue.
	 */

	public void sendNpcChat(String line1, int emotion) {
		int id = player.getNpcClickId();
		player.getPacketSender().sendNpcHeadOnInterface(id, 4883);
		player.getPacketSender().sendInterfaceAnimation(4883, new Animation(emotion));
		player.getPacketSender().sendString(4884, NpcDefinition.forId(id) != null ? NpcDefinition.forId(id).getName().replaceAll("_", " ") : "");
		player.getPacketSender().sendString(line1, 4885);
		player.getPacketSender().sendChatboxInterface(4882);
	}

	public void sendNpcChat(String line1, String line2, int emotion) {
		int id = player.getNpcClickId();
		player.getPacketSender().sendNpcHeadOnInterface(id, 4888);
		player.getPacketSender().sendInterfaceAnimation(4888, new Animation(emotion));
		player.getPacketSender().sendString(4889, NpcDefinition.forId(id) != null ? NpcDefinition.forId(id).getName().replaceAll("_", " ") : "");
		player.getPacketSender().sendString(line1, 4890);
		player.getPacketSender().sendString(line2, 4891);
		player.getPacketSender().sendChatboxInterface(4887);
	}

	public void sendNpcChat(String line1, String line2, String line3, int emotion) {
		int id = player.getNpcClickId();
		player.getPacketSender().sendNpcHeadOnInterface(id, 4894);
		player.getPacketSender().sendInterfaceAnimation(4894, new Animation(emotion));
		player.getPacketSender().sendString(4895, NpcDefinition.forId(id) != null ? NpcDefinition.forId(id).getName().replaceAll("_", " ") : "");
		player.getPacketSender().sendString(line1, 4896);
		player.getPacketSender().sendString(line2, 4897);
		player.getPacketSender().sendString(line3, 4898);
		player.getPacketSender().sendChatboxInterface(4893);
	}

	public void sendNpcChat(String line1, String line2, String line3, String line4, int emotion) {
		int id = player.getNpcClickId();
		player.getPacketSender().sendNpcHeadOnInterface(id, 4901);
		player.getPacketSender().sendInterfaceAnimation(4901, new Animation(emotion));
		player.getPacketSender().sendString(4902, NpcDefinition.forId(id) != null ? NpcDefinition.forId(id).getName().replaceAll("_", " ") : "");
		player.getPacketSender().sendString(line1, 4903);
		player.getPacketSender().sendString(line2, 4904);
		player.getPacketSender().sendString(line3, 4905);
		player.getPacketSender().sendString(line4, 4906);
		player.getPacketSender().sendChatboxInterface(4900);
	}

	public void sendPlayerChat(int dialogueAnimation, String... s) {
		switch (s.length) {
			case 1:
				this.sendPlayerChat1(dialogueAnimation, s[0]);
				break;
			case 2:
				this.sendPlayerChat2(dialogueAnimation, s[0], s[1]);
				break;
			case 3:
				this.sendPlayerChat3(dialogueAnimation, s[0], s[1], s[2]);
				break;
			case 4:
				this.sendPlayerChat4(dialogueAnimation, s[0], s[1], s[2], s[3]);
				break;
		}
	}

	private void sendPlayerChat1(int animationId, String s) {
		if (player.isTeleporting()) {
			return;
		}
		if(animationId < 1) {
			animationId = CALM;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
		}
		player.getPacketSender().sendPlayerHeadOnInterface(969);
		player.getPacketSender().sendInterfaceAnimation(969, new Animation(animationId));
		player.getPacketSender().sendString(970, player.getUsername());
		player.getPacketSender().sendString(971, s);
		player.getPacketSender().sendChatboxInterface(968);
	}

	private void sendPlayerChat2(int animationId, String s, String s1) {
		if (player.isTeleporting()) {
			return;
		}
		if(animationId < 1) {
			animationId = CALM;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
		}
		player.getPacketSender().sendInterfaceAnimation(974, new Animation(animationId));
		player.getPacketSender().sendString(player.getUsername(), 975);
		player.getPacketSender().sendString(s, 976);
		player.getPacketSender().sendString(s1, 977);
		player.getPacketSender().sendPlayerHeadOnInterface(974);
		player.getPacketSender().sendChatboxInterface(973);
	}

	private void sendPlayerChat3(int animationId, String s, String s1, String s2) {
		if (player.isTeleporting()) {
			return;
		}
		if(animationId < 1) {
			animationId = CALM;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
		}
		player.getPacketSender().sendInterfaceAnimation(980, new Animation(animationId));
		player.getPacketSender().sendString(player.getUsername(), 981);
		player.getPacketSender().sendString(s, 982);
		player.getPacketSender().sendString(s1, 983);
		player.getPacketSender().sendString(s2, 984);
		player.getPacketSender().sendPlayerHeadOnInterface(980);
		player.getPacketSender().sendChatboxInterface(979);
	}

	private void sendPlayerChat4(int animationId, String s, String s1, String s2, String s3) {
		if (player.isTeleporting()) {
			return;
		}
		if(animationId < 1) {
			animationId = CALM;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
		}
		player.getPacketSender().sendInterfaceAnimation(987, new Animation(animationId));
		player.getPacketSender().sendString(player.getUsername(), 988);
		player.getPacketSender().sendString(s, 989);
		player.getPacketSender().sendString(s1, 990);
		player.getPacketSender().sendString(s2, 991);
		player.getPacketSender().sendString(s3, 992);
		player.getPacketSender().sendPlayerHeadOnInterface(987);
		player.getPacketSender().sendChatboxInterface(986);
	}

	/**
	 * Statements.
	 */
	public void sendStatement(String[] lines) {
		switch (lines.length) {
			case 1:
				this.sendStatement(lines[0]);
				break;
			case 2:
				this.sendStatement(lines[0], lines[1]);
				break;
			case 3:
				this.sendStatement(lines[0], lines[1], lines[2]);
				break;
			case 4:
				this.sendStatement(lines[0], lines[1], lines[2], lines[3]);
				break;
			case 5:
				this.sendStatement(lines[0], lines[1], lines[2], lines[3], lines[4]);
				break;
		}
	}

	public void sendStatement(String line1, String line2) {
		player.getPacketSender().sendString(line1, 360);
		player.getPacketSender().sendString(line2, 361);
		player.getPacketSender().sendChatboxInterface(359);
	}

	public void sendStatement(String line1, String line2, String line3) {
		player.getPacketSender().sendString(line1, 364);
		player.getPacketSender().sendString(line2, 365);
		player.getPacketSender().sendString(line3, 366);
		player.getPacketSender().sendChatboxInterface(363);
	}

	public void sendStatement(String line1, String line2, String line3, String line4) {
		player.getPacketSender().sendString(line1, 369);
		player.getPacketSender().sendString(line2, 370);
		player.getPacketSender().sendString(line3, 371);
		player.getPacketSender().sendString(line4, 372);
		player.getPacketSender().sendChatboxInterface(368);
	}

	public void sendStatement(String line1, String line2, String line3, String line4, String line5) {
		player.getPacketSender().sendString(line1, 375);
		player.getPacketSender().sendString(line2, 376);
		player.getPacketSender().sendString(line3, 377);
		player.getPacketSender().sendString(line4, 378);
		player.getPacketSender().sendString(line5, 379);
		player.getPacketSender().sendChatboxInterface(374);;
	}

	public void sendStatement(String msg) {
		if (player.isTeleporting()) {
			return;
		}
		if (player.getTrading().inTrade()) {
			player.getTrading().declineTrade(true);
		}
		if (player.getDueling().inDuelScreen && player.getDueling().inDuelWith == -1) {
			player.getDueling().declineDuel(true);
		}
		player.getPacketSender().sendString(msg, 357);
		player.getPacketSender().sendChatboxInterface(356);
	}
}
