package auction.services;

import auction.models.AuctionSession;
import auction.models.Lot;

public class AuctionSessionService {
    // Fields

    // Constructors

    // Methods
    public Lot receiveLot(String name) {
        return new Lot(name);
    }

    public AuctionSession createAuction(Lot lot) {
        return new AuctionSession(lot);
    }
}
