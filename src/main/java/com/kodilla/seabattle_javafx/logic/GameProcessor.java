package com.kodilla.seabattle_javafx.logic;

import com.kodilla.seabattle_javafx.data.*;
import com.kodilla.seabattle_javafx.presentation.Drawer;
import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameProcessor {

    public static Player playerOne;
    public static Player playerTwo;
    private Printer printer;
    private Keyboard keyboard;
    public static boolean continueGame = true;
    public static Player currentPlayer;

    private Options menu = new Menu();
    private Options settings = new Settings();
    private Options playerSettings = new PlayerSettings();
    private Options playerTurnOptions;
    private Drawer drawer;

    public GameProcessor() {
        this.playerTurnOptions = new PlayerTurnOptions();
        this.menu = new Menu();
        this.printer = new Printer();
        this.keyboard = new Keyboard();
        this.drawer = new Drawer();
    }

    public GameProcessor(Menu menu, Printer printer, Keyboard keyboard, PlayerTurnOptions playerTurnOptions) {
        this.playerTurnOptions = playerTurnOptions;
        this.menu = menu;
        this.printer = printer;
        this.keyboard = keyboard;
    }

    public void exitGame() {
        printer.printExitGame();
    }
    public void exitGameFx(Stage primaryStage) {
        primaryStage.close();
    }

    public Player getPlayerOneFromPlayerSettings(PlayerSettings playerSettings) {
        if ((PlayerSettings.getCurrentPlayerSettings() >= 0) && (PlayerSettings.getCurrentPlayerSettings() <= 2)) {
            Player humanPlayer = new HumanPlayer();
            printer.askForPlayerOneName();
            humanPlayer.setName(keyboard.getString());
            return humanPlayer;
        } else if (PlayerSettings.getCurrentPlayerSettings() == 3) {
            return new ComputerPlayer();
        } else return null;
    }

    public Player getPlayerOneFromPlayerSettingsFx(PlayerSettings playerSettings) {

        if ((PlayerSettings.getCurrentPlayerSettings() >= 0) && (PlayerSettings.getCurrentPlayerSettings() <= 2)) {
            Player humanPlayer = new HumanPlayer();

            return humanPlayer;
        } else if (PlayerSettings.getCurrentPlayerSettings() == 3) {
            return new ComputerPlayer();
        } else return null;
    }
    public Player getPlayerTwoFromPlayerSettingsFx(PlayerSettings playerSettings) {
        if (PlayerSettings.getCurrentPlayerSettings() == 2) {
            Player humanPlayer = new HumanPlayer();

            return humanPlayer;
        } else if ((PlayerSettings.getCurrentPlayerSettings() == 0) || (PlayerSettings.getCurrentPlayerSettings() == 3)) {
            return new ComputerPlayer();
        } else if (PlayerSettings.getCurrentPlayerSettings() == 1) {
            return new ComputerPlayer();   //temporarily. TODO - hard difficulty
        } else return null;
    }
    public Player getPlayerTwoFromPlayerSettings(PlayerSettings playerSettings) {
        if (PlayerSettings.getCurrentPlayerSettings() == 2) {
            Player humanPlayer = new HumanPlayer();
            printer.askForPlayerTwoName();
            humanPlayer.setName(keyboard.getString());
            return humanPlayer;
        } else if ((PlayerSettings.getCurrentPlayerSettings() == 0) || (PlayerSettings.getCurrentPlayerSettings() == 3)) {
            return new ComputerPlayer();
        } else if (PlayerSettings.getCurrentPlayerSettings() == 1) {
            return new ComputerPlayer();   //temporarily. TODO - hard difficulty
        } else return null;
    }

    public void startGame() {

        PlayerSettings playerSettings = new PlayerSettings();
        printer.boardDrawer();

        setPlayerOne(getPlayerOneFromPlayerSettings(playerSettings));
        printer.askForSetShips(playerOne);
        playerOne.shipsSetUp();

        setPlayerTwo(getPlayerTwoFromPlayerSettings(playerSettings));
        printer.askForSetShips(playerTwo);
        playerTwo.shipsSetUp();

        boolean battleEnd = false;
        while (!battleEnd) {
            if (!singleRoundProcessor(playerOne, playerTwo)) {
                battleEnd = true;
                processGame();
            }
        }
    }

    public boolean checkIfPlayersSet() {
        PlayerSettings playerSettings = new PlayerSettings();
        playerOne = getPlayerOneFromPlayerSettingsFx(playerSettings);
        playerTwo = getPlayerTwoFromPlayerSettingsFx(playerSettings);
        drawer.askAndSetPlayerName(playerOne, "ONE");
        drawer.askAndSetPlayerName(playerTwo, "TWO");
        if (playerOne.getName() != null && playerTwo.getName() != null && playerOne.getName() != "" && playerTwo.getName() != "") {
            return true;
        } else {
            return false;
        }
    }
    public boolean checkIfPlayersShipsAllSet() {

        currentPlayer = playerOne;
        Stage stage1 = new Stage();
        drawer.drawPlayerBoardForShipsSetUp(stage1, playerOne);
        if (playerOne.isAllShipsSet()) {
            stage1.close();

            currentPlayer = playerTwo;
            Stage stage2 = new Stage();
            drawer.drawPlayerBoardForShipsSetUp(stage2, playerTwo);
            if (playerTwo.isAllShipsSet()) {
                stage2.close();

            }
        }

        if (playerOne.isAllShipsSet() && playerTwo.isAllShipsSet()) {
            System.out.println("checkIfPlayersShipsAllSet(): TRUE");
            return true;
        } else {
            System.out.println("checkIfPlayersShipsAllSet(): FALSE");
            return false;
        }
    }
    public void startGameFx(Stage primaryStage) {

        if (checkIfPlayersSet()) {

            currentPlayer = playerOne;


            if (checkIfPlayersShipsAllSet()) {

                boolean battleEnd = false;
                while (!battleEnd) {
                    if (!singleRoundProcessorFx(playerOne, playerTwo)) {
                        battleEnd = true;
                        processGameFX(primaryStage);
                    }
                }
            }

        } else {
            processGameFX(primaryStage);
        }

    }

    public boolean singleRoundProcessor(Player playerOne, Player playerTwo) {

        if (!(singleTurnProcessor(playerOne, playerTwo))) {
            return false;
        } else if (!(singleTurnProcessor(playerTwo, playerOne))) {
            return false;
        }
        return true;
    }

    public boolean singleRoundProcessorFx(Player playerOne, Player playerTwo) {

        if (!(singleTurnProcessorFx(playerOne, playerTwo))) {
            return false;
        } else if (!(singleTurnProcessorFx(playerTwo, playerOne))) {
            return false;
        }
        return true;
    }

    public boolean singleTurnProcessor(Player currentPlayer, Player otherPlayer) {

//        if (currentPlayer.getName().equals("Computer")) {
//            singleShotProcessor(currentPlayer, otherPlayer);
//            if (winnerOfBattleCheck(currentPlayer, otherPlayer) != null) {
//                return false;
//            }
//        } else {
//            if (!playerTurnOptions.singleRoundSelectOption(currentPlayer, otherPlayer, this)) {
//                return false;
//            } else {
//                if (winnerOfBattleCheck(currentPlayer, otherPlayer) != null) {
//                    return false;
//                } else {
//                    return true;
//                }
//            }
//        }
        return true;
    }
    public boolean singleTurnProcessorFx(Player currentPlayer, Player otherPlayer) {

        if (currentPlayer.getName().equals("Computer")) {
            singleShotProcessor(currentPlayer, otherPlayer);
            if (winnerOfBattleCheck(currentPlayer, otherPlayer) != null) {
                return false;
            }
        } else {
            //singleShotProcessorFx(currentPlayer, otherPlayer);
            drawer.drawBoardForPlayerTurn(currentPlayer, otherPlayer);
            if (winnerOfBattleCheck(currentPlayer, otherPlayer) != null) {
                return false;
            }
        }
        return true;
    }

    public void singleShotProcessor(Player attacker, Player defender) {
        Printer printer = new Printer();
        Validator validator = new Validator();
        Board board = new Board();
        String target;
        printer.hostileBoardDrawer(attacker, defender);
        printer.askForTarget(attacker);
        target = attacker.selectTarget();
        boolean shotFired = false;
        while (!shotFired) {
            if (validator.validateIsTargetOnBoard(target, board)) {
                attacker.addShot(target);
                for (Ship ship : defender.getShips()) {
                    ship.getStatusOnBoard().replace(target, "hit");
                    ship.checkIfShipSink();
                }
                shotFired = true;
            } else {
                printer.targetOutOfBoardMessage();
                printer.askForTarget(attacker);
                target = attacker.selectTarget();
            }
        }
        printer.hostileBoardDrawer(attacker, defender);

        Player winner;
        Player loser = null;
        winner = winnerOfBattleCheck(attacker, defender);
        if (winner != null) {
            winner.setScore(1);
            if (winner.equals(attacker)) {
                loser = defender;
            }

            printer.playersBoardDrawer(loser);
            playerWinGame(winner);
        }
    }

    public void singleShotProcessorFx(Player attacker, Player defender, String target) {
        Printer printer = new Printer();
        Validator validator = new Validator();
        Board board = new Board();

        printer.hostileBoardDrawer(attacker, defender);

        if (validator.validateIsTargetOnBoard(target, board)) {

            attacker.addShot(target);

            for (Ship ship : defender.getShips()) {
                    ship.getStatusOnBoard().replace(target, "hit");
                    ship.checkIfShipSink();
                }
            } else {
                printer.targetOutOfBoardMessage();
            }

        Player winner;
        Player loser = null;
        winner = winnerOfBattleCheck(attacker, defender);
        if (winner != null) {
            winner.setScore(1);
            if (winner.equals(attacker)) {
                loser = defender;
            }

            printer.playersBoardDrawer(loser);
            playerWinGame(winner);
        }
    }

    public Player winnerOfBattleCheck(Player attacker, Player defender) {

        boolean defenderAllShipsSunk = defender.getShips().stream()
                .allMatch(ship -> ship.getStatusOnBoard().containsValue("sink"));

        if (defenderAllShipsSunk) {
            return attacker;
        } else {
            return null;
        }
    }

    public void playerWinGame(Player winner) {
        ScoreBoard.addScore(winner.getName(), winner.getScore());
        Printer printer = new Printer();
        printer.printWinner(winner);
    }

    public void processGame() {
        printer.titlePrinter();
        printer.optionsPrinter(menu);
        menu.selectOption();
    }
    public void processGameFX(Stage primaryStage) {
        Drawer drawer = new Drawer();
        try {
            drawer.drawScene(primaryStage, drawer.drawMenu(menu,primaryStage));
        } catch (Exception e) {

        }
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }
}
