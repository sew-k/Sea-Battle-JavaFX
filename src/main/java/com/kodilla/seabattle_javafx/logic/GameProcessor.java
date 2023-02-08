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

    public GameProcessor(Menu menu, Printer printer, Keyboard keyboard, PlayerTurnOptions playerTurnOptions) {
        this.playerTurnOptions = playerTurnOptions;
        this.menu = menu;
        this.printer = printer;
        this.keyboard = keyboard;
    }

    public void exitGameFx(Stage primaryStage) {
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

        if (checkIfPlayersSetFx()) {
            setCurrentPlayerFx(playerOne);
            System.out.println("checking is playerOne set as current: " + playerOne.equals(currentPlayer));
            System.out.println("checking is playerTwo set as waiting: " + playerTwo.equals(waitingPlayer));

            if (checkIfPlayersShipsAllSetFx()) {
                processGameFx(primaryStage, playerOne, playerTwo);
            }

        } else {
            processGameFX(primaryStage);
        }
    }
    public boolean checkIfPlayersSetFx() {
        PlayerSettings playerSettings = new PlayerSettings();
        playerOne = getPlayerOneFromPlayerSettingsFx(playerSettings);
        playerTwo = getPlayerTwoFromPlayerSettingsFx(playerSettings);

        if (!ComputerPlayer.class.isInstance(playerOne)) {
            drawer.askAndSetPlayerName(playerOne, "ONE");
        }
        if (!ComputerPlayer.class.isInstance(playerTwo)) {
            drawer.askAndSetPlayerName(playerTwo, "TWO");
        }

        if (playerOne.getName() != null && playerTwo.getName() != null && playerOne.getName() != "" && playerTwo.getName() != "") {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfPlayersShipsAllSetFx() {
        setCurrentPlayerFx(playerOne);
        Stage stage1 = new Stage();

        if (!ComputerPlayer.class.isInstance(playerOne)) {
            drawer.drawPlayerBoardForShipsSetUp(stage1, playerOne);
        } else {
            playerOne.shipsSetUp();
        }

        if (playerOne.isAllShipsSet()) {
            stage1.close();
            setCurrentPlayerFx(playerTwo);
            Stage stage2 = new Stage();

            if (!ComputerPlayer.class.isInstance(playerTwo)) {
                drawer.drawPlayerBoardForShipsSetUp(stage2, playerTwo);
            } else {
                playerTwo.shipsSetUp();
            }
            if (playerTwo.isAllShipsSet()) {
                stage2.close();
            }
        }

        if (playerOne.isAllShipsSet() && playerTwo.isAllShipsSet()) {
            System.out.println("checkIfPlayersShipsAllSet(): TRUE " + playerOne.getName());
            System.out.println("checkIfPlayersShipsAllSet(): TRUE " + playerTwo.getName());
            return true;
        } else {
            System.out.println("checkIfPlayersShipsAllSet(): FALSE");
            return false;
        }
    }
    public void processGameFx(Stage primaryStage, Player playerOne, Player playerTwo) {
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
            setCurrentPlayerFx(playerOne);
            singleTurnProcessorFx(primaryStage, currentPlayer, waitingPlayer);
            if (continueGame) {
                setCurrentPlayerFx(playerTwo);
                singleTurnProcessorFx(primaryStage, currentPlayer, waitingPlayer);
            } else {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }


    public boolean singleTurnProcessorFx(Stage primaryStage, Player currentPlayer, Player otherPlayer) {

        System.out.println("processing turn for player " + currentPlayer.getName());

        if (ComputerPlayer.class.isInstance(currentPlayer)) {
            singleShotProcessor(currentPlayer, otherPlayer);
            if (winnerOfBattleCheck(currentPlayer, otherPlayer) != null) {
                return false;
            }
        } else {
            if (PlayerSettings.getCurrentPlayerSettings() == 2) {
                drawer.drawGetReadyWindowForPlayer(currentPlayer);
            } else {
                drawer.drawPlayersBoardsForProcessTheBattle(playerOne, playerTwo);
            }

            //drawer.drawBoardForPlayerTurn(getPlayerOne(), getPlayerTwo());
            if (winnerOfBattleCheck(currentPlayer, otherPlayer) != null) {
                continueGame = false;
                return false;
            }
        }
        return false;
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
        } else {
            if (currentPlayer.equals(playerOne)) {
                currentPlayer = playerTwo;
                System.out.println("changed current player to player two");
            } else if (currentPlayer.equals(playerTwo)) {
                currentPlayer = playerOne;
                System.out.println("changed current player to player one");
            }
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
        } else {
            endPlayerTurnFx(currentPlayer);
//            if (currentPlayer.equals(playerOne)) {
//                currentPlayer = playerTwo;
//                System.out.println("changed current player to player two");
//            } else if (currentPlayer.equals(playerTwo)) {
//                currentPlayer = playerOne;
//                System.out.println("changed current player to player one");
//            }
        }
    }

    public void endPlayerTurnFx(Player currentPlayer) {
        if (currentPlayer.equals(playerOne)) {
            setCurrentPlayerFx(playerTwo);
            System.out.println("changed current player to player two");
        } else if (currentPlayer.equals(playerTwo)) {
            setCurrentPlayerFx(playerOne);
            System.out.println("changed current player to player one");
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
