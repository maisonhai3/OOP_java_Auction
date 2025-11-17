package auction.usecases;

import auction.domain.AuctionSession;

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

    public int getAuctionSessionCount() {
        return this.sessionList.size();
    }
}
