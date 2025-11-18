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
    public void addLot(Lot lot) {
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
