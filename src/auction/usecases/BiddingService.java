package auction.usecases;

public class BiddingService {
    // Singleton
    private static final BiddingService INSTANCE = new BiddingService();

    // Private Constructor
    private BiddingService() {
    }

    public static BiddingService getInstance() {
        return INSTANCE;
    }

    // Methods
}
