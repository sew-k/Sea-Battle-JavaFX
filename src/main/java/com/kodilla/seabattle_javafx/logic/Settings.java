package com.kodilla.seabattle_javafx.logic;

import com.kodilla.seabattle_javafx.data.Board;
import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;

import java.util.*;

public class Settings extends Options {
    private final String optionsTitle = "Game Settings";
    private final List<String> settings = new ArrayList<>(Arrays.asList("Ships configuration", "Player settings", "Game board size", "Exit settings"));
    private static Map<Integer,Integer> shipCountSettings;
    static {
        shipCountSettings = new HashMap<>();
        shipCountSettings.put(4,1);
        shipCountSettings.put(3,2);
        shipCountSettings.put(2,3);
        shipCountSettings.put(1,4);
    }

    private static boolean onePlayerGame = true;
    private String keyForChangeSettings = "y";
    private String keyForLeaveSettings = "n";

    public String getKeyForChangeSettings() {
        return keyForChangeSettings;
    }

    public String getKeyForLeaveSettings() {
        return keyForLeaveSettings;
    }

    public void setDefaultShipCountSettings() {
        //temporarily - default values
        shipCountSettings.put(4,1);
        shipCountSettings.put(3,2);
        shipCountSettings.put(2,3);
        shipCountSettings.put(1,4);
    }
    public void setDefaultShipCountSettingsForTest() {
        //temporarily - default values
        shipCountSettings.put(4,0);
        shipCountSettings.put(3,0);
        shipCountSettings.put(2,0);
        shipCountSettings.put(1,1);
    }

    @Override
    public List<String> getOptions() {
        return settings;
    }
    @Override
    public String getOptionsTitle() {
        return optionsTitle;
    }

    public static Map<Integer, Integer> getShipCountSettings() {
        return shipCountSettings;
    }

    public static void setShipCountSettings(int shipSize) {
        Keyboard keyboard = new Keyboard();
        Printer printer = new Printer();
        printer.printShipSettingsToChange(shipSize);
        int key = keyboard.getInt();
        shipCountSettings.replace(shipSize,key);
    }

    public static boolean isOnePlayerGame() {
        return onePlayerGame;
    }

    public static void setOnePlayerGame(boolean onePlayerGame) {
        Settings.onePlayerGame = onePlayerGame;
    }


    public boolean changeSettingsOrLeave(String key) {
        boolean result = false;
        if (key.equals(getKeyForChangeSettings())) {
            result = true;
        } else if (!key.equals(getKeyForChangeSettings())) {
            result = false;
        }
        return result;
    }

    @Override
    public void selectOption() {

        super.selectOption();
        Keyboard keyboard = new Keyboard();
        Validator validator = new Validator();
        Printer printer = new Printer();
        GameProcessor processor = new GameProcessor();
        printer.askForSelect();
        boolean end = false;
        while (!end) {
            String key = keyboard.getString();
            if (validator.validateForOptions(key, this)) {
                if (Integer.parseInt(key) == 0) {
                    Map<Integer,Integer> shipCountSettings = getShipCountSettings();
                    printer.printShipCountSettings(shipCountSettings);
                    printer.printChangeSettingsOrLeave();
                    key = keyboard.getString();
                    boolean correct = false;
                    while (!correct) {
                        if (validator.validateForChangeSettings(key, this)) {
                            if (changeSettingsOrLeave(key)) {
                                correct = true;
                                for (Map.Entry<Integer,Integer> entry : shipCountSettings.entrySet()) {
                                    setShipCountSettings(entry.getKey());
                                }
                                printer.optionsPrinter(this);
                                selectOption();

                            } else if (!changeSettingsOrLeave(key)) {
                                correct = true;
                                printer.optionsPrinter(this);
                                selectOption();
                            }
                        } else {
                            printer.incorrectSelectionMessage();
                            key = keyboard.getString();
                        }
                    }
                } else if (Integer.parseInt(key) == 1) {                        //for player settings
                    PlayerSettings playerSettings = new PlayerSettings();
                    printer.optionsPrinter(playerSettings);
                    printer.currentPlayerSettingsPrinter();
                    printer.printChangeSettingsOrLeave();
                    key = keyboard.getString();
                    boolean correct = false;
                    while (!correct) {
                        if (validator.validateForChangeSettings(key, playerSettings)) {
                            if (changeSettingsOrLeave(key)) {
                                correct = true;
                                playerSettings.selectOption();
                            } else if (!changeSettingsOrLeave(key)) {
                                correct = true;
                                printer.optionsPrinter(this);
                                selectOption();
                            }
                        } else {
                            printer.incorrectSelectionMessage();
                            key = keyboard.getString();
                        }
                    }
                    printer.optionsPrinter(this);
                    selectOption();

                } else if (Integer.parseInt(key) == 2) {
                    printer.printGameBoardSettings();
                    printer.printChangeSettingsOrLeave();
                    key = keyboard.getString();
                    boolean correct = false;
                    while (!correct) {
                        if (validator.validateForChangeSettings(key, this)) {
                            if (changeSettingsOrLeave(key)) {

                                printer.askToSetNewNumberOfColumns();
                                int newColumnsNumber = keyboard.getInt();
                                while (!((newColumnsNumber > 2) && (newColumnsNumber < 27))) {
                                    printer.incorrectSelectionMessage();
                                    newColumnsNumber = keyboard.getInt();
                                    }
                                if ((newColumnsNumber > 2) && (newColumnsNumber < 27)) {
                                    Board.setColumnsCount(newColumnsNumber);
                                    printer.askToSetNewNumberOfRows();
                                    int newRowsNumber = keyboard.getInt();
                                    while (!((newRowsNumber > 2) && (newRowsNumber < 27))) {
                                        printer.incorrectSelectionMessage();
                                        newRowsNumber = keyboard.getInt();
                                    }
                                    if ((newRowsNumber > 2) && (newRowsNumber < 27)) {
                                        Board.setRowsCount(newRowsNumber);
                                    }
                                }
                                correct = true;
                                printer.optionsPrinter(this);
                                selectOption();

                            } else if (!changeSettingsOrLeave(key)) {
                                correct = true;
                                printer.optionsPrinter(this);
                                selectOption();
                            }
                        } else {
                            printer.incorrectSelectionMessage();
                            key = keyboard.getString();
                        }
                    }

                } else if (Integer.parseInt(key) == 3) {
                    processor.processGame();
                }
                return;
            } else {
                printer.incorrectSelectionMessage();
            }
        }
    }
}
