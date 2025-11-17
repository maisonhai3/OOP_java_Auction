package auction.usecases;

import auction.domain.User;
import auction.domain.UserType;
import auction.domain.repositories.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User management use case service
 * Follows Clean Architecture: Use case layer coordinates between presentation and domain
 * Uses dependency injection for loose coupling
 */
@Service
public class UserService {
    // Dependencies injected via constructor (Dependency Inversion Principle)
    private final IUserRepository userRepository;

    // Constructor injection (recommended by Spring)
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Create a new user account
     *
     * @param username Username for the new user
     * @return Created user
     * @throws IllegalArgumentException if username already exists or is invalid
     */
    public User createUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        User newUser = new User(username);
        return userRepository.save(newUser);
    }

    /**
     * Log in an existing user
     *
     * @param username Username to log in
     * @return User if found
     * @throws IllegalArgumentException if user not found
     */
    public User logInUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            // Auto-create user if not exists (for now - you can change this behavior)
            return createUser(username);
        }

        return user.get();
    }

    /**
     * Change user type (role)
     *
     * @param user       User to modify
     * @param targetType New user type
     * @return true if successful
     */
    public Boolean changeUserType(User user, UserType targetType) {
        if (user == null || targetType == null) {
            throw new IllegalArgumentException("User and target type cannot be null");
        }

        user.setUserType(targetType);
        userRepository.save(user);
        return true;
    }

    /**
     * Find user by username
     *
     * @param username Username to search
     * @return Optional containing user if found
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Check if username exists
     *
     * @param username Username to check
     * @return true if exists
     */
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
