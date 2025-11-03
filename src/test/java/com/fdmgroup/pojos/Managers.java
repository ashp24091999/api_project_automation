package com.fdmgroup.pojos;

import java.util.List;

public class Managers {
	private int id;
	private double salary;
	private int age;
	private String name;
	private List<Staff> staffs;
	public Managers() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Managers(int id, double salary, int age, String name, List<Staff> staffs) {
		super();
		this.id = id;
		this.salary = salary;
		this.age = age;
		this.name = name;
		this.staffs = staffs;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Staff> getStaffs() {
		return staffs;
	}
	public void setStaffs(List<Staff> staffs) {
		this.staffs = staffs;
	}
	
	
	

}
