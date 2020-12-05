/*******************************************************/
/*  Name:           Andre Benedito                     */
/*  Date Created:   03/22/18 (MM/DD/YY)                */
/*  Date Completed: 04/15/18                           */
/*  Description:                                       */
/*    Simple util function to hash string using SHA512 */
/*  Resources:                                         */
/*    https://stackoverflow.com/questions/33085493/    */
/*      hash-a-password-with-sha-512-in-java           */
/*******************************************************/

package com.zyla.coin;

import java.security.MessageDigest;

public class SHA512 {
    public String hash(String text) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-512"); // because SHA-256 ain't enough, tisk tisk bitcoin
        } catch (Exception e) {
            e.printStackTrace();
        }

        md.update(text.getBytes());
        byte byteData[] = md.digest();

        StringBuffer strBuffer = new StringBuffer();
        for (int a = 0; a < byteData.length; a++) {
            strBuffer.append(Integer.toString((byteData[a] & 0xff) + 0x100, 16).substring(1));
        }
        return strBuffer.toString().toUpperCase(); // Optional upper case format
    }
}