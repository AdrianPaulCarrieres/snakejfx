package app.model;


import app.controller.GameController;
import javafx.scene.effect.BoxBlur;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.io.File;
import java.io.IOException;;
import java.util.ArrayList;

public class Model {
	public GameController gameController;

	private Dimension screenSize;

	private static long TASK_UPDATE_PERIOD_MS;
	private static long TASK_UPDATE_DELAY_MS;

	private static int WINDOW_HEIGHT;
	private static int WINDOW_WIDTH;
	private static int GRID_BLOCK_SIZE;

	private Grid grid;

	private int nbSnake;
	private int appearSpeed = 10;
	private int threadCounter;

	private boolean isGameInProgress = false;
	private boolean isGameOver = false;
	private boolean isPaused = false;

	private ArrayList<Snake> listSnake;

	private Snake snake1;
	private Snake snake2;
	private int nbfruits = 0;
	private int nbfruits2 = 0;

	private boolean isSolo;
	private Boolean isDuo;
	private Boolean isIA1;
	private Boolean isIA2;
	private Boolean reverse;
	private Boolean end1;
	private Boolean end2;
	private Boolean tie;

	public File mangerFruit = new File(System.getProperty("user.dir")+"/src/app/ressources/sons/MangerFruit.wav");
	public Clip clipMangerFruit;

	public File mort = new File(System.getProperty("user.dir")+"/src/app/ressources/sons/Mort.wav");
	public Clip clipMort;

	public File gameOver = new File(System.getProperty("user.dir")+"/src/app/ressources/sons/GameOver.wav");
	public Clip clipGameOver;

	public File tp = new File(System.getProperty("user.dir")+"/src/app/ressources/sons/Tp.wav");
	public Clip clipTp;
	public FloatControl volumeTp;

	public File redirection = new File(System.getProperty("user.dir")+"/src/app/ressources/sons/Redirection.wav");
	public Clip clipRedirection;

	private int malusCounter = 0;
	private boolean isMalusSurGrille = false;
	private boolean malusEnCours = false;
	private BoxBlur boxblur = new BoxBlur();

	public Model(){
	}

	public void initGame(long TaskUpdatePeriodeMS, long TaskUpdateDelay, int nbSnake, Boolean reverse){
		TASK_UPDATE_DELAY_MS = TaskUpdateDelay;
		TASK_UPDATE_PERIOD_MS = TaskUpdatePeriodeMS;

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		WINDOW_HEIGHT = height;
//		WINDOW_WIDTH = width;
//		GRID_BLOCK_SIZE = blockSize;
		end1 = false;
		end2 = false;
		tie = false;

		if (screenSize.getWidth()<1500){
			WINDOW_WIDTH = 900;
			WINDOW_HEIGHT = 600;
			GRID_BLOCK_SIZE = 30;
		}
		else if (screenSize.getWidth()>1500 && screenSize.getWidth()<1900){
			WINDOW_WIDTH = 1200;
			WINDOW_HEIGHT = 800;
			GRID_BLOCK_SIZE = 40;
		}
		else if (screenSize.getWidth()>1900){
			WINDOW_WIDTH = 1500;
			WINDOW_HEIGHT = 1000;
			GRID_BLOCK_SIZE = 50;
		}

		this.nbSnake = nbSnake;
		this.reverse = reverse;

		grid = new Grid(WINDOW_WIDTH,WINDOW_HEIGHT,GRID_BLOCK_SIZE);
		isSolo = (nbSnake == 1);
		isDuo = (nbSnake == 2 || nbSnake == 3 || nbSnake == 4);
		isIA2 = (nbSnake == 3 || nbSnake == 4);
		isIA1 = (nbSnake == 4);

		listSnake=new ArrayList<>();

		snake1 = new Snake(WINDOW_WIDTH,WINDOW_HEIGHT,GRID_BLOCK_SIZE);
		snake1.setHeadLocation(GRID_BLOCK_SIZE,GRID_BLOCK_SIZE);
		snake1.setNextDirection(Direction.RIGHT);
		snake1.setCurrentDirection(Direction.RIGHT);;
		listSnake.add(0,snake1);

		if(isDuo){
			snake2 = new Snake(WINDOW_WIDTH,WINDOW_HEIGHT,GRID_BLOCK_SIZE);
			snake2.setHeadLocation(WINDOW_WIDTH-GRID_BLOCK_SIZE, WINDOW_HEIGHT-GRID_BLOCK_SIZE);
			snake2.setNextDirection(Direction.LEFT);
			snake2.setCurrentDirection(Direction.LEFT);

			listSnake.add(1,snake2);
		}
		if(listSnake.size()>2) {
			listSnake.subList(2, listSnake.size()).clear();
		}
		grid.genRedirection(listSnake);

		threadCounter = 0;

		resetMalus();
	}

	public void restart(){
		initGame(TASK_UPDATE_DELAY_MS,TASK_UPDATE_PERIOD_MS,nbSnake,reverse);
	}

	public void setSnakeDirection(Snake snake, Direction dir){
		switch(dir){
			case UP:
				if(snake.getCurrentDirection()!=Direction.DOWN){
					snake.setPastDirection(snake.getCurrentDirection());
					snake.setNextDirection(dir);
				}
				break;
			case DOWN:
				if(snake.getCurrentDirection()!=Direction.UP){
					snake.setPastDirection(snake.getCurrentDirection());
					snake.setNextDirection(dir);
				}
				break;
			case LEFT:
				if(snake.getCurrentDirection()!=Direction.RIGHT){
					snake.setPastDirection(snake.getCurrentDirection());
					snake.setNextDirection(dir);
				}
				break;
			case RIGHT:
				if(snake.getCurrentDirection()!=Direction.LEFT){
					snake.setPastDirection(snake.getCurrentDirection());
					snake.setNextDirection(dir);
				}
				break;
		}
	}

	public Boolean checkThreadCounter(){
		if(threadCounter%1000 == 0){
			threadCounter = 0;
			return true;
		}
		return false;
	}

	public void updateThreadCounter(){
		threadCounter++;
	}

	public void popup() throws IOException {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource(System.getProperty("user.dir")+"/src/app/view/fxmlView/GameOverSolo.fxml"));
		//loader.setController(gameController);
        //Parent root = loader.load();
        //Scene scene = new Scene(root, 250, 150);
        //Stage stage = new Stage();
		//stage.initModality(Modality.APPLICATION_MODAL);
		//stage.initStyle(StageStyle.UNDECORATED);
        //stage.setScene(scene);
        //stage.show();
	}

	public void update1() {

		if (isGameInProgress()) {

			if(reverse) {
				if (checkThreadCounter()) {

					if (snake1.getAppearSpeed() > 2) {
						snake1.setAppearSpeed(snake1.getAppearSpeed() - 1);
					}
				}

				if (threadCounter % snake1.getAppearSpeed() == 0) {
					snake1.addPointToPrint(1);
				}
			}

			if (threadCounter % 10 == 0) {
				if(isIA1){
					takeIADecision(snake1);
				}
				else{
					snake1.setCurrentDirection(snake1.getNextDirection());
				}

			}
			snake1.snakeUpdate();

			if (grid.itemCheck(snake1) == 1 && grid.foundFood(snake1)) {
				snake1.setFocusingItem(false);
				if(reverse){
					snake1.addPointToDel(6);
				}
				else{
					snake1.addPointToPrint(10);
				}

				nbfruits++;
				clipMangerFruit.stop();
				clipMangerFruit.setFramePosition(0);
				clipMangerFruit.start();
			}
			if (grid.itemCheck(snake1) == 2 && grid.foundFood(snake1)) {
				snake1.setFocusingItem(false);
				if(reverse) {
					snake1.addPointToDel(18);
				}
				else{
					snake1.addPointToPrint(50);
				}
				nbfruits++;
				clipMangerFruit.stop();
				clipMangerFruit.setFramePosition(0);
				clipMangerFruit.start();
			}
			if (grid.foundTP(snake1)) {
				snake1.setFocusingItem(false);
				clipTp.stop();
				clipTp.setFramePosition(0);
				clipTp.start();
			}
			grid.foundRedirection(snake1, listSnake);

			if(grid.foundRedirection(snake1, listSnake)) {
				snake1.setFocusingItem(false);
				clipRedirection.stop();
				clipRedirection.setFramePosition(0);
				clipRedirection.start();
			}

			foundMalus(snake1);

			if (isDuo) {
				if (checkColisionWithSnake2()) {
					end1 = true;

				}
			}
			if (checkColisionSolo1()) {
				end1 = true;
			}
			if (end1) {
				clipMort.stop();
				clipMort.start();

				clipGameOver.stop();
				clipGameOver.setFramePosition(0);
				clipGameOver.start();

				try {
					popup();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		System.out.println("snake1 end");
	}

	public void update2(){
		if (isGameInProgress()) {

			if(reverse) {
				if (checkThreadCounter()) {
					if (snake2.getAppearSpeed() > 1) {
						snake2.setAppearSpeed(snake2.getAppearSpeed() - 1);
					}
				}

				if (threadCounter % snake2.getAppearSpeed() == 0) {
					snake2.addPointToPrint(1);
				}
			}
			if (threadCounter % 10 == 0) {
				if(isIA2){
					takeIADecision(snake2);
				}
				else{
					snake2.setCurrentDirection(snake2.getNextDirection());
				}

			}
//				model.getSnake1().setCurrentDirection(model.getCurrentSnakeDirection());
			snake2.snakeUpdate();
			if(grid.itemCheck(snake2) == 1 && grid.foundFood(snake2) ){
				snake2.setFocusingItem(false);
				if(reverse) {
					snake2.addPointToDel(6);
				}
				else{
					snake2.addPointToPrint(10);
				}
				nbfruits2++;
				clipMangerFruit.stop();
				clipMangerFruit.setFramePosition(0);
				clipMangerFruit.start();
			}
            if(grid.itemCheck(snake2) == 2 && grid.foundFood(snake2)){
                nbfruits2++;
                snake2.setFocusingItem(false);
                if(reverse) {
					snake2.addPointToDel(18);
				}
                else{
					snake2.addPointToPrint(50);
				}
                clipMangerFruit.stop();
                clipMangerFruit.setFramePosition(0);
                clipMangerFruit.start();
            }

			if(grid.foundTP(snake2)){
				snake2.setFocusingItem(false);
				clipTp.stop();
				clipTp.setFramePosition(0);
				clipTp.start();
			}

			grid.foundRedirection(snake2, listSnake);

			if(grid.foundRedirection(snake2, listSnake)) {
				System.out.println("tp");
				snake2.setFocusingItem(false);
				clipRedirection.stop();
				clipRedirection.setFramePosition(0);
				clipRedirection.start();
			}

			if(isDuo) {
				if (checkColisionWithSnake1()) {
					end2 = true;
				}
			}
			if(checkColisionSolo2()){
				end2 = true;
			}
			if(end2){
				clipMort.stop();
				clipMort.start();

				clipGameOver.stop();
				clipGameOver.setFramePosition(0);
				clipGameOver.start();
			}

			foundMalus(snake2);

		}
		System.out.println("snake2 end");
	}

	public void foundMalus(Snake snake) {
		for (int i = 0; i < grid.getListItem().size(); i++) {
			if (snake.getHeadLocation().equals(grid.getListItem().get(i).getLocation()) && grid.getListItem().get(i).getType()==3) {
				setMalus();
				setMalusEnCours(true);
				setIsMalusSurGrille(false);
				grid.virerItem(snake.getHeadLocation(), grid.getListItem().get(i).getType());
			}
		}
	}


	public Boolean isObstacleUp1(Snake snake,int i, int portee){
		if((snake.getHeadLocation().getY()==(((snake1.getTail().get(i).getY()+portee*GRID_BLOCK_SIZE)%WINDOW_HEIGHT)+WINDOW_HEIGHT)%WINDOW_HEIGHT && snake.getHeadLocation().getX()==snake1.getTail().get(i).getX())
		|| (snake.getHeadLocation().getY() ==(snake1.getHeadLocation().getY()+(portee+1)*GRID_BLOCK_SIZE) && snake.getHeadLocation().getX()==snake1.getHeadLocation().getX())) {
			System.out.println("Obstacle UP 1");
			return true;
		}
		return false;
	}

	public Boolean isObstacleDown1(Snake snake,int i, int portee){
		if((snake.getHeadLocation().getY()==(snake1.getTail().get(i).getY()-portee*GRID_BLOCK_SIZE)%WINDOW_HEIGHT && snake.getHeadLocation().getX()==snake1.getTail().get(i).getX())
				|| (snake.getHeadLocation().getY() == (snake1.getHeadLocation().getY()-(portee+1)*GRID_BLOCK_SIZE) && snake.getHeadLocation().getX()==snake1.getHeadLocation().getX())) {
			System.out.println("Obstacle DOWN 1");
			return true;
		}
		return false;
	}

	public Boolean isObstacleRight1(Snake snake,int i,int portee){
		if((snake.getHeadLocation().getX()==(snake1.getTail().get(i).getX()-portee*GRID_BLOCK_SIZE)%WINDOW_WIDTH && snake.getHeadLocation().getY()==snake1.getTail().get(i).getY())
				|| (snake.getHeadLocation().getX() == (snake1.getHeadLocation().getX()-(portee+1)*GRID_BLOCK_SIZE) && snake.getHeadLocation().getY()==snake1.getHeadLocation().getY())){
			System.out.println("Obstacle RIGHT 1");
			return true;
		}
		return false;
	}

	public Boolean isObstacleLeft1(Snake snake,int i,int portee){
		if((snake.getHeadLocation().getX()==(((snake1.getTail().get(i).getX()+portee*GRID_BLOCK_SIZE)%WINDOW_WIDTH)+WINDOW_WIDTH)%WINDOW_WIDTH && snake.getHeadLocation().getY()==snake1.getTail().get(i).getY())
				|| (snake.getHeadLocation().getX() == (snake1.getHeadLocation().getX()+(portee+1)*GRID_BLOCK_SIZE) && snake.getHeadLocation().getY()==snake1.getHeadLocation().getY())){
			System.out.println("Obstacle LEFT 1");
			return true;
		}
		return false;
	}
	public Boolean isObstacleUp2(Snake snake,int i, int portee){
		if((snake.getHeadLocation().getY()==(((snake2.getTail().get(i).getY()+portee*GRID_BLOCK_SIZE)%WINDOW_HEIGHT)+WINDOW_HEIGHT)%WINDOW_HEIGHT && snake.getHeadLocation().getX()==snake2.getTail().get(i).getX())
				|| (snake.getHeadLocation().getY() == (snake2.getHeadLocation().getY()+(portee+1)*GRID_BLOCK_SIZE) && snake.getHeadLocation().getX()==snake2.getHeadLocation().getX())){
			System.out.println("Obstacle UP 2");
			return true;
		}
		return false;
	}

	public Boolean isObstacleDown2(Snake snake,int i,int portee){
		if((snake.getHeadLocation().getY()==(snake2.getTail().get(i).getY()-portee*GRID_BLOCK_SIZE) && snake.getHeadLocation().getX()==snake2.getTail().get(i).getX())
			|| (snake.getHeadLocation().getY() == (snake2.getHeadLocation().getY()-(portee+1)*GRID_BLOCK_SIZE) && snake.getHeadLocation().getX()==snake2.getHeadLocation().getX())){
			System.out.println("Obstacle DOWN 2");
			return true;
		}
		return false;
	}

	public Boolean isObstacleRight2(Snake snake,int i,int portee){
		if((snake.getHeadLocation().getX()==(snake2.getTail().get(i).getX()-portee*GRID_BLOCK_SIZE) && snake.getHeadLocation().getY()==snake2.getTail().get(i).getY())
				|| (snake.getHeadLocation().getX() == snake2.getHeadLocation().getX()-(portee+1)*GRID_BLOCK_SIZE && snake.getHeadLocation().getY()==snake2.getHeadLocation().getY())){
			System.out.println("Obstacle RIGHT 2");
			return true;
		}
		return false;
	}
	public Boolean isObstacleLeft2(Snake snake,int i,int portee){
		if((snake.getHeadLocation().getX()==(((snake2.getTail().get(i).getX()+portee*GRID_BLOCK_SIZE)%WINDOW_WIDTH)+WINDOW_WIDTH)%WINDOW_WIDTH && snake.getHeadLocation().getY()==snake2.getTail().get(i).getY())
				|| (snake.getHeadLocation().getX() == (snake2.getHeadLocation().getX()+(portee+1)*GRID_BLOCK_SIZE) && snake.getHeadLocation().getY()==snake2.getHeadLocation().getY())){
			System.out.println("Obstacle LEFT 2");
			return true;
		}
		return false;
	}

	////////////////////////////////////////////////////////////////////////////////////////


	public void takeIADecision(Snake snake){
		snake.setDecisionDirection(snake.getCurrentDirection());

		if (snake.getFocusingItem() && !isFoodExisting(snake.getFocusedItem())) {
			snake.setFocusingItem(false);
		}
		if(!snake.getFocusingItem()){
			focusItemIA(snake);
		}
		System.out.println(snake.getDecisionDirection());
		esquiveIA(snake);
		System.out.println(snake.getDecisionDirection());

		snake.setCurrentDirection(snake.getDecisionDirection());
	}

	public void focusItemIA(Snake snake){
		Boolean test = false;
		if((snake.getDecisionDirection()== Direction.LEFT || snake.getDecisionDirection()== Direction.RIGHT )){
			for (int i = 0; i < grid.getListItem().size(); i++) {
				if(snake.getHeadLocation().getX()==grid.getListItem().get(i).getLocation().getX()){
					snake.setFocusingItem(true);
					System.out.println("focus");
					snake.setFocusedItem(grid.getListItem().get(i));

					if(snake.getHeadLocation().getY()-grid.getListItem().get(i).getLocation().getY()< 0) {
						for (int j = 0; j < snake1.getTail().size() ; j++) {
							if((snake.getHeadLocation().getY()==(((snake1.getTail().get(j).getY()-GRID_BLOCK_SIZE)%WINDOW_HEIGHT)+WINDOW_HEIGHT)%WINDOW_HEIGHT && snake.getHeadLocation().getX()==snake1.getTail().get(j).getX())
									|| (snake.getHeadLocation().getY() ==(snake1.getHeadLocation().getY()-GRID_BLOCK_SIZE) && snake.getHeadLocation().getX()==snake1.getHeadLocation().getX())
									|| (snake.getHeadLocation().getY() ==(snake1.getHeadLocation().getY()-2*GRID_BLOCK_SIZE) && snake.getHeadLocation().getX()==snake1.getHeadLocation().getX())
									|| (snake.getHeadLocation().getY()==(((snake1.getTail().get(j).getY()-2*GRID_BLOCK_SIZE)%WINDOW_HEIGHT)+WINDOW_HEIGHT)%WINDOW_HEIGHT && snake.getHeadLocation().getX()==snake1.getTail().get(j).getX())) {
								snake.setDecisionDirection(snake.getCurrentDirection());
								snake.setFocusingItem(false);
								System.out.println("peut pas tourner");
								test = true;
								break;
							}
						}
						if(!test) {
							for (int j = 0; j < snake2.getTail().size(); j++) {
								if ((snake.getHeadLocation().getY() == (((snake2.getTail().get(j).getY() - GRID_BLOCK_SIZE) % WINDOW_HEIGHT) + WINDOW_HEIGHT) % WINDOW_HEIGHT && snake.getHeadLocation().getX() == snake2.getTail().get(j).getX())
										|| (snake.getHeadLocation().getY() == (snake2.getHeadLocation().getY() - GRID_BLOCK_SIZE) && snake.getHeadLocation().getX() == snake2.getHeadLocation().getX())
										|| (snake.getHeadLocation().getY() == (snake2.getHeadLocation().getY() - 2 * GRID_BLOCK_SIZE) && snake.getHeadLocation().getX() == snake2.getHeadLocation().getX())
										|| (snake.getHeadLocation().getY() == (((snake2.getTail().get(j).getY() - 2 * GRID_BLOCK_SIZE) % WINDOW_HEIGHT) + WINDOW_HEIGHT) % WINDOW_HEIGHT && snake.getHeadLocation().getX() == snake2.getTail().get(j).getX())) {
									snake.setDecisionDirection(snake.getCurrentDirection());
									snake.setFocusingItem(false);
									System.out.println("peut pas tourner");
									test = true;
									break;
								}
							}
						}
						if(!test) {
							snake.setDecisionDirection(Direction.DOWN);

							break;
						}
					}

					else{
						for (int j = 0; j < snake1.getTail().size() ; j++) {
							if((snake.getHeadLocation().getY()==(((snake1.getTail().get(j).getY()+GRID_BLOCK_SIZE)%WINDOW_HEIGHT)+WINDOW_HEIGHT)%WINDOW_HEIGHT && snake.getHeadLocation().getX()==snake1.getTail().get(j).getX())
									|| (snake.getHeadLocation().getY() ==(snake1.getHeadLocation().getY()+GRID_BLOCK_SIZE) && snake.getHeadLocation().getX()==snake1.getHeadLocation().getX())
									|| (snake.getHeadLocation().getY() ==(snake1.getHeadLocation().getY()+2*GRID_BLOCK_SIZE) && snake.getHeadLocation().getX()==snake1.getHeadLocation().getX())
									|| (snake.getHeadLocation().getY()==(((snake1.getTail().get(j).getY()+2*GRID_BLOCK_SIZE)%WINDOW_HEIGHT)+WINDOW_HEIGHT)%WINDOW_HEIGHT && snake.getHeadLocation().getX()==snake1.getTail().get(j).getX())) {
								snake.setDecisionDirection(snake.getCurrentDirection());
								snake.setFocusingItem(false);
								System.out.println("peut pas tourner");
								test = true;
								break;
							}
						}
						if(!test) {
							for (int j = 0; j < snake2.getTail().size(); j++) {
								if ((snake.getHeadLocation().getY() == (((snake2.getTail().get(j).getY() + GRID_BLOCK_SIZE) % WINDOW_HEIGHT) + WINDOW_HEIGHT) % WINDOW_HEIGHT && snake.getHeadLocation().getX() == snake2.getTail().get(j).getX())
										|| (snake.getHeadLocation().getY() == (snake2.getHeadLocation().getY() + GRID_BLOCK_SIZE) && snake.getHeadLocation().getX() == snake2.getHeadLocation().getX())
										|| (snake.getHeadLocation().getY() == (snake2.getHeadLocation().getY() + 2 * GRID_BLOCK_SIZE) && snake.getHeadLocation().getX() == snake2.getHeadLocation().getX())
										|| (snake.getHeadLocation().getY() == (((snake2.getTail().get(j).getY() + 2 * GRID_BLOCK_SIZE) % WINDOW_HEIGHT) + WINDOW_HEIGHT) % WINDOW_HEIGHT && snake.getHeadLocation().getX() == snake2.getTail().get(j).getX())) {
									snake.setDecisionDirection(snake.getCurrentDirection());
									snake.setFocusingItem(false);
									System.out.println("peut pas tourner");
									test = true;
									break;
								}
							}
						}if(!test) {
							snake.setDecisionDirection(Direction.UP);
							break;
						}
					}

				}
			}
		}
		else if((snake.getDecisionDirection()== Direction.UP || snake.getDecisionDirection()== Direction.DOWN) && !snake.getFocusingItem()){
			for (int i = 0; i < grid.getListItem().size(); i++) {
				if(snake.getHeadLocation().getY()==grid.getListItem().get(i).getLocation().getY()){
					snake.setFocusingItem(true);
					System.out.println("focus");
					snake.setFocusedItem(grid.getListItem().get(i));
					if(snake.getHeadLocation().getX()-grid.getListItem().get(i).getLocation().getX()< 0) {
						for (int j = 0; j < snake1.getTail().size() ; j++) {
							if((snake.getHeadLocation().getX()==(((snake1.getTail().get(j).getX()-GRID_BLOCK_SIZE)%WINDOW_HEIGHT)+WINDOW_HEIGHT)%WINDOW_HEIGHT && snake.getHeadLocation().getY()==snake1.getTail().get(j).getY())
									|| (snake.getHeadLocation().getX() ==(snake1.getHeadLocation().getX()-GRID_BLOCK_SIZE) && snake.getHeadLocation().getY()==snake1.getHeadLocation().getY())
									|| (snake.getHeadLocation().getX() ==(snake1.getHeadLocation().getX()-2*GRID_BLOCK_SIZE) && snake.getHeadLocation().getY()==snake1.getHeadLocation().getY())
									|| (snake.getHeadLocation().getX()==(((snake1.getTail().get(j).getX()-2*GRID_BLOCK_SIZE)%WINDOW_HEIGHT)+WINDOW_HEIGHT)%WINDOW_HEIGHT && snake.getHeadLocation().getY()==snake1.getTail().get(j).getY())) {
								snake.setDecisionDirection(snake.getCurrentDirection());
								snake.setFocusingItem(false);
								System.out.println("peut pas tourner");
								test = true;
								break;
							}
						}
						if(!test) {
							for (int j = 0; j < snake2.getTail().size(); j++) {
								if ((snake.getHeadLocation().getX() == (((snake2.getTail().get(j).getX() - GRID_BLOCK_SIZE) % WINDOW_HEIGHT) + WINDOW_HEIGHT) % WINDOW_HEIGHT && snake.getHeadLocation().getY() == snake2.getTail().get(j).getY())
										|| (snake.getHeadLocation().getX() == (snake2.getHeadLocation().getX() - GRID_BLOCK_SIZE) && snake.getHeadLocation().getY() == snake2.getHeadLocation().getY())
										|| (snake.getHeadLocation().getX() == (snake2.getHeadLocation().getX() - 2 * GRID_BLOCK_SIZE) && snake.getHeadLocation().getY() == snake2.getHeadLocation().getY())
										|| (snake.getHeadLocation().getX() == (((snake2.getTail().get(j).getX() - 2 * GRID_BLOCK_SIZE) % WINDOW_HEIGHT) + WINDOW_HEIGHT) % WINDOW_HEIGHT && snake.getHeadLocation().getY() == snake2.getTail().get(j).getY())) {
									snake.setDecisionDirection(snake.getCurrentDirection());
									snake.setFocusingItem(false);
									System.out.println("peut pas tourner");
									test = true;
									break;
								}
							}
						}
						if(!test){
							snake.setDecisionDirection(Direction.RIGHT);
							break;
						}
					}
					else{
						for (int j = 0; j < snake1.getTail().size() ; j++) {
							if((snake.getHeadLocation().getX()==(((snake1.getTail().get(j).getX()+GRID_BLOCK_SIZE)%WINDOW_HEIGHT)+WINDOW_HEIGHT)%WINDOW_HEIGHT && snake.getHeadLocation().getY()==snake1.getTail().get(j).getY())
									|| (snake.getHeadLocation().getX() ==(snake1.getHeadLocation().getX()+GRID_BLOCK_SIZE) && snake.getHeadLocation().getY()==snake1.getHeadLocation().getY())
									|| (snake.getHeadLocation().getX() ==(snake1.getHeadLocation().getX()+2*GRID_BLOCK_SIZE) && snake.getHeadLocation().getY()==snake1.getHeadLocation().getY())
									|| (snake.getHeadLocation().getX()==(((snake1.getTail().get(j).getX()+2*GRID_BLOCK_SIZE)%WINDOW_HEIGHT)+WINDOW_HEIGHT)%WINDOW_HEIGHT && snake.getHeadLocation().getY()==snake1.getTail().get(j).getY())) {
								snake.setDecisionDirection(snake.getCurrentDirection());
								snake.setFocusingItem(false);
								System.out.println("peut pas tourner");
								test = true;
								break;
							}
						}
						if(!test) {
							for (int j = 0; j < snake2.getTail().size(); j++) {
								if ((snake.getHeadLocation().getX() == (((snake2.getTail().get(j).getX() + GRID_BLOCK_SIZE) % WINDOW_HEIGHT) + WINDOW_HEIGHT) % WINDOW_HEIGHT && snake.getHeadLocation().getY() == snake2.getTail().get(j).getY())
										|| (snake.getHeadLocation().getX() == (snake2.getHeadLocation().getX() + GRID_BLOCK_SIZE) && snake.getHeadLocation().getY() == snake2.getHeadLocation().getY())
										|| (snake.getHeadLocation().getX() == (snake2.getHeadLocation().getX() + 2 * GRID_BLOCK_SIZE) && snake.getHeadLocation().getY() == snake2.getHeadLocation().getY())
										|| (snake.getHeadLocation().getX() == (((snake2.getTail().get(j).getX() + 2 * GRID_BLOCK_SIZE) % WINDOW_HEIGHT) + WINDOW_HEIGHT) % WINDOW_HEIGHT && snake.getHeadLocation().getY() == snake2.getTail().get(j).getY())) {
									snake.setDecisionDirection(snake.getCurrentDirection());
									snake.setFocusingItem(false);
									System.out.println("peut pas tourner");
									test = true;
									break;
								}
							}
						}
						if(!test){
							snake.setDecisionDirection(Direction.LEFT);
						}
					}

				}
			}
		}
	}

	public void esquiveIA(Snake snake){
		boolean counterEsquive = false;
		boolean esquive = false;

		switch(snake.getDecisionDirection()){
			case UP:
				for (int i = 0; i < snake1.getTail().size() ; i++) {
					if(isObstacleUp1(snake,i,1)||isObstacleUp1(snake,i,2)) {
						System.out.println("obstacle : "+snake1.getTail().get(i));
						System.out.println("tete snake : "+snake.getHeadLocation());
						snake.setFocusingItem(false);
						esquive = true;

						if(snake.getFocusingItem()){
							snake.setDecisionDirection(snake.getCurrentDirection());
						}
						for (int j = 0; j < snake1.getTail().size() ; j++) {

							if (isObstacleRight1(snake, j, 1) || isObstacleRight1(snake, j, 2) || isObstacleRight1(snake, j, 3) || isObstacleRight1(snake, j, 4) || isObstacleRight1(snake, j, 5)) {
								snake.setDecisionDirection(Direction.LEFT);
								counterEsquive = true;
								break;

							} else if (isObstacleLeft1(snake, j, 1) || isObstacleLeft1(snake, j, 2) || isObstacleLeft1(snake, j, 3) || isObstacleLeft1(snake, j, 4)|| isObstacleLeft1(snake, j, 4)) {
								snake.setDecisionDirection(Direction.RIGHT);
								counterEsquive = true;
								break;
							}
						}
						if(counterEsquive) {
							break;
						}
						else {
							snake.setDecisionDirection(Direction.RIGHT);
							System.out.println("par defaut");
						}
						System.out.println("esquiveUP1");
						break;
					}
				}

				if(esquive){break;}

				for (int i = 0; i < snake2.getTail().size(); i++) {
					if(isObstacleUp2(snake,i,1)||isObstacleUp2(snake,i,2)) {
						System.out.println("obstacle : "+snake2.getTail().get(i));
						System.out.println("tete snake : "+snake.getHeadLocation());
						snake.setFocusingItem(false);

						if(snake.getFocusingItem()){
							snake.setDecisionDirection(snake.getCurrentDirection());
						}

						for (int j = 0; j < snake2.getTail().size() ; j++) {
							if (isObstacleRight2(snake, j, 1) || isObstacleRight2(snake, j, 2) || isObstacleRight2(snake, j, 3)|| isObstacleRight2(snake, j, 4)|| isObstacleRight2(snake, j, 5)) {
								snake.setDecisionDirection(Direction.LEFT);
								counterEsquive = true;
								break;
							} else if (isObstacleLeft2(snake, j, 1) || isObstacleLeft2(snake, j, 2) || isObstacleLeft2(snake, j, 3) || isObstacleLeft2(snake, j, 4) || isObstacleLeft2(snake, j, 5)) {
								snake.setDecisionDirection(Direction.RIGHT);
								counterEsquive = true;
								break;
							}
						}
						if(counterEsquive) {
							break;
						}
						else {
							 snake.setDecisionDirection(Direction.RIGHT);
							 System.out.println("par defaut");
						}
						System.out.println("esquiveUP2");
						break;
					}
				}
				break;

			case DOWN:
				for (int i = 0; i < snake1.getTail().size() ; i++) {
					if(isObstacleDown1(snake,i,1)||isObstacleDown1(snake,i,2)) {
						System.out.println("obstacle : "+snake1.getTail().get(i));
						System.out.println("tete snake : "+snake.getHeadLocation());
						snake.setFocusingItem(false);
						esquive = true;

						if(snake.getFocusingItem()){
							snake.setDecisionDirection(snake.getCurrentDirection());
						}
						for (int j = 0; j < snake1.getTail().size() ; j++) {
							if (isObstacleRight1(snake, j, 1) || isObstacleRight1(snake, j, 2) || isObstacleRight1(snake, j, 3) || isObstacleRight1(snake, j, 4)|| isObstacleRight1(snake, j, 5)) {
								snake.setDecisionDirection(Direction.LEFT);
								counterEsquive = true;
								break;
							} else if (isObstacleLeft1(snake, j, 1) || isObstacleLeft1(snake, j, 2) || isObstacleLeft1(snake, j, 3)|| isObstacleLeft1(snake, j, 4)|| isObstacleLeft1(snake, j, 5)) {
								snake.setDecisionDirection(Direction.RIGHT);
								counterEsquive = true;
								break;
							}
						}
						if(counterEsquive){
							break;
						}
						else {
							 snake.setDecisionDirection(Direction.LEFT);
							 System.out.println("par defaut");
						}
						System.out.println("esquiveDOWN1");
						break;
					}
				}
				if(esquive){
					break;
				}
				for (int i = 0; i < snake2.getTail().size() ; i++) {

					if(isObstacleDown2(snake,i,1)||isObstacleDown2(snake,i,2)) {
						System.out.println("obstacle : "+snake2.getTail().get(i));
						System.out.println("tete snake : "+snake.getHeadLocation());
						snake.setFocusingItem(false);


						if(snake.getFocusingItem()){
							snake.setDecisionDirection(snake.getCurrentDirection());
						}
						for (int j = 0; j < snake2.getTail().size() ; j++) {
							if (isObstacleRight2(snake, j, 1) || isObstacleRight2(snake, j, 2) || isObstacleRight2(snake, j, 3)|| isObstacleRight2(snake, j, 4)|| isObstacleRight2(snake, j, 5)) {
								snake.setDecisionDirection(Direction.LEFT);
								counterEsquive = true;
								break;
							} else if (isObstacleLeft2(snake, j, 1) || isObstacleLeft2(snake, j, 2) || isObstacleLeft2(snake, j, 3)|| isObstacleLeft2(snake, j, 4)|| isObstacleLeft2(snake, j, 5)) {
								snake.setDecisionDirection(Direction.RIGHT);
								counterEsquive = true;
								break;
							}
						}
						if(counterEsquive){
							break;
						}
						else {
							 snake.setDecisionDirection(Direction.LEFT);
							 System.out.println("par defaut");
						}
						System.out.println("esquiveDOWN2");
						break;
					}
				}
				break;
			case LEFT:
				for (int i = 0; i < snake1.getTail().size() ; i++) {
					if(isObstacleLeft1(snake,i,1)||isObstacleLeft1(snake,i,2)) {
						System.out.println("obstacle 1 : "+snake1.getTail().get(i));
						System.out.println("tete snake : "+snake.getHeadLocation());
						snake.setFocusingItem(false);
						esquive = true;

						if(snake.getFocusingItem()){
							snake.setDecisionDirection(snake.getCurrentDirection());
						}
						for (int j = 0; j < snake1.getTail().size() ; j++) {
							if (isObstacleUp1(snake, j, 1) || isObstacleUp1(snake, j, 2) || isObstacleUp1(snake, j, 3)|| isObstacleUp1(snake, j, 4)|| isObstacleUp1(snake, j, 5)) {
								snake.setDecisionDirection(Direction.DOWN);
								counterEsquive = true;
								break;

							} else if (isObstacleDown1(snake, j, 1) || isObstacleDown1(snake, j, 2) || isObstacleDown1(snake, j, 3)|| isObstacleDown1(snake, j, 4)|| isObstacleDown1(snake, j, 5)) {
								snake.setDecisionDirection(Direction.UP);
								counterEsquive = true;
								break;
							}
						}
						if(counterEsquive){
							break;
						}
						else {
							 snake.setDecisionDirection(Direction.UP);
							 System.out.println("par defaut");
						}
						System.out.println("esquiveLEFT1");
						break;
					}
				}
				if(esquive){break;}

				for (int i = 0; i < snake2.getTail().size() ; i++) {

					if(isObstacleLeft2(snake,i,1)||isObstacleLeft2(snake,i,1)) {
						System.out.println("obstacle 2 : "+snake2.getTail().get(i));
						System.out.println("tete snake : "+snake.getHeadLocation());
						snake.setFocusingItem(false);

						if(snake.getFocusingItem()){
							snake.setDecisionDirection(snake.getCurrentDirection());
						}
						for (int j = 0; j < snake2.getTail().size() ; j++) {
							if (isObstacleUp2(snake, j, 1) || isObstacleUp2(snake, j, 2) || isObstacleUp2(snake, j, 3)|| isObstacleUp2(snake, j, 4)|| isObstacleUp2(snake, j, 5)) {
								snake.setDecisionDirection(Direction.DOWN);
								counterEsquive = true;
								break;
							}
							else if (isObstacleDown2(snake, j, 1) || isObstacleDown2(snake, j, 2) || isObstacleDown2(snake, j, 3)|| isObstacleDown2(snake, j, 4)|| isObstacleDown2(snake, j, 5)) {
								snake.setDecisionDirection(Direction.UP);
								counterEsquive = true;
								break;
							}
						}
						if(counterEsquive){
							break;
						}
						else {
							snake.setDecisionDirection(Direction.UP);
							System.out.println("par defaut");
						}
						System.out.println("esquiveLEFT2");
						break;
					}
				}
				break;

			case RIGHT:
				for (int i = 0; i < snake1.getTail().size() ; i++) {
					if(isObstacleRight1(snake,i,1)||isObstacleRight1(snake,i,2)) {

						System.out.println("obstacle 1: "+snake1.getTail().get(i));
						System.out.println("tete snake : "+snake.getHeadLocation());
						snake.setFocusingItem(false);
						esquive = true;

						if(snake.getFocusingItem()){
							snake.setDecisionDirection(snake.getCurrentDirection());
						}

						for (int j = 0; j < snake1.getTail().size() ; j++) {
							if (isObstacleUp1(snake, j, 1) || isObstacleUp1(snake, j, 2) || isObstacleUp1(snake, j, 3)|| isObstacleUp1(snake, j, 4)|| isObstacleUp1(snake, j, 5)) {
								snake.setDecisionDirection(Direction.DOWN);
								counterEsquive = true;
								break;
							}
							else if (isObstacleDown1(snake, j, 1) || isObstacleDown1(snake, j, 2) || isObstacleDown1(snake, j, 3)|| isObstacleUp1(snake, j, 4)|| isObstacleUp1(snake, j, 5)) {
								snake.setDecisionDirection(Direction.UP);
								counterEsquive = true;
								break;
							}
						}
						if(counterEsquive) {
							break;
						}
						else{
							snake.setDecisionDirection(Direction.DOWN);
							System.out.println("par defaut");
						}

						System.out.println("esquiveRIGHT1");
						break;
					}
				}

				if(esquive){break;}

				for (int i = 0; i < snake2.getTail().size() ; i++) {

					if(isObstacleRight2(snake,i,1)||isObstacleRight2(snake,i,2)) {

						System.out.println("obstacle 2: "+snake2.getTail().get(i));
						System.out.println("tete snake : "+snake.getHeadLocation());
						snake.setFocusingItem(false);


						if(snake.getFocusingItem()){
							snake.setDecisionDirection(snake.getCurrentDirection());
						}
						for (int j = 0; j < snake2.getTail().size() ; j++) {
							if (isObstacleUp2(snake, j, 1) || isObstacleUp2(snake, j, 2) || isObstacleUp2(snake, j, 3)|| isObstacleUp2(snake, j, 4)|| isObstacleUp2(snake, j, 5)) {
								snake.setDecisionDirection(Direction.DOWN);
								counterEsquive = true;
								break;
							} else if (isObstacleDown2(snake, j, 1) || isObstacleDown2(snake, j, 2) || isObstacleDown2(snake, j, 3)|| isObstacleUp2(snake, j, 4)|| isObstacleUp2(snake, j, 5)) {
								snake.setDecisionDirection(Direction.UP);
								counterEsquive = true;
								break;
							}
						}
						if(counterEsquive){break;}
						else {
							 snake.setDecisionDirection(Direction.DOWN);
							 System.out.println("par defaut");
						}
						System.out.println("esquiveRIGHT2");
						break;
					}
				}
				break;
		}
	}

	public Boolean isFoodExisting(Item item){
		for (int i = 0; i < grid.getListItem().size() ; i++) {
			if(item.equals(grid.getListItem().get(i))){
				return true;
			}
		}
		return false;
	}

	public Boolean checkColisionSolo1(){
		return snake1.collidedWithTail();
	}
	public Boolean checkColisionSolo2(){
		return snake2.collidedWithTail();
	}

	public Boolean checkColisionWithSnake2(){
		Boolean perdu = false;
		for (int i = 0; i < snake2.getTail().size(); i++) {
			if(snake1.getHeadLocation().equals(snake2.getTail().get(i))){
				perdu = true;
			}
		}
		if(snake1.getHeadLocation().equals(snake2.getHeadLocation())) {
			perdu = true;
		}
		return perdu;
	}
	public Boolean checkColisionWithSnake1(){
		Boolean perdu = false;
		for (int i = 0; i < snake1.getTail().size(); i++) {
			if(snake2.getHeadLocation().equals(snake1.getTail().get(i))){
				perdu = true;
			}
		}
		if(snake2.getHeadLocation().equals(snake1.getHeadLocation())) {
			perdu = true;
		}
		return perdu;
	}

	public void setMalus(){
		boxblur.setWidth(20.0f);
		boxblur.setHeight(20.0f);
		boxblur.setIterations(3);
	}

	public void resetMalus(){
		boxblur.setWidth(0);
		boxblur.setHeight(0);
	}




	// GETTERS ANS SETTERS


	public void setSnake1(Snake snake) {
		this.snake1 = snake;
	}
	public void setSnake2(Snake snake) {
		this.snake2 = snake;
	}
	public Snake getSnake1() {
		return snake1;
	}
	public Snake getSnake2(){
		return snake2;
	}
	public static long getTaskUpdatePeriodMs() {
		return TASK_UPDATE_PERIOD_MS;
	}
	public static void setTaskUpdatePeriodMs(long taskUpdatePeriodMs) {
		TASK_UPDATE_PERIOD_MS = taskUpdatePeriodMs;
	}
	public static long getTaskUpdateDelayMs() {
		return TASK_UPDATE_DELAY_MS;
	}
	public static void setTaskUpdateDelayMs(long taskUpdateDelayMs) {
		TASK_UPDATE_DELAY_MS = taskUpdateDelayMs;
	}
	public static int getWindowHeight() {
		return WINDOW_HEIGHT;
	}
	public static void setWindowHeight(int windowHeight) {
		WINDOW_HEIGHT = windowHeight;
	}
	public static int getWindowWidth() {
		return WINDOW_WIDTH;
	}
	public static void setWindowWidth(int windowWidth) {
		WINDOW_WIDTH = windowWidth;
	}
	public static int getGridBlockSize() {
		return GRID_BLOCK_SIZE;
	}
	public static void setGridBlockSize(int gridBlockSize) {
		GRID_BLOCK_SIZE = gridBlockSize;
	}
	public Grid getGrid() {
		return grid;
	}
	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	public int getAppearSpeed() {
		return appearSpeed;
	}
	public void setAppearSpeed(int appearSpeed) {
		this.appearSpeed = appearSpeed;
	}
	public boolean isGameInProgress() {
		return isGameInProgress;
	}
	public void setGameInProgress(boolean gameInProgress) {
		isGameInProgress = gameInProgress;
	}
	public boolean isGameOver() {
		return isGameOver;
	}
	public void setGameOver(boolean gameOver) {
		isGameOver = gameOver;
	}
	public boolean isPaused() {
		return isPaused;
	}
	public void setPaused(boolean paused) {
		isPaused = paused;
	}
	public int getNbSnake() {
		return nbSnake;
	}
	public void setNbSnake(int nbSnake) {
		this.nbSnake = nbSnake;
	}
	public boolean getSolo() {
		return isSolo;
	}
	public Boolean getDuo() {
		return isDuo;
	}
	public void setDuo(Boolean duo) {
		isDuo = duo;
	}
	public int getThreadCounter() {
		return threadCounter;
	}
	public ArrayList<Snake> getListSnake() {
		return listSnake;
	}
	public void setListSnake(ArrayList<Snake> listSnake) {
		this.listSnake = listSnake;
	}
	public int getNbfruits() {
		return nbfruits;
	}
	public void setNbfruits(int nbfruits) {
		this.nbfruits = nbfruits;
	}
	public int getNbfruits2() {
		return nbfruits2;
	}
	public void setNbfruits2(int nbfruits2) {
		this.nbfruits2 = nbfruits2;
	}
	public void setThreadCounter(int threadCounter) {
		this.threadCounter = threadCounter;
	}
	public Boolean getIA1() {
		return isIA1;
	}
	public Boolean getIA2() {
		return isIA2;
	}
	public Boolean getReverse() {
		return reverse;
	}
	public void setReverse(Boolean reverse) {
		this.reverse = reverse;
	}
	public int getMalusCounter(){
		return malusCounter;
	}
	public void setMalusCounter(int malusCounter){
		this.malusCounter = malusCounter;
	}
	public boolean getMalusEnCours(){
		return malusEnCours;
	}
	public void setMalusEnCours(boolean malusEnCours){
		this.malusEnCours = malusEnCours;
	}
	public BoxBlur getBoxBlur(){
		return boxblur;
	}
	public void setBoxblur(BoxBlur boxblur){
		this.boxblur = boxblur;
	}

	public boolean getIsMalusSurGrille() {
		return isMalusSurGrille;
	}

	public void setIsMalusSurGrille(boolean isMalusSurGrille) {
		this.isMalusSurGrille = isMalusSurGrille;
	}

	public Boolean getEnd1() {
		return end1;
	}

	public void setEnd1(Boolean end1) {
		this.end1 = end1;
	}

	public Boolean getEnd2() {
		return end2;
	}

	public void setEnd2(Boolean end2) {
		this.end2 = end2;
	}

	public Boolean getTie() {
		return tie;
	}

	public void setTie(Boolean tie) {
		this.tie = tie;
	}
}

