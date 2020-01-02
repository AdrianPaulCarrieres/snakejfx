package app.controller.fxmlController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import app.controller.GameController;
import app.model.Model;
import app.view.fxmlView.FxmlViewManager;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VueController extends FxmlController {

	public VueController(){
	}

	@FXML
	public void commencer(){
		model.initGame(Model.getTaskUpdatePeriodMs(),
				Model.getTaskUpdateDelayMs(),
				1,true
		);
		new GameController(model, primaryStage);
	}

	@FXML
	public void scores(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Score.fxml"));
		Scene scene = new Scene(root,400,400);
		Stage stage = (Stage) ( (Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	@FXML
    private GridPane anchorRoot;	
	@FXML
    private StackPane parentContainer;
	
	@FXML
	private void afficherAide(){

		FxmlViewManager.loadInStage("Aide",model, primaryStage);

//
//		    Scene scene = anchorRoot.getScene(); //This creates a new scene called scene and assigns it as the Sample.FXML document which was named "loader"
//		    Stage app_stage = (Stage) ( (Node) event.getSource()).getScene().getWindow(); //this accesses the window.
//		    app_stage.setScene(scene); //This sets the scene as scene
//		    app_stage.show();
//		    loader.translateXProperty().set(scene.getHeight());
//		    parentContainer.getChildren().add(loader);
//
//		    //Create new TimeLine animation
//	        Timeline timeline = new Timeline();
//	        //Animate Y property
//	        KeyValue kv = new KeyValue(loader.translateXProperty(), 0, Interpolator.EASE_IN);
//	        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
//	        timeline.getKeyFrames().add(kf);
//	        //After completing animation, remove first scene
//	        timeline.setOnFinished(t -> {
//	            parentContainer.getChildren().remove(anchorRoot);
//	        });
//	        timeline.play();
	 }

	public void exit(ActionEvent event) {
		System.out.println("A bientot !");
		System.exit(0);
	}

}
