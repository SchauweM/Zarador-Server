package com.zarador.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

import com.zarador.cache.zip.BZip2Decompressor;

public final class Archive {
	private static int getMedium(ByteBuffer buffer) {
		return ((buffer.getShort() & 0xFFFF) << 8) | (buffer.get() & 0xFF);
	}
	private final Entry[] entries;
	private final int entryCount;

	public static byte[] decompressGZIP(byte[] data) {
		try {
			GZIPInputStream gzi = new GZIPInputStream(new ByteArrayInputStream(data));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = gzi.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public Archive(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		int size = getMedium(buffer);
		int compressedSize = getMedium(buffer);
		entryCount = buffer.getShort() & 0xFFFF;
		boolean decompressed;
		if (compressedSize == 0) {
			byte[] decodedData = new byte[size];
			System.arraycopy(data, 8, decodedData, 0, data.length - 8);
			data = decompressGZIP(decodedData);
			buffer = ByteBuffer.wrap(data);
			decompressed = true;
		} else if (compressedSize != size) {
			byte[] abyte1 = new byte[size];
			BZip2Decompressor.decompress(abyte1, size, data, compressedSize, 6);
			data = abyte1;
			buffer = ByteBuffer.wrap(data);
			decompressed = true;
		} else {
			decompressed = false;
		}
		int offset = buffer.position() + entryCount * 10;
		entries = new Entry[entryCount];
		//System.out.println("archive: " + name + ", entries: " + entries.length);
		for (int file = 0; file < entryCount; file++) {
			int hash = buffer.getInt();
			int entrySize = getMedium(buffer);
			int entryCompressedSize = getMedium(buffer);
			byte[] entryData = new byte[entrySize];
			if (!decompressed) {
				BZip2Decompressor.decompress(entryData, entrySize, data, entryCompressedSize, offset);
			} else {
				System.arraycopy(data, offset, entryData, 0, entrySize);
			}
			entries[file] = new Entry(hash, entryData);
			//System.out.println("entry: " + file + ", hash: " + hash);
			offset += entryCompressedSize;
		}
	}

	public Entry getNamedFile(String fileName) {
		int hash = 0;
		fileName = fileName.toUpperCase();
		for (int charIndex = 0; charIndex < fileName.length(); charIndex++) {
			hash = (hash * 61 + fileName.charAt(charIndex)) - 32;
		}
		for (int entry = 0; entry < entryCount; entry++) {
			if (entries[entry].identifier() != hash) {
				continue;
			}
			return entries[entry];
		}
		return null;
	}

	public static final class Entry {
		private final int identifier;
		private final byte[] data;

		private Entry(int identifier, byte[] data) {
			this.identifier = identifier;
			this.data = data;
		}

		public int identifier() {
			return identifier;
		}

		public byte[] data() {
			return data;
		}

		public ByteBuffer buffer() {
			return ByteBuffer.wrap(data);
		}
	}
}
