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

    // Methods
}
