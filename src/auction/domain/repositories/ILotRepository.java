package auction.domain.repositories;

import auction.domain.Lot;
import auction.domain.LotStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Lot entity
 * Following Clean Architecture: Domain layer defines the interface (port)
 */
public interface ILotRepository {
    /**
     * Save a lot
     *
     * @param lot Lot to save
     * @return Saved lot
     */
    Lot save(Lot lot);

    /**
     * Find lot by name
     *
     * @param name Lot name
     * @return Optional containing lot if found
     */
    Optional<Lot> findByName(String name);

    /**
     * Get all lots
     *
     * @return List of all lots
     */
    List<Lot> findAll();

    /**
     * Find lots by status
     *
     * @param status Lot status
     * @return List of lots with the given status
     */
    List<Lot> findByStatus(LotStatus status);

    /**
     * Delete a lot
     *
     * @param name Lot name to delete
     * @return true if deleted, false if not found
     */
    boolean deleteByName(String name);

    /**
     * Check if lot exists
     *
     * @param name Lot name
     * @return true if exists
     */
    boolean existsByName(String name);
}