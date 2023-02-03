package com.kodilla.seabattle_javafx.logic;

import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.List;

public abstract class Options {

    private List<String> options;
    private String optionsTitle;

    public List<String> getOptions() {
        return options;
    }

    public String getOptionsTitle() {
        return optionsTitle;
    }

    public void selectOption() {

    }
    public void selectOptionFx(Button button, Stage stage) {

    }

}
