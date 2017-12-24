//
// Created by simone on 15/12/17.
//

#include <zconf.h>
#include "reader.h"
#include "poison_pill.h"
#include "HWC1/msg_string.h"
#include <stdio.h>

/*funzione standard per il consumo di un messaggio da parte di un reader, sicuramente è possibile una
 * versione dove si vuole diversificare il modo di consumare i messaggi quindi prevedere in reader_t un
 * puntatore a funzione*/
void default_consumption_of_message(msg_t * current_message,reader_t *reader){
    sleep((unsigned int) (rand() % 3));
    /*può essere previsto anche una stampa*/
    printf("messaggio %s dal reader %s \n",(char *) current_message->content,reader->name);
}
reader_t *init_reader(unsigned int size,char *name){
    reader_t *reader = malloc(sizeof(reader_t));
    reader->name = name;
    reader->reader_buffer = buffer_init(size);
    pthread_create(&reader->reader,NULL,read_message,reader);
    return reader;
}

void destroy_reader(reader_t *reader){
    //se rimosso dalla lista
    if(1){
        buffer_destroy(reader->reader_buffer);
        free(reader);
    }
}

void *read_message(void *args){
    reader_t *reader = (reader_t *) args;
    /*prendo il messaggio da consumare*/
    msg_t *current_message = get_bloccante(reader->reader_buffer);
    while(current_message != POISON_PILL){
        /*consumo del messaggio, */
        default_consumption_of_message(current_message,reader);
        /*fine consumo del messaggio*/
        msg_destroy_string(current_message);
        current_message = get_bloccante(reader->reader_buffer);
    }
    destroy_reader(reader);
    pthread_exit(NULL);
}