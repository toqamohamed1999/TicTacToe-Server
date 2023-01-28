package tictactoe.server;


import Logic.ServerHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.derby.jdbc.ClientDriver;

public class ServerScreen extends AnchorPane {

    protected final Label label;
    protected final PieChart pieChart;
    protected final Button startStopButton;
    protected final Button refresh;
    
    Connection conn;
    PreparedStatement pst;
    ResultSet rs  ;
    Double offline;

    public ServerScreen(Stage stage) {
        try {
            DriverManager.registerDriver(new ClientDriver());
            conn=DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToe","root","root");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
         

        label = new Label();
        pieChart = new PieChart(getChartData());
        startStopButton = new Button();
        refresh = new Button();
         
        setId("AnchorPane");
        setPrefHeight(430.0);
        setPrefWidth(600.0);

        label.setLayoutX(230.0);
        label.setLayoutY(32.0);
        label.setPrefHeight(49.0);
        label.setPrefWidth(255.0);
        label.setText("Server");
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(new Font("Impact", 42.0));
        getStylesheets().add("/tictactoe/server/Styles.css");

        pieChart.setLayoutX(130.0);
        pieChart.setLayoutY(80.0);
        pieChart.setPrefHeight(230.0);
        pieChart.setPrefWidth(320.0);
        pieChart.setLegendVisible(false);

        startStopButton.setLayoutX(200.0);
        startStopButton.setLayoutY(330.0);
        startStopButton.setMnemonicParsing(false);
        startStopButton.setPrefHeight(25.0);
        startStopButton.setPrefWidth(177.0);
        startStopButton.setText("Start");
        startStopButton.setTextFill(javafx.scene.paint.Color.valueOf("#f9002d"));
        startStopButton.setFont(new Font("Impact", 24.0));
        startStopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if ((startStopButton.getText().equals("Start"))) {
                    startServer();
                } else {
                    stopServer();
                }

            }
        });
        
        refresh.setLayoutX(400.0);
        refresh.setLayoutY(100.0);
        refresh.setMnemonicParsing(false);
        refresh.setPrefHeight(25.0);
        refresh.setPrefWidth(177.0);
        refresh.setText("Refresh");
        refresh.setTextFill(javafx.scene.paint.Color.valueOf("#f9002d"));
        refresh.setFont(new Font("Impact", 24.0));
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
            pst = conn.prepareStatement("select count(*) from ROOT.MYUSER");
            rs = pst.executeQuery() ;
            if(rs.next())
             offline=(double)rs.getInt(1);
                    System.err.println(offline);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
                pieChart.setData(getChartData());

            }
        });

        getChildren().add(startStopButton);
        getChildren().add(refresh);
        getChildren().add(pieChart);
        getChildren().add(label);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            if(startStopButton.getText().equals("Start")){
                System.exit(0);
            }
            else{
                stopServer();
                System.exit(0);
            }
        }
         });

    }
     
    private ObservableList<Data> getChartData() {

        ObservableList<Data> list = FXCollections.observableArrayList();
        int onlineSize = ServerHandler.getVectorSize();
        if(offline!=null){
            System.err.println("online: "+onlineSize);
            System.err.println("offline: "+(offline-onlineSize));
            list.addAll(new PieChart.Data("Online", onlineSize),
                new PieChart.Data("Offline", offline-onlineSize));
        }
        else{
            list.addAll(new PieChart.Data("Online", 0),
            new PieChart.Data("Offline", 100));
        }
       
        return list;
    }

    ServerHandler serverHandler = null;
    Thread th;

    void startServer() {
        System.out.println("############# server start");
        startStopButton.setText("Stop");
        serverHandler = new ServerHandler();

        th = new Thread(new Runnable() {
            @Override
            public void run() {
                serverHandler.startServer();
            }
        });
        th.start();

    }
    
    void stopServer() {
        System.out.println("############# server stop");
        startStopButton.setText("Start");
        th.stop();
        serverHandler.stopServer();
    }

}
