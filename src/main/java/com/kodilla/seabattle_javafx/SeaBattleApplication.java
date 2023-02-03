package com.kodilla.seabattle_javafx;

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
        //FXMLLoader fxmlLoader = new FXMLLoader(SeaBattleApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);

//        FileInputStream fileInputStream = new FileInputStream("C:/dev/kodilla-course/SeaBattle_JavaFX/build/resources/SeaBattleImage.jpg");
//        VBox root = new VBox();
//        Image image = new Image(fileInputStream);
//        ImageView imageView = new ImageView(image);
//        imageView.setFitWidth(320);
//        imageView.setPreserveRatio(true);
//        root.getChildren().add(imageView);
//
//        Options mainMenu = new Menu();
//        List<String> mainMenuList = mainMenu.getOptions();
//        for (String option : mainMenuList) {
//            Button bt = new Button(option);
//            bt.setMinSize(100,30);
//            root.getChildren().add(bt);
//        }
//        root.setAlignment(Pos.BASELINE_CENTER);
//        Scene scene = new Scene(root,320, 380);
//
//        primaryStage.setTitle("Sea Battle Game");
//        primaryStage.setScene(scene);
//        primaryStage.show();

        Drawer drawer = new Drawer();
        drawer.drawMenu(menu, primaryStage);

        GameProcessor gameProcessor = new GameProcessor();
        gameProcessor.processGameFX(primaryStage);
    }

    public static void main(String[] args) {
        launch();
    }
}