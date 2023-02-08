package com.kodilla.seabattle_javafx;

import com.kodilla.seabattle_javafx.data.HumanPlayer;
import com.kodilla.seabattle_javafx.data.Player;
import com.kodilla.seabattle_javafx.logic.*;
import com.kodilla.seabattle_javafx.presentation.Drawer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class SeaBattleApplication extends Application {

    private Options menu = new Menu();
    private Options settings = new Settings();
    private Options playerSettings = new PlayerSettings();
    private Options playerTurnOptions = new PlayerTurnOptions();
    @Override
    public void start(Stage primaryStage) throws IOException {
        GameProcessor gameProcessor = new GameProcessor();
        gameProcessor.processGameFX(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}