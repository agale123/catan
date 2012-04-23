package Catan;

import java.util.*;
import java.lang.*;

public class Vertex {

	private ArrayList<Vertex> _neighbors;
	//private ArrayList<Hex> _hexes;
	private int	_object = 0;
	private int _vertexNum;
	private Player _owner = null;
	
	public Vertex(int num) {
		_vertexNum = num;
	}
	
	public void setNeighbors(ArrayList<Vertex> neighbors) {
		_neighbors = neighbors;
	}
	
	public int getObject() {
		return _object;
	}
	
	public void setObject(int x) {
		_object = x;
	}
	
	public void setOwner(Player p) {
		_owner = p;
	}
	
	public Player getOwner() {
		return _owner;
	}
	
	public int getNumber() {
		return _vertexNum;
	}
	
	public boolean isBuildable() {
		for (Vertex v : _neighbors) {
			if (v.getObject() != 0) {
				return false;
			}
		} return true;
	}
}
