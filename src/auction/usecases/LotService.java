package auction.usecases;

import auction.domain.Lot;
import auction.infrastructure.LotRepository;

import java.util.List;

public class LotService {
    private final LotRepository lotRepository;

    // Singleton
    private static final LotService INSTANCE = new LotService();

    private LotService() {
        this.lotRepository = new LotRepository();
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
     * @param estimateMinStr   The minimum estimate price as a string (optional)
     * @param estimateMaxStr   The maximum estimate price as a string (optional)
     * @param reservePriceStr  The reserve price as a string (optional)
     * @return The created Lot object, or null if validation fails
     */
    public Lot createLot(String name, String estimateMinStr, String estimateMaxStr, String reservePriceStr) {
        // 1. Parse and validate
        Float estimateMin = null;
        Float estimateMax = null;
        Float reservePrice = null;

        try {
            if (estimateMinStr != null && !estimateMinStr.trim().isEmpty()) {
                estimateMin = Float.parseFloat(estimateMinStr);
            }
            if (estimateMaxStr != null && !estimateMaxStr.trim().isEmpty()) {
                estimateMax = Float.parseFloat(estimateMaxStr);
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

        // Create PriceRange
        auction.domain.PriceRange estimateRange = null;
        try {
            estimateRange = new auction.domain.PriceRange(estimateMin, estimateMax);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }

        // 2. Use Builder to create domain object
        // Service is the ONLY place that knows about Builder
        Lot lot = new Lot.LotBuilder(name)
                .estimateRange(estimateRange)
                .reservePrice(reservePrice)
                .build();

        // 3. Save to database
        int lotId = lotRepository.save(lot);
        if (lotId > 0) {
            System.out.println("Lot created successfully with ID: " + lotId);
        }

        return lot;
    }

    /**
     * Get all lots from database
     */
    public List<Lot> getAllLots() {
        return lotRepository.findAll();
    }

    /**
     * Get lot by database ID
     */
    public Lot getLot(int id) {
        return lotRepository.findById(id);
    }

    /**
     * Delete lot by database ID
     */
    public boolean removeLot(int id) {
        return lotRepository.delete(id);
    }

    /**
     * Get total count of lots in database
     */
    public int getLotCount() {
        return lotRepository.count();
    }
}
