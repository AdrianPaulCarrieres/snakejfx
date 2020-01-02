package app.view.fxmlView;

import app.Main;
import app.controller.fxmlController.FxmlController;
import app.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class FxmlViewManager {

    /**
     * private constructor to prevent instantiation
     */
    private FxmlViewManager() {
        throw new UnsupportedOperationException();
    }

    /**
     * load the fxml as parent root into the primaryStage scene
     * @param fxmlFileName fxml file name located in view/fxmlView/ with the .fxml extension
     * @param model model
     * @param primaryStage primary stage
     */
    public static void loadInStage(String fxmlFileName, Model model, Stage primaryStage) {

        FXMLLoader fxmlLoader;
        FxmlController controller;

        fxmlLoader = new FXMLLoader(Main.class.getResource("view/fxmlView/"+fxmlFileName+".fxml"));
        try {
            if (primaryStage.getScene() == null)
                primaryStage.setScene(new Scene(fxmlLoader.load() ));
            else
                primaryStage.getScene().setRoot( fxmlLoader.load() );
        } catch (IOException ignored) {
        }
        controller = fxmlLoader.getController();
        controller.setModel(model);
        controller.setPrimaryStage(primaryStage);
    }


}
