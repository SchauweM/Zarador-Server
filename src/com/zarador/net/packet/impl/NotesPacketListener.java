package com.zarador.net.packet.impl;


import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.util.Misc;
import com.zarador.world.content.NoteHandler;
import com.zarador.world.entity.impl.player.Player;

/**
 * Note saving and changes
 * @author Greg
 */

public class NotesPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int code = packet.getOpcode();
		switch (code) {
		case NOTE_TEXT_OPCODE:
			saveNote(player, packet);
			break;
		case NOTE_COMMAND_OPCODE:
			handleNotes(player, packet);
			break;
		}
	}
	
	private void handleNotes(Player player, Packet packet) {
		int command = packet.readShort();
		int noteId = packet.readShort();
		System.out.println(command);
		switch(command) {
		case 0://Save
			NoteHandler.addNote(player, noteId, player.getTempNote());
			player.setTempNote("");
			break;
		case 1://Delete
			NoteHandler.deleteNote(player, noteId);
			break;
		case 2:
		case 3:
		case 4:
		case 5://Colour
			NoteHandler.setNoteColour(player, noteId, command-1);
			break;
		case 6://Delete all
            NoteHandler.deleteAllNotes(player);
			break;
		}
	}
	
	private void saveNote(Player player, Packet packet) {
		String message = Misc.readString(packet.getBuffer());
		System.out.println(message);
		if(player.getTempNote() == "")
		player.setTempNote(message);
	}

	public static final int NOTE_TEXT_OPCODE = 104;
	public static final int NOTE_COMMAND_OPCODE = 105;
}
