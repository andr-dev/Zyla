/**********************************************************/
/*  Name:           Andre Benedito                        */
/*  Date Created:   03/11/18 (MM/DD/YY)                   */
/*  Date Completed: 03/22/18                              */
/*  Description:                                          */
/*    Creates a wallet with a public private key pair for */
/*    use in transactions. Has functions to generate key  */
/*    pair or load from file.                             */
/*  Resources:                                            */
/*    https://docs.oracle.com/javase/tutorial/security/   */
/*      apisign/step2.html                                */
/**********************************************************/

package com.zyla.coin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	private HashMap<String, TransactionOutput> UTXOMap = new HashMap<String, TransactionOutput>();
	
	public Wallet (String PUBKF, String PRVKF, boolean exists) { // Create wallet from new Private/Public Pair
		if (exists) {
			loadKeyPair(PUBKF, PRVKF);
		} else {
			try {
				genKeyPair(PUBKF, PRVKF);
			} catch (NoSuchAlgorithmException e) {
				Util.printMessage("Invalid Algorithm");
			}
		}
	}
	
	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}
	
	public PublicKey getPublicKey() {
		return this.publicKey;
	}
	
	private KeyPair genKeyPair(String PUBLIC_KEY_FILE, String PRIVATE_KEY_FILE) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(2048);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
		
		File publicKeyFile = new File(PUBLIC_KEY_FILE);
		if (!publicKeyFile.exists()) {
			publicKeyFile.getParentFile().mkdirs();
		}
		
		saveKeyToFile(publicKeyFile, this.publicKey);
		
		File privateKeyFile = new File(PRIVATE_KEY_FILE);
		if (!privateKeyFile.exists()) {
			privateKeyFile.getParentFile().mkdirs();
		}
		
		saveKeyToFile(privateKeyFile, this.privateKey);
		return keyPair;
	}
	
	private void loadKeyPair (String PUBKF, String PRVKF) {
		try {
			Path pathPrv = Paths.get(PRVKF);
			byte[] bytesPrv = Files.readAllBytes(pathPrv);
			PKCS8EncodedKeySpec ksP = new PKCS8EncodedKeySpec(bytesPrv);
			
			Path pathPub = Paths.get(PUBKF);
			byte[] bytesPub = Files.readAllBytes(pathPub);
			X509EncodedKeySpec ksX = new X509EncodedKeySpec(bytesPub);
			
			KeyFactory kf = KeyFactory.getInstance("RSA");
			this.privateKey = kf.generatePrivate(ksP);
			this.publicKey = kf.generatePublic(ksX);
			Util.printMessage("COMPLETE");
		} catch (Exception e) {
			Util.printMessage(e + "");
		}
	}
	
	private void saveKeyToFile (File filePath, Key key) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			out.write(key.getEncoded());
			Util.printMessage(filePath + "|" + key.getFormat());
		} catch (IOException e) {
			Util.printMessage("ERROR : IO Exception");
		}
	}
	
	public double getBalance () {
		double total = 0;
		for (Map.Entry<String, TransactionOutput> item: ZylaBlockChain.UTXOMap.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			if (UTXO.pertainsTo(publicKey)) {
				UTXOMap.put(UTXO.id, UTXO);
				total += UTXO.value;
			}
		}
		return total;
	}
	
	public Transaction createTransaction (PublicKey reciever, double value) {
		if (getBalance() < value) {
			Util.printMessage("Not enough funds to complete transaction.");
			return null;
		}
		
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		
		double total = 0;
		
		for (Map.Entry<String, TransactionOutput> tOut: this.UTXOMap.entrySet()) {
			TransactionOutput utxo = tOut.getValue();
			total += utxo.value;
			inputs.add(new TransactionInput(utxo.id));
			if (total >= value) { // or equal to?
				break;
			}
		}
		
		/* DEBUG */
		// Util.printMessage("Total : " + total);
		
		Transaction output = new Transaction(this.publicKey, reciever, value, inputs);
		output.genSignature(this.privateKey);
		
		for (TransactionInput in: inputs) {
			UTXOMap.remove(in.TransactionOutputID);
		}
		return output;
	}
}