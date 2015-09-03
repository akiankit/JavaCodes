package com.practice.thread;

public class PrintEvenOdd {
	public static void main(String[] args) throws InterruptedException {
		Boolean lock = new Boolean(false);
		int max = 5;
		Thread thread1 = new Thread(new Odd(lock, max), "Odd");
		thread1.start();
		Thread thread2 = new Thread(new Even(lock, max), "Even");
		thread2.start();
		thread1.join();
		thread2.join();
		System.out.println("End");
	}

}

class Even implements Runnable {
	Boolean shared;
	int max;

	public Even(Boolean shared, int max) {
		this.shared = shared;
		this.max = max;
	}

	public void run() {
		synchronized (shared) {
			try {
				shared.wait(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (int i = 2; i <= max; i += 2) {
				if (i > max) {
					shared.notify();
					break;
				}
				System.out.println(Thread.currentThread().getName() + " " + i);
				try {
					shared.notify();
					shared.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

class Odd implements Runnable {
	Boolean shared;
	int max;

	public Odd(Boolean shared, int max) {
		this.shared = shared;
		this.max = max;
	}

	public void run() {
		synchronized (shared) {
			for (int i = 1; i <= max; i += 2) {
				if (i > max) {
					shared.notify();
					break;
				}
				System.out.println(Thread.currentThread().getName() + " " + i);
				try {
					shared.notify();
					shared.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
