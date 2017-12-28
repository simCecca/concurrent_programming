package ParallelDecomposizion.StaticAndUniversal;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Executors;

public class ArraySum {
    /*l'array deve essere di dimensioni enormi, da far si che i costi di gestione siano irrisori
    * confrontati con il costo dell'operazione*/
    private static int[] array= {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private final static int NCPU = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*faccio un pool di thread pari al numero di processori fisici*/
        ExecutorService pool = Executors.newFixedThreadPool(NCPU);
        List<Future<Integer>> pending = new LinkedList<Future<Integer>>();
        int sliceSize = array.length / NCPU;

        /*elaborazione parallela dei sottoproblemi*/
        int l = 0;
        for (int w = 0; w<NCPU ; w++)
        {
            int h = Math.min(l + sliceSize, array.length);
            pending.add(pool.submit(new ArraySumTask(array,l,h)));
            //non è l = h + 1
            l = h;
        }
        int sum = 0;
        //riomposizione dei risultati parziali
        for(Future<Integer> f: pending)
            sum += f.get();
        System.out.println("the sum is: " + sum);
        pool.shutdown();

    }
}
