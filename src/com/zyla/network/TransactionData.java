package com.zyla.network;

import com.zyla.coin.Transaction;
import com.zyla.coin.Util;

public class TransactionData {
	
	private final int transactionDataSize;
	
	public TransactionData (Transaction trans) {
		transactionDataSize = trans.transID.length() + (Integer.BYTES * 2) + (trans.inputs.size() * new TransactionInputData().getSize() + (trans.outputs.size() * new TransactionOutputData().getSize()));
		Util.printMessage( "Transsaction ID              : " + trans.transID.length()
						+ "|Transaction Input #          : " + trans.inputs.size()
						+ "|Transaction Output #         : " + trans.outputs.size()
						+ "|Transaction Input(s)         : " + trans.inputs.size() * new TransactionInputData().getSize()
						+ "|Transactino Output(s)        : " + trans.outputs.size() * new TransactionOutputData().getSize()
						+ "|Transaction Signature        : " + trans.signature.length);
	}

	public int getTransactionSize() {
		return transactionDataSize;
	}

	public byte[] convertToByteArray() {
		return new byte[64];
	}
}