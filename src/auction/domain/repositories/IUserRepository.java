package auction.domain.repositories;

import auction.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * Following Clean Architecture: Domain layer defines the interface (port)
 * Infrastructure layer provides the implementation (adapter)
 */
public interface IUserRepository {
    /**
     * Save a user to the repository
     *
     * @param user User to save
     * @return Saved user
     */
    User save(User user);

    /**
     * Find user by username
     *
     * @param username Username to search for
     * @return Optional containing user if found, empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by ID
     *
     * @param id User ID
     * @return Optional containing user if found, empty otherwise
     */
    Optional<User> findById(String id);

    /**
     * Get all users
     *
     * @return List of all users
     */
    List<User> findAll();

    /**
     * Delete a user
     *
     * @param username Username of user to delete
     * @return true if deleted, false if not found
     */
    boolean deleteByUsername(String username);

    /**
     * Check if user exists by username
     *
     * @param username Username to check
     * @return true if exists, false otherwise
     */
    boolean existsByUsername(String username);
}