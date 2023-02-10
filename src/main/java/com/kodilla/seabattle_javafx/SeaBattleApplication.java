package com.kodilla.seabattle_javafx;

import com.kodilla.seabattle_javafx.logic.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class SeaBattleApplication extends Application {

    private Options menu = new Menu();
    private Options settings = new Settings();
    private Options playerSettings = new PlayerSettings();
    private Options playerTurnOptions = new PlayerTurnOptions();
    @Override
    public void start(Stage primaryStage) throws IOException {
        GameProcessor gameProcessor = new GameProcessor();
        primaryStage.setAlwaysOnTop(true);
        gameProcessor.processGameFX(primaryStage);
        primaryStage.setAlwaysOnTop(false);
    }

    public static void main(String[] args) {
        launch();
    }
}