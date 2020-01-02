package app.controller.fxmlController;


import app.controller.GameController;
import app.model.Model;
import app.view.fxmlView.FxmlViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import javax.jws.WebParam;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ModeSelectController extends FxmlController implements Initializable {
    private  boolean isreverse = false;

//    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//    private int width = 1500;
//    private int height = 1000;
//    private int block = 50;

    private int taskPeriod = 10;
    private int taskDelay = 10;

    @FXML
    private ChoiceBox listeMods;
    @FXML
    private javafx.scene.control.Label labelmod;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void choix(){
        if (listeMods.getValue().equals("Reverse Snake")){
            isreverse = true;
            labelmod.setText("Reverse Snake");
        }else {
            labelmod.setText("Snake");
            isreverse = false;
        }
    }
    
    @FXML
    private ChoiceBox listeVitesse;
    @FXML
    private javafx.scene.control.Label labelvitesse;
    @FXML
    public void choixVit(){
        if (listeVitesse.getValue().equals("Easy")){
            taskPeriod = 20;
            taskDelay = 20;
            labelvitesse.setText("Easy");
        }
        else if (listeVitesse.getValue().equals("Medium")){
            taskPeriod = 10;
            taskDelay = 10;
            labelvitesse.setText("Medium");
        }
        else if (listeVitesse.getValue().equals("Hard")){
            taskPeriod = 2;
            taskDelay = 2;
            labelvitesse.setText("Hard");
        }
    }

    public ModeSelectController(){}
    @FXML
    public void Solo(){
        model.initGame(taskPeriod, taskDelay, 1, isreverse);
        new GameController(model, primaryStage);
    }
    @FXML
    public void Duo(){

        model.initGame(taskPeriod, taskDelay, 2, isreverse);
        new GameController(model, primaryStage);
    }

    @FXML
    public void IA(){

        model.initGame(taskPeriod, taskDelay, 3, isreverse);
        new GameController(model, primaryStage);
    }
    @FXML
    public void duoIA(){

        model.initGame(taskPeriod, taskDelay,  4, isreverse);
        new GameController(model, primaryStage);
    }
    @FXML
    public void Retour(){
        FxmlViewManager.loadInStage("MainMenu",model, primaryStage);
    }
}
