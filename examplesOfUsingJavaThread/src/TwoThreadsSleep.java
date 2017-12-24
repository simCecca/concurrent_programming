public class TwoThreadsSleep extends Thread{
    public void run(){ loop(); }

    public void loop(){
        //get a reference to the thread running
        Thread t = Thread.currentThread();
        String name = t.getName();

        System.out.println("just entered loop " + name);
        for(int i=0; i<10 ; i++){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("name= "+name);
        }
        System.out.println("about to leave loop "+name);
    }
    public static void main(String[] args){
        TwoThreadsSleep sleepy = new TwoThreadsSleep();
        sleepy.setName("my worker thread");
        sleepy.start();
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sleepy.loop();
    }
}
