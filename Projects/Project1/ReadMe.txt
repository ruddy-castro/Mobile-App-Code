Project 1
Authors: Ivy Nguyen, Ly Do, Ruddy Castro

------------------------------------------------------------------------------------------------------
Program Design
------------------------------------------------------------------------------------------------------
This is a simple Login project that allows a user to login to the application or create a new account. 
The project uses three activities and one helper class to handle the given tasks. 

Login Activity: 
Signup Activity: An activity for the user to sign-up for a new account by providing username, password,
        email and phone number. This class also does simple validation for the input fields from users
        before saving the account to database.

Welcome Activity: A simple activity that uses a TextView to display the username of the user signing
        in. An intent is used to get the username from the login activity after a successful
        login is detected.
Data: A class representing a mock database using a hashmap. A singleton design pattern was used to make
        sure that only one instance of Data was running at any given time. Helper methods were created
        to help with the adding and verifying of accounts.

------------------------------------------------------------------------------------------------------
Contributions
------------------------------------------------------------------------------------------------------
Ivy: Login Activity, readme.txt, xmls
Ly: Signup Activity, singleton pattern implementation 
Ruddy: Data class, Intent implementation