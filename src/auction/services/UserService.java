package auction.services;

import auction.models.BigMamaManager;
import auction.models.User;
import auction.models.UserType;

public class UserService {
    // Fields
    private BigMamaManager bigMamaManager;

    // Constructors
    public UserService(BigMamaManager bigMmManager) {
        bigMamaManager = bigMmManager;
    }

    // Methods
    public User createUser(String username) {
        return new User(username);
    }

    public Boolean changeUserType(User user, UserType targetType) {
        return true;
    }
}
