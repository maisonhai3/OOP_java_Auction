package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.Lot;

public class AuctionSessionService {
    // Singleton
    private static final AuctionSessionService INSTANCE = new AuctionSessionService();

    // Fields
    private final LotService lotService = LotService.getInstance();

    // Private Constructor
    private AuctionSessionService() {
    }

    public static AuctionSessionService getInstance() {
        return INSTANCE;
    }

    // Methods
    public AuctionSession createAuctionSession(String title, int lotID) {
        Lot lot = lotService.getLot(lotID);
        return new AuctionSession(lot);
    }
}
