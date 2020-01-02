package app.view;

import app.Main;
import app.controller.GameController;
import app.controller.fxmlController.UiController;
import app.model.Direction;
import app.model.Grid;
import app.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.IOException;

public class Game extends BorderPane {

    private Model model;
    private GameController controller;

    private Canvas canvas;
    private Grid grid;
    private GraphicsContext graphicsContext;
    private Button buttonBack;
    private GridPane gp;

    public Game(Model model, GameController controller) {
        this.model = model;
        this.controller = controller;
    }




    private void initElement() throws IOException {
        // init play zone
        if(model.getSolo()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/fxmlView/UISolo.fxml"));
            gp = fxmlLoader.load();
            ((UiController)fxmlLoader.getController()).setController(this.controller);
            ((UiController)fxmlLoader.getController()).setModel(this.model);
            canvas = new Canvas(Model.getWindowWidth(), model.getWindowHeight());
            graphicsContext = canvas.getGraphicsContext2D();
            //TODO background canvas
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(0,0,canvas.getWidth(), canvas.getHeight());

            gp.add(canvas, 1, 0);
            gp.setStyle("-fx-background-color: #263238;");

            // init keyboard
            setOnKeyPressed(controller);
        }
        else {

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view/fxmlView/UI.fxml"));

            gp = fxmlLoader.load();
            ((UiController) fxmlLoader.getController()).setController(this.controller);
            ((UiController) fxmlLoader.getController()).setModel(this.model);
            canvas = new Canvas(Model.getWindowWidth(), model.getWindowHeight());
            graphicsContext = canvas.getGraphicsContext2D();
            //TODO background canvas
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            gp.add(canvas, 1, 0);
            gp.setStyle("-fx-background-color: #263238;");

            // init keyboard
            setOnKeyPressed(controller);
        }
    }

    private void createView() {
        gp.setStyle("-fx-background-color: #263238;");
        // create play zone
        setCenter(gp);

        try {
            controller.clipMusiqueFond = AudioSystem.getClip();
            controller.clipMusiqueFond.open(AudioSystem.getAudioInputStream(controller.musiqueFond));
            controller.volumeMusiqueFond = (FloatControl) controller.clipMusiqueFond.getControl(FloatControl.Type.MASTER_GAIN);
            controller.volumeMusiqueFond.setValue(-10.0f);

            controller.clipMusiqueFond.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


    }

    public void display() {
        try {
            initElement();
            createView();
        }catch(IOException io) {

        }

        try {
            model.clipMangerFruit = AudioSystem.getClip();
            model.clipMangerFruit.open(AudioSystem.getAudioInputStream(model.mangerFruit));

            model.clipMort = AudioSystem.getClip();
            model.clipMort.open(AudioSystem.getAudioInputStream(model.mort));

            model.clipGameOver = AudioSystem.getClip();
            model.clipGameOver.open(AudioSystem.getAudioInputStream(model.gameOver));

            model.clipMangerFruit = AudioSystem.getClip();
            model.clipMangerFruit.open(AudioSystem.getAudioInputStream(model.mangerFruit));

            model.clipTp = AudioSystem.getClip();
            model.clipTp.open(AudioSystem.getAudioInputStream(model.tp));

            model.volumeTp = (FloatControl) model.clipTp.getControl(FloatControl.Type.MASTER_GAIN);
            model.volumeTp.setValue(-20.0f);

            model.clipRedirection = AudioSystem.getClip();
            model.clipRedirection.open(AudioSystem.getAudioInputStream(model.redirection));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void updateDuo() {
        drawGrid();
        drawItem();
        drawSnakeSolo();
        drawSnakeDuo();
        drawTP();
        drawRedirection();
        drawEffect();
    }

    public void updateSolo(){
        drawGrid();
        drawItem();
        drawSnakeSolo();
        drawTP();
        drawRedirection();
        drawEffect();
    }


    private void drawGrid() {

        //TODO voir alternative

        graphicsContext.clearRect(0,0, Model.getWindowWidth(), Model.getWindowHeight());
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, Model.getWindowWidth(), Model.getWindowHeight());
//        graphicsContext.setStroke(Color.LIGHTGRAY);
//        graphicsContext.setLineWidth(0.1);

    }

    private Image imageCorpsSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpent.png"));
    private Image imageCorpsSerpent2 = new Image(getClass().getResourceAsStream("../ressources/images/serpent2.png"));

    private void drawSnakeDuo() {

//        for (int i = 0; i < model.getSnake1().getTail().size(); i++) {
//            graphicsContext.drawImage(imageCorpsSerpent, model.getSnake1().getTail().get(i).getX(), model.getSnake1().getTail().get(i).getY(), model.getSnake1().getBlockSize(), model.getSnake1().getBlockSize());
//
//        }
//
//        Image imageSerpent;
//        if(model.getSnake1().getCurrentDirection()== Direction.UP) {
//            imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentHaut.png"));
//        } else if(model.getSnake1().getCurrentDirection()== Direction.DOWN) {
//            imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentBas.png"));
//        } else if(model.getSnake1().getCurrentDirection()== Direction.LEFT) {
//            imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentGauche.png"));
//        } else {
//            imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentDroite.png"));
//        }
//        graphicsContext.drawImage(imageSerpent, model.getSnake1().getHeadLocation().getX()-2.5, model.getSnake1().getHeadLocation().getY()-2.5, model.getSnake1().getBlockSize()+5, model.getSnake1().getBlockSize()+5);

//        if(model.getSnake1().getCurrentDirection()== Direction.UP) {
//            Image imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentHaut.png"));
//            graphicsContext.drawImage(imageSerpent, model.getSnake1().getHeadLocation().getX(), model.getSnake1().getHeadLocation().getY(), model.getSnake1().getBlockSize(), model.getSnake1().getBlockSize());
//        } else if(model.getSnake1().getCurrentDirection()== Direction.DOWN) {
//            Image imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentBas.png"));
//            graphicsContext.drawImage(imageSerpent, model.getSnake1().getHeadLocation().getX(), model.getSnake1().getHeadLocation().getY(), model.getSnake1().getBlockSize(), model.getSnake1().getBlockSize());
//        } else if(model.getSnake1().getCurrentDirection()== Direction.LEFT) {
//            Image imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentGauche.png"));
//            graphicsContext.drawImage(imageSerpent, model.getSnake1().getHeadLocation().getX(), model.getSnake1().getHeadLocation().getY(), model.getSnake1().getBlockSize(), model.getSnake1().getBlockSize());
//        } else {
//            Image imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentDroite.png"));
//            graphicsContext.drawImage(imageSerpent, model.getSnake1().getHeadLocation().getX(), model.getSnake1().getHeadLocation().getY(), model.getSnake1().getBlockSize(), model.getSnake1().getBlockSize());
//        }
        for (int i = 0; i < model.getSnake2().getTail().size(); i++) {
            graphicsContext.drawImage(imageCorpsSerpent2, model.getSnake2().getTail().get(i).getX(), model.getSnake2().getTail().get(i).getY(), model.getSnake2().getBlockSize(), model.getSnake2().getBlockSize());
        }

        Image imageSerpent2;
        if(model.getSnake2().getCurrentDirection()== Direction.UP) {
            imageSerpent2 = new Image(getClass().getResourceAsStream("../ressources/images/serpentHaut2.png"));
        } else if(model.getSnake2().getCurrentDirection()== Direction.DOWN) {
            imageSerpent2 = new Image(getClass().getResourceAsStream("../ressources/images/serpentBas2.png"));
        } else if(model.getSnake2().getCurrentDirection()== Direction.LEFT) {
            imageSerpent2 = new Image(getClass().getResourceAsStream("../ressources/images/serpentGauche2.png"));
        } else {
            imageSerpent2 = new Image(getClass().getResourceAsStream("../ressources/images/serpentDroite2.png"));
        }
        graphicsContext.drawImage(imageSerpent2, model.getSnake2().getHeadLocation().getX()-2.5, model.getSnake2().getHeadLocation().getY()-2.5, model.getSnake2().getBlockSize()+5, model.getSnake2().getBlockSize()+5);

//            if(model.getSnake2().getCurrentDirection()== Direction.UP) {
//                Image imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentHaut.png"));
//                graphicsContext.drawImage(imageSerpent, model.getSnake2().getHeadLocation().getX(), model.getSnake2().getHeadLocation().getY(), model.getSnake2().getBlockSize(), model.getSnake2().getBlockSize());
//            } else if(model.getSnake2().getCurrentDirection()== Direction.DOWN) {
//                Image imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentBas.png"));
//                graphicsContext.drawImage(imageSerpent, model.getSnake2().getHeadLocation().getX(), model.getSnake2().getHeadLocation().getY(), model.getSnake2().getBlockSize(), model.getSnake2().getBlockSize());
//            } else if(model.getSnake2().getCurrentDirection()== Direction.LEFT) {
//                Image imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentGauche.png"));
//                graphicsContext.drawImage(imageSerpent, model.getSnake2().getHeadLocation().getX(), model.getSnake2().getHeadLocation().getY(), model.getSnake2().getBlockSize(), model.getSnake2().getBlockSize());
//            } else {
//                Image imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentDroite.png"));
//                graphicsContext.drawImage(imageSerpent, model.getSnake2().getHeadLocation().getX(), model.getSnake2().getHeadLocation().getY(), model.getSnake2().getBlockSize(), model.getSnake2().getBlockSize());
//            }

    }

    public void drawSnakeSolo(){
        for (int i = 0; i < model.getSnake1().getTail().size(); i++) {
            graphicsContext.drawImage(imageCorpsSerpent, model.getSnake1().getTail().get(i).getX(), model.getSnake1().getTail().get(i).getY(), model.getSnake1().getBlockSize(), model.getSnake1().getBlockSize());

        }

        Image imageSerpent;
        if(model.getSnake1().getCurrentDirection()== Direction.UP) {
            imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentHaut.png"));
        } else if(model.getSnake1().getCurrentDirection()== Direction.DOWN) {
            imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentBas.png"));
        } else if(model.getSnake1().getCurrentDirection()== Direction.LEFT) {
            imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentGauche.png"));
        } else {
            imageSerpent = new Image(getClass().getResourceAsStream("../ressources/images/serpentDroite.png"));
        }
        graphicsContext.drawImage(imageSerpent, model.getSnake1().getHeadLocation().getX()-2.5, model.getSnake1().getHeadLocation().getY()-2.5, model.getSnake1().getBlockSize()+5, model.getSnake1().getBlockSize()+5);
    }

    private void drawItem() {
        for(int i = 0; i < model.getGrid().getListItem().size(); i++){
            if(model.getGrid().getListItem().get(i).getType() == 1){
                Image image = new Image(getClass().getResourceAsStream("../ressources/images/fruit.png"));
                graphicsContext.drawImage(image, model.getGrid().getListItem().get(i).getLocation().getX(), model.getGrid().getListItem().get(i).getLocation().getY(), Model.getGridBlockSize(), Model.getGridBlockSize());
            } else if(model.getGrid().getListItem().get(i).getType() == 2){
                Image image = new Image(getClass().getResourceAsStream("../ressources/images/superFruit.png"));
                graphicsContext.drawImage(image, model.getGrid().getListItem().get(i).getLocation().getX(), model.getGrid().getListItem().get(i).getLocation().getY(), Model.getGridBlockSize(), Model.getGridBlockSize());
            } else if(model.getGrid().getListItem().get(i).getType() == 3){
                Image image = new Image(getClass().getResourceAsStream("../ressources/images/malus.png"));
                graphicsContext.drawImage(image, model.getGrid().getListItem().get(i).getLocation().getX(), model.getGrid().getListItem().get(i).getLocation().getY(), Model.getGridBlockSize(), Model.getGridBlockSize());
            }
        }
    }

    private void drawTP(){
        Image image = new Image(getClass().getResourceAsStream("../ressources/images/TP.png"));
        graphicsContext.drawImage(image, model.getGrid().getTp().getCouple().get(0).getX(), model.getGrid().getTp().getCouple().get(0).getY(), Model.getGridBlockSize(), Model.getGridBlockSize());
        graphicsContext.drawImage(image, model.getGrid().getTp().getCouple().get(1).getX(), model.getGrid().getTp().getCouple().get(1).getY(), Model.getGridBlockSize(), Model.getGridBlockSize());
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText(""+ model.getGrid().getTp().getUtilisationsRestantes(),model.getGrid().getTp().getCouple().get(0).getX(), model.getGrid().getTp().getCouple().get(0).getY() + Model.getGridBlockSize() );
        graphicsContext.fillText(""+ model.getGrid().getTp().getUtilisationsRestantes(),model.getGrid().getTp().getCouple().get(1).getX(), model.getGrid().getTp().getCouple().get(1).getY() + Model.getGridBlockSize());
    }

    private void drawRedirection(){
        Image image = new Image(getClass().getResourceAsStream("../ressources/images/arrow"+ model.getGrid().getRedirection().getDirection().toString() + ".png"));
        graphicsContext.drawImage(image, model.getGrid().getRedirection().getPos().getX(), model.getGrid().getRedirection().getPos().getY(), Model.getGridBlockSize(), Model.getGridBlockSize());
    }

    private void drawEffect(){
        canvas.setEffect(model.getBoxBlur());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public GameController getController() {
        return controller;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public Button getButtonBack() {
        return buttonBack;
    }

    public void setButtonBack(Button buttonBack) {
        this.buttonBack = buttonBack;
    }
}
