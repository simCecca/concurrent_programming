//
// Created by simone on 07/12/17.
//

#ifndef HWC2_PROVIDER_H
#define HWC2_PROVIDER_H

#include "HWC1/buffer.h"

typedef struct provider_parameters{
    unsigned int how_many_messages;
    buffer_t *buffer_of_message;
};

void *send_a_finished_sequence_of_messages_to_the_dispatcher(void *arg);

#endif //HWC2_PROVIDER_H
