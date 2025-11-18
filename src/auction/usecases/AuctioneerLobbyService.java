package auction.usecases;

public class AuctioneerLobbyService {
    // Fields

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
