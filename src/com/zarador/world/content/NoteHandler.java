package com.zarador.world.content;

import java.util.List;

import com.zarador.world.entity.impl.player.Player;

public class NoteHandler {

	private static final int DEFAULT_COLOUR = 1;

	
	public static void login(Player player) {
		//Send blank reset
		player.getPacketSender().sendNoteReset();
		//Send notes
		List<String> notes = player.getNotes();
		List<Integer> colours = player.getNoteColours();
		player.getPacketSender().sendNotes(notes, colours);
	}
	
	public static void addNote(Player player, int index, String text) {
		if(player.getNotes().size()-1 < index) {
			player.addNote(text);
			player.addNoteColour(DEFAULT_COLOUR);
		} else {
			player.setNote(index, text);
			player.setNoteColour(index, DEFAULT_COLOUR);
		}
	}
	
	public static void deleteNote(Player player, int index) {
		player.deleteNote(index);
		player.deleteNoteColour(index);
	}

	public static void deleteAllNotes(Player player) {
		for(int i = player.getNotes().size()-1; i >= 0; i--) {
			player.deleteNote(i);
			player.deleteNoteColour(i);
		}
	}

	public static void setNoteColour(Player player, int index, int colour) {
		player.setNoteColour(index, colour);
	}
}
