package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.Lot;

public class AuctionSessionService {
    // Fields
    private final LotService lotService = LotService.getInstance();

    // Constructors

    // Methods
    public AuctionSession createAuctionSession(String title, int lotID) {
        Lot lot = lotService.getLot(lotID);
        return new AuctionSession(lot);
    }
}
