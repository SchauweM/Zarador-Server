package com.zarador.world.content.pos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a group of offers in the pos.
 *
 * @author Blake
 */
public class PosOffers {

	private String owner_name;
	private String caption;
	private int shopItems;
	private long coins_to_collect;
	private List<PosOffer> offers = new ArrayList<>();

	public PosOffers(String owner, String caption, int shopItems, long coins_to_collect, PosOffer[] offers) {
		this.owner_name = owner;
		this.caption = caption;
		this.shopItems = shopItems;
		this.coins_to_collect = coins_to_collect;
		for (int i = 0; i < offers.length; i++)
			this.offers.add(i, offers[i]);
	}

	public String getOwner() {
		return owner_name;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getAmountInShop() {
		return shopItems;
	}

	public long getCoinsToCollect() {
		return coins_to_collect;
	}

	public void resetCoinsCollect() {
		this.coins_to_collect = 0;
	}

	public void addCoinsToCollect(long i) {
		this.coins_to_collect += i;
	}

	public List<PosOffer> getOffers() {
		return offers;
	}

	public PosOffer forId(int id) {
		for (PosOffer e : offers) {
			if (e == null) {
				continue;
			}
			if (e.getItemId() == id)
				return e;
		}
		return null;
	}

	public boolean addOffer(PosOffer o) {
		return offers.add(o);
	}

	public boolean removeOffer(PosOffer o) {
		return offers.remove(o);
	}

	public void save(DataOutputStream out) throws IOException {
		out.writeUTF(getOwner());
		out.writeUTF(getCaption());
		out.writeInt(getOffers().size());
		out.writeLong(getCoinsToCollect());
		for (int i2 = 0; i2 < getOffers().size(); i2++) {
			if (getOffers().get(i2) == null) {
				out.writeInt(-1);
				out.writeInt(-1);
				out.writeInt(-1);
				out.writeLong(-1);
				// System.out.println("Writing negative variable");
			} else {
				out.writeInt(getOffers().get(i2).getItemId());
				out.writeInt(getOffers().get(i2).getAmount());
				out.writeInt(getOffers().get(i2).getSoldAmount());
				out.writeLong(getOffers().get(i2).getPrice());
				// System.out.println("Writing positive variable
				// "+getOffers().get(i2).getItemId()+"");
			}
		}
	}

	public void saveRAF(RandomAccessFile out) throws IOException {
		out.writeUTF(getOwner());
		out.writeUTF(getCaption());
		out.writeInt(getOffers().size());
		out.writeLong(getCoinsToCollect());
		for (int i2 = 0; i2 < getOffers().size(); i2++) {
			if (getOffers().get(i2) == null) {
				out.writeInt(-1);
				out.writeInt(-1);
				out.writeInt(-1);
				out.writeLong(-1);
				// System.out.println("Writing negative variable");
			} else {
				out.writeInt(getOffers().get(i2).getItemId());
				out.writeInt(getOffers().get(i2).getAmount());
				out.writeInt(getOffers().get(i2).getSoldAmount());
				out.writeLong(getOffers().get(i2).getPrice());
				// System.out.println("Writing positive variable
				// "+getOffers().get(i2).getItemId()+"");
			}
		}
	}

}