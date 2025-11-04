package com.fdmgroup.stepdefinitions;

import static org.hamcrest.Matchers.notNullValue;

import static org.junit.Assert.assertEquals;


import java.util.List;


import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;



import com.fdmgroup.pojos.Foods;
public class F1_Foods_StepDefinition {
	
	private String lastPatchedId;
	private double expectedPrice;
	private Response response;
	
	String createdName;
	double createdPrice;
	String createdId;
	

	@When("User sends a GET request to {string}")
	public void user_sends_a_get_request_to(String endpoint) {
		response = given()
				.contentType(ContentType.JSON)
				.log().all() // request information
			.when()
				.get(endpoint)
			.then()
				.assertThat()
					.header("Content-Type", containsString("application/json"))
				.extract().response();
	}

	@Then("The response status code should be displayed as {int}")
	public void the_response_status_code_should_be_displayed_as(Integer statuscode) {
		assertThat(response.getStatusCode(), is(statuscode));
	    
	}

	@Then("The response body should contain a list of foods")
	public void the_response_body_should_contain_a_list_of_foods() {
		// Deserialize JSON response to List of Food objects
		List<Foods> foods = response.jsonPath().getList("",Foods.class);
		assertThat(foods, hasSize(greaterThan(0)));
		
	}

	@Then("Each food item should include the fields {string}, {string}, and {string}")
	public void each_food_item_should_include_the_fields_and(String id, String name, String price) {
		List<Foods> foods = response.jsonPath().getList("",Foods.class);
		for (Foods food : foods) {
	        assertThat("Food ID should not be null", food.getId(), is(notNullValue()));
	        assertThat("Food Name should not be null", food.getName(), is(notNullValue()));
	        assertThat("Food Price should not be null", food.getPrice(), is(notNullValue()));
	        
		}
		response.then().log().all(); // print response information
		
	}
	

	@When("User sends a POST request to {string} with id {string}, name {string} and price {double}")
	public void user_sends_a_post_request_to_with_name_and_price(String endpoint, String itemid, String itemname, Double itemprice) {
		Foods newfood = new Foods(itemid, itemname, itemprice);
		createdId = itemid;
		createdName = itemname;
	    createdPrice = itemprice;
		response = given()
                .contentType(ContentType.JSON)
            .with()
                .body(newfood)
                .log().all()
            .when()
                .post(endpoint)
            .then()
                .log().all()
                .assertThat()
                    .header("Content-Type", containsString("application/json"))
                .extract().response();
	}
	
	@Then("The response body should contain the created food details")
	public void the_response_body_should_contain_the_created_food_details() {
		Foods added = response.as(Foods.class);

	    assertThat(added.getId(), is(createdId));
	    assertThat(added.getName(), is(createdName));
	    assertThat(added.getPrice(), is(createdPrice));
	}

	@Then("The newly created food item should be present in the list")
	public void the_newly_created_food_item_should_be_present_in_the_list() {
		List<Foods> foods = response.jsonPath().getList("", Foods.class);
	    boolean isFound = false;

	    for (Foods food : foods) {
	        if (food.getName().equals(createdName) &&
	            Double.compare(food.getPrice(), createdPrice) == 0) {
	            isFound = true;
	            break;
	        }
	    }
	    assertThat("Newly created food item should be present in the list", isFound, is(true));
	}
	@When("User wants to update the price of the food item to {string} with id {string}")
	public void user_wants_to_update_the_price_of_the_food_item_to_with_id(String price, String id) {
		expectedPrice = Double.parseDouble(price);
	    lastPatchedId = id;
	    System.out.println(id);
	    Foods updatedFood = new Foods();
	    updatedFood.setPrice(expectedPrice);

	    
	    response = given()
	            .contentType(ContentType.JSON)
	            .pathParam("id", id)
	            .body(updatedFood)
	        .when()
	            .patch("http://localhost:3000/foods/{id}")
	        .then()
	        	.log().all()
	            
	            .extract().response();
	}

	@Then("The price of the food item should be updated")
	public void the_price_of_the_food_item_should_be_updated() {
		// Re-GET to verify it actually persisted
	    Foods reloaded = given()
	            .contentType(ContentType.JSON)
	            .pathParam("id", lastPatchedId)
	        .when()
	            .get("http://localhost:3000/foods/{id}")
	        .then()
	            
	            .extract().as(Foods.class);

	    assertThat("Updated food should not be null", reloaded, notNullValue());
	    
	    assertEquals(expectedPrice, reloaded.getPrice(), 1e-6);
	    System.out.println("Updated food: " + reloaded.getName() + " | New price: " + reloaded.getPrice());
	}
	
	

}
