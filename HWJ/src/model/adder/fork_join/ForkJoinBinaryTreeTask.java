package model.adder.fork_join;


import model.processor.FakeProcessor;
import model.tree.structure.Node;

import java.util.concurrent.RecursiveTask;

/**
 * Struttura dei Task
 if ( N < SEQUENTIAL_THRESHOLD ) {
    // più conveniente una soluzione seriale
    << soluzione diretta (e seriale) del problema >>
 } else {
    // più conveniente una decomposizione parallela
    <<decomposizione in sotto-task >>
    <<fork di tutti i sotto-task >>
    <<join di tutti i sotto-task >>
    <<ricomposizione dei risultati parziali >>
 }
 */
public class ForkJoinBinaryTreeTask extends RecursiveTask<Integer> {

    private Node currentNode;
    /*soglia sotto la quale si procede serialmente, da varie misurazioni viene fuori che è migliore
      fino ad un altezza in un intorno ai (5 - 6 - 7) => nodi(32 - 64 - 128)*/
    private int SEQUENTIAL_THRESHOLD = 64;

    private int nodiMancanti; //a spanne

    private FakeProcessor processor;

    public ForkJoinBinaryTreeTask(Node currentNode, int nodiMancanti){
        this.currentNode = currentNode;

        /*avere questo tipo di dato statico è buono se l'albero non aumenta di molto le sue dimensioni
        * durante il calcolo, altrimenti si dovrebbe calcolare dinamicamente, ma visto che la documentazione
        * dice che si può sbagliare anche di molto, allora va bene anche una granularità meno fine*/
        this.nodiMancanti = nodiMancanti;

        this.processor = new FakeProcessor(1000);
    }

    @Override
    protected Integer compute() {

        if(this.nodiMancanti < this.SEQUENTIAL_THRESHOLD)
            return this.sommaSeriale(this.currentNode);

        else{
            int currentValue = this.processor.onerousFunction(this.currentNode.getValue());
            //decomposizione in sotto-task
            this.nodiMancanti--;
            ForkJoinBinaryTreeTask left = new ForkJoinBinaryTreeTask(this.currentNode.getSx(),this.nodiMancanti);
            ForkJoinBinaryTreeTask rigth = new ForkJoinBinaryTreeTask(this.currentNode.getDx(),this.nodiMancanti);

            //fork di tutti i sotto-task (potenzialmente delego)
            left.fork();
            //lo fa il thread corrente giustamente
            int rigthAns = rigth.compute();
            //join di tutti i sotto-task
            int leftAns = left.join();
            return leftAns + rigthAns + currentValue;
        }
    }


    private int sommaSeriale(Node current){
        int somma = 0;
        if(current == null)
            return 0;
        somma = processor.onerousFunction(current.getValue());
        if(current.getDx() != null)
            somma += sommaSeriale(current.getDx());
        if(current.getSx() != null)
            somma += sommaSeriale(current.getSx());
        return somma;
    }
}
