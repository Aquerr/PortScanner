package io.github.aquerr;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Main extends Application
{
    private ObservableList<String> reachablePorts = FXCollections.observableArrayList();
//    private List<Integer> reachablePorts = new ArrayList<>();
    private TextField startingPortField;
    private TextField endingPortField;
    private TextField ipToScanField;
    private ProgressBar progressBar;
    private ProgressIndicator progressIndicator;

    private Thread scanThread = new Thread(this::scanPorts);


    public static void main(String[] args) {

//        Main main = new Main();
//        Stage stage = new Stage(StageStyle.DECORATED);
//        Pane pane = new Pane();
//        pane.setMinWidth(400);
//        pane.setMinHeight(300);
//        Scene scene = new Scene(pane, pane.getWidth(),pane.getHeight());
        launch(args);
    }
//
//    private static String showList(List<Integer> ports)
//    {
//        StringBuilder stringBuilder = new StringBuilder();
//        for(int port : ports)
//        {
//            stringBuilder.append(port);
//            stringBuilder.append(",");
//        }
//        return stringBuilder.toString();
//    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        GridPane pane = new GridPane();
        pane.setPrefSize(400, 300);
//        pane.setMaxWidth(300);
//        pane.setMaxHeight(200);
        Scene scene = new Scene(pane, pane.getPrefWidth(), pane.getPrefHeight());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setHeight(pane.getHeight());
        primaryStage.setWidth(pane.getWidth());
        primaryStage.setResizable(false);
        primaryStage.setTitle("Port Scanner");

        Button button = new Button("Scan ports");
        button.setLayoutX(210);
        button.setLayoutY(330);
        button.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                scanThread.start();
//                scanPorts();
            }
        });

        startingPortField = new TextField();
        startingPortField.setLayoutX(50);
        startingPortField.setLayoutY(300);
        startingPortField.setPromptText("Enter starting port...");
        endingPortField = new TextField();
        endingPortField.setLayoutX(300);
        endingPortField.setLayoutY(300);
        endingPortField.setPromptText("Enter ending port...");

        ipToScanField = new TextField();
        ipToScanField.setLayoutX(175);
        ipToScanField.setLayoutY(250);
        ipToScanField.setPromptText("Enter ip...");

        VBox openedPortsBox = new VBox();
        openedPortsBox.setSpacing(10);
        Label openPortsLabel = new Label("Open Ports");
        ListView<String> listView = new ListView<>();
        listView.setPrefWidth(50);
        listView.setPrefHeight(150);
        listView.setLayoutX(5);
        listView.setLayoutY(50);
        listView.setItems(reachablePorts);
        openedPortsBox.getChildren().addAll(openPortsLabel, listView);

        progressBar = new ProgressBar(0);
        progressBar.setLayoutX(175);
        progressBar.setLayoutY(380);
        progressBar.setMinWidth(100);
        progressBar.setMinHeight(20);
//        progressBar.setBlendMode(BlendMode.BLUE);
//        progressBar.setPadding(Insets.EMPTY);

        progressIndicator = new ProgressIndicator();
        progressIndicator.setLayoutX(250);
        progressIndicator.setLayoutY(380);

        pane.setPadding(new Insets(10));
        pane.add(button, 1, 2);
        pane.setVgap(10);
        pane.setHgap(10);
        pane.add(openedPortsBox, 0, 0);
        pane.add(ipToScanField, 1, 1);
        pane.add(startingPortField, 0, 1);
        pane.add(endingPortField, 2, 1);
        pane.add(progressBar, 1, 3);
//        pane.getChildren().add(button);
//        pane.getChildren().add(openedPortsBox);
//        pane.getChildren().add(ipToScanField);
//        pane.getChildren().add(startingPortField);
//        pane.getChildren().add(endingPortField);
//        pane.getChildren().add(progressBar);
    }

    public void scanPorts()
    {
        //Get values
        String ipToScan = ipToScanField.getText();
        String startingPort = startingPortField.getText();
        String endingPort = endingPortField.getText();

//        int maxPort = 65535;
//        int currentPort = 25500;
        int maxPort = new Integer(endingPort);
        int currentPort = new Integer(startingPort);

        int delta = maxPort - currentPort;
        double progressBarAddition = (double)1 / (double)delta;

//        boolean showList = false;
//        int x = 0;
        while(currentPort <= maxPort)
        {
//            if(showList)
//            {
//                showList(reachablePorts);
//                showList = false;
//            }

            try(Socket socket = new Socket())
            {
                System.out.println("Trying connecting on port " + currentPort);
                InetAddress inetAddress = InetAddress.getByName(ipToScan);
                socket.connect(new InetSocketAddress(inetAddress, currentPort), 5000); //Wait 5 seconds
                reachablePorts.add(Integer.toString(currentPort));
            }
            catch(IOException e)
            {
                System.out.println("Unreachable port " + currentPort + "!");
            }

            progressBar.setProgress(progressBar.getProgress() + progressBarAddition);
            currentPort++;
//            x++;

//            if(x > 4)
//            {
//                showList = true;
//                x = 0;
//            }
        }

//        System.out.println("List of reachable ports for localhost");
//        StringBuilder stringBuilder = new StringBuilder();
//        for(int port : reachablePorts)
//        {
//            stringBuilder.append(port);
//            stringBuilder.append(",");
//        }
//        System.out.println(stringBuilder.toString());
    }
}
