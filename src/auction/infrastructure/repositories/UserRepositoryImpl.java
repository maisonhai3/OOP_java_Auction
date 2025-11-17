package auction.infrastructure.repositories;

import auction.domain.User;
import auction.domain.repositories.IUserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of IUserRepository
 * Uses Spring's @Repository annotation for automatic component scanning
 * Thread-safe using ConcurrentHashMap
 */
@Repository
public class UserRepositoryImpl implements IUserRepository {
    // In-memory storage (simulates database)
    private final Map<String, User> userStore = new ConcurrentHashMap<>();
    private int idCounter = 1;

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Generate ID if new user (you might need to add getId() method to User entity)
        String username = user.getUsername();
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        userStore.put(username, user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userStore.get(username));
    }

    @Override
    public Optional<User> findById(String id) {
        // For now, we're using username as ID
        // You can enhance this later with proper ID field
        return findByUsername(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    @Override
    public boolean deleteByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userStore.remove(username) != null;
    }

    @Override
    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userStore.containsKey(username);
    }

    /**
     * Clear all users (useful for testing)
     */
    public void clear() {
        userStore.clear();
        idCounter = 1;
    }

    /**
     * Get count of users (useful for testing/monitoring)
     */
    public int count() {
        return userStore.size();
    }
}