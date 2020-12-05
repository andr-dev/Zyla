package com.zyla.network.old;

import com.zyla.coin.Transaction;
import com.zyla.coin.Util;

public class CoinbaseData {

    private final int coinbaseSize = 64;

    public CoinbaseData (Transaction trans) {
//		Util.printMessage( "Transaction ID              : " + trans.calcTransHashCoinbase()
//						+ "|Transaction Input #          : " + trans.inputs.size()
//						+ "|Transaction Output #         : " + trans.outputs.size()
//						+ "|Transaction Input(s)         : " + trans.inputs.toString()
//		+ "|Transactino Output(s)        : " + trans.outputs.toString()
//		+ "|Transaction Signature        : " + trans.signature.length);
    }

    public int getCoinbaseSize() {
        return coinbaseSize;
    }

    public byte[] convertToByteArray () {
        return new byte[64]; // TODO
    }
}