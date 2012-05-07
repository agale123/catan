package gamelogic;

/**
Represents a pair of x and y coordinates
*/
public class CoordPair implements java.io.Serializable{
    
    public int _x, _y;
    
    public CoordPair(int x, int y) {
		_x = x;
		_y = y;
    }
    
    public boolean equals(Object o) {
		CoordPair v = (CoordPair) o;
		return (v.getX() == _x && v.getY() == _y);
    }
    
    public int hashCode() {
		return (int) (13*_x + 31*_y);
    }

    public int getX() { return _x; }
    public int getY() { return _y; }
    
    public String toString() {
		return "(" + _x + "," + _y + ")";
    }
}
