package com.kodilla.seabattle_javafx.presentation;

import com.kodilla.seabattle_javafx.data.Board;
import com.kodilla.seabattle_javafx.data.Player;
import com.kodilla.seabattle_javafx.data.ScoreBoard;
import com.kodilla.seabattle_javafx.data.Ship;
import com.kodilla.seabattle_javafx.logic.Options;
import com.kodilla.seabattle_javafx.logic.PlayerSettings;
import com.kodilla.seabattle_javafx.logic.Settings;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Printer {

    public void optionsPrinter(Options options) {
        System.out.println("\n----------------------------");
        System.out.println(options.getOptionsTitle());
        for (String choicePosition : options.getOptions()) {
            System.out.println("[" + options.getOptions().indexOf(choicePosition) + "] - " + choicePosition);
        }
    }

    public void titlePrinter() {
        System.out.println("============================");
        System.out.println("SEA BATTLE");
    }

    public void askForTarget(Player player) {
        System.out.print(" - " + player.getName() + ", please select your target on the board: ");
    }

    public void askForField(Player player) {
        System.out.print(" - " + player.getName() + ", please select field on the board: ");
    }
    public void askForNextField(Player player) {
        System.out.print(" - " + player.getName() + ", please select next field on the board: ");
    }
    public void printTarget(String target) {
        System.out.println(target);
    }


    public void showScoreBoard() {
        System.out.println("----------------------------");
        System.out.println("SCORE BOARD");
        for (Map.Entry<String,Integer> entry : ScoreBoard.getScoreMap().entrySet()) {
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
        }
        System.out.println("----------------------------");
    }

    public void printChangeSettingsOrLeave() {
        Settings settings = new Settings();
        System.out.print(" - press key [" + settings.getKeyForChangeSettings() + "] to change, or press [" + settings.getKeyForLeaveSettings() + "] to leave: ");
    }

    public void printShipCountSettings(Map<Integer,Integer> shipCountSettings) {
        for (Map.Entry<Integer,Integer> entry : shipCountSettings.entrySet()) {
            if (entry.getValue() != 0) {
                String shipSize = "";
                for (int i = 0; i < entry.getKey(); i++) {
                    shipSize = shipSize + "[]";
                }
                System.out.println("Quantity of " + shipSize + " is: " + entry.getValue());
            }
        }
    }
    public String printShipCountSettingsFx(Map<Integer,Integer> shipCountSettings) {
        String message = "";
        for (Map.Entry<Integer,Integer> entry : shipCountSettings.entrySet()) {
            if (entry.getValue() != 0) {
                String shipSize = "";
                for (int i = 0; i < entry.getKey(); i++) {
                    shipSize = shipSize + "[]";
                }
                message = message + ("Quantity of " + shipSize + " is: " + entry.getValue() + "\n");
            }
        }
        return message;
    }

    public void printShipSettingsToChange(int shipSize) {
        String shipSizeString = "";
        for (int i = 0; i < shipSize; i++) {
            shipSizeString = shipSizeString + "[]";
        }
        System.out.print("Set quantity of " + shipSizeString + ": ");
    }
    public void printExitGame() {
        System.out.println("exit game");
    }


    public void askToSetNewNumberOfColumns() {
        System.out.print("- please set new number of Columns (3-26 max): ");
    }
    public void askToSetNewNumberOfRows() {
        System.out.print("- please set new number of Rows (3-26 max): ");
    }

    public void printShip(Ship ship) {
            System.out.println(ship);
    }

    public void askForSetUpShip(Ship ship) {
        System.out.print("\nTrying set up ship: ");
        printShip(ship);
    }

    public void printShipAdded(Ship ship) {
        System.out.print("Successfully added ship: ");
        printShip(ship);
    }

    public void currentPlayerSettingsPrinter() {
        System.out.println("Current settings: " + PlayerSettings.getCurrentPlayerSettings() + "\n");
    }

    public void printGameBoardSettings() {
        System.out.println("Size of game board:");
        System.out.println("- columns: " + Board.getColumnsCount());
        System.out.println("- rows: " + Board.getRowsCount());
    }



    public void askForSelect() {
        System.out.print("Please select an option: ");
    }
    public void askForPlayerOneName() {
        System.out.print("Player ONE - please enter your name: ");
    }
    public void askForPlayerTwoName() {
        System.out.print("Player TWO - please enter your name: ");
    }
    public void askForSetShips(Player player) {
        System.out.println("\n" + player.getName() + ", please set up your ships.\n");
    }
    public String askForSetShipsFx(Player player) {
        return ("\n" + player.getName() + ", please set up your ships.\n");
    }

    public void incorrectSelectionMessage() {
        System.out.print("Incorrect selection. Please, select again: ");
    }

    public void targetOutOfBoardMessage() {
        System.out.println("target out of board!");
    }

    public void boardDrawer() {

        System.out.println("Board of the game - your battlefield");
        Board board = new Board();
        System.out.print("  ");
        for (int i = 0; i< board.getColumns().size(); i++) {
            System.out.print("  " + board.getColumns().get(i));
        }
        System.out.println("");
        for (int r = 0; r< board.getRowsCount(); r++) {
            String spacing = "   ";
            int rowNumber = Integer.parseInt(board.getRows().get(r));
            if (rowNumber >= 10) {
                spacing = "  ";
            }
            System.out.print(rowNumber + spacing);
            for (int j = 0; j < board.getColumns().size(); j++) {
                System.out.print(".  ");
            }
            System.out.print("\n");
        }
    }

    public void printPlayerShots(Player player) {
        System.out.println("List of shots player '" + player.getName() + "':");
        for (String shot : player.getShots()) {
            System.out.println(shot);
        }
    }

    public void whoseTurnInformation(Player player) {
        System.out.println("\nPlayer '" + player.getName() + "' turn!");
    }

    public void printPlayerShips(Player player) {
        System.out.println("List of ships player '" + player.getName() + "':");
        player.getShips().stream()
                .flatMap(ship -> ship.getStatusOnBoard().entrySet().stream())
                .forEach(System.out::println);
    }

    public void printWinner(Player winner) {
        System.out.println("Player " + winner.getName() + " win the game!");
    }

    public void printShipBufferZone(Ship ship) {
        for (String field : ship.getBufferZone()) {
            System.out.println(" - printing buffer zone of ship");
            System.out.println("(" + field + ")");
            System.out.println("-------------------");
        }
    }

    public void playersBoardDrawer(Player player) {
        System.out.println("Board of the game - " + player.getName() + "'s battlefield");
        List<Ship> playerShips = player.getShips();

        Board playersBoard = new Board();
        System.out.print("  ");
        for (int i = 0; i< playersBoard.getColumns().size(); i++) {
            System.out.print("  " + playersBoard.getColumns().get(i));
        }
        System.out.println("");
        for (int r = 0; r< playersBoard.getRowsCount(); r++) {
            String spacing = "   ";
            int rowNumber = Integer.parseInt(playersBoard.getRows().get(r));
            if (rowNumber >= 10) {
                spacing = "  ";
            }
            System.out.print(rowNumber + spacing);
            for (int j = 0; j < playersBoard.getColumns().size(); j++) {
                String field = ".";
                String coordinates;
                coordinates = playersBoard.getColumns().get(j) + playersBoard.getRows().get(r);
                for (Ship ship : playerShips) {
                    if (ship.getStatusOnBoard().get(coordinates) != null) {
                        field = Character.toString(ship.getStatusOnBoard().get(coordinates).toUpperCase().charAt(0));
                    }
                }
                System.out.print(field + "  ");
            }
            System.out.print("\n");
        }
    }

    public void printAvailableFieldsForShipSetUp(List<String> availableFieldsForShipSetUp) {
        String a = "";
        if (availableFieldsForShipSetUp.toString().length() > 50) {
            a = availableFieldsForShipSetUp.toString().substring(0,45) + " ... ]";
        } else {
            a = availableFieldsForShipSetUp.toString();
        }
        System.out.println("\nAvailable fields for ship: " + a);
    }

    public void hostileBoardDrawer(Player attacker, Player hostile) {
        System.out.println("Board of the game - " + hostile.getName() + "'s battlefield");
        List<Ship> playerShips = hostile.getShips();
        Set<String> attackerShots = attacker.getShots();

        Board playersBoard = new Board();
        System.out.print("  ");
        for (int i = 0; i< playersBoard.getColumns().size(); i++) {
            System.out.print("  " + playersBoard.getColumns().get(i));
        }
        System.out.println("");
        for (int r = 0; r< playersBoard.getRowsCount(); r++) {
            String spacing = "   ";
            int rowNumber = Integer.parseInt(playersBoard.getRows().get(r));
            if (rowNumber >= 10) {
                spacing = "  ";
            }
            System.out.print(rowNumber + spacing);
            for (int j = 0; j < playersBoard.getColumns().size(); j++) {
                String field = ".";
                String coordinates;
                coordinates = playersBoard.getColumns().get(j) + playersBoard.getRows().get(r);
                for (String shot : attackerShots) {
                    if (attackerShots.contains(coordinates)) {
                        field = "x";
                    }

                }
                for (Ship ship : playerShips) {
                    if (ship.getStatusOnBoard().get(coordinates) != null) {
                        field = Character.toString(ship.getStatusOnBoard().get(coordinates).toUpperCase().charAt(0));
                        if (field.equals("G")) {
                            field = ".";
                        }
                    }
                }
                System.out.print(field + "  ");
            }
            System.out.print("\n");
        }
    }
}
