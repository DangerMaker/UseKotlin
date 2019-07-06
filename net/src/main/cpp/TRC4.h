//
//  TRC4.hpp
//  XFrame
//
//  Created by guangming xu on 2019/4/4.
//  Copyright © 2019 guangguang. All rights reserved.
//

#ifndef TRC4_hpp
#define TRC4_hpp

#include <stdio.h>
#include "rc4.h"

char*  ascii2hex(char* chs,int len);

void TRC4(unsigned char *buf, u_int32_t buflen, const unsigned char *key, u_int32_t keylen);

#endif /* TRC4_hpp */
