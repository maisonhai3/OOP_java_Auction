package auction.usecases;

import auction.domain.AuctionSession;

import java.util.ArrayList;
import java.util.List;

public class UserLobbyServices {
    // Service Hub
    private final StaffLobbyServices staffLobbyService = StaffLobbyServices.getInstance();


    // Fields
    private List<AuctionSession> sessionList;

    // Singleton instance - Eager initialization (created at app start)
    private static final UserLobbyServices INSTANCE = new UserLobbyServices();
    private UserLobbyServices() {
        this.sessionList = new ArrayList<>();
        System.out.println("LobbyServices initialized");
    }
    public static UserLobbyServices getInstance() {
        return INSTANCE;
    }

    // Services
    public List<AuctionSession> getAuctionSessionList() {
        this.sessionList = staffLobbyService.getAvailableAuctionSession();
        return this.sessionList;
    }

    public int getAuctionSessionCount() {
        return this.sessionList.size();
    }
}
