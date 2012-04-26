package gamelogic;

import java.util.*;
import java.lang.*;

public class Hex {
	
	private boolean	_disabled = false;
	private int		_rollNum;
	private int		_hexNum;
	private BoardObject.type _resource;
	private double _x, _y;
	ArrayList<Vertex> _vertices;

	//public Hex(int hexNum) {
	public Hex(int hexHum, double x, double y) {
		_hexNum = hexNum;
		_x = x;
		_y = y;
	}
	
	public void setVertices(ArrayList<Vertex> vertices) {
	    _vertices = vertices;
	}
	
	public void setRollAndResource(int rollNum, BoardObject.type resource) {
	    _rollNum = rollNum;
	    _resource = resource;
	}
	
	public ArrayList<Vertex> getVertices() { return _vertices; }
	public int getRollNum() { return _rollNum; }
	public int getNum() { return _hexNum; }
	public BoardObject.type getResource() { return _resource; }
	public double getX() { return _x; }
	public double getY() { return _y; }
}
