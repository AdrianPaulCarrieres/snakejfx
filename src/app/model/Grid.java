package app.model;

import java.util.ArrayList;
import java.util.Random;

public class Grid {
	public Model model;
	private int height;
	private int width;
	private int pixelsPerSquare;
	ArrayList<Item> listItem = new ArrayList<>();
	private Teleporteur tp;
	private Redirection redirection;

	public Grid(int width, int height, int pixelsPerSquare) {
		this.width = width;
		this.height = height;
		this.pixelsPerSquare = pixelsPerSquare;
		regenAll();
	}

	public void reset() {
		listItem.clear();
		regenAll();
	}

	public void regenAll(){
		listItem.clear();

		int x = width/4;
		int y = height/4;
		x = Math.round(x / pixelsPerSquare) * pixelsPerSquare;
		y = Math.round(y / pixelsPerSquare) * pixelsPerSquare;

		listItem.add(new Item(x, y, 1));
		tp = new Teleporteur(width, height, pixelsPerSquare);

		Random bRandon = new Random();
		x = Math.round(bRandon.nextInt(width) / pixelsPerSquare) * pixelsPerSquare;
		y = Math.round(bRandon.nextInt(height) / pixelsPerSquare) * pixelsPerSquare;
		redirection = new Redirection(new Segment(x, y));
	}

	public boolean foundFood(Snake snake) {
		for (int i = 0; i < listItem.size(); i++) {
			if (snake.getHeadLocation().equals(listItem.get(i).getLocation())) {
				ArrayList<Snake> l = new ArrayList<>();
				l.add(snake);
				addFood(l);
				virerItem(snake.getHeadLocation(), listItem.get(i).getType());
				return true;
			}
		}
		return false;
	}

	public int itemCheck(Snake snake) {
		for (int i = 0; i < listItem.size(); i++) {
			if (snake.getHeadLocation().equals(listItem.get(i).getLocation())) {
				return listItem.get(i).getType();
			}
		}
		return 0;
	}

	public boolean foundTP(Snake snake) {
		if (tp.getCouple().contains(snake.getHeadLocation())) {
			Segment s = tp.getSortie(snake.getHeadLocation());
			snake.setHeadLocation(s.getX(), s.getY());
			ArrayList<Snake> l = new ArrayList<>();
			l.add(snake);
			genTP(l);
			return true;
		}
		return false;
	}

	public void addFood(ArrayList<Snake> listSnake) {
		if (listItem.size() < 8) {
			int compteur = 1 + (int) (Math.random() * ((6 - 1) + 1));
			int z = 0;
			if (listItem.isEmpty() && compteur == 0) {
				z--;
			}
			for (int k = z; k < compteur; k++) {
				Segment s = getEmptySegment(listSnake);
				int probaSuperFruit = (int) (Math.random() * 100);
				if (probaSuperFruit <= 10) {
					listItem.add(new Item(s.getX(), s.getY(), 2));
				} else {
					listItem.add(new Item(s.getX(), s.getY(), 1));
				}
			}
		}
	}

	public void addMalus(Snake snake1, Snake snake2){
		boolean dejaDedans = false;
		for(int i=0; i<listItem.size(); i++){
			if(listItem.get(i).getType()==3){
				dejaDedans = true;
			}
		}
		if(!dejaDedans){
			ArrayList<Snake> l = new ArrayList<>();
			l.add(snake1);
			if(snake2!=null){
				l.add(snake2);
			}
			Segment s = getEmptySegment(l);
			listItem.add(new Item(s.getX(), s.getY(), 3));
		}
	}

	public ArrayList<Item> getListItem() {
		return listItem;
	}

	public void virerItem(Segment s, int type) {
		listItem.remove(new Item(s.getX(), s.getY(), type));
	}


	public void genTP(ArrayList<Snake> listSnake) {
		if (tp.getUtilisationsRestantes() <= 0) {
			Random bRandon = new Random();
			int x = Math.round(bRandon.nextInt(width) / pixelsPerSquare) * pixelsPerSquare;
			int y = Math.round(bRandon.nextInt(height) / pixelsPerSquare) * pixelsPerSquare;
			int m = Math.round(bRandon.nextInt(width) / pixelsPerSquare) * pixelsPerSquare;
			int n = Math.round(bRandon.nextInt(height) / pixelsPerSquare) * pixelsPerSquare;
			int u = 1 + (int) (Math.random() * ((4 - 1) + 1));
			for (int i = 0; i < listSnake.size(); i++) {
				for (int j = 0; j < listSnake.get(i).getPosHead().size(); j++) {
					boolean flag = false;
					boolean flagBis = false;
					do {
						x = Math.round(bRandon.nextInt(width) / pixelsPerSquare) * pixelsPerSquare;
						y = Math.round(bRandon.nextInt(height) / pixelsPerSquare) * pixelsPerSquare;
						m = Math.round(bRandon.nextInt(width) / pixelsPerSquare) * pixelsPerSquare;
						n = Math.round(bRandon.nextInt(height) / pixelsPerSquare) * pixelsPerSquare;
						u = 1 + (int) (Math.random() * ((4 - 1) + 1));

						flag = (x == m) && (y == n);
						flagBis = (x == listSnake.get(i).getPosHead().get(j).getX() && m == listSnake.get(i).getPosHead().get(j).getX() && y == listSnake.get(i).getPosHead().get(j).getY() && n == listSnake.get(i).getPosHead().get(j).getY());
					} while (flag && flagBis);
				}
			}
			tp = new Teleporteur(x, y, m, n, u);
		}
	}

	public void genRedirection(ArrayList<Snake> listSnake) {
		redirection = new Redirection(getEmptySegment(listSnake));
	}


	public Teleporteur getTp() {
		return tp;
	}

	public Segment getEmptySegment(ArrayList<Snake> listSnake) {
		int x = Math.round((int) (Math.random() * ((width))) / pixelsPerSquare) * pixelsPerSquare;
		int y = Math.round((int) (Math.random() * ((height))) / pixelsPerSquare) * pixelsPerSquare;
		for (int i = 0; i < listSnake.size(); i++) {
			for (int j = 0; j < listSnake.get(i).getPosHead().size(); j++) {
				Random bRandon = new Random();
				do {
					x = Math.round(bRandon.nextInt(width) / pixelsPerSquare) * pixelsPerSquare;
					y = Math.round(bRandon.nextInt(height) / pixelsPerSquare) * pixelsPerSquare;
				} while (x == listSnake.get(i).getPosHead().get(j).getX() && y == listSnake.get(i).getPosHead().get(j).getY() && listItem.contains(new Item(x, y, 1)) && listItem.contains(new Item(x, y, 2)) && listItem.contains(new Item(x, y, 3)) && tp.getCouple().contains(new Segment(x, y)));
			}
		}
		return new Segment(x, y);
	}

	public Redirection getRedirection() {
		return redirection;
	}


	public boolean foundRedirection(Snake snake, ArrayList<Snake> listSnake) {

		if(snake.getHeadLocation().equals(redirection.getPos())) {
			Direction dir = redirection.getDirection();
			switch (dir) {
				case UP:
					if (snake.getCurrentDirection() != Direction.DOWN) {
						snake.setPastDirection(snake.getCurrentDirection());
						snake.setNextDirection(dir);
						return true;
					}
					else{
						genRedirection(listSnake);
					}
					break;
				case DOWN:
					if (snake.getCurrentDirection() != Direction.UP) {
						snake.setPastDirection(snake.getCurrentDirection());
						snake.setNextDirection(dir);
						return true;
					}
					else{
						genRedirection(listSnake);
					}
					break;
				case LEFT:
					if (snake.getCurrentDirection() != Direction.RIGHT) {
						snake.setPastDirection(snake.getCurrentDirection());
						snake.setNextDirection(dir);
						return true;
					}
					else{
						genRedirection(listSnake);
					}
					break;
				case RIGHT:
					if (snake.getCurrentDirection() != Direction.LEFT) {
						snake.setPastDirection(snake.getCurrentDirection());
						snake.setNextDirection(dir);
						return true;
					}
					else{
						genRedirection(listSnake);
					}

					break;
			}
		}
		return false;
	}
}
