package auction.services;

import auction.models.AuctionSession;

import java.util.ArrayList;
import java.util.List;

public class LobbyServices {
    // Singleton instance - Eager initialization (created at app start)
    private static final LobbyServices INSTANCE = new LobbyServices();

    // Fields
    private List<AuctionSession> sessionList;

    // Private Constructor - prevents instantiation from outside
    private LobbyServices() {
        this.sessionList = new ArrayList<>();
        System.out.println("LobbyServices initialized");
    }

    // Public method to get the singleton instance
    public static LobbyServices getInstance() {
        return INSTANCE;
    }

    // Services
    public List<AuctionSession> getAuctionSessionList() {
        return this.sessionList;
    }

    // Additional helper methods for managing sessions
    public void addAuctionSession(AuctionSession session) {
        this.sessionList.add(session);
    }

    public void removeAuctionSession(AuctionSession session) {
        this.sessionList.remove(session);
    }

    public int getAuctionSessionCount() {
        return this.sessionList.size();
    }
}
