package app.controller.fxmlController;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import app.controller.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;
import app.model.Model;
import app.model.Snake;
import app.view.Game;
import app.view.fxmlView.FxmlViewManager;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class UiController{
	private Model model;
	private GameController gameController;

	public UiController() {
		
	}
	
	public void setController(GameController controller) {
		gameController = controller;
	}
	public void setModel(Model model_) {
		model = model_;
	}

    @FXML
    private Canvas canvas;
    public GridPane GP;
    public Label score;
    public Label score2;

    @FXML
    private void bouger(KeyEvent event){
//        System.out.print(event.getCode());
//        snakeGame.touche(event.getCode());
    }
    @FXML
    private Button Start;
    public Button Pause;
    @FXML
    public void start(ActionEvent event) {
        Pause.setText("Pause");
        gameController.setPause(Pause);
        gameController.clipMusiqueFond.stop();
        gameController.clipMusiqueFond.setFramePosition(0);
        gameController.clipMusiqueFond.start();
    	if(model.isGameInProgress()) {
    		
    		gameController.endGame("le joueur a mis fin à la partie");
    		
    	}else {
    	    gameController.setLabelScore(score, score2);
    	    gameController.start(Start);
    	}


    }

    @FXML
    public void changeCouleur(Button butt){
        butt.setStyle("-fx-background-color: #090a0c,        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),        linear-gradient(#20262b, #191d22),        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0)); -fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0; -fx-text-fill: white; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 ); -fx-text-fill: linear-gradient(white, #d0d0d0); -fx-font-size: 12; -fx-padding: 10 20 10 20;");
        TimerTask task = new TimerTask() {
            public void run() {
                butt.setStyle("-fx-background-color: #090a0c,        linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),        linear-gradient(#20262b, #191d22),        radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0)); -fx-background-radius: 5,4,3,5; -fx-background-insets: 0,1,2,0; -fx-text-fill: white; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 ); -fx-font-size: 12; -fx-padding: 10 20 10 20;");
            }
        };
        Timer timer = new Timer("Timer");
        long delay = 100; //  delay time in ms
        timer.schedule(task, delay);
    }


    @FXML
    private Button Quitter;

    @FXML
    void exitProgram(ActionEvent event){
        changeCouleur(((Button)event.getSource()));
        System.out.println("A bientot !");
        System.exit(0);
    }


    @FXML
    private void Mettrepause(ActionEvent event) {
    	gameController.pause();
    }

    @FXML
    private Button MenuPr;

    public void menu() {
    	gameController.menu();
    }


    private static boolean sonactivé=true;

    @FXML
    private Button Mute;

    @FXML
    private void son(){
        if (Mute.getText().equals("Mute")){
            Mute.setText("Son");
        }else{
            Mute.setText("Mute");
        }
        if (sonactivé){
            sonactivé=false;
            System.out.println("son désactivé");
            gameController.clipMusiqueFond.stop();
            model.clipTp.close();
            model.clipMangerFruit.close();
            model.clipGameOver.close();
            model.clipMort.close();
            model.clipRedirection.close();
        }
        else {
            sonactivé=true;
            System.out.println("son activé");
            gameController.clipMusiqueFond.start();
            try {
                model.clipTp = AudioSystem.getClip();
                model.clipTp.open(AudioSystem.getAudioInputStream(model.tp));

                model.clipMangerFruit = AudioSystem.getClip();
                model.clipMangerFruit.open(AudioSystem.getAudioInputStream(model.mangerFruit));

                model.clipGameOver = AudioSystem.getClip();
                model.clipGameOver.open(AudioSystem.getAudioInputStream(model.gameOver));

                model.clipMort = AudioSystem.getClip();
                model.clipMort.open(AudioSystem.getAudioInputStream(model.mort));

                model.clipRedirection = AudioSystem.getClip();
                model.clipRedirection.open(AudioSystem.getAudioInputStream(model.redirection));
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.volumeTp = (FloatControl) model.clipTp.getControl(FloatControl.Type.MASTER_GAIN);
            model.volumeTp.setValue(-20.0f);
        }
    }
        }
