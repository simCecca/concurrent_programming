//
// Created by simone on 24/11/17.
//
#include <CUnit/CUnit.h>
#include <CUnit/Basic.h>
#include "buffer.h"
#include "msg_string.h"
#include <unistd.h>


static buffer_t * buffer = NULL;
msg_t *msg1 = NULL;
msg_t *msg2 = NULL;
msg_t *msg3 = NULL;
pthread_t producer;
pthread_t consumer;
msg_t *msg_returned_from_producer;
msg_t *msg_returned_from_consumer;
int time_sleep_producer = 2;
int time_sleep_consumer = 2;
int buffer_dimension = 4;
msg_t **msg_array_producers = NULL;
msg_t **msg_array_consumers = NULL;
pthread_t *producers = NULL;
pthread_t *consumers = NULL;
int threads_that_are_using_the_buffer = 0;
/*per passare la funzione put come parametro (passo il puntatore a funzione)*/
typedef struct msg *(*generic_put)(buffer_t *,msg_t *);

typedef struct parameter_thread_task{
    int buffer_size;
    int array_dim;
    int number_of_thread;
    //normalmente utiizzato per il task del produttore
    void * task_1_for_thread;
    //normalmente utilizzato per il task del consumatore
    void * task_2_for_thread;
};



/*UTILS*/

void create_a_empty_buffer(int buffer_size){
    buffer = buffer_init((unsigned int) buffer_size);
    msg1 = msg_init_string((char *) ("first message"));
    msg2 = msg_init_string((char *) ("second message"));
    threads_that_are_using_the_buffer = 0;
}


void create_a_full_buffer(int buffer_size,generic_put put){
    buffer = buffer_init((unsigned int) buffer_size);
    msg1 = msg_init_string((char *) ("first message"));
    msg2 = msg_init_string((char *) ("second message"));
    //si può fare anche concorrentemente richiamando many_producters
    for(int i=0; i<buffer_size; i++)
        put(buffer,msg1);
}

void * insert_a_message_in_the_buffer_blocking(void *time_sleep){
    int tempo = *((int *) time_sleep);
    sleep((unsigned int) tempo);
    return put_bloccante(buffer,msg1);
}

void * remove_a_message_to_the_buffer_blocking(void *time_sleep){
    int tempo = *((int *) time_sleep);
    sleep((unsigned int) tempo);
    return get_bloccante(buffer);
}

void * insert_a_message_in_the_buffer_not_blocking(void *time_sleep){
    int tempo = *((int *) time_sleep);
    sleep((unsigned int) tempo);
    return put_non_bloccante(buffer,msg1);
}

void * remove_a_message_to_the_buffer_not_blocking(void *time_sleep){
    int tempo = *((int *) time_sleep);
    sleep((unsigned int) tempo);
    return get_non_bloccante(buffer);
}

void * many_consumers(void * parameters){
    struct parameter_thread_task *par= (struct parameter_thread_task *) parameters;
    int buffer_size = par->buffer_size;
    msg_array_consumers = malloc(sizeof(msg_t *) * par->array_dim);
    consumers = malloc(sizeof(pthread_t) * par->number_of_thread);
    for(int i = 0; i<buffer_size; i++)
        pthread_create(&(consumers[i]), NULL, par->task_2_for_thread, &time_sleep_consumer);
    for(int i = 0; i<buffer_size; i++)
        pthread_join(consumers[i], (void **) &(msg_array_consumers[i]));
}
void * many_producers( void * parameters){
    struct parameter_thread_task *par= (struct parameter_thread_task *) parameters;
    int buffer_size = par->buffer_size;
    msg_array_producers = malloc(sizeof(msg_t *) * par->array_dim);
    producers = malloc(sizeof(pthread_t) * par->number_of_thread);
    for(int i = 0; i<buffer_size; i++)
        pthread_create(&(producers[i]), NULL, par->task_1_for_thread, &time_sleep_producer);
    for(int i = 0; i<buffer_size; i++) {
        pthread_join(producers[i], (void **) &(msg_array_producers[i]));
        threads_that_are_using_the_buffer++;
    }

}

void destroy_msg_array_and_producers(){
    free(msg_array_producers);
    free(producers);
    buffer_destroy(buffer);
    time_sleep_producer = 2;
}

void destroy_msg_array_and_consumers(){
    free(msg_array_consumers);
    free(consumers);
    time_sleep_consumer = 2;
}


//per eseguire produttori bloccanti e non bloccanti assieme
void * many_producers_blocking_and_not_blocking(void * parameters){
    struct parameter_thread_task *par= (struct parameter_thread_task *) parameters;
    int buffer_size = par->buffer_size;
    msg_array_producers = malloc(sizeof(msg_t *) * par->array_dim);
    producers = malloc(sizeof(pthread_t) * par->number_of_thread);
    void **tasks = malloc(sizeof(void *) * 2);
    tasks[0] = par->task_1_for_thread;
    tasks[1] = par->task_2_for_thread;
    for(int i = 0; i<buffer_size; i++)
        pthread_create(&(producers[i]), NULL, tasks[i % 2], &time_sleep_producer);
    for(int i = 0; i<buffer_size; i++)
        pthread_join(producers[i], (void **) &(msg_array_producers[i]));
    free(tasks);
}

void * many_consumers_blocking_and_not_blocking(void * parameters){
    struct parameter_thread_task *par= (struct parameter_thread_task *) parameters;
    int buffer_size = par->buffer_size;
    msg_array_consumers = malloc(sizeof(msg_t *) * par->array_dim);
    consumers = malloc(sizeof(pthread_t) * par->number_of_thread);
    void **tasks = malloc(sizeof(void *) * 2);
    tasks[0] = par->task_1_for_thread;
    tasks[1] = par->task_2_for_thread;
    for(int i = 0; i<buffer_size; i++)
        pthread_create(&(consumers[i]), NULL, tasks[i % 2], &time_sleep_consumer);
    for(int i = 0; i<buffer_size; i++)
        pthread_join(consumers[i], (void **) &(msg_array_consumers[i]));
    free(tasks);
}

/*TEST*/

/*(P=1; C=0; N=1)*/
void test_product_a_single_message_in_a_empty_buffer_blocking(void){
    buffer = buffer_init(1);
    msg1 = msg_init_string((char *) ("I'm the first message"));
    CU_ASSERT_PTR_EQUAL(put_bloccante(buffer,msg1), msg1);
    CU_ASSERT_PTR_EQUAL(get_bloccante(buffer),msg1);
    buffer_destroy(buffer);
}

void test_product_a_single_message_in_a_empty_buffer_not_blocking(void){
    buffer = buffer_init(1);
    msg1 = msg_init_string((char *) ("I'm the first message"));
    CU_ASSERT_PTR_EQUAL(put_non_bloccante(buffer,msg1), msg1);
    CU_ASSERT_PTR_EQUAL(get_non_bloccante(buffer),msg1);
    buffer_destroy(buffer);
}

/*(P=0; C=1; N=1)*/
void test_consuming_a_single_message_from_a_full_buffer_blocking(void){

    create_a_full_buffer(1,put_bloccante);
    //Verify that the buffer is full
    CU_ASSERT_EQUAL((buffer->insertion_index - buffer->extract_index),buffer->maxsize);

    CU_ASSERT_PTR_EQUAL(get_bloccante(buffer),msg1);
    buffer_destroy(buffer);
}

void test_consuming_a_single_message_from_a_full_buffer_not_blocking(void){
    create_a_full_buffer(1,put_non_bloccante);
    //Verify that the buffer is full
    CU_ASSERT_EQUAL((buffer->insertion_index - buffer->extract_index),buffer->maxsize);

    CU_ASSERT_PTR_EQUAL(get_non_bloccante(buffer),msg1);
    buffer_destroy(buffer);
}

/*(P=1; C=0; N=1)*/

void test_production_in_a_full_buffer_blocking(void){
    create_a_full_buffer(1,put_bloccante);
    pthread_create(&consumer, NULL, remove_a_message_to_the_buffer_blocking, &time_sleep_consumer);
    CU_ASSERT_PTR_EQUAL(put_bloccante(buffer,msg2),msg2);
    //Verify that the buffer is full
    CU_ASSERT_EQUAL((buffer->insertion_index - buffer->extract_index),buffer->maxsize);
    buffer_destroy(buffer);
}

void test_production_in_a_full_buffer_not_blocking(void){
    create_a_full_buffer(1,put_non_bloccante);
    CU_ASSERT_PTR_EQUAL(put_non_bloccante(buffer,msg2),BUFFER_ERROR);
    buffer_destroy(buffer);
}

/*(P=0; C=1; N=1)*/
/*così sto intrinsecamente facendo P=1*/
void test_consuming_from_an_empty_buffer_blocking(void){
    create_a_empty_buffer(1);
    pthread_create(&producer, NULL, insert_a_message_in_the_buffer_blocking, &time_sleep_producer);

    CU_ASSERT_PTR_EQUAL(get_bloccante(buffer),msg1);

    buffer_destroy(buffer);
}

void test_consuming_from_an_empty_buffer_not_blocking(void){
    buffer = buffer_init(1);
    CU_ASSERT_PTR_EQUAL(get_non_bloccante(buffer),BUFFER_ERROR);
    buffer_destroy(buffer);
}

/*(P=1; C=1; N=1)*/

void test_concurrent_consumption_and_production_of_a_message_from_a_single_buffer_first_the_consumer_blocking(void){
    create_a_empty_buffer(1);
    msg_t *msg;
    pthread_create(&producer, NULL, insert_a_message_in_the_buffer_blocking, &time_sleep_producer);
    CU_ASSERT_PTR_EQUAL(get_bloccante(buffer),msg1);
    pthread_join(producer, (void **)&msg);
    CU_ASSERT_PTR_EQUAL(msg,msg1);
    buffer_destroy(buffer);
}

void test_concurrent_consumption_and_production_of_a_message_from_a_single_buffer_first_the_consumer_not_blocking(void){
    create_a_full_buffer(1,put_non_bloccante);
    pthread_create(&producer, NULL, insert_a_message_in_the_buffer_not_blocking, &time_sleep_producer);
    CU_ASSERT_PTR_EQUAL(get_non_bloccante(buffer),msg1);
    pthread_join(producer, (void **) &msg_returned_from_producer);
    CU_ASSERT_PTR_EQUAL(msg_returned_from_producer,msg1);
    buffer_destroy(buffer);

}

/*(P=1; C=1; N=1)*/

void test_concurrent_consumption_and_production_of_a_message_from_a_single_buffer_first_the_producer_blocking(){
    create_a_empty_buffer(1);
    time_sleep_consumer = 0;
    pthread_create(&producer, NULL, insert_a_message_in_the_buffer_blocking, &time_sleep_producer);
    pthread_create(&consumer, NULL, remove_a_message_to_the_buffer_blocking, &time_sleep_consumer);
    pthread_join(producer,(void **) &msg_returned_from_producer);
    pthread_join(consumer,(void **) &msg_returned_from_consumer);
    CU_ASSERT_PTR_EQUAL(msg_returned_from_producer,msg1);
    CU_ASSERT_PTR_EQUAL(msg_returned_from_consumer,msg1);
    buffer_destroy(buffer);
}

void test_concurrent_consumption_and_production_of_a_message_from_a_single_buffer_first_the_producer_not_blocking(){
    create_a_empty_buffer(1);
    pthread_create(&consumer,NULL,remove_a_message_to_the_buffer_not_blocking,&time_sleep_consumer);
    CU_ASSERT_PTR_EQUAL(put_non_bloccante(buffer,msg1),msg1);
    pthread_join(consumer,(void **) &msg2);
    CU_ASSERT_PTR_EQUAL(msg2,msg1);
    buffer_destroy(buffer);
}

/*(P>1; C=0; N=1)*/
/*senza un consumatore ho stallo =>P>1; C=1; N=1*/
void test_concurrent_production_of_multiple_messages_in_one_empty_buffer_unit_blocking(){
    create_a_empty_buffer(1);
    pthread_create(&producer,NULL,insert_a_message_in_the_buffer_blocking,&time_sleep_producer);
    CU_ASSERT_PTR_EQUAL(put_bloccante(buffer,msg2),msg2);
    pthread_create(&consumer,NULL,remove_a_message_to_the_buffer_blocking,&time_sleep_consumer);
    pthread_join(producer,(void **) &msg3);
    CU_ASSERT_PTR_EQUAL(msg1,msg3);
    buffer_destroy(buffer);
}

void test_concurrent_production_of_multiple_messages_in_one_empty_buffer_unit_not_blocking(){
    create_a_empty_buffer(1);
    pthread_create(&producer,NULL,insert_a_message_in_the_buffer_not_blocking,&time_sleep_producer);
    CU_ASSERT_PTR_EQUAL(put_non_bloccante(buffer,msg2),msg2);
    pthread_join(producer,(void **) &msg3);
    CU_ASSERT_PTR_EQUAL(msg3,BUFFER_ERROR)
    buffer_destroy(buffer);
}

/*(P=0; C>1; N=1)*/
void test_concurrent_consumption_of_multiple_messages_from_a_full_unit_buffer_not_blocking(){
    create_a_full_buffer(1,put_non_bloccante);
    pthread_create(&consumer,NULL,remove_a_message_to_the_buffer_not_blocking,&time_sleep_consumer);
    CU_ASSERT_PTR_EQUAL(get_non_bloccante(buffer),msg1);
    pthread_join(consumer,(void **) &msg2);
    CU_ASSERT_PTR_EQUAL(msg2,BUFFER_ERROR);
    buffer_destroy(buffer);
}
/*con p=1 perchè altrimenti ho stallo che non posso risolvere dato che è bloccante*/
void test_concurrent_consumption_of_multiple_messages_from_a_full_unit_buffer_blocking(){
    create_a_full_buffer(1,put_bloccante);

    pthread_create(&consumer,NULL,remove_a_message_to_the_buffer_blocking,&time_sleep_consumer);
    CU_ASSERT_PTR_EQUAL(get_bloccante(buffer),msg1);
    pthread_create(&producer,NULL,insert_a_message_in_the_buffer_blocking,&time_sleep_producer);
    pthread_join(consumer,(void **) &msg2);
    CU_ASSERT_PTR_EQUAL(msg2,msg1);
    buffer_destroy(buffer);
}

/*(P>1; C=0; N>1)*/

void test_concurrent_production_of_multiple_messages_in_one_empty_buffer_the_buffer_does_not_fill_blocking(){
    struct parameter_thread_task for_producers = {buffer_dimension-1,buffer_dimension,buffer_dimension-1,insert_a_message_in_the_buffer_blocking,NULL};
    create_a_empty_buffer(buffer_dimension);
    many_producers(&for_producers);
    for(int i=0;i<(for_producers.buffer_size);i++)
        CU_ASSERT_PTR_EQUAL(msg_array_producers[i],msg1);
    destroy_msg_array_and_producers();
}

void test_concurrent_production_of_multiple_messages_in_one_empty_buffer_the_buffer_does_not_fill_not_blocking(){
    struct parameter_thread_task for_producers = {buffer_dimension-1,buffer_dimension,buffer_dimension-1,insert_a_message_in_the_buffer_not_blocking,NULL};
    create_a_empty_buffer(buffer_dimension);
    many_producers(&for_producers);
    for(int i=0;i<(for_producers.buffer_size);i++)
        CU_ASSERT_PTR_EQUAL(msg_array_producers[i],msg1);
    destroy_msg_array_and_producers();
}

/*(P>1; C=0; N>1)*/

void test_concuttent_production_of_multiple_messages_in_a_full_buffer_the_buffer_is_already_saturated_blocking(){
    struct parameter_thread_task parameters_for_multi_threading = {buffer_dimension-1,buffer_dimension,buffer_dimension-1,insert_a_message_in_the_buffer_blocking,remove_a_message_to_the_buffer_blocking};
    create_a_full_buffer(buffer_dimension,put_bloccante);
    time_sleep_consumer = 4;
    time_sleep_producer = 0;
    pthread_create(&consumer,NULL,many_consumers,&parameters_for_multi_threading);
    pthread_create(&producer,NULL,many_producers,&parameters_for_multi_threading);
    pthread_join(consumer,NULL);
    pthread_join(producer,NULL);
    for(int i=0;i<(parameters_for_multi_threading.buffer_size);i++)
        CU_ASSERT_PTR_EQUAL(msg_array_producers[i],msg1);
    destroy_msg_array_and_producers();
}

void test_concuttent_production_of_multiple_messages_in_a_full_buffer_the_buffer_is_already_saturated_not_blocking(){
    struct parameter_thread_task for_producers = {buffer_dimension-1,buffer_dimension,buffer_dimension-1,insert_a_message_in_the_buffer_not_blocking,NULL};
    create_a_full_buffer(buffer_dimension,put_non_bloccante);
    many_producers(&for_producers);
    for(int i=0;i<(for_producers.buffer_size);i++)
        CU_ASSERT_PTR_EQUAL(msg_array_producers[i],BUFFER_ERROR);
    destroy_msg_array_and_producers();
}

/*(P>1; C=0; N>1)*/
//forzatura in realtà è p>1; c>1; n>1
void test_concurrent_production_of_multiple_messages_in_a_empty_buffer_the_buffer_is_saturated_in_progress_blocking() {
    struct parameter_thread_task parameters_for_multi_threading = {buffer_dimension-1,buffer_dimension,buffer_dimension-1,insert_a_message_in_the_buffer_blocking,remove_a_message_to_the_buffer_blocking};
    create_a_empty_buffer(buffer_dimension-2);
    time_sleep_consumer = 4;
    time_sleep_producer = 0;
    pthread_create(&producer, NULL, many_producers, &parameters_for_multi_threading);
    sleep(3);
    CU_ASSERT_EQUAL((buffer->insertion_index - buffer->extract_index),buffer->maxsize);
    pthread_create(&consumer, NULL, many_consumers, &parameters_for_multi_threading);
    pthread_join(producer, NULL);
    pthread_join(consumer, NULL);
    for (int i = 0; i < parameters_for_multi_threading.buffer_size; i++)
        CU_ASSERT_PTR_EQUAL(msg_array_consumers[i], msg1);
    destroy_msg_array_and_consumers();
    destroy_msg_array_and_producers();
}

void test_concurrent_production_of_multiple_messages_in_a_empty_buffer_the_buffer_is_saturated_in_progress_not_blocking() {
    struct parameter_thread_task for_producers = {buffer_dimension-2,buffer_dimension,buffer_dimension-2,insert_a_message_in_the_buffer_not_blocking,NULL};
    create_a_empty_buffer(for_producers.buffer_size);
    many_producers(&for_producers);
    for (int i = 0; i < for_producers.number_of_thread; i++)
        CU_ASSERT_PTR_EQUAL(msg_array_producers[i], msg1);
    pthread_create(&producer,NULL,insert_a_message_in_the_buffer_not_blocking,&time_sleep_producer);
    pthread_join(producer,(void **) &msg3);
    CU_ASSERT_EQUAL(msg3,BUFFER_ERROR);
    destroy_msg_array_and_producers();
}

/*(P=0; C>1; N>1)*/
void test_concurrent_consumption_of_multiple_message_from_a_full_buffer_blocking(){
    struct parameter_thread_task for_consumers = {buffer_dimension,buffer_dimension,buffer_dimension,NULL,remove_a_message_to_the_buffer_blocking};
    create_a_full_buffer(buffer_dimension,put_bloccante);
    pthread_create(&consumer,NULL,many_consumers,&for_consumers);
    pthread_join(consumer,NULL);
    CU_ASSERT_EQUAL((buffer->insertion_index - buffer->extract_index),0);
    for (int i = 0; i < buffer_dimension; i++)
        CU_ASSERT_PTR_EQUAL(msg_array_consumers[i], msg1);
    destroy_msg_array_and_consumers();
    buffer_destroy(buffer);
}

void test_concurrent_consumption_of_multiple_message_from_a_full_buffer_not_blocking(){
    struct parameter_thread_task for_consumers = {buffer_dimension,buffer_dimension,buffer_dimension,NULL,remove_a_message_to_the_buffer_not_blocking};
    create_a_full_buffer(buffer_dimension,put_non_bloccante);
    pthread_create(&consumer,NULL,many_consumers,&for_consumers);
    pthread_join(consumer,NULL);
    CU_ASSERT_EQUAL((buffer->insertion_index - buffer->extract_index),0);
    for (int i = 0; i < buffer_dimension; i++)
        CU_ASSERT_PTR_EQUAL(msg_array_consumers[i], msg1);
    destroy_msg_array_and_consumers();
    buffer_destroy(buffer);
}

/*(P>1; C>1; N=1)*/
void test_concurrent_consumptions_and_productions_of_multiple_messages_in_a_unit_buffer_blocking(){
    struct parameter_thread_task parameters_for_multi_threading = {buffer_dimension,buffer_dimension,buffer_dimension,insert_a_message_in_the_buffer_blocking,remove_a_message_to_the_buffer_blocking};
    create_a_empty_buffer(1);
    pthread_create(&consumer,NULL,many_consumers,&parameters_for_multi_threading);
    pthread_create(&producer,NULL,many_producers,&parameters_for_multi_threading);
    pthread_join(consumer,NULL);
    pthread_join(producer,NULL);
    for (int i = 0; i < buffer_dimension; i++)
    {
        CU_ASSERT_PTR_EQUAL(msg_array_consumers[i], msg1);
        CU_ASSERT_PTR_EQUAL(msg_array_producers[i], msg1);
    }
    destroy_msg_array_and_consumers();
    destroy_msg_array_and_producers();
}

void test_concurrent_consumptions_and_productions_of_multiple_messages_in_a_unit_buffer_not_blocking(){
    struct parameter_thread_task parameters_for_multi_threading = {buffer_dimension,buffer_dimension,buffer_dimension,insert_a_message_in_the_buffer_not_blocking,remove_a_message_to_the_buffer_not_blocking};
    create_a_empty_buffer(1);
    pthread_create(&consumer,NULL,many_consumers,&parameters_for_multi_threading);
    pthread_create(&producer,NULL,many_producers,&parameters_for_multi_threading);
    pthread_join(consumer,NULL);
    pthread_join(producer,NULL);
    int number_of_buffer_error = 0;
    for (int i = 0; i < buffer_dimension; i++)
    {
        if(msg_array_consumers[i] == BUFFER_ERROR)
            number_of_buffer_error++;
        if(msg_array_producers[i] == BUFFER_ERROR)
            number_of_buffer_error++;
        CU_ASSERT(msg_array_consumers[i] == msg1 || msg_array_consumers[i] == BUFFER_ERROR);
        CU_ASSERT(msg_array_producers[i] == msg1 || msg_array_producers[i] == BUFFER_ERROR);
    }
    CU_ASSERT_TRUE(number_of_buffer_error > 0);
    destroy_msg_array_and_consumers();
    destroy_msg_array_and_producers();
}

/*(P>1; C>1; N>1)*/

void test_concurrent_consumptions_and_productions_of_multiple_messages_in_a_buffer_blocking(){
    struct parameter_thread_task parameters_for_multi_threading = {buffer_dimension * 2,buffer_dimension * 2,buffer_dimension * 2,insert_a_message_in_the_buffer_blocking,remove_a_message_to_the_buffer_blocking};
    create_a_empty_buffer(buffer_dimension);
    pthread_create(&consumer,NULL,many_consumers,&parameters_for_multi_threading);
    pthread_create(&producer,NULL,many_producers,&parameters_for_multi_threading);
    pthread_join(consumer,NULL);
    pthread_join(producer,NULL);
    for (int i = 0; i < parameters_for_multi_threading.buffer_size; i++)
    {
        CU_ASSERT_PTR_EQUAL(msg_array_consumers[i], msg1);
        CU_ASSERT_PTR_EQUAL(msg_array_producers[i], msg1);
    }
    destroy_msg_array_and_consumers();
    destroy_msg_array_and_producers();
}

void test_concurrent_consumptions_and_productions_of_multiple_messages_in_a_buffer_not_blocking(){
    struct parameter_thread_task parameters_for_multi_threading = {buffer_dimension * 2,buffer_dimension * 2,buffer_dimension * 2,insert_a_message_in_the_buffer_not_blocking,remove_a_message_to_the_buffer_not_blocking};
    create_a_empty_buffer(buffer_dimension);
    pthread_create(&consumer,NULL,many_consumers,&parameters_for_multi_threading);
    pthread_create(&producer,NULL,many_producers,&parameters_for_multi_threading);
    pthread_join(consumer,NULL);
    pthread_join(producer,NULL);
    int number_of_buffer_error = 0;
    for (int i = 0; i < parameters_for_multi_threading.buffer_size; i++)
    {
        if(msg_array_consumers[i] == BUFFER_ERROR)
            number_of_buffer_error++;
        if(msg_array_producers[i] == BUFFER_ERROR)
            number_of_buffer_error++;
        CU_ASSERT(msg_array_consumers[i] == msg1 || msg_array_consumers[i] == BUFFER_ERROR);
        CU_ASSERT(msg_array_producers[i] == msg1 || msg_array_producers[i] == BUFFER_ERROR);
    }
    CU_ASSERT_TRUE(number_of_buffer_error > 0);
    destroy_msg_array_and_consumers();
    destroy_msg_array_and_producers();
}

/*bloccante e non bloccante assieme*/
/*(P>1; C>1; N>1)*/
void test_both_blocking_and_not_blocking(){
    struct parameter_thread_task parameters_for_multi_threading_producers = {buffer_dimension * 2,buffer_dimension * 2,buffer_dimension * 2,insert_a_message_in_the_buffer_blocking,insert_a_message_in_the_buffer_not_blocking};
    struct parameter_thread_task parameters_for_multi_threading_consumers = {buffer_dimension * 2,buffer_dimension * 2,buffer_dimension * 2,remove_a_message_to_the_buffer_blocking,remove_a_message_to_the_buffer_not_blocking};
    create_a_empty_buffer(buffer_dimension);
    pthread_create(&consumer,NULL,many_consumers_blocking_and_not_blocking,&parameters_for_multi_threading_consumers);
    pthread_create(&producer,NULL,many_producers_blocking_and_not_blocking,&parameters_for_multi_threading_producers);
    pthread_join(consumer,NULL);
    pthread_join(producer,NULL);
    int number_of_buffer_error = 0;
    for (int i = 0; i < parameters_for_multi_threading_producers.buffer_size; i++)
    {
        if(msg_array_consumers[i] == BUFFER_ERROR)
            number_of_buffer_error++;
        if(msg_array_producers[i] == BUFFER_ERROR)
            number_of_buffer_error++;
        printf("giggi\n");
        CU_ASSERT(msg_array_consumers[i] == msg1 || msg_array_consumers[i] == BUFFER_ERROR);
        CU_ASSERT(msg_array_producers[i] == msg1 || msg_array_producers[i] == BUFFER_ERROR);
    }
    CU_ASSERT_TRUE(number_of_buffer_error > 0);
    destroy_msg_array_and_consumers();
    destroy_msg_array_and_producers();
}

int main() {
    CU_initialize_registry(); //inizializzazione testing
    CU_pSuite suite = CU_add_suite("buffer_init", 0, 0);
    /*sono presenti tutti i test ordinati nello stesso modo in cui sono proposti, in particolare B sta a rappresentare la
     * versione bloccante del test e NB la non bloccante*/
    CU_add_test(suite, "(P=1; C=0; N=1) B", test_product_a_single_message_in_a_empty_buffer_blocking);
    CU_add_test(suite, "(P=1; C=0; N=1) NB", test_product_a_single_message_in_a_empty_buffer_not_blocking);
    CU_add_test(suite, "(P=0; C=1; N=1) B", test_consuming_a_single_message_from_a_full_buffer_blocking);
    CU_add_test(suite, "(P=0; C=1; N=1) NB",test_consuming_a_single_message_from_a_full_buffer_not_blocking);
    CU_add_test(suite, "(P=1; C=0; N=1) B", test_production_in_a_full_buffer_blocking);
    CU_add_test(suite, "(P=1; C=0; N=1) NB", test_production_in_a_full_buffer_not_blocking);
    CU_add_test(suite, "(P=0; C=1; N=1) B", test_consuming_from_an_empty_buffer_blocking);
    CU_add_test(suite, "(P=0; C=1; N=1) NB", test_consuming_from_an_empty_buffer_not_blocking);
    CU_add_test(suite, "(P=1; C=1; N=1) B",test_concurrent_consumption_and_production_of_a_message_from_a_single_buffer_first_the_consumer_blocking);
    CU_add_test(suite, "(P=1; C=1; N=1) NB",test_concurrent_consumption_and_production_of_a_message_from_a_single_buffer_first_the_consumer_not_blocking);
    CU_add_test(suite, "(P=1; C=1; N=1) B",test_concurrent_consumption_and_production_of_a_message_from_a_single_buffer_first_the_producer_blocking);
    CU_add_test(suite, "(P=1; C=1; N=1) NB",test_concurrent_consumption_and_production_of_a_message_from_a_single_buffer_first_the_producer_not_blocking);
    CU_add_test(suite, "(P>1; C=0; N=1) B",test_concurrent_production_of_multiple_messages_in_one_empty_buffer_unit_blocking);
    CU_add_test(suite, "(P>1; C=0; N=1) NB",test_concurrent_production_of_multiple_messages_in_one_empty_buffer_unit_not_blocking);
    CU_add_test(suite, "(P=0; C>1; N=1) B",test_concurrent_consumption_of_multiple_messages_from_a_full_unit_buffer_blocking);
    CU_add_test(suite, "(P=0; C>1; N=1) NB",test_concurrent_consumption_of_multiple_messages_from_a_full_unit_buffer_not_blocking);
    CU_add_test(suite, "(P>1; C=0; N>1) B",test_concurrent_production_of_multiple_messages_in_one_empty_buffer_the_buffer_does_not_fill_blocking);
    CU_add_test(suite, "(P>1; C=0; N>1) NB",test_concurrent_production_of_multiple_messages_in_one_empty_buffer_the_buffer_does_not_fill_not_blocking);
    CU_add_test(suite, "(P>1; C=0; N>1) B",test_concuttent_production_of_multiple_messages_in_a_full_buffer_the_buffer_is_already_saturated_blocking);
    CU_add_test(suite, "(P>1; C=0; N>1) NB",test_concuttent_production_of_multiple_messages_in_a_full_buffer_the_buffer_is_already_saturated_not_blocking);
    CU_add_test(suite, "(P>1; C=0; N>1) B",test_concurrent_production_of_multiple_messages_in_a_empty_buffer_the_buffer_is_saturated_in_progress_blocking);
    CU_add_test(suite, "(P>1; C=0; N>1) NB",test_concurrent_production_of_multiple_messages_in_a_empty_buffer_the_buffer_is_saturated_in_progress_not_blocking);
    CU_add_test(suite, "(P=0; C>1; N>1) B",test_concurrent_consumption_of_multiple_message_from_a_full_buffer_blocking);
    CU_add_test(suite, "(P=0; C>1; N>1) NB",test_concurrent_consumption_of_multiple_message_from_a_full_buffer_not_blocking);
    CU_add_test(suite, "(P>1; C>1; N=1) B",test_concurrent_consumptions_and_productions_of_multiple_messages_in_a_unit_buffer_blocking);
    CU_add_test(suite, "(P>1; C>1; N=1) NB",test_concurrent_consumptions_and_productions_of_multiple_messages_in_a_unit_buffer_not_blocking);
    CU_add_test(suite, "(P>1; C>1; N>1) B",test_concurrent_consumptions_and_productions_of_multiple_messages_in_a_buffer_blocking);
    CU_add_test(suite, "(P>1; C>1; N>1) NB",test_concurrent_consumptions_and_productions_of_multiple_messages_in_a_buffer_not_blocking);
    CU_add_test(suite, "(P>1; C>1; N>1) blocking_and_not_blocking",test_both_blocking_and_not_blocking);
    CU_basic_set_mode(CU_BRM_VERBOSE);
    CU_basic_run_tests();
    CU_cleanup_registry(); //quando ho finito il testing per pulire la memoria utilizzata dal framework
    return 0;
}
