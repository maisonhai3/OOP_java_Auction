package auction.domain;

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
    private Float hammerPrice;
    private Float buyerPremiumFee;


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
    }

    // Constructor for repository (when loading from database)
    public AuctionSession() {
        this.catalog = new java.util.ArrayList<>();
        this.status = AuctionStatus.SCHEDULED;
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
}
