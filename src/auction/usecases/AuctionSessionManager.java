package auction.usecases;

import auction.domain.AuctionSession;
import auction.infrastructure.AuctionSessionRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton manager that maintains live (in-memory) auction sessions.
 * Ensures all observers share the same instance for the Observer Pattern to work.
 */
public class AuctionSessionManager {
    private static final AuctionSessionManager INSTANCE = new AuctionSessionManager();

    // Cache of live auction sessions (sessionId -> AuctionSession instance)
    private final Map<Integer, AuctionSession> liveSessions;
    private final AuctionSessionRepository repository;

    private AuctionSessionManager() {
        this.liveSessions = new HashMap<>();
        this.repository = new AuctionSessionRepository();
    }

    public static AuctionSessionManager getInstance() {
        return INSTANCE;
    }

    /**
     * Get an auction session. Returns cached instance if available,
     * otherwise loads from database and caches it.
     *
     * @param sessionId The auction session ID
     * @return The auction session, or null if not found
     */
    public AuctionSession getOrLoadSession(int sessionId) {
        // Check if we already have this session in memory
        if (liveSessions.containsKey(sessionId)) {
            System.out.println("Returning cached auction session " + sessionId);
            return liveSessions.get(sessionId);
        }

        // Load from database
        System.out.println("Loading auction session " + sessionId + " from database");
        AuctionSession session = repository.findById(sessionId);

        if (session != null) {
            // Cache it for future use
            liveSessions.put(sessionId, session);
            System.out.println("Cached auction session " + sessionId);
        }

        return session;
    }

    /**
     * Remove a session from the cache (e.g., when auction ends)
     *
     * @param sessionId The auction session ID to remove
     */
    public void removeSession(int sessionId) {
        liveSessions.remove(sessionId);
        System.out.println("Removed auction session " + sessionId + " from cache");
    }

    /**
     * Clear all cached sessions
     */
    public void clearAll() {
        liveSessions.clear();
        System.out.println("Cleared all cached auction sessions");
    }
}