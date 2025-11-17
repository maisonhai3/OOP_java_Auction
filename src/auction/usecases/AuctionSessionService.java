package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.Lot;

public class AuctionSessionService {
    // Fields

    // Constructors

    // Methods
    public Lot receiveLot(String name) {
        return new Lot(name);
    }

    public AuctionSession createAuctionSession(String title, String lotID) {
        // Search database for the lotId;
        String lotId = "";

        Lot lot = new Lot(lotId);
        return new AuctionSession(lot);
    }
}
