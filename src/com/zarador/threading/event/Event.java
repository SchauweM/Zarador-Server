package com.zarador.threading.event;


import com.zarador.GameServer;

/**
 * @author Omicron
 */
public abstract class Event implements Runnable {
	protected boolean running = true;
	protected long delay;

	protected Event(long delay) {
		this.delay = delay;
	}

	public boolean isRunning() {
		return running;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		if (delay < 0) {
			throw new IllegalArgumentException("Delay must be positive.");
		}
		this.delay = delay;
	}

	@Override
	public final void run() {
		if (!running) {
			return;
		}
		long start = System.currentTimeMillis();
		this.execute();
		if (running) {
			long time = delay - (System.currentTimeMillis() - start);
			GameServer.submit(this, time < 0 ? 0 : time);
		}
	}

	public abstract void execute();

	public void stop() {
		running = false;
	}
}
