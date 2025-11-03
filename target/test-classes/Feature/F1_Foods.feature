# Author : Aiswarya
# Date   : 30 Oct, 2025

Feature: User should be able to view the list of all available foods, create a new food item, and update the price of an existing food itemm by its id


  	Scenario: User able to view all available foods
    	When User sends a GET request to "/foods"
    	Then The response status code should be displayed as 200
    	And The response body should contain a list of foods
    	And Each food item should include the fields "id", "name", and "price"
    	

	Scenario: User can create a new food item and verify it was added
  		When User sends a POST request to "/foods/" with id "5", name "veggie noodles" and price 10.50
  		Then The response status code should be displayed as 201
  		And The response body should contain the created food details
  		When User sends a GET request to "/foods"
  		Then The newly created food item should be present in the list


	Scenario Outline: User can update the price of an existing food items by id and verify the change
    	When User wants to update the price of the food item to "<price>" with id "<id>"
    	Then The price of the food item should be updated 
 
    	Examples:
	      | id  | price |
	      | 1 	| 6.00  |
	      | 2   | 9.00  |
	      
	     
	     