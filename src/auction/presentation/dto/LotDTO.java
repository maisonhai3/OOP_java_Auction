package auction.presentation.dto;

import auction.domain.Lot;

/**
 * Data Transfer Object for Lot
 * Separates domain entities from presentation layer
 */
public class LotDTO {
    private final String name;
    private String status;
    private Float estimatePrice;
    private Float reservePrice;

    // Constructor from domain entity
    public LotDTO(Lot lot) {
        this.name = lot.getName();
        this.status = lot.getStatus() != null ? lot.getStatus().toString() : null;
        this.estimatePrice = lot.getEstimatePrice();
        this.reservePrice = lot.getReservePrice();
    }

    // Constructor with fields
    public LotDTO(String name, String status, Float estimatePrice, Float reservePrice) {
        this.name = name;
        this.status = status;
        this.estimatePrice = estimatePrice;
        this.reservePrice = reservePrice;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Float getEstimatePrice() {
        return estimatePrice;
    }

    public Float getReservePrice() {
        return reservePrice;
    }

    // Static factory method
    public static LotDTO fromDomain(Lot lot) {
        return new LotDTO(lot);
    }

    @Override
    public String toString() {
        return "LotDTO{" +
                "name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", estimatePrice=" + estimatePrice +
                ", reservePrice=" + reservePrice +
                '}';
    }
}