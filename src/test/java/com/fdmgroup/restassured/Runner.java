package com.fdmgroup.restassured;

public class Runner {
	public static void main(String[] args) {
		//GET Methods
		FoodMethods.performGETFood();
		
		//POST Methods
		//FoodMethods.performPOSTFood("5", "noodles", 10.50);
		//MessageMethods.performPOSTComment("5", 4, "crispy",5);
		
		//PATCH Methods
		//FoodMethods.performPATCHFood("5");
		
		//DELETE Methods
		//MessageMethods.performDELETEComment("5");
		
		//PATCH Methods
		//ManagersMethods.updateStaffSalary("1", 10);  // Manager 1 → +10%
        //ManagersMethods.updateStaffSalary("2", 20);  // Manager 2 → +20%
		
		// Delete staff “2” under manager named “Bell Pepper”
        //ManagersMethods.deleteStaffUnderManagerByName("Bell Pepper", "2");

	}
}
