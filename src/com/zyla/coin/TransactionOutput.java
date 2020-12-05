/***********************************************/
/*  Name:           Andre Benedito             */
/*  Date Created:   03/17/18 (MM/DD/YY)        */
/*  Date Completed: 03/20/18                   */
/*  Description:                               */
/*    Transaction output class                 */
/*  Resources:                                 */
/*    https://bitcoin.org/bitcoin.pdf (Page 2) */
/***********************************************/

package com.zyla.coin;

import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey owner;
    public double value;
    public String ParentTransactionID;

    public TransactionOutput(PublicKey owner, double value, String ParentTransactionID) {
        this.id = Util.applySHA256(Util.getStringFromKey(owner) + Double.toString(value) + ParentTransactionID);
        this.owner = owner;
        this.value = value;
        this.ParentTransactionID = ParentTransactionID;
    }

    public boolean pertainsTo (PublicKey publicKey) {
        return (this.owner == publicKey);
    }
}
