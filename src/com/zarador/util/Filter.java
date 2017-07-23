package com.zarador.util;

public interface Filter<E> {
	boolean accept(E e);
}
