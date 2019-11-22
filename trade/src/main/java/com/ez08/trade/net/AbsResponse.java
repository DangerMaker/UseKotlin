package com.ez08.trade.net;

import android.util.Log;
import com.ez08.trade.net.head.STradeBaseHead;
import com.xuhao.didi.core.utils.BytesUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AbsResponse {

    protected byte[] body;
    public int wPid;

    public AbsResponse(byte[] head, byte[] originBody, byte[] aesKey) {
        ByteBuffer headBuffer = ByteBuffer.wrap(head);
        headBuffer.order(ByteOrder.LITTLE_ENDIAN);
        STradeBaseHead sTradeBaseHead = new STradeBaseHead(headBuffer);

        byte[] encryptBody;
        if (sTradeBaseHead.bEncrypt == 1) {
            //解密
            encryptBody = OpensslHelper.decrypt(aesKey, originBody, sTradeBaseHead.dwEncRawSize);
        } else {
            encryptBody = originBody;
        }

        if(sTradeBaseHead.wPid == 2010){
            Log.e("encryptBody",BytesUtils.toHexStringForLog(encryptBody));
        }


        if (sTradeBaseHead.btCompressFlag == 2) {
//            //解压
            body = OpensslHelper.unPress(encryptBody.length, sTradeBaseHead.dwRawSize, encryptBody);
        } else {
            body = encryptBody;
        }

        wPid = sTradeBaseHead.wPid;

    }

    protected int sizeof(Object type) {
        return NetUtil.sizeOf(type);
    }

}
