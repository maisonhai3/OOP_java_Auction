package auction.usecases;

public class AuctionSessionService {
    // Singleton
    private static final AuctionSessionService INSTANCE = new AuctionSessionService();

    // Fields

    // Private Constructor
    private AuctionSessionService() {
    }

    public static AuctionSessionService getInstance() {
        return INSTANCE;
    }


}
