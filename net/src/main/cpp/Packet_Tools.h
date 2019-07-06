//
// Created by ntguser on 2019/5/7.
//

#ifndef YCTRADELIB_PACKET_TOOLS_H
#define YCTRADELIB_PACKET_TOOLS_H



#define max(a,b) (((a) > (b)) ? (a) : (b))
#define min(a,b) (((a) < (b)) ? (a) : (b))

#define _COMPRESS_NONE 0
#define _COMPRESS_ZLIB 2

//
extern unsigned char* as_unsigned_char_array(JNIEnv *env, jbyteArray array,int &outlength);
extern jbyteArray as_byte_array(JNIEnv *env, unsigned char* buf, int len);
extern const char * NewCodedString(JNIEnv *env, const char *chars, const char * charCode,jint len);


#endif //YCTRADELIB_PACKET_TOOLS_H
