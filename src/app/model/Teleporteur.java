package app.model;

import java.util.ArrayList;
import java.util.Objects;

public class Teleporteur {

	private ArrayList<Segment>Couple = new ArrayList<>();
	private int utilisationsRestantes;

	public Teleporteur(int x, int y, int i, int j, int u){
		this(new Segment(x, y), new Segment(i, j), u);
	}

	public Teleporteur(Segment a, Segment b, int u){
		Couple.add(a);
		Couple.add(b);
		utilisationsRestantes = u;
	}

	public Teleporteur(int width, int height, int pixelsPerSquare){
		int x = width;
		int y = height;
		x = Math.round(x / pixelsPerSquare) * pixelsPerSquare;
		y = Math.round(y / pixelsPerSquare) * pixelsPerSquare;

		Couple.add(new Segment(0,0));
		Couple.add(new Segment(width - Model.getGridBlockSize(), height - Model.getGridBlockSize()));

		utilisationsRestantes = 1;
	}

	public Segment getSortie(Segment entree){
		int indexEntree = Couple.indexOf(entree);
		utilisationsRestantes--;
		return Couple.get((indexEntree+1)%2);
	}

	public Segment getSortie(int x, int y){
		return getSortie(new Segment(x, y));
	}

	public ArrayList<Segment> getCouple() {
		return Couple;
	}

	public void setCouple(ArrayList<Segment> couple) {
		Couple = couple;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Teleporteur that = (Teleporteur) o;
		return Objects.equals(Couple, that.Couple);
	}

	@Override
	public int hashCode() {
		return Objects.hash(Couple);
	}

	@Override
	public String toString() {
		return "Teleporteur{" +
				"Couple=" + Couple +
				'}';
	}

	public int getUtilisationsRestantes(){
		return utilisationsRestantes;
	}

	public void setUtilisationsRestantes(int i){
		this.utilisationsRestantes = i;
	}
}
