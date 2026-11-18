import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles database connectivity to MySQL for the Hostel Management System.
 */
public class DBConnection {
    // Database credentials and configuration
    // Adjust port, username, and password according to your local MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/hostel_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "aanya123";

    // Static block to register MySQL JDBC Driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found! Make sure mysql-connector-j.jar is in your classpath.");
            e.printStackTrace();
        }
    }

    /**
     * Obtains a new connection to the MySQL hostel_db database.
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
