package ThreadInterference;

/**
 * La JVM associa un semaforo binario in stile
 competitivo (mutex ricorsivo) ad ogni oggetto
 Utilizzando la parola chiave synchronized, un
 blocco di codice può entrare in sezione critica
 “bloccando” un qualsiasi oggetto
 synchronized (object) {}
 anche interi metodi possono essere eseguiti in
 sezione critica
 modifier synchronized type method_name() {
 ...
 }
 */
public class CorruptWriteSynchronized {
        private String fname;
        private String lname;
        public synchronized void setNames(String firstName,
                                          String lastName) {
            print("entering setNames()");
            fname = firstName;
// A thread might be swapped out here...
            if ( fname.length() < 5 ) {
                try {Thread.sleep(1000);} catch( InterruptedException x){ }
            } else {
                try {Thread.sleep(2000);} catch( InterruptedException x){ }
            }
            lname = lastName;
            print("leaving setNames() - " + lname + ", " + fname);
        }

    public static void print(String msg) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + ": " + msg);
    }

    public static void main(String[] args) {
        final CorruptWriteSynchronized cw = new CorruptWriteSynchronized();
        //classe anonima, implementa Runnable
        Runnable runA = new Runnable() {
            public void run() { cw.setNames("George","Washington");}
        };
        Thread threadA = new Thread(runA, "threadA");
        threadA.start();
        try { Thread.sleep(200); } catch ( InterruptedException x) { }
        Runnable runB = new Runnable() {
            public void run() { cw.setNames("Abe","Lincoln");
            }
        };
        Thread threadB = new Thread(runB, "threadB");
        threadB.start();
    }
}
