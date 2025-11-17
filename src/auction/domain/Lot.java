package auction.domain;

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

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LotStatus getStatus() {
        return status;
    }

    public void setStatus(LotStatus status) {
        this.status = status;
    }

    public Float getEstimatePrice() {
        return estimatePrice;
    }

    public void setEstimatePrice(Float estimatePrice) {
        this.estimatePrice = estimatePrice;
    }

    public Float getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(Float reservePrice) {
        this.reservePrice = reservePrice;
    }

    public Boolean getNoReserve() {
        return noReserve;
    }

    public void setNoReserve(Boolean noReserve) {
        this.noReserve = noReserve;
    }
}
