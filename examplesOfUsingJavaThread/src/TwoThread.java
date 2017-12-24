/**
 * there are two ways to create thread in java;
 * first way (not so good)
 */
public class TwoThread extends Thread{
    public void run(){
        for (int i =0; i<10 ; i++)
            System.out.println("Thread");
    }

    public static void main(String[] args) {
        TwoThread first = new TwoThread();
        first.start();

        for (int i = 0; i < 10; i++)
            System.out.println("Main");
    }
}
