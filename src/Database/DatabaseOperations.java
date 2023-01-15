package Database;

import Logic.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseOperations {

    DatabaseConnection databaseConnection;
    PreparedStatement pst = null;
    ArrayList<User> usersList = new ArrayList();
    User user = null;
    Connection con;

    public DatabaseOperations() {
        databaseConnection = DatabaseConnection.getInstanse();
        con = databaseConnection.con;
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

    public boolean getUser(String email, String Password) {
        try {
            Statement stmt = con.createStatement();
            String queryString = new String(
                    "select * from myUser where \"email\" = ? and \"password\" = ?");
            PreparedStatement s = con.prepareStatement(queryString);
            s.setString(1, email);
            s.setString(2, Password);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getString("gender"), rs.getInt("score"), rs.getString("recordspath"));
                System.out.println("heloooooooooooooo" + user.toString());
                return true;
            }
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean checkUserExist(String email, String password) {
        getAllUsers();
        for (User user : usersList) {
            if (email.equalsIgnoreCase(user.getEmail()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public int signUpDatabase(String[] signUpData) {
        int rs = -1;
        //  System.out.println("User Exisist = " + checkUserExist(signUpData[1], signUpData[2]));
        try {

            pst = con.prepareStatement("INSERT INTO \"MYUSER\" (\"userName\", \"email\", \"password\", \"gender\")VALUES(?,?,?,?)");

            for (int i = 2; i < signUpData.length; i++) {

                pst.setString(i - 1, signUpData[i]);

                System.out.println("the element in " + i + " =" + signUpData[i]);
            }
            rs = pst.executeUpdate();

        } catch (SQLException ex) {

        }
        return rs;
    }

    public User getUserUsingEmail(String email) {
        try {
            Statement stmt = con.createStatement();
            String queryString = new String(
                    "select * from myUser where \"email\" = ?");
            PreparedStatement s = con.prepareStatement(queryString);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            System.out.println("emaillllllllllllllllllll " + email);
            if (rs.next()) {
                System.out.println("enterrrrrrrrrrrrrrrrrrrrrrrrrr");
                user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getString("gender"), rs.getInt("score"), rs.getString("recordspath"));
            }
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public int getUsersCount() {
        usersList.clear();
        try {
            Statement stmt = con.createStatement();
            String queryString = new String("select COUNT(*) AS recordCount from myUser");
            ResultSet rs = stmt.executeQuery(queryString);
            while (rs.next()) {
                int count = rs.getInt("recordCount");
                return count;
            }
            stmt.close();
        } catch (SQLException ex) {
        }
        return -1;
    }

    public void updateScore(String email, int score) {
        try {
            pst = con.prepareStatement("UPDATE myUser set score=? WHERE email =" + email);
            pst.setInt(1, score);
            int rs = pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
