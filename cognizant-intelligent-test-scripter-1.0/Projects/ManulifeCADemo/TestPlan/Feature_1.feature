Feature: A user should be able to search for a Pet owner entering the Last Name of the owner

Scenario Outline: When a user enters the Last Name of an existing Pet owner and click on 'Find Owner' button the owner details appears
Given The user navigates to the 'Find Owners' screen of the PetClinic portal
When The user enters a valid Last Name <Search_Last_Name> of the PetOwner
And Clicks on the 'Find Owner' button
Then The Owner Name <Owner_Name> , Owner Address <Owner_Address>, Owner City <Owner_City> and Owner Telephone <Owner_Telephone> appear under 'Owner Information' section

Examples:
|Search_Last_Name|Owner_Name|Owner_Address|Owner_City|Owner_Telephone|
|BLACK|Jeff Black|1450 Oak Blvd.|Monona|6085555387|
|Davis|Harold Davis|563 Friendly St.|Windsor|6085553198|


Scenario Outline: When a user enters the Last Name of an existing Pet Owner and clicks on 'Find Owner' button, the Pet details appears
Given The user navigates to the 'Find Owners' screen of the PetClinic portal
When The user enters a valid Last Name <Search_Last_Name> of the PetOwner
And Clicks on the 'Find Owner' button
Then The Pet Name <Pet_Name>, Pet DOB <Pet_Birth_Date>, Pet Type <Pet_Type> appear under 'Pets and Visits' section

Examples:
|Search_Last_Name|Pet_Name|Pet_Birth_Date|Pet_Type|
|BLACK|Lucky|2011-08-06|bird|


Scenario Outline: When a user enters an invalid Last Name of Pet Owner and clicks on 'Find Owner' button, the application shows an error message
Given The user navigates to the 'Find Owners' screen of the PetClinic portal
When The user enters an invalid Last Name <Search_Last_Name> of the PetOwner
And Clicks on the 'Find Owner' button
Then The webpage shows an error message <error_message>

Examples:
|Search_Last_Name|error_message|
|SenGupta|has not been found|

