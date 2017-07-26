package com.zarador.world.content.luckycrates;

public enum KeyData {

	LUCKY_CRATE_KEY("Lucky crate key", 100),
	MEGA_CRATE_KEY("Mega crate key", 200),
	LEGENDARY_CRATE_KEY("Legendary crate key", 300);
	
	private String keyName;
	private int itemID;
	
	private  KeyData(String name, int id)  {
		this.keyName = name;
		this.itemID = id;
	}
	
	public String getKeyName() {
		return keyName;
	}
	
	public int getItemID() {
		return itemID;
	}
	
}
