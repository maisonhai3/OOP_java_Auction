package auction.models;

public class Auctioner {
    // Fields
    private User userAccount;

    // Constructors

    // Methods
    public Auction createAuction() {
        return new Auction();
    }

    public Boolean startAuction() {
        return true;
    }

    public Boolean stopAuction() {
        return true;
    }

    public Boolean placeBid() {
        return true;
    }
}
