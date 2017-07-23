package com.zarador.util;

public abstract class FilterExecutable<E> {
	public boolean accept(E e) {
		return true;
	}

	public abstract void execute(E e);
}
