package Logic;

import Database.DatabaseOperations;
import java.sql.SQLException;

public class ServerHandlerLogic {

    DatabaseOperations databaseOperations;

    public ServerHandlerLogic() {
        databaseOperations = new DatabaseOperations();

    }

    public void divideMessage(String operation) {
        String[] arrOfStr = operation.split(",");
        if (arrOfStr[0].equalsIgnoreCase("signIn")) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$" + databaseOperations.checkUserExist(arrOfStr[1], arrOfStr[2]));
        }
    }

  

}
