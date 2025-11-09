package auction.models;

public class Lot {
    // Fields
    private LotStatus status;
    private Float estimatePrice;

    // The confidential minimum price that the consignor is willing to accept.
    // If bidding doesn't reach the reserve, the item is not sold.
    private Float reservePrice;

    private Boolean noReserve;
    // Constructors

    // Methods
}
