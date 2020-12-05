package com.zyla.network;

import java.nio.ByteBuffer;
import java.util.Arrays;

import com.zyla.coin.Util;

public class BlockHeaderData {
	
	private final int blockHeaderSize = 140;
	private String previousHash;
	private String merkleRoot;
	private long timeStamp;
	private int nonce;
	
	public BlockHeaderData (String previousHash, String merkleRoot, long timeStamp, int nonce) {
		this.previousHash = previousHash;
		this.merkleRoot = merkleRoot;
		this.timeStamp = timeStamp;
		this.nonce = nonce;
	}
	
	public int getHeaderSize () {
		return blockHeaderSize;
	}
	
	public byte[] convertToByteArray () {
		byte[] out = new byte[blockHeaderSize];
		byte[] tSByte = ByteBuffer.allocate(Long.BYTES).putLong(this.timeStamp).array();
		byte[] str;
		int buf = 0;
		
		// Previous Hash
		str = this.previousHash.getBytes();
		System.arraycopy(str, 0, out, buf, str.length);
		Util.printMessage(str.length + "");
		buf += str.length;
		
		// Merkle Root
		str = this.merkleRoot.getBytes();
		System.arraycopy(str, 0, out, buf, str.length);
		Util.printMessage(str.length + "");
		buf += str.length;
		
		// Time Stamp
		str = tSByte;
		System.arraycopy(str, 0, out, buf, str.length);
		Util.printMessage(str.length + "");
		buf += str.length;
		
		// Nonce
		str = ByteBuffer.allocate(Integer.BYTES).putInt(this.nonce).array();
		System.arraycopy(str, 0, out, buf, str.length);
		Util.printMessage(str.length + "");
		buf += str.length;
		Util.printMessage(" P Hash : " + Arrays.toString(this.previousHash.getBytes()) + "| Merkle : " +  Arrays.toString(this.merkleRoot.getBytes()) + "| T Stamp : " + Arrays.toString(tSByte) + "| Nonce : " + this.nonce);
		
		return out;
	}
}