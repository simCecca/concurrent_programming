package model.adder;

import model.processor.FakeProcessor;
import model.tree.structure.Node;

import java.util.Deque;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 * Framework per la decomposizione parallela ==> per la decomposizione dinamica
 * (visto i benefici ottenibili con scheduler dedicati che tengano
 esplicitamente conto della natura e delle relazioni intercorrenti
 tra i task sottomessi hanno meritato la progettazione di un intero Framework
 “Fork/Join” per la decomposizione parallela)

 il numero di task da creare normalmente per un problema di questo tipo non è banalmente
 prevedible; dipende direttamente dalla dimensione N del problema trattato.
 in sostanta la decomposizione in task viene rimandara a tempo dinamico ==> inizialmente task a grana
 più grossa che poi diventano a grana più fine.
 viene utilizzata una soglia SEQUENTIAL_THRESHOLD che mi va a classificare i task in piccoli ==> soluzione seriale
 o grandi ==> decomposizione parallela

 WORK STEALING:
 Ogni worker thread mantiene una propria scheduling queue La scheduling queue è acceduta

 LIFO: per il processamento “ordinario” dei propri task da parte dei worker  thread
 FIFO: per il reperimento di task degli altri worker thread da parte dei  worker thread altrimenti
 inattivi
 */

public class UnboundedBufferSumTask implements Callable<Integer>{
    private CyclicBarrier flowSybchonizer;
    private Deque<Node> illimitateNodeBuffer;
    private Node currentNode;
    private FakeProcessor processor;

    public UnboundedBufferSumTask(CyclicBarrier flowSybchronizer, Deque<Node> illimitateNodeBuffer){
        this.flowSybchonizer = flowSybchronizer;
        this.illimitateNodeBuffer = illimitateNodeBuffer;
        currentNode = null;
        this.processor = new FakeProcessor(1000);
    }

    @Override
    public Integer call() throws Exception {
        /*i thread visitatori accedono al buffer nel seguente modo: estraggono ripetutamente e concorrentemente
         * un nodo dalla testa del buffer ed inseriscono in coda gli eventuali figli, elaborano il valore del nodo
         * e procedono sino al verificarsi di una opportuna condizione di terminazione*/

        //poolFirst e non removeFirst perchè non vogliamo eccezioni
        currentNode = illimitateNodeBuffer.pollFirst();
        boolean riSincronizza = false;
        try
        {
            if (currentNode != null) {
                if (currentNode.getSx() != null) {
                    riSincronizza = true;
                    illimitateNodeBuffer.add(currentNode.getSx());
                }
                if (currentNode.getDx() != null) {
                    riSincronizza = true;
                    illimitateNodeBuffer.add(currentNode.getDx());
                }
                //per non rischiare che ci sia qualche thread dormiente
                if (riSincronizza) this.flowSybchonizer.reset();
                return processor.onerousFunction(currentNode.getValue()) + call();
            } else {
                //aspetto che tutti i flussi si sincronizzino qui
                this.flowSybchonizer.await();
            }
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (BrokenBarrierException e)
        {
            return 0;
        }
        return 0;
    }

}
