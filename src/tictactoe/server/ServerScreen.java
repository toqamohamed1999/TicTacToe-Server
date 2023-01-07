package tictactoe.server;

import java.net.URL;
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

public class ServerScreen extends AnchorPane {

    protected final Label label;
    protected final PieChart pieChart;
    protected final Button startStopButton;

    public ServerScreen() {

        label = new Label();
        pieChart = new PieChart(getChartData());
        startStopButton = new Button();

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

        getChildren().add(startStopButton);
        getChildren().add(pieChart);
        getChildren().add(label);

    }

    private ObservableList<Data> getChartData() {
        ObservableList<Data> list = FXCollections.observableArrayList();
        list.addAll(new PieChart.Data("Online", 80),
                new PieChart.Data("Offline", 20));

        return list;
    }

    ServerHandler serverHandler = null;

    void startServer() {
        System.out.println("############# server start");
        startStopButton.setText("Stop");
        serverHandler = new ServerHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                serverHandler.startServer();
            }
        }).start();

    }

    void stopServer() {
        System.out.println("############# server stop");
        startStopButton.setText("Start");
        serverHandler.stopServer();
    }

}
