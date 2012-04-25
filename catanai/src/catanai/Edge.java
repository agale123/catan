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
	
	public void build(Player control) {
		if (! _road) {
			_road = true;
			_controller = control;
		}
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
	
	public Set<Edge> neighbors() {
		HashSet<Edge> toReturn = new HashSet<Edge>();
		for (Vertex v : _ends) for (Edge e : v.edges()) if (e != this) toReturn.add(e);
		return toReturn;
	}
}
