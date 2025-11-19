public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new auction.presentation.StaffApp();
            new auction.presentation.EndUserApp();
            new auction.presentation.EndUserApp();
            new auction.presentation.AuctioneerApp();
        });
    }
}
