package com.fdmgroup.restassured;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import com.fdmgroup.pojos.Comments;

import io.restassured.http.ContentType;

public class MessageMethods {
	
		// 4
		public static void performPOSTComment(String id, int userid, String body, int foodId) {
			Comments postContent = new Comments(id, userid, body, foodId);
			var response = given()
						.contentType(ContentType.JSON)
					.with()
						.body(postContent)
					.when()
						.post("http://localhost:3000/comments/")
					.then()
						.assertThat()
							.statusCode(201);
			
			//Print the result
			System.out.println(response.log().body());	
		}
		
		// 5
		public static void performDELETEComment(String id) {
			var response = given()
					.contentType(ContentType.JSON)
			.when()
				.delete("http://localhost:3000/comments/" + id)
			.then()
				.assertThat()
					.statusCode(200);
		//Print the result
		System.out.println(response.log().body());
				
		}
		
		// 6
		public static void performGETPostQueryParameter(int userid, String body, int foodId) {
			Comments[] comments = given()
					.contentType(ContentType.JSON)
				.with()
					.queryParam("userid",userid)
					.queryParam("foodId",foodId)
				.when()
					.get("http://localhost:3000/comments/")
				.then()
					.assertThat()
						.body("author", hasItem(body))
					.extract().as(Comments[].class);
			//Perform assertion on Deserialize JSON objects
			assertThat(comments.length,is(1));
			List<String> bodys = new ArrayList<>();
			for (Comments comment : comments ) {
				bodys.add(comment.getBody());
			}
			assertThat(bodys,hasItem(body));
					
		}
		

}
