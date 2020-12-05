/*********************************************************/
/*  Name:           Andre Benedito                       */
/*  Date Created:   03/17/18 (MM/DD/YY)                  */
/*  Date Completed: 03/18/18                             */
/*  Description:                                         */
/*    Transaction input class - contains transaction id  */
/*    and transaction output                             */
/*  Resources:                                           */
/*    https://bitcoin.org/bitcoin.pdf (Page 2)           */
/*********************************************************/

package com.zyla.coin;

public class TransactionInput {
    public String TransactionOutputID;
    public TransactionOutput UTXO;

    public TransactionInput (String TransactionOutputID) {
        this.TransactionOutputID = TransactionOutputID;
    }
}