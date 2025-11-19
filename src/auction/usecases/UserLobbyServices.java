package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.enums.AuctionStatus;
import auction.infrastructure.AuctionSessionRepository;

import java.util.List;

public class UserLobbyServices {
    // Repository - direct access to data source
    private final AuctionSessionRepository auctionSessionRepository;

    // Singleton instance - Eager initialization (created at app start)
    private static final UserLobbyServices INSTANCE = new UserLobbyServices();
    private UserLobbyServices() {
        this.auctionSessionRepository = new AuctionSessionRepository();
        System.out.println("UserLobbyServices initialized");
    }
    public static UserLobbyServices getInstance() {
        return INSTANCE;
    }

    // Services
    public List<AuctionSession> getAuctionSessionList() {
        // Get available sessions directly from repository (no coupling with StaffLobbyServices!)
        return auctionSessionRepository.findByStatus(AuctionStatus.SCHEDULED, AuctionStatus.STARTED);
    }

    public int getAuctionSessionCount() {
        return getAuctionSessionList().size();
    }
}
