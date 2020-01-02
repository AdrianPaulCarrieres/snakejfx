package app.model;

import java.util.Objects;

public class Item {
	private Segment location;
	private int type;

	public Item(int x, int y, int type) {
		location = new Segment(x, y);
		this.type = type;
	}

	public Segment getLocation() {
		return location;
	}

	public int getType(){
		return this.type;
	}

	@Override
	public String toString() {
		return "Model.Item [myLocation=" + location + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Item item = (Item) o;
		return type == item.type &&
				location.equals(item.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(location, type);
	}
}
