//
// Created by simone on 23/11/17.
//

#include <stdio.h>
#include "buffer.h"

buffer_t* buffer_init(unsigned int maxsize){
    buffer_t *buffer = malloc(sizeof(buffer_t));

    buffer->maxsize = maxsize;
    //the indices start from 0
    buffer->extract_index = 0;
    buffer->insertion_index = 0;
    //init mutex => verde
    pthread_mutex_init(&buffer->use_extract_index,NULL);
    pthread_mutex_init(&buffer->use_insertion_index,NULL);
    //init semaphore
    sem_init(&buffer->full_num,0,0);
    sem_init(&buffer->empty_num,0,maxsize);

    buffer->content = malloc(sizeof(msg_t *) * maxsize);

    return buffer;
}

void buffer_destroy(buffer_t* buffer){

    //destroy mutex
    pthread_mutex_destroy(&buffer->use_insertion_index);
    pthread_mutex_destroy(&buffer->use_extract_index);
    //destroy semaphore
    sem_destroy(&buffer->full_num);
    sem_destroy(&buffer->empty_num);
    //free of the message array
    free(buffer->content);
    //free of the empty buffer
    free(buffer);
}

msg_t * put_bloccante(buffer_t * buffer, msg_t * msg){
    if(buffer == NULL) return BUFFER_ERROR;

    //p(vuote)
    sem_wait(&buffer->empty_num);
        //p(uso_d)
        pthread_mutex_lock(&buffer->use_insertion_index);
            //BUFFER[D] <- M
            buffer->content[buffer->insertion_index % buffer->maxsize] = msg;
            //D<-(D+1) mod N;
            buffer->insertion_index = buffer->insertion_index + 1;
        //v(uso_d)
        pthread_mutex_unlock(&buffer->use_insertion_index);
    //v(piene)
    sem_post(&buffer->full_num);

    return msg;
}

msg_t * get_bloccante(buffer_t * buffer){
    msg_t* messaggio = NULL;
    //p(piene)
    sem_wait(&buffer->full_num);
        //p(uso_t)
        pthread_mutex_lock(&buffer->use_extract_index);
            //M <- BUFFER[T]
            messaggio = buffer->content[buffer->extract_index % buffer->maxsize];
            //T<-(T+1) mod N;
            buffer->extract_index = buffer->extract_index + 1;
        //v(uso_t)
        pthread_mutex_unlock(&buffer-> use_extract_index);
    //v(vuote)
    sem_post(&buffer->empty_num);

    return messaggio;
}

msg_t * put_non_bloccante(buffer_t * buffer, msg_t * msg){
    if(sem_trywait(&buffer->empty_num) == -1)
        return BUFFER_ERROR;
    else
    {
            pthread_mutex_lock(&buffer->use_insertion_index);
                buffer->content[buffer->insertion_index % buffer->maxsize] = msg;
                buffer->insertion_index = buffer->insertion_index + 1;
            pthread_mutex_unlock(&buffer->use_insertion_index);
        sem_post(&buffer->full_num);
        return msg;
    }
}

msg_t * get_non_bloccante(buffer_t * buffer){
    if(sem_trywait(&buffer->full_num) == -1)
        return BUFFER_ERROR;
    else
    {
        msg_t* messaggio = NULL;
            pthread_mutex_lock(&buffer->use_extract_index);
                messaggio = buffer->content[buffer->extract_index % buffer->maxsize];
                buffer->extract_index = buffer->extract_index + 1;
            pthread_mutex_unlock(&buffer-> use_extract_index);
        sem_post(&buffer->empty_num);
        return messaggio;
    }
}