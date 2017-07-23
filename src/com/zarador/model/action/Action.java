package com.zarador.model.action;

import com.zarador.executable.Executable;

public abstract class Action implements Executable {
	private boolean running = true;
	protected int ticks;

	protected Action(int ticks) {
		this.ticks = ticks;
	}

	public final boolean isRunning() {
		return running;
	}

	public ActionPolicy getActionPolicy() {
		return ActionPolicy.NONE;
	}

	public void initialize() {
	}

	public final void run() {
		if (!running) {
			return;
		}
		if (--ticks != 0) {
			return;
		}
		ticks = this.execute();
		if (ticks == Executable.STOP) {
			this.stop();
		}
	}

	public void stop() {
		running = false;
		this.dispose();
	}

	public void handleFailed() {
	}

	public void dispose() {
	}

	public static enum ActionPolicy {
		NONE,
		QUEUE,
		FIXED,
		CLEAR
	}
}
