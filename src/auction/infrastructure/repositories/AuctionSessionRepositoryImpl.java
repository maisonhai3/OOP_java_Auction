package auction.infrastructure.repositories;

import auction.domain.AuctionSession;
import auction.domain.AuctionStatus;
import auction.domain.repositories.IAuctionSessionRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In-memory implementation of IAuctionSessionRepository
 * Uses Spring's @Repository annotation for automatic component scanning
 */
@Repository
public class AuctionSessionRepositoryImpl implements IAuctionSessionRepository {
    private final Map<String, AuctionSession> sessionStore = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public AuctionSession save(AuctionSession session) {
        if (session == null) {
            throw new IllegalArgumentException("AuctionSession cannot be null");
        }

        // Generate ID if new session
        if (session.getAuctionId() == null || session.getAuctionId().trim().isEmpty()) {
            session.setAuctionId("AUCTION-" + idCounter.getAndIncrement());
        }

        sessionStore.put(session.getAuctionId(), session);
        return session;
    }

    @Override
    public Optional<AuctionSession> findById(String auctionId) {
        if (auctionId == null || auctionId.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(sessionStore.get(auctionId));
    }

    @Override
    public List<AuctionSession> findAll() {
        return new ArrayList<>(sessionStore.values());
    }

    @Override
    public List<AuctionSession> findByStatus(AuctionStatus status) {
        if (status == null) {
            return Collections.emptyList();
        }
        return sessionStore.values().stream()
                .filter(session -> status.equals(session.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(String auctionId) {
        if (auctionId == null || auctionId.trim().isEmpty()) {
            return false;
        }
        return sessionStore.remove(auctionId) != null;
    }

    @Override
    public boolean existsById(String auctionId) {
        if (auctionId == null || auctionId.trim().isEmpty()) {
            return false;
        }
        return sessionStore.containsKey(auctionId);
    }

    /**
     * Clear all sessions (useful for testing)
     */
    public void clear() {
        sessionStore.clear();
        idCounter.set(1);
    }

    /**
     * Get count of sessions
     */
    public int count() {
        return sessionStore.size();
    }
}