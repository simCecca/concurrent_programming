#include <stdio.h>
#include <zconf.h>
#include "dispatcher.h"
#include "HWC1/msg_string.h"
#include "provider.h"
#include "reader.h"

pthread_t provider;
int main() {
    unsigned int i = 5;
    list_t *list = list_init();

    struct dispatcher_parameters *return_from_dispatcher_init = dispatcher_init(i,list);

    struct provider_parameters provider_param= {i+5,return_from_dispatcher_init->buffer_provider_to_dispatcher};

    pthread_create(&provider,NULL,send_a_finished_sequence_of_messages_to_the_dispatcher,&provider_param);
    reader_t * reader1 = init_reader(i,"first_reader");
    reader_t * reader2 = init_reader(i,"second_reader");

    addElement(list,reader1);
    addElement(list,reader2);
    pthread_join(reader2->reader,NULL);
    pthread_join(reader1->reader,NULL);
    pthread_join(return_from_dispatcher_init->dispatcher,NULL);
    return 0;
}