package auction.usecases;

import auction.domain.SuperAdmin;
import auction.domain.User;
import auction.domain.UserType;

public class UserService {
    // Fields
    private SuperAdmin bigMamaManager;

    // Constructors
    public UserService() {
        bigMamaManager = new SuperAdmin();
    }

    // Methods
    public User createUser(String username) {
        return new User(username);
    }

    public User logInUser(String username) {
        return new User(username);
    }

    public Boolean changeUserType(User user, UserType targetType) {
        return true;
    }
}
