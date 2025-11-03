package com.fdmgroup.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.pojos.Foods;


import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class FoodMethods {
	// 1
	public static void performGETFood() {
		Response response = given()
				.contentType(ContentType.JSON)
				.log().all() // request information
			.when()
				.get("http://localhost:3000/foods")
			.then()
				.log().all() // response information
				.assertThat()
					.body("name",hasSize(4))
					.header("Content-type", is("application/json"))
					.statusCode(200)
				.extract().response();
	// Deserialize JSON response to List of Food objects
	List<Foods> foods = response.jsonPath().getList("",Foods.class);
	
	
	//Assertion 
	assertThat(foods,hasSize(4));
	List<String> names = new ArrayList<>();
	List<String> ids = new ArrayList<>();
	List<Double> prices = new ArrayList<>();
	for (Foods food : foods ) {
		ids.add(food.getId());
		names.add(food.getName());
		prices.add(food.getPrice());	
	}
	
	assertThat(ids,containsInAnyOrder("1","2","3","4"));
	assertThat(names,containsInAnyOrder("cucumber salad","french fries","soft drink","burgers"));
	assertThat(prices,containsInAnyOrder(2.5,5.5,5.5,9.55));
	
	//Print the result
	//System.out.println(response.log().body());
	//System.out.println(response.log().headers());
		
	}
	
	// 2
	public static void performPOSTFood(String id, String name, double price) {
		Foods postContent = new Foods(id, name, price);
		var response = given()
					.contentType(ContentType.JSON)
				.with()
					.body(postContent)
				.when()
					.post("http://localhost:3000/foods/")
				.then()
					.assertThat()
						.statusCode(201);
		
		//Print the result
		System.out.println(response.log().body());	
	}
	
	// 3
	public static void performPATCHFood(String id) {
		Foods postContent = new Foods(11.99);
		
		var response = given()
					.contentType(ContentType.JSON)
					.body(postContent)
				.when()
					.patch("http://localhost:3000/foods/" + id)
				.then()
					.assertThat()
						.statusCode(200);
		
		//Print the result
		System.out.println(response.log().body());
		
	}
	

}
