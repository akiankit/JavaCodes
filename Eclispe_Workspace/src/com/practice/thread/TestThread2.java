package com.practice.thread;

public class TestThread2 {

    public static void main(String[] args) {
        Test2 test2 = new Test2();
        test2.start();
    }

}

class Test2 extends Thread {
    static int i = 1;

    @Override
    public void run() {
        while (i < 100) {
            System.out.println(i);
            i++;
            if (i % 10 == 0) {
                System.out.println("string");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
