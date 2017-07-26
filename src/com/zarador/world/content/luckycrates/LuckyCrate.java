package com.zarador.world.content.luckycrates;

import com.zarador.world.entity.impl.player.Player;

public class LuckyCrate {

	public static final int[] LUCKY_CRATE_REWARDS = {
				
	};
	
	public static final int[] MEGA_CRATE_REWARDS = {
			
	};
	
	public static final int[] LEGENDARY_CRATE_REWARDS = {
			
	};
	
	public static void handleChestOpening(int chestID, int keyID, Player player) {
		
		CrateData data = CrateData.getById(chestID);
		
		if(data.getKey().getItemID() == keyID) {
			//Handle reward
		} else {
			player.getPacketSender().sendMessage("@blu@You can only use "+data.getKey().getKeyName()+" on "+data.getCrateName());
		}
	}
	
}
