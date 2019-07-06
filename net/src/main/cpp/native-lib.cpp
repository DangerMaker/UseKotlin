#include <jni.h>
#include <android/log.h>
#include <string>
#include "Packet_Tools.h"
#include "TRC4.h"
#include "Packet_VerificationCode.h"
#include "Packet_TradeBase.h"
#include "Packet_TradeGate.h"
#include "GenerateKey.h"
#include "cBase64.h"
#include "md5.h"
#include "ZlibDll.h"
#include <arpa/inet.h>
#include <vector>
#include <locale>
#include <codecvt>

#define LOGI(...) \
  ((void)__android_log_print(ANDROID_LOG_INFO, "hello-libs::", __VA_ARGS__))

//////////////////////////////////////////////////////////////////////////
//
//生成交互秘钥使用的函数
static CGenerateKey _generateKey;
static char *_mx = NULL;
static BYTE *rc4_key = new BYTE[16];
static STradeGateUserInfo g_UserInfo;

char * genPublicKey() {
    char *gx = NULL;
    if (_mx) _generateKey.CBN_Free(_mx); _mx = NULL;

    if (!_generateKey.CBN_CreateLocalKey(_mx, gx)) {
        return NULL;
    }
    if (_mx == NULL || gx == NULL) {
        if(_mx)  _generateKey.CBN_Free(_mx); _mx = NULL;
        if(gx) _generateKey.CBN_Free(gx); gx = NULL;
        return NULL;
    }
    return gx;
}


bool genPMD5(char *gy,BYTE* result,int resultlen)
{
    char *key = NULL;
    if(!_generateKey.CBN_CalcKey(gy, _mx, key)) return false;
    if(key == NULL) return false;
    memset(result, 0, 16);
    MD5((BYTE*)key, strlen(key), result);
    if(key) {_generateKey.CBN_Free(key); key = NULL;}
    //
    return true;
}

bool encrypt(STradeBaseHead* pHead,BYTE* body)
{
    BYTE *pBody = body;
    if(pHead->bEncrypt){
        return false;
    }
    if(pBody == NULL) {
        return false;
    }
    if(pHead->dwBodySize<=0)
        return false;
    DWORD dwCheckSum = 0;
    for(DWORD    ii=0; ii<pHead->dwBodySize;    dwCheckSum += (DWORD)((BYTE*) pBody)[ii++]);
    pHead->dwCRC_BeforeEnc    = dwCheckSum;
    pHead->dwCRC_Raw_With_Id  = dwCheckSum + pHead->wPid;
    pHead->bEncrypt    = true;

    TRC4(pBody, pHead->dwBodySize, rc4_key, 16);
    return true;
}

bool decrypt(STradeBaseHead* pHead,BYTE* body)
{
    BYTE *pBody = body;
    if(!pHead->bEncrypt){
        return true;
    }
    if(pBody == NULL) {
        return false;
    }
    if(pHead->dwBodySize<=0)
        return false;
    DWORD dwCheckSum = 0;
    DWORD dwCRC_BeforeEnc = 0;
    //
    DWORD i = 0;
    while(i<pHead->dwBodySize)
    {
        DWORD decodeLen = min(1024,pHead->dwBodySize-i);
        TRC4(pBody+i, decodeLen, rc4_key, 16);
        i+= 1024;
    }
    //
    for(DWORD ii=0; ii<pHead->dwBodySize; dwCheckSum += (DWORD)((BYTE*) pBody)[ii++])
    {
        dwCRC_BeforeEnc    = dwCheckSum;
    }
    //
    if(dwCRC_BeforeEnc != pHead->dwCRC_BeforeEnc)
    {
        return false;
    }
    return true;
}

BYTE* uncompress(STradeBaseHead* pHead,BYTE* body,int& outlength) {
    if(pHead->btCompressFlag == _COMPRESS_NONE){
        return NULL;
    }
    if(pHead->btCompressFlag != _COMPRESS_ZLIB) {
        return NULL;
    }
    BYTE *buffer = new BYTE[pHead->dwRawSize];
    memset(buffer, 0, pHead->dwRawSize);

    long destlen = pHead->dwRawSize;
    int nRet = CZlibDll::UnCompressGZ(body, pHead->dwBodySize, buffer, destlen);
    if(nRet != Z_OK) {
        if(buffer)
        {
            delete [] buffer;
        }
        buffer = NULL;
        return NULL;
    }
    if(destlen != (int)pHead->dwRawSize) {
        //"解压验证失败");
        if(buffer)
        {
            delete [] buffer;
        }
        buffer = NULL;
        return NULL;
    }

    pHead->dwBodySize = pHead->dwRawSize;
    pHead->btCompressFlag = _COMPRESS_NONE;
    outlength = pHead->dwRawSize;
    return buffer;
}


void loadUserInfo(STradeGateUserInfo &info)
{
    strncpy(info.sz_trdpwd, g_UserInfo.sz_trdpwd, sizeof(g_UserInfo.sz_trdpwd));
    strncpy(info.sz_netaddr, g_UserInfo.sz_netaddr, sizeof(g_UserInfo.sz_netaddr));
    strncpy(info.sz_netaddr2, g_UserInfo.sz_netaddr2, sizeof(g_UserInfo.sz_netaddr2));

    if (g_UserInfo.n64_custid != 0) {
        info.n64_custid = g_UserInfo.n64_custid;
        strncpy(info.sz_custorgid, g_UserInfo.sz_orgid, sizeof(g_UserInfo.sz_custorgid));
        strncpy(info.sz_orgid, g_UserInfo.sz_orgid, sizeof(g_UserInfo.sz_orgid));
        strncpy(info.sz_custcert, g_UserInfo.sz_custcert, sizeof(g_UserInfo.sz_custcert));
    }
}


void setLoginUserInfo(__int64 n64_custid,const char* sz_custorgid,const char* sz_orgid,const char* sz_custcert)
{
    g_UserInfo.n64_custid = n64_custid;
    strncpy(g_UserInfo.sz_custorgid, sz_custorgid, strlen(sz_custorgid));
    strncpy(g_UserInfo.sz_orgid, sz_orgid, strlen(sz_orgid));
    strncpy(g_UserInfo.sz_custcert, sz_custcert, strlen(sz_custcert));
}



extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_ez08_trade_net_NativeTools_getVerifyCodePackFromJNI(JNIEnv* env,
                                                             jobject /* this */,
                                                             int width,
                                                             int height
)
{
    DWORD headSize = sizeof(SVerificationCodeHead);
    DWORD bodySize = sizeof(SVerificationCodeBody_ReqPic);
    //DWORD bodySizeA = sizeof(SVerificationCodeBody_ReqPicA);

    SVerificationCodeHead *pHead = (SVerificationCodeHead *)new BYTE[headSize];
    memset(pHead, 0, headSize);
    pHead->wPid = VERIFICATION_CODE_PID__REQ_PIC;
    pHead->dwBodyLen = bodySize;

    SVerificationCodeBody_ReqPic *pBody = (SVerificationCodeBody_ReqPic *)new BYTE[bodySize];
    memset(pBody, 0, bodySize);
    pBody->dwWidth = width;//80;//SCALEDPI(80);
    pBody->dwHeight = height;//SCALEDPI(25);
    //
    int lenall = headSize + bodySize;
    BYTE * buffall = new BYTE[lenall];
    memcpy(buffall,pHead,headSize);
    memcpy(buffall+headSize,pBody,bodySize);
    //
    jbyteArray jbarray = as_byte_array(env,buffall,lenall);
    delete [] buffall; buffall = NULL;
    delete [] pHead; pHead = NULL;
    delete [] pBody; pBody = NULL;
    return jbarray;
}



extern "C" JNIEXPORT jstring JNICALL
Java_com_ez08_trade_net_NativeTools_parseVerifyCodeHeadFromJNI(JNIEnv* env,
                                                               jobject /* this */,
                                                               jbyteArray buffer
)
{
    int outlength;
    unsigned char*  bytebuffer = as_unsigned_char_array(env,buffer,outlength);
    SVerificationCodeHead *pHead = (SVerificationCodeHead *)bytebuffer;
    std::string jsonstr = pHead->toJSON();
    return env->NewStringUTF(jsonstr.c_str());
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_ez08_trade_net_NativeTools_parseVerifyCodeBodyAFromJNI(JNIEnv* env,
                                                                jobject /* this */,
                                                                jbyteArray buffer
)
{
    int outlength;
    unsigned char*  bytebuffer = as_unsigned_char_array(env,buffer,outlength);
    SVerificationCodeBody_ReqPicA *pBody = (SVerificationCodeBody_ReqPicA *)bytebuffer;
    int base64len = (outlength/3+1)*4+1;
    char *basestr = new char[base64len];
    base64_encode((const char *)(bytebuffer+sizeof(SVerificationCodeBody_ReqPicA)),(const long)pBody->dwPicLen,basestr);
    //
    std::string jsonstr = pBody->toJSON(basestr);
    //
    delete[] basestr;
    return env->NewStringUTF(jsonstr.c_str());
}



extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_ez08_trade_net_NativeTools_genTradePacketKeyExchangePackFromJNI(JNIEnv* env,
                                                                         jobject,/* this */
                                                                         jint reqId

)
{
    char *gx = genPublicKey();
    DWORD headSize = sizeof(STradeBaseHead);
    DWORD bodySize = sizeof(STradePacketKeyExchange) + (DWORD)strlen(gx) + 1;

    STradeBaseHead *pHead = (STradeBaseHead *)new BYTE[headSize];
    memset(pHead, 0, headSize);
    pHead->wPid = PID_TRADE_KEY_EXCHANGE;
    pHead->dwBodyLen = bodySize;
    pHead->dwReqId = reqId;//

    STradePacketKeyExchange *pBody = (STradePacketKeyExchange *)new BYTE[bodySize];
    memset(pBody, 0, bodySize);
    pBody->dwIP = 0x80000001;//
    pBody->btUseRC4 = 1;//
    //
    int lenall = headSize + bodySize;
    BYTE * buffall = new BYTE[lenall];
    memcpy(buffall,pHead,headSize);
    memcpy(buffall+headSize,pBody,sizeof(STradePacketKeyExchange));
    memcpy(buffall+headSize+sizeof(STradePacketKeyExchange),gx,strlen(gx));
    //
    jbyteArray jbarray = as_byte_array(env,buffall,lenall);
    delete [] buffall; buffall = NULL;
    delete [] pHead; pHead = NULL;
    delete [] pBody; pBody = NULL;
    return jbarray;
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_ez08_trade_net_NativeTools_parseTradeHeadFromJNI(JNIEnv* env,
                                                          jobject /* this */,
                                                          jbyteArray buffer
)
{
    int outlength;
    unsigned char*  bytebuffer = as_unsigned_char_array(env,buffer,outlength);
    STradeBaseHead *pHead = (STradeBaseHead *)bytebuffer;
    std::string jsonstr = pHead->toJSON();
    return env->NewStringUTF(jsonstr.c_str());
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_ez08_trade_net_NativeTools_parseTradePacketKeyExchangeFromJNI(JNIEnv* env,
                                                                       jobject /* this */,
                                                                       jbyteArray buffer
)
{
    int outlength;
    unsigned char*  bytebuffer = as_unsigned_char_array(env,buffer,outlength);
    STradePacketKeyExchange *pBody = (STradePacketKeyExchange *)bytebuffer;
    //
    int KESize = sizeof(STradePacketKeyExchange);
    DWORD nGXLen = outlength - KESize;
    //
    char *inGX = new char[nGXLen+1];
    memset(inGX, 0, nGXLen+1);
    memcpy(inGX,bytebuffer+KESize,nGXLen);
    //
    genPMD5(inGX,rc4_key,16);
    char *paddress = inet_ntoa(*(in_addr*)(&pBody->dwIP));
    strncpy(g_UserInfo.sz_netaddr, paddress, strlen(paddress));
    const char *netaddr2 = "ZNZ|000EC6CF8DA4|PLEXTOR";
    strncpy(g_UserInfo.sz_netaddr2,netaddr2,strlen(netaddr2));
    //
    std::string jsonstr = pBody->toJSON(inGX);
    delete[] inGX;inGX = NULL;
    return env->NewStringUTF(jsonstr.c_str());
}


extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_ez08_trade_net_NativeTools_genTradeGateLoginPackFromJNI(JNIEnv* env,
                                                                 jobject /* this */,
                                                                 jstring userType,
                                                                 jstring userId,
                                                                 jstring password,
                                                                 jstring checkCode,
                                                                 jstring verifiCodeId,
                                                                 jint reqId
)
{
    jboolean b=true;
    const char *strUserType = env->GetStringUTFChars(userType,&b);
    const char *strUserId = env->GetStringUTFChars(userId, &b);
    const char *strCheckCode = env->GetStringUTFChars(checkCode, &b);
    const char *strVerifiCodeId = env->GetStringUTFChars(verifiCodeId, &b);
    const char *strPassword = env->GetStringUTFChars(password, &b);
    memset(g_UserInfo.sz_trdpwd,0,sizeof(g_UserInfo.sz_trdpwd));
    strncpy(g_UserInfo.sz_trdpwd, strPassword, strlen(strPassword));
    //
    char *gx = genPublicKey();
    DWORD headSize = sizeof(STradeBaseHead);
    DWORD bodySize = sizeof(STradeGateLogin);
    //
    STradeBaseHead *pHead = (STradeBaseHead *)new BYTE[headSize];
    memset(pHead, 0, headSize);
    pHead->wPid = PID_TRADE_GATE__LOGIN;
    pHead->dwBodyLen = pHead->dwRawSize = bodySize;
    pHead->dwReqId = reqId;//

    STradeGateLogin *pBody = (STradeGateLogin *)new BYTE[bodySize];
    memset(pBody, 0, bodySize);
    //
    strcpy(pBody->sz_inputtype, strUserType);
    strcpy(pBody->sz_inputid,   strUserId);
    //
    BYTE * MD5_of_Client = new BYTE[16];
    memset(MD5_of_Client, 0, 16);
    MD5((BYTE*)strUserId, strlen(strUserId), MD5_of_Client);
    //
    memcpy(pBody->btMD5_of_Client, MD5_of_Client,16);
    strcpy(pBody->szVerificationCode,   strCheckCode);
    strcpy(pBody->szVerificationId,    strVerifiCodeId);
    loadUserInfo(pBody->userinfo);
    //
    bool bencresult = encrypt(pHead,(BYTE*)(pBody));
    if(bencresult == false)
    {
        return NULL;
    }
    //
    int lenall = headSize + bodySize;
    BYTE * buffall = new BYTE[lenall];
    memcpy(buffall,pHead,headSize);
    memcpy(buffall+headSize,pBody,sizeof(STradeGateLogin));
    //
    jbyteArray jbarray = as_byte_array(env,buffall,lenall);
    delete [] buffall; buffall = NULL;
    delete [] pHead; pHead = NULL;
    delete [] pBody; pBody = NULL;
    return jbarray;
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_ez08_trade_net_NativeTools_parseTradeGateLoginFromJNI(JNIEnv* env,
                                                               jobject /* this */,
                                                               jbyteArray headbuffer,
                                                               jbyteArray bodybuffer
)
{
    int headlength;
    unsigned char*  byteheadbuffer = as_unsigned_char_array(env,headbuffer,headlength);
    STradeBaseHead *pHead = (STradeBaseHead *)byteheadbuffer;
    int outlength;
    unsigned char*  bytebuffer = as_unsigned_char_array(env,bodybuffer,outlength);
    STradeGateLoginA *pBody = (STradeGateLoginA *)bytebuffer;
    //
    decrypt(pHead,bytebuffer);
    //
    BYTE *pNewBody = uncompress(pHead,bytebuffer,outlength);
    //
    bool bNeedDelete = false;
    if(pNewBody != NULL)
    {
        bNeedDelete = true;
    }
    else
    {
        pNewBody = bytebuffer;
    }
    //
    pBody = (STradeGateLoginA *)pNewBody;
    std::string jsonstr = "";
    //
    if(!pBody->bLoginSucc){
        //char* strErrMsg = new char[pBody->dwLen*2];
        //code_convert("gb2312","utf-8",pNewBody+sizeof(STradeGateLoginA),pBody->dwLen,strErrMsg,pBody->dwLen*2);
        char* strErrMsg = new char[pBody->dwLen];
        memcpy(strErrMsg,pNewBody+sizeof(STradeGateLoginA),pBody->dwLen);
        jsonstr = pBody->toJSON(env,strErrMsg,NULL);
    }
    else if(pBody->dwCount>1000) {
        const char* strErrMsg = "the package length error!";
        jsonstr = pBody->toJSON(env,strErrMsg,NULL);
    }
    else if(outlength !=(sizeof(STradeGateLoginA) + pBody->dwCount * sizeof(STradeGateLoginAItem)) ) {
        const char* strErrMsg = "the package length error!";
        jsonstr = pBody->toJSON(env,strErrMsg,NULL);
    }
    else
    {
        STradeGateLoginAItem lists[pBody->dwCount];
        for(int i=0;i<pBody->dwCount;i++)
        {
            memcpy((BYTE*)&lists[i],pNewBody + sizeof(STradeGateLoginA)+i*sizeof(STradeGateLoginAItem),sizeof(STradeGateLoginAItem));
        }
        //
        const char* strErrMsg = "success";
        jsonstr = pBody->toJSON(env,strErrMsg,lists);
        //
        if(pBody->dwCount>0)
        {
            setLoginUserInfo(lists[0].n64_custid,lists[0].sz_orgid,lists[0].sz_orgid,lists[0].sz_custcert);
        }
    }
    //

    //
    if(bNeedDelete)
    {
        delete[] pNewBody;
        pNewBody = NULL;
    }
    return env->NewStringUTF(jsonstr.c_str());
    //
}


/*------------心跳包---------------*/

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_ez08_trade_net_NativeTools_genTradeHeartBeatFromJNI(JNIEnv* env,
                                                             jobject /* this */
)
{
    DWORD headSize = sizeof(STradeBaseHead);
    DWORD bodySize = sizeof(STradeCommOK);

    STradeBaseHead *pHead = (STradeBaseHead *)new BYTE[headSize];
    memset(pHead, 0, headSize);
    pHead->wPid = PID_TRADE_COMM_OK;
    pHead->dwBodyLen = pHead->dwRawSize = bodySize;
    pHead->dwReqId = 0x81;//
    pHead->btCompressFlag =_COMPRESS_NONE;
    pHead->bEncrypt        = 0;
    pHead->dwCRC_BeforeEnc = 0;
    //
    STradeCommOK *pBody = (STradeCommOK *)new BYTE[bodySize];
    memset(pBody, 0, bodySize);
    //
    int lenall = headSize + bodySize;
    BYTE * buffall = new BYTE[lenall];
    memcpy(buffall,pHead,headSize);
    memcpy(buffall+headSize,pBody,sizeof(STradeCommOK));
    //
    jbyteArray jbarray = as_byte_array(env,buffall,lenall);
    delete [] buffall; buffall = NULL;
    delete [] pHead; pHead = NULL;
    delete [] pBody; pBody = NULL;
    return jbarray;
}

/*------------通用业务包---------------*/

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_ez08_trade_net_NativeTools_genTradeGateBizFunFromJNI(JNIEnv* env,
                                                              jobject /* this */,
                                                              jbyteArray content,
                                                              jint reqId
)
{
    int outlength=0;
    const BYTE *contentbuffer =  as_unsigned_char_array(env,content,outlength);
    //
    DWORD headSize = sizeof(STradeBaseHead);
    DWORD bodySize = sizeof(STradeGateBizFun) + outlength + 1;
    //
    STradeBaseHead *pHead = (STradeBaseHead *)new BYTE[headSize];
    memset(pHead, 0, headSize);
    pHead->wPid = PID_TRADE_GATE__BIZFUN;
    pHead->dwBodyLen = pHead->dwRawSize = bodySize;
    pHead->dwReqId = reqId;//

    STradeGateBizFun *pBody = (STradeGateBizFun *)new BYTE[bodySize];
    memset(pBody, 0, bodySize);
    pBody->dwContentLen = outlength + 1;
    memcpy(pBody+1, contentbuffer, outlength + 1);
    //
    loadUserInfo(pBody->userinfo);
    //
    bool bencresult = encrypt(pHead,(BYTE*)(pBody));
    if(bencresult == false)
    {
        return NULL;
    }
    //
    int lenall = headSize + bodySize;
    BYTE * buffall = new BYTE[lenall];
    memcpy(buffall,pHead,headSize);
    memcpy(buffall+headSize,pBody,bodySize);
    //
    jbyteArray jbarray = as_byte_array(env,buffall,lenall);
    delete [] buffall; buffall = NULL;
    delete [] pHead; pHead = NULL;
    delete [] pBody; pBody = NULL;
    return jbarray;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ez08_trade_net_NativeTools_parseTradeGateBizFunFromJNI(JNIEnv* env,
                                                                jobject /* this */,
                                                                jbyteArray headbuffer,
                                                                jbyteArray bodybuffer
)
{
    int headlength;
    unsigned char*  byteheadbuffer = as_unsigned_char_array(env,headbuffer,headlength);
    STradeBaseHead *pHead = (STradeBaseHead *)byteheadbuffer;
    int outlength;
    unsigned char*  bytebuffer = as_unsigned_char_array(env,bodybuffer,outlength);
    STradeGateBizFunA *pBody = (STradeGateBizFunA *)bytebuffer;
    //
    decrypt(pHead,bytebuffer);
    //
    BYTE *pNewBody = uncompress(pHead,bytebuffer,outlength);
    //
    bool bNeedDelete = false;
    if(pNewBody != NULL)
    {
        bNeedDelete = true;
    }
    else
    {
        pNewBody = bytebuffer;
    }
    //
    pBody = (STradeGateBizFunA *)pNewBody;
    //
    char* content = new char[pBody->dwContentLen];
    memcpy(content,pNewBody+sizeof(STradeGateBizFunA),pBody->dwContentLen);
    std::string jsonstr = pBody->toJSON(env,content);
    delete[] content;
    content = NULL;
    //
    if(bNeedDelete)
    {
        delete[] pNewBody;
        pNewBody = NULL;
    }
    return env->NewStringUTF(jsonstr.c_str());
    //
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ez08_trade_net_NativeTools_parseTradeGateErrorFromJNI(JNIEnv* env,
                                                               jobject /* this */,
                                                               jbyteArray headbuffer,
                                                               jbyteArray bodybuffer
)
{
    int headlength;
    unsigned char*  byteheadbuffer = as_unsigned_char_array(env,headbuffer,headlength);
    STradeBaseHead *pHead = (STradeBaseHead *)byteheadbuffer;
    int outlength;
    unsigned char*  bytebuffer = as_unsigned_char_array(env,bodybuffer,outlength);
    STradeGateError *pBody = (STradeGateError *)bytebuffer;
    //
    decrypt(pHead,bytebuffer);
    //
    BYTE *pNewBody = uncompress(pHead,bytebuffer,outlength);
    //
    bool bNeedDelete = false;
    if(pNewBody != NULL)
    {
        bNeedDelete = true;
    }
    else
    {
        pNewBody = bytebuffer;
    }
    //
    pBody = (STradeGateError *)pNewBody;
    //
    char* content = new char[pHead->dwRawSize - sizeof(STradeGateError)];
    memcpy(content,pNewBody+sizeof(STradeGateError),pHead->dwRawSize - sizeof(STradeGateError));
    std::string jsonstr = pBody->toJSON(env,content);
    delete[] content;
    content = NULL;
    //
    if(bNeedDelete)
    {
        delete[] pNewBody;
        pNewBody = NULL;
    }
    return env->NewStringUTF(jsonstr.c_str());
    //
}

/*------------行情查询---------------*/

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_ez08_trade_net_NativeTools_genTradeHQQueryFromJNI(JNIEnv* env,
                                                           jobject /* this */,
                                                           jstring market,
                                                           jstring secucode,
                                                           jint reqId
)
{
    jboolean b=true;
    const char *strMarket = env->GetStringUTFChars(market,&b);
    const char *strSecucode = env->GetStringUTFChars(secucode, &b);
    //
    DWORD headSize = sizeof(STradeBaseHead);
    DWORD bodySize = sizeof(STradeHQQuery);
    //
    STradeBaseHead *pHead = (STradeBaseHead *)new BYTE[headSize];
    memset(pHead, 0, headSize);
    pHead->wPid = PID_TRADE_HQ_QUERY;
    pHead->dwBodyLen = pHead->dwRawSize = bodySize;
    pHead->dwReqId = reqId;//

    STradeHQQuery *pBody = (STradeHQQuery *)new BYTE[bodySize];
    memset(pBody, 0, bodySize);
    memcpy(&pBody->idMarket,(BYTE*)strMarket,strlen(strMarket));
    strncpy(pBody->szCode, strSecucode, min(strlen(strSecucode),sizeof(pBody->szCode)));
    //
    bool bencresult = encrypt(pHead,(BYTE*)(pBody));
    if(bencresult == false)
    {
        return NULL;
    }
    //
    int lenall = headSize + bodySize;
    BYTE * buffall = new BYTE[lenall];
    memcpy(buffall,pHead,headSize);
    memcpy(buffall+headSize,pBody,bodySize);
    //
    jbyteArray jbarray = as_byte_array(env,buffall,lenall);
    delete [] buffall; buffall = NULL;
    delete [] pHead; pHead = NULL;
    delete [] pBody; pBody = NULL;
    return jbarray;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_ez08_trade_net_NativeTools_parseTradeHQQueryFromJNI(JNIEnv* env,
                                                             jobject /* this */,
                                                             jbyteArray headbuffer,
                                                             jbyteArray bodybuffer
)
{
    int headlength;
    unsigned char*  byteheadbuffer = as_unsigned_char_array(env,headbuffer,headlength);
    STradeBaseHead *pHead = (STradeBaseHead *)byteheadbuffer;
    int outlength;
    unsigned char*  bytebuffer = as_unsigned_char_array(env,bodybuffer,outlength);
    STradeHQQueryA *pBody = (STradeHQQueryA *)bytebuffer;
    //
    decrypt(pHead,bytebuffer);
    //
    BYTE *pNewBody = uncompress(pHead,bytebuffer,outlength);
    //
    bool bNeedDelete = false;
    if(pNewBody != NULL)
    {
        bNeedDelete = true;
    }
    else
    {
        pNewBody = bytebuffer;
    }
    //
    pBody = (STradeHQQueryA *)pNewBody;
    //
    std::string jsonstr = pBody->toJSON();
    //
    if(bNeedDelete)
    {
        delete[] pNewBody;
        pNewBody = NULL;
    }
    return env->NewStringUTF(jsonstr.c_str());
    //
}