package model.adder.unbounded;

import model.processor.FakeProcessor;
import model.tree.structure.Node;

import java.util.Deque;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 *
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
        //boolean riSincronizza = false;
        try
        {
            if (currentNode != null) {
                if (currentNode.getSx() != null) {
                    //riSincronizza = true;
                    illimitateNodeBuffer.add(currentNode.getSx());
                }
                if (currentNode.getDx() != null) {
                    //riSincronizza = true;
                    illimitateNodeBuffer.add(currentNode.getDx());
                }
                //per non rischiare che ci sia qualche thread dormiente
                //if (riSincronizza) this.flowSybchonizer.reset();
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
            System.out.println("BrokenBarrier in unbounded task");
        }
        return 0;
    }

}
