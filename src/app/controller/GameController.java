package app.controller;

import app.model.Direction;
import app.model.Model;
import app.model.Snake;
import app.view.Game;
import app.view.fxmlView.FxmlViewManager;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class GameController extends Controller {
    private Game view;
    private GameController controller;

    private AnimationTimer animationTimer;
    private Timer timer;
    private TimerTask task;

    private Label score;
    private Label score2;
    private Button start;
    private Button pause;
    private String MessageFin;

    public File musiqueFond = new File(System.getProperty("user.dir")+"/src/app/ressources/sons/MusiqueFond1.wav");
    public Clip clipMusiqueFond;
    public FloatControl volumeMusiqueFond;

    public void setPause(Button pausebtn){
        pause = pausebtn;
    }
    public GameController(Model model, Stage primaryStage) {
        super(model, primaryStage);
        view = new Game(model, this);
        if (primaryStage.getScene() == null)
            primaryStage.setScene(new Scene(view));
        else
            primaryStage.getScene().setRoot(view);
        view.display();
    }
    public void start(Button startbtn){
        start = startbtn;
        start.setText("Arrêter");
        pause.setText("Pause");
        model.setNbfruits(0);
        model.setNbfruits2(0);
        model.setThreadCounter(0);
        model.setMalusCounter(0);
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                updateScore();
                if (model.isGameInProgress()) {
                    if(model.getDuo()){
                        view.updateDuo();
                    }
                    else{
                        view.updateSolo();
                    }
                } else if (model.isGameOver()) {
                    animationTimer.stop();
                    try {
                        showEndGameAlert();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    model.setEnd2(false);
                    model.setEnd1(false);
                    model.getGrid().reset();
                    model.setSnake1(new Snake(Model.getWindowWidth(), Model.getWindowHeight(), Model.getGridBlockSize()));
                    model.getSnake1().setHeadLocation(Model.getGridBlockSize(), Model.getGridBlockSize());
                    model.getSnake1().setCurrentDirection(Direction.RIGHT);
                    model.getSnake1().setNextDirection(Direction.RIGHT);
                    if(model.getDuo()){
                        model.setSnake2(new Snake(Model.getWindowWidth(), Model.getWindowHeight(), Model.getGridBlockSize()));
                        model.getSnake2().setHeadLocation(Model.getWindowWidth()-Model.getGridBlockSize(), Model.getWindowHeight()-Model.getGridBlockSize());
                        model.getSnake2().setCurrentDirection(Direction.LEFT);
                        model.getSnake2().setNextDirection(Direction.LEFT);
                    }
                }
            }
        };

        model.setGameInProgress(true);
        model.setGameOver(false);

        animationTimer.start();

        task = createTimerTask();
        timer = new Timer("Timer");
        timer.scheduleAtFixedRate(task, Model.getTaskUpdateDelayMs(), Model.getTaskUpdatePeriodMs());
    }

    public void updateScore(){
        if (model.getDuo()){
            if (model.getSnake2()==null){
                if (model.getNbfruits()*10-model.getSnake1().getTail().size()>0){
                    score.setText(Integer.toString(model.getNbfruits()*10-model.getSnake1().getTail().size()));
                }else{
                    score.setText("0");
                }
                if (model.getNbfruits2()*10-model.getSnake2().getTail().size()>0){
                    score2.setText(Integer.toString(model.getNbfruits2()*10-model.getSnake2().getTail().size()));
                }else{
                    score2.setText("0");
                }
             }else if (model.getDuo()){
                score.setText(Integer.toString(model.getNbfruits()*10));
                score2.setText(Integer.toString(model.getNbfruits2()*10));
            }


        }else if (model.getSolo()){
            score.setText(Integer.toString(model.getSnake1().getTail().size()));
        }else{
            score.setText(Integer.toString(model.getSnake1().getTail().size()));
            score2.setText(Integer.toString(model.getSnake1().getTail().size()));

        }


    }
    public void setLabelScore(Label labelScore, Label labelScore2){
        score = labelScore;
        score2 = labelScore2;
    }
    @Override
    public void handle(Event event) {
        if (event.getSource().equals(view.getButtonBack())) {
            model.restart();
            animationTimer.start();
        }

        if (event instanceof KeyEvent) {
            KeyEvent e = (KeyEvent)event;
            if (e.getCode() == KeyCode.Z) {
                model.setSnakeDirection(model.getSnake1(),Direction.UP);
            }
            else if (e.getCode() == KeyCode.S) {
                model.setSnakeDirection(model.getSnake1(),Direction.DOWN);
            }
            else if (e.getCode() == KeyCode.Q) {
                model.setSnakeDirection(model.getSnake1(),Direction.LEFT);
            }
            else if (e.getCode() == KeyCode.D) {
                model.setSnakeDirection(model.getSnake1(),Direction.RIGHT);
            }
            else if (e.getCode() == KeyCode.P) {
                pause();
            }
            if(model.getDuo()){
                if (e.getCode() == KeyCode.UP) {
                    model.setSnakeDirection(model.getSnake2(),Direction.UP);
                }
                else if (e.getCode() == KeyCode.DOWN) {
                    model.setSnakeDirection(model.getSnake2(),Direction.DOWN);
                }
                else if (e.getCode() == KeyCode.LEFT) {
                    model.setSnakeDirection(model.getSnake2(),Direction.LEFT);
                }
                else if (e.getCode() == KeyCode.RIGHT) {
                    model.setSnakeDirection(model.getSnake2(),Direction.RIGHT);
                }
            }
        }
    }

    public void pause() {
        if (model.isPaused()) {
            task = createTimerTask();
            timer = new Timer("Timer");
            timer.scheduleAtFixedRate(task, Model.getTaskUpdateDelayMs(), Model.getTaskUpdatePeriodMs());
            model.setPaused(false);
            pause.setText("Pause");
        }
        else {
            timer.cancel();
            timer = null;
            model.setPaused(true);
            pause.setText("Reprendre");
        }
    }

    private TimerTask createTimerTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                Boolean finished = false;

                model.checkThreadCounter();
                if(model.getDuo()){
                    model.update1();
                    model.update2();
                }
                else{
                    model.update1();
                }

                model.updateThreadCounter();

                if(!model.getIsMalusSurGrille()){
                    model.setMalusCounter(model.getMalusCounter()+1);
                }
                if(model.getMalusEnCours() && model.getMalusCounter() == 0){
                    view.getCanvas().setEffect(model.getBoxBlur());
                }
                if(model.getMalusEnCours() && model.getMalusCounter()%750 == 0){
                    model.resetMalus();
                    view.getCanvas().setEffect(model.getBoxBlur());
                    model.setMalusEnCours(false);
                } else if(!model.getMalusEnCours() && model.getMalusCounter()%1500 == 0){
                    model.getGrid().addMalus(model.getSnake1(), model.getSnake2());
                    model.setIsMalusSurGrille(true);
                    model.setMalusCounter(0);
                }
                System.out.println(model.getEnd1()+" "+model.getEnd2());

                if(model.getSnake1().getHeadLocation().equals(model.getSnake2().getHeadLocation())){
                    System.out.println("tie");
                    endGame("Tie");
                    MessageFin = "Aucun gagnant !";
                    finished = true;
                }

                if(model.getEnd1() && !finished){
                    endGame("Snake 1 perd");
                    MessageFin = "Le joueur 1 a perdu";
                }
                if(model.getEnd2() && !finished){
                    System.out.println("test");
                    endGame("Snake 2 perd");
                    MessageFin = "Le joueur 2 a perdu";
                }

            }
        };
        return task;
    }
    public void menu() {
        model.resetMalus();
        view.getCanvas().setEffect(model.getBoxBlur());
        model.setMalusEnCours(false);
        model.setGameInProgress(false);
        model.setGameOver(true);
        model.setPaused(false);
        clipMusiqueFond.stop();
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        FxmlViewManager.loadInStage("MainMenu",model, primaryStage);
        clipMusiqueFond.stop();
        clipMusiqueFond.setFramePosition(0);
    }
    public void endGame(String reason) {
        writeScores(Integer.parseInt(score.getText()));
        if (model.getSnake2()!=null && model.getDuo()){
            writeScores(Integer.parseInt(score2.getText()));
        }
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        model.resetMalus();
        view.getCanvas().setEffect(model.getBoxBlur());
        model.setMalusEnCours(false);
        model.setGameInProgress(false);
        model.setGameOver(true);
        model.setPaused(false);
        System.out.println("Game over: " + reason);
        clipMusiqueFond.stop();
    }

    public void popup() throws IOException {

    }

    private void showEndGameAlert() throws IOException {
        start.setText("Start !");

        //String gameOverText = "Game Over! Score: ";
        //double textWidth = getTextWidth(gameOverText);

       // view.getGraphicsContext().setFill(Color.BLACK);
        //view.getGraphicsContext().fillText(
        //        gameOverText,
        //        (float)Model.getWindowWidth() / 2 - (textWidth / 2),
        //        Model.getWindowHeight() / 2 - 24
        //);


        String gameOverText = MessageFin;
        double textWidth = getTextWidth(gameOverText);

        view.getGraphicsContext().setFill(Color.WHITE);
        view.getGraphicsContext().fillText(
                gameOverText,
                (float)Model.getWindowWidth() / 2 - (textWidth / 2),
                Model.getWindowHeight() / 2 - 24
        );
    }

    private double getTextWidth(String string) {
        Text text = new Text(string);
        new Scene(new Group(text));
        text.applyCss();
        return text.getLayoutBounds().getWidth();
    }
    public void writeScores(int score) {
        int[] tblscores = new int[3];
        File fichier = new File(System.getProperty("user.dir")+"/src/app/ressources/Score.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(fichier));
            String line;
            int z=0;
            while ((line = br.readLine()) != null) {
                tblscores[z] = Integer.parseInt(line);
                z++;
            }
            System.out.print("avant");
            System.out.println(tblscores[0]+tblscores[1]+tblscores[2]);
            System.out.print("après");
            br.close();
        }catch (FileNotFoundException fne){
            System.out.println("fichier non trouvé");
        }catch (IOException ioexp){
            System.out.println("io exception");
        }
        for (int i = 0; i <  tblscores.length; i++) {
            if (score>tblscores[i]){
                switch (i){
                    case 0:
                        tblscores[i+2]=tblscores[i+1];
                        tblscores[i+1]=tblscores[i];
                        break;
                    case 1:
                        tblscores[i+1]=tblscores[i];
                        break;
                }
                tblscores[i] = score;
                break;
            }
        }
        try {
            String scores = tblscores[0] + "\n" + tblscores[1] + "\n" + tblscores[2];
            byte[] strToBytes = scores.getBytes();
            Path path = Paths.get(System.getProperty("user.dir")+"/src/app/ressources/Score.txt");
            Files.write(path, strToBytes);
        }catch(FileNotFoundException fne){
            System.out.println("fichier non trouvé");
        }catch (IOException ioexcep){
            System.out.println(ioexcep);
        }
    }
}

