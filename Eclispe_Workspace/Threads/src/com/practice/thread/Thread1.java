package com.practice.thread;

public class Thread1 {

    public static void main(String[] args) {
        Numbers numbers = new Numbers();
        Thread t1 = new Thread(numbers,"thread1");
        Thread t2 = new Thread(numbers,"thread2");
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}


class Numbers implements Runnable {

    static int i=1;
    
    Object obj = new Object();
    @Override
    public void run() {
        synchronized (this) {
            while (i < 10) {
                try {
                    System.out.println(Thread.currentThread().getName() + " " + i++);
                    wait(i);
                } catch (InterruptedException e) {
                    System.out.println("Exception happend for thread="
                            + Thread.currentThread().getName());
                    e.printStackTrace();
                }
            }
        }
    }
    
}