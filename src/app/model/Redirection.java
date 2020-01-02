package app.model;

public class Redirection {

	private Direction direction;
	private Segment pos;

	public Redirection(Direction r, Segment p){
		direction = r;
		pos = p;
	}

	public Redirection(Segment p){
		int nombreAleatoire = (int)(Math.random() * ((3) + 1));
		direction = Direction.values()[nombreAleatoire];
		pos = p;
	}

	public Direction getDirection(){
		return direction;
	}

	public Segment getPos(){
		return pos;
	}
}
