package auction.usecases;

import auction.domain.SuperAdmin;
import auction.domain.User;
import auction.domain.enums.UserType;

public class UserService {
    // Singleton
    private static final UserService INSTANCE = new UserService();

    // Fields
    private SuperAdmin bigMamaManager;

    // Private Constructor
    private UserService() {
        bigMamaManager = new SuperAdmin();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    // Methods
    public static User createUser(String username) {
        return new User(username);
    }

    public static User logInUser(String username) {
        return new User(username);
    }

    public Boolean changeUserType(User user, UserType targetType) {
        return true;
    }
}
