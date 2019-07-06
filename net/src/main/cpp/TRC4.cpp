//
//  TRC4.cpp
//  XFrame
//
//  Created by guangming xu on 2019/4/4.
//  Copyright © 2019 guangguang. All rights reserved.
//

#include <jni.h>
#include <string.h>
#include "TRC4.h"

char*  ascii2hex(char* chs,int len)
{
    //用于接收到的串转换成要用的十六进制串返回主窗口调用
    char hex[16] = {'0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'A', 'B', 'C', 'D', 'E', 'F'};

    char *ascii = new char[len * 3 + 1];// calloc ascii

    int i = 0;
    while( i < len )
    {
        int b= chs[i] & 0x000000ff;
        ascii[i*2] = hex[b/16] ;
        ascii[i*2+1] = hex[b%16] ;
        ++i;
    }
    return ascii;                    // ascii 返回之前未释放
}

void TRC4(unsigned char *buf, u_int32_t buflen, const unsigned char *key, u_int32_t keylen) {
    RC4_KEY rc4key;
    RC4_set_key(&rc4key, keylen, key);
    unsigned char *buffer = new unsigned char[buflen];
    RC4(&rc4key, buflen, buf ,buffer);
    memcpy(buf, buffer, buflen);
    delete [] buffer;
    buffer = NULL;
}
