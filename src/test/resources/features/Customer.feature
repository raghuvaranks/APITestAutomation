Feature: Functionality of customer api

@custedt
Scenario Outline: Verify the customer api
Given invoke the customer api endpt with input "<TC_NAME>"
When PUT method is called
Then status should be 200
And verify the expected response "<TC_NAME>"

Examples:
|TC_NAME|
|SC01_TC001_Add_Cust|