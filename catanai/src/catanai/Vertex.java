package catanai;

import java.util.HashSet;
import java.util.Set;

public class Vertex implements AIConstants {
	private Set<Edge> _edges;
	private Set<Tile> _tiles;
	private BuildType _build;
	private Player _controller;
	private Resource _port;
	private BoardCoordinate _location;
	
	public Vertex(Set<Edge> edges, Set<Tile> tiles) {
		_edges = edges;
		_tiles = tiles;
		_build = BuildType.None;
		_controller = null;
		_port = null;
		_location = null;
	}
	
	public void addEdge(Edge inc) {
		if (! (_edges.size() >= MAX_ADJ_TILES || _edges.contains(inc))) _edges.add(inc);
	}
	
	public void addTile(Tile t) {
		if (! (_tiles.size() >= MAX_ADJ_TILES || _tiles.contains(t))) _tiles.add(t);
	}
	
	public void build(Player control) {
		if (_controller == null && control != null) {
			_build = BuildType.Settlement;
			_controller = control;
		}
	}
	
	public void upgrade() {
		if (_build == BuildType.Settlement) _build = BuildType.City;
	}
	
	public void setLocation(BoardCoordinate loc) {
		if (_location == null) _location = loc;
	}
	
	public BoardCoordinate location() {
		return _location;
	}
	
	public int distance(Vertex other) {
		return this.location().distance(other.location());
	}
	
	public double value() {
		double value = 0;
		for (Tile t : _tiles) value += DIE_FREQ[t.roll()];
		return value;
	}
	
	public Player controller() {
		return _controller;
	}
	
	public BuildType buildType() {
		return _build;
	}
	
	public Set<Tile> tiles() {
		return _tiles;
	}
	
	public Set<Edge> edges() {
		return _edges;
	}
	
	public boolean isPort() {
		return _port != null;
	}
	
	public Set<Vertex> neighbors() {
		HashSet<Vertex> toReturn = new HashSet<Vertex>();
		for (Edge e : _edges) for (Vertex v : e.ends()) if (v != this) toReturn.add(v);
		return toReturn;
	}
}
