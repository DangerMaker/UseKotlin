//
// Created by ntguser on 2019/4/28.
//

#ifndef YCTRADELIB_CBASE64_H
#define YCTRADELIB_CBASE64_H

extern char * base64_encode(const char *buf, const long size, char *base64Char);
extern char * base64_decode(const char *base64Char, const long base64CharSize, char *originChar, long originCharSize);

#endif //YCTRADELIB_CBASE64_H
