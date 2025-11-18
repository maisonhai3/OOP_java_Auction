package auction.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages SQLite database connection and schema initialization.
 * Singleton pattern ensures only one connection instance exists.
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:auction.db";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Create connection
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connection established: " + DB_URL);
            // Initialize schema
            initializeSchema();
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found. Please add sqlite-jdbc JAR to lib/");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to database");
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Check if connection is still valid
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void initializeSchema() {
        try (Statement stmt = connection.createStatement()) {
            // Create Lots table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS lots (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            estimate_price REAL,
                            reserve_price REAL,
                            status TEXT DEFAULT 'UNSOLD',
                            no_reserve INTEGER DEFAULT 0
                        )
                    """);

            // Create AuctionSessions table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS auction_sessions (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            title TEXT,
                            lot_id INTEGER,
                            status TEXT DEFAULT 'SCHEDULED',
                            start_time TEXT,
                            end_time TEXT,
                            FOREIGN KEY (lot_id) REFERENCES lots(id)
                        )
                    """);

            // Create Users table (for future use)
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS users (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            username TEXT UNIQUE NOT NULL,
                            user_type TEXT,
                            created_at TEXT DEFAULT CURRENT_TIMESTAMP
                        )
                    """);

            System.out.println("Database schema initialized successfully");
        } catch (SQLException e) {
            System.err.println("Failed to initialize database schema");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
