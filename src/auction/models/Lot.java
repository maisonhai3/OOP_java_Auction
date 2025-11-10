package auction.models;

public class Lot {
    // Fields
    private String name;
    private LotStatus status;
    private Float estimatePrice;

    // The confidential minimum price that the consignor is willing to accept.
    // If bidding doesn't reach the reserve, the item is not sold.
    private Float reservePrice;

    private Boolean noReserve;

    // Constructors
    public Lot(String name, Float estimatePrice, Float reservePrice) {
        this.name = name;
        this.estimatePrice = estimatePrice;
        this.reservePrice = reservePrice;
    }

    public Lot(String name) {
        this(name, null, null);
    }

    // Methods
}
