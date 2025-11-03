package com.fdmgroup.stepdefinitions;

import static io.restassured.RestAssured.given;
import com.fdmgroup.pojos.Comments;
import io.restassured.http.ContentType;
import java.util.List;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;



public class F2_Comments_StepDefinition {
	
	Response response;
	String deletedCommentid;
	String createdBody;
	@When("User creates a new comment with id {string}, userid {int} body {string} and foodId {int}")
	public void user_creates_a_new_comment_with_id_userid_body_and_food_id(String id, Integer userid, String body, Integer foodId) {
		
		Comments commentContent = new Comments(id, userid, body, foodId);
		response = given()
					.contentType(ContentType.JSON)
					.log().all()
				.with()
					.body(commentContent)
				.when()
					.post("/comments")
				.then()
					.log().all()
					.assertThat()
						.statusCode(201)
				.extract().response();
		
		createdBody = body;

	}

	@Then("The newly created comment should be present in the list")
	public void the_newly_created_comment_should_be_present_in_the_list() {
	    Comments comment = response.as(Comments.class);
	    assertThat("The comment is not present",comment.getBody(), is(createdBody));
	}
	
	@When("User deletes the comment based on comment id {string}")
	public void user_deletes_the_comment_based_on_comment_id(String id) {
		deletedCommentid = id;
		response = given()
				.contentType(ContentType.JSON)
				.log().all()
		.when()
			.delete("/comments/" + id)
		.then()
			.log().all()
			.assertThat()
				.statusCode(200)
			.extract().response();
	
	System.out.println(response);
	}

	@Then("The comment should not be present in the list")
	public void the_comment_should_not_be_present_in_the_list() {
	    List<Comments> remaining = given()
	            .contentType(ContentType.JSON)
	            .queryParam("id", deletedCommentid)      
	        .when()
	            .get("/comments")
	        .then()
	            .log().all()
	            .statusCode(200)
	            .extract()
	            .jsonPath().getList("", Comments.class);

	 
	    assertThat("Comment should be deleted", remaining.size(), is(0));
	    System.out.println("comment successfully deleted and no longer visible.");
	    
	}
	
	@When("User wants to get the comment information based on the user id {int} and the food id {int}")
	public void user_wants_to_get_the_comment_information_based_on_the_user_id_and_the_food_id(Integer userid, Integer foodId) {
		response = given()
				.contentType(ContentType.JSON)
				.log().all()
			.with()
				.queryParam("userid",userid)
				.queryParam("foodId",foodId)
			.when()
				.get("/comments")
			.then()
				.log().all()
				.assertThat()
					.statusCode(200)
				.extract().response();
		
	}

	@Then("The body of the message should match {string}")
	public void the_body_of_the_message_should_match(String expectedBody) {
		Comments[] comments = response.as(Comments[].class);
	    assertThat("No comments returned", comments.length, greaterThan(0));
	    assertThat("Body does not match", comments[0].getBody(), is(expectedBody));
	    System.out.println("Expected: " + expectedBody);
	    System.out.println("Actual: " + comments[0].getBody());
	}




}
