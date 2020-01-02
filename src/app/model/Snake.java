package app.model;

import java.util.ArrayList;
import java.util.List;

public class Snake {

	private Direction currentDirection;
	private Direction nextDirection;
	private Direction pastDirection;
	private Direction decisionDirection;
	private Segment headLocation = new Segment(0, 0);
	private List<Segment> tail = new ArrayList<>();
	private List<Segment> posHead = new ArrayList<>();
	private Segment lastTailEnd;
	private int height;
	private int width;
	private int blockSize;
	private int toPrint = 0;
	private int toDePrint = 0;
	private int appearSpeed;
	private Boolean isFocusingItem = false;
	private Item focusedItem;
	private Boolean isEscaping = false;


	public Snake(int width, int height, int blockSize) {
		this.width = width;
		this.height = height;
		this.blockSize = blockSize;
		this.currentDirection = Direction.RIGHT;
		this.appearSpeed = 5;
	}

	public void snakeUpdate() {

		if(tail.size()==0){
			toDePrint = 0;
		}

		if(toPrint>0 && toDePrint==0){
			addTailSegment();
			toPrint--;
		}
		else if(toPrint>0 && toDePrint>0){
			toDePrint--;
			toPrint--;
		}
		else if(toPrint==0 && toDePrint>0){
			delTailSegment();
			toDePrint--;
		}

		this.posHead.add(0,new Segment(headLocation.getX(),headLocation.getY()));
		lastTailEnd = posHead.get(posHead.size()-1);
		for (int i = 0; i < tail.size() ; i++) {
			deplacementSimple(posHead.get(i),tail.get(i));
		}

		if(posHead.size()>tail.size()) {
			posHead.subList(tail.size(), posHead.size()).clear();
		}
		switch (currentDirection) {
			case UP:
				headLocation.setY((((headLocation.getY() - blockSize/10)%height)+height)%height);
				break;

			case DOWN:
				headLocation.setY((headLocation.getY() + blockSize/10)%height);
				break;

			case LEFT:
				headLocation.setX((((headLocation.getX() - blockSize/10)%width)+width)%width);
				break;

			case RIGHT:
				headLocation.setX((headLocation.getX() + blockSize/10)%width);
				break;

			default:
				break;
		}

	}

	public boolean collidedWithTail() {
		boolean isCollision = false;

		for (Segment tailSegment : tail) {
			if (headLocation.equals(tailSegment)) {
				isCollision = true;
				break;
			}
		}

		return isCollision;
	}

	public void addTailSegment(){
		tail.add(new Segment(headLocation.getX(), headLocation.getY()));

	}
	public void delTailSegment(){
		tail.remove(tail.size()-1);
	}

	public void setCurrentDirection(Direction myDirection) {

		this.currentDirection = myDirection;

	}

	public Direction getCurrentDirection(){
		return this.currentDirection;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setHeadLocation(int x, int y) {
		headLocation.setX(x);
		headLocation.setY(y);
	}

	public Segment getHeadLocation() {
		return headLocation;
	}

	public List<Segment> getTail() {
		return tail;
	}

	public void deplacementSimple(Segment pos, Segment segment){
		segment.setX(pos.getX()%width);
		segment.setY(pos.getY()%height);
	}

	public void addPointToPrint(int i){
		toPrint += i;
	}

	public List<Segment> getPosHead() {
		return posHead;
	}

	public void addPointToDel(int i) { toDePrint += i*10; }

	public int getAppearSpeed() {
		return appearSpeed;
	}

	public void setAppearSpeed(int appearSpeed) {
		this.appearSpeed = appearSpeed;
	}

	public Segment getLastTailEnd() {
		return lastTailEnd;
	}

	public void setLastTailEnd(Segment lastTailEnd) {
		this.lastTailEnd = lastTailEnd;
	}

	public Direction getNextDirection() {
		return nextDirection;
	}

	public void setNextDirection(Direction nextDirection) {
		this.nextDirection = nextDirection;
	}

	public Boolean getFocusingItem() {
		return isFocusingItem;
	}

	public void setFocusingItem(Boolean focusingItem) {
		isFocusingItem = focusingItem;
	}

	public Boolean getEscaping() {
		return isEscaping;
	}

	public void setEscaping(Boolean escaping) {
		isEscaping = escaping;
	}

	public Direction getPastDirection() {
		return pastDirection;
	}

	public void setPastDirection(Direction pastDirection) {
		this.pastDirection = pastDirection;
	}

	public void setDecisionDirection(Direction dir){
		this.decisionDirection = dir;
	}
	public Direction getDecisionDirection() {
		return decisionDirection;
	}

	public Item getFocusedItem() {
		return focusedItem;
	}

	public void setFocusedItem(Item focusedItem) {
		this.focusedItem = focusedItem;
	}
}
