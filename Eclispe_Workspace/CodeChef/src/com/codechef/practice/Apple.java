package com.codechef.practice;

final class Apple extends Fruit {

	/**
	 * @param args
	 */
	public void eat (){
		System.out.println ("eating");
	}

	//public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println ("Hello World!!");
		
	//}
}

class Temp {
	public static void main(String[] args) {
		//TODO Auto-generated method stub
		System.out.println ("Hello World!!");
		Apple a = new Apple();
		a.setColor(1);
		System.out.print(a.getColor());
		a.eat();
	 }
}
