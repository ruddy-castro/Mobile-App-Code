package com.example.project1;

import java.util.HashMap;

/**
 * Data is a mock database class that handles API calls for the program.
 */
public class Data {

    private static Data instance;

    // A hashmap data structure for holding usernames and passwords pair
    HashMap <String, String> hmCredentials;

    /**
     * getInstance makes sure that only one instance of a particular class exists.
     * @return single running instance of the Data class.
     */
    public static Data getInstance() {
        // If no instance exists, a new instance of the Data class is created. If an instance
        // exists, the if statement is skipped and the current running instance is returned.
        if (instance == null) {
            // synchronized is making sure only one thread is executing the Data instance at a time
            synchronized (Data.class) {
                if (instance == null) {
                    instance = new Data();
                }
            }
        }
        return instance;
    }

    /**
     * Constructor for a Data object. A credential hashmap is populated with test data.
     */
    private Data() {

        hmCredentials = new HashMap<>();

        // Adding some items into the hashmap table
        hmCredentials.put("AJ", "CoolDude1");
        hmCredentials.put("test", "1234");
    }

    /**
     * This method adds a new username and password to the hashmap.
     * @param username A string object representing the validated username of the account.
     * @param password A string object representing the validated password of the account.
     */
    public void AddCredential(String username, String password){
        hmCredentials.put(username, password);
    }

    /**
     * This method checks if username exists in the hashmap.
     * @param username A string object representing the username of the account.
     */
    public Boolean CheckUsername(String username){
        return hmCredentials.containsKey(username);
    }

    /**
     * This method checks a username and password combination is correct.
     * @param username A string object representing the username of the account.
     * @param Password A string object representing the password of the account.
     * @return A boolean representing if the username exists in the hash map.
     */
    public Boolean CheckCredentials(String username, String Password) {
        return CheckUsername(username) && Password.equals(hmCredentials.get(username));
    }
}
