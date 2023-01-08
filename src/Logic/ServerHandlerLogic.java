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
        if (databaseOperations.getUser(operationArr[2], operationArr[3])) {
            return true;
        }
        return false;
    }
    
      public boolean checksignUpUserExist() {
          System.out.println("$$$$$"+operationArr[3]+operationArr[4]);
        if (databaseOperations.getUser(operationArr[3], operationArr[4])) {
            return true;
        }
        return false;
    }

}
