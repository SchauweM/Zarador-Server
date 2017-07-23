package com.zarador.model;

import com.zarador.world.entity.Entity;

public class GroundItem extends Entity {

	public GroundItem(Item item, Position pos, String owner, boolean isGlobal, int showDelay, boolean goGlobal,
			int globalTimer) {
		super(pos);
		this.setItem(item);
		this.owner = owner;
		this.fromIP = "";
		this.isGlobal = isGlobal;
		this.showDelay = showDelay;
		this.goGlobal = goGlobal;
		this.globalTimer = globalTimer;
	}

	public GroundItem(Item item, Position pos, String owner, String fromIP, boolean isGlobal, int showDelay, boolean goGlobal, int globalTimer, int address) {
		super(pos);
		this.setItem(item);
		this.owner = owner;
		this.fromIP = fromIP;
		this.isGlobal = isGlobal;
		this.showDelay = showDelay;
		this.goGlobal = goGlobal;
		this.globalTimer = globalTimer;
		this.address = address;
	}

	public GroundItem(Item item, Position pos, String owner, String fromIP, boolean isGlobal, int showDelay, boolean goGlobal, int globalTimer, int address, boolean isIronManPked, String oldOwner) {
		super(pos);
		this.setItem(item);
		this.owner = owner;
		this.fromIP = fromIP;
		this.isGlobal = isGlobal;
		this.showDelay = showDelay;
		this.goGlobal = goGlobal;
		this.globalTimer = globalTimer;
		this.address = address;
		this.isIronmanPked = isIronManPked;
		this.oldOwner = oldOwner;
	}

	public GroundItem(Item item, Position pos, String owner, String fromIP, boolean isGlobal, int showDelay,
			boolean goGlobal, int globalTimer, boolean ironman) {
		super(pos);
		this.setItem(item);
		this.owner = owner;
		this.fromIP = fromIP;
		this.isGlobal = isGlobal;
		this.showDelay = showDelay;
		this.goGlobal = goGlobal;
		this.globalTimer = globalTimer;
		this.ironman_pk_loot = ironman;
	}

	private Item item;
	private String owner, fromIP;
	private boolean isGlobal;
	private int showDelay;
	private boolean goGlobal;
	private boolean ironman_pk_loot = false;
	private int globalTimer;
	private boolean hasBeenPickedUp;
	private boolean refreshNeeded;
	private boolean shouldProcess = true;
	private int address;
	private boolean isIronmanPked = false;
	private String oldOwner;

	public Item getItem() {
		return item;
	}

	public int getAddress() {
		return address;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setFromIP(String IP) {
		this.fromIP = IP;
	}

	public boolean getIronman() {
		return ironman_pk_loot;
	}

	public String getFromIP() {
		return this.fromIP;
	}

	public void setGlobalStatus(boolean l) {
		this.isGlobal = l;
	}

	public boolean isGlobal() {
		return this.isGlobal;
	}

	public void setShowDelay(int l) {
		this.showDelay = l;
	}

	public int getShowDelay() {
		return this.showDelay;
	}

	public void setGoGlobal(boolean l) {
		this.goGlobal = l;
	}

	public boolean shouldGoGlobal() {
		return this.goGlobal;
	}

	public void setGlobalTimer(int l) {
		this.globalTimer = l;
	}

	public int getGlobalTimer() {
		return this.globalTimer;
	}

	public void setPickedUp(boolean s) {
		this.hasBeenPickedUp = s;
	}

	public boolean hasBeenPickedUp() {
		return this.hasBeenPickedUp;
	}

	public void setRefreshNeeded(boolean s) {
		this.refreshNeeded = s;
	}

	public boolean isRefreshNeeded() {
		return this.refreshNeeded;
	}

	public boolean shouldProcess() {
		return shouldProcess;
	}

	public void setShouldProcess(boolean shouldProcess) {
		this.shouldProcess = shouldProcess;
	}

	public boolean isIronmanPked() {
		return isIronmanPked;
	}

	public void setIronmanPked(boolean ironmanPked) {
		isIronmanPked = ironmanPked;
	}

	public String getOldOwner() {
		return oldOwner;
	}

	public void setOldOwner(String oldOwner) {
		this.oldOwner = oldOwner;
	}
}
