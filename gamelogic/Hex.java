package gamelogic;

import java.util.*;
import java.lang.*;

/**
Represents a hex on the game board
*/
public class Hex {
	
	private int _rollNum;
	private int _hexNum;
	private catanui.BoardObject.type _resource;
	private double _x, _y;
	ArrayList<Vertex> _vertices;

	public Hex(int hexNum, double x, double y) {
		_hexNum = hexNum;
		_x = x;
		_y = y;
	}
	
	//set the hex's 6 vertices
	public void setVertices(ArrayList<Vertex> vertices) {
	    _vertices = vertices;
	}
	
	//what type of resource the hex is
	public void setResource(catanui.BoardObject.type resource) {
	    _resource = resource;
	}
	
	//the roll number associated with the hex
	public void setRollNum(int i) {
		_rollNum = i;
	}
	
	//whether a vertex is part of this hex
	public boolean containsVertex(Vertex v) {
		for(Vertex w : _vertices) {
			if(v.equals(w)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Vertex> getVertices() { return _vertices; }
	public int getRollNum() { return _rollNum; }
	public int getNum() { return _hexNum; }
	public catanui.BoardObject.type getResource() { return _resource; }
	public double getX() { return _x; }
	public double getY() { return _y; }
}
