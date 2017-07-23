package com.zarador.world.content.pos;

/**
 * Represents a single pos offer.
 * 
 * @author Blake
 */
public class PosOffer implements Comparable<PosOffer> {

	private int itemId;
	private int amount;
	private int soldAmount;
	private long price;

	public PosOffer(int itemId, int amount, int soldAmount, long price) {
		this.itemId = itemId;
		this.amount = amount;
		this.soldAmount = soldAmount;
		this.price = price;
	}

	/**
	 * Gets the item id.
	 * 
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Gets the amount
	 * 
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Increases the item amount.
	 * 
	 * @param item_amount
	 */
	public void increaseAmount(int item_amount) {
		this.amount += item_amount;
	}

	/**
	 * Decreases the item amount.
	 * 
	 * @param item_amount
	 */
	public void decreaseAmount(int item_amount) {
		this.amount -= item_amount;
	}

	/**
	 * Gets the sold amount
	 * 
	 * @return the sold amount
	 */
	public int getSoldAmount() {
		return soldAmount;
	}

	public void setSoldAmount(int amount) {
		this.soldAmount = amount;
	}

	/**
	 * Gets the price
	 * 
	 * @return the price
	 */
	public long getPrice() {
		return price;
	}

	@Override
	public int compareTo(PosOffer offer) {
		return getPrice() > offer.getPrice() ? 1 : -1;
	}

}
