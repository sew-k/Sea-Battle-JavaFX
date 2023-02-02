package com.kodilla.seabattle_javafx.logic;

import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Menu extends Options {
    private final String optionsTitle = "Game Menu";
    private final List<String> options = new ArrayList<>(Arrays.asList("Start Game","Settings","Score Board","Exit"));

    public List<String> getOptions() {
        return options;
    }
    @Override
    public String getOptionsTitle() {
        return optionsTitle;
    }
    @Override
    public void selectOption() {

        super.selectOption();
        Settings settings = new Settings();
        Keyboard keyboard = new Keyboard();
        Validator validator = new Validator();
        Printer printer = new Printer();
        GameProcessor processor = new GameProcessor();
        boolean incorrect = false;
        printer.askForSelect();

        while (!incorrect) {

            String key = keyboard.getString();
            if (validator.validateForOptions(key, this)) {
                if (Integer.parseInt(key) == 0) {
                    processor.startGame();
                } else if (Integer.parseInt(key) == 1) {
                    printer.optionsPrinter(settings);
                    settings.selectOption();
                } else if (Integer.parseInt(key) == 2) {
                    printer.showScoreBoard();
                    processor.processGame();
                } else if (Integer.parseInt(key) == 3) {
                    processor.exitGame();
                    return;
                }
                return;
            } else {
                printer.incorrectSelectionMessage();
            }
        }
    }
}
