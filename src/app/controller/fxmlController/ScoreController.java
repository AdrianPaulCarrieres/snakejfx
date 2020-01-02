package app.controller.fxmlController;


import app.view.fxmlView.FxmlViewManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.print.DocFlavor.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class ScoreController extends FxmlController implements Initializable {
	
	public ScoreController() {
		
	}
	
	@FXML
	public Label score1;
	public Label score2;
	public Label score3;

	@Override 
	public void initialize(java.net.URL location, ResourceBundle resources) {
		String[] tblscores = new String[3];
        File fichier = new File(System.getProperty("user.dir")+"/src/app/ressources/Score.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(fichier));
            String line;
            int z=0;
            while ((line = br.readLine()) != null) {
                tblscores[z] = line;
                z++;
            }
            br.close();
        }catch (FileNotFoundException fne){
            System.out.println("fichier non trouvé");
        }catch (IOException ioexp){
            System.out.println("io exception");
        }
        score1.setText(tblscores[0]);
        score2.setText(tblscores[1]);
        score3.setText(tblscores[2]);
		
	}
	
	@FXML
	private void retour(ActionEvent event) throws Exception{
		FxmlViewManager.loadInStage("MainMenu",model, primaryStage);
	}


}
