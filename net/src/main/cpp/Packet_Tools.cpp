//
// Created by ntguser on 2019/5/7.
//

#include <jni.h>
#include "Packet_Tools.h"


//////////////////////////////////////////////////////////////////////////
const char * NewCodedString(JNIEnv *env, const char *chars, const char * charCode,jint len)
{
    jclass stringClass;
    jmethodID cid;
    jbyteArray elemArr;
    jstring result;
    jstring jencoding;
    stringClass = env->FindClass("java/lang/String");
    if (stringClass == NULL) {
    return NULL; /* exception thrown */     }
    /* Get the method ID for the String(byte[] data, String charsetName) constructor */
    cid = env->GetMethodID(stringClass,"<init>", "([BLjava/lang/String;)V");
    if (cid == NULL) {
    return NULL;
    /* exception thrown */
    }
    jencoding = env->NewStringUTF(charCode);
    /* Create a byte[] that holds the string characters */
    elemArr = env->NewByteArray(len);
    if (elemArr == NULL) {
    return NULL; /* exception thrown */
    }
    env->SetByteArrayRegion(elemArr, 0, len, (jbyte*)chars);
    /* Construct a java.lang.String object */
    result = (jstring)(env->NewObject(stringClass, cid, elemArr, jencoding));
    /* Free local references */     env->DeleteLocalRef(elemArr);
    env->DeleteLocalRef(stringClass);
    jboolean b=true;
    const char *aa = env->GetStringUTFChars(result,&b);
    return aa;
}

unsigned char* as_unsigned_char_array(JNIEnv *env, jbyteArray array,int &outlength)
{
    outlength = env->GetArrayLength (array);
    unsigned char* buf = new unsigned char[outlength];
    env->GetByteArrayRegion(array, 0, outlength, reinterpret_cast<jbyte*>(buf));

    return buf;
}


jbyteArray as_byte_array(JNIEnv *env, unsigned char* buf, int len)
{
    jbyteArray array = env->NewByteArray(len);
    env->SetByteArrayRegion(array, 0, len, reinterpret_cast<jbyte*>(buf));

    return array;
}


