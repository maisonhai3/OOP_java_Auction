package auction.usecases;

import auction.domain.SuperAdmin;
import auction.domain.User;
import auction.domain.enums.USER_TYPES;

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

    public Boolean changeUserType(User user, USER_TYPES targetType) {
        return true;
    }

    /**
     * Check if a user exists (basic validation for now)
     * TODO: Add UserRepository to check against database
     *
     * @param username the username to check
     * @return true if user exists (for now: not null and not empty)
     */
    public boolean userExists(String username) {
        // Basic validation - check if username is valid
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        // TODO: When UserRepository is implemented, check database
        // return userRepository.findByUsername(username) != null;

        // For now, assume any non-empty username is valid
        return true;
    }
}
