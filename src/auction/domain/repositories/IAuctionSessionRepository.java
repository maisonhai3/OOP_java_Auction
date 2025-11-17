package auction.domain.repositories;

import auction.domain.AuctionSession;
import auction.domain.AuctionStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AuctionSession entity
 * Following Clean Architecture: Domain layer defines the interface (port)
 */
public interface IAuctionSessionRepository {
    /**
     * Save an auction session
     *
     * @param session AuctionSession to save
     * @return Saved auction session
     */
    AuctionSession save(AuctionSession session);

    /**
     * Find auction session by ID
     *
     * @param auctionId Auction ID
     * @return Optional containing auction if found
     */
    Optional<AuctionSession> findById(String auctionId);

    /**
     * Get all auction sessions
     *
     * @return List of all auctions
     */
    List<AuctionSession> findAll();

    /**
     * Find auctions by status
     *
     * @param status Auction status
     * @return List of auctions with the given status
     */
    List<AuctionSession> findByStatus(AuctionStatus status);

    /**
     * Delete an auction session
     *
     * @param auctionId Auction ID to delete
     * @return true if deleted, false if not found
     */
    boolean deleteById(String auctionId);

    /**
     * Check if auction exists
     *
     * @param auctionId Auction ID
     * @return true if exists
     */
    boolean existsById(String auctionId);
}