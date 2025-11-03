# Author : Aiswarya
# Date : 30 Oct, 2025

Feature: User should be able to create a new comment, delete a comment and get comment information


Scenario: User should be able to create a new comment
 	When User creates a new comment with id "5", userid 4 body "crispy" and foodId 5
 	Then The newly created comment should be present in the list
 


Scenario: User should be able delete the comment based on comment id
	When User deletes the comment based on comment id "5"
	Then The comment should not be present in the list
	

Scenario: User should be able to get comment information 
	When User wants to get the comment information based on the user id 1 and the food id 4
	Then The body of the message should match "amazing"



