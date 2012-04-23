package Catan;

import java.util.*;
import java.lang.*;

public class Hex {
	
	private boolean	_disabled = false;
	private int		_rollNum;
	private int		_hexNum;
	private int _resource;
	ArrayList<Integer> _vertices;

	public Hex(int resource, int rollNum, int hexNum, ArrayList<Integer> vertices) {
		_resource = resource;
		_rollNum = rollNum;
		_hexNum = hexNum;
		_vertices = vertices;
	}
	
	public ArrayList<Integer> getVertices() { return _vertices; }
	public int getRollNum() { return _rollNum; }
	public int getNum() { return _hexNum; }
	public int getResource() { return _resource; }
}
