import java.sql.*;

public class UserAuthentication {
    private Connection connection;

    public UserAuthentication() {
        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/SportsTrivia", "postgres", "HardcorE14");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean signUpUser(String username, String password) {
        try {
            // Check if the username already exists
            if (usernameExists(username)) {
                System.out.println("Username already exists. Please choose a different username.");
                return false;
            }

            // Prepare the SQL statement
            String sql = "INSERT INTO Users (username, password, num_of_wins) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setInt(3, 0);

            // Execute the query
            int rowsAffected = statement.executeUpdate();

            // Close the statement
            statement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean loginUser(String username, String password) {
        try {
            // Prepare the SQL statement
            String sql = "SELECT * FROM Users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if the user exists
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return storedPassword.equals(password);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean usernameExists(String username) throws SQLException {
        // Prepare the SQL statement
        String sql = "SELECT * FROM Users WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);

        // Execute the query
        ResultSet resultSet = statement.executeQuery();

        // Check if any rows were returned
        boolean userExists = resultSet.next();

        // Close the result set and statement
        resultSet.close();
        statement.close();

        return userExists;
    }
}
