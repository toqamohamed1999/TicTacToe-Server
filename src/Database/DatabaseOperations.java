package Database;

import Logic.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseOperations {

    DatabaseConnection databaseConnection;
    public PreparedStatement pst;
    public Statement statement;
    ArrayList<User> usersList = new ArrayList();
    User user;
    public Connection con;

    public DatabaseOperations() {
        databaseConnection = DatabaseConnection.getInstanse();
        con = databaseConnection.con;
        getAllUsers();
    }

    public void getAllUsers() {
        usersList.clear();
        try {
            Statement stmt = con.createStatement();
            String queryString = new String("select * from myUser");
            ResultSet rs = stmt.executeQuery(queryString);
            while (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getString("gender"), rs.getInt("score"), rs.getString("recordspath"));
                usersList.add(user);
                System.out.println(user.toString());
            }
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean checkUserExist(String email, String password) {
        for (User user : usersList) {
            if (email.equalsIgnoreCase(user.getEmail()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public void signUpDatabase(String[] signUpData) {    
        
      //  System.out.println("User Exisist = " + checkUserExist(signUpData[1], signUpData[2]));
        if(checkUserExist(signUpData[1], signUpData[2])==false)   { 
        try {

            pst = con.prepareStatement("INSERT INTO \"MYUSER\" (\"userName\", \"email\", \"password\", \"gender\")VALUES(?,?,?,?)");

            for (int i = 0; i < signUpData.length; i++) {
                pst.setString(i + 1, signUpData[i]);
            }

            int rs = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        }
        
    }

}
