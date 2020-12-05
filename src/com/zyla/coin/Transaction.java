/*********************************************************/
/*  Name:           Andre Benedito                       */
/*  Date Created:   03/22/18 (MM/DD/YY)                  */
/*  Date Completed: 04/15/18                             */
/*  Description:                                         */
/*    Create transaction from transactionInput and       */
/*    transactionOutput                                  */
/*  Resources:                                           */
/*    https://bitcoin.org/bitcoin.pdf (Page 2)           */
/*    https://github.com/ethereum/wiki/wiki/White-Paper# */
/*      bitcoin-as-a-state-transition-system             */
/*********************************************************/

package com.zyla.coin;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
	public PublicKey sender;
	public PublicKey reciever;
	public String transID;
	public byte[] signature;
	public double value;
	
	private static int seq = 0;
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	
	public Transaction (PublicKey from, PublicKey to, double value, ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.reciever = to;
		this.value = value;
		this.inputs = inputs;
	}
	
	private String calcTransHash() {
		seq++;
		return Util.applySHA256(Util.getStringFromKey(sender) + Util.getStringFromKey(reciever) + Double.toString(value) + seq);
	}
	
	public void genSignature (PrivateKey PrivateKey) {
		signature = Util.applySig(PrivateKey, (Util.getStringFromKey(sender) + Util.getStringFromKey(reciever) + Double.toString(value)));
	}
	
	public boolean verSignature () {
		return Util.verifySig(signature, sender, (Util.getStringFromKey(sender) + Util.getStringFromKey(reciever) + Double.toString(value)));
	}
	
	public double getInputValue() {
		double total = 0;
		for (TransactionInput a : inputs) {
			if (a.UTXO != null) {
				total += a.UTXO.value;
			}
		}
		return total;
	}
	
	public double getOutputValue() {
		double total = 0;
		for (TransactionOutput b : outputs) {
			total += b.value;
		}
		return total;
	}
	
	public boolean process () {
		if (verSignature() != true) {
			Util.printMessage("Transaction Signature Failed");
			return false;
		}
		
		for (TransactionInput c : inputs) {
			c.UTXO = ZylaBlockChain.UTXOMap.get(c.TransactionOutputID);
		}
		
		if (getInputValue() < ZylaBlockChain.minTrans) {
			Util.printMessage("Too little inputs [" + getInputValue() + "]");
			return false;
		}
		
		double afterValue = getInputValue() - value;
		transID = calcTransHash();
		outputs.add(new TransactionOutput(this.reciever, value, transID));
		outputs.add(new TransactionOutput(this.sender, afterValue, transID));
		
		for (TransactionOutput a : outputs) {
			ZylaBlockChain.UTXOMap.put(a.id, a);
			/* DEBUG */
			// Util.printMessage("Input T_UTXO|id : " + a.id + "|value : " + a.value + "|owner : " + a.owner + "|parent-transaction : " + a.ParentTransactionID);
		}
		
		for (TransactionInput a : inputs) {
			if (a.UTXO != null) {
				ZylaBlockChain.UTXOMap.remove(a.UTXO.id, a.UTXO);
				/* DEBUG */
				// Util.printMessage("Output T_UTXO|id : " + a.UTXO.id + "|value : " + a.UTXO.value + "|owner : " + a.UTXO.owner + "|parent-transaction : " + a.UTXO.ParentTransactionID);
			}
		}
		
		return true;
	}
}