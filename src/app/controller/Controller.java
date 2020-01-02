package app.controller;

import app.model.Model;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public abstract class Controller implements EventHandler {

    protected Model model ;
    protected Stage primaryStage;

    public Controller(Model model, Stage primaryStage) {
        this.model = model;
        this.primaryStage = primaryStage;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public abstract void handle(Event event);
}
