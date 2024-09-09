import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String user = "postgres";
    private String password = "Anassanass00";

    public Connection connectAndDisplayInfo() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully.");

            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println("Database Product Name: " + metaData.getDatabaseProductName());
            System.out.println("Database Product Version: " + metaData.getDatabaseProductVersion());
            System.out.println("User Name: " + metaData.getUserName());
            System.out.println("Current Database: " + connection.getCatalog());

            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            System.out.println("List of tables:");
            while (tables.next()) {
                System.out.println(tables.getString("TABLE_NAME"));
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createUsersTable(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "users", new String[]{"TABLE"});

            if (!tables.next()) {
                System.out.println("The 'users' table does not exist. Creating the table...");
                String createTableSQL = "CREATE TABLE users ("
                        + "id SERIAL PRIMARY KEY, "
                        + "username VARCHAR(50) NOT NULL, "
                        + "email VARCHAR(50) NOT NULL, "
                        + "password VARCHAR(50) NOT NULL)";
                try (Statement statement = connection.createStatement()) {
                    statement.execute(createTableSQL);
                    System.out.println("Created 'users' table successfully.");
                }
            } else {
                System.out.println("The 'users' table already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertNewData(Connection connection) {
        String insertSQL = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, "new_user");
            preparedStatement.setString(2, "new_user@example.com");
            preparedStatement.setString(3, "password123");
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " row(s) into the users table.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}