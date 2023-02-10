package com.kodilla.seabattle_javafx.logic;

import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerSettings extends Options {
    private final String optionsTitle = "Player Settings";
    private final List<String> playerSettings = new ArrayList<>(Arrays.asList(
            "Player vs Computer (normal)",
            "Player vs Computer (hard)",             //TODO - implementation of hard-version of ComputerPlayer
            "Player vs Player (hot seat)",
            "Computer vs Computer"));                //TODO - implementation different names or numerators for both Cpu-players
    private static int currentPlayerSettings = 2;

    @Override
    public String getOptionsTitle() {
        return optionsTitle;
    }
    @Override
    public List<String> getOptions() {
        return playerSettings;
    }

    public static int getCurrentPlayerSettings() {
        return currentPlayerSettings;
    }

    public static void setCurrentPlayerSettings(int currentPlayerSettings) {
        PlayerSettings.currentPlayerSettings = currentPlayerSettings;
    }

    @Override
    public void selectOption() {

        super.selectOption();
        Keyboard keyboard = new Keyboard();
        Validator validator = new Validator();
        Printer printer = new Printer();
        printer.askForSelect();
        boolean end = false;
        while (!end) {
            String key = keyboard.getString();
            if (validator.validateForOptions(key, this)) {
                setCurrentPlayerSettings(Integer.parseInt(key));
                printer.currentPlayerSettingsPrinter();
                end = true;
            } else {
                printer.incorrectSelectionMessage();
            }
        }
    }

}
