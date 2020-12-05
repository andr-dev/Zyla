package com.zyla.network;

import com.zyla.coin.Util;

public class ZylaProtocol {
	private final String header = "ZylaProtocol";
	private final long seqNum;
	private final byte dataType; // 0 = block
	private int dataLength;
	private BlockData data;
	private String hash;
	private final String footer = "ProtoEnd";
	
	public ZylaProtocol (int seqNum, byte dataType, int dataLength, BlockData data) {
		this.seqNum = seqNum;
		this.dataType = dataType;
		this.dataLength = dataLength;
		this.data = data;
		this.hash = Util.applySHA256("" + this.seqNum + this.dataType + this.dataLength + this.data);
		Util.printMessage( "Header          : " + this.header
						+ "|Sequence Number : " + this.seqNum
						+ "|Data Type       : " + this.dataType
						+ "|Data Length     : " + this.dataLength
						+ "|Data            : " + this.data.toString()
						+ "|SHA256          : " + this.hash
						+ "|Footer          : " + this.footer);
		
	}
	
	public byte[] toByteArray () {
		byte[] out = new byte[512];
		
		
		return new byte[2];
	}
}
