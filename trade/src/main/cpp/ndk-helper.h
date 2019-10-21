//
// Created by 刘小鱼 on 2019-09-27.
//

#include <jni.h>
#include <string>
#include "zlib.h"

#define BYTE            unsigned char
#define LPBYTE          unsigned char *
#define __int64         long long
#define WORD            u_int16_t
#define DWORD           u_int32_t
#define INT             int32_t
#define UINT            uint32_t
#define BOOLEAN            int32_t

unsigned char* jByteArray2UnsignedChar(JNIEnv *env, jbyteArray array,int &outlength);

jbyteArray unsignedChar2JByteArray(JNIEnv *env, unsigned char* buf, int len);

BYTE* unPress(DWORD dwBodySize,DWORD dwRawSize,BYTE* body,int& outlength);

bool decrypt(BYTE *key,DWORD dwEncRawSize, BYTE *body);