
package tictactoe.server;

import Logic.ServerHandler;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class TicTacToeServer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root =new ServerScreen(stage);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

   
    public static void main(String[] args) {
        launch(args);
        new ServerHandler();
    }
    
}
