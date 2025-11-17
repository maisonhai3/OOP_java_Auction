package auction.presentation;

import auction.usecases.LobbyServices;
import auction.usecases.UserService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * End User Application UI
 * Spring-managed component with dependency injection
 * Following Clean Architecture: Presentation layer
 */
@Component
public class EndUserApp {
    // Fields
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Dependencies injected by Spring
    private final UserService userService;
    private final LobbyServices lobbyServices;

    String usernameText = "";

    // Modern Color Scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Professional Blue
    private static final Color PRIMARY_DARK = new Color(31, 97, 141);        // Darker Blue
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);       // Green
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Light Gray
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);           // Dark Gray
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);        // Light Gray Text
    private static final Color DANGER_COLOR = new Color(231, 76, 60);        // Red

    // Fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font HEADING_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font SUBHEADING_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    // Constructor with dependency injection (Spring will call this)
    public EndUserApp(UserService userService, LobbyServices lobbyServices) {
        this.userService = userService;
        this.lobbyServices = lobbyServices;
        System.out.println("EndUserApp created with Spring DI");
        initializeUI();
    }

    /**
     * Initialize the UI (called from constructor)
     */
    private void initializeUI() {
        // Main Window
        frame = new JFrame("Going Going Gone - Auction System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 500));
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);

        // Panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BACKGROUND_COLOR);

        // Add all your scenes as separate panels
        cardPanel.add(createWelcomePanel(), "LOGIN");
        cardPanel.add(createLobbyPanel(), "LOBBY");
        cardPanel.add(createRoomPanel(), "ROOM");

        // Add to frame but don't show yet (will be shown by AuctionApplication)
        frame.add(cardPanel);
    }

    /**
     * Show the UI (called after Spring context is ready)
     */
    public void show() {
        frame.setVisible(true);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        // Create card-style login box
        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBackground(CARD_BACKGROUND);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        loginCard.setMaximumSize(new Dimension(450, 500));

        // App Title
        JLabel appTitle = new JLabel("Going Going Gone");
        appTitle.setFont(TITLE_FONT);
        appTitle.setForeground(PRIMARY_COLOR);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitle = new JLabel("Auction Management System");
        subtitle.setFont(LABEL_FONT);
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Spacer
        loginCard.add(Box.createVerticalStrut(30));
        loginCard.add(appTitle);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(subtitle);
        loginCard.add(Box.createVerticalStrut(40));

        // Username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field
        JTextField username = new JTextField(20);
        username.setFont(SUBHEADING_FONT);
        username.setMaximumSize(new Dimension(350, 40));
        username.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        loginCard.add(usernameLabel);
        loginCard.add(Box.createVerticalStrut(8));
        loginCard.add(username);
        loginCard.add(Box.createVerticalStrut(25));

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setMaximumSize(new Dimension(350, 45));
        buttonPanel.setOpaque(false);

        // Login Button
        JButton loginButton = createStyledButton("Log In", PRIMARY_COLOR, Color.WHITE);
        loginButton.addActionListener(e -> {
            this.usernameText = username.getText();
            if (!usernameText.trim().isEmpty()) {
                userService.logInUser(usernameText);
                cardLayout.show(cardPanel, "LOBBY");
            }
        });

        // Create Account Button
        JButton createButton = createStyledButton("Create Account", Color.WHITE, PRIMARY_COLOR);
        createButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        createButton.addActionListener(e -> {
            this.usernameText = username.getText();
            if (!usernameText.trim().isEmpty()) {
                userService.createUser(usernameText);
                cardLayout.show(cardPanel, "LOBBY");
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(createButton);

        loginCard.add(buttonPanel);
        loginCard.add(Box.createVerticalStrut(20));

        // Add card to main panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(loginCard, gbc);

        return panel;
    }

    private JPanel createLobbyPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel title = new JLabel("Auction Lobby");
        title.setFont(HEADING_FONT);
        title.setForeground(TEXT_COLOR);

        JLabel welcomeLabel = new JLabel("Welcome, " + usernameText);
        welcomeLabel.setFont(LABEL_FONT);
        welcomeLabel.setForeground(TEXT_LIGHT);

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        // Main content area - List of available auctions
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Example auction cards (you can populate this dynamically later)
        for (int i = 1; i <= 3; i++) {
            contentPanel.add(createAuctionCard("Auction Room #" + i, "Live", i * 5 + " items"));
            contentPanel.add(Box.createVerticalStrut(15));
        }

        // Bottom button panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JButton joinButton = createStyledButton("Join New Auction", ACCENT_COLOR, Color.WHITE);
        joinButton.setPreferredSize(new Dimension(200, 45));
        joinButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "ROOM");
        });

        JButton logoutButton = createStyledButton("Logout", Color.WHITE, DANGER_COLOR);
        logoutButton.setPreferredSize(new Dimension(120, 45));
        logoutButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DANGER_COLOR, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        logoutButton.addActionListener(e -> {
            usernameText = "";
            cardLayout.show(cardPanel, "LOGIN");
        });

        bottomPanel.add(joinButton);
        bottomPanel.add(logoutButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createAuctionCard(String name, String status, String items) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Left side - Auction info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(SUBHEADING_FONT);
        nameLabel.setForeground(TEXT_COLOR);

        JLabel detailsLabel = new JLabel(items + " - " + status);
        detailsLabel.setFont(LABEL_FONT);
        detailsLabel.setForeground(TEXT_LIGHT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(detailsLabel);

        // Right side - Join button
        JButton joinBtn = createStyledButton("Join", PRIMARY_COLOR, Color.WHITE);
        joinBtn.setPreferredSize(new Dimension(100, 35));
        joinBtn.addActionListener(e -> {
            cardLayout.show(cardPanel, "ROOM");
        });

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(joinBtn, BorderLayout.EAST);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(245, 247, 248));
            }

            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_BACKGROUND);
            }
        });

        return card;
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel title = new JLabel("Auction Room #1");
        title.setFont(HEADING_FONT);
        title.setForeground(TEXT_COLOR);

        JLabel statusLabel = new JLabel("LIVE - 12 Bidders");
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(ACCENT_COLOR);

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(statusLabel, BorderLayout.EAST);

        // Main content area - Split into two sections
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Left side - Current Lot Info
        JPanel lotPanel = new JPanel();
        lotPanel.setLayout(new BoxLayout(lotPanel, BoxLayout.Y_AXIS));
        lotPanel.setBackground(CARD_BACKGROUND);
        lotPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel lotTitle = new JLabel("Current Lot");
        lotTitle.setFont(SUBHEADING_FONT);
        lotTitle.setForeground(TEXT_COLOR);
        lotTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lotName = new JLabel("Vintage Watch Collection");
        lotName.setFont(HEADING_FONT);
        lotName.setForeground(PRIMARY_COLOR);
        lotName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel currentBid = new JLabel("Current Bid: $1,250");
        currentBid.setFont(SUBHEADING_FONT);
        currentBid.setForeground(TEXT_COLOR);
        currentBid.setAlignmentX(Component.LEFT_ALIGNMENT);

        lotPanel.add(lotTitle);
        lotPanel.add(Box.createVerticalStrut(15));
        lotPanel.add(lotName);
        lotPanel.add(Box.createVerticalStrut(10));
        lotPanel.add(currentBid);
        lotPanel.add(Box.createVerticalGlue());

        // Right side - Bidding Panel
        JPanel biddingPanel = new JPanel();
        biddingPanel.setLayout(new BoxLayout(biddingPanel, BoxLayout.Y_AXIS));
        biddingPanel.setBackground(CARD_BACKGROUND);
        biddingPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel bidTitle = new JLabel("Place Your Bid");
        bidTitle.setFont(SUBHEADING_FONT);
        bidTitle.setForeground(TEXT_COLOR);
        bidTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField bidField = new JTextField(15);
        bidField.setFont(SUBHEADING_FONT);
        bidField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        bidField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        bidField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton placeBidButton = createStyledButton("Place Bid", ACCENT_COLOR, Color.WHITE);
        placeBidButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        placeBidButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        biddingPanel.add(bidTitle);
        biddingPanel.add(Box.createVerticalStrut(15));
        biddingPanel.add(bidField);
        biddingPanel.add(Box.createVerticalStrut(15));
        biddingPanel.add(placeBidButton);
        biddingPanel.add(Box.createVerticalGlue());

        contentPanel.add(lotPanel);
        contentPanel.add(biddingPanel);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JButton backButton = createStyledButton("Back to Lobby", PRIMARY_COLOR, Color.WHITE);
        backButton.setPreferredSize(new Dimension(200, 45));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "LOBBY"));

        JButton leaveButton = createStyledButton("Leave Auction", Color.WHITE, DANGER_COLOR);
        leaveButton.setPreferredSize(new Dimension(150, 45));
        leaveButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DANGER_COLOR, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        leaveButton.addActionListener(e -> cardLayout.show(cardPanel, "LOBBY"));

        bottomPanel.add(backButton);
        bottomPanel.add(leaveButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (bgColor.equals(Color.WHITE)) {
                    button.setBackground(new Color(245, 247, 248));
                } else {
                    button.setBackground(bgColor.darker());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }
}
