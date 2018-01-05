package model.speedUp;

import model.adder.bounded.BoundedBinaryTreeAdder;
import model.adder.serial.SerialBinaryTreeAdder;
import model.adder.unbounded.UnboundedBufferBinaryTreeAdder;
import model.tree.structure.Node;
import model.tree.utils.CreateBinaryTreeBalanced;

import java.util.LinkedList;
import java.util.List;
/**
 * misura e visualizza tutti i tempi delle varie implementazioni, analizzando il comportamento asintotico
 */
public class Measurenment {

    private List<Long> unboundedBufferResult = new LinkedList<>();
    private List<Long> boundedBufferResult = new LinkedList<>();
    private List<Long> serialResult = new LinkedList<>();
    private Node node = null;

    private CreateBinaryTreeBalanced tree = new CreateBinaryTreeBalanced();
    private UnboundedBufferBinaryTreeAdder unboundAdder;
    private BoundedBinaryTreeAdder boundedAdder;
    private SerialBinaryTreeAdder serialAdder = new SerialBinaryTreeAdder();


    public void misura() {
        //warm up
        iteration(15);
        this.listClear();
        //real measurenment
        iteration(12);
        visualizza(12);
    }

    private void iteration(int time) {
        for(int i=0 ; i<time ; i++){

            this.node = this.tree.treeCreate(i+1);
            this.unboundedIteration();
            this.boundedIterator();
            this.serialIterator();
        }
    }

    private void listClear(){
        this.serialResult.clear();
        this.boundedBufferResult.clear();
        this.unboundedBufferResult.clear();
    }

    private void visualizza(int time){

        System.out.println("l'obiettivo è quello di misurare il comportamento asintotico delle varie" +
                "implementazioni, i tempi sono riportati in secondi; \n" +
                "dopo una prima fase di warm-up; in particolare si parte da un altezza di 1 \n" +
                "fino ad arrivare a 12 quindi 4095 nodi (NB: per come vengono assegnati i valori ai nodi, 13 è l'altezza massima dopo la quale si ha StackOverflow)");
        System.out.println("| TREE HEIGHT |  | UNBOUNDED BUFFER |   | BOUNDED BUFFER |    | SERIAL |  ");
        for(int i = 0; i<time ; i++) {
            System.out.println("            " + (i+1) + "                " + (this.unboundedBufferResult.get(i)/1000.0) +
            "              " + (this.boundedBufferResult.get(i)/1000.0) + "              " + (this.serialResult.get(i)/1000.0));
        }
    }

    private void unboundedIteration(){
        /*ridefinire ogni volta l'UnboundedBufferBinaryTreeAdder è importante poichè una volta che
         * hai fatto Executor.shutdown giustamente se riaccedi all'Executor ti da un eccezione, il
         * problema è il tipo di eccezione che è utilizzata in più contesti java.util.concurrent.RejectedExecutionException*/
        long initTime = 0;
        long lastTime = 0;
        this.unboundAdder = new UnboundedBufferBinaryTreeAdder();
        initTime = System.currentTimeMillis();
        unboundAdder.computeOnerousSum(this.node);
        lastTime = System.currentTimeMillis();
        unboundedBufferResult.add(lastTime - initTime);
    }

    private void boundedIterator(){
        long initTime = 0;
        long lastTime = 0;
        this.boundedAdder = new BoundedBinaryTreeAdder();
        initTime = System.currentTimeMillis();
        boundedAdder.computeOnerousSum(this.node);
        lastTime = System.currentTimeMillis();
        boundedBufferResult.add(lastTime - initTime);
    }

    private void serialIterator(){
        long initTime = 0;
        long lastTime = 0;
        //this.serialAdder = new SerialBinaryTreeAdder();
        initTime = System.currentTimeMillis();
        serialAdder.computeOnerousSum(this.node);
        lastTime = System.currentTimeMillis();
        serialResult.add(lastTime - initTime);
    }



    public static void main(String[] args) throws InterruptedException {
        Measurenment misura = new Measurenment();
        misura.misura();
        misura.misura();
        //andamento solo con 12, ogni tanto bounded è seriale

    }
}
