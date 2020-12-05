/******************************************************************/
/*  Name:           Andre Benedito                                */
/*  Date Created:   03/11/18 (MM/DD/YY)                           */
/*  Date Completed: 03/19/18                                      */
/*  Description:                                                  */
/*    Block class to create Blockchain ArrayList                  */
/*    Also for mining :)                                          */
/*  Resources:                                                    */
/*    https://dev.to/damcosset/blockchain-what-is-in-a-block-48jo */
/*    https://github.com/ethereum/wiki/wiki/White-Paper#mining    */
/******************************************************************/

package com.zyla.coin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class Block implements Serializable {
//	private static final long serialVersionUID = -963096774997274046L;
	
	private String currentHash;
	private String previousHash;
	private String merkleRoot;
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private long timeStamp; // Standard Time Stamp - # of milliseconds since 1/1/1970
	private int nonce;
	
	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.currentHash = calcHash();
	}
	
	public String getHash () {
		return this.currentHash;
	}
	public String getPrevHash () {
		return this.previousHash;
	}
	public Long getTimeStamp () {
		return this.timeStamp;
	}
	public String getMerkleRoot () {
		return this.merkleRoot;
	}
	public ArrayList<Transaction> getTransactions () {
		return this.transactions;
	}
	public int getNonce () {
		return this.nonce;
	}
	
	public String calcHash() {
		return Util.applySHA256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
	}
	
	public void mineBlock (int dif) {
		merkleRoot = MerkleTree.getMerkleTreeRoot(transactions);
		String targStr = new String( new char[dif]).replace('\0', '0');
		Long a = (long) 0;
		
		Long sTime = System.nanoTime();
		while(!(currentHash.substring(0, dif).equals(targStr))) {
			nonce ++;
			currentHash = calcHash();
			a++;
		}
		Long eTime = System.nanoTime();
		
		Util.printMessage("Update:|Block Mined        :  " + currentHash + "|Hashes Calculated  :  " + a + "|Hashrate           :  " + Util.getHashRate(a, (eTime - sTime)));
	}
	
	public boolean addTransaction (Transaction transaction) {
		if (transaction == null) {
			return false;
		}
		char a = '0';
		String s = "";
		for ( int i = 0; i < 64; ++i) {
			s += a;
		}
		if (!s.equals(previousHash)) {
			if (transaction.process() != true) {
				Util.printMessage("Transaction Failed");
				return false;
			}
		}
		
		transactions.add(transaction);
		Util.printMessage("Transaction added to the Blockchain");
		return true;
	}
}