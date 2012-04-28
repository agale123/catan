package gamelogic;

import java.util.*;
import java.lang.*;

public class Vertex {

	private ArrayList<Integer> _neighbors;
	//private ArrayList<Hex> _hexes;
	private int	_object = 0;
	private int _vertexNum;
	private int _owner = -1;
	int _x, _y;
	
	/*public Vertex(int num, ArrayList<Integer> neighbors) {
		_vertexNum = num;
		_neighbors = neighbors;
	}*/
	public Vertex(int x, int y) {
		_x = x;
		_y = y;
	}
	
	//public void setNeighbors(ArrayList<Vertex> neighbors) {
	/*public void setNeighbors(ArrayList<Integer> neighbors) {
		_neighbors = neighbors;
	}*/
	
	public boolean equals(Object o) {
	    Vertex v = (Vertex) o;
	    return (v.getX() == _x && v.getY() == _y);
	}
	
	public ArrayList<Integer> getNeighbors() {
	    return _neighbors;
	}
	
	public int getObject() {
		return _object;
	}
	
	public void setObject(int x) { //settlement = 1, city = 2
		_object = x;
	}
	
	public void setOwner(int p) {
		_owner = p;
	}
	
	public int getOwner() {
		return _owner;
	}
	
	public int getNumber() {
		return _vertexNum;
	}
	
	public int getX() { return _x; }
	public int getY() { return _y; }
	
	/*public boolean isBuildable() {
		for (Vertex v : _neighbors) {
			if (v.getObject() != 0) {
				return false;
			}
		} return true;
	}*/
}
