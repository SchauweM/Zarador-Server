package com.zarador.util;

public class ByteStream {
	private final byte[] buffer;
	private int offset;

	public ByteStream(byte[] buffer) {
		this.buffer = buffer;
		this.offset = 0;
	}

	public void skip() {
		offset += 1;
	}

	public void setOffset(int position) {
		offset = position;
	}

	public void setOffset(long position) {
		offset = (int) position;
	}

	public int length() {
		return buffer.length;
	}

	public byte getByte() {
		return buffer[offset++];
	}

	public int getUnsignedByte() {
		return buffer[offset++] & 0xff;
	}

	public int getShort() {
		int val = (this.getByte() << 8) + this.getByte();
		if (val > 32767) {
			val -= 0x10000;
		}
		return val;
	}

	public int getUnsignedShort() {
		return (this.getUnsignedByte() << 8) + this.getUnsignedByte();
	}

	public int getInt() {
		return (this.getUnsignedByte() << 24) + (this.getUnsignedByte() << 16) + (this.getUnsignedByte() << 8) + this.getUnsignedByte();
	}

	public long getLong() {
		long l = (long) this.getInt() & 0xffffffffL;
		long l1 = (long) this.getInt() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public int getUSmart() {
		int i = buffer[offset] & 0xff;
		return i < 128 ? this.getUnsignedByte() : this.getUnsignedShort() - 32768;
	}

	public int getUSmart2() {
		int baseVal = 0;
		int lastVal;
		while ((lastVal = this.getUSmart()) == 32767) {
			baseVal += 32767;
		}
		return baseVal + lastVal;
	}

	public String getNString() {
		int i = offset;
		while (buffer[offset++] != 0) {
		}
		return new String(buffer, i, offset - i - 1);
	}

	public byte[] getBytes() {
		int i = offset;
		while (buffer[offset++] != 10) {
		}
		byte[] abyte0 = new byte[offset - i - 1];
		System.arraycopy(buffer, i, abyte0, i - i, offset - 1 - i);
		return abyte0;
	}

	public byte[] read(int length) {
		byte[] b = new byte[length];
		for (int i = 0; i < length; i++) {
			b[i] = buffer[offset++];
		}
		return b;
	}
}
	