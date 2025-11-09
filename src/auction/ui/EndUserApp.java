package auction.ui;

import javax.swing.*;
import java.awt.*;

public class EndUserApp {
    // Fields
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;

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
        frame.setVisible(true);

        // Panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Add all your scenes as separate panels
        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createLobbyPanel(), "LOBBY");
        cardPanel.add(createRoomPanel(), "ROOM");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Create Account");
        JTextField username = new JTextField(15);
        JButton createButton = new JButton("Create Account");

        createButton.addActionListener(e -> {
            // Switch to lobby
            cardLayout.show(cardPanel, "LOBBY");
        });

        panel.add(label);
        panel.add(username);
        panel.add(createButton);
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
