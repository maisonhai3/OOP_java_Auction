package auction.presentation;

import auction.domain.AuctionSession;
import auction.domain.Lot;
import auction.usecases.LotService;
import auction.usecases.StaffLobbyServices;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StaffApp {
    // Fields
    private JFrame frame;
    private JPanel auctionListPanel;
    private JPanel lotListPanel;

    // Services
    private final StaffLobbyServices staffLobbyServices = StaffLobbyServices.getInstance();
    private final LotService lotService = LotService.getInstance();

    // Modern Color Scheme (same as EndUserApp)
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

    // Main method
    public static void main(String[] args) {
        // Initialize services at app start
        System.out.println("Initializing staff application services...");
        StaffLobbyServices.getInstance();
        LotService.getInstance();

        // Launch the UI
        SwingUtilities.invokeLater(() -> new StaffApp());
    }

    // Constructor
    public StaffApp() {
        // Main Window
        frame = new JFrame("Staff Portal - Auction Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(900, 700));
        frame.setSize(1100, 750);
        frame.setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header
        JPanel headerPanel = createHeaderPanel();

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(LABEL_FONT);
        tabbedPane.setBackground(BACKGROUND_COLOR);

        // Tab 1: Lot Management
        JPanel lotTab = createLotManagementTab();
        tabbedPane.addTab("Lot Management", lotTab);

        // Tab 2: Auction Session Management
        JPanel auctionTab = createAuctionManagementTab();
        tabbedPane.addTab("Auction Session Management", auctionTab);

        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add main panel to frame
        frame.add(mainPanel);
        frame.setVisible(true);

        // Initial load
        refreshLotList();
        refreshAuctionList();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel title = new JLabel("Staff Portal");
        title.setFont(HEADING_FONT);
        title.setForeground(TEXT_COLOR);

        JLabel subtitle = new JLabel("Auction Management System");
        subtitle.setFont(LABEL_FONT);
        subtitle.setForeground(TEXT_LIGHT);

        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(subtitle, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createLotManagementTab() {
        JPanel tabPanel = new JPanel(new BorderLayout(15, 15));
        tabPanel.setBackground(BACKGROUND_COLOR);

        // Form Panel for creating lots
        JPanel formPanel = createLotFormPanel();

        // List Panel for displaying lots
        lotListPanel = new JPanel();
        lotListPanel.setLayout(new BoxLayout(lotListPanel, BoxLayout.Y_AXIS));
        lotListPanel.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(lotListPanel);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                        "All Lots",
                        0,
                        0,
                        SUBHEADING_FONT,
                        TEXT_COLOR
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        tabPanel.add(formPanel, BorderLayout.NORTH);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        return tabPanel;
    }

    private JPanel createLotFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_BACKGROUND);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                        "Create New Lot",
                        0,
                        0,
                        SUBHEADING_FONT,
                        TEXT_COLOR
                ),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        // Name field
        JLabel nameLabel = new JLabel("Lot Name");
        nameLabel.setFont(LABEL_FONT);
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField nameField = new JTextField(30);
        nameField.setFont(SUBHEADING_FONT);
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Estimate Price field
        JLabel estimatePriceLabel = new JLabel("Estimate Price (optional)");
        estimatePriceLabel.setFont(LABEL_FONT);
        estimatePriceLabel.setForeground(TEXT_COLOR);
        estimatePriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField estimatePriceField = new JTextField(30);
        estimatePriceField.setFont(SUBHEADING_FONT);
        estimatePriceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        estimatePriceField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        estimatePriceField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Reserve Price field
        JLabel reservePriceLabel = new JLabel("Reserve Price (optional)");
        reservePriceLabel.setFont(LABEL_FONT);
        reservePriceLabel.setForeground(TEXT_COLOR);
        reservePriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField reservePriceField = new JTextField(30);
        reservePriceField.setFont(SUBHEADING_FONT);
        reservePriceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        reservePriceField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        reservePriceField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create button
        JButton createButton = createStyledButton("Create Lot", ACCENT_COLOR, Color.WHITE);
        createButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        createButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        createButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String estimatePrice = estimatePriceField.getText().trim();
            String reservePrice = reservePriceField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Lot name is required",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create lot using service
            Lot lot = lotService.createLot(name, estimatePrice, reservePrice);

            if (lot != null) {
                JOptionPane.showMessageDialog(frame,
                        "Lot created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear fields
                nameField.setText("");
                estimatePriceField.setText("");
                reservePriceField.setText("");

                // Refresh the list
                refreshLotList();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Failed to create lot. Check the prices are valid numbers.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to form panel
        formPanel.add(nameLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(estimatePriceLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(estimatePriceField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(reservePriceLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(reservePriceField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createButton);

        return formPanel;
    }

    private JPanel createAuctionManagementTab() {
        JPanel tabPanel = new JPanel(new BorderLayout(15, 15));
        tabPanel.setBackground(BACKGROUND_COLOR);

        // Form Panel for creating auction sessions
        JPanel formPanel = createAuctionFormPanel();

        // List Panel for displaying auction sessions
        auctionListPanel = new JPanel();
        auctionListPanel.setLayout(new BoxLayout(auctionListPanel, BoxLayout.Y_AXIS));
        auctionListPanel.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(auctionListPanel);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                        "All Auction Sessions",
                        0,
                        0,
                        SUBHEADING_FONT,
                        TEXT_COLOR
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        tabPanel.add(formPanel, BorderLayout.NORTH);
        tabPanel.add(scrollPane, BorderLayout.CENTER);

        return tabPanel;
    }

    private JPanel createAuctionFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_BACKGROUND);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                        "Create New Auction Session",
                        0,
                        0,
                        SUBHEADING_FONT,
                        TEXT_COLOR
                ),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        // Title field
        JLabel titleLabel = new JLabel("Auction Title");
        titleLabel.setFont(LABEL_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField titleField = new JTextField(30);
        titleField.setFont(SUBHEADING_FONT);
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        titleField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Lot ID field
        JLabel lotIdLabel = new JLabel("Lot ID");
        lotIdLabel.setFont(LABEL_FONT);
        lotIdLabel.setForeground(TEXT_COLOR);
        lotIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField lotIdField = new JTextField(30);
        lotIdField.setFont(SUBHEADING_FONT);
        lotIdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lotIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        lotIdField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create button
        JButton createButton = createStyledButton("Create Auction Session", ACCENT_COLOR, Color.WHITE);
        createButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        createButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        createButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String lotIdStr = lotIdField.getText().trim();

            if (title.isEmpty() || lotIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Please fill in all fields",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int lotId = Integer.parseInt(lotIdStr);

                // Check if lot exists
                if (lotService.getLot(lotId) == null) {
                    JOptionPane.showMessageDialog(frame,
                            "Lot ID " + lotId + " does not exist. Please create the lot first.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Create auction session
                AuctionSession auction = staffLobbyServices.createAuctionSession(title, lotId);

                // Show success message
                JOptionPane.showMessageDialog(frame,
                        "Auction session created successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Clear fields
                titleField.setText("");
                lotIdField.setText("");

                // Refresh the list
                refreshAuctionList();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Lot ID must be a number",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add components to form panel
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(titleField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(lotIdLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(lotIdField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createButton);

        return formPanel;
    }

    private void refreshLotList() {
        // Clear existing list
        lotListPanel.removeAll();

        // Get all lots
        List<Lot> lots = lotService.getAllLots();

        if (lots.isEmpty()) {
            JLabel emptyLabel = new JLabel("No lots created yet");
            emptyLabel.setFont(LABEL_FONT);
            emptyLabel.setForeground(TEXT_LIGHT);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            lotListPanel.add(Box.createVerticalStrut(20));
            lotListPanel.add(emptyLabel);
        } else {
            for (int i = 0; i < lots.size(); i++) {
                Lot lot = lots.get(i);
                lotListPanel.add(createLotCard(lot, i));
                lotListPanel.add(Box.createVerticalStrut(10));
            }
        }

        // Refresh the UI
        lotListPanel.revalidate();
        lotListPanel.repaint();
    }

    private void refreshAuctionList() {
        // Clear existing list
        auctionListPanel.removeAll();

        // Get all auction sessions
        List<AuctionSession> auctions = staffLobbyServices.getAllAuctionSessions();

        if (auctions.isEmpty()) {
            JLabel emptyLabel = new JLabel("No auction sessions created yet");
            emptyLabel.setFont(LABEL_FONT);
            emptyLabel.setForeground(TEXT_LIGHT);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            auctionListPanel.add(Box.createVerticalStrut(20));
            auctionListPanel.add(emptyLabel);
        } else {
            for (int i = 0; i < auctions.size(); i++) {
                AuctionSession auction = auctions.get(i);
                auctionListPanel.add(createAuctionCard(auction, i));
                auctionListPanel.add(Box.createVerticalStrut(10));
            }
        }

        // Refresh the UI
        auctionListPanel.revalidate();
        auctionListPanel.repaint();
    }

    private JPanel createLotCard(Lot lot, int index) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Left side - Lot info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Lot #" + index + ": " + lot.getName());
        nameLabel.setFont(SUBHEADING_FONT);
        nameLabel.setForeground(TEXT_COLOR);

        String estimateText = lot.getEstimatePrice() != null ? "$" + lot.getEstimatePrice() : "No estimate";
        String reserveText = lot.getReservePrice() != null ? "$" + lot.getReservePrice() : "No reserve";
        JLabel detailsLabel = new JLabel("Estimate: " + estimateText + " | Reserve: " + reserveText);
        detailsLabel.setFont(LABEL_FONT);
        detailsLabel.setForeground(TEXT_LIGHT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(detailsLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createAuctionCard(AuctionSession auction, int index) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Left side - Auction info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Auction Session #" + (index + 1));
        nameLabel.setFont(SUBHEADING_FONT);
        nameLabel.setForeground(TEXT_COLOR);

        String statusText = auction.getStatus() != null ? auction.getStatus().toString() : "PENDING";
        JLabel detailsLabel = new JLabel("Status: " + statusText);
        detailsLabel.setFont(LABEL_FONT);
        detailsLabel.setForeground(TEXT_LIGHT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(detailsLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    // Helper method to create styled buttons (same as EndUserApp)
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
}
