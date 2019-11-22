//
// Created by 刘小鱼 on 2019-09-27.
//

#include "ndk-helper.h"
#include "ZlibDll.h"
#include "aes.h"


unsigned char *jByteArray2UnsignedChar(JNIEnv *env, jbyteArray array, int &outlength) {
    outlength = env->GetArrayLength(array);
    unsigned char *buf = new unsigned char[outlength];
    env->GetByteArrayRegion(array, 0, outlength, reinterpret_cast<jbyte *>(buf));
    return buf;
}

char *jByteArray2Char(JNIEnv *env, jbyteArray array) {
    char *chars = NULL;
    jbyte *bytes = env->GetByteArrayElements(array, 0);
    int chars_len = env->GetArrayLength(array);
    chars = new char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, bytes, chars_len);
    chars[chars_len] = 0;
    env->ReleaseByteArrayElements(array, bytes, 0);
    return chars;
}


jbyteArray unsignedChar2JByteArray(JNIEnv *env, unsigned char *buf, int len) {
    jbyteArray array = env->NewByteArray(len);
    env->SetByteArrayRegion(array, 0, len, reinterpret_cast<jbyte *>(buf));
    return array;
}


BYTE *unPress(DWORD dwBodySize, DWORD dwRawSize, BYTE *body, int &outlength) {
    BYTE *buffer = new BYTE[dwRawSize];
    memset(buffer, 0, dwRawSize);

    long destlen = dwRawSize;
    int nRet = CZlibDll::UnCompressGZ(body, dwBodySize, buffer, destlen);
    if (nRet != Z_OK) {
        delete[] buffer;
        buffer = NULL;
        return buffer;
    }
    outlength = dwRawSize;
    return buffer;
}

bool decrypt(BYTE *key, DWORD dwEncRawSize, BYTE *body) {
    BYTE *pBody = body;
    AES_KEY aeskey;
    AES_set_decrypt_key(key, 128, &aeskey);
    unsigned char ivec[16];
    memset(ivec, 0xcc, sizeof(ivec));
    AES_cbc128_decrypt((BYTE *) pBody, pBody, dwEncRawSize, &aeskey, ivec);
    return true;
}