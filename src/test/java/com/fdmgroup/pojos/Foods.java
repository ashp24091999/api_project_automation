package com.fdmgroup.pojos;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Foods {
	private String id;
	private String name;
	private double price;
	public Foods() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Foods(double price) {
		super();
		this.price = price;
	}
	public Foods(String id, String name, double price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	

}
