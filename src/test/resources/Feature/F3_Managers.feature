# Author : Aiswarya
# Date : 30 Oct, 2025


Feature: User should be able to update the list of staffs and also delete some staff information

Scenario: User should be able to update the salary of staffs based on managers id
	When The user updates all staff salaries by 10 percent whose manager id 1
  	Then All the staff information gets updated
	
Scenario: User should be able to delete staff information
	When The user wants to delete information for staff with id 1 whose manager name is "Bell Pepper"
	Then The number of staffs under "Bell Pepper" should be reduced by one
	
