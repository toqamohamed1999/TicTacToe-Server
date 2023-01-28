package Database;

import Logic.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.derby.jdbc.ClientDriver;

public class DatabaseConnection {

    private static DatabaseConnection databaseConnection;
    Connection con = null;

    private DatabaseConnection() {
        try {
            DriverManager.registerDriver(new ClientDriver());
            con = DriverManager.
                    getConnection("jdbc:derby://localhost:1527/TicTacToe", "root", "root");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static DatabaseConnection getInstanse() {
        if (databaseConnection == null) {
            return new DatabaseConnection();
        }
        return databaseConnection;
    }
   
}
