
package com.initial.util;

public class ThreadTime {

    public static void main(String[] args) {
        CalculateTime ct = new CalculateTime();
        Thread t = new Thread(ct);
        ct.setStartTime(System.currentTimeMillis());
        t.start();
        getPrimesLessThanNSieve(100000000);
        ct.setEndTime(System.currentTimeMillis());
    }

    static public int getPrimesLessThanNSieve(int n) {
        int[] arr = new int[n + 1];
        for (int i = 1; i < n + 1; i++) {
            arr[i] = 1;
        }
        for (int i = 3; i <= n; i += 2) {
            for (int j = 3 * i; j <= n; j += (i + i)) {
                arr[j] = 0;
            }
        }
        int count = 1;
        for (int i = 3; i <= n; i += 2) {
            if (arr[i] == 1)
                count++;
        }
        return count;
    }
}

class CalculateTime implements Runnable {

    private long startTime;

    private long endTime;

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public void run() {
        while (endTime == 0 && System.currentTimeMillis() - startTime < 1000) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (System.currentTimeMillis() - startTime > 1000) {
            System.out.println("Function is taking more than 1 sec");
        } else {
            System.out.println("startTime="+startTime+" endTime="+endTime);
            System.out.println("Total time taken=" + (endTime - startTime));
        }
        System.out.println("Thread is going to die");
    }

}
