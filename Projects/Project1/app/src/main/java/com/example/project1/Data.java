package com.example.project1;

import java.util.HashMap;

// Mock database and API calls
public class Data {

    private static Data instance;

    // A hashmap data structure for holding usernames and passwords pair
    HashMap <String, String> hmCredentials;

    public static Data getInstance() {
        if (instance == null) {
            synchronized (Data.class) {
                if (instance == null) {
                    instance = new Data();
                }
            }
        }
        return instance;
    }

    private Data() {

        hmCredentials = new HashMap<>();

        // Adding some items into the hashmap table

        hmCredentials.put("AJ", "CoolDude1");
        hmCredentials.put("test", "1234");
    }

    // This method adds a new username and password to the hashmap
    public void AddCredential(String username, String password){
        hmCredentials.put(username, password);
    }

    // This method checks if username exists in the hashmap
    public Boolean CheckUsername(String username){
        return hmCredentials.containsKey(username);
    }

    // This method checks a username and password combination is correct!
    public Boolean CheckCredentials(String username, String Password) {
        return CheckUsername(username) && Password.equals(hmCredentials.get(username));
    }
}
