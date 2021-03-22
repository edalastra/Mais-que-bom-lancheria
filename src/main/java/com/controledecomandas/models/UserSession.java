package com.controledecomandas.models;

public final class UserSession {

    private static UserSession instance;

    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public static UserSession getInstace(User user) {
        if(instance == null) {
            instance = new UserSession(user);
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void cleanUserSession() {
       this.user = null;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + user.getFirstName()+ " " + user.getLastName() +
                ", id=" + user.getId() +
                '}';
    }
}