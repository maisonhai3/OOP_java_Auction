package auction.services;

import auction.models.User;
import auction.models.UserType;

public class UserService {
    // Fields

    // Constructors

    // Methods
    public User createUser(String username) {
        if (admin.getUserType() == UserType.MANAGER) {
            return new User(username);
        } else {
            throw new SecurityException("Only Admin can change user type.");
        }
    }

    public Boolean changeUserType(User admin, User user, UserType targetType) {
        if (admin.getUserType() == UserType.MANAGER) {
            user.setUserType(targetType);
            return true;
        } else {
            throw new SecurityException("Only Admin can change user type.");
        }
    }
}
