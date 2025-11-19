package auction.domain;

import auction.domain.enums.USER_TYPES;

public class User {
    // Fields
    private String userId;
    private final String username;
    private USER_TYPES userType;

    // Constructors
    public User(String username) {
        this.username = username;
    }
    // TODO: adapt constructor to use userId when implementing user management


    public USER_TYPES getUserType() {
        return userType;
    }

    public void setUserType(USER_TYPES userType) {
        this.userType = userType;
    }

    // Methods
    @Override
    public String toString() {
        return username;
    }
}
