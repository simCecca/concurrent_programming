//
// Created by simone on 07/12/17.
//

#include <zconf.h>
#include "provider.h"
#include "HWC1/msg_string.h"
#include "HWC1/buffer.h"
#include "poison_pill.h"
#include <stdio.h>
#include <stdlib.h>
/*il provider spedisce una sequenza finita di messaggi al dispatcher; la
sequenza è sempre terminata da una poison pill e dopo il suo invio il
provider termina spontaneamente*/
/*si basa tutto pensando che il provider non crei i messaggi lui stesso ma magari gli arrivino tramite parametro
 * simulazione di uno o più client*/

/*definisco una tipologia di messaggi*/
msg_t *produce_a_asynchronous_message_count(unsigned int count){
    char message[12];
    sprintf(message,"%d",count);
    printf("count %d\n",count);
    return msg_init_string(message);
}

void *send_a_finished_sequence_of_messages_to_the_dispatcher(void *param){
    struct provider_parameters *paramteter = (struct provider_parameters *) param;
    buffer_t *provider_buffer = paramteter->buffer_of_message;
    int how_many_messages = paramteter->how_many_messages;
    unsigned int init = 0;
    while(init < how_many_messages)
    {
        put_bloccante(provider_buffer,produce_a_asynchronous_message_count(init));
        init++;
    }
    put_bloccante(provider_buffer,POISON_PILL);
    pthread_exit(NULL);
    return POISON_PILL;
}
