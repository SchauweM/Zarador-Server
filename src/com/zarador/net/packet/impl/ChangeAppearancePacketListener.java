package com.zarador.net.packet.impl;

import com.zarador.GameSettings;
import com.zarador.model.Appearance;
import com.zarador.model.Flag;
import com.zarador.model.Gender;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.util.Misc;
import com.zarador.world.entity.impl.player.Player;

public class ChangeAppearancePacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		String appearanceAttributes = Misc.readString(packet.getBuffer());
		if (appearanceAttributes == null || appearanceAttributes.length() <= 1)
			return;
		try {
			if (GameSettings.DEBUG_MODE) {
				// PlayerLogs.log(player, "" + player.getUsername()
				// + " has changed their appearance in
				// ChangeAppearancePacketListener");
			}
			String[] parts = appearanceAttributes.split(" ");
			int gender = Integer.parseInt(parts[0]);
			if (gender != 0 && gender != 1) {
				return;
			}
			player.getAppearance().setGender(Gender.forId(gender));
			final int[] apperances = new int[MALE_VALUES.length];
			final int[] colors = new int[ALLOWED_COLORS.length];
			int currentPartIndex = 1;
			for (int i = 0; i < apperances.length; i++, currentPartIndex++) {
				int value = Integer.parseInt(parts[currentPartIndex]);
				if (value < (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0]) || value > (gender == 0 ? MALE_VALUES[i][1] : FEMALE_VALUES[i][1]))
					value = (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0]);
				apperances[i] = value;
			}
			for (int i = 0; i < colors.length; i++, currentPartIndex++) {
				int value = Integer.parseInt(parts[currentPartIndex]);
				if (value < ALLOWED_COLORS[i][0] || value > ALLOWED_COLORS[i][1])
					value = ALLOWED_COLORS[i][0];
				colors[i] = value;
			}
			if (player.getAppearance().canChangeAppearance() && player.getInterfaceId() > 0) {
				// Appearance looks
				player.getAppearance().set(Appearance.GENDER, gender);
				player.getAppearance().set(Appearance.HEAD, apperances[0]);
				player.getAppearance().set(Appearance.CHEST, apperances[2]);
				player.getAppearance().set(Appearance.ARMS, apperances[3]);
				player.getAppearance().set(Appearance.HANDS, apperances[4]);
				player.getAppearance().set(Appearance.LEGS, apperances[5]);
				player.getAppearance().set(Appearance.FEET, apperances[6]);
				player.getAppearance().set(Appearance.BEARD, apperances[1]);

				// Colors
				player.getAppearance().set(Appearance.HAIR_COLOUR, colors[0]);
				player.getAppearance().set(Appearance.TORSO_COLOUR, colors[1]);
				player.getAppearance().set(Appearance.LEG_COLOUR, colors[2]);
				player.getAppearance().set(Appearance.FEET_COLOUR, colors[3]);
				player.getAppearance().set(Appearance.SKIN_COLOUR, colors[4]);

				player.getUpdateFlag().flag(Flag.APPEARANCE);
			}
		} catch (Exception e) {
			player.getAppearance().set();
			// e.printStackTrace();
		}
		player.getPacketSender().sendInterfaceRemoval();
		player.getAppearance().setCanChangeAppearance(false);
	}

	private static final int[][] ALLOWED_COLORS = { { 0, 11 }, // hair color
			{ 0, 15 }, // torso color
			{ 0, 15 }, // legs color
			{ 0, 5 }, // feet color
			{ 0, 7 } // skin color
	};

	private static final int[][] FEMALE_VALUES = {
		    { 45, 362 }, // head
			{ -1, -1 }, // jaw
			{ 56, 587 }, // torso
			{ 61, 426 }, // arms
			{ 67, 538 }, // hands
			{ 70, 506 }, // legs
			{ 79, 555 }, // feet
	};

	private static final int[][] MALE_VALUES = {
			{ 0, 352 }, // head
			{ 10, 308 }, // jaw
			{ 18, 474 }, // torso
			{ 26, 619 }, // arms
			{ 33, 394 }, // hands
			{ 36, 651 }, // legs
			{ 42, 442 }, // feet
	};
}
