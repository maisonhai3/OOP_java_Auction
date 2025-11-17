package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.AuctionStatus;
import auction.domain.repositories.IAuctionSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lobby management service
 * Spring manages this as a singleton automatically (@Service annotation)
 * No need for manual singleton pattern with Spring
 */
@Service
public class LobbyServices {
    private final IAuctionSessionRepository auctionRepository;

    // Constructor injection (Spring automatically injects dependencies)
    public LobbyServices(IAuctionSessionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
        System.out.println("LobbyServices initialized with Spring DI");
    }

    /**
     * Get all auction sessions in the lobby
     *
     * @return List of all auction sessions
     */
    public List<AuctionSession> getAuctionSessionList() {
        return auctionRepository.findAll();
    }

    /**
     * Get only active/live auction sessions
     *
     * @return List of live auctions
     */
    public List<AuctionSession> getLiveAuctionSessions() {
        return auctionRepository.findByStatus(AuctionStatus.LIVE);
    }

    /**
     * Get count of all auction sessions
     *
     * @return Count of sessions
     */
    public int getAuctionSessionCount() {
        return auctionRepository.findAll().size();
    }

    /**
     * Get count of live auction sessions
     *
     * @return Count of live sessions
     */
    public int getLiveAuctionSessionCount() {
        return auctionRepository.findByStatus(AuctionStatus.LIVE).size();
    }
}
