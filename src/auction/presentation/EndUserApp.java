package auction.presentation;

import auction.domain.AuctionSession;
import auction.usecases.AuctionSessionService;
import auction.usecases.LotInfoDTO;
import auction.usecases.UserLobbyServices;
import auction.usecases.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EndUserApp {
    // Fields
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel lobbyContentPanel;
    private JPanel auctionRoomPanel;

    // Auction room dynamic labels
    private JLabel lotNameLabel;
    private JLabel estimateRangeLabel;
    private JLabel openingBidLabel;
    private JLabel bidIncrementLabel;

    String usernameText = "";
    int currentAuctionSessionId = -1; // Track which auction user joined

    // Services
    private final UserLobbyServices userLobbyServices = UserLobbyServices.getInstance();
    private final AuctionSessionService auctionSessionService = AuctionSessionService.getInstance();

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

    // Constructors
    public static void main(String[] args) {
        // Initialize services at app start (Singleton pattern - Eager initialization)
        System.out.println("Initializing application services...");
        UserLobbyServices.getInstance();
        UserService.getInstance();

        // Launch the UI
        SwingUtilities.invokeLater(() -> new EndUserApp());
    }

    public EndUserApp() {
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
        cardPanel.add(createAuctionRoomPanel(), "ROOM");

        // Finalize
        frame.add(cardPanel);
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
            try {
                this.usernameText = username.getText();
                System.out.println("Login button clicked, username: " + usernameText);
                if (!usernameText.trim().isEmpty()) {
                    System.out.println("Username is valid, logging in...");
                    UserService.logInUser(usernameText);
                    System.out.println("Refreshing auction list...");
                    refreshAuctionList();
                    System.out.println("Showing lobby...");
                    cardLayout.show(cardPanel, "LOBBY");
                    System.out.println("Lobby shown successfully");
                } else {
                    System.out.println("Username is empty!");
                }
            } catch (Exception ex) {
                System.err.println("Error during login:");
                ex.printStackTrace();
            }
        });

        // Create Account Button
        JButton createButton = createStyledButton("Create Account", Color.WHITE, PRIMARY_COLOR);
        createButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        createButton.addActionListener(e -> {
            try {
                this.usernameText = username.getText();
                System.out.println("Create Account button clicked, username: " + usernameText);
                if (!usernameText.trim().isEmpty()) {
                    System.out.println("Username is valid, creating account...");
                    UserService.createUser(usernameText);
                    System.out.println("Refreshing auction list...");
                    refreshAuctionList();
                    System.out.println("Showing lobby...");
                    cardLayout.show(cardPanel, "LOBBY");
                    System.out.println("Lobby shown successfully");
                } else {
                    System.out.println("Username is empty!");
                }
            } catch (Exception ex) {
                System.err.println("Error during account creation:");
                ex.printStackTrace();
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
        lobbyContentPanel = new JPanel();
        lobbyContentPanel.setLayout(new BoxLayout(lobbyContentPanel, BoxLayout.Y_AXIS));
        lobbyContentPanel.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(lobbyContentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Note: Auction list will be loaded when user logs in

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

    private JPanel createAuctionCard(AuctionSession auction, String name, String status, String items) {
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
            // Set current auction session ID and refresh the room panel
            currentAuctionSessionId = auction.getId();
            refreshAuctionRoomPanel();
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

    private JPanel createAuctionRoomPanel() {
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

        // Dynamic lot name label
        lotNameLabel = new JLabel("Loading...");
        lotNameLabel.setFont(HEADING_FONT);
        lotNameLabel.setForeground(PRIMARY_COLOR);
        lotNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Dynamic estimate range label
        estimateRangeLabel = new JLabel("Estimate: Loading...");
        estimateRangeLabel.setFont(LABEL_FONT);
        estimateRangeLabel.setForeground(TEXT_LIGHT);
        estimateRangeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Dynamic opening bid label
        openingBidLabel = new JLabel("Opening Bid: Loading...");
        openingBidLabel.setFont(SUBHEADING_FONT);
        openingBidLabel.setForeground(TEXT_COLOR);
        openingBidLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Dynamic bid increment label
        bidIncrementLabel = new JLabel("Bid Increment: Loading...");
        bidIncrementLabel.setFont(LABEL_FONT);
        bidIncrementLabel.setForeground(TEXT_LIGHT);
        bidIncrementLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        lotPanel.add(lotTitle);
        lotPanel.add(Box.createVerticalStrut(15));
        lotPanel.add(lotNameLabel);
        lotPanel.add(Box.createVerticalStrut(10));
        lotPanel.add(estimateRangeLabel);
        lotPanel.add(Box.createVerticalStrut(8));
        lotPanel.add(openingBidLabel);
        lotPanel.add(Box.createVerticalStrut(8));
        lotPanel.add(bidIncrementLabel);
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

        // Custom amount bid button
        JButton placeBidButton = createStyledButton("Place Custom Bid", ACCENT_COLOR, Color.WHITE);
        placeBidButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        placeBidButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        placeBidButton.addActionListener(e -> {
            try {
                // Validate inputs
                if (currentAuctionSessionId == -1) {
                    JOptionPane.showMessageDialog(frame,
                            "Please join an auction first",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String bidAmountStr = bidField.getText().trim();
                if (bidAmountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a bid amount",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                float bidAmount = Float.parseFloat(bidAmountStr);

                // Place custom bid
                auctionSessionService.placeBidCustomAmount(currentAuctionSessionId, bidAmount, usernameText);

                // Show success message
                JOptionPane.showMessageDialog(frame,
                        "Bid placed successfully: $" + String.format("%.2f", bidAmount),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear the bid field
                bidField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Invalid bid amount. Please enter a valid number.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Bid failed: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                        "An error occurred: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Incremental bid button
        JButton incrementalBidButton = createStyledButton("Quick Bid (+Increment)", PRIMARY_COLOR, Color.WHITE);
        incrementalBidButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        incrementalBidButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        incrementalBidButton.addActionListener(e -> {
            try {
                // Validate auction session
                if (currentAuctionSessionId == -1) {
                    JOptionPane.showMessageDialog(frame,
                            "Please join an auction first",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Place incremental bid
                auctionSessionService.placeBidIncremental(currentAuctionSessionId, usernameText);

                // Show success message
                JOptionPane.showMessageDialog(frame,
                        "Incremental bid placed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Bid failed: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame,
                        "An error occurred: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        biddingPanel.add(bidTitle);
        biddingPanel.add(Box.createVerticalStrut(15));
        biddingPanel.add(bidField);
        biddingPanel.add(Box.createVerticalStrut(15));
        biddingPanel.add(placeBidButton);
        biddingPanel.add(Box.createVerticalStrut(10));
        biddingPanel.add(incrementalBidButton);
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

    private void refreshAuctionList() {
        // Clear existing list
        lobbyContentPanel.removeAll();

        // Get real auction sessions from service
        List<AuctionSession> auctions = userLobbyServices.getAuctionSessionList();

        if (auctions.isEmpty()) {
            // Show empty state message
            JLabel emptyLabel = new JLabel("No auction sessions available");
            emptyLabel.setFont(SUBHEADING_FONT);
            emptyLabel.setForeground(TEXT_LIGHT);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            lobbyContentPanel.add(Box.createVerticalStrut(50));
            lobbyContentPanel.add(emptyLabel);
        } else {
            // Create cards for each auction session
            for (int i = 0; i < auctions.size(); i++) {
                AuctionSession auction = auctions.get(i);

                // Get auction details
                String name = auction.getTitle() != null ? auction.getTitle() : "Auction #" + (i + 1);
                String status = auction.getStatus() != null ? auction.getStatus().toString() : "PENDING";
                String items = auction.getCatalog() != null ? auction.getCatalog().size() + " items" : "0 items";

                lobbyContentPanel.add(createAuctionCard(auction, name, status, items));
                lobbyContentPanel.add(Box.createVerticalStrut(15));
            }
        }

        // Refresh the UI
        lobbyContentPanel.revalidate();
        lobbyContentPanel.repaint();
    }

    private void refreshAuctionRoomPanel() {
        // Check if an auction session has been selected
        if (currentAuctionSessionId == -1) {
            lotNameLabel.setText("No auction selected");
            estimateRangeLabel.setText("");
            openingBidLabel.setText("");
            bidIncrementLabel.setText("");
            return;
        }

        // Fetch lot information using the service
        LotInfoDTO lotInfo = auctionSessionService.getLotInfo(currentAuctionSessionId);

        if (lotInfo == null) {
            lotNameLabel.setText("Error loading lot information");
            estimateRangeLabel.setText("");
            openingBidLabel.setText("");
            bidIncrementLabel.setText("");
            return;
        }

        // Update labels with real data
        lotNameLabel.setText(lotInfo.getLotName());

        // Display estimate range
        if (lotInfo.getEstimateRange() != null && lotInfo.getEstimateRange().hasValue()) {
            estimateRangeLabel.setText("Estimate: " + lotInfo.getEstimateRange().toDisplayString());
        } else {
            estimateRangeLabel.setText("Estimate: No estimate available");
        }

        // Display opening bid
        if (lotInfo.getOpeningBid() != null) {
            openingBidLabel.setText(String.format("Opening Bid: $%.2f", lotInfo.getOpeningBid()));
        } else {
            openingBidLabel.setText("Opening Bid: Not set");
        }

        // Display bid increment
        if (lotInfo.getBidIncrement() != null) {
            bidIncrementLabel.setText(String.format("Bid Increment: $%.2f", lotInfo.getBidIncrement()));
        } else {
            bidIncrementLabel.setText("Bid Increment: Not set");
        }
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
