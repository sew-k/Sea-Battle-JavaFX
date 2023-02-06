package com.kodilla.seabattle_javafx.presentation;

import com.kodilla.seabattle_javafx.data.*;
import com.kodilla.seabattle_javafx.logic.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Drawer {

    private Options menu = new Menu();
    private Options settings = new Settings();
    private Options playerSettings = new PlayerSettings();
    private Options playerTurnOptions = new PlayerTurnOptions();
    private Printer printer = new Printer();

    private Player currentPlayer;

    private static String message;


    public void drawScene(Stage primaryStage, Scene scene) {
        primaryStage.setTitle("Sea Battle Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Scene drawMenu(Options options, Stage primaryStage) throws IOException {
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
        return scene;

    }

    public void drawScoreBoard() throws IOException {

    }

    public Pane playerBoardDrawerToLayout(Player player) {
        Board playerBoard = new Board();
        Label text = new Label("\nPlayer '"+ player.getName() +" board \n");
        GridPane gridPaneBoard = new GridPane();
        int gridSize = 30;

        for (int column = 0; column < playerBoard.getColumns().size(); column++) {
            Label columLabel = new Label(playerBoard.getColumns().get(column));
            columLabel.setMinSize(gridSize, gridSize);
            columLabel.setMaxSize(gridSize, gridSize);
            columLabel.setAlignment(Pos.CENTER);
            gridPaneBoard.add(columLabel,column + 1,0);
            for (int row = 0; row < playerBoard.getRowsCount(); row++) {

                Label rowLabel = new Label(playerBoard.getRows().get(row));
                rowLabel.setMinSize(gridSize, gridSize);
                rowLabel.setMaxSize(gridSize, gridSize);
                rowLabel.setAlignment(Pos.CENTER);
                gridPaneBoard.add(rowLabel,0,row + 1);
                Button fieldBt = new Button(playerBoard.getColumns().get(column) + playerBoard.getRows().get(row));
//                String isFieldAnyShipStatus = player.getShips().stream()
//                        .flatMap(s -> s.getStatusOnBoard().entrySet().stream())
//                        .filter(e -> e.getKey().equals(fieldBt.getText()))
//                        .map(e -> e.getValue())
//                        .toString();
                String isFieldAnyShipStatus = "";
                for (Ship ship : player.getShips()) {
                    for (Map.Entry<String,String> entry : ship.getStatusOnBoard().entrySet()) {
                        if (entry.getKey().equals(fieldBt.getText())) {
                            isFieldAnyShipStatus = entry.getValue();
                        }
                    }
                }


                if (isFieldAnyShipStatus.equals("good")) {
                    fieldBt.setTextFill(Color.GREEN);
                } else if (isFieldAnyShipStatus.equals("hit")) {
                    fieldBt.setTextFill(Color.ORANGE);
                } else if (isFieldAnyShipStatus.equals("sink")) {
                    fieldBt.setTextFill(Color.RED);
                } else {
                    fieldBt.setTextFill(Color.LIGHTBLUE);
                }

                fieldBt.setMinSize(gridSize, gridSize);
                fieldBt.setMaxSize(gridSize, gridSize);
                fieldBt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        GameProcessor gameProcessor = new GameProcessor();

                        gameProcessor.singleShotProcessorFx(GameProcessor.playerOne, GameProcessor.playerOne, fieldBt.getText());


                        //TODO here to implement behaviour of button when hit or not...
                        //also buttons disabled if this player turn...
                        //or draw ships under the button?

                        fieldBt.setVisible(false);
                    }
                });
                gridPaneBoard.add(fieldBt,column + 1,row + 1);
            }
        }
        VBox vbox = new VBox(text,gridPaneBoard);
        return vbox;
    }

    public void drawBoardForPlayerTurn(Player playerOne, Player playerTwo) {
        Stage primaryStage = new Stage();
        Label text = new Label("This is your battlefield...\n");

        BorderPane borderPane = new BorderPane();
        HBox boards = new HBox();
        boards.getChildren().addAll(playerBoardDrawerToLayout(playerOne), playerBoardDrawerToLayout(playerTwo));
        borderPane.setCenter(boards);
        borderPane.setTop(text);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }
    public void playerBoardDrawerToLayoutForSetUpShips(Stage primaryStage, Player player, Ship ship) throws IOException {
//        Label text = new Label("This is your battlefield...\n");
//        BorderPane borderPane = new BorderPane();
//        HBox boards = new HBox();
//
//        Board playerBoard = new Board();
//        GridPane gridPaneBoard = new GridPane();
//        int gridSize = 35;
//        currentPlayer = player;
//        currentShip = ship;
//
//        for (int column = 0; column < playerBoard.getColumns().size(); column++) {
//            Label columLabel = new Label(playerBoard.getColumns().get(column));
//            columLabel.setMinSize(gridSize, gridSize);
//            columLabel.setMaxSize(gridSize, gridSize);
//            columLabel.setAlignment(Pos.CENTER);
//            gridPaneBoard.add(columLabel,column + 1,0);
//            for (int row = 0; row < playerBoard.getRowsCount(); row++) {
//
//                Label rowLabel = new Label(playerBoard.getRows().get(row));
//                rowLabel.setMinSize(gridSize, gridSize);
//                rowLabel.setMaxSize(gridSize, gridSize);
//                rowLabel.setAlignment(Pos.CENTER);
//                gridPaneBoard.add(rowLabel,0,row + 1);
//                Button fieldBt = new Button(playerBoard.getColumns().get(column) + playerBoard.getRows().get(row));
//                fieldBt.setTextFill(Color.LIGHTBLUE);
//                fieldBt.setMinSize(gridSize, gridSize);
//                fieldBt.setMaxSize(gridSize, gridSize);
//                fieldBt.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//
//                        Player player = currentPlayer;
//                        if (player.tryFieldForShipSetUp(fieldBt.getText(), currentShip)) {
//                            fieldBt.setVisible(false);
//                        } else {
//                            System.out.println("FIELD NOT AVAILABLE");
//                        }
//                    }
//                });
//                gridPaneBoard.add(fieldBt,column + 1,row + 1);
//            }
//        }
//        boards.getChildren().addAll(gridPaneBoard);
//        borderPane.setCenter(boards);
//        borderPane.setTop(text);
//        Scene scene = new Scene(borderPane);
//        primaryStage.setScene(scene);
//        primaryStage.showAndWait();
    }

    public void  drawPlayerBoardForShipsSetUp(Stage primaryStage, Player player) {
        Label text = new Label("\nPlayer '"+ player.getName() +" board \n");
        BorderPane borderPane = new BorderPane();
        VBox board = new VBox();
        Board playerBoard = new Board();
        GridPane gridPaneBoard = new GridPane();
        int gridSize = 35;
        currentPlayer = player;
        //currentShip = ship;

        for (int column = 0; column < playerBoard.getColumns().size(); column++) {
            Label columLabel = new Label(playerBoard.getColumns().get(column));
            columLabel.setMinSize(gridSize, gridSize);
            columLabel.setMaxSize(gridSize, gridSize);
            columLabel.setAlignment(Pos.CENTER);
            gridPaneBoard.add(columLabel,column + 1,0);
            for (int row = 0; row < playerBoard.getRowsCount(); row++) {

                Label rowLabel = new Label(playerBoard.getRows().get(row));
                rowLabel.setMinSize(gridSize, gridSize);
                rowLabel.setMaxSize(gridSize, gridSize);
                rowLabel.setAlignment(Pos.CENTER);
                gridPaneBoard.add(rowLabel,0,row + 1);
                Button fieldBt = new Button(playerBoard.getColumns().get(column) + playerBoard.getRows().get(row));
                fieldBt.setTextFill(Color.LIGHTBLUE);
                fieldBt.setMinSize(gridSize, gridSize);
                fieldBt.setMaxSize(gridSize, gridSize);
                fieldBt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        if (currentPlayer.tryFieldForShipSetUp(fieldBt.getText(), currentPlayer.getCurrentShip())) {
                            fieldBt.setVisible(false);
                            if (currentPlayer.isAllShipsSet()) {
                                primaryStage.close();
                            }
                        } else {
                            System.out.println("FIELD NOT AVAILABLE");
                        }
                    }
                });
                gridPaneBoard.add(fieldBt,column + 1,row + 1);
            }
        }

        Pane shipsConfigurationPane = drawShipsConfigurationPane();
        board.getChildren().addAll(gridPaneBoard, shipsConfigurationPane);

        borderPane.setCenter(board);
        borderPane.setTop(text);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }

    public Pane drawShipsConfigurationPane() {
        Label text = new Label("Select ship to set on board\n");
        BorderPane borderPane = new BorderPane();
        message = " - please, select ship to set on board";
        GridPane gridPaneShips = new GridPane();
        Player currentPlayer = GameProcessor.currentPlayer;
        Map<Integer,Integer> shipCountSettings = currentPlayer.getShipsToSet();
        int i = 0;
        for (Map.Entry<Integer,Integer> entry : shipCountSettings.entrySet()) {
            Button buttonAsShip = new Button();

            buttonAsShip.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    Player thatPlayer = GameProcessor.currentPlayer;
                    thatPlayer.setCurrentShip(entry.getKey());
                    message = " - you select ship - size: " + currentPlayer.getCurrentShip() + " to set on board";
                    System.out.println("Current player: " + currentPlayer);
                    System.out.println("Current ship: " + currentPlayer.getCurrentShip());

                }
            });
            double buttonWidth = 30;
            double buttonHeight = 30;
            double currentButtonWidth = buttonWidth * entry.getKey();

            buttonAsShip.setMinSize(currentButtonWidth, buttonHeight);
            gridPaneShips.add(buttonAsShip,0, i);
            Label shipsToSet = new Label(" - number to set: " + entry.getValue());

            gridPaneShips.add(shipsToSet,1 , i);
            i++;
        }
        Label terminal = new Label(currentPlayer.getName() + message);
        borderPane.setTop(text);
        borderPane.setBottom(terminal);
        borderPane.setCenter(gridPaneShips);
        return borderPane;
    }

    public void askPlayerForSelectShipToSetWindow(Player player) {
        Stage stage = new Stage();
        Label text = new Label("Select ship to set on board\n");
        BorderPane borderPane = new BorderPane();
        message = " - please, select ship to set on board";
        GridPane gridPaneShips = new GridPane();
        Player currentPlayer = GameProcessor.currentPlayer;
        Map<Integer,Integer> shipCountSettings = currentPlayer.getShipsToSet();
        int i = 0;
        for (Map.Entry<Integer,Integer> entry : shipCountSettings.entrySet()) {
            Button buttonAsShip = new Button();

            buttonAsShip.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    Player player = GameProcessor.currentPlayer;
                    player.setCurrentShip(entry.getKey());
                    message = " - you select ship - size: " + currentPlayer.getCurrentShip() + " to set on board";
                    System.out.println("Current player: " + currentPlayer);
                    System.out.println("Current ship: " + currentPlayer.getCurrentShip());
                    stage.close();

                }
            });
            double buttonWidth = 30;
            double buttonHeight = 30;
            double currentButtonWidth = buttonWidth * entry.getKey();

            buttonAsShip.setMinSize(currentButtonWidth, buttonHeight);
            gridPaneShips.add(buttonAsShip,0, i);
            Label shipsToSet = new Label(" - number to set: " + entry.getValue());

            gridPaneShips.add(shipsToSet,1 , i);
            i++;
        }
        Label terminal = new Label(currentPlayer.getName() + message);
        borderPane.setTop(text);
        borderPane.setBottom(terminal);
        borderPane.setCenter(gridPaneShips);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void askAndSetPlayerName(Player player, String numberOfPlayer) {

        Stage stage = new Stage();
        Label label = new Label("Player " + numberOfPlayer + " - please enter your name: ");
        TextField name = new TextField();
        Button confirmBt = new Button("Confirm");
        confirmBt.isDefaultButton();
        confirmBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player.setName(name.getText());

                System.out.println(name.getText());
                System.out.println("PLAYER SET NAME: " + player.getName());
                stage.close();
            }
        });
        Button cancelBt = new Button("Cancel/exit");
        cancelBt.isCancelButton();
        cancelBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        VBox vbox = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        hbox1.getChildren().addAll(label, name);
        hbox2.getChildren().addAll(confirmBt,cancelBt);
        vbox.getChildren().addAll(hbox1,hbox2);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.showAndWait();
    }
    public void terminalWindowForPlayer(Player player, String text) throws Exception{

        Stage stage = new Stage();
        Label label = new Label("Player '" + player.getName() + "' - " + text);
        Button confirmBt = new Button("OK");
        confirmBt.isDefaultButton();
        confirmBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("PLAYER " + player.getName() + "SET : OK");
                stage.close();
            }
        });
        Button cancelBt = new Button("Cancel/ exit game");
        cancelBt.isCancelButton();
        cancelBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                GameProcessor.continueGame = false;
            }
        });
        VBox vbox = new VBox();
        HBox hbox1 = new HBox();
        hbox1.getChildren().addAll(confirmBt,cancelBt);
        vbox.getChildren().addAll(label, hbox1);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.showAndWait();
    }
    public void askPlayer(Player player, Pane pane) {
        Stage stage = new Stage();

        Label label = new Label("Player '" + player.getName());
        Button confirmBt = new Button("OK");
        confirmBt.isDefaultButton();
        confirmBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("PLAYER " + player.getName() + "SET : OK");
                stage.close();
            }
        });
        Button cancelBt = new Button("Cancel/ exit game");
        cancelBt.isCancelButton();
        cancelBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
                GameProcessor.continueGame = false;
            }
        });
        VBox vbox = new VBox();
        HBox hbox1 = new HBox();
        hbox1.getChildren().addAll(confirmBt,cancelBt);
        vbox.getChildren().addAll(label, pane, hbox1);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.showAndWait();
    }
    public void askPlayerForSetupShips(Player player, Pane pane) {

        Stage stage = new Stage();
        Label label = new Label("Player ");
        TextField name = new TextField();
        Button confirmBt = new Button("Confirm");
        confirmBt.isDefaultButton();
        confirmBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player.setName(name.getText());

                System.out.println(name.getText());
                System.out.println("PLAYER SET NAME: " + player.getName());
                stage.close();
            }
        });
        Button cancelBt = new Button("Cancel/exit");
        cancelBt.isCancelButton();
        cancelBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameProcessor.continueGame = false;
                stage.close();
            }
        });
        VBox vbox = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        hbox1.getChildren().addAll(label, name);
        hbox2.getChildren().addAll(confirmBt,cancelBt);
        vbox.getChildren().addAll(pane,hbox2);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
