package auction.usecases;

public class StaffLobbyServices {
    // Singleton
    private static final StaffLobbyServices INSTANCE = new StaffLobbyServices();

    // Private Constructor
    private StaffLobbyServices() {
    }

    public static StaffLobbyServices getInstance() {
        return INSTANCE;
    }

    // Methods
}
