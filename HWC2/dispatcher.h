//
// Created by simone on 07/12/17.
//

#ifndef HWC2_DISPATCHER_H
#define HWC2_DISPATCHER_H

#include "HWC1/msg.h"
#include "HWC1/buffer.h"
#include "hwc2list/list.h"

typedef struct dispatcher_parameters{
    list_t *list_of_readers;
    buffer_t *buffer_provider_to_dispatcher;
    pthread_t dispatcher;
};

struct dispatcher_parameters *dispatcher_init(int,list_t*);

#endif //HWC2_DISPATCHER_H
