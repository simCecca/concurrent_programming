//
// Created by simone on 07/12/17.
//
/*dispatcher, ovvero un flusso di esecuzione il cui compito è quello
di raccogliere una sequenza finita di messaggi provenienti da un altro flusso
provider e smistarne celermente delle sue copie verso dei flussi reader*/

/*il dispatcher inoltra tutti i messaggi ricevuti dal provider verso tutti
i flussi reader correntemente accettati ed organizzati in una Lista dei
reader*/

#include <string.h>
#include <stdio.h>
#include "dispatcher.h"
#include "poison_pill.h"
#include "HWC1/msg_string.h"
#include "hwc2list/list.h"
#include "reader.h"
int is_slow(buffer_t *buffer){
    printf("%d %d\n",buffer->insertion_index - buffer->extract_index, buffer->maxsize-1);
    return (buffer->insertion_index - buffer->extract_index) == buffer->maxsize;
}

void send_message_to_reader(msg_t *message,list_t *list){
    iterator_t *iterator = iterator_init(list);
    for(int i = 0; i<size(list); i++){
        /*invia il messaggio corrente a tutti i reader nella lista corrente*/
        msg_t *message_copy = msg_copy_string(message);
        reader_t *current_reader = (reader_t *) next(iterator);
        /*se il buffer del reader corrente è pieno allora vuol dire che è lento e gli invio
         * la poison pill*/
        if(is_slow(current_reader->reader_buffer)) {
            put_non_bloccante(current_reader->reader_buffer, POISON_PILL);
            printf("te scoppio\n");
        }
        else
            /*il dispatcher inserisce il messaggio nel buffer del reader corrente*/
            put_non_bloccante(current_reader->reader_buffer,message_copy);
    }

}

void *from_provider_to_readers(void *param){

    struct dispatcher_parameters *paramteter = (struct dispatcher_parameters *) param;

    buffer_t *dispatcher_buffer = paramteter->buffer_provider_to_dispatcher;

    while(1)
    {
        msg_t *current_message = get_bloccante(dispatcher_buffer);
        if( current_message != POISON_PILL){ send_message_to_reader(current_message,paramteter->list_of_readers);  }
        else
        {
            send_message_to_reader(msg_copy_pill(current_message),paramteter->list_of_readers);
            buffer_destroy(dispatcher_buffer);
            return 0;
        }
    }
}
/*è del dispatcher la responsabilità di creare e distruggere il buffer anche perchè è lui che sa
 * quando distruggerlo in modo tale che gli altri abbiano letto tutti i messaggi dal buffer*/
struct dispatcher_parameters *dispatcher_init(int buffer_dim,list_t *list){
    //pthread_t dispatcher = (pthread_t) malloc(sizeof(pthread_t));
    struct dispatcher_parameters *parameters = malloc(sizeof(struct dispatcher_parameters));
    parameters->list_of_readers = list;
    parameters->buffer_provider_to_dispatcher = buffer_init((unsigned int) buffer_dim);

    pthread_create(&parameters->dispatcher,NULL,from_provider_to_readers,parameters);
    return parameters;
}

