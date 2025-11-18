package auction.usecases;

import auction.domain.AuctionSession;
import auction.domain.Lot;

import java.util.ArrayList;
import java.util.List;

public class StaffLobbyServices {
    // Service hub
    private final LotService lotService = LotService.getInstance();

    // Fields
    private List<AuctionSession> auctionList;

    // Singleton
    private static final StaffLobbyServices INSTANCE = new StaffLobbyServices();
    private StaffLobbyServices() {
        this.auctionList = new ArrayList<>();
        System.out.println("StaffLobbyServices initialized");
    }
    public static StaffLobbyServices getInstance() {
        return INSTANCE;
    }

    // Methods
    public AuctionSession createAuctionSession(String title, int lotID) {
        Lot lot = lotService.getLot(lotID);
        AuctionSession auction = new AuctionSession(lot);

        // Add to list
        this.auctionList.add(auction);

        return auction;
    }

    public List<AuctionSession> getAllAuctionSessions() {
        return new ArrayList<>(this.auctionList);
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
