package com.kodilla.seabattle_javafx.logic;

import com.kodilla.seabattle_javafx.presentation.Drawer;
import com.kodilla.seabattle_javafx.presentation.Keyboard;
import com.kodilla.seabattle_javafx.presentation.Printer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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
    public void selectOptionFx(Button button, Stage primaryStage) {
        Drawer drawer = new Drawer();
        GameProcessor gameProcessor = new GameProcessor();
        Settings settings = new Settings();

        if (button.getText().equals(this.options.get(0))) {
            gameProcessor.startGameFx(primaryStage);

        } else if (button.getText().equals(this.options.get(1))) {
            drawer.drawSettings();

        } else if (button.getText().equals(this.options.get(2))) {
            drawer.drawScoreBoard();

        } else if (button.getText().equals(this.options.get(3))) {
            gameProcessor.exitGameFx(primaryStage);
        }
    }
}
