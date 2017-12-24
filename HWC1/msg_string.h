//
// Created by simone on 26/11/17.
//

#ifndef HWC1_MSGSTRING_H_H
#define HWC1_MSGSTRING_H_H

#include "msg.h"

msg_t *msg_init_string(void *content);

void msg_destroy_string(msg_t *msg);

msg_t* msg_copy_string (msg_t* msg);

#endif //HWC1_MSGSTRING_H_H
