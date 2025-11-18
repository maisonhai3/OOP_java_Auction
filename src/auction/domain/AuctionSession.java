package auction.domain;

import java.util.Date;
import java.util.List;

public class AuctionSession {
    // Fields
    private String auctionId;

    private String title;
    private List<User> participants;
    private List<Lot> catalog;

    private AuctionStatus status;
    private Date startTime;
    private Date endTime;

    private Float openingBid;
    private Float bidIncrement;
    private Bid bid;
    private Bid currentBid;

    private Float hammerPrice;
    private Float buyerPremiumFee;


    // Constructors
    public AuctionSession(Lot lot) {
        this.catalog = new java.util.ArrayList<>();
        if (lot != null) {
            this.catalog.add(lot);
        }
        this.status = AuctionStatus.SCHEDULED;
    }

    // Constructor for repository (when loading from database)
    public AuctionSession() {
        this.catalog = new java.util.ArrayList<>();
        this.status = AuctionStatus.SCHEDULED;
    }

    // Getters
    public String getTitle() {
        return this.title;
    }

    public AuctionStatus getStatus() {
        return this.status;
    }

    public List<Lot> getCatalog() {
        return this.catalog;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public void setCatalog(List<Lot> catalog) {
        this.catalog = catalog;
    }
}
