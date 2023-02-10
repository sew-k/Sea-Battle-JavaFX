package com.kodilla.seabattle_javafx.logic;

import com.kodilla.seabattle_javafx.data.*;
import com.kodilla.seabattle_javafx.presentation.Drawer;
import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;
import javafx.stage.Stage;

public class GameProcessor {

    public static Player playerOne;
    public static Player playerTwo;
    private Printer printer;
    private Keyboard keyboard;
    public static boolean continueGame = true;
    public static Player currentPlayer;
    public static Player waitingPlayer;

    private Options menu = new Menu();
    private Options settings = new Settings();
    private Options playerSettings = new PlayerSettings();
    private Options playerTurnOptions;
    private Drawer drawer;

    public void setCurrentPlayerFx(Player player) {
        currentPlayer = player;
        if (currentPlayer.equals(playerOne)) {
            waitingPlayer = playerTwo;
        } else if (currentPlayer.equals(playerTwo)) {
            waitingPlayer = playerOne;
        }
    }

    public GameProcessor() {
        this.playerTurnOptions = new PlayerTurnOptions();
        this.menu = new Menu();
        this.printer = new Printer();
        this.keyboard = new Keyboard();
        this.drawer = new Drawer();
    }

    public void exitGameFx(Stage primaryStage) {
        continueGame = false;
        primaryStage.close();
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
    public void startGameFx(Stage primaryStage) {
        continueGame = true;

        if (checkIfPlayersSetFx()) {
            setCurrentPlayerFx(playerOne);

            if (checkIfPlayersShipsAllSetFx()) {
                processTheBattleFx(primaryStage, playerOne, playerTwo);
            }
        } else {
            processGameFX(primaryStage);
        }
    }
    public boolean checkIfPlayersSetFx() {
        PlayerSettings playerSettings = new PlayerSettings();
        playerOne = getPlayerOneFromPlayerSettingsFx(playerSettings);
        playerTwo = getPlayerTwoFromPlayerSettingsFx(playerSettings);

        if ((!ComputerPlayer.class.isInstance(playerOne)) && (continueGame)) {
            drawer.askAndSetPlayerName(playerOne, "ONE");
        }
        if ((playerOne.getName() != null) && (!ComputerPlayer.class.isInstance(playerTwo)) && (continueGame)) {
            drawer.askAndSetPlayerName(playerTwo, "TWO");
        }

        if (playerOne.getName() != null && playerTwo.getName() != null && playerOne.getName() != "" && playerTwo.getName() != ""  && (continueGame)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfPlayersShipsAllSetFx() {
        setCurrentPlayerFx(playerOne);
        playerOne.shipsSetUp();

        if (playerOne.isAllShipsSet()) {
            setCurrentPlayerFx(playerTwo);
            playerTwo.shipsSetUp();
        }

        if (playerOne.isAllShipsSet() && playerTwo.isAllShipsSet()) {
            return true;
        } else {
            return false;
        }
    }
    public void processTheBattleFx(Stage primaryStage, Player playerOne, Player playerTwo) {
        setCurrentPlayerFx(playerOne);
        boolean battleEnd = false;
        while (!battleEnd) {
            if (!singleRoundProcessorFx(primaryStage, playerOne, playerTwo)) {
                battleEnd = true;
                processGameFX(primaryStage);
            }
        }
    }

    public boolean singleRoundProcessorFx(Stage primaryStage, Player playerOne, Player playerTwo) {
        if (continueGame) {
            singleTurnProcessorFx(primaryStage, currentPlayer, waitingPlayer);
            if (continueGame) {
                singleTurnProcessorFx(primaryStage, currentPlayer, waitingPlayer);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean singleTurnProcessorFx(Stage primaryStage, Player attacker, Player defender) {

        System.out.println("processing turn for player " + attacker.getName());
        if (continueGame) {
            if (PlayerSettings.getCurrentPlayerSettings() == 0) {
                if (ComputerPlayer.class.isInstance(attacker)) {
                    singleShotProcessor(attacker, defender);
                    endPlayerTurnFx(attacker);
                } else {
                    drawer.drawPlayersBoardsForProcessTheBattle(attacker, defender);
                }

            } else if (PlayerSettings.getCurrentPlayerSettings() == 2) {
                drawer.drawGetReadyWindowForPlayer(attacker);
            }
            return true;
        } else {
            return false;
        }
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
    }
    public void endPlayerTurnFx(Player player) {
        Player winner = winnerOfBattleCheck(currentPlayer, waitingPlayer);
        if (winner == null) {
            setCurrentPlayerFx(waitingPlayer);
        } else {
            playerWinGameFx(winner);
        }
    }
    public Player winnerOfBattleCheck(Player attacker, Player defender) {
        boolean defenderAllShipsSunk = defender.getShips().stream()
                .allMatch(ship -> ship.getStatusOnBoard().containsValue("sink"));

        if (defenderAllShipsSunk) {
            attacker.setScore(1);
            continueGame = false;
            return attacker;
        } else {
            return null;
        }
    }
    public void playerWinGameFx(Player winner) {
        ScoreBoard.addScore(winner.getName(), winner.getScore());
        Printer printer = new Printer();
        printer.printWinner(winner);
        drawer.drawPlayerWinGame(winner);
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
