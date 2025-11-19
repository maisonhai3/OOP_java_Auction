package auction.domain;

public class Bid {
    private Float amount;
    private User bidder;

    public Bid(Float amount, String userId) {
        this.amount = amount;
        this.bidder = new User(userId);
    }

    public Float getAmount() {
        return amount;
    }

    public User getBidder() {
        return bidder;
    }
}
