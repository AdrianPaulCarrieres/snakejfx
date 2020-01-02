package app.controller.fxmlController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import app.view.fxmlView.FxmlViewManager;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AideController extends FxmlController {

	public AideController(){}

	@FXML
	private Button boutonRetour;

	@FXML
    private AnchorPane container;

    @FXML
    private void retourScene(ActionEvent event) throws IOException {
    	System.out.println("Retour au menu");
		FxmlViewManager.loadInStage("MainMenu",model, primaryStage);

//		;//Creates a Parent called loader and assign it as ScReen2.FXML
//	    Scene scene = boutonRetour.getScene(); //This creates a new scene called scene and assigns it as the Sample.FXML document which was named "loader"
//	    Stage app_stage = (Stage) ( (Node) event.getSource()).getScene().getWindow(); //this accesses the window.
//	    app_stage.setScene(scene); //This sets the scene as scene
//	    app_stage.show();
//	    StackPane parentContainer = (StackPane) scene.getRoot();
//	    loader.translateXProperty().set(scene.getHeight());
//	    parentContainer.getChildren().add(loader);
//
//	    //Create new TimeLine animation
//        Timeline timeline = new Timeline();
//        //Animate Y property
//        KeyValue kv = new KeyValue(loader.translateXProperty(), 0, Interpolator.EASE_IN);
//        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
//        timeline.getKeyFrames().add(kf);
//        //After completing animation, remove first scene
//        timeline.setOnFinished(t -> {
//            parentContainer.getChildren().remove(container);
//        });
//        timeline.play();
    }


}
