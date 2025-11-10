package auction.services;

import auction.models.Auction;
import auction.models.Lot;

public class AuctionService {
    // Fields

    // Constructors

    // Methods
    public Lot receiveLot(String name) {
        return new Lot(name);
    }

    public Auction createAuction(Lot lot) {
        return new Auction(lot);
    }
}
