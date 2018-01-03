package model.speedUp;

import model.adder.unbounded.UnboundedBufferBinaryTreeAdder;
import model.tree.structure.Node;
import model.tree.utils.CreateBinaryTree;

import java.util.LinkedList;
import java.util.List;
/**
 * misura e visualizza tutti i tempi delle varie implementazioni, analizzando il comportamento asintotico
 */
public class Measurenment {

    private List<Long> unboundedBufferResult = new LinkedList<>();
    private Node node = null;

    private CreateBinaryTree tree = new CreateBinaryTree();
    private UnboundedBufferBinaryTreeAdder unboundAdder;


    public void misura() throws InterruptedException {
        long initTime = 0;
        long lastTime = 0;
        for(int i=0 ; i<12 ; i++){
            /*ridefinire ogni volta l'UnboundedBufferBinaryTreeAdder è importante poichè una volta che
            * hai fatto Executor.shutdown giustamente se riaccedi all'Executor ti da un eccezione, il
            * problema è il tipo di eccezione che è utilizzata in più contesti java.util.concurrent.RejectedExecutionException*/
            this.unboundAdder = new UnboundedBufferBinaryTreeAdder();
            this.node = this.tree.treeCreate(i+1);
            initTime = System.currentTimeMillis();
            unboundAdder.computeOnerousSum(this.node);
            lastTime = System.currentTimeMillis();
            unboundedBufferResult.add(lastTime - initTime);
        }
        visualizza();
    }

    public void visualizza(){
        int i = 1;
        System.out.println("l'obiettivo è quello di misurare il comportamento asintotico delle varie" +
                "implementazioni, i tempi sono riportati in secondi \n(NB: 15 è la dimensione massima dopo la quale si ha StackOverflow)");
        System.out.println("UNBOUNDED BUFFER");
        for(Long current : unboundedBufferResult) {
            System.out.println(i + "    " +(current/1000.0));
            i++;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Measurenment misura = new Measurenment();
        misura.misura();
    }
}
