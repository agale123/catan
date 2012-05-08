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
	
	public boolean build(Player p) {
		if (p != null && isLegal(p) && hasIncRoad(p)) {
			_build = BuildType.Settlement;
			_controller = p;
			return true;
		}
		return false;
	}
	
	public boolean buildInitial(Player p) {
		if (p == null) System.out.println("Null player in buildInitial!"); // TODO: Debug line
		else if (! isLegal(p)) System.out.println("Illegal vertex for construction: " + toString()); // TODO: Debug line
		if (p != null && isLegal(p)) {
			_build = BuildType.Settlement;
			_controller = p;
			return true;
		}
		return false;
	}
	
	public boolean upgrade(Player p) {
		if (_controller == p && _build == BuildType.Settlement) {
			_build = BuildType.City;
			return true;
		}
		else return false;
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
	
	public boolean port() {
		return _port != null;
	}
	
	public Set<Vertex> neighbors() {
		HashSet<Vertex> toReturn = new HashSet<Vertex>();
		l0: for (Edge e : _edges) {
			for (Vertex v : e.ends()) {
				if (v != this) {
					toReturn.add(v);
					continue l0;
				}
			}
		}
		return toReturn;
	}
	
	public boolean isLegal(Player p) {
		if (_controller != null) return false;
		int inc_roads = 0;
		for (Edge e : _edges) if (e.road() && e.controller() != p) inc_roads++;
		if (inc_roads >= 2) return false;
		for (Vertex v: neighbors()) if (v.controller() != null) return false;
		return true;
	}
	
	public boolean hasIncRoad(Player p) {
		for (Edge e : _edges) if (e.road() && e.controller() == p) return true;
		return false;
	}
	
	public boolean gainsResource(Resource res) {
		for (Tile t : tiles()) if (TILE_RES.containsKey(t.resource()) || TILE_RES.get(t.resource()) == res) return true;
		return false;
	}
	
	public int numResourceOver(Vertex other) {
		int ret = 0;
		for (Resource r : Resource.values())
			if (this.gainsResource(r) && ! other.gainsResource(r)) ret++;
		return ret;
	}
	
	public Edge edgeTo(Vertex other) {
		if (distance(other) != 1) return null;
		for (Edge e : _edges) if (e.hasEnd(other)) return e;
		return null;
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof Vertex) && (this.location().equals(((Vertex) other).location()));
	}
	
	@Override
	public String toString() {
		return this.location().toString();
	}
}
