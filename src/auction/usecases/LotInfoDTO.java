package auction.usecases;

import auction.domain.PriceRange;

/**
 * Data Transfer Object for Lot information displayed to end users.
 * This DTO hides sensitive information like reserve price.
 */
public class LotInfoDTO {
    private final String lotName;
    private final PriceRange estimateRange;
    private final Float openingBid;
    private final Float bidIncrement;

    public LotInfoDTO(String lotName, PriceRange estimateRange,
                      Float openingBid, Float bidIncrement) {
        this.lotName = lotName;
        this.estimateRange = estimateRange;
        this.openingBid = openingBid;
        this.bidIncrement = bidIncrement;
    }

    // Getters only (immutable DTO)
    public String getLotName() {
        return lotName;
    }

    public PriceRange getEstimateRange() {
        return estimateRange;
    }

    public Float getOpeningBid() {
        return openingBid;
    }

    public Float getBidIncrement() {
        return bidIncrement;
    }
}