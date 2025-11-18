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

    // PRIVATE constructor - only accessible via Builder
    private Lot(LotBuilder builder) {
        this.name = builder.name;
        this.estimatePrice = builder.estimatePrice;
        this.reservePrice = builder.reservePrice;
        this.status = LotStatus.UNSOLD; // Default status
        this.noReserve = false; // Default value
    }

    // Getters
    public String getName() {
        return name;
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

    public Float getReservePrice() {
        return reservePrice;
    }

    public Boolean getNoReserve() {
        return noReserve;
    }

    public void setNoReserve(Boolean noReserve) {
        this.noReserve = noReserve;
    }

    // Builder Pattern
    public static class LotBuilder {
        // Required field
        private final String name;

        // Optional fields
        private Float estimatePrice = null;
        private Float reservePrice = null;

        // Constructor - only requires mandatory fields
        public LotBuilder(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name is required");
            }
            this.name = name;
        }

        // Fluent methods for optional fields
        public LotBuilder estimatePrice(Float price) {
            this.estimatePrice = price;
            return this;
        }

        public LotBuilder reservePrice(Float price) {
            this.reservePrice = price;
            return this;
        }

        // Build method - creates the Lot
        public Lot build() {
            return new Lot(this);
        }
    }
}
