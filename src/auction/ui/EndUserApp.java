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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Create Account");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JTextField username = new JTextField(15);
        username.setPreferredSize(new Dimension(200, 30));
        JButton createButton = new JButton("Create Account");
        createButton.setPreferredSize(new Dimension(250, 60));
        JButton loginButton = new JButton("Log In");
        loginButton.setPreferredSize(new Dimension(250, 60));

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(label, gbc);

        gbc.gridy = 1;
        panel.add(username, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(createButton, gbc);

        gbc.gridx = 1;
        panel.add(loginButton, gbc);

        return panel;
    }

    private JPanel createLobbyPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Auction Lobby", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton joinButton = new JButton("Join Auction");
        joinButton.setPreferredSize(new Dimension(150, 40));

        joinButton.addActionListener(e -> {
            // Switch to auction room
            cardLayout.show(cardPanel, "ROOM");
        });

        buttonPanel.add(joinButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Auction Room", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back to Lobby");
        backButton.setPreferredSize(new Dimension(150, 40));

        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "LOBBY");
        });

        buttonPanel.add(backButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }
}
