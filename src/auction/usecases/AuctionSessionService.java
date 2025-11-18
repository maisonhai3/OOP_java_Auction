package auction.usecases;

import auction.domain.AuctionSession;

public class AuctionSessionService {
    // Fields

    // Constructors

    // Methods
    public AuctionSession createAuctionSession(String title, String lotID) {
        return new AuctionSession();
    }
}
