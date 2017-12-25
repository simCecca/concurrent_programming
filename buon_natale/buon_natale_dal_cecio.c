

#include <zconf.h>
#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>

pthread_t *buon_natale = NULL;

typedef struct {
    int number_of_buon_natale;
    void *task_for_buon;
    void *task_for_natale;
}patameter_thread_task;

void *i_m_a_beautiful_buon(void *buon){
    printf("buon \n");
}

void *i_m_a_beautiful_natale(void *natale){
    printf("natale \n\n");
}

void *amdahl_nun_te_temo(void *param){
    patameter_thread_task *parameters = (patameter_thread_task*) param;

    void **task_buon_natale = malloc(sizeof(void *) * 2);
    task_buon_natale[0] = parameters->task_for_buon;
    task_buon_natale[1] = parameters->task_for_natale;
    int number_of_buon_natale = parameters->number_of_buon_natale * 2;
    buon_natale = malloc(sizeof(pthread_t) * number_of_buon_natale);
    for(int i = 0; i<number_of_buon_natale ;i++){
        pthread_create(&(buon_natale[i]),NULL,task_buon_natale[i%2],NULL);
    }
    for(int i = 0; i<number_of_buon_natale ;i++){
        pthread_join(buon_natale[i],NULL);
    }
}

int main() {
    pthread_t auguri_dal_cecio;
    patameter_thread_task parametri = {10,i_m_a_beautiful_buon,i_m_a_beautiful_natale};
    pthread_create(&auguri_dal_cecio,NULL,amdahl_nun_te_temo,&parametri);
    pthread_join(auguri_dal_cecio,NULL);
    //delle free nun me ne frega ncazzo

    return 0;
}