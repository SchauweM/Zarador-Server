package com.zarador.threading.task;

import java.util.Arrays;

public final class TaskList {
	private Task[] tasks = new Task[10];
	private int size;

	public int size() {
		return size;
	}

	public void reset() {
		size = 0;
	}

	public Task get(int index) {
		return tasks[index];
	}

	public void add(Task task) {
		this.ensureCapacity(size + 1);
		tasks[size++] = task;
	}

	public Task[] toArray() {
		return Arrays.copyOf(tasks, size);
	}

	private void ensureCapacity(int minCapacity) {
		int oldCapacity = tasks.length;
		if (minCapacity <= oldCapacity) {
			return;
		}
		int newCapacity = Math.max((oldCapacity * 3) / 2 + 1, minCapacity);
		tasks = Arrays.copyOf(tasks, newCapacity);
	}
}
