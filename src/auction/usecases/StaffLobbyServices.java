package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.Lot;

import java.util.List;

public class StaffLobbyServices {
    // Service hub
    private final LotService lotService = LotService.getInstance();

    // Fields
    private List<AuctionSession> auctionList;

    // Singleton
    private static final StaffLobbyServices INSTANCE = new StaffLobbyServices();
    private StaffLobbyServices() {
        System.out.println("StaffLobbyServices initialized");
    }
    public static StaffLobbyServices getInstance() {
        return INSTANCE;
    }

    // Methods
    public AuctionSession createAuctionSession(String title, int lotID) {
        Lot lot = lotService.getLot(lotID);
        AuctionSession auction = new AuctionSession(lot);

        return auction;
    }

    public List<AuctionSession> getAvailableAuctionSession() {
        return this.auctionList.stream()
                .filter(auctionSession -> {
                    switch (auctionSession.getStatus()) {
                        case SCHEDULED, STARTED -> {
                            return true;
                        }
                        default -> {
                            return false;
                        }
                    }
                })
                .toList();
    }
}
