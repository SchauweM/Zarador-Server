package com.zarador.engine.task.impl;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.world.entity.impl.player.Player;

public class VenomImmunityTask extends Task {

	public VenomImmunityTask(final Player p) {
		super(1, p, false);
		this.p = p;
	}

	final Player p;

	@Override
	public void execute() {
		if (p == null || !p.isRegistered()) {
			stop();
			return;
		}
		int currentImmunity = p.getVenomImmunity();
		if (currentImmunity > 0) {
			p.setVenomImmunity(currentImmunity - 1);
		} else {
			p.setVenomImmunity(0);
			p.getPacketSender().sendMessage("You are no longer immune to venom.");
			stop();
		}
	}

	public static void makeImmune(final Player p, int seconds) {
		int currentImmunity = p.getVenomImmunity();
		boolean startEvent = currentImmunity == 0;
		p.setVenomImmunity(currentImmunity + seconds);
		p.setVenomDamage(0);
		p.getPacketSender().sendConstitutionOrbVenom(false);
		if (!startEvent)
			return;
		TaskManager.submit(new VenomImmunityTask(p));
	}
}
