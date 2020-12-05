package com.zyla.network;

import com.zyla.coin.Util;

public class BlockData {
	private int blockSize; // to be defined
	private BlockHeaderData blockHeader; // 140 bytes
	private int txCount; // 4 bytes (max 2^32 = 4,294,967,296) (always min is 1 since coinbase is one)
	private CoinbaseData coinbase; // coinbase transaction
	private TransactionData rawTransactions; // Defined by txCount * 64 bytes
	
	public BlockData (BlockHeaderData blockHeader, int txCount) {
		this.blockHeader = blockHeader;
		this.txCount = txCount;
		this.coinbase = new CoinbaseData();
		this.rawTransactions = new TransactionData();
		this.blockSize = blockHeader.getHeaderSize() + txCount + coinbase.getCoinbaseSize() + (rawTransactions.getTransactionSize() * txCount); // add function to calculate block size
	}
	
	public int getBlockSize () {
		return 1;
	}
	
	public boolean addTransactionToBlock (byte[] rawTransactions) {
		if (rawTransactions.length != (txCount * 64)) {
			Util.printMessage("Error : Raw transaction count is greater than defined in BlockHeaderData!");
			return false;
		}
		
		// do stuff to add to rawTransactions
		
		return true;
	}
	
	@Override
	public String toString () {
		return "This part is still under development";
	}
	
	public byte[] convertToByteArray () {
		byte[] out = new byte[blockSize];
		byte[] str = new byte[blockSize];
		int buf = 0;
		
		// Block Header in Byte Form
		str = blockHeader.convertToByteArray();
		System.arraycopy(str, 0, out, buf, str.length);
		Util.printMessage(str.length + "");
		buf += str.length;
		
		// Coinbase Transaction
		str = coinbase.convertToByteArray();
		System.arraycopy(str, 0, out, buf, str.length);
		Util.printMessage(str.length + "");
		buf += str.length;
		
		for (int a = 0; a < txCount; a++) {
			// Transactions to Byte Form
			str = rawTransactions.convertToByteArray();         // change rawTransactions to an array and fo rawTransactions[a].convertToByteArray();
			System.arraycopy(str, 0, out, buf, str.length);
			Util.printMessage(str.length + "");
			buf += str.length;
		}
		
		return out;
	}
}