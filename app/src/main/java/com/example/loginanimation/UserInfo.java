package com.example.loginanimation;

public class UserInfo {

    // String variable for storing the user's name.
    private String name;

    // String variable for storing the user's username.
    private String username;

    // String variable for storing the user's password.
    private String password;

    // An empty constructor is required when using Firebase Realtime Database.
    public UserInfo() {

    }

    // Getter and setter methods for the 'name' variable.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter methods for the 'username' variable.
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter methods for the 'password' variable.
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

