package model.speedUp;

import bounded.BoundedBinaryTreeAdder;
import fork_join.ForkJoinBinaryTreeAdder;
import serial.SerialBinaryTreeAdder;
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
    private List<Long> forkJoinResult = new LinkedList<>();
    private Node node = null;

    private CreateBinaryTreeBalanced tree = new CreateBinaryTreeBalanced();
    private UnboundedBufferBinaryTreeAdder unboundAdder;
    private BoundedBinaryTreeAdder boundedAdder;
    private ForkJoinBinaryTreeAdder forkJoinAdder;
    private SerialBinaryTreeAdder serialAdder = new SerialBinaryTreeAdder();


    public void misura() {
        //warm up
        iteration(16);
        this.listClear();
        //real measurenment
        iteration(15);
        visualizza(15);
    }

    private void iteration(int time) {
        for(int i=0 ; i<time ; i++){

            this.node = this.tree.treeCreate(i+1);
            this.unboundedIteration();
            this.boundedIterator();
            this.serialIterator();
            this.forkJoinIterator();
        }
    }

    private void listClear(){
        this.serialResult.clear();
        this.boundedBufferResult.clear();
        this.unboundedBufferResult.clear();
        this.forkJoinResult.clear();
    }

    private void visualizza(int time){

        System.out.println("l'obiettivo è quello di misurare il comportamento asintotico delle varie" +
                "implementazioni, i tempi sono riportati in secondi; \n" +
                "dopo una prima fase di warm-up; in particolare si parte da un altezza di 1 \n" +
                "fino ad arrivare a 15 (32677 nodi), nel file.txt sono riportati i risultati di una esecuzione" +
                "con altezza 20 (1048575 nodi) eseguiti su di una macchina dual-core");
        System.out.println("| TREE HEIGHT |  | UNBOUNDED BUFFER |   | BOUNDED BUFFER |    | SERIAL |       | FORK JOIN |   ");
        for(int i = 0; i<time ; i++) {
            System.out.println("            " + (i+1) + "                " + (this.unboundedBufferResult.get(i)/1000.0) +
            "              " + (this.boundedBufferResult.get(i)/1000.0) + "              " + (this.serialResult.get(i)/1000.0) +
            "               " + (this.forkJoinResult.get(i)/1000.0));
        }
        double serialLastResult = this.serialResult.get(time-1)/1000.0;
        double speedUnbounded = serialLastResult / (this.unboundedBufferResult.get(time-1)/1000.0);
        double speedBounded = serialLastResult /(this.boundedBufferResult.get(time-1)/1000.0);
        double speedForkJoin = serialLastResult / (this.forkJoinResult.get(time-1)/1000.0);
        System.out.println("\nspeed-up ottenuti (su hight = " + time + ") : UNBOUNDED BUFFER  " + speedUnbounded +
        "  BOUNDED BUFFER  " + speedBounded + "  FORK JOIN  " + speedForkJoin);
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

    private void forkJoinIterator(){
        long initTime = 0;
        long lastTime = 0;
        this.forkJoinAdder = new ForkJoinBinaryTreeAdder();
        initTime = System.currentTimeMillis();
        forkJoinAdder.computeOnerousSum(this.node);
        lastTime = System.currentTimeMillis();
        forkJoinResult.add(lastTime - initTime);
    }



    public static void main(String[] args) throws InterruptedException {
        Measurenment misura = new Measurenment();
        misura.misura();
    }
}
