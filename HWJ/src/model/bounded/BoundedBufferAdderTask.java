package model.bounded;

import model.processor.FakeProcessor;
import model.tree.structure.Node;

import java.util.Deque;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Struttura dei Task:
 *
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
public class BoundedBufferAdderTask implements Callable<Integer> {

    private CyclicBarrier barrier;
    private LinkedBlockingDeque<Deque<Node>> tasksQueue;

    private int height;
    private FakeProcessor processor;

    /*nasce il bisogno di avere due liste che essenzialmente hanno lo stesso utilizzo, solamente che
    * la prima, limitateNodeBuffer, ci serve in una fase iniziale perchè è la blockingQueue passata
    * a tutti i thread, invece currentNode è la lista dei task del thread in esame, quella che poi
    * andrà inserita nella tasksQueue*/
    private LinkedBlockingDeque<Node> limitateNodeBuffer;
    private Deque<Node> currentNode;


    public BoundedBufferAdderTask(CyclicBarrier barrier, LinkedBlockingDeque<Node> limitateNodeBuffer, int height, LinkedBlockingDeque<Deque<Node>> tasksQueue){
        this.barrier = barrier;
        this.limitateNodeBuffer = limitateNodeBuffer;
        this. height = height;
        this.tasksQueue = tasksQueue;
        this.processor = new FakeProcessor(1000);

        /*sto limitando il buffer facendolo di dimensione D per ogni Thread ==> O(n*d)*/
        this.currentNode = new LinkedBlockingDeque<>(this.height);
    }

    @Override
    public Integer call() throws Exception {
        /*prendo la root se non l'ha già presa un atro thread, quindi in un contesto multi-thread
        * è sicuro che solo un thread avrà la root e gli altri avranno null*/
        Node current = limitateNodeBuffer.poll();

        //se sono il primo ad aver acceduto alla lista
        if(current != null) this.currentNode.add(current);

        //inserisco il buffer del thread corrente nella lista dei buffer
        this.tasksQueue.add(this.currentNode);

        

        return null;
    }
}
