package Lesson2;

import java.sql.*;

public class Program2 {
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/test";
    static final String USER = "postgres";
    static final String PASS = "admin";

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            //Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM test");
            while (result.next()) {
                System.out.println("Номер в выборке #" + result.getRow()
                        + "\t " + result.getInt("id")
                        + "\t" + result.getString("login")
                        + "\t" + result.getInt("id")
                        + "\t" + result.getString("login")
                        + "\t" + result.getString("pswrd")
                        + "\t" + result.getString("nick"));
            }
            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Ok");
    }
}
