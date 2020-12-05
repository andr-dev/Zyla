package com.zyla.network.old;

import java.nio.ByteBuffer;
import com.zyla.coin.Util;

public class ZylaProtocol {
    private final String header = "ZylaProtocol";
    private final byte dataType; // 0 = block
    private long seqNum;
    private int dataLength;
    private BlockData data;
    private String hash;
    private final String footer = "ProtoEnd";

    public ZylaProtocol (byte dataType) {
        this.dataType = dataType;
    }

    public void setSeqNum (int seqNum) {
        this.seqNum = seqNum;
    }

    public void setData (BlockData data) {
        this.dataLength = data.getSize();
        this.data = data;
    }

    public void calcHash () {
        this.hash = Util.applySHA256("" + this.seqNum + this.dataType + this.dataLength + this.data.convertToByteArray());
    }

    public ZylaProtocol (int seqNum, byte dataType, BlockData data) {
        this.seqNum = seqNum;
        this.dataType = dataType;
        this.dataLength = data.getSize();
        this.data = data;
        this.hash = Util.applySHA256("" + this.seqNum + this.dataType + this.dataLength + this.data.convertToByteArray());

        // DEBUG
        Util.printMessage( "Header          : " + this.header
                + "|Sequence Number : " + this.seqNum
                + "|Data Type       : " + this.dataType
                + "|Data Length     : " + this.dataLength
                + "|Data            : " + this.data.convertToByteArray()
                + "|SHA256          : " + this.hash
                + "|Footer          : " + this.footer);

    }

    public int getSize () {
        return header.getBytes().length + Byte.BYTES + Long.BYTES + Integer.BYTES + dataLength + hash.getBytes().length + footer.getBytes().length;
    }

    public byte[] toByteArray () {
        byte[] out = new byte[header.getBytes().length + Byte.BYTES + Long.BYTES + Integer.BYTES + dataLength + hash.getBytes().length + footer.getBytes().length];
        byte[] str = new byte[out.length];
        int buf = 0;

        Util.printMessage( "Zyla Protocal Out Length : " + out.length
                + "|Header                   : " + header.getBytes().length
                + "|Data Type                : " + Byte.BYTES
                + "|Sequence Number          : " + Long.BYTES
                + "|Data Length              : " + Integer.BYTES
                + "|Data                     : " + data.getSize()
                + "|SHA256                   : " + hash.getBytes().length
                + "|Footer                   : " + footer.getBytes().length);

        Util.printMessage("ZylaProtocal toByteArray Size : " + out.length)	;


        str = header.getBytes();
        System.arraycopy(str, 0, out, buf, str.length);
        buf += str.length;

        str = new byte[] {dataType};
        System.arraycopy(str, 0, out, buf, str.length);
        buf += str.length;

        str = Util.longToBytes(seqNum);
        System.arraycopy(str, 0, out, buf, str.length);
        buf += str.length;

        str = ByteBuffer.allocate(Integer.BYTES).putInt(dataLength).array();
        System.arraycopy(str, 0, out, buf, str.length);
        buf += str.length;

        str = data.convertToByteArray();
        System.arraycopy(str, 0, out, buf, str.length);
        buf += str.length;

        str = hash.getBytes();
        System.arraycopy(str, 0, out, buf, str.length);
        buf += str.length;

        str = footer.getBytes();
        System.arraycopy(str, 0, out, buf, str.length);
        buf += str.length;

        return out;
    }
}
