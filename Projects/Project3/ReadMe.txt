Project 3
Authors: Ivy Nguyen, Ly Do, Ruddy Castro

------------------------------------------------------------------------------------------------------
Program Design
------------------------------------------------------------------------------------------------------
This is a project that displays the available cars for sale within a given zipcode. The project 
gathers the information from an external source via an API. The program is strucutred to 

------------------------
Model Classes
------------------------
Car: A class that represents a Car object. This class uses Lombok to create the getters and setters.
	Each variable is serialized based on the JSON attribute name from the given API.
 
CarMake: A class that represents a CarMake object. This class uses Lombok to create the getters and
	setters. Some variables are serialized based on the JSON attribute name from the given API.

CarModel: A class that represents a CarModel object. This class uses Lombok to create the getters and 
	setters. Some variables are serialized based on the JSON attribute name from the given API.

------------------------
Service Classes
------------------------
CarService: Interface for making API calls to get car related info.

CarServiceException: An exception class used for the CarService implementation.

CarServiceImpl: Implementation of the CarService interface.

------------------------
Activity Classes
------------------------
CarDetailActivity: A class to display Car details. 

CarDetailFragment: A class to display Car details in two panes.

MainActivity: Main class that handles the application logic by calling the various classes and their 
	methods.
 

------------------------------------------------------------------------------------------------------
Contributions
------------------------------------------------------------------------------------------------------
Ivy: XMLs, CarDetailFragment, MainActivity, CarDetailActivity
Ly: Car, CarMake, CarModel, CarService, CarServiceException, CarServiceImpl, MainActivity
Ruddy: Car, CarDetailActivity, CarDetailFragment, MainActivity, XMLs, readme.txt