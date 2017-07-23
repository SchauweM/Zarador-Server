package com.zarador.cache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

public final class Container {
	private static final byte[] buffer = new byte[9999999];

	public static byte[] unpack(byte[] data) throws IOException {
		int offset = 0;
		GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(data));
		while (true) {
			if (offset == buffer.length) {
				System.out.println("[OnDemand] buffer overflow ");
				break;
			}
			int numRead = in.read(buffer, offset, buffer.length - offset);
			if (numRead == -1) {
				break;
			}
			offset += numRead;
		}
		return Arrays.copyOf(buffer, offset);
	}
}
