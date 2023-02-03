package com.kodilla.seabattle_javafx.presentation;

import com.kodilla.seabattle_javafx.data.Board;
import com.kodilla.seabattle_javafx.logic.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Drawer {

    private Options menu = new Menu();
    private Options settings = new Settings();
    private Options playerSettings = new PlayerSettings();
    private Options playerTurnOptions = new PlayerTurnOptions();
    private Printer printer = new Printer();

    public void drawScene(Stage primaryStage) {
//        VBox root = new VBox();
//        Scene scene = new Scene(root,320, 380);
//        drawMainTitle(scene);
//        drawMenu(menu, scene);
//        primaryStage.setTitle("Sea Battle Game");
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    public void drawMenu(Options options, Stage primaryStage) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:/dev/kodilla-course/SeaBattle_JavaFX/build/resources/SeaBattleImage.jpg");
        VBox root = new VBox();
        Image image = new Image(fileInputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(320);
        imageView.setPreserveRatio(true);
        root.getChildren().add(imageView);
        root.setAlignment(Pos.BASELINE_CENTER);
        Scene scene = new Scene(root, 320, 380);

        List<String> optionsList = options.getOptions();
        for (String option : optionsList) {
            Button bt = new Button(option);
            bt.setMinSize(130,30);
            bt.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    options.selectOptionFx(bt, primaryStage);
                }
            });
            root.getChildren().add(bt);
        }

        primaryStage.setTitle("Sea Battle Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void drawMainTitle(Options options, Stage primaryStage) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:/dev/kodilla-course/SeaBattle_JavaFX/build/resources/SeaBattleImage.jpg");
        VBox root = new VBox();
        Image image = new Image(fileInputStream);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(320);
        imageView.setPreserveRatio(true);
        root.getChildren().add(imageView);
        root.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(root,320, 380);

        primaryStage.setTitle("Sea Battle Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void drawScoreBoard() throws IOException {

    }

    public void boardDrawer(Stage primaryStage) throws IOException {

        Board board = new Board();
        GridPane gridPaneBoard = new GridPane();
        int gridSize = 30;

        for (int column = 0; column < board.getColumns().size(); column++) {
            Label columLabel = new Label(board.getColumns().get(column));
            columLabel.setMinSize(gridSize, gridSize);
            columLabel.setMaxSize(gridSize, gridSize);
            columLabel.setAlignment(Pos.CENTER);
            gridPaneBoard.add(columLabel,column + 1,0);
            for (int row = 0; row < board.getRowsCount(); row++) {
                //Button fieldBt = new Button(board.getColumns().get(column));
                Label rowLabel = new Label(board.getRows().get(row));
                rowLabel.setMinSize(gridSize, gridSize);
                rowLabel.setMaxSize(gridSize, gridSize);
                rowLabel.setAlignment(Pos.CENTER);
                gridPaneBoard.add(rowLabel,0,row + 1);
                Button fieldBt = new Button(" ");
                fieldBt.setMinSize(gridSize, gridSize);
                fieldBt.setMaxSize(gridSize, gridSize);
                gridPaneBoard.add(fieldBt,column + 1,row + 1);
            }
        }

        Label text = new Label("This is your battlefield...\n");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPaneBoard);
        borderPane.setTop(text);

        Label shipCountSettings = new Label("\nCurrent ship count settings are:\n" +
                printer.printShipCountSettingsFx(Settings.getShipCountSettings()));
        borderPane.setBottom(shipCountSettings);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }
}
