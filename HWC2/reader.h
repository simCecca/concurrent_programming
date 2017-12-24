//
// Created by simone on 15/12/17.
//

#ifndef HWC2_READER_H
#define HWC2_READER_H

#include "HWC1/buffer.h"

typedef struct{
    char *name;
    buffer_t *reader_buffer;
    pthread_t reader;
}reader_t;

reader_t *init_reader(unsigned int,char*);
void *read_message(void *);
void destroy_reader(reader_t *);

#endif //HWC2_READER_H
