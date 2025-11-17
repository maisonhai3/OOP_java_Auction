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
    public List<AuctionSession> getSessionList() {
        return this.sessionList;
    }

    // Additional helper methods for managing sessions
    public void addSession(AuctionSession session) {
        this.sessionList.add(session);
    }

    public void removeSession(AuctionSession session) {
        this.sessionList.remove(session);
    }

    public int getSessionCount() {
        return this.sessionList.size();
    }
}
