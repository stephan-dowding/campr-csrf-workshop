package com.thoughtworks.securityinourdna;

public class UserState {

    private boolean adminLoggedIn;

    public UserState(boolean adminLoggedIn) {
        this.adminLoggedIn = adminLoggedIn;
    }

    public boolean isAdminLoggedIn() {
        return adminLoggedIn;
    }
}
