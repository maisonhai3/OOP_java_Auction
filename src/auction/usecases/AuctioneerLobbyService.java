package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.AuctionStatus;
import auction.infrastructure.AuctionSessionRepository;

import java.util.List;

public class AuctioneerLobbyService {
    // Service hub
    private final LotService lotService = LotService.getInstance();
    private final AuctionSessionRepository auctionSessionRepository;

    // Fields

    // Singleton
    private static final AuctioneerLobbyService INSTANCE = new AuctioneerLobbyService();

    private AuctioneerLobbyService() {
        this.auctionSessionRepository = new AuctionSessionRepository();
        System.out.println("AuctioneerLobbyService initialized");
    }

    public static AuctioneerLobbyService getInstance() {
        return INSTANCE;
    }

    // Services
    public List<AuctionSession> getAvailableAuctionSession() {
        return auctionSessionRepository.findByStatus(
                AuctionStatus.SCHEDULED,
                AuctionStatus.STARTED
        );
    }
}
