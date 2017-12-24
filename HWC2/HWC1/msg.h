//
// Created by simone on 23/11/17.
//

#ifndef HWC1_MSG_H
#define HWC1_MSG_H
/*general structure, so that we need to a different .h file for each content type*/
typedef struct msg {
    void* content;
    // generico contenuto del messaggio
    struct msg * (*msg_init)(void*);
    // creazione msg
    void (*msg_destroy)(struct msg *);
    // deallocazione msg
    struct msg * (*msg_copy)(struct msg *); // creazione/copia msg
} msg_t;

#endif //HWC1_MSG_H
