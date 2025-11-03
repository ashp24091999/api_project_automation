package com.fdmgroup.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import static io.restassured.RestAssured.baseURI;
public class Hooks {

    @Before
    public void setUp() {
        baseURI = "http://localhost:3000";

    }

    @After
    public void tearDown() {
        
        System.out.println("Scenario completed");
    }
}
