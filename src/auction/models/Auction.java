package auction.models;

import java.util.Date;
import java.util.List;

public class Auction {
    // Fields
    private String auctionId;

    private String title;
    private List<User> participants;
    private List<Item> items;

    private Date startTime;
    private Date endTime;

    private AuctionStatus status;

    // Constructors
    public Auction() {
    }

    // Methods
}
