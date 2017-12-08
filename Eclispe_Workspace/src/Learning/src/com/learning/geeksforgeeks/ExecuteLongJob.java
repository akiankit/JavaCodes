package com.learning.geeksforgeeks;

public class ExecuteLongJob {

	private static boolean interruputTask = false;
	
	private static class MyTask extends Thread{
		public void run(){
			while(!interruputTask){
				//Do some time consuming steps.
				// Step1
				// Step2
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		//Start executing the long running task.
		Thread task = new MyTask();
		synchronized (ExecuteLongJob.class) {
			task.start();
			// Wait for 5 seconds and then interrupt the task.
			Thread.sleep(5000L);
			interruputTask = true;
			
			//Wait for thread to expire
			task.join();
		}
		
	}

}
