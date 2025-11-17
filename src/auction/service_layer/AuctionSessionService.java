package auction.service_layer;

import auction.business_layer.AuctionSession;
import auction.business_layer.Lot;

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
