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
    }

    // Getters and Setters
    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public List<Lot> getCatalog() {
        return catalog;
    }

    public void setCatalog(List<Lot> catalog) {
        this.catalog = catalog;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Float getOpeningBid() {
        return openingBid;
    }

    public void setOpeningBid(Float openingBid) {
        this.openingBid = openingBid;
    }

    public Float getBidIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(Float bidIncrement) {
        this.bidIncrement = bidIncrement;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public Bid getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(Bid currentBid) {
        this.currentBid = currentBid;
    }

    public Float getHammerPrice() {
        return hammerPrice;
    }

    public void setHammerPrice(Float hammerPrice) {
        this.hammerPrice = hammerPrice;
    }

    public Float getBuyerPremiumFee() {
        return buyerPremiumFee;
    }

    public void setBuyerPremiumFee(Float buyerPremiumFee) {
        this.buyerPremiumFee = buyerPremiumFee;
    }
}
