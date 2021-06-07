Project 1
Authors: Ivy Nguyen, Ly Do, Ruddy Castro

------------------------------------------------------------------------------------------------------
Program Design
------------------------------------------------------------------------------------------------------
This is a simple Login project that allows a user to login to the application or create a new account. 
The project uses three activities and one helper class to handle the given tasks. 

Login Activity: An activity that check the user's login credential. If it exists in the system, the 
	user will successfully log into his/her account. If it doesn't, a toast message will show
	up. The user can also access to the signup activity for creating a new account.

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
Ivy: Login Activity, xmls, Welcome Activity
Ly: Signup Activity, singleton pattern implementation 
Ruddy: Data class, Intent implementation, readme.txt