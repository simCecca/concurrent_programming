package ProducerConsumerCompetition;

/**
 * Due tipologie di problemi di sincronizzazione:
  gli accessi a CubbyHole devono essere sincronizzati
 di modo che Producer e Consumer non effettuino
 scritture o letture multiple
  gli accessi devono essere coordinati di modo che
 
 
 Consumer consumi solo quando esiste qualcosa da
 consumare
 Producer produca solo quando esiste lo spazio per
 accogliere il valore prodotto
 */
public class CubbyHole {
    private int contents;
    private boolean available = false;

    /**
     * Java supporta la cooperazione tramite
     primitive wait() e signal() nello stile delle
     variabili condizione di C.A.R. Hoare
     
     
     
     wait() e notify() in java
     Le primitive in questione sono metodi dalla
     classe Object
     I thread possono sincronizzarsi sul verificarsi
     di certe condizioni/eventi
     * @return
     */

    public synchronized int get() {
        while(!available) {
            try {
        // wait for Producer to put value
                wait();
            } catch (InterruptedException e) {}
        }
        available = false;
       // notify Producer that value has been retrieved
        notifyAll();
        return contents;
    }

    public synchronized void put(int value) {
        while (available) {
            try {
        // wait for Consumer to get value
                wait();
            } catch (InterruptedException e) {
            }
        }
        contents = value;
        available = true;
        // notify Consumer that value has been set
        notifyAll();
    }
}
