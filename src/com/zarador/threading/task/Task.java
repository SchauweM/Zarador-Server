package com.zarador.threading.task;

import com.zarador.threading.GameEngine;

public abstract class Task {
	public abstract void execute(GameEngine context);

	public void dispose() {
	}
}
