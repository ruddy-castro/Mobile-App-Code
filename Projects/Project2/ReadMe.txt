Project 1
Authors: Ivy Nguyen, Ly Do, Ruddy Castro

------------------------------------------------------------------------------------------------------
Program Design
------------------------------------------------------------------------------------------------------
This is a simple Login project that allows a user to login to the application or create a new account. 
The project uses three activities and one helper class to handle the given tasks. 

Lower Fragment: A fragment that holds all options to control the photo application. The fragment is set 
	up to cycle through the photos via buttons. Two check boxes are used to replace the fragment
	with a gridview, not implemented, or cycle through the photos automatically. 

Main Activity: An activity that controls the interaction between both fragments. This activity holds 
	the image array that is used for the shared data.

Shared Data: A ViewModel class to share data between the MainActivity and its fragments. It is a
    singleton in the MainActivity scope. It uses the observer pattern so that the subscribers can
    listen to events when the selected index is changed.

Upper Fragment: A fragment that displays the image file that is currently chosen. This fragment uses
	a ViewModel to refresh the page with a new image. 

------------------------------------------------------------------------------------------------------
Contributions
------------------------------------------------------------------------------------------------------
Ivy: Lower Fragment, XMLs, Main Activity
Ly: Lower Fragment, ViewModel Pattern Implementation, Main Activity
Ruddy: Upper Fragment, Lower Fragment, XMLs, readme.txt