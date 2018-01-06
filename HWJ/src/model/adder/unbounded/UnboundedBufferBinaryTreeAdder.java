package model.adder.unbounded;

import model.adder.BinaryTreeAdder;
import model.tree.structure.Node;
import model.tree.utils.BinaryTreeUtils;

import java.util.Deque;
import java.util.concurrent.*;

/**
* Decomposizione Statica ed Esistenziale:
 * sono dati un certo numero di Solver per un dato
 problema, ciascuno restituisce un valore di tipo Result
 i Solver cercano la soluzione concorrentemente
 Si usano in particolare i “CompletionService”
 (CompletionService è un interfaccia)
 CompletionService >> ExecutorCompletionService >>
 Oltre a
 
 Executor
 Future

 java.util.concurrent. CompletionService:
 Interfaccia che permette di disaccoppiare la
 sottomissione di nuovi task da eseguire, dalla
 consumazione dei corrispondenti risultati
 I risultati sono consumati nell’ordine temporale in
 cui i task sono completati, indipendentemente
 dall’ordine di sottomissione
*
* */
public class UnboundedBufferBinaryTreeAdder implements BinaryTreeAdder {

    private int NCPU;
    private ExecutorService pool;

    /*descrizione sopra*/
    private CompletionService<Integer> ecs;

    /*Utile per la decomposizione di problemi in sottoproblemi da risolvere concorrentemente
    * è CyclicBarrier il quale consente di fermare e sincronizzare dei thread presso una
     * barriera in attesa che tutti i partecipanti la raggiungono
     * I sottoproblemi si affidano a thread distinti La barriera serve per sincronizzarsi
     * sulla loro fine e ricomporne i risultati*/
    private CyclicBarrier flowSybchronizer;

    /*java.util.Deque: è una double ended queue, quindi supporta insrimenti ed estrazioni
    * efficienti da ambo gli estremi, ovviamente in un contesto multi Thread, nel nostro contesto
    * è richiesta una versione bloccante (ottima per tecniche di workstealing) ==> java.util.concurrent.BlockingQueue
     * in particolare è richiesta una coda illimitata ==> java.util.concurrent. LinkedBlockingQueue*/
    private Deque<Node> illimitateNodeBuffer;

    //per la stopping condition dei task
    private BinaryTreeUtils treeUtils;


    public UnboundedBufferBinaryTreeAdder(){
        this.NCPU = Runtime.getRuntime().availableProcessors();
        this.pool = Executors.newFixedThreadPool(NCPU);
        this.ecs = new ExecutorCompletionService<Integer>(pool);
        /*CyclicBarrier(int parties) parties = NCPU essendo questi il numero di flussi in gioco */
        this.flowSybchronizer = new CyclicBarrier(NCPU);
        /*bloccante & illimitata*/
        this.illimitateNodeBuffer = new LinkedBlockingDeque<>();
        this.treeUtils = new BinaryTreeUtils();
    }

    /*essenzialmente un mix tra primo esempio e secondo di pc-18 */
    @Override
    public int computeOnerousSum(Node root) {
        /*inizializzo la coda sincronizzo e faccio partire i NCPU flussi (i solvers)*/
        this.illimitateNodeBuffer.add(root);
        this.treeUtils.setFinishVisit(false);
        /*sottomissione dei task*/
        for(int i = 0; i<this.NCPU ; i++)
            /*Future<V> submit(Callable<V> task) non mi salvo i feature in una lista semplicemente
            * perchè tutto è finito quando tutti i future hanno ottenuto i dati quindi non lascio
            * nessuno in esecuzione*/
        this.ecs.submit(new UnboundedBufferSumTask(flowSybchronizer,illimitateNodeBuffer,this.treeUtils));

        int somma = 0;
        /*somma dei risultati dei vari solvers*/
        try {for(int i = 0; i<this.NCPU ; i++)
            /*try e catch per take()*/
                somma += this.ecs.take().get();

        }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
            finally {
            /*di ExecutorService, shutdown dei task eseguiti e nessun nuovo task è accettato*/
            pool.shutdown();
        }
        return somma;
    }
}
