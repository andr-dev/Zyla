package com.zyla.network;

import com.zyla.coin.Util;

public class BlockData {
	private int blockSize; // to be defined
	private BlockHeaderData blockHeader; // 140 bytes
	private int txCount; // 4 bytes (max 2^32 = 4,294,967,296)
	private CoinbaseData coinbase; // coinbase transaction
	private TransactionData[] rawTransactions; // Defined by txCount * 64 bytes
	
	public BlockData (BlockHeaderData blockHeader, int txCount, CoinbaseData coinbaseTransaction, TransactionData[] rawTransactions) {
		this.blockHeader = blockHeader;
		this.txCount = txCount;
		this.rawTransactions = new TransactionData[txCount];
		this.coinbase = coinbaseTransaction;
		for (int a = 0; a < rawTransactions.length; a++) {
			this.rawTransactions[a] = rawTransactions[a];
		}
		
		// this.blockSize = blockHeader.getHeaderSize() + Integer.BYTES + coinbase.getCoinbaseSize() + (rawTransactions[0].getTransactionSize() * txCount); // add function to calculate block size
		this.blockSize = blockHeader.getHeaderSize() + Integer.BYTES + (rawTransactions[0].getTransactionSize() * txCount); // add function to calculate block size
	}
	
	public int getSize () {
		return blockSize;
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
		buf += str.length;
		
		// Coinbase Transaction
		str = coinbase.convertToByteArray();
		System.arraycopy(str, 0, out, buf, str.length);
		buf += str.length;
		
		for (int a = 0; a < txCount; a++) {
			// Transactions to Byte Form
			str = rawTransactions[a].convertToByteArray();
			System.arraycopy(str, 0, out, buf, str.length);
			buf += str.length;
		}
		
		return out;
	}
}