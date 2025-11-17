package auction.domain;

/**
 * Auctioneer domain entity
 * Represents an auctioneer role in the system
 * Business logic moved to AuctionSessionService (following Clean Architecture)
 */
public class Auctioneer {
    // Fields
    private User userAccount;

    // Constructors
    public Auctioneer() {
    }

    public Auctioneer(User userAccount) {
        this.userAccount = userAccount;
    }

    // Getters and Setters
    public User getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;
    }
}
