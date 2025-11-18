package auction.usecases;

public class NotificationService {
    // Singleton
    private static final NotificationService INSTANCE = new NotificationService();

    // Private Constructor
    private NotificationService() {
    }

    public static NotificationService getInstance() {
        return INSTANCE;
    }

    // Methods
}
