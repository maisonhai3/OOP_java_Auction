package auction;

import auction.presentation.EndUserApp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

/**
 * Main Spring Boot Application Entry Point
 * Follows Clean Architecture: Application layer orchestrates the system
 * <p>
 * This class:
 * 1. Bootstraps Spring Boot context
 * 2. Enables component scanning for all packages under 'auction'
 * 3. Launches the Swing UI with dependency injection
 */
@SpringBootApplication
public class AuctionApplication {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("Starting Going Going Gone - Auction System");
        System.out.println("Clean Architecture with Spring Framework");
        System.out.println("=================================================\n");

        // Start Spring Boot application context
        ConfigurableApplicationContext context = SpringApplication.run(AuctionApplication.class, args);

        System.out.println("\nSpring Boot context started successfully!");
        System.out.println("Launching UI...\n");

        // Launch Swing UI on Event Dispatch Thread with Spring DI
        SwingUtilities.invokeLater(() -> {
            // Get EndUserApp bean from Spring context (with all dependencies injected)
            EndUserApp app = context.getBean(EndUserApp.class);
            app.show();
        });
    }
}