package com.zarador.cache;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public final class RSCache implements AutoCloseable {
	public static RSCache create(File root) throws IOException {
		File cacheDirectory = root;
		File data = new File(cacheDirectory, "main_file_cache.dat");
		if (!data.exists()) {
			System.err.println("File not found: " + data.getAbsolutePath());
			throw new FileNotFoundException();
		}
		ExtendedRandomAccessFile[] indexFiles = new ExtendedRandomAccessFile[255];
		int file = 0;
		while (file < 254) {
			File index = new File(cacheDirectory, "main_file_cache.idx" + file);
			if (!index.exists()) {
				break;
			}
			indexFiles[file++] = new ExtendedRandomAccessFile(index, "r");
		}
		if (file == 0) {
			throw new FileNotFoundException();
		}
		return new RSCache(new File(root, "data"), new ExtendedRandomAccessFile(data, "r"), Arrays.copyOf(indexFiles, file));
	}

	private final File dataRoot;
	private final ExtendedRandomAccessFile dataFile;
	private final ExtendedRandomAccessFile[] indexFiles;

	private RSCache(File dataRoot, ExtendedRandomAccessFile dataFile, ExtendedRandomAccessFile[] indexFiles) throws IOException {
		this.dataRoot = dataRoot;
		this.dataFile = dataFile;
		this.indexFiles = indexFiles;
	}

	public byte[] getFile(int index, int file) {
		try {
			int sectorAmount = (int) (dataFile.length() / 520L);
			indexFiles[index].seek(file * 6);
			int fileSize = indexFiles[index].readUnsignedMediumInt();
			if (fileSize == 0) {
				//System.err.println("Rej: " + index + ',' + file);
			}
			int sector = indexFiles[index].readUnsignedMediumInt();
			index += 1;
			byte[] data = new byte[fileSize];
			int offset = 0;
			int chunk = 0;
			while (offset < fileSize) {
				if (sector == 0) {
					throw new CacheFormatException("Unexpected sector");
				}
				dataFile.seek(sector * 520);
				int sectorFileId = dataFile.readUnsignedShort();
				int sectorChunk = dataFile.readUnsignedShort();
				int nextSector = dataFile.readUnsignedMediumInt();
				int sectorIndexFile = dataFile.readUnsignedByte();
				if (sectorFileId != file) {
					throw new CacheFormatException("File id mismatch: " + file + " != " + sectorFileId);
				}
				if (sectorChunk != chunk++) {
					throw new CacheFormatException("Chunk mismatch: " + (chunk - 1) + " != " + sectorChunk);
				}
				if (nextSector < 0 || nextSector > sectorAmount) {
					throw new CacheFormatException("Sector does not exist: " + nextSector);
				}
				if (sectorIndexFile != index) {
					throw new CacheFormatException("Index file mismatch: " + index + " != " + sectorIndexFile);
				}
				int remaining = fileSize - offset;
				if (remaining > 512) {
					remaining = 512;
				}
				dataFile.read(data, offset, remaining);
				sector = nextSector;
				offset += remaining;
			}
			return data;
		} catch (IOException e) {
			return null;
		}
	}

	public byte[] getDataFile(String name) {
		try {
			return Files.readAllBytes(new File(name).toPath());
		} catch (IOException e) {
			return null;
		}
	}

	public int getCacheFileCount() {
		return indexFiles.length;
	}

	public int getIndexFileCount(int cacheFile) {
		try {
			return (int) (indexFiles[cacheFile].length() / 6);
		} catch (IOException e) {
			return 0;
		}
	}

	@Override
	public void close() throws IOException {
		dataFile.close();
		for (RandomAccessFile file : indexFiles) {
			file.close();
		}
	}

	private static final class ExtendedRandomAccessFile extends RandomAccessFile {
		public ExtendedRandomAccessFile(File file, String mode) throws FileNotFoundException {
			super(file, mode);
		}

		public int readUnsignedMediumInt() throws IOException {
			int ch1 = super.read() & 0xFF;
			int ch2 = super.read() & 0xFF;
			int ch3 = super.read() & 0xFF;
			if ((ch1 | ch2 | ch3) < 0) {
				throw new EOFException();
			}
			return (ch1 << 16) | (ch2 << 8) | ch3;
		}
	}
}
