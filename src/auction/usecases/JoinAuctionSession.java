package auction.usecases;

import auction.domain.AuctionSession;

import java.beans.PropertyChangeListener;

/**
 * Use case for joining an auction session.
 * Uses AuctionSessionManager to ensure all observers share the same instance.
 */
public class JoinAuctionSession {
    private final AuctionSessionManager sessionManager;

    // --- Singleton ---
    private static final JoinAuctionSession INSTANCE = new JoinAuctionSession();

    private JoinAuctionSession() {
        this.sessionManager = AuctionSessionManager.getInstance();
    }

    public static JoinAuctionSession getInstance() {
        return INSTANCE;
    }

    /**
     * Join an auction session and register as an observer.
     * Returns the shared (cached) instance so all observers receive events.
     *
     * @param sessionId  The auction session ID to join
     * @param uiListener The observer (UI component) to register
     * @return The auction session instance, or null if not found
     */
    public AuctionSession execute(int sessionId, PropertyChangeListener uiListener) {
        // Get the shared instance from the manager (cached or loaded)
        AuctionSession aucSession = sessionManager.getOrLoadSession(sessionId);

        if (aucSession != null) {
            aucSession.addObserver(uiListener);
            System.out.println("Joined auction session with ID: " + sessionId);
        } else {
            System.err.println("Could not find auction session with ID: " + sessionId);
        }
        return aucSession;
    }
}
