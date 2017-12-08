package com.codechef.practice;

public abstract class Fruit {
	private int color;
	public int getColor (){
		return color;
	}
	public void setColor(int color){
		this.color=color;
	}
	public abstract void eat ();

}