package com.fdmgroup.stepdefinitions;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fdmgroup.pojos.Managers;
import com.fdmgroup.pojos.Staff;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
public class F3_Managers_StepDefinition {
	
	Response response;
	int managerId;
	
	
	private int beforeStaffCount;
	@When("The user updates all staff salaries by {int} percent whose manager id {int}")
	public void the_user_updates_all_staff_salaries_by_percent_whose_manager_id(Integer percent, Integer mid) {
		managerId = mid;  
		List<Managers> hits = given()
	            .contentType(ContentType.JSON)
	            .log().all()
	            .queryParam("id", mid)
	        .when()
	            .get("/managers")            
	        .then()
	            .log().all()
	            .statusCode(200)
	            .extract()
	            .jsonPath()
	            .getList("", Managers.class);

	    assertThat("No manager found for id=" + mid, hits.size(), is(1));
	    Managers manager = hits.get(0);
	    
	    double factor = 1 + (percent / 100.0);
	    for (Staff s : manager.getStaffs()) {
	        s.setSalary(s.getSalary() * factor);
	    }

	    Map<String, Object> patchBody = new HashMap<>();
	    patchBody.put("staffs", manager.getStaffs());
	    response = given()
	            .contentType(ContentType.JSON)
	            .accept(ContentType.JSON)
	            .log().all()
	            .pathParam("id", manager.getId())     
	            .body(patchBody)
	        .when()
	            .patch("/managers/{id}")
	        .then()
	            .log().all()
	            .statusCode(200)
	            .extract().response();
	
	}
	

	@Then("All the staff information gets updated")
	public void all_the_staff_information_gets_updated_bulk() {
		
	    Managers[] after = given()
	            .contentType(ContentType.JSON)
	            .log().all()
	            .queryParam("id", managerId)
	        .when()
	            .get("/managers")
	        .then()
	            .log().all()
	            .statusCode(200)
	            .extract().as(Managers[].class);

	    assertThat("Return exactly one manager after update", after.length, is(1));
	    Managers updated = after[0];
	    assertThat("Manager should not be null", updated, notNullValue());
	    assertThat("Staff list should not be empty", updated.getStaffs().size(), greaterThan(0));

	    for (Staff s : updated.getStaffs()) {
	        assertThat("Updated salary should be > 0", s.getSalary(), greaterThan(0.0));}
	}
	
	@When("The user wants to delete information for staff with id {int} whose manager name is {string}")
	public void the_user_wants_to_delete_information_for_staff_with_id_whose_manager_name_is(int staffId, String managerName) {
	    
	    List<Managers> matches = given()
	            .contentType(ContentType.JSON)
	            .log().all()
	            .queryParam("name", managerName)
	        .when()
	            .get("/managers")
	        .then()
	            .log().all()
	            .statusCode(200)
	            .extract()
	            .jsonPath().getList("", Managers.class);
	    Managers target = null;
	    for (Managers m : matches) {
	        if (m.getName().equalsIgnoreCase(managerName)) {
	            target = m;
	            break;
	        }
	    }
	    assertThat("Manager with name '" + managerName + "' not found", target, notNullValue());

	    beforeStaffCount = target.getStaffs().size();

	    boolean removed = target.getStaffs().removeIf(s -> s.getId() == staffId);
	    assertThat("No staff with id=" + staffId + " under manager '" + managerName + "'", removed, is(true));

	    response = given()
	            .contentType(ContentType.JSON)
	            .accept(ContentType.JSON)
	            .log().all()
	            .pathParam("id", target.getId())
	            .body(target)                          
	        .when()
	            .patch("/managers/{id}")
	        .then()
	            .log().all()
	            .statusCode(200)
	            .extract().response();
	}

	
	
	@Then("The number of staffs under {string} should be reduced by one")
	public void the_number_of_staffs_under_should_be_reduced_by_one(String managerName) {
		
	    List<Managers> after = given()
	            .contentType(ContentType.JSON)
	            .log().all()
	            .queryParam("name", managerName)
	        .when()
	            .get("/managers")
	        .then()
	            .log().all()
	            .statusCode(200)
	            .extract()
	            .jsonPath().getList("", Managers.class);

	    Managers updated = null;
	    for (Managers m : after) {
	        if (m.getName().equalsIgnoreCase(managerName)) {
	            updated = m;
	            break;
	        }
	    }
	    assertThat("Manager not found after update: " + managerName, updated, notNullValue());

	    assertThat("Staff count did not decrease by one for manager '" + managerName + "'",
	        updated.getStaffs().size(),
	        is(beforeStaffCount - 1)
	    );
	    
	}


}
