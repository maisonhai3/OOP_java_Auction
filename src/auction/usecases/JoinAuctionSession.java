package auction.usecases;

import auction.domain.AuctionSession;
import auction.infrastructure.AuctionSessionRepository;

import java.beans.PropertyChangeListener;

public class JoinAuctionSession {
    private final AuctionSessionRepository repo;

    public JoinAuctionSession() {
        this.repo = new AuctionSessionRepository();
    }

    public AuctionSession execute(int sessionId, PropertyChangeListener uiListener) {
        AuctionSession aucSession = repo.findById(sessionId);

        if (aucSession != null) {
            aucSession.addObserver(uiListener);
            System.out.println("Joined auction session with ID: " + sessionId);
        } else {
            System.err.println("Could not find auction session with ID: " + sessionId);
        }
        return aucSession;
    }
}
