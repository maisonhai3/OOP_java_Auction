package auction.models;

public class User {
    // Fields
    private final String username;
    private UserType userType;

    // Constructors
    public User(String username) {
        this.username = username;
    }

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
