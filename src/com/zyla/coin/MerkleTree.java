/*********************************************************************/
/*  Name:           Andre Benedito                                   */
/*  Date Created:   03/17/18 (MM/DD/YY)                              */
/*  Date Completed: 04/02/18 (MM/DD/YY)                              */
/*  Description:                                                     */
/*    getMerkleTreeRoot()                                            */
/*      Creates a hash from multiple transactions to be              */
/*      included in the block to prevent transactions                */
/*      from being changed                                           */
/*    How it works:                                                  */
/*      Say there are 4 transactions: a, b, c & d                    */
/*      Hash A, B, C and D                                           */
/*      Hash(a + b) = e and hash(c + d) = f                          */
/*      Hash(e + f) = merkleTreeRoot                                 */
/*      If any of the original transactions are changed              */
/*      the final hash, the root hash, will change.                  */
/*  Resources:                                                       */
/*    https://github.com/ethereum/wiki/wiki/White-Paper#merkle-trees */
/*    https://bitcoin.org/bitcoin.pdf                                */
/*********************************************************************/

package com.zyla.coin;

import java.util.ArrayList;

public class MerkleTree {
    public static String getMerkleTreeRoot (ArrayList<Transaction> transactions) {
        int count = transactions.size();
        ArrayList<String> prevTreeLayer = new ArrayList<String>();

        for (Transaction transaction : transactions) {
            prevTreeLayer.add(transaction.transID);
        }

        ArrayList<String> curTreeLayer = prevTreeLayer;
        while (count > 1) {
            curTreeLayer = new ArrayList<String>();
            for (int a = 1; a < prevTreeLayer.size(); a++) {
                curTreeLayer.add(Util.applySHA256(prevTreeLayer.get(a - 1) + prevTreeLayer.get(a)));
            }
            count = curTreeLayer.size();
            prevTreeLayer = curTreeLayer;
        }

        String merkleRoot = (curTreeLayer.size() == 1) ? curTreeLayer.get(0) : "";
        return merkleRoot;
    }
}
