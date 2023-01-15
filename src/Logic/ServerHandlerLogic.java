package Logic;

import Database.DatabaseOperations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerHandlerLogic {

    public DatabaseOperations databaseOperations;
    String[] operationArr = null;
    static ArrayList<User> usersList = new ArrayList<>();
    User user;
    public static Map<String, String> map = new HashMap();

    public ServerHandlerLogic() {
        databaseOperations = new DatabaseOperations();
        ;
        System.out.println("call ServerHandlerLogic constrastor");
    }

    public String[] divideMessage(String operation) {
        operationArr = operation.split(",");
        return operationArr;
    }

    public boolean checkUserExist() {
        if (databaseOperations.getUser(operationArr[2], operationArr[3])) {
            return true;
        }
        return false;
    }

    public boolean checksignUp() {
        if (databaseOperations.signUpDatabase(operationArr) != -1) {
            return true;
        }
        return false;
    }

    public void addUserToOnlineList(String email) {
        user = databaseOperations.getUserUsingEmail(email);
        if (user != null) {
            usersList.add(user);
        }
    }

    public ArrayList<User> getOnlineListUsers() {
        return usersList;
    }

    public String getProfileData(String email, String userIp) {
        user = databaseOperations.getUserUsingEmail(email);
        String data = user.getUserName() + "," + user.getEmail() + "," + user.getScore() + "," + user.getGender();
        map.put(userIp, data);
        return data;
    }

    public int getusersCount(String email) {
        return databaseOperations.getUsersCount();
    }

}
