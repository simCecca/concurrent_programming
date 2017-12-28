package ParallelDecomposizion.StaticAndUniversal;

/**
 * Sommare tutti gli elementi di una array di enormi
 dimensioni
 Si utilizzano
 
 
 
 Per questo esempio molto semplice:
 
 
 Executor per ottenere un pool di thread
 Future per contenere i risultati parziali
 numero di task (Callable e Future) creati
  prevedibile a priori
  numero di task creati == numero di CPU fisiche
  L'overhead dovuto alla creazione dei task è ottimale
 la parallelizzazione ha senso per array di dimensioni sufficientemente
 grandi da rendere questi costi relativamente trascurabili


 statica = è possibile decomporla in sottoproblemi a tempostatico
 universale = la ricerca del risultato finale è conclusa quando sono noti tutti i parziali
 */

import java.util.concurrent.Callable;
public class ArraySumTask implements Callable<Integer> {
    private int []array;
    private int low,high;

    public ArraySumTask(int []array,int low,int high){
        this.array = array;
        this.low = low;
        this.high = high;
    }

    /**
     * Callable<V> è un interfaccia che ha solo un metodo "V call();" che corrisponde al
     * metodo run() dell'interfaccia Runnable, solamente che in questo caso possiamo
     * ritornare una valore, di tipo V (generics)
     * @return
     */
    public Integer call(){
        int result = 0;
        for(int i = this.low; i<this.high ;i++){
            System.out.println("current number: " + this.array[i]);
            result += this.array[i];
        }

        return result;
    }

}
