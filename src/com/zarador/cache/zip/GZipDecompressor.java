package com.zarador.cache.zip;

import java.nio.ByteBuffer;
import java.util.zip.Inflater;

public final class GZipDecompressor {
	public static Inflater inflater = new Inflater(true);

	public static void decompress(ByteBuffer buffer, byte[] newData) {
		byte[] buffer2 = buffer.array();
		if (buffer2[buffer.position()] != 31 || buffer2[buffer.position() + 1] != -117) {
			throw new RuntimeException("Invalid GZIP header!");
		}
		try {
			inflater.setInput(buffer2, buffer.position() + 10, buffer2.length - (buffer.position() + 10 + 8));
			inflater.inflate(newData);
		} catch (Exception exception) {
			inflater.reset();
			throw new RuntimeException("Invalid GZIP compressed data!");
		}
		inflater.reset();
	}
}
