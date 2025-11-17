package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.AuctionStatus;
import auction.domain.Lot;
import auction.domain.repositories.IAuctionSessionRepository;
import auction.domain.repositories.ILotRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Auction session management use case service
 * Contains logic moved from Auctioneer entity (following Clean Architecture)
 * Coordinates between repositories and domain entities
 */
@Service
public class AuctionSessionService {
    private final IAuctionSessionRepository auctionRepository;
    private final ILotRepository lotRepository;

    // Constructor injection
    public AuctionSessionService(IAuctionSessionRepository auctionRepository,
                                 ILotRepository lotRepository) {
        this.auctionRepository = auctionRepository;
        this.lotRepository = lotRepository;
    }

    /**
     * Receive a new lot for auction
     *
     * @param name          Lot name
     * @param estimatePrice Estimated price
     * @param reservePrice  Reserve price
     * @return Created lot
     */
    public Lot receiveLot(String name, Float estimatePrice, Float reservePrice) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Lot name cannot be empty");
        }

        if (lotRepository.existsByName(name)) {
            throw new IllegalArgumentException("Lot already exists: " + name);
        }

        Lot lot = new Lot(name, estimatePrice, reservePrice);
        return lotRepository.save(lot);
    }

    /**
     * Create a new auction session
     *
     * @param title   Auction title
     * @param lotName Name of the lot to auction
     * @return Created auction session
     */
    public AuctionSession createAuctionSession(String title, String lotName) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Auction title cannot be empty");
        }

        // Find lot by name from repository
        Optional<Lot> lotOpt = lotRepository.findByName(lotName);
        if (lotOpt.isEmpty()) {
            throw new IllegalArgumentException("Lot not found: " + lotName);
        }

        Lot lot = lotOpt.get();
        AuctionSession session = new AuctionSession(lot);
        session.setTitle(title);
        session.setStatus(AuctionStatus.PENDING);

        return auctionRepository.save(session);
    }

    /**
     * Start an auction (moved from Auctioneer entity)
     *
     * @param auctionId ID of auction to start
     * @return true if started successfully
     */
    public Boolean startAuction(String auctionId) {
        Optional<AuctionSession> sessionOpt = auctionRepository.findById(auctionId);
        if (sessionOpt.isEmpty()) {
            throw new IllegalArgumentException("Auction not found: " + auctionId);
        }

        AuctionSession session = sessionOpt.get();
        session.setStatus(AuctionStatus.LIVE);
        session.setStartTime(new Date());
        auctionRepository.save(session);

        return true;
    }

    /**
     * Stop an auction (moved from Auctioneer entity)
     *
     * @param auctionId ID of auction to stop
     * @return true if stopped successfully
     */
    public Boolean stopAuction(String auctionId) {
        Optional<AuctionSession> sessionOpt = auctionRepository.findById(auctionId);
        if (sessionOpt.isEmpty()) {
            throw new IllegalArgumentException("Auction not found: " + auctionId);
        }

        AuctionSession session = sessionOpt.get();
        session.setStatus(AuctionStatus.CLOSED);
        session.setEndTime(new Date());
        auctionRepository.save(session);

        return true;
    }

    /**
     * Get all auction sessions
     *
     * @return List of all auctions
     */
    public List<AuctionSession> getAllAuctions() {
        return auctionRepository.findAll();
    }

    /**
     * Get auctions by status
     *
     * @param status Auction status
     * @return List of auctions with given status
     */
    public List<AuctionSession> getAuctionsByStatus(AuctionStatus status) {
        return auctionRepository.findByStatus(status);
    }

    /**
     * Find auction by ID
     *
     * @param auctionId Auction ID
     * @return Optional containing auction if found
     */
    public Optional<AuctionSession> findAuctionById(String auctionId) {
        return auctionRepository.findById(auctionId);
    }

    /**
     * Get all lots
     *
     * @return List of all lots
     */
    public List<Lot> getAllLots() {
        return lotRepository.findAll();
    }
}
