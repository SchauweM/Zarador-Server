package com.zarador.world.content.lottery;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents individual
 *
 * @author Jonny
 */
public class Lotteries {

	private List<Lottery> offers = new ArrayList<>();

	public Lotteries(Lottery[] offers) {
		if(offers == null) {
			return;
		}
		for (int i = 0; i < offers.length; i++)
			this.offers.add(i, offers[i]);
	}
	
	public List<Lottery> getOffers() {
		return offers;
	}

	public boolean addOffer(Lottery o) {
		return offers.add(o);
	}

	public boolean removeOffer(Lottery o) {
		return offers.remove(o);
	}

	public void saveRAF(RandomAccessFile out) throws IOException {
		out.writeInt(getOffers().size());
		out.writeLong(LotterySaving.lottery1Timer);
		out.writeBoolean(LotterySaving.LOTTERY_ON);
		for (int i2 = 0; i2 < getOffers().size(); i2++) {
			if (getOffers().get(i2) == null) {
				out.writeUTF("");
			} else {
				out.writeUTF(getOffers().get(i2).getUsername());
			}
		}
	}

}