package model.adder.fork_join;

import model.adder.BinaryTreeAdder;
import model.tree.structure.Node;
import model.tree.utils.CreateBinaryTreeBalanced;

import java.util.concurrent.ForkJoinPool;

/**
 * Java Fork/Join Framework:
 * Principali 4 classi di interesse in java.util.concurrent:
 * - Per gli executor service: ForkJoinPool: specializza ExecutorService
 * - Per i task: ForkJoinTask: classe base astratta
 *      Per i task che restituiscono un valore  RecursiveTask<V>: specializza (ma non implementa) Callable<V>
        Per i task che non restituiscono un valore RecursiveAction: specializza (ma non implementa) Runnable
    ForkJoinPool realizza l'ExecutorService dove per default il numero di thread è pari al numero di cpu
    fisiche disponibili

 fork: serve a decomporre i task a tempo dinamico
 join: attende la terminazione di un task (ritorna il risultato)

 */
public class ForkJoinBinaryTreeAdder implements BinaryTreeAdder{



    @Override
    public int computeOnerousSum(Node root) {
      /*conviene mettere il pool direttamente in computeOnerousSum perchè tanto il pool deve essere
       * reinizializzato dopo ogni shutdown ed essenzialmente c'è solo questa variabile */
      ForkJoinPool pool = new ForkJoinPool();
      int somma = 0;
      try {

         int numeroNodi = new CreateBinaryTreeBalanced().getNodeNumber(root);
         int nodiMancanti = numeroNodi / Runtime.getRuntime().availableProcessors();
          /*l'invoke ritorna il risultato del task sottomesso*/
              somma += pool.invoke(new ForkJoinBinaryTreeTask(root,nodiMancanti));
      }
      finally{ pool.shutdown(); }

      return somma;
    }
}
