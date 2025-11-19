package auction.domain;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Value object representing a price range with minimum and maximum values.
 * Immutable and ensures min <= max when both are provided.
 */
public class PriceRange {
    private final Float minPrice;
    private final Float maxPrice;

    public PriceRange(Float minPrice, Float maxPrice) {
        // Validation: if both are provided, min must be <= max
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public Float getMinPrice() {
        return minPrice;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }

    /**
     * Check if this price range has any value
     */
    public boolean hasValue() {
        return minPrice != null || maxPrice != null;
    }

    /**
     * Format the price range as a display string
     * Examples:
     * - "$1,000 - $2,000" (both min and max)
     * - "$1,000+" (only min)
     * - "Up to $2,000" (only max)
     * - "No estimate" (neither)
     */
    public String toDisplayString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);

        if (minPrice != null && maxPrice != null) {
            return formatter.format(minPrice) + " - " + formatter.format(maxPrice);
        } else if (minPrice != null) {
            return formatter.format(minPrice) + "+";
        } else if (maxPrice != null) {
            return "Up to " + formatter.format(maxPrice);
        } else {
            return "No estimate";
        }
    }

    @Override
    public String toString() {
        return toDisplayString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceRange that = (PriceRange) o;
        return java.util.Objects.equals(minPrice, that.minPrice) &&
                java.util.Objects.equals(maxPrice, that.maxPrice);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(minPrice, maxPrice);
    }
}

