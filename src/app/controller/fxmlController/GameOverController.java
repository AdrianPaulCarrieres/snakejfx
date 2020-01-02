package app.controller.fxmlController;


import app.controller.GameController;
import app.model.Model;
import app.view.fxmlView.FxmlViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ResourceBundle;


public class GameOverController extends FxmlController implements Initializable {
    private GameController gameController;

    public void setController(GameController controller) {
        gameController = controller;
    }
    public void setModel(Model model_) {
        model = model_;
    }

	public GameOverController() {
		
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
        //score3.setText(tblscores[2]);

	}
	
	@FXML
	private void retourMenu(ActionEvent event) throws Exception{
		FxmlViewManager.loadInStage("MainMenu",model, primaryStage);
	}


}
