package auction.infrastructure.repositories;

import auction.domain.Lot;
import auction.domain.LotStatus;
import auction.domain.repositories.ILotRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of ILotRepository
 * Uses Spring's @Repository annotation for automatic component scanning
 */
@Repository
public class LotRepositoryImpl implements ILotRepository {
    private final Map<String, Lot> lotStore = new ConcurrentHashMap<>();

    @Override
    public Lot save(Lot lot) {
        if (lot == null) {
            throw new IllegalArgumentException("Lot cannot be null");
        }

        String name = lot.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Lot name cannot be null or empty");
        }

        lotStore.put(name, lot);
        return lot;
    }

    @Override
    public Optional<Lot> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(lotStore.get(name));
    }

    @Override
    public List<Lot> findAll() {
        return new ArrayList<>(lotStore.values());
    }

    @Override
    public List<Lot> findByStatus(LotStatus status) {
        if (status == null) {
            return Collections.emptyList();
        }
        return lotStore.values().stream()
                .filter(lot -> status.equals(lot.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return lotStore.remove(name) != null;
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return lotStore.containsKey(name);
    }

    /**
     * Clear all lots (useful for testing)
     */
    public void clear() {
        lotStore.clear();
    }

    /**
     * Get count of lots
     */
    public int count() {
        return lotStore.size();
    }
}