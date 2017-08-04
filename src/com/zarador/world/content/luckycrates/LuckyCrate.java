package com.zarador.world.content.luckycrates;

import com.zarador.model.container.ItemContainer;
import com.zarador.model.definitions.ItemDefinition;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class LuckyCrate {

	public static final int[][] LUCKY_CRATE_REWARDS = {
		{1040, 1}
	};
	
	public static final int[][] MEGA_CRATE_REWARDS = {
		{1042, 1}	
	};
	
	public static final int[][] LEGENDARY_CRATE_REWARDS = {
		{1050, 1}	
	};
	
	public static void handleChestOpening(int chestID, int keyID, Player player) {
		
		CrateData data = CrateData.getById(chestID);
		
		if(data.getKey().getItemID() == keyID) {
			if(player.getInventory().getFreeSlots() >= 1) {
				int rewardID = Misc.getRandom(data.getRewards().length-1);
				player.getInventory().add(data.getRewards()[rewardID][0], data.getRewards()[rewardID][1]);
				player.getPacketSender().sendMessage("@or2@You have recieved "+data.getRewards()[rewardID][1]+"x "
				+ ItemDefinition.forId(data.getRewards()[rewardID][0]).getName() + " from your "+data.getCrateName());
			} else {
				player.getPacketSender().sendMessage("@blu@You need atleast 1 free inventory slot to do this.");
			}
		} else {
			player.getPacketSender().sendMessage("@blu@You can only use "+data.getKey().getKeyName()+" on "+data.getCrateName());
		}
	}
	
	public static void handleDailyChest(Player player) {
		
	}
	
}
