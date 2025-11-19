package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.Bid;
import auction.domain.Lot;
import auction.infrastructure.AuctionSessionRepository;

public class AuctionSessionService {
    // Fields
    private final AuctionSessionRepository auctionSessionRepository;
    private final UserService userService;

    // Singleton
    private static final AuctionSessionService INSTANCE = new AuctionSessionService();

    private AuctionSessionService() {
        this.auctionSessionRepository = new AuctionSessionRepository();
        this.userService = UserService.getInstance();
    }

    public static AuctionSessionService getInstance() {
        return INSTANCE;
    }

    // --- Services ---

    /**
     * Get lot information for display to end users.
     * Returns a DTO that hides sensitive information like reserve price.
     *
     * @param auctionSessionId the database ID of the auction session
     * @return LotInfoDTO containing estimate range, opening bid, and bid increment
     */
    public LotInfoDTO getLotInfo(int auctionSessionId) {
        // Get auction session from repository
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId);

        if (auctionSession == null) {
            System.err.println("Auction session not found with ID: " + auctionSessionId);
            return null;
        }

        // Get the current lot (assuming first lot in catalog for now)
        if (auctionSession.getCatalog() == null || auctionSession.getCatalog().isEmpty()) {
            System.err.println("No lots found in auction session ID: " + auctionSessionId);
            return null;
        }

        Lot currentLot = auctionSession.getCatalog().get(0);

        // Create DTO with lot info (opening bid and increment already calculated by domain)
        return new LotInfoDTO(
                currentLot.getName(),
                currentLot.getEstimateRange(),
                auctionSession.getOpeningBid(),  // Calculated by AuctionSession domain logic
                auctionSession.getBidIncrement()  // Calculated by AuctionSession domain logic
        );
    }

    public void placeBidCustomAmount(int auctionSessionId, float bidAmount, String bidderUserId) {
        // Validate User existence
        if (!userService.userExists(bidderUserId)) {
            System.err.println("User not found: " + bidderUserId);
            throw new IllegalArgumentException("User does not exist: " + bidderUserId);
        }

        // Validate auction session
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId);
        if (auctionSession == null) {
            System.err.println("Auction session not found with ID: " + auctionSessionId);
            throw new IllegalArgumentException("Auction session not found with ID: " + auctionSessionId);
        }

        // Main
        Bid bid = new Bid(bidAmount, bidderUserId);
        auctionSession.placeBid(bid);
        System.out.println("Placing bid of " + bidAmount + " in auction session ID: " + auctionSessionId);
    }

    public void placeBidIncremental(int auctionSessionId, String bidderUserId) {
        // Validate User existence
        if (!userService.userExists(bidderUserId)) {
            System.err.println("User not found: " + bidderUserId);
            throw new IllegalArgumentException("User does not exist: " + bidderUserId);
        }

        // Validate auction session
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId);
        if (auctionSession == null) {
            System.err.println("Auction session not found with ID: " + auctionSessionId);
            throw new IllegalArgumentException("Auction session not found with ID: " + auctionSessionId);
        }

        // Calculate new bid amount
        Float currentBidAmount = (auctionSession.getCurrentBid() != null)
                ? auctionSession.getCurrentBid().getAmount()
                : 0f;
        Float newBidAmount = Math.max(currentBidAmount + auctionSession.getBidIncrement(), auctionSession.getOpeningBid());

        // Main
        Bid bid = new Bid(newBidAmount, bidderUserId);
        auctionSession.placeBid(bid);
        System.out.println("Placing incremental bid of " + newBidAmount + " in auction session ID: " + auctionSessionId);
    }
}
