package Logic;

import Database.DatabaseOperations;
import java.util.ArrayList;

public class ServerHandlerLogic {

    DatabaseOperations databaseOperations;
    String[] operationArr = null;
    static ArrayList<User> usersList = new ArrayList<>();
    User user;

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

    public void addUserToOnlineList() {
        user = databaseOperations.getUserUsingEmail(operationArr[1]);
        System.out.println("user============== " + user.toString());
        if (user != null) {
            usersList.add(user);
            System.out.println("sizelogic===== " + usersList.size());
        }
    }

    public ArrayList<User> getOnlineListUsers() {
        System.out.println("sizelogic2===== " + usersList.size());
        return usersList;
    }

}
