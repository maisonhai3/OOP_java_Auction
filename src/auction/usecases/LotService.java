package auction.usecases;

import auction.domain.Lot;

import java.util.ArrayList;
import java.util.List;

public class LotService {
    public List<Lot> lotsInStorage;

    // Singleton
    public static final LotService INSTANCE = new LotService();

    private LotService() {
        this.lotsInStorage = new ArrayList<>();
    }

    public LotService getInstance() {
        return INSTANCE;
    }

}
