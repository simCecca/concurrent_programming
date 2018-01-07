package model.adder.unbounded;

import model.processor.FakeProcessor;
import model.tree.structure.Node;
import model.tree.utils.BinaryTreeUtils;

import java.util.Deque;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;


public class UnboundedBufferSumTask implements Callable<Integer>{
    private CyclicBarrier flowSybchonizer;
    private Deque<Node> illimitateNodeBuffer;
    private Node currentNode;
    private FakeProcessor processor;

    private BinaryTreeUtils treeUtils;

    public UnboundedBufferSumTask(CyclicBarrier flowSybchronizer, Deque<Node> illimitateNodeBuffer, BinaryTreeUtils treeUtils){
        this.flowSybchonizer = flowSybchronizer;
        this.illimitateNodeBuffer = illimitateNodeBuffer;
        currentNode = null;
        this.processor = new FakeProcessor(1000);
        this.treeUtils = treeUtils;
    }

    @Override
    public Integer call() throws Exception {
        /*i thread visitatori accedono al buffer nel seguente modo: estraggono ripetutamente e concorrentemente
         * un nodo dalla testa del buffer ed inseriscono in coda gli eventuali figli, elaborano il valore del nodo
         * e procedono sino al verificarsi di una opportuna condizione di terminazione*/
        Integer somma = 0;
        while(!this.treeUtils.getFinishVisit()) {
            somma += this.addCurrentNode();
        }
        return somma;
    }

    /**
     * calcola il valore corrente e introduce nel buffer i nodi da valutare, se la visita è finita
     * sincronizza tutti i flussi sulla fine
     * @return il valore del nodo corrente
     */
    private Integer addCurrentNode(){
        Integer somma = 0;

        boolean riSincronizza = false;
        try {
            //poolFirst e non removeFirst perchè non vogliamo eccezioni
            currentNode = illimitateNodeBuffer.pollFirst();
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
                //System.out.println("somma : " + Thread.currentThread().getName());
                somma = processor.onerousFunction(currentNode.getValue());
            } else {
                /*quando ho visitato tutto l'albero*/
                if (this.flowSybchonizer.getParties() - this.flowSybchonizer.getNumberWaiting() == 1)
                    this.treeUtils.setFinishVisit(true);
                //aspetto che tutti i flussi si sincronizzino qui
                //System.out.println(" await " + Thread.currentThread().getName());
                this.flowSybchonizer.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            /*ci serve il reset della barriera perchè potrebbe essere che appena il primo flusso prende la radice
            * l'altro flusso potrebbe andare a cercare un nodo nel buffer comune, ma non c'è nulla e si
            * immobilizzerebbe sull'await ==> solo un thread lavora, allora io ogni tot resetto e se uno
            * si è bloccato sull'await l'altro lo sblocca (solo nel caso in cui ancora lavora)*/
            return 0;
        }
        return somma;
    }

}
