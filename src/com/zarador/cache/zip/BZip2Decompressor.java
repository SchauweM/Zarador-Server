package com.zarador.cache.zip;

public final class BZip2Decompressor {
	public static final BZip2BlockEntry entry = new BZip2BlockEntry();

	public static int decompress(byte[] output, int decompressedSize, byte[] input, int compressedSize, int offset) {
		synchronized (entry) {
			entry.input = input;
			entry.offset = offset;
			entry.output = output;
			entry.nextOut = 0;
			entry.availableIn = compressedSize;
			entry.availableOut = decompressedSize;
			entry.bsLive = 0;
			entry.bsBuff = 0;
			entry.totalInLo32 = 0;
			entry.totalInHi32 = 0;
			entry.totalOutLo32 = 0;
			entry.totalOutHi32 = 0;
			entry.blockNo = 0;
			readBlock(entry);
			decompressedSize -= entry.availableOut;
			return decompressedSize;
		}
	}

	private static void undoRle(BZip2BlockEntry entry) {
		byte stateOutCh = entry.stateOutCh;
		int stateOutLen = entry.stateOutLen;
		int nBlockUsed = entry.nBlockUsed;
		int k0 = entry.k0;
		int[] tt = BZip2BlockEntry.tt;
		int tPos = entry.tPos;
		byte[] output = entry.output;
		int nextOut = entry.nextOut;
		int availableOut = entry.availableOut;
		int availableOutInit = availableOut;
		int nblockPP = entry.nBlock + 1;
		label0:
		do {
			if (stateOutLen > 0) {
				do {
					if (availableOut == 0) {
						break label0;
					}
					if (stateOutLen == 1) {
						break;
					}
					output[nextOut] = stateOutCh;
					stateOutLen--;
					nextOut++;
					availableOut--;
				} while (true);
				if (availableOut == 0) {
					stateOutLen = 1;
					break;
				}
				output[nextOut] = stateOutCh;
				nextOut++;
				availableOut--;
			}
			boolean flag = true;
			while (flag) {
				flag = false;
				if (nBlockUsed == nblockPP) {
					stateOutLen = 0;
					break label0;
				}
				stateOutCh = (byte) k0;
				tPos = tt[tPos];
				byte k1 = (byte) (tPos & 0xff);
				tPos >>= 8;
				nBlockUsed++;
				if (k1 != k0) {
					k0 = k1;
					if (availableOut == 0) {
						stateOutLen = 1;
					} else {
						output[nextOut] = stateOutCh;
						nextOut++;
						availableOut--;
						flag = true;
						continue;
					}
					break label0;
				}
				if (nBlockUsed != nblockPP) {
					continue;
				}
				if (availableOut == 0) {
					stateOutLen = 1;
					break label0;
				}
				output[nextOut] = stateOutCh;
				nextOut++;
				availableOut--;
				flag = true;
			}
			stateOutLen = 2;
			tPos = tt[tPos];
			byte k1 = (byte) (tPos & 0xff);
			tPos >>= 8;
			if (++nBlockUsed != nblockPP) {
				if (k1 != k0) {
					k0 = k1;
				} else {
					stateOutLen = 3;
					tPos = tt[tPos];
					byte byte2 = (byte) (tPos & 0xff);
					tPos >>= 8;
					if (++nBlockUsed != nblockPP) {
						if (byte2 != k0) {
							k0 = byte2;
						} else {
							tPos = tt[tPos];
							byte k12 = (byte) (tPos & 0xff);
							tPos >>= 8;
							nBlockUsed++;
							stateOutLen = (k12 & 0xff) + 4;
							tPos = tt[tPos];
							k0 = (byte) (tPos & 0xff);
							tPos >>= 8;
							nBlockUsed++;
						}
					}
				}
			}
		} while (true);
		int totalOutLo32 = entry.totalOutLo32;
		entry.totalOutLo32 += availableOutInit - availableOut;
		if (entry.totalOutLo32 < totalOutLo32) {
			entry.totalOutHi32++;
		}
		entry.stateOutCh = stateOutCh;
		entry.stateOutLen = stateOutLen;
		entry.nBlockUsed = nBlockUsed;
		entry.k0 = k0;
		BZip2BlockEntry.tt = tt;
		entry.tPos = tPos;
		entry.output = output;
		entry.nextOut = nextOut;
		entry.availableOut = availableOut;
	}

	private static void readBlock(BZip2BlockEntry entry) {
		int gminLen = 0;
		int[] glimit = null;
		int[] gbase = null;
		int[] gperm = null;
		entry.blockSize100k = 1;
		if (BZip2BlockEntry.tt == null) {
			BZip2BlockEntry.tt = new int[entry.blockSize100k * 0x186a0];
		}
		boolean flag19 = true;
		while (flag19) {
			if (getByte(entry) == 23) {
				return;
			}
			getByte(entry);
			getByte(entry);
			getByte(entry);
			getByte(entry);
			getByte(entry);
			entry.blockNo++;
			getByte(entry);
			getByte(entry);
			getByte(entry);
			getByte(entry);
			entry.randomised = getBit(entry) != 0;
			if (entry.randomised) {
				//System.out.println("PANIC! RANDOMISED BLOCK!");
			}
			entry.origPtr = 0;
			entry.origPtr = entry.origPtr << 8 | getByte(entry) & 0xFF;
			entry.origPtr = entry.origPtr << 8 | getByte(entry) & 0xFF;
			entry.origPtr = entry.origPtr << 8 | getByte(entry) & 0xFF;
			for (int i = 0; i < 16; i++) {
				entry.inUse16[i] = getBit(entry) == 1;
			}
			for (int i = 0; i < 256; i++) {
				entry.inUse[i] = false;
			}
			for (int i = 0; i < 16; i++) {
				if (entry.inUse16[i]) {
					for (int i3 = 0; i3 < 16; i3++) {
						if (getBit(entry) == 1) {
							entry.inUse[i * 16 + i3] = true;
						}
					}
				}
			}
			makeMaps(entry);
			int alphaSize = entry.numInUse + 2;
			int nGroups = getBits(3, entry);
			int nSelectors = getBits(15, entry);
			for (int i = 0; i < nSelectors; i++) {
				int j = 0;
				do {
					if (getBit(entry) == 0) {
						break;
					}
					j++;
				} while (true);
				entry.selectorMtf[i] = (byte) j;
			}
			byte[] pos = new byte[6];
			for (byte v = 0; v < nGroups; v++) {
				pos[v] = v;
			}
			for (int i = 0; i < nSelectors; i++) {
				byte v = entry.selectorMtf[i];
				byte tmp = pos[v];
				for (; v > 0; v--) {
					pos[v] = pos[v - 1];
				}
				pos[0] = tmp;
				entry.selector[i] = tmp;
			}
			for (int t = 0; t < nGroups; t++) {
				int current = getBits(5, entry);
				for (int k1 = 0; k1 < alphaSize; k1++) {
					while (true) {
						if (getBit(entry) == 0) {
							break;
						}
						if (getBit(entry) == 0) {
							current++;
						} else {
							current--;
						}
					}
					entry.length[t][k1] = (byte) current;
				}
			}
			for (int t = 0; t < nGroups; t++) {
				byte minLen = 32;
				int maxLen = 0;
				for (int l1 = 0; l1 < alphaSize; l1++) {
					if (entry.length[t][l1] > maxLen) {
						maxLen = entry.length[t][l1];
					}
					if (entry.length[t][l1] < minLen) {
						minLen = entry.length[t][l1];
					}
				}
				createDecodeTables(entry.limit[t], entry.base[t], entry.perm[t], entry.length[t], minLen, maxLen, alphaSize);
				entry.minLens[t] = minLen;
			}
			int eob = entry.numInUse + 1;
			int nBlockMax = 0x186a0 * entry.blockSize100k;
			int groupNo = -1;
			int groupPos = 0;
			for (int i = 0; i <= 255; i++) {
				entry.unzftab[i] = 0;
			}
			int kk = 4095;
			for (int ii = 15; ii >= 0; ii--) {
				for (int jj = 15; jj >= 0; jj--) {
					entry.mtfa[kk] = (byte) (ii * 16 + jj);
					kk--;
				}
				entry.mtfBase[ii] = kk + 1;
			}
			int nblock = 0;
			if (groupPos == 0) {
				groupNo++;
				groupPos = 50;
				byte byte12 = entry.selector[groupNo];
				gminLen = entry.minLens[byte12];
				glimit = entry.limit[byte12];
				gperm = entry.perm[byte12];
				gbase = entry.base[byte12];
			}
			groupPos--;
			int zn = gminLen;
			int zvec;
			byte zj;
			for (zvec = getBits(zn, entry); zvec > glimit[zn]; zvec = zvec << 1 | zj) {
				zn++;
				zj = getBit(entry);
			}
			for (int nextSym = gperm[zvec - gbase[zn]]; nextSym != eob; ) {
				if (nextSym == 0 || nextSym == 1) {
					int es = -1;
					int n = 1;
					do {
						if (nextSym == 0) {
							es += n;
						} else if (nextSym == 1) {
							es += 2 * n;
						}
						n *= 2;
						if (groupPos == 0) {
							groupNo++;
							groupPos = 50;
							byte byte13 = entry.selector[groupNo];
							gminLen = entry.minLens[byte13];
							glimit = entry.limit[byte13];
							gperm = entry.perm[byte13];
							gbase = entry.base[byte13];
						}
						groupPos--;
						int zn2 = gminLen;
						int zvec2;
						byte zj2;
						for (zvec2 = getBits(zn2, entry); zvec2 > glimit[zn2]; zvec2 = zvec2 << 1 | zj2) {
							zn2++;
							zj2 = getBit(entry);
						}
						nextSym = gperm[zvec2 - gbase[zn2]];
					} while (nextSym == 0 || nextSym == 1);
					es++;
					byte b = entry.seqToUnseq[entry.mtfa[entry.mtfBase[0]] & 0xff];
					entry.unzftab[b & 0xff] += es;
					for (; es > 0; es--) {
						BZip2BlockEntry.tt[nblock] = b & 0xff;
						nblock++;
					}
				} else {
					int nn = nextSym - 1;
					byte b;
					if (nn < 16) {
						int pp = entry.mtfBase[0];
						b = entry.mtfa[pp + nn];
						for (; nn > 3; nn -= 4) {
							int z = pp + nn;
							entry.mtfa[z] = entry.mtfa[z - 1];
							entry.mtfa[z - 1] = entry.mtfa[z - 2];
							entry.mtfa[z - 2] = entry.mtfa[z - 3];
							entry.mtfa[z - 3] = entry.mtfa[z - 4];
						}
						for (; nn > 0; nn--) {
							entry.mtfa[pp + nn] = entry.mtfa[(pp + nn) - 1];
						}
						entry.mtfa[pp] = b;
					} else {
						int lno = nn / 16;
						int off = nn % 16;
						int pp = entry.mtfBase[lno] + off;
						b = entry.mtfa[pp];
						for (; pp > entry.mtfBase[lno]; pp--) {
							entry.mtfa[pp] = entry.mtfa[pp - 1];
						}
						entry.mtfBase[lno]++;
						for (; lno > 0; lno--) {
							entry.mtfBase[lno]--;
							entry.mtfa[entry.mtfBase[lno]] = entry.mtfa[(entry.mtfBase[lno - 1] + 16) - 1];
						}
						entry.mtfBase[0]--;
						entry.mtfa[entry.mtfBase[0]] = b;
						if (entry.mtfBase[0] == 0) {
							int kk2 = 4095;
							for (int ii = 15; ii >= 0; ii--) {
								for (int jj = 15; jj >= 0; jj--) {
									entry.mtfa[kk2] = entry.mtfa[entry.mtfBase[ii] + jj];
									kk2--;
								}
								entry.mtfBase[ii] = kk2 + 1;
							}
						}
					}
					entry.unzftab[entry.seqToUnseq[b & 0xff] & 0xff]++;
					BZip2BlockEntry.tt[nblock] = entry.seqToUnseq[b & 0xff] & 0xff;
					nblock++;
					if (groupPos == 0) {
						groupNo++;
						groupPos = 50;
						byte gse1 = entry.selector[groupNo];
						gminLen = entry.minLens[gse1];
						glimit = entry.limit[gse1];
						gperm = entry.perm[gse1];
						gbase = entry.base[gse1];
					}
					groupPos--;
					int zn2 = gminLen;
					int zvec2;
					byte zj2;
					for (zvec2 = getBits(zn2, entry); zvec2 > glimit[zn2]; zvec2 = zvec2 << 1 | zj2) {
						zn2++;
						zj2 = getBit(entry);
					}
					nextSym = gperm[zvec2 - gbase[zn2]];
				}
			}
			entry.stateOutLen = 0;
			entry.stateOutCh = 0;
			entry.cftab[0] = 0;
			System.arraycopy(entry.unzftab, 0, entry.cftab, 1, 256);
			for (int k2 = 1; k2 <= 256; k2++) {
				entry.cftab[k2] += entry.cftab[k2 - 1];
			}
			for (int i = 0; i < nblock; i++) {
				byte b = (byte) (BZip2BlockEntry.tt[i] & 0xff);
				BZip2BlockEntry.tt[entry.cftab[b & 0xff]] |= i << 8;
				entry.cftab[b & 0xff]++;
			}
			entry.tPos = BZip2BlockEntry.tt[entry.origPtr] >> 8;
			entry.nBlockUsed = 0;
			entry.tPos = BZip2BlockEntry.tt[entry.tPos];
			entry.k0 = (byte) (entry.tPos & 0xff);
			entry.tPos >>= 8;
			entry.nBlockUsed++;
			entry.nBlock = nblock;
			undoRle(entry);
			flag19 = entry.nBlockUsed == entry.nBlock + 1 && entry.stateOutLen == 0;
		}
	}

	private static byte getByte(BZip2BlockEntry entry) {
		return (byte) getBits(8, entry);
	}

	private static byte getBit(BZip2BlockEntry entry) {
		return (byte) getBits(1, entry);
	}

	private static int getBits(int numBits, BZip2BlockEntry entry) {
		int result;
		while (true) {
			if (entry.bsLive >= numBits) {
				int v = entry.bsBuff >> entry.bsLive - numBits & (1 << numBits) - 1;
				entry.bsLive -= numBits;
				result = v;
				break;
			}
			entry.bsBuff = entry.bsBuff << 8 | entry.input[entry.offset] & 0xff;
			entry.bsLive += 8;
			entry.offset++;
			entry.availableIn--;
			entry.totalInLo32++;
			if (entry.totalInLo32 == 0) {
				entry.totalInHi32++;
			}
		}
		return result;
	}

	private static void makeMaps(BZip2BlockEntry entry) {
		entry.numInUse = 0;
		for (int i = 0; i < 256; i++) {
			if (entry.inUse[i]) {
				entry.seqToUnseq[entry.numInUse] = (byte) i;
				entry.numInUse++;
			}
		}
	}

	private static void createDecodeTables(int[] limit, int[] base, int[] perm, byte[] length, int minLen, int maxLen, int k) {
		int pp = 0;
		for (int i = minLen; i <= maxLen; i++) {
			for (int j = 0; j < k; j++) {
				if (length[j] == i) {
					perm[pp] = j;
					pp++;
				}
			}
		}
		for (int i = 0; i < 23; i++) {
			base[i] = 0;
		}
		for (int i = 0; i < k; i++) {
			base[length[i] + 1]++;
		}
		for (int i = 1; i < 23; i++) {
			base[i] += base[i - 1];
		}
		for (int i = 0; i < 23; i++) {
			limit[i] = 0;
		}
		int vec = 0;
		for (int i = minLen; i <= maxLen; i++) {
			vec += base[i + 1] - base[i];
			limit[i] = vec - 1;
			vec <<= 1;
		}
		for (int i = minLen + 1; i <= maxLen; i++) {
			base[i] = (limit[i - 1] + 1 << 1) - base[i];
		}
	}

	private static final class BZip2BlockEntry {
		public byte[] input;
		public int offset;
		public int availableIn;
		public int totalInLo32;
		public int totalInHi32;
		public byte[] output;
		public int nextOut;
		public int availableOut;
		public int totalOutLo32;
		public int totalOutHi32;
		public byte stateOutCh;
		public int stateOutLen;
		public boolean randomised;
		public int bsBuff;
		public int bsLive;
		public int blockSize100k;
		public int blockNo;
		public int origPtr;
		public int tPos;
		public int k0;
		public final int[] unzftab = new int[256];
		public int nBlockUsed;
		public final int[] cftab = new int[257];
		public static int[] tt;
		public int numInUse;
		public final boolean[] inUse = new boolean[256];
		public final boolean[] inUse16 = new boolean[16];
		public final byte[] seqToUnseq = new byte[256];
		public final byte[] mtfa = new byte[4096];
		public final int[] mtfBase = new int[16];
		public final byte[] selector = new byte[18002];
		public final byte[] selectorMtf = new byte[18002];
		public final byte[][] length = new byte[6][258];
		public final int[][] limit = new int[6][258];
		public final int[][] base = new int[6][258];
		public final int[][] perm = new int[6][258];
		public final int[] minLens = new int[6];
		public int nBlock;
	}
}
