package auction.domain;

import auction.domain.enums.UserType;

public class User {
    // Fields
    private String userId;
    private final String username;
    private UserType userType;

    // Constructors
    public User(String username) {
        this.username = username;
    }
    // TODO: adapt constructor to use userId when implementing user management


    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    // Methods
    @Override
    public String toString() {
        return username;
    }
}
