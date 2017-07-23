package com.zarador.net.packet.impl;

import com.zarador.GameSettings;
import com.zarador.net.packet.Packet;
import com.zarador.net.packet.PacketListener;
import com.zarador.world.content.BankPin;
import com.zarador.world.content.MoneyPouch;
import com.zarador.world.entity.impl.player.Player;

public class WithdrawMoneyFromPouchPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()
				&& player.getBankPinAttributes().onDifferent(player)) {
			BankPin.init(player, false);
			return;
		}
		int amount = packet.readInt();
		MoneyPouch.withdrawMoney(player, amount);
		if (GameSettings.DEBUG_MODE) {
			// PlayerLogs.log(player,
			// "" + player.getUsername() + " in
			// WithdrawMoneyFromPouchPacketListener " + amount + "");
		}
	}

}
