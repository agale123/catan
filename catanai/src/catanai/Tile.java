package catanai;

import java.util.Set;

public class Tile implements AIConstants {
	private int _rollNum;
	private TileType _resource;
	private boolean _robber;
	private Set<Edge> _edges;
	
	public Tile(Set<Edge> edges) {
		_rollNum = -1;
		_resource = null;
		_robber = false;
		_edges = edges;
	}
	
	public void setRoll(int roll) {
		if (_rollNum == -1 && roll >= 2 && roll <= 12) _rollNum = roll;
	}
	
	public void setType(TileType res) {
		if (_resource == null) _resource = res;
	}
	
	public void addEdge(Edge inc) {
		if (_edges.size() < MAX_SIDES) _edges.add(inc);
	}
	
	public void setRobber() {
		_robber = true;
	}
	
	public void removeRobber() {
		_robber = false;
	}
	
	public boolean isRobbed() {
		return _robber;
	}
	
	public TileType resource() {
		return _resource;
	}
	
	public int roll() {
		return _rollNum;
	}
	
	public Set<Edge> getEdges() {
		return _edges;
	}
}
