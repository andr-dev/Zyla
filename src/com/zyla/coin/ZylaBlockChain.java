/*********************************************************/
/*  Name:           Andre Benedito                       */
/*  Date Created:   03/11/18 (MM/DD/YY)                  */
/*  Date Completed: 03/22/18                             */
/*  Description:                                         */
/*    Main class for blockchain                          */
/*      Where everything comes together :)               */
/*  Resources:                                           */
/*    https://github.com/ethereum/wiki/wiki/White-Paper# */
/*      bitcoin-as-a-state-transition-system             */
/*    https://bitcoin.org/bitcoin.pdf                    */
/*********************************************************/

/************************************************************/
/*                                                          */
/*    BTW my hopes were to implement sockets and make a     */
/*    consensus algorithm but i soon learned sockets are    */
/*    a lot harder than what they seem plus for a block-    */
/*    chain to work I need to do client server at the       */
/*    same time which means I also need to learn threads    */
/*    which I then need to deal with ... its difficult      */
/*                                                          */
/*----------------------------------------------------------*/
/*                                                          */
/*    If I have time i will delete this and just throw in   */
/*    the consensus algorithm and all will be good          */
/*            (If I remember to delete this XD)             */
/*                                                          */
/************************************************************/

package com.zyla.coin;

import java.security.Security;

// import java.net.ServerSocket;
// import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;

public class ZylaBlockChain {
	public static HashMap<String, TransactionOutput> UTXOMap = new HashMap<String, TransactionOutput>();
	public static double minTrans = 0.01;
	public static Transaction genTransaction;
	public static ArrayList<Block> bChain = new ArrayList<Block>();
	private static int dif = 5;
	
	private static Block newBlock;
	
	public ZylaBlockChain () {
		bChain = new ArrayList<Block>();
	}
	
	public ArrayList<Block> getChain () {
		return bChain;
	}
	
	public void addToBlockChain () {
		bChain.add(new Block(bChain.get(bChain.size() - 1).getHash()));
	}
	
//	public void genGenesis () {
//		char a = '0';
//		String s = "";
//		for ( int i = 0; i < 64; ++i) {
//			s += a;
//		}
//		bChain.add(new Block(s));
//	}
	
	public static Boolean isChainValid() {
		Block cBlock, pBlock; // Current Block + Previous Block
		String hTarget = new String(new char[dif]).replace('\0', '0');
		HashMap<String, TransactionOutput> tUTXOMap = new HashMap<String, TransactionOutput>();
		tUTXOMap.put(genTransaction.outputs.get(0).id, genTransaction.outputs.get(0));
		
		for (int a = 1; a < bChain.size(); a++) {
			cBlock = bChain.get(a);
			pBlock = bChain.get(a - 1);
			
			if (!cBlock.getHash().equals(cBlock.calcHash())) { // Check If Block Hash Is The Correct Hash
				Util.printMessage("Invalid Hash : Current Hash Not Equal");
				return false;
			}
			
			if (!pBlock.getHash().equals(cBlock.getPrevHash())) { // Check If Previous Block Hash Is The Correct Hash
				Util.printMessage("Invalid Hash : Previous Hash Not Equal");
				return false;
			}
			
			if (!cBlock.getHash().substring(0, dif).equals(hTarget)) { // Check If Block Hash Has Been Mined
				Util.printMessage("Invalid Hash : Block Has Not Been Mined");
				return false;
			}
			
			TransactionOutput tOutput;
			
			for (int b = 0; b < cBlock.getTransactions().size(); b++) { // For Each Transaction In The Current Block
				Transaction cTransaction = cBlock.getTransactions().get(b);
				
				if (!cTransaction.verSignature()) { // Verify The Transaction Signature Is Valid
					Util.printMessage("Invalid Signature : Transaction " + b + " [" + cTransaction.transID + "] Is Invalid");
					return false;
				}
				
				if (cTransaction.getInputValue() != cTransaction.getOutputValue()) { // Check If Input Value Is Equal To Output Value
					Util.printMessage("Invalid Input/Output : Input Is Not Equal To Output");
					return false;
				}
				
				for (TransactionInput in : cTransaction.inputs) { // For Each Of The Inputs In The Transaction
					tOutput = tUTXOMap.get(in.TransactionOutputID);
					
					if (tOutput == null) { // Check If Input Is Null (Is Missing)
						Util.printMessage("Invalid Input : Input Referenced On Transaction " + b + " [" + in.TransactionOutputID + "] Does Not Exist");
						return false;
					}
					
					if (in.UTXO.value != tOutput.value) { // Check If Input Is Invalid
						Util.printMessage("Invalid Input : Input Referenced On Transaction " + b + " [" + in.TransactionOutputID + " Is Invalid");
						return false;
					}
					
					tUTXOMap.remove(in.TransactionOutputID);
				}
				
				for (TransactionOutput out : cTransaction.outputs) { // For Each Of The Outputs
					tUTXOMap.put(out.id, out);
				}
				
				if (cTransaction.outputs.get(0).owner != cTransaction.reciever) { // Check If Transaction Recipient Matches Output Recipient
					Util.printMessage("Invalid Parameter : Output Recipient On Transaction " + b + " [" + cTransaction.transID + "] Does Not Match Transaction Output");
					return false;
				}
				
				if (cTransaction.outputs.get(1).owner != cTransaction.sender) { // Check If Transaction Sender Matches Output Sender
					Util.printMessage("Invalid Parameter : Output On Transaction" + b + " [" + cTransaction.transID + " Is Not Sender");
					return false;
				}
			}
		}
		return true;
	}
	
	private Wallet wallet;
	private Wallet wallet2;
	private Wallet wallet3;
	private Wallet genWallet;
	
	public void genWallets() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		wallet = new Wallet("C:\\Users\\Andre\\Desktop\\publickey1.pub", "C:\\Users\\Andre\\Desktop\\privatekey1.key", false);
		wallet2 = new Wallet("C:\\Users\\Andre\\Desktop\\publickey2.pub", "C:\\Users\\Andre\\Desktop\\privatekey2.key", false);
		wallet3 = new Wallet("C:\\Users\\Andre\\Desktop\\publickey3.pub", "C:\\Users\\Andre\\Desktop\\privatekey3.key", false);
		genWallet = new Wallet("C:\\Users\\Andre\\Desktop\\publickey4.pub", "C:\\Users\\Andre\\Desktop\\privatekey4.key", false);
		Util.printMessage("Wallet 1:|Private Key : " + wallet.getPrivateKey() + "|Public Key : " + wallet.getPublicKey());
		Util.printMessage("Wallet 2:|Private Key : " + wallet2.getPrivateKey() + "|Public Key : " + wallet2.getPublicKey());
		Util.printMessage("Wallet 3:|Private Key : " + wallet3.getPrivateKey() + "|Public Key : " + wallet3.getPublicKey());
		Util.printMessage("Gen Wallet 4:|Private Key : " + genWallet.getPrivateKey() + "|Public Key : " + genWallet.getPublicKey());
	}
	
	public void genGenesis() {
		genTransaction = new Transaction(genWallet.getPublicKey(), wallet.getPublicKey(), 100, new ArrayList<TransactionInput>());
		genTransaction.genSignature(genWallet.getPrivateKey());
		genTransaction.transID = "0000000000000000000000000000000000000000000000000000000000000001";
		genTransaction.outputs.add(new TransactionOutput(genTransaction.reciever, genTransaction.value, genTransaction.transID));
		UTXOMap.put(genTransaction.outputs.get(0).id, genTransaction.outputs.get(0));
		char a = '0';
		String s = "";
		for ( int i = 0; i < 64; ++i) {
			s += a;
		}
		Block gen = new Block(s);
		gen.addTransaction(genTransaction);
		addBlock(gen);
		Util.printMessage(gen.getMerkleRoot() + "");
		
		Util.printMessage("Genesis|Wallet 1 Balance : " + wallet.getBalance() + "|Wallet 2 Balance : " + wallet2.getBalance() + "|Wallet 3 Balance : " + wallet3.getBalance());
	}
	
	public void perpareBlock() {
		
	}
	
//	
//	public static void main (String[] args) {
//		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//		
//		Wallet wallet = new Wallet("C:\\Users\\Andre\\Desktop\\publickey1.pub", "C:\\Users\\Andre\\Desktop\\privatekey1.key", false);
//		Wallet wallet2 = new Wallet("C:\\Users\\Andre\\Desktop\\publickey2.pub", "C:\\Users\\Andre\\Desktop\\privatekey2.key", false);
//		Wallet wallet3 = new Wallet("C:\\Users\\Andre\\Desktop\\publickey3.pub", "C:\\Users\\Andre\\Desktop\\privatekey3.key", false);
//		Wallet genWallet = new Wallet("C:\\Users\\Andre\\Desktop\\publickey4.pub", "C:\\Users\\Andre\\Desktop\\privatekey4.key", false);
//		Util.printMessage("Wallet 1:|Private Key : " + wallet.getPrivateKey() + "|Public Key : " + wallet.getPublicKey());
//		Util.printMessage("Wallet 2:|Private Key : " + wallet2.getPrivateKey() + "|Public Key : " + wallet2.getPublicKey());
//		Util.printMessage("Wallet 3:|Private Key : " + wallet3.getPrivateKey() + "|Public Key : " + wallet3.getPublicKey());
//		Util.printMessage("Gen Wallet 4:|Private Key : " + genWallet.getPrivateKey() + "|Public Key : " + genWallet.getPublicKey());
//		
//		genTransaction = new Transaction(genWallet.getPublicKey(), wallet.getPublicKey(), 100, null);
//		genTransaction.genSignature(genWallet.getPrivateKey());
//		genTransaction.transID = "0";
//		genTransaction.outputs.add(new TransactionOutput(genTransaction.reciever, genTransaction.value, genTransaction.transID));
//		UTXOMap.put(genTransaction.outputs.get(0).id, genTransaction.outputs.get(0));
//		
//		Util.printMessage("Creating Block");
//		Block genesis = new Block("0");
//		genesis.addTransaction(genTransaction);
//		addBlock(genesis);
//		
//		Util.printMessage("Genesis|Wallet 1 Balance : " + wallet.getBalance() + "|Wallet 2 Balance : " + wallet2.getBalance() + "|Wallet 3 Balance : " + wallet3.getBalance());
//		
//		Block block1 = new Block(genesis.getHash());
//		block1.addTransaction(wallet.createTransaction(wallet2.getPublicKey(), 14));
//		addBlock(block1);
//		
//		Util.printMessage("Block1|Wallet 1 Balance : " + wallet.getBalance() + "|Wallet 2 Balance : " + wallet2.getBalance() + "|Wallet 3 Balance : " + wallet3.getBalance());
//		Util.printMessage("isChainValid?|" + isChainValid());
//		
//		Block block2 = new Block(block1.getHash());
//		block2.addTransaction(wallet2.createTransaction(wallet3.getPublicKey(), 8));
//		block2.addTransaction(wallet3.createTransaction(wallet2.getPublicKey(), 3));
//		addBlock(block2);
//		Util.printMessage("Block 2 Size : " + block2.getTransactions().size());
//		
//		Util.printMessage("Block1|Wallet 1 Balance : " + wallet.getBalance() + "|Wallet 2 Balance : " + wallet2.getBalance() + "|Wallet 3 Balance : " + wallet3.getBalance());
//		Util.printMessage("isChainValid?|" + isChainValid());
//	}
//	
	public static void addBlock (Block b) {
		b.mineBlock(dif);
		bChain.add(b);
	}
}