package gamelogic;

import java.util.*;
import java.lang.*;

public class Vertex {
	private int	_object = 0;
	private int _owner = -1;
	int _x, _y;
	int _port = -1;
	
	public Vertex(int x, int y) {
		_x = x;
		_y = y;
	}
	
	public boolean equals(Object o) {
	    Vertex v = (Vertex) o;
	    return (v.getX() == _x && v.getY() == _y);
	}
	
	public int hashCode() {
		return (int) (_x*3 + _y);
	}
	
	public void setObject(int x) { //settlement = 1, city = 2
		_object = x;
	}
	
	public void setOwner(int p) {
		_owner = p;
	}
	
	public void setPort(int i) {
	    _port = i;
	}
	
	public int getObject() { return _object; }
	public int getOwner() { return _owner; }
	public int getX() { return _x; }
	public int getY() { return _y; }
	public int getPort() { return _port; }
	
	public String toString() {
		return _x + " " + _y + " " + _owner;
	}
}
