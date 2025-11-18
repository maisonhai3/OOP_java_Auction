package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.AuctionStatus;
import auction.domain.Lot;
import auction.infrastructure.AuctionSessionRepository;

import java.util.List;

public class StaffLobbyServices {
    // Service hub
    private final LotService lotService = LotService.getInstance();
    private final AuctionSessionRepository auctionSessionRepository;

    // Singleton
    private static final StaffLobbyServices INSTANCE = new StaffLobbyServices();
    private StaffLobbyServices() {
        this.auctionSessionRepository = new AuctionSessionRepository();
        System.out.println("StaffLobbyServices initialized");
    }
    public static StaffLobbyServices getInstance() {
        return INSTANCE;
    }

    // Methods
    public AuctionSession createAuctionSession(String title, int lotID) {
        Lot lot = lotService.getLot(lotID);
        if (lot == null) {
            System.err.println("Cannot create auction session: Lot with ID " + lotID + " not found");
            return null;
        }

        AuctionSession auction = new AuctionSession(lot);
        auction.setTitle(title);

        // Save to database
        int sessionId = auctionSessionRepository.save(auction, lotID);
        if (sessionId > 0) {
            System.out.println("Auction session created successfully with ID: " + sessionId);
        }

        return auction;
    }

    public List<AuctionSession> getAllAuctionSessions() {
        return auctionSessionRepository.findAll();
    }

    public List<AuctionSession> getAvailableAuctionSession() {
        return auctionSessionRepository.findByStatus(
                AuctionStatus.SCHEDULED,
                AuctionStatus.STARTED
        );
    }
}
