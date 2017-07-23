package com.zarador.cache;

public final class CacheFormatException extends RuntimeException {
	public CacheFormatException(String message) {
		super(message);
	}

	public CacheFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
