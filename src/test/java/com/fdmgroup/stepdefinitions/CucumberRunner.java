package com.fdmgroup.stepdefinitions;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
//Runner
@RunWith(Cucumber.class)
@CucumberOptions(
	features = "src/test/resources/Feature",
	glue = "com.fdmgroup.stepdefinitions",
	tags = "",
	plugin = {
			"pretty",
			"json:Report/cucumber.json",
			"junit:Report/cucumber.junit",
			"html:Report/cucumber.html"
	},
	monochrome = false,
	dryRun = false,
	publish = true
)
public class CucumberRunner {
}
