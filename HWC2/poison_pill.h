//
// Created by simone on 07/12/17.
//

#ifndef HWC2_POISON_PILL_H
#define HWC2_POISON_PILL_H

#include "HWC1/msg.h"

#define POISON_PILL ( (msg_t*)&POISON_PILL_MSG )

msg_t* msg_init_pill(void *);
msg_t* msg_copy_pill(msg_t *);
void msg_destroy_pill(msg_t *);

extern const msg_t POISON_PILL_MSG;

#endif //HWC2_POISON_PILL_H
