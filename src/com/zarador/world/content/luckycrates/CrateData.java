package com.zarador.world.content.luckycrates;

public enum CrateData {

	LUCKY_CRATE("Lucky crate", 100, KeyData.LUCKY_CRATE_KEY, LuckyCrate.LUCKY_CRATE_REWARDS),
	MEGA_CRATE("Mega crate", 200, KeyData.MEGA_CRATE_KEY, LuckyCrate.MEGA_CRATE_REWARDS),
	LEGENDARY_CRATE("Legendary crate", 300, KeyData.LEGENDARY_CRATE_KEY, LuckyCrate.LEGENDARY_CRATE_REWARDS);
	
	private String crateName;
	private int itemID;
	private KeyData key;
	private int[][] rewards;
	
	private  CrateData(String name, int id, KeyData key, int[][] rewards)  {
		this.crateName = name;
		this.itemID = id;
		this.key = key;
		this.rewards = rewards;
	}
	
	public String getCrateName() {
		return crateName;
	}
	
	public int getItemID() {
		return itemID;
	}
	
	public KeyData getKey() {
		return key;
	}
	
	public int[][] getRewards() {
		return rewards;
	}
	
	public static CrateData getById(int id) {
	    for(CrateData data : values()) {
	        if(data.getItemID() == id) return data;
	    }
	    return null;
	 }
	
}
