/*package com.fdmgroup.restassured;

import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import com.fdmgroup.pojos.Managers;
import com.fdmgroup.pojos.Staff;
import java.util.*;
import java.util.stream.Collectors;

public class ManagersMethods {

    
    public static void updateStaffSalary(String managerId, double percent) {

        System.out.println("\n>>> Updating salaries for Manager ID: " + managerId + " by " + percent + "%");

        
        Managers manager = 
            given()
                .contentType(ContentType.JSON)
                .log().all()
            .when()
                .get("http://localhost:3000/managers/{id}", managerId)
            .then()
                .log().all()
                .statusCode(200)
                .extract().as(Managers.class);

        
        for (Staff s : manager.getStaffs()) {
            double newSalary = s.getSalary() * (1 + (percent / 100));
            s.setSalary(newSalary);
        }

       
        Map<String, Object> payload = new HashMap<>();
        payload.put("staffs", manager.getStaffs());

        
        given()
            .contentType(ContentType.JSON)
            .body(payload)
            .log().all()
        .when()
            .patch("http://localhost:3000/managers/{id}", managerId)
        .then()
            .statusCode(200);

       
        Managers after =
            given()
                .contentType(ContentType.JSON)
            .when()
                .get("http://localhost:3000/managers/{id}", managerId)
            .then()
                .statusCode(200)
                .extract().as(Managers.class);

    
        System.out.println("Manager: " + after.getName());
        for (Staff s : after.getStaffs()) {
            System.out.println("  - " + s.getName() + " → new salary: " + s.getSalary());
        }
    }
    
 
    public static void deleteStaffUnderManagerByName(String managerName, String staffIdToDelete) {

        
        Managers[] matches =
            given()
                .contentType(ContentType.JSON)
            .when()
                .get("http://localhost:3000/managers?name={name}", managerName)
            .then()
                .statusCode(200)
                .extract().as(Managers[].class);

        if (matches.length == 0) throw new AssertionError("No manager found with name='" + managerName + "'");
        if (matches.length > 1) System.out.println("Warning: multiple managers named '" + managerName + "'. Using the first.");

        Managers before = matches[0];                 // JSON → POJO (deserialized)
        String managerId = before.getId();            // internal use only

        List<Staff> staffsBefore = before.getStaffs() == null ? Collections.emptyList() : before.getStaffs();
        int countBefore = staffsBefore.size();
        if (countBefore == 0) throw new AssertionError("Manager '" + managerName + "' has no staff.");

        
        List<Staff> updated = staffsBefore.stream()
            .filter(s -> !staffIdToDelete.equals(s.getId()))
            .collect(Collectors.toList());

        if (updated.size() == countBefore) {
            throw new AssertionError("Staff id='" + staffIdToDelete + "' not found under manager '" + managerName + "'.");
        }

        
        Map<String, Object> payload = new HashMap<>();
        payload.put("staffs", updated);

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .patch("http://localhost:3000/managers/{id}", managerId)
        .then()
            .statusCode(200);

        
        Managers[] afterMatches =
            given()
                .contentType(ContentType.JSON)
            .when()
                .get("http://localhost:3000/managers?name={name}", managerName)
            .then()
                .statusCode(200)
                .extract().as(Managers[].class);

        Managers after = afterMatches[0];
        List<Staff> staffsAfter = after.getStaffs() == null ? Collections.emptyList() : after.getStaffs();

        // Assertions
        if (staffsAfter.size() != countBefore - 1)
            throw new AssertionError("Count did not decrease by 1 for manager '" + managerName + "'.");
        boolean stillPresent = staffsAfter.stream().anyMatch(s -> staffIdToDelete.equals(s.getId()));
        if (stillPresent)
            throw new AssertionError("Deleted staff id still present under manager '" + managerName + "'.");

        System.out.printf("Deleted staff id='%s' under manager name='%s'. Count %d -> %d%n",
                staffIdToDelete, managerName, countBefore, staffsAfter.size());
    }

}*/
