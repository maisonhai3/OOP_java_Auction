package auction.presentation.dto;

import auction.domain.User;

/**
 * Data Transfer Object for User
 * Separates domain entities from presentation layer
 * Following Clean Architecture: Don't expose domain entities to outer layers
 */
public class UserDTO {
    private final String username;
    private String userType;

    // Constructor from domain entity
    public UserDTO(User user) {
        this.username = user.getUsername();
        this.userType = user.getUserType() != null ? user.getUserType().toString() : null;
    }

    // Constructor with fields
    public UserDTO(String username, String userType) {
        this.username = username;
        this.userType = userType;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }

    // Static factory method to convert from domain entity
    public static UserDTO fromDomain(User user) {
        return new UserDTO(user);
    }
}