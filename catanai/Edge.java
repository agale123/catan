package catanai;

import java.util.HashSet;
import java.util.Set;

public class Edge implements AIConstants {
	private Set<Vertex> _ends;
	private boolean _road;
	private Player _controller;
	
	public Edge(Set<Vertex> ends) {
		_ends = ends;
		_road = false;
		_controller = null;
	}
	
	public void addEnd(Vertex v) {
		if (_ends.size() < NUM_ENDS) _ends.add(v);
	}
	
	public boolean build(Player control) {
		if (! _road) {
			_road = true;
			_controller = control;
			return true;
		}
		else return false;
	}
	
	public boolean road() {
		return _road;
	}
	
	public Player controller() {
		return _controller;
	}
	
	public Set<Vertex> ends() {
		return _ends;
	}
	
	public boolean hasEnd(Vertex end) {
		for (Vertex v : _ends) if (v.equals(end)) return true;
		return false;
	}
	
	public Set<Edge> neighbors() {
		HashSet<Edge> toReturn = new HashSet<Edge>();
		for (Vertex v : _ends) for (Edge e : v.edges()) if (e != this) toReturn.add(e);
		return toReturn;
	}
	
	@Override
	public String toString() {
		Vertex ends[] = new Vertex[_ends.size()];
		int i = 0;
		for (Vertex v : _ends) {
			ends[i] = v;
			i++;
		}
		String toReturn = "between ";
		for (int j = 0; j < ends.length; j++) toReturn += ends[j].toString() + ((j == ends.length - 1)? ".":" and ");
		return toReturn;
	}
}
