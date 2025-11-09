package auction.services;

public class ReceptionService {
    // Fields
    private UserService userService;

    // Methods
    public String createUserAccount(String username) {
        userService.createUser();
        return username;
    }
}
