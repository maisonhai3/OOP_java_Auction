package auction.usecases;

public class AuctioneerLobbyService {
    // Singleton
    private static final AuctioneerLobbyService INSTANCE = new AuctioneerLobbyService();

    // Private Constructor
    private AuctioneerLobbyService() {
    }

    public static AuctioneerLobbyService getInstance() {
        return INSTANCE;
    }

    // Methods
}
