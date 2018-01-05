package model.adder.bounded;

import model.adder.BinaryTreeAdder;
import model.tree.structure.Node;
import model.tree.utils.CreateBinaryTreeBalanced;

import java.util.Deque;
import java.util.concurrent.*;

/**
 * progettare e testare una solizione che, senza compromettere il livello di parallelismo raggiunto
 * dalla soluzione Unbounded, superi l'idea dell'unico buffer illimitato ed utilizzi invece buffer di
 * dimensione limitata.
 *
 * ----------------------------------------------------------------------------------------------------
 * Framework per la decomposizione parallela ==> per la decomposizione dinamica
 * (visto i benefici ottenibili con scheduler dedicati che tengano
 esplicitamente conto della natura e delle relazioni intercorrenti
 tra i task sottomessi hanno meritato la progettazione di un intero Framework
 “Fork/Join” per la decomposizione parallela)

 il numero di task da creare normalmente per un problema di questo tipo non è banalmente
 prevedible; dipende direttamente dalla dimensione N del problema trattato.
 in sostanza la decomposizione in task viene rimandara a tempo dinamico ==> inizialmente task a grana
 più grossa che poi diventano a grana più fine.
 viene utilizzata una soglia SEQUENTIAL_THRESHOLD che mi va a classificare i task in piccoli ==> soluzione seriale
 o grandi ==> decomposizione parallela

 WORK STEALING:
 Ogni worker thread mantiene una propria scheduling queue La scheduling queue è acceduta

 LIFO: per il processamento “ordinario” dei propri task da parte dei worker  thread
 FIFO: per il reperimento di task degli altri worker thread da parte dei  worker thread altrimenti
 inattivi

 Il lavoro ordinario ed il work stealing insistono su parti opposte
 della Deque limitando la competizione
 si tratta di Deque che consentono ins./estr. concorrenti alle
 estremità opposte
 --------------------------------------------------------------------------------------------
 in questa parte dell'hw è richiestodi ragionare in questo modo e non di utilizzare il framework il
 quale utilizzo è previsto nella prossima implementazione
 */
public class BoundedBinaryTreeAdder implements BinaryTreeAdder{

    private int NCPU;
    private ExecutorService pool;
    private CompletionService<Integer> ecs;
    private CyclicBarrier barrier;

    /*per poter rubare dai buffer altrui, si deve avere un riferimento ai buffer
    * LinkedBlockingDeque poichè si è in un contesto multi-thread*/
    private LinkedBlockingDeque<Deque<Node>> tasksDeque;

    /*buffer del singolo thread*/
    private LinkedBlockingDeque<Node> limitateNodeBuffer;

    /*per la suddivisione dei buffer interni a taskDeque*/
    private CreateBinaryTreeBalanced tree;




    public BoundedBinaryTreeAdder(){
        this.NCPU = Runtime.getRuntime().availableProcessors();
        /*solito pool di thread di dimensioni NCPU*/
        this.pool = Executors.newFixedThreadPool(this.NCPU);
        this.ecs = new ExecutorCompletionService<>(pool);

        this.barrier = new CyclicBarrier(this.NCPU);
        this.limitateNodeBuffer = new LinkedBlockingDeque<>();
        this.tasksDeque = new LinkedBlockingDeque<>();

        this.tree = new CreateBinaryTreeBalanced();
    }

    @Override
    public int computeOnerousSum(Node root) {
        this.limitateNodeBuffer.add(root);
        int height = this.tree.getHeight(root);

        for(int i = 0; i<this.NCPU ; i++)
            this.ecs.submit(new BoundedBufferAdderTask(barrier,limitateNodeBuffer,height,tasksDeque));

        int somma = 0;
        try {
        for(int i = 0; i<this.NCPU ; i++)

                somma += this.ecs.take().get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();

            }finally {this.pool.shutdown();}

        return somma;
    }
}
