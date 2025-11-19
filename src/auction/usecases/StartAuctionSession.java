package auction.usecases;

import auction.domain.AuctionSession;
import auction.infrastructure.AuctionSessionRepository;

/**
 * Use case for auctioneer to start an auction session.
 * Singleton pattern.
 */
public class StartAuctionSession {
    private static final StartAuctionSession INSTANCE = new StartAuctionSession();
    private final AuctionSessionRepository repository;

    private StartAuctionSession() {
        this.repository = new AuctionSessionRepository();
    }

    public static StartAuctionSession getInstance() {
        return INSTANCE;
    }

    /**
     * Start an auction session.
     * Changes the status from SCHEDULED to STARTED and notifies all observers.
     *
     * @param sessionId The ID of the auction session to start
     * @return The started auction session
     * @throws IllegalArgumentException if the session is not found
     * @throws IllegalStateException    if the session cannot be started
     */
    public AuctionSession execute(int sessionId) {
        // Fetch the auction session
        AuctionSession session = repository.findById(sessionId);

        if (session == null) {
            throw new IllegalArgumentException("Auction session not found with ID: " + sessionId);
        }

        // Start the auction (domain logic)
        session.startAuction();

        // Persist the status change
        repository.updateStatus(sessionId, session.getStatus());

        return session;
    }
}