package model.adder.bounded;

import model.processor.FakeProcessor;
import model.tree.structure.Node;

import java.util.Deque;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.Thread.sleep;

public class BoundedBufferAdderTask implements Callable<Integer> {

    private CyclicBarrier barrier;
    private LinkedBlockingDeque<Deque<Node>> tasksQueue;

    private int height;
    private FakeProcessor processor;

    /*nasce il bisogno di avere due liste che essenzialmente hanno lo stesso utilizzo, solamente che
    * la prima, limitateNodeBuffer, ci serve in una fase iniziale perchè è la blockingQueue passata
    * a tutti i thread, invece currentBufferNode è la lista dei task del thread in esame, quella che poi
    * andrà inserita nella tasksQueue*/
    private LinkedBlockingDeque<Node> limitateNodeBuffer;
    private Deque<Node> currentBufferNode;
    private Node nodoRubato;

    public BoundedBufferAdderTask(CyclicBarrier barrier, LinkedBlockingDeque<Node> limitateNodeBuffer, int height, LinkedBlockingDeque<Deque<Node>> tasksQueue){
        this.barrier = barrier;
        this.limitateNodeBuffer = limitateNodeBuffer;
        this. height = height;
        this.tasksQueue = tasksQueue;
        this.processor = new FakeProcessor(1000);

        /*sto limitando il buffer facendolo di dimensione D per ogni Thread ==> O(n*d)*/
        this.currentBufferNode = new LinkedBlockingDeque<>(this.height);
    }

    @Override
    public Integer call() throws InterruptedException {
        /*prendo la root se non l'ha già presa un atro thread, quindi in un contesto multi-thread
        * è sicuro che solo un thread avrà la root e gli altri avranno null*/
        Node vittima = limitateNodeBuffer.poll();

        //inserisco il buffer del thread corrente nella lista dei buffer
        this.tasksQueue.add(this.currentBufferNode);

        return this.somma(vittima);


    }
    /*la prima volta che richiamo questo metodo, this.nodoRubato, può essere null, oppure !=  null
    * in base a se sono stato il primo thread ad accedere al buffer limitateNodeBuffer comune a tutti
    * i thread*/

    /**
     * sottomette tutti i task e se li finisce prova a rubarli a qualche altro thread
     * @return la somma di tutti i nodi che riesce a sommare prima della fine della struttura
     */
    private int somma(Node current){
        /*se non ho finito i miei task interni al mio buffer this.currentBufferNode, allora
         * non rubo*/
        try
        {
            if(current != null)
            {
                int currentValue = this.processor.onerousFunction(current.getValue());
                Node dx = current.getDx();
                Node sx = current.getSx();
                if( dx != null)
                {
                    this.currentBufferNode.addLast(dx);
                }
                if( sx != null)
                { //se lo prendo invece di metterlo nel buffer sono sicuro che non rimango a secco
                    this.currentBufferNode.addLast(sx);
                }
                //if(reset) barrier.reset();
                return currentValue + somma(this.currentBufferNode.pollLast());
            }
            else {
                /*quando ho finito i miei task, devo rubare da altri*/
                boolean rubato = this.ruba();
                if (rubato) {
                   return somma(this.nodoRubato);
                }
                //fine dei giochi
                barrier.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            System.out.println("bariera rotta");
        }
        return 0;
    }


    /**
     *provo a rubare
     * @return true se sono riuscito a ribare, false altrimenti
     */
    private boolean ruba() {

        for (Deque<Node> buffer : this.tasksQueue) {
            if (buffer != this.currentBufferNode) {
                //lo prendo subito, prima che me lo rubino
                this.nodoRubato = buffer.pollFirst();
                if (this.nodoRubato != null)
                    return true;
            }
        }
        return false;
    }
}
/*la cosa migliore da fare è rubare a chi ne ha di più:
        * - perchè così rubo un qualcosa di grosso
        * - perchè, se ne ha di più vuol dire che è più lento così e evito lo stallo
        * - perchè sicuramente il numero di Thread non sarà mai elevato, quindi scalerà
        * molto sul numero di task da assegnare a tutti i thread, quindi il costo computazionale
        * che richiede il calcolo della ricerca del massimo è < del beneficio possibile.
          - devo scorrere comunque una sola volta tutta la lista, perchè una volta scorsa trovo
          quello a task maggiori, e se quello è a null, significa che ho finito tutta la visita

    IMPLEMENTAZIONE:

        Deque<Node> maxSize = null;
        int size = 0 ;
        for (Deque<Node> buffer : this.tasksQueue)
        {
            if (buffer != this.currentBufferNode)
            {/*rubo da questo*/
             /*   int currentSize = buffer.size();
                if (currentSize > size)
                {
                    size = currentSize;
                    maxSize = buffer;
                }
            }
        }

        //se ho trovato il max rubo e ritorno true
        if(maxSize != null)
        {
            this.nodoRubato = maxSize.pollFirst();
            return true;
        }
        if(warm_up < 3){
            warm_up++;
            sleep(20);
            return ruba();
        }
        return false;
      }

        */
