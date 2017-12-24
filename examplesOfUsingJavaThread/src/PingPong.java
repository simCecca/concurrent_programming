/**
 * second way
 */
public class PingPong implements Runnable{
    String word;  //what word to print
    int delay;    //how long the pause

    PingPong(String whatToSay, int delayTime) {
        this.word = whatToSay;
        this.delay = delayTime;
    }

    //runnable method
    public void run(){
        try{
            for(int i = 0; i<20 ; i++)
                System.out.print(word + " ");
                Thread.sleep(delay+500);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Runnable ping = new PingPong("ping",33);
        Runnable pong = new PingPong("PONG",100);
        new Thread(pong).start();
        new Thread(ping).start();
    }
}
