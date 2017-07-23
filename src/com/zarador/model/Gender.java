package com.zarador.model;

/**
 * Represents a player's sex aka gender.
 * 
 * @author relex lawl
 */

public enum Gender {

	MALE, FEMALE;

	public static Gender forId(int id) {
		for (Gender book : Gender.values()) {
			if (book.ordinal() == id) {
				return book;
			}
		}
		return MALE;
	}

}
