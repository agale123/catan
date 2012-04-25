package catanai;

public class BoardCoordinate implements AIConstants {
	private int _x, _y, _z;
	
	public BoardCoordinate(int x, int y, int z) {
		_x = x;
		_y = y;
		_z = z;
	}
	
	public int x() {
		return _x;
	}
	
	public int y() {
		return _y;
	}
	
	public int z() {
		return _z;
	}
	
	// distance: Returns minimum number of edges between this and other.
	public int distance(BoardCoordinate other) {
		int dx = Math.abs(this.x() - other.x());
		int dy = Math.abs(this.y() - other.y());
		int dz = Math.abs(this.z() - other.z());
		return dx + dy + dz;
	}
	
	// validMove: Returns whether one can move in the given dimension and direction from this.
	// Throws an IllegalArgumentException if the given dimension is not valid.
	private boolean validMove(int dim, boolean dir) {
		switch (dim) {
		case 0:
			if (dir) return this.x() < CEIL_X && (this.y() % 2 == this.z() % 2);
			else return this.x() > 0 && (this.y() % 2 != this.z() % 2);
		case 1:
			if (dir) return this.y() < CEIL_Y && (this.x() % 2 == this.z() % 2);
			else return this.y() > 0 && (this.x() % 2 != this.z() % 2);
		case 2:
			if (dir) return this.z() < CEIL_Z && (this.x() % 2 == this.y() % 2);
			else return this.z() > FLOOR_Z && (this.x() % 2 != this.y() % 2);
		default:
			throw new IllegalArgumentException("Dimensionality out of bounds!");
		}
	}
	
	// moveIn: Returns the BoardCoordinate adjacent this in the given dimension and direction.
	// Returns null if the new coordinate cannot exist on the board.
	// Throws IllegalArgumentException if the dimension is not valid.
	// 0 => x, 1 => y, 2 => z
	public BoardCoordinate moveIn(int dim, boolean dir) {
		if (dim < 0 || dim > 2) throw new IllegalArgumentException("Dimensionality out of bounds!");
		if (! validMove(dim, dir)) return null;
		int offset = (dir)? 1:-1;
		switch (dim) {
		case 0:
			return new BoardCoordinate(this.x() + offset, this.y(), this.z());
		case 1:
			return new BoardCoordinate(this.x(), this.y() + offset, this.z());
		case 2:
			return new BoardCoordinate(this.x(), this.y(), this.z() + offset);
		default:
			throw new IllegalArgumentException("Dimensionality out of bounds!");
		}
	}
}
