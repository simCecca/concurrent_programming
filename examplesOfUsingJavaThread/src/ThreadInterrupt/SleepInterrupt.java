package ThreadInterrupt;

/**
 * per poter interrompere un thread si utilizza il metodo interrupt(), solo che noi
 * dialoghiamo con un oggetto thread che non è il vero f.d.e. ma è l'unico che può parlare
 * con la JVM, la quale gestisce i f.d.e. a questo punto una volta invocato il metodo interrupt()
 * stiamo instigando al suicidio un thread, ma nessuno meglio di lui sa quando può morire, quindi
 * in un momento in cui il suo suicidio non comporti problemi allo spazio di memoria degli altri thread
 * quindi: -mentre fa attesa, ad esempio una sleep -all'atto dell'invocazione di una sleep; -durante
 * l'esecuzione di una wait
 */
public class SleepInterrupt implements Runnable {
    public void run(){
        System.out.println("in run() - about to sleep 20 seconds");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            System.out.println("in run() - interrupted while sleeping");
            return;
        }
        System.out.println("in run() - doing stuff after nap");
        System.out.println("in run() - leaving normally");
    }

    public static void main(String[] args){
        SleepInterrupt si = new SleepInterrupt();
        Thread harakiriThread = new Thread(si);
        harakiriThread.start();
        // Be sure that the new thread gets a chance to
        // run for a while .
        try { Thread.sleep(2000); }
        catch ( InterruptedException x ) { }
        System.out.println("in main() - interrupting other thread");
        harakiriThread.interrupt();
        System.out.println("in main() - leaving");
    }

}
