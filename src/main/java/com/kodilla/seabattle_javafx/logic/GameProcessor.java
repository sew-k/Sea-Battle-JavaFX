package com.kodilla.seabattle_javafx.logic;

import com.kodilla.seabattle_javafx.data.*;
import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;

public class GameProcessor {

    private Player playerOne;
    private Player playerTwo;
    private PlayerTurnOptions playerTurnOptions;
    private Menu menu;
    private Printer printer;
    private Keyboard keyboard;

    public GameProcessor() {
        this.playerTurnOptions = new PlayerTurnOptions();
        this.menu = new Menu();
        this.printer = new Printer();
        this.keyboard = new Keyboard();
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

    public boolean singleRoundProcessor(Player playerOne, Player playerTwo) {

        if (!(singleTurnProcessor(playerOne, playerTwo))) {
            return false;
        } else if (!(singleTurnProcessor(playerTwo, playerOne))) {
            return false;
        }
        return true;
    }

    public boolean singleTurnProcessor(Player currentPlayer, Player otherPlayer) {

        if (currentPlayer.getName().equals("Computer")) {
            singleShotProcessor(currentPlayer, otherPlayer);
            if (winnerOfBattleCheck(currentPlayer, otherPlayer) != null) {
                return false;
            }
        } else {
            if (!playerTurnOptions.singleRoundSelectOption(currentPlayer, otherPlayer, this)) {
                return false;
            } else {
                if (winnerOfBattleCheck(currentPlayer, otherPlayer) != null) {
                    return false;
                } else {
                    return true;
                }
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
