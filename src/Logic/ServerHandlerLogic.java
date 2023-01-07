package Logic;

import Database.DatabaseOperations;

public class ServerHandlerLogic {

    DatabaseOperations databaseOperations;
    String[] operationArr = null;

    public ServerHandlerLogic() {
        databaseOperations = new DatabaseOperations();

    }

    public String[] divideMessage(String operation) {
        operationArr = operation.split(",");
        return operationArr;
    }

    public boolean checkUserExist() {
        if (databaseOperations.checkUserExist(operationArr[2], operationArr[3])) {
            return true;
        }
        return false;
    }

}
