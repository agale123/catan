package gamelogic;

public class CoordPair {
    
    public int _x, _y;
    
    public CoordPair(int x, int y) {
		_x = x;
		_y = y;
    }
    
    public boolean equals(Object o) {
		Vertex v = (Vertex) o;
		return (v.getX() == _x && v.getY() == _y);
    }
    
    public int hashCode() {
		return (int) (13*_x + 31*_y);
    }
}