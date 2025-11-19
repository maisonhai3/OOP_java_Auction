package auction.presentation;

import auction.domain.AuctionSession;
import auction.domain.Bid;
import auction.domain.enums.AuctionStatus;
import auction.domain.enums.BIDDING_EVENT;
import auction.infrastructure.AuctionSessionRepository;
import auction.usecases.AuctioneerLobbyService;
import auction.usecases.JoinAuctionSession;
import auction.usecases.StartAuctionSession;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class AuctioneerApp implements PropertyChangeListener {
    // Fields
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel auctionListPanel;
    private JPanel auctionRoomPanel;

    // Auction room dynamic labels
    private JLabel lotNameLabel;
    private JLabel statusLabel;
    private JLabel currentPriceLabel;
    private JLabel currentBidderLabel;
    private JLabel countdownLabel;

    // Current auction tracking
    private int currentAuctionSessionId = -1;
    private AuctionSession currentAuctionSession;

    // Countdown timers
    private javax.swing.Timer noBidTimer;  // 5-second timer waiting for bids
    private javax.swing.Timer countdownTimer;  // Timer for countdown display
    private int countdownPhase = 0;  // 0=waiting, 1=first Going, 2=second Going, 3=Gone
    private int countdownValue = 3;  // Current countdown number

    // Services
    private final AuctioneerLobbyService auctioneerLobbyService = AuctioneerLobbyService.getInstance();
    private final JoinAuctionSession joinAuctionSession = JoinAuctionSession.getInstance();
    private final StartAuctionSession startAuctionSession = StartAuctionSession.getInstance();

    // Modern Color Scheme (same as other apps)
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Professional Blue
    private static final Color PRIMARY_DARK = new Color(31, 97, 141);        // Darker Blue
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);       // Green
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Light Gray
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);           // Dark Gray
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);        // Light Gray Text
    private static final Color ORANGE_COLOR = new Color(230, 126, 34);       // Orange for auctioneer

    // Fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font HEADING_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font SUBHEADING_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    // Main method
    public static void main(String[] args) {
        // Initialize services at app start
        System.out.println("Initializing auctioneer application services...");
        AuctioneerLobbyService.getInstance();

        // Launch the UI
        SwingUtilities.invokeLater(() -> new AuctioneerApp());
    }

    // Constructor
    public AuctioneerApp() {
        // Main Window
        frame = new JFrame("Auctioneer Portal - Auction Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);

        // Setup CardLayout for switching between views
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BACKGROUND_COLOR);

        // Add panels
        cardPanel.add(createLobbyPanel(), "LOBBY");
        cardPanel.add(createAuctionRoomPanel(), "ROOM");

        // Add card panel to frame
        frame.add(cardPanel);
        frame.setVisible(true);

        // Initial load
        refreshAuctionList();
    }

    private JPanel createLobbyPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel title = new JLabel("Auctioneer Portal");
        title.setFont(HEADING_FONT);
        title.setForeground(ORANGE_COLOR);

        JLabel subtitle = new JLabel("Manage Live Auctions");
        subtitle.setFont(LABEL_FONT);
        subtitle.setForeground(TEXT_LIGHT);

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(subtitle, BorderLayout.EAST);

        // Auction List Panel
        auctionListPanel = new JPanel();
        auctionListPanel.setLayout(new BoxLayout(auctionListPanel, BoxLayout.Y_AXIS));
        auctionListPanel.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(auctionListPanel);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                        "Available Auction Sessions",
                        0,
                        0,
                        SUBHEADING_FONT,
                        TEXT_COLOR
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Bottom panel with refresh button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JButton refreshButton = createStyledButton("Refresh List", ACCENT_COLOR, Color.WHITE);
        refreshButton.setPreferredSize(new Dimension(150, 45));
        refreshButton.addActionListener(e -> refreshAuctionList());

        bottomPanel.add(refreshButton);

        // Add panels to main panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
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

        JLabel title = new JLabel("Live Auction");
        title.setFont(HEADING_FONT);
        title.setForeground(ORANGE_COLOR);

        // Initialize status label
        statusLabel = new JLabel("Loading...");
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(TEXT_LIGHT);

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(statusLabel, BorderLayout.EAST);

        // Main content area
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(CARD_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Lot Information Section
        JLabel lotSectionTitle = new JLabel("Current Lot");
        lotSectionTitle.setFont(SUBHEADING_FONT);
        lotSectionTitle.setForeground(TEXT_COLOR);
        lotSectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Initialize lot name label
        lotNameLabel = new JLabel("Loading...");
        lotNameLabel.setFont(HEADING_FONT);
        lotNameLabel.setForeground(ORANGE_COLOR);
        lotNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lotSectionTitle);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(lotNameLabel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Current Highest Bid Section
        JLabel bidSectionTitle = new JLabel("Current Highest Bid");
        bidSectionTitle.setFont(SUBHEADING_FONT);
        bidSectionTitle.setForeground(TEXT_COLOR);
        bidSectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Initialize current price label
        currentPriceLabel = new JLabel("No bids yet");
        currentPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        currentPriceLabel.setForeground(ACCENT_COLOR);
        currentPriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Initialize current bidder label
        currentBidderLabel = new JLabel("");
        currentBidderLabel.setFont(LABEL_FONT);
        currentBidderLabel.setForeground(TEXT_LIGHT);
        currentBidderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Initialize countdown label
        countdownLabel = new JLabel("");
        countdownLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        countdownLabel.setForeground(ORANGE_COLOR);
        countdownLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(bidSectionTitle);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(currentPriceLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(currentBidderLabel);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(countdownLabel);
        contentPanel.add(Box.createVerticalGlue());

        // Bottom Panel - Action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bottomPanel.setBackground(BACKGROUND_COLOR);

        JButton backButton = createStyledButton("Back to Lobby", PRIMARY_COLOR, Color.WHITE);
        backButton.setPreferredSize(new Dimension(180, 45));
        backButton.addActionListener(e -> {
            // Stop all countdown timers
            stopAllTimers();

            // Unregister observer when leaving
            if (currentAuctionSession != null) {
                currentAuctionSession.removeObserver(this);
                currentAuctionSession = null;
                System.out.println("Auctioneer left auction room");
            }
            currentAuctionSessionId = -1;
            cardLayout.show(cardPanel, "LOBBY");
        });

        bottomPanel.add(backButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshAuctionList() {
        // Clear existing list
        auctionListPanel.removeAll();

        // Get available auction sessions
        List<AuctionSession> auctions = auctioneerLobbyService.getAvailableAuctionSession();

        if (auctions.isEmpty()) {
            // Show empty state message
            JLabel emptyLabel = new JLabel("No available auction sessions");
            emptyLabel.setFont(SUBHEADING_FONT);
            emptyLabel.setForeground(TEXT_LIGHT);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            auctionListPanel.add(Box.createVerticalStrut(50));
            auctionListPanel.add(emptyLabel);
        } else {
            // Create cards for each auction session
            for (int i = 0; i < auctions.size(); i++) {
                AuctionSession auction = auctions.get(i);
                auctionListPanel.add(createAuctionCard(auction, i));
                auctionListPanel.add(Box.createVerticalStrut(10));
            }
        }

        // Refresh the UI
        auctionListPanel.revalidate();
        auctionListPanel.repaint();

        System.out.println("Auction list refreshed. Found " + auctions.size() + " available auctions.");
    }

    private JPanel createAuctionCard(AuctionSession auction, int index) {
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

        String title = auction.getTitle() != null ? auction.getTitle() : "Auction Session #" + (index + 1);
        JLabel nameLabel = new JLabel(title);
        nameLabel.setFont(SUBHEADING_FONT);
        nameLabel.setForeground(TEXT_COLOR);

        String statusText = auction.getStatus() != null ? auction.getStatus().toString() : "PENDING";
        String itemCount = auction.getCatalog() != null ? auction.getCatalog().size() + " item(s)" : "0 items";
        JLabel detailsLabel = new JLabel("Status: " + statusText + " | " + itemCount);
        detailsLabel.setFont(LABEL_FONT);
        detailsLabel.setForeground(TEXT_LIGHT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(detailsLabel);

        // Right side - Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton manageButton = createStyledButton("Manage", ORANGE_COLOR, Color.WHITE);
        manageButton.setPreferredSize(new Dimension(100, 35));
        manageButton.addActionListener(e -> {
            // Join the auction as observer (auctioneer monitors the auction)
            currentAuctionSessionId = auction.getId();
            currentAuctionSession = joinAuctionSession.execute(currentAuctionSessionId, this);

            if (currentAuctionSession != null) {
                JOptionPane.showMessageDialog(frame,
                        "Now managing auction: " + title + "\nStatus: " + currentAuctionSession.getStatus(),
                        "Auction Management",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton startButton = createStyledButton("Start", ACCENT_COLOR, Color.WHITE);
        startButton.setPreferredSize(new Dimension(80, 35));
        startButton.addActionListener(e -> {
            try {
                // Join the auction first to get the observed instance (if not already joined)
                if (currentAuctionSession == null || currentAuctionSession.getId() != auction.getId()) {
                    currentAuctionSessionId = auction.getId();
                    currentAuctionSession = joinAuctionSession.execute(currentAuctionSessionId, this);
                }

                if (currentAuctionSession == null) {
                    JOptionPane.showMessageDialog(frame,
                            "Failed to load auction session",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Start the auction DIRECTLY on the observed instance
                // This ensures all observers receive the AUCTION_STARTED event
                currentAuctionSession.startAuction();

                // Persist the status change to database
                AuctionSessionRepository repository = new AuctionSessionRepository();
                repository.updateStatus(auction.getId(), currentAuctionSession.getStatus());

                System.out.println("Auction started successfully: " + title);

                // Refresh auction room panel with data
                refreshAuctionRoomPanel();

                // Switch to auction room view
                cardLayout.show(cardPanel, "ROOM");

            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Cannot start auction: " + ex.getMessage(),
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

        buttonPanel.add(manageButton);
        buttonPanel.add(startButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private void refreshAuctionRoomPanel() {
        if (currentAuctionSession == null) {
            lotNameLabel.setText("No auction loaded");
            statusLabel.setText("Status: Unknown");
            currentPriceLabel.setText("No bids yet");
            return;
        }

        // Update lot name
        if (currentAuctionSession.getCatalog() != null && !currentAuctionSession.getCatalog().isEmpty()) {
            String lotName = currentAuctionSession.getCatalog().get(0).getName();
            lotNameLabel.setText(lotName);
        } else {
            lotNameLabel.setText("No lot assigned");
        }

        // Update status
        String status = currentAuctionSession.getStatus() != null
                ? currentAuctionSession.getStatus().toString()
                : "UNKNOWN";
        statusLabel.setText("Status: " + status);
        statusLabel.setForeground(ACCENT_COLOR); // Green for started status

        // Update current price and bidder
        if (currentAuctionSession.getCurrentBid() != null) {
            currentPriceLabel.setText(String.format("$%.2f", currentAuctionSession.getCurrentBid().getAmount()));
            currentPriceLabel.setForeground(ACCENT_COLOR);

            // Display bidder information
            String bidderName = currentAuctionSession.getCurrentBid().getBidder().toString();
            currentBidderLabel.setText("Bidder: " + bidderName);
            currentBidderLabel.setForeground(TEXT_COLOR);
        } else {
            currentPriceLabel.setText("No bids yet");
            currentPriceLabel.setForeground(TEXT_LIGHT);
            currentBidderLabel.setText("");
        }

        System.out.println("Auction room panel refreshed");
    }

    // --- Countdown Logic ---

    /**
     * Start the 5-second timer waiting for new bids.
     * If no bids come in, start the countdown sequence.
     */
    private void startNoBidTimer() {
        // Cancel existing timers
        stopAllTimers();

        // Start 5-second timer
        noBidTimer = new javax.swing.Timer(5000, e -> {
            // No bid in 5 seconds, start countdown
            System.out.println("No bid in 5 seconds, starting countdown...");
            startCountdownSequence();
        });
        noBidTimer.setRepeats(false);
        noBidTimer.start();

        // Clear countdown display
        countdownLabel.setText("");
    }

    /**
     * Reset the timer when a new bid comes in.
     */
    private void resetNoBidTimer() {
        if (noBidTimer != null && noBidTimer.isRunning()) {
            noBidTimer.restart();
        } else {
            startNoBidTimer();
        }

        // Clear countdown display
        countdownLabel.setText("");
        countdownPhase = 0;
    }

    /**
     * Start the countdown sequence: Going (3,2,1) -> Going (3,2,1) -> GONE!
     */
    private void startCountdownSequence() {
        countdownPhase = 1;
        countdownValue = 3;

        // Display "Going..."
        countdownLabel.setText("Going " + countdownPhase + "st ...");
        countdownLabel.setForeground(ORANGE_COLOR);

        // Wait 1 second, then start countdown
        javax.swing.Timer delayTimer = new javax.swing.Timer(1000, e -> {
            startCountdown();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    /**
     * Display the countdown numbers.
     */
    private void startCountdown() {
        countdownTimer = new javax.swing.Timer(1000, e -> {
            if (countdownValue > 0) {
                // Display countdown number
                countdownLabel.setText(String.valueOf(countdownValue));
                countdownLabel.setForeground(ORANGE_COLOR);
                countdownValue--;
            } else {
                // Countdown finished for this phase
                if (countdownPhase == 1) {
                    // First "Going" done, start second "Going"
                    countdownPhase = 2;
                    countdownValue = 3;
                    countdownLabel.setText("Going " + countdownPhase + "nd ...");
                    countdownLabel.setForeground(ORANGE_COLOR);
                } else if (countdownPhase == 2) {
                    // Second "Going" done, show "GONE!"
                    countdownPhase = 3;
                    countdownTimer.stop();
                    auctionGone();
                }
            }
        });
        countdownTimer.start();
    }

    /**
     * Called when auction is sold (GONE!).
     */
    private void auctionGone() {
        countdownLabel.setText("GONE!");
        countdownLabel.setForeground(ACCENT_COLOR);

        System.out.println("🔨 AUCTION SOLD!");

        // Stop all timers
        stopAllTimers();

        // Close the auction - this will:
        // 1. Change status to CLOSED
        // 2. Fire SOLD event to all observers (including bidders)
        if (currentAuctionSession != null) {
            currentAuctionSession.closeAuction();

            // Persist the status change to database
            AuctionSessionRepository repository = new AuctionSessionRepository();
            repository.updateStatus(currentAuctionSessionId, AuctionStatus.CLOSED);

            System.out.println("Auction closed and status persisted to database");
        }

        // Show sold message after 2 seconds
        javax.swing.Timer soldTimer = new javax.swing.Timer(2000, e -> {
            JOptionPane.showMessageDialog(frame,
                    "Auction SOLD!\n" +
                            "Winning Bid: " + currentPriceLabel.getText() + "\n" +
                            "Winner: " + currentBidderLabel.getText(),
                    "Auction Complete",
                    JOptionPane.INFORMATION_MESSAGE);

            countdownLabel.setText("");
        });
        soldTimer.setRepeats(false);
        soldTimer.start();
    }

    /**
     * Stop all countdown timers.
     */
    private void stopAllTimers() {
        if (noBidTimer != null) {
            noBidTimer.stop();
        }
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        countdownPhase = 0;
        countdownValue = 3;
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

        return button;
    }

    // --- Observer Pattern Implementation ---

    /**
     * Called when the auction session fires a property change event.
     * Auctioneer receives notifications about bid updates and auction status changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String eventName = evt.getPropertyName();

        if (BIDDING_EVENT.BID_UPDATE.toString().equals(eventName)) {
            // Bid update event - update UI in real-time
            Bid newBid = (Bid) evt.getNewValue();
            if (newBid != null) {
                String bidderName = newBid.getBidder().toString();
                System.out.println("🔔 [AUCTIONEER] BID UPDATE: New bid $" + String.format("%.2f", newBid.getAmount()) + " from " + bidderName);

                // Update the current price and bidder labels on the UI thread
                SwingUtilities.invokeLater(() -> {
                    if (currentAuctionSession != null && currentAuctionSession.getCurrentPrice() != null) {
                        currentPriceLabel.setText(String.format("$%.2f", currentAuctionSession.getCurrentPrice()));
                        currentPriceLabel.setForeground(ACCENT_COLOR);

                        // Update bidder label
                        currentBidderLabel.setText("Bidder: " + bidderName);
                        currentBidderLabel.setForeground(TEXT_COLOR);

                        // Reset/start the countdown timer
                        resetNoBidTimer();
                    }
                });
            }
        } else if (BIDDING_EVENT.AUCTION_STARTED.toString().equals(eventName)) {
            // Auction started event
            System.out.println("🔔 [AUCTIONEER] AUCTION STARTED");

            // Update status label
            SwingUtilities.invokeLater(() -> {
                if (currentAuctionSession != null) {
                    statusLabel.setText("Status: " + currentAuctionSession.getStatus());
                    statusLabel.setForeground(ACCENT_COLOR);

                    // Clear any countdown
                    stopAllTimers();
                    countdownLabel.setText("");
                }
            });
        }
    }
}
