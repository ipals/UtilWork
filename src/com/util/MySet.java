package com.util;

public class MySet {

	int[] a;
	int n;
	
	public MySet(int capacity){
		a = new int[capacity];
	}
	
	public int size(){
		return n;
	}
	
	public boolean isEmpty(){
		return n==0;
	}
}
