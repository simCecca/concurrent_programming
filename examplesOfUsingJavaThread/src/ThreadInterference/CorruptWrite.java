package ThreadInterference;

/**
 * Pericolo di Interferenza
 
 
 
 Diversi thread possono condividere degli
 oggetti a cui accedono concorrentemente
 E’ necessario sincronizzare gli accessi agli
 oggetti condivisi
 In caso contrario si possono verificare
 interferenze ed errori dipendenti dalla
 particolare s.e.a. o seq. di interleaving
 adottata dalla JVM
 */
public class CorruptWrite extends Object{
    private String fname;
    private String lname;

    /**
     * setta i due parametri fname e lname e per essere sicuri che niente avvenga a livello
     * atomico introduciamo dei tempi di attesa.
     * @param fistdName
     * @param lastName
     */
    public void setNames(String fistdName, String lastName){
        print("entering setNames()");
        fname = fistdName;
        //a thread might be swapped out here....
        if(fname.length() < 5)
        {
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
            {
                try {Thread.sleep(2000);} catch( InterruptedException x){ }
            }
        lname = lastName;
        print("leaving setName() - " + lname + ", " + fname);
    }

    public static void print(String msg) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + ": " + msg);
    }

    public static void main(String[] args) {
        final CorruptWrite cw = new CorruptWrite();
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
