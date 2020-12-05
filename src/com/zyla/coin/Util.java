/***************************************************/
/*  Name:           Andre Benedito                 */
/*  Date Created:   03/22/18 (MM/DD/YY)            */
/*  Date Completed: Always Improving :)            */
/*  Description:                                   */
/*    Util class for various utility functions for */
/*    cleaner code and various other purposes      */
/***************************************************/

package com.zyla.coin;

import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.text.DecimalFormat;
import java.util.Base64;

public class Util {
	
	public static void printMessage (String stringInput) {
		int strLength = 0;
		String[] pMsg = stringInput.split("\\|");
		
		for (int c = 0; c < pMsg.length; c++) {
			if (pMsg[c].length() > strLength) {
				strLength = pMsg[c].length();
			}
		}
		
		String topBotBar = "_";
		for (int a = 0; a < strLength; a++) {
			topBotBar += "_";
		}
		topBotBar += "_";
		
		System.out.printf(" " + topBotBar + "%n");
		for (int b = 0; b < pMsg.length; b++) {
			int misLength = strLength - pMsg[b].length();
			System.out.printf("| " + pMsg[b]);
			
			for (int c = 0; c < misLength; c++) {
				System.out.printf(" ");
			}
			System.out.printf(" |%n");
		}
		System.out.printf("|" + topBotBar + "|%n");
	}
	
	public static String applySHA256 (String input) {
		try {
			MessageDigest mDig = MessageDigest.getInstance("SHA-256");
			byte[] hash = mDig.digest(input.getBytes("UTF-8"));
			StringBuffer hexHash = new StringBuffer();
			for (int a = 0; a < hash.length; a++) {
				String hex = Integer.toHexString(0xff & hash[a]);
				if (hex.length() == 1) {
					hexHash.append('0');
				}
				hexHash.append(hex);
			}
			
			return hexHash.toString();
			
		} catch (Exception e) {
			System.err.printf("%nError:%n" + e);
			return null;
		}
	}
	
	public static String getStringFromKey (PrivateKey key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	public static String getStringFromKey (PublicKey key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
	
	public static byte[] applySig (PrivateKey privateKey, String data) {
		Signature sign;
		byte[] out = new byte[0];
		try {
			sign = Signature.getInstance("SHA512withRSA");
			sign.initSign(privateKey);
			byte[] byteData = data.getBytes();
			sign.update(byteData);
			out = sign.sign();
		} catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public static boolean verifySig ( byte[] signature, PublicKey publicKey, String data) {
		Signature sign;
		try {
			sign = Signature.getInstance("SHA512withRSA");
			sign.initVerify(publicKey);
			sign.update(data.getBytes());
			
			return sign.verify(signature);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getHashRate (Long hRate, Long time) {
		int byteSize = 1024;
		double hR = hRate / (((double) time) / 1000000000);
		String outRate = "";
		String[] rates = new String[] {" H/s", " KH/s", " MH/s", " GH/s", " TH/s", " PH/s"};
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		
		for (int a = 0; a < rates.length; a++) {
			if (hR > Math.pow(byteSize, (a))) {
				outRate = df.format(hR / Math.pow(byteSize, a)) + rates[a];
			}
		}
		
		return outRate;
	}

	public static byte[] longToBytes(long seqNum) {
		byte[] out = new byte[8];
		for (int i = 7; i >= 0; i--) {
			out[i] = (byte)(seqNum & 0xFF);
			seqNum >>= 8;
		}
		return out;
	}
}