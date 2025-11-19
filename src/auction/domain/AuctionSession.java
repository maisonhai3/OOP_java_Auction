package auction.domain;

import auction.domain.enums.AuctionStatus;
import auction.domain.enums.BIDDING_EVENT;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.List;

import static java.lang.Math.max;

public class AuctionSession {
    // Fields
    private Integer id; // Database ID
    private String auctionId;

    private String title;
    private List<User> participants;
    private List<Lot> catalog;

    private AuctionStatus status;
    private Date startTime;
    private Date endTime;

    private Float openingBid;
    private Float bidIncrement;

    // Going once, going twice, sold to...
    private Bid bid;
    private Bid currentBid;
    private Float currentPrice = 0f;

    private Float hammerPrice;
    private Float buyerPremiumFee;

    // Helpers
    // Observer Pattern
    private PropertyChangeSupport supporter;

    // Constructors
    public AuctionSession(Lot lot) {
        this.catalog = new java.util.ArrayList<>();
        if (lot != null) {
            this.catalog.add(lot);
        }
        this.status = AuctionStatus.SCHEDULED;

        PriceRange priceRange = lot.getEstimateRange();

        this.openingBid = priceRange.hasValue() ?
                priceRange.getMinPrice() : Float.valueOf(0.0f);

        this.bidIncrement = max(0, this.openingBid * 0.05f); // 5% of opening bid

        // Observer Pattern
        this.supporter = new PropertyChangeSupport(this);
    }

    // Constructor for repository (when loading from database)
    public AuctionSession() {
        this.catalog = new java.util.ArrayList<>();
        this.status = AuctionStatus.SCHEDULED;

        // Observer Pattern
        this.supporter = new PropertyChangeSupport(this);
    }

    // Getters
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AuctionStatus getStatus() {
        return this.status;
    }

    public List<Lot> getCatalog() {
        return this.catalog;
    }

    public Float getOpeningBid() {
        return this.openingBid;
    }

    public Bid getCurrentBid() {
        return this.currentBid;
    }

    public Float getCurrentPrice() {
        return this.currentPrice;
    }

    public Float getBidIncrement() {
        return this.bidIncrement;
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

    public void setOpeningBid(Float openingBid) {
        this.openingBid = openingBid;
    }

    public void setBidIncrement(Float bidIncrement) {
        this.bidIncrement = bidIncrement;
    }

    //  --- Observer Pattern ---
    public void addObserver(PropertyChangeListener pcl) {
        this.supporter.addPropertyChangeListener(pcl);
    }

    public void removeObserver(PropertyChangeListener pcl) {
        this.supporter.removePropertyChangeListener(pcl);
    }

    //  --- Auction Lifecycle Methods ---
    public void startAuction() {
        // Validate
        if (this.status != AuctionStatus.SCHEDULED) {
            throw new IllegalStateException("Auction can only be started from SCHEDULED status. Current status: " + this.status);
        }

        // Change status
        AuctionStatus oldStatus = this.status;
        this.status = AuctionStatus.STARTED;
        this.startTime = new Date();

        // Notify all observers
        supporter.firePropertyChange(BIDDING_EVENT.AUCTION_STARTED.toString(), oldStatus, AuctionStatus.STARTED);
    }

    public void closeAuction() {
        // Validate
        if (this.status != AuctionStatus.STARTED && this.status != AuctionStatus.IN_FAIR_WARNING) {
            throw new IllegalStateException("Auction can only be closed from STARTED or IN_FAIR_WARNING status. Current status: " + this.status);
        }

        // Change status
        AuctionStatus oldStatus = this.status;
        this.status = AuctionStatus.CLOSED;
        this.endTime = new Date();

        // Set hammer price to current price
        this.hammerPrice = this.currentPrice;

        // Notify all observers with the winning bid information (SOLD event)
        supporter.firePropertyChange(BIDDING_EVENT.SOLD.toString(), oldStatus, this.currentBid);
    }

    public void placeBid(Bid newBid) {
        // Validate
        if (newBid == null) {
            throw new IllegalArgumentException("Bid cannot be null");
        }
        if (this.status != AuctionStatus.STARTED) {
            throw new IllegalStateException("Auction is not active. Current status: " + this.status);
        }
        if (currentBid != null && newBid.getAmount() <= currentBid.getAmount()) {
            throw new IllegalArgumentException("Bid must be higher than current bid");
        }
        if (newBid.getAmount() < openingBid) {
            throw new IllegalArgumentException("Bid must be at least the opening bid");
        }

        // Main
        Bid oldBid = this.currentBid;
        this.currentBid = newBid;
        this.currentPrice = newBid.getAmount();

        supporter.firePropertyChange(BIDDING_EVENT.BID_UPDATE.toString(), oldBid, newBid);
    }
}
