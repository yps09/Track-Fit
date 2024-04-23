package com.android.trackfit;

// User.java

public class User {
    private String email;
    private String username;

    // Required default constructor for DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    // You can add additional methods or modify the class as needed
}

