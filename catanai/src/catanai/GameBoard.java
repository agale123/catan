package catanai;

import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;

public class GameBoard implements AIConstants {
	private Hashtable<BoardCoordinate, Vertex> _v;
	private Set<Edge> _e;
	private Set<Tile> _t;
	
	public GameBoard() {
		this(false);
	}
	
	public GameBoard(boolean extended) {
		// TODO: Add support for extended boards.
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
		for (BoardCoordinate c : _v.keySet()) {
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
				if (rem == 0) break;
			}
			done.add(c);
		}
		// TODO: Populate the tile set.
	}
	
	public Vertex mostValuableLegalVertex() {
		return mostValuableLegalVertex(new BoardCoordinate(0, 0, 0), CEIL_X + CEIL_Y + CEIL_Z);
	}
	
	public Vertex mostValuableLegalVertex(BoardCoordinate center, int dist) {
		Vertex bestVertex = null;
		double maxValue = 0;
		for (BoardCoordinate c : _v.keySet()) {
			if (c.distance(center) <= dist && _v.get(c).value() > maxValue) {
				bestVertex = _v.get(c);
				maxValue = _v.get(c).value();
			}
		}
		return bestVertex;
	}
	
	public boolean placeRoad(Edge target) {
		// TODO: Finish this.
		return false;
	}
	
	public boolean placeSettlement(Vertex target) {
		// TODO: Finish this.
		return false;
	}
	
	public boolean placeCity(Vertex target) {
		// TODO: Finish this.
		return false;
	}
}
