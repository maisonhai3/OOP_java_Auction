package auction.usecases;

import auction.domain.Lot;

import java.util.ArrayList;
import java.util.List;

public class LotService {
    private List<Lot> lotsInStorage;

    // Singleton
    private static final LotService INSTANCE = new LotService();

    private LotService() {
        this.lotsInStorage = new ArrayList<>();
    }

    public static LotService getInstance() {
        return INSTANCE;
    }

    // Methods to manage lots

    /**
     * Creates and stores a new Lot from raw string data (typically from UI).
     * This is the "backend API" that handles validation and parsing.
     *
     * @param name             The name of the lot (required)
     * @param estimatePriceStr The estimate price as a string (optional)
     * @param reservePriceStr  The reserve price as a string (optional)
     * @return The created Lot object, or null if validation fails
     */
    public Lot createLot(String name, String estimatePriceStr, String reservePriceStr) {
        // 1. Parse and validate
        Float estimatePrice = null;
        Float reservePrice = null;

        try {
            if (estimatePriceStr != null && !estimatePriceStr.trim().isEmpty()) {
                estimatePrice = Float.parseFloat(estimatePriceStr);
            }
            if (reservePriceStr != null && !reservePriceStr.trim().isEmpty()) {
                reservePrice = Float.parseFloat(reservePriceStr);
            }
        } catch (NumberFormatException e) {
            // In a real application, throw a custom exception
            // e.g., throw new InvalidInputException("Price must be a number");
            System.err.println("Error: Invalid price - " + e.getMessage());
            return null;
        }

        // 2. Use Builder to create domain object
        // Service is the ONLY place that knows about Builder
        Lot lot = new Lot.LotBuilder(name)
                .estimatePrice(estimatePrice)
                .reservePrice(reservePrice)
                .build();

        // 3. Store the object
        this.addLot(lot);

        return lot;
    }

    /**
     * Internal method to add a Lot to storage.
     * Private because only the service itself should call this.
     */
    private void addLot(Lot lot) {
        lotsInStorage.add(lot);
    }

    public List<Lot> getAllLots() {
        return new ArrayList<>(lotsInStorage); // Return copy for safety
    }

    public Lot getLot(int index) {
        if (index >= 0 && index < lotsInStorage.size()) {
            return lotsInStorage.get(index);
        }
        return null;
    }

    public boolean removeLot(Lot lot) {
        return lotsInStorage.remove(lot);
    }

    public int getLotCount() {
        return lotsInStorage.size();
    }
}
