package app.controller.fxmlController;

import app.model.Model;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class FxmlController {

    Model model;
    Stage primaryStage;

    /**
     * empty constructor
     */
    public FxmlController() {
    }

    /**
     * model setters
     * @param model model
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * primaryStage setter
     * @param primaryStage primaryStage
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * initialize method
     */
    @FXML
    public void initialize() {
    }
}
