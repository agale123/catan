package gamelogic;

import java.util.*;
import java.lang.*;

public class Hex {
	
	private int		_rollNum;
	private int		_hexNum;
	private catanui.BoardObject.type _resource;
	private double _x, _y;
	ArrayList<Vertex> _vertices;

	//public Hex(int hexNum) {
	public Hex(int hexNum, double x, double y) {
		_hexNum = hexNum;
		_x = x;
		_y = y;
	}
	
	public void setVertices(ArrayList<Vertex> vertices) {
	    _vertices = vertices;
	}
	
	public void setResource(catanui.BoardObject.type resource) {
	    _resource = resource;
	}
	
	public void setRollNum(int i) {
		_rollNum = i;
	}
	
	public ArrayList<Vertex> getVertices() { return _vertices; }
	public int getRollNum() { return _rollNum; }
	public int getNum() { return _hexNum; }
	public catanui.BoardObject.type getResource() { return _resource; }
	public double getX() { return _x; }
	public double getY() { return _y; }
	
	public boolean containsVertex(Vertex v) {
		for(Vertex w : _vertices) {
			if(v.equals(w)) {
				return true;
			}
		}
		return false;
	}
}
