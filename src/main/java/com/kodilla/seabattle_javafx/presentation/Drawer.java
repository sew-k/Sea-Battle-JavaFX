package com.kodilla.seabattle_javafx.presentation;

import com.kodilla.seabattle_javafx.data.*;
import com.kodilla.seabattle_javafx.logic.*;
import com.kodilla.seabattle_javafx.logic.Menu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Drawer {

    private Options menu = new Menu();
    private Options settings = new Settings();
    private Options playerSettings = new PlayerSettings();
    private Options playerTurnOptions = new PlayerTurnOptions();
    private Printer printer = new Printer();

    private static String message;
    private static int gridSize = 40;

    public void drawScene(Stage primaryStage, Scene scene) {
        primaryStage.setTitle("Sea Battle Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Scene drawMenu(Options options, Stage primaryStage) throws IOException {
        VBox root = new VBox();
        Image image = new Image("SeaBattleImage.jpg");
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

    public void drawScoreBoard() {                          //TODO - to complete
        Stage stage = new Stage();
        Label scoreBoardLabel = new Label("Score board");
        scoreBoardLabel.setStyle("-fx-font-weight: bold;");

        GridPane scoreBoardGrid = new GridPane();
        List<Map.Entry<String, Integer>> scoreBoardListSorted = new ArrayList<>(ScoreBoard.getScoreMap().entrySet().stream().toList());
        scoreBoardListSorted.sort(Map.Entry.comparingByValue());
        int j = 0;
        for (int i = (scoreBoardListSorted.size() - 1); i >= 0; i--) {
            String name = scoreBoardListSorted.get(i).getKey();
            String score = Integer.toString(scoreBoardListSorted.get(i).getValue());
            scoreBoardGrid.add(new Label((Integer.toString(j + 1)) + ". "), 0, j);
            scoreBoardGrid.add(new Label(name), 1, j);
            scoreBoardGrid.add(new Label(score), 2, j);
            j++;
        }

        Button okButton = new Button("Ok");
        okButton.setMinSize(130,30);
        okButton.setAlignment(Pos.CENTER);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(scoreBoardLabel);
        borderPane.setCenter(scoreBoardGrid);
        borderPane.setBottom(okButton);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void  drawPlayerBoardForShipsSetUp(Stage primaryStage, Player player) {
        Label text = new Label("\nPlayer '"+ player.getName() +" board \n");
        BorderPane borderPane = new BorderPane();
        VBox board = new VBox();
        Board playerBoard = new Board();
        GridPane gridPaneBoard = new GridPane();

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
                String coordinate = playerBoard.getColumns().get(column) + playerBoard.getRows().get(row);
                Button fieldBt = new Button(coordinate);
                fieldBt.setTextFill(Color.GRAY);
                fieldBt.setId(coordinate);
                fieldBt.setMinSize(gridSize, gridSize);
                fieldBt.setMaxSize(gridSize, gridSize);
                List<String> playerFieldsOfShipsList = player.getShips().stream()
                        .flatMap(s -> s.getStatusOnBoard().entrySet().stream())
                        .map(e -> e.getKey())
                        .collect(Collectors.toList());
                if (playerFieldsOfShipsList.size() > 0) {
                    for (String coordinatesOfFieldNotAvailable : playerFieldsOfShipsList) {
                        if (fieldBt.getId().equals(coordinatesOfFieldNotAvailable)) {
                            fieldBt.setStyle("-fx-background-color: gray;");
                            fieldBt.setDisable(true);
                        }
                    }
                }

                fieldBt.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (GameProcessor.currentPlayer.getCurrentShip() != null) {
                            if (GameProcessor.currentPlayer.tryFieldForShipSetUp(primaryStage, fieldBt.getId(), GameProcessor.currentPlayer.getCurrentShip())) {
                                //fieldBt.setVisible(false);
                                fieldBt.setStyle("-fx-background-color: gray;");

                                if (GameProcessor.currentPlayer.isAllShipsSet()) {
                                    primaryStage.close();
                                }
                            } else {
                                System.out.println("FIELD NOT AVAILABLE");

                            }
                        } else {
                            drawMessageForPlayer(GameProcessor.currentPlayer, "First you must select ship to set!");
                        }
                    }
                });
                gridPaneBoard.add(fieldBt,column + 1,row + 1);
            }
        }
//        Button nextShipButton = new Button("Set next ship");
//        nextShipButton.setMinSize(130,30);
//        nextShipButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                primaryStage.close();
//                //drawPlayerBoardForShipsSetUp(new Stage(), GameProcessor.currentPlayer);
//            }
//        });
//        HBox buttonsHBox = new HBox(nextShipButton);
        Pane shipsConfigurationPane = drawShipsConfigurationPane();
        board.getChildren().addAll(gridPaneBoard, shipsConfigurationPane);
        borderPane.setCenter(board);
        borderPane.setTop(text);
//        borderPane.setBottom(buttonsHBox);
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

        int i = 0;
        for (Map.Entry<Integer,Integer> entry : currentPlayer.getShipsToSet().entrySet()) {
            Button buttonAsShip = new Button();
            buttonAsShip.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Player thatPlayer = GameProcessor.currentPlayer;
                    buttonAsShip.setStyle("-fx-background-color: gray");
                    thatPlayer.setCurrentShip(entry.getKey());
                    message = " - you select ship - size: " + currentPlayer.getCurrentShip() + " to set on board";
                    System.out.println("Current player: " + currentPlayer);
                    System.out.println("Current ship: " + currentPlayer.getCurrentShip());
                    borderPane.setDisable(true);
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
    public void drawGetReadyWindowForPlayer(Player currentPlayer) {
        Stage stage = new Stage();
        Label text = new Label("Player '" + currentPlayer.getName() + "', get ready for your turn!");
        Button okButton = new Button("Ready!");
        okButton.setMinSize(130,30);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                drawPlayersBoardsForProcessTheBattle(GameProcessor.playerOne, GameProcessor.playerTwo);
                stage.close();
            }
        });
        Button giveUpButton = new Button("Exit game");
        giveUpButton.setMinSize(130,30);
        giveUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameProcessor.continueGame = false;
                stage.close();
            }
        });
        HBox buttonsHbox = new HBox(okButton,giveUpButton);
        buttonsHbox.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(text);
        borderPane.setBottom(buttonsHbox);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.showAndWait();
    }
    public void drawPlayersBoardsForProcessTheBattle(Player playerOne, Player playerTwo) {
        Stage primaryStage = new Stage();
        Label text = new Label("This is your battlefield...\n");
        Button endTurnButton = new Button("End turn");
        endTurnButton.setMinSize(130,30);
        endTurnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameProcessor gameProcessor = new GameProcessor();
                primaryStage.close();
                if (gameProcessor.winnerOfBattleCheck(GameProcessor.currentPlayer, GameProcessor.waitingPlayer) != null) {
                    Drawer drawer = new Drawer();
                    drawer.drawPlayerWinGame(gameProcessor.winnerOfBattleCheck(GameProcessor.currentPlayer, GameProcessor.waitingPlayer));
                }
            }
        });
        Button giveUpAndExitGame = new Button("Exit game");
        giveUpAndExitGame.setMinSize(130,30);
        giveUpAndExitGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameProcessor.continueGame = false;
                primaryStage.close();
            }
        });
        BorderPane borderPane = new BorderPane();
        HBox boardsHBox = new HBox();
        boardsHBox.getChildren().add(drawPlayerBoardToLayout(primaryStage, playerOne));
        boardsHBox.getChildren().add(drawPlayerBoardToLayout(primaryStage, playerTwo));
        HBox buttonsHbox = new HBox(endTurnButton, giveUpAndExitGame);
        buttonsHbox.setAlignment(Pos.CENTER);
        borderPane.setCenter(boardsHBox);
        borderPane.setTop(text);
        borderPane.setBottom(buttonsHbox);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }
    public Pane drawPlayerBoardToLayout(Stage stage, Player player) {
        Board playerBoard = new Board();
        Label text = new Label("\nPlayer '"+ player.getName() +" board \n");
        GridPane gridPaneBoard = new GridPane();

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
                String fieldCoordinates = playerBoard.getColumns().get(column) + playerBoard.getRows().get(row);

                if (player.equals(GameProcessor.waitingPlayer)) {
                    Button field = setButtonForAimOnDefenderPlayerBoard(player, fieldCoordinates, fieldCoordinates);
                    field.setMinSize(gridSize, gridSize);
                    field.setMaxSize(gridSize, gridSize);
                    Label labelField = setLabelFieldForAttackerPlayerBoard(GameProcessor.waitingPlayer, fieldCoordinates);
                    labelField.setMinSize(gridSize, gridSize);
                    labelField.setMaxSize(gridSize, gridSize);
                    Pane stack = new StackPane();
                    stack.getChildren().add(labelField);
                    stack.getChildren().add(field);
                    gridPaneBoard.add(stack,column + 1,row + 1);
                    gridPaneBoard.add(field,column + 1,row + 1);
                } else if (player.equals(GameProcessor.currentPlayer)) {
                    Label field = setLabelFieldForAttackerPlayerBoard(player, fieldCoordinates);
                    field.setMinSize(gridSize, gridSize);
                    field.setMaxSize(gridSize, gridSize);
                    gridPaneBoard.add(field,column + 1,row + 1);
                }
            }
        }
        //gridPaneBoard.setStyle("-fx-border-color: grey;");
        VBox vbox = new VBox(text,gridPaneBoard);
        return vbox;
    }
    public Button setButtonForAimOnDefenderPlayerBoard(Player defender, String buttonCoordinates, String buttonLabel) {
        Button fieldButton = new Button(buttonLabel);
        String isFieldAnyShipStatus = " ";

        for (Ship ship : defender.getShips()) {
            for (Map.Entry<String,String> entry : ship.getStatusOnBoard().entrySet()) {
                if (entry.getKey().equals(buttonCoordinates)) {
                    isFieldAnyShipStatus = entry.getValue();
                }
            }
        }

        for (String fieldShot : GameProcessor.currentPlayer.getShots()) {
            if (fieldShot.equals(buttonCoordinates)) {
                fieldButton.setVisible(false);
                fieldButton.setDisable(true);
            }
        }

        if (Settings.isCheatMode()) {
            if (isFieldAnyShipStatus.equals("good")) {
                fieldButton.setTextFill(Color.GREEN);
                fieldButton.setStyle("-fx-background-color: LIGHTGREEN");
            } else if (isFieldAnyShipStatus.equals("hit")) {
                fieldButton.setTextFill(Color.ORANGE);
                fieldButton.setStyle("-fx-background-color: GOLD");
            } else if (isFieldAnyShipStatus.equals("sink")) {
                fieldButton.setTextFill(Color.RED);
                fieldButton.setStyle("-fx-background-color: PINK");
            } else {
                fieldButton.setTextFill(Color.GRAY);
            }
        } else {
            fieldButton.setText("");
        }

        fieldButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fieldButton.setVisible(false);
                GameProcessor gameProcessor = new GameProcessor();
                gameProcessor.singleShotProcessorFx(GameProcessor.currentPlayer, GameProcessor.waitingPlayer, buttonCoordinates);
            }
        });
        return fieldButton;
    }
    public Label setLabelFieldForAttackerPlayerBoard(Player attacker, String fieldLabelCoordinates) {
        Label fieldLabel = new Label();
        String isFieldAnyShipStatus = " ";

        for (String shot : GameProcessor.waitingPlayer.getShots()) {
            if (shot.equals(fieldLabelCoordinates)) {
                isFieldAnyShipStatus = "x";
                if (attacker.equals(GameProcessor.waitingPlayer)) {
                    isFieldAnyShipStatus = " ";
                }
            }
        }

        for (Ship ship : attacker.getShips()) {
            for (Map.Entry<String,String> entry : ship.getStatusOnBoard().entrySet()) {
                if (entry.getKey().equals(fieldLabelCoordinates)) {
                    isFieldAnyShipStatus = entry.getValue();
                }
            }
        }
        fieldLabel.setText(isFieldAnyShipStatus.toUpperCase());
        fieldLabel.setAlignment(Pos.CENTER);

        if (isFieldAnyShipStatus.equals("good")) {
            fieldLabel.setTextFill(Color.GREEN);
        } else if (isFieldAnyShipStatus.equals("hit")) {
            fieldLabel.setTextFill(Color.ORANGE);
        } else if (isFieldAnyShipStatus.equals("sink")) {
            fieldLabel.setTextFill(Color.RED);
        } else {
            fieldLabel.setTextFill(Color.BLACK);
        }
        fieldLabel.setStyle("-fx-border-color: grey;");
        return fieldLabel;
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
        confirmBt.setMinSize(130,30);
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
        cancelBt.setMinSize(130,30);
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
        hbox2.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(hbox1,hbox2);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.showAndWait();
    }
    public void drawMessageForPlayer(Player player, String message) {
        Stage stage = new Stage();
        Label label = new Label("Player '" + player.getName() + "': " + message);
        Button confirmBt = new Button("OK");
        confirmBt.setMinSize(130,30);
        confirmBt.isDefaultButton();
        confirmBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        VBox vBox = new VBox();
        HBox hBox = new HBox();
        hBox.getChildren().addAll(confirmBt);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(label, hBox);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.showAndWait();
    }
    public void drawDialogMessageForPlayer(Player player, Pane pane) {
        Stage stage = new Stage();
        Label label = new Label("Player '" + player.getName());
        Button confirmBt = new Button("OK");
        confirmBt.setMinSize(130,30);
        confirmBt.isDefaultButton();
        confirmBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("PLAYER " + player.getName() + "SET : OK");
                stage.close();
            }
        });
        Button cancelBt = new Button("Cancel/ exit game");
        cancelBt.setMinSize(130,30);
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
    public void drawPlayerWinGame(Player winner) {
        Stage stage = new Stage();
        Label label = new Label("Player '" + winner.getName() + "' win game!");
        Button confirmBt = new Button("OK");
        confirmBt.setMinSize(130,30);
        confirmBt.isDefaultButton();
        confirmBt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(label, confirmBt);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.showAndWait();
    }
    public void drawSettings() {
        Settings settings = new Settings();
        Label titleSettingsLabel = new Label(settings.getOptionsTitle());
        List<String> settingsList = new ArrayList<>(settings.getOptions());

        PlayerSettings playerSettings = new PlayerSettings();
        Label titlePlayerSettingsLabel = new Label(playerSettings.getOptionsTitle());
        List<String> playerSettingsList = new ArrayList<>(playerSettings.getOptions());

        Stage stage = new Stage();

        Label playerSettingsLabel = new Label(settings.getOptions().get(1));
        playerSettingsLabel.setStyle("-fx-font-weight: bold;");
        RadioButton pVsCn = new RadioButton(playerSettings.getOptions().get(0));
        pVsCn.setId(Integer.toString(0));
        RadioButton pVsCh = new RadioButton(playerSettings.getOptions().get(1));
        pVsCh.setId(Integer.toString(1));
        pVsCh.setDisable(true);
        RadioButton pVsp = new RadioButton(playerSettings.getOptions().get(2));
        pVsp.setId(Integer.toString(2));
        RadioButton cVsC = new RadioButton(playerSettings.getOptions().get(3));
        cVsC.setId(Integer.toString(3));
        ToggleGroup playerSettingsRadioButtonsGroup = new ToggleGroup();
        pVsCn.setToggleGroup(playerSettingsRadioButtonsGroup);
        pVsCh.setToggleGroup(playerSettingsRadioButtonsGroup);
        pVsp.setToggleGroup(playerSettingsRadioButtonsGroup);
        cVsC.setToggleGroup(playerSettingsRadioButtonsGroup);

        if (PlayerSettings.getCurrentPlayerSettings() == Integer.parseInt(pVsCn.getId())) {
            playerSettingsRadioButtonsGroup.selectToggle(pVsCn);
        } else if (PlayerSettings.getCurrentPlayerSettings() == Integer.parseInt(pVsCh.getId())) {
            playerSettingsRadioButtonsGroup.selectToggle(pVsCh);
        } else if (PlayerSettings.getCurrentPlayerSettings() == Integer.parseInt(pVsp.getId())) {
            playerSettingsRadioButtonsGroup.selectToggle(pVsp);
        } else if (PlayerSettings.getCurrentPlayerSettings() == Integer.parseInt(cVsC.getId())) {
            playerSettingsRadioButtonsGroup.selectToggle(cVsC);
        }

        VBox playerSettingsVBox = new VBox(playerSettingsLabel, pVsCn, pVsCh, pVsp, cVsC);
        playerSettingsVBox.setStyle("-fx-border-color: grey;");

        Label shipCountSettingsLabel = new Label(settings.getOptions().get(0));
        shipCountSettingsLabel.setStyle("-fx-font-weight: bold;");
        Map<Integer, Integer> shipCountSettings = Settings.getShipCountSettings();
        BorderPane shipCountSettingsBorderPane = new BorderPane();
        GridPane gridPaneShips = new GridPane();

        Map<Integer,Integer> temporaryShipCountSettings = Settings.getShipCountSettings();


        List<TextField> textFieldList = new ArrayList<>();
        int i = 0;
        for (Map.Entry<Integer,Integer> entry : temporaryShipCountSettings.entrySet()) {
            Label labelAsShip = new Label();
            double labelAsShipWidth = 20;
            double labelAsShipHeight = 20;
            double currentLabelAsShipWidth = labelAsShipWidth * entry.getKey();
            labelAsShip.setMinSize(currentLabelAsShipWidth, labelAsShipHeight);
            labelAsShip.setStyle("-fx-background-color: white;");
            labelAsShip.setStyle("-fx-border-color: grey");

            TextField textField = new TextField(Integer.toString(entry.getValue()));
            textField.setAccessibleText(Integer.toString(entry.getValue()));
            entry.setValue(Integer.parseInt(textField.getAccessibleText()));
            textField.setId(Integer.toString(entry.getKey()));
            textField.setMaxSize(30,20);
            textFieldList.add(i, textField);
            gridPaneShips.add(labelAsShip,0, i);
            Label shipsToSet = new Label(" - current number: ");
            gridPaneShips.add(shipsToSet,1 , i);
            gridPaneShips.add(textFieldList.get(i),2 , i);
            i++;
        }
        shipCountSettingsBorderPane.setTop(shipCountSettingsLabel);
        shipCountSettingsBorderPane.setCenter(gridPaneShips);
        shipCountSettingsBorderPane.setStyle("-fx-border-color: grey;");

        Label boardSizeLabel = new Label(settings.getOptions().get(2));
        boardSizeLabel.setStyle("-fx-font-weight: bold;");
        TextField heightTextField = new TextField(Integer.toString(Board.getRowsCount()));
        TextField widthTextField = new TextField(Integer.toString(Board.getColumnsCount()));
        heightTextField.setMinSize(40,20);
        heightTextField.setMaxSize(40,20);
        widthTextField.setMinSize(40,20);
        widthTextField.setMaxSize(40,20);
        HBox boardHeightHBox = new HBox(new Label("Height: "), heightTextField);
        HBox boardWidthHBox = new HBox(new Label("Width: "), widthTextField);
        VBox boardSizeVBox = new VBox(boardSizeLabel, boardHeightHBox, boardWidthHBox);
        boardSizeVBox.setStyle("-fx-border-color: grey;");

        Label cheatModeLabel = new Label(settings.getOptions().get(3));
        cheatModeLabel.setStyle("-fx-font-weight: bold;");
        RadioButton cheatModeRadioButton = new RadioButton("Cheat mode [on/off]");
        cheatModeRadioButton.setSelected(settings.isCheatMode());
        VBox cheatModeVBox = new VBox(cheatModeLabel, cheatModeRadioButton);
        cheatModeVBox.setStyle("-fx-border-color: grey;");

        Button acceptButton = new Button("Accept & Save");
        acceptButton.setMinSize(130,30);
        acceptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Settings settings = new Settings();
                Board.setRowsCount(Integer.parseInt(heightTextField.getText()));
                System.out.println("Board size height set to: " + Board.getRowsCount());
                Board.setColumnsCount(Integer.parseInt(widthTextField.getText()));
                System.out.println("Board size width set to: " + Board.getColumnsCount());
                for (int j=0; j < textFieldList.size(); j++) {
                    temporaryShipCountSettings.replace(Integer.parseInt(textFieldList.get(j).getId()), Integer.parseInt(textFieldList.get(j).getText()));
                }
                Settings.setShipCountSettings(temporaryShipCountSettings);

                int selected = playerSettingsRadioButtonsGroup.getToggles().indexOf(playerSettingsRadioButtonsGroup.getSelectedToggle());
                PlayerSettings.setCurrentPlayerSettings(selected);

                Settings.setCheatMode(cheatModeRadioButton.isSelected());

                stage.close();
            }
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setMinSize(130,30);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        HBox buttonsHBox = new HBox(acceptButton, cancelButton);
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.setStyle("-fx-border-color: grey;");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(playerSettingsVBox, shipCountSettingsBorderPane, boardSizeVBox, cheatModeVBox, buttonsHBox);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
