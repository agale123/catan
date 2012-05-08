package catanai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class GameBoard implements AIConstants {
	private Map<BoardCoordinate, Vertex> _v;
	private Set<Edge> _e;
	private Map<BoardCoordinate, Tile> _t;

	public GameBoard() {
		this(false);
	}

	public GameBoard(boolean extended) {
		_v = new HashMap<BoardCoordinate, Vertex>();
		_e = new HashSet<Edge>();
		_t = new HashMap<BoardCoordinate, Tile>();
		Vertex current_v;
		HashSet<BoardCoordinate> vertices = (extended)? VALID_VERTS_EXP:VALID_VERTS;
		for (BoardCoordinate c : vertices) {
			current_v = new Vertex(new HashSet<Edge>(), new HashSet<Tile>());
			current_v.setLocation(c);
			_v.put(c, current_v);
		}
		int rem = MAX_ADJ_TILES;
		Edge current_e;
		HashSet<BoardCoordinate> done = new HashSet<BoardCoordinate>();
		l0: for (BoardCoordinate c : _v.keySet()) {
			for (BoardCoordinate d : _v.keySet()) {
				if (done.contains(d)) continue;
				if (c.distance(d) == 1) {
					current_e = new Edge(new HashSet<Vertex>());
					current_e.addEnd(_v.get(c));
					current_e.addEnd(_v.get(d));
					_v.get(c).addEdge(current_e);
					_v.get(d).addEdge(current_e);
					_e.add(current_e);
				}
				if (rem == 0) break l0;
			}
			done.add(c);
		}
		// Populate the tile set.
		int tile_rem = (extended)? NUM_TILES_EXP:NUM_TILES;
		if (! extended) {
			for (BoardCoordinate c : _v.keySet()) {
				if (tile_rem == 0) break;
				if (c.moveIn(DIM_Z, true) == null || c.moveIn(DIM_X, true) == null) continue;
				try {
					if (c.moveIn(DIM_X, true).moveIn(DIM_Y, true).moveIn(DIM_Z, true) == null)
						continue;
				}
				catch (NullPointerException e) {
					continue;
				}
				Tile t = new Tile(new HashSet<Vertex>());
				t.addVertex(_v.get(c));
				t.addVertex(_v.get(c.moveIn(DIM_X, true)));
				t.addVertex(_v.get(c.moveIn(DIM_Z, true)));
				t.addVertex(_v.get(c.moveIn(DIM_X, true).moveIn(DIM_Y, true)));
				t.addVertex(_v.get(c.moveIn(DIM_Z, true).moveIn(DIM_Y, true)));
				t.addVertex(_v.get(c.moveIn(DIM_X, true).moveIn(DIM_Y, true).moveIn(DIM_Z, true)));
				_t.put(c, t);
				_v.get(c).addTile(t);
				_v.get(c.moveIn(DIM_X, true)).addTile(t);
				_v.get(c.moveIn(DIM_Z, true)).addTile(t);
				_v.get(c.moveIn(DIM_X, true).moveIn(DIM_Y, true)).addTile(t);
				_v.get(c.moveIn(DIM_Z, true).moveIn(DIM_Y, true)).addTile(t);
				_v.get(c.moveIn(DIM_X, true).moveIn(DIM_Y, true).moveIn(DIM_Z, true)).addTile(t);
				tile_rem--;
			}
		}
		else {
			for (BoardCoordinate c : _v.keySet()) {
				if (tile_rem == 0) break;
				if (c.moveInExp(DIM_Z, true) == null || c.moveInExp(DIM_X, true) == null) continue;
				try {
					if (c.moveInExp(DIM_X, true).moveInExp(DIM_Y, true).moveInExp(DIM_Z, true) == null)
						continue;
				}
				catch (NullPointerException e) {
					continue;
				}
				Tile t = new Tile(new HashSet<Vertex>());
				t.addVertex(_v.get(c));
				t.addVertex(_v.get(c.moveInExp(DIM_X, true)));
				t.addVertex(_v.get(c.moveInExp(DIM_Z, true)));
				t.addVertex(_v.get(c.moveInExp(DIM_X, true).moveInExp(DIM_Y, true)));
				t.addVertex(_v.get(c.moveInExp(DIM_Z, true).moveInExp(DIM_Y, true)));
				t.addVertex(_v.get(c.moveInExp(DIM_X, true).moveInExp(DIM_Y, true).moveInExp(DIM_Z, true)));
				_t.put(c, t);
				_v.get(c).addTile(t);
				_v.get(c.moveInExp(DIM_X, true)).addTile(t);
				_v.get(c.moveInExp(DIM_Z, true)).addTile(t);
				_v.get(c.moveInExp(DIM_X, true).moveInExp(DIM_Y, true)).addTile(t);
				_v.get(c.moveInExp(DIM_Z, true).moveInExp(DIM_Y, true)).addTile(t);
				_v.get(c.moveInExp(DIM_X, true).moveInExp(DIM_Y, true).moveInExp(DIM_Z, true)).addTile(t);
				tile_rem--;
			}
		}
	}

	public void getResourceInfo(gamelogic.PublicGameBoard pub) {
		List<catanui.BoardObject.type> data = pub.resData();
		for (int i = 0; i < data.size(); i++) {
			Tile active = getTileByInt(i);
			if (active == null) {
				System.out.println("getResourceInfo is halting prematurely at " + Integer.toString(i)); // TODO: Debug line
				return;
			}
			switch (data.get(i)) {
			case WHEAT:
				active.setType(TileType.Wheat);
				break;
			case ORE:
				active.setType(TileType.Ore);
				break;
			case WOOD:
				active.setType(TileType.Timber);
				break;
			case SHEEP:
				active.setType(TileType.Sheep);
				break;
			case BRICK:
				active.setType(TileType.Brick);
				break;
			default:
				active.setType(TileType.Desert);
				break;
			}
		}
	}

	/**
	 * getRollInfo: Feeds tile roll info to the board.
	 * @param pub: The PublicGameBoard from which the information is taken.
	 */
	public void getRollInfo(gamelogic.PublicGameBoard pub) {
		List<Integer> data = pub.rollData();
		for (int i = 0; i < data.size(); i++) {
			Tile active = getTileByInt(i);
			if (active == null) {
				System.out.println("getRollInfo is halting prematurely at " + Integer.toString(i)); // TODO: Debug line
				return;
			}
			if (active.resource() == TileType.Desert || data.get(i) < 2 || data.get(i) > 12) continue;
			active.setRoll(data.get(i));
		}
	}

	/**
	 * mostValuableLegalVertex
	 * @param p: Player for whom legality is determined
	 * @return: Returns the most valuable legal vertex for p on the board.
	 */
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
		Vertex bestVertex = null;
		double maxValue = 0;
		for (BoardCoordinate c : _v.keySet()) {
			if (c.distance(center) <= dist && _v.get(c).isLegal(p) && 
					_v.get(c).value() > maxValue &&
					shortestLegalPath(p, _v.get(center), _v.get(c)).size() <= dist) {
				bestVertex = _v.get(c);
				maxValue = _v.get(c).value();
			}
		}
		return bestVertex;
	}

	/**
	 * shortestLegalPath: Uses Dijkstra's algorithm to determine shortest path.
	 * @param p: The player for whom legality is determined.
	 * @param a: The source vertex
	 * @param b: The destination vertex
	 * @return: Returns a list containing the edges of the path, beginning with the first edge from the source.
	 * Returns null if no legal path exists.
	 */
	public List<Edge> shortestLegalPath(Player p, Vertex a, Vertex b) {
		if (! (_v.containsValue(a) && _v.containsValue(b))) {
			System.out.println("shortestLegalPath fails sanity check"); // TODO: Debug line
			return null;
		}
		HashMap<Vertex, Vertex> previous = new HashMap<Vertex, Vertex>();
		HashMap<Vertex, Integer> dist = new HashMap<Vertex, Integer>();
		HashSet<Vertex> unexp = new HashSet<Vertex>(_v.values());
		dist.put(a, 0);
		Vertex active;
		int s_dist;
		while (! unexp.isEmpty()) {
			active = null;
			s_dist = 1000000;
			for (Vertex v : dist.keySet()) {
				if (dist.get(v) < s_dist && unexp.contains(v)) {
					active = v;
					s_dist = dist.get(v);
				}
			}
			if (active == null) return null;
			else if (active.equals(b)) break;
			unexp.remove(active);
			for (Vertex v : active.neighbors()) {
				if (active.edgeTo(v) == null) System.out.println("No edge between " + active.toString() + " and " + v.toString()); // TODO: Debug line
				if ((! unexp.contains(v)) || (v.controller() != null && ! v.controller().equals(p)) ||
						(active.edgeTo(v).road() && 
								active.edgeTo(v).controller().equals(p))) continue;
				if ((! dist.containsKey(v)) || dist.get(v) > dist.get(active) + 1) {
					dist.put(v, dist.get(active) + 1);
					previous.put(v, active);
				}
			}
		}
		Stack<Edge> s = new Stack<Edge>();
		active = b;
		while (previous.containsKey(active)) {
			s.push(active.edgeTo(previous.get(active)));
			active = previous.get(active);
		}
		ArrayList<Edge> res = new ArrayList<Edge>();
		while (! s.isEmpty()) res.add(s.pop());
		return res;
	}

	/**
	 * shortestLegalPathFromPlayer
	 * @param p: The player for whom legality is determined
	 * @param v: The destination vertex
	 * @return: Returns the shortest legal path from the road network of p to v.
	 * Returns null if no legal path exists.
	 */
	public List<Edge> shortestLegalPathFromPlayer(Player p, Vertex v) {
		List<Edge> path = null, t;
		for (Vertex v0 : p.vertOnNetwork()) {
			t = shortestLegalPath(p, v0, v);
			if (t != null && (path == null || t.size() < path.size())) path = t;
		}
		return path;
	}

	public boolean placeRoad(Player p, Edge target) {
		if (! _e.contains(target)) return false;
		else {
			p.addRoad(target);
			return target.build(p);
		}
	}

	public boolean placeSettlement(Player p, Vertex target) {
		if (! _v.containsValue(target)) {
			System.out.println("Settlement not placed because board cannot locate the target."); // TODO: Debug line
			return false;
		}
		else {
			p.addSettlement(target);
			return target.build(p);
		}
	}

	public boolean placeInitialSettlement(Player p, Vertex target) {
		if (! _v.containsValue(target)) {
			System.out.println("Settlement not placed because board cannot locate the target."); // TODO: Debug line
			return false;
		}
		else return target.buildInitial(p);
	}

	public boolean placeCity(Player p, Vertex target) {
		if (! _v.containsValue(target)) return false;
		else {
			p.addCity(target);
			return target.upgrade(p);
		}
	}

	public void moveRobber(Tile target) {
		if (! _t.containsValue(target)) return;
		for (Tile t : _t.values()) {
			if (t.isRobbed()) {
				t.removeRobber();
				break;
			}
		}
		target.setRobber();
	}

	public Edge getEdgeByInt(int v_i, int v_j) {
		Vertex i = getVertexByInt(v_i);
		Vertex j = getVertexByInt(v_j);
		if (i != null && j != null && i.neighbors().contains(j)) {
			Edge e = null;
			for (Edge e0 : i.edges()) {
				if (e0.ends().contains(j)) {
					e = e0;
					break;
				}
			}
			return e;
		}
		else return null;
	}

	public Vertex getVertexByInt(int v_i) {
		int x = FLOOR_X - 1, y = FLOOR_Y - 1, z = FLOOR_Z - 1;
		for (int x_ : X_GROUPS.keySet()) {
			if (X_GROUPS.get(x_).contains(v_i)) {
				x = x_;
				break;
			}
		}
		if (x < FLOOR_X) return null;
		for (int y_ : Y_GROUPS.keySet()) {
			if (Y_GROUPS.get(y_).contains(v_i)) {
				y = y_;
				break;
			}
		}
		if (y < FLOOR_Y) return null;
		for (int z_ : Z_GROUPS.keySet()) {
			if (Z_GROUPS.get(z_).contains(v_i)) {
				z = z_;
				break;
			}
		}
		if (z < FLOOR_Z) return null;
		BoardCoordinate c = new BoardCoordinate(x, y, z);
		if (_v.containsKey(c)) return _v.get(c);
		else return null;
	}

	public Tile getTileByInt(int t_i) {
		if (t_i < 0) return null;
		else if (t_i < 3) return _t.get(new BoardCoordinate(2 + t_i, t_i, -2));
		else if (t_i < 7) return _t.get(new BoardCoordinate(t_i - 2, t_i - 3, -1));
		else if (t_i < 12) return _t.get(new BoardCoordinate(t_i - 7, t_i - 7, 0));
		else if (t_i < 16) return _t.get(new BoardCoordinate(t_i - 12, t_i - 11, 1));
		else if (t_i < 19) return _t.get(new BoardCoordinate(t_i - 16, t_i - 14, 2));
		else return null;
	}
}
