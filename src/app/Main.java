package app;

import app.controller.GameController;
import app.model.Model;
import app.view.fxmlView.FxmlViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        FxmlViewManager.loadInStage("MainMenu",new Model(), primaryStage);

        primaryStage.setTitle("Snake");
        // primaryStage.getIcons().add(new
        // Image(Main.class.getResourceAsStream("resource/image/icon/icon_app.png")));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        primaryStage.setMinWidth(screenSize.getWidth() - 5); // - Pixels en bordure
        primaryStage.setMinHeight(screenSize.getHeight() - 50);

        primaryStage.setWidth(screenSize.getWidth() - 5);
        primaryStage.setHeight(screenSize.getHeight() - 5);

        primaryStage.setMaxWidth(screenSize.getWidth() - 5);
        primaryStage.setMaxHeight(screenSize.getHeight() - 5);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
        primaryStage.setFullScreen(true);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
