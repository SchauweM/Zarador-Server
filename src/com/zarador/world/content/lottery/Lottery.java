package com.zarador.world.content.lottery;

/**
 * Represents a single pos offer.
 * 
 * @author Blake
 */
public class Lottery implements Comparable<Lottery> {

	private String username;

	public Lottery(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int compareTo(Lottery o) {
		return 0;
	}
}
