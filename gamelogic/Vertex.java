package gamelogic;

import java.util.*;
import java.lang.*;
import catanui.*;

public class Vertex {
	private int	_object = 0;
	private int _owner = -1;
	int _x, _y;
	BoardObject.type _port = null;
	
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
	
	public void setPort(BoardObject.type t) {
	    _port = t;
	}
	
	public boolean isPort() {
	    if (_port == null) {
		return false;
	    }
	    return true;
	}
	
	public int getObject() { return _object; }
	public int getOwner() { return _owner; }
	public int getX() { return _x; }
	public int getY() { return _y; }
	public BoardObject.type getPort() { return _port; }
	
	public String toString() {
		return _x + " " + _y + " " + _owner;
	}
}
