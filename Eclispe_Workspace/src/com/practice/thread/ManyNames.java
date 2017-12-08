package com.practice.thread;

public class ManyNames {

    public static void main(String[] args) {
        NameRunnable nr = new NameRunnable();
        Thread one = new Thread(nr);
        Thread two = new Thread(nr);
        Thread three = new Thread(nr);
        one.setName("one");
        two.setName("two");
        three.setName("three");
        one.start();
        two.start();
        three.start();
    }

}

class NameRunnable implements Runnable {
    
    public void run() {
        for(int x=1;x<=4;x++){
            System.out.println(Thread.currentThread().getName() + " " + x);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
}
