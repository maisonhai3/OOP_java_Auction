package auction.ui;

import auction.services.UserService;

import javax.swing.*;
import java.awt.*;

public class EndUserApp {
    // Fields
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    UserService userService = new UserService();
    String usernameText = "";


    // Constructors
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EndUserApp());
    }

    public EndUserApp() {
        // Main Window
        frame = new JFrame("Going Going Gone");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Add all your scenes as separate panels
        cardPanel.add(createWelcomePanel(), "LOGIN");
        cardPanel.add(createLobbyPanel(), "LOBBY");
        cardPanel.add(createRoomPanel(), "ROOM");

        // Finalize
        frame.add(cardPanel);
        frame.setVisible(true);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Create Account");

        JTextField username = new JTextField(15);

        JButton createButton = new JButton("Create Account");
        JButton loginButton = new JButton("Log In");

        createButton.addActionListener(e -> {
            this.usernameText = username.getText();
            userService.createUser(usernameText);
            cardLayout.show(cardPanel, "LOBBY");
        });

        loginButton.addActionListener(e -> {
            this.usernameText = username.getText();
            userService.logInUser(usernameText);
            cardLayout.show(cardPanel, "LOBBY");
        });

        panel.add(label);
        panel.add(username);
        panel.add(createButton);
        panel.add(loginButton);
        return panel;
    }

    private JPanel createLobbyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Auction Lobby", SwingConstants.CENTER);
        JButton joinButton = new JButton("Join Auction");

        joinButton.addActionListener(e -> {
            // Switch to auction room
            cardLayout.show(cardPanel, "ROOM");
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(joinButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Auction Room", SwingConstants.CENTER);
        JButton backButton = new JButton("Back to Lobby");

        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "LOBBY");
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(backButton, BorderLayout.SOUTH);
        return panel;
    }
}
