package catanai;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameBoard implements AIConstants {
	private Hashtable<BoardCoordinate, Vertex> _v;
	private Set<Edge> _e;
	private Set<Tile> _t;
	
	public GameBoard() {
		this(false);
	}
	
	public GameBoard(boolean extended) {
		_v = new Hashtable<BoardCoordinate, Vertex>();
		_e = new HashSet<Edge>();
		_t = new HashSet<Tile>();
		Vertex current_v;
		for (BoardCoordinate c : VALID_VERTS) {
			current_v = new Vertex(new HashSet<Edge>(), new HashSet<Tile>());
			current_v.setLocation(c);
			_v.put(c, current_v);
		}
		int rem = MAX_ADJ_TILES;
		Edge current_e;
		HashSet<BoardCoordinate> done = new HashSet<BoardCoordinate>();
		l0: for (BoardCoordinate c : _v.keySet()) {
			for (BoardCoordinate d : _v.keySet()) {
				if (c.distance(d) == 1) {
					rem--;
					if (! done.contains(d)) {
						current_e = new Edge(new HashSet<Vertex>());
						current_e.addEnd(_v.get(c));
						current_e.addEnd(_v.get(d));
						_v.get(c).addEdge(current_e);
						_v.get(d).addEdge(current_e);
						_e.add(current_e);
					}
				}
				if (rem == 0) break l0;
			}
			done.add(c);
		}
		// Populate the tile set.
		int tile_rem = NUM_TILES;
		for (BoardCoordinate c : _v.keySet()) {
			if (tile_rem == 0) break;
			if (c.moveIn(DIM_Z, true) == null || c.moveIn(DIM_X, true) == null) continue;
			Tile t = new Tile(new HashSet<Vertex>());
			t.addEdge(_v.get(c));
			t.addEdge(_v.get(c.moveIn(DIM_X, true)));
			t.addEdge(_v.get(c.moveIn(DIM_Z, true)));
			t.addEdge(_v.get(c.moveIn(DIM_X, true).moveIn(DIM_Y, true)));
			t.addEdge(_v.get(c.moveIn(DIM_Z, true).moveIn(DIM_Y, true)));
			t.addEdge(_v.get(c.moveIn(DIM_X, true).moveIn(DIM_Y, true).moveIn(DIM_Z, true)));
			_t.add(t);
			_v.get(c).addTile(t);
			_v.get(c.moveIn(DIM_X, true)).addTile(t);
			_v.get(c.moveIn(DIM_Z, true)).addTile(t);
			_v.get(c.moveIn(DIM_X, true).moveIn(DIM_Y, true)).addTile(t);
			_v.get(c.moveIn(DIM_Z, true).moveIn(DIM_Y, true)).addTile(t);
			_v.get(c.moveIn(DIM_X, true).moveIn(DIM_Y, true).moveIn(DIM_Z, true)).addTile(t);
			tile_rem--;
		}
	}
	
	public Vertex mostValuableLegalVertex(Player p) {
		return mostValuableLegalVertex(p, BoardCoordinate.ORIGIN, (CEIL_X - FLOOR_X) + (CEIL_Y - FLOOR_Y) + (CEIL_Z - FLOOR_Z));
	}
	
	/**
	 * mostValuableLegalVertex
	 * @param p: Player for whom legality is determined
	 * @param center: The coordinate of reference
	 * @param dist: The radius of the search
	 * @return: Returns the most valuable legal vertex for p within dist edges of center.
	 */
	public Vertex mostValuableLegalVertex(Player p, BoardCoordinate center, int dist) {
		// TODO: Fix this to account for legality of paths.
		Vertex bestVertex = null;
		double maxValue = 0;
		for (BoardCoordinate c : _v.keySet()) {
			if (c.distance(center) <= dist && _v.get(c).isLegal(p) && _v.get(c).value() > maxValue) {
				bestVertex = _v.get(c);
				maxValue = _v.get(c).value();
			}
		}
		return bestVertex;
	}
	
	public List<Edge> shortestLegalPath(Player p, Vertex a, Vertex b) {
		// TODO: Implement this.
		ArrayList<Edge> path = new ArrayList<Edge>();
		
		return path;
	}
	
	public List<Edge> shortestLegalPathFromPlayer(Player p, Vertex v) {
		// TODO: Implement this.
		ArrayList<Edge> path = new ArrayList<Edge>();
		
		return path;
	}
	
	public boolean placeRoad(Player p, Edge target) {
		if (! _e.contains(target)) return false;
		else return target.build(p);
	}
	
	public boolean placeSettlement(Player p, Vertex target) {
		if (! _v.contains(target)) return false;
		else return target.build(p);
	}
	
	public boolean placeCity(Player p, Vertex target) {
		if (! _v.contains(target)) return false;
		else return target.upgrade(p);
	}
	
	public Edge getEdgeByInt(int v_i, int v_j) {
		// TODO: Implement this.
		for (Edge e : _v.get(BoardCoordinate.ORIGIN).edges()) return e;
		return null;
	}
	
	public Vertex getVertexByInt(int v_i) {
		// TODO: Implement this.
		return _v.get(BoardCoordinate.ORIGIN);
	}
}
