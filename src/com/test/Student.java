package com.test;

public class Student {

	private String name;
	private int no;
	private int phonenum;

    public Student(int phonenum) {
        this.phonenum = phonenum;
    }

    public Student(String name, int no) {
        this.name = name;
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public int getNo() {
        return no;
    }
}
