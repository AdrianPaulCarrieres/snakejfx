package app.controller.fxmlController;


import app.view.fxmlView.FxmlViewManager;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import javax.tools.Tool;
import java.awt.*;

public class MainMenuController extends FxmlController {
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public MainMenuController(){
	}

	@FXML
	public void commencer(){
		FxmlViewManager.loadInStage("ModeSelect",model, primaryStage);
	}

	@FXML
	public void scores(){
		FxmlViewManager.loadInStage("Score",model, primaryStage);
	}

	@FXML
    private GridPane anchorRoot;	
	@FXML
    private StackPane parentContainer;
	
	@FXML
	private void afficherAide(){
		FxmlViewManager.loadInStage("Aide",model, primaryStage);
		System.out.println(screenSize.getWidth());
	 }

	public void exit() {
		System.exit(0);
	}

}
