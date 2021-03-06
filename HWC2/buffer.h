//
// Created by simone on 23/11/17.
//

#ifndef HWC1_BUFFER_H
#define HWC1_BUFFER_H

#include "msg.h"
#include <pthread.h>
#include <semaphore.h>
#include <stdlib.h>

#define BUFFER_ERROR (msg_t *) NULL

typedef struct buffer {
    //indici di estrazine e inserimento
    unsigned int extract_index;
    unsigned int insertion_index;

    //stile competitivo mutua esclusione
    pthread_mutex_t use_extract_index;
    pthread_mutex_t use_insertion_index;

    // cooperazione rispettivamente, numero di spazi occupati da messaggi e numero spazi liberi
    sem_t full_num;
    sem_t empty_num;

    msg_t **content;

    unsigned int maxsize;

}buffer_t;

/* allocazione / deallocazione buffer */
// creazione di un buffer vuoto di dim. max nota
buffer_t* buffer_init(unsigned int maxsize);
// deallocazione di un buffer
void buffer_destroy(buffer_t* buffer);
/* operazioni sul buffer */
// inserimento bloccante: sospende se pieno, quindi
// effettua l’inserimento non appena si libera dello spazio
// restituisce il messaggio inserito; N.B.: msg!=null
msg_t* put_bloccante(buffer_t* buffer, msg_t* msg);
// inserimento non bloccante: restituisce BUFFER_ERROR se pieno,
// altrimenti effettua l’inserimento e restituisce il messaggio
// inserito; N.B.: msg!=null
msg_t* put_non_bloccante(buffer_t* buffer, msg_t* msg);
// estrazione bloccante: sospende se vuoto, quindi
// restituisce il valore estratto non appena disponibile
msg_t* get_bloccante(buffer_t* buffer);
// estrazione non bloccante: restituisce BUFFER_ERROR se vuoto
// ed il valore estratto in caso contrario
msg_t* get_non_bloccante(buffer_t* buffer);


#endif //HWC1_BUFFER_H
