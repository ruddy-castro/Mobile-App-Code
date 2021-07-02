Project 3
Authors: Ivy Nguyen, Ly Do, Ruddy Castro

------------------------------------------------------------------------------------------------------
Program Design
------------------------------------------------------------------------------------------------------
Expense Tracker is an application that will calculate the daily expenses and annual saving amount
of a user based on the inputted annual income, desired saving goal, and maximum daily expenses.
The program uses a navigation drawer to navigate throught the different features of the application.
The application uses Firebase to authenticate users and hold user data to generate the reports. 

------------------------
Model Classes
------------------------
Expense: A class that represents the expense object. The class uses Lombok for all getters/setter.

Settings: A class that represents the setting object. The class uses Lombok for all getters/setter.

User: A class that represents the user object. The class uses Lombok for all getters/setter.

------------------------
UI Classes
------------------------
AboutUs: AboutUs is a Fragment that displays information about the application.

DataEntryFragment: The fragment for data entries, which are the list of expenses. A user can create a
	new expense from this fragment.

DataEntryRecyclerViewAdapter: Recycler View adapter for all reports and data entry.

HelpCenter: HelpCenter is a Fragment that allows a user to submit a question they may have about the 
	program.

DailyExpensesFragment: The fragment to show the daily expenses graph and list.

DailySavingsFragment: Fragment to display the chart and the list of daily savings.

ItemizedReportFragment: Fragment to display the itemized chart on a specific range of days.

SearchFragment: Fragment that display the search option.

------------------------
Activity Classes
------------------------
ExpenseEntryActivity: A class for enter expense where the user can enter the information for a new 
	expense. The expense gets added to firestore. 

Login: This is a class for Login activity where the user is authenticated through Firebase 
	authentication.

SettingsActivity: The Activity for settings where users can customize their data.

Signup: This is a class for Sign-up activity where the user gets added to Firebase through their
	authentication system.

SplashActivity: The class to take care of the splash screen in the beginning using a GIF.

WelcomeScreen: The welcome screen after the user successfully logged in. This welcome screen holds
	the navigation drawer for all features of the application. 
 
------------------------------------------------------------------------------------------------------
Contributions
------------------------------------------------------------------------------------------------------
Ivy: XMLs, Reports, Login, Signup, HelpCenter, Firebase
Ly: WelcomeScreen, Firebase, Reports, Model, SettingsActivity, DataEntryActivity
Ruddy: XMLs, Reports, Login, Reports, DataEntry, Search, SplachActivity