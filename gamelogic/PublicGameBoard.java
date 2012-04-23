package gamelogic;

import java.util.*;

public class PublicGameBoard {
	
	private ArrayList<Vertex>	_vertices;
	private ArrayList<Hex>		_hexes;
	private ArrayList<Edge>		_edges;
	private ArrayList<Player>	_players;
	boolean _firstRound = true;
	private int _longestRd = 4;
	private int _longestRd_Owner = -1;
	private int _largestArmy = 2;
	private int _largestArmy_Owner = -1;
	private int _robberLoc = 1;
	
	public PublicGameBoard(ArrayList<Vertex> points, ArrayList<Hex> hexes, ArrayList<Edge> edges, ArrayList<Player> players) {
		_vertices = points;
		_hexes = hexes;
		_edges = edges;
		_players = players;
		setupTestBoard();
	}
	
	public void setFirstRoundOver() {
		_firstRound = false;
	}
	
	public boolean canBuildSettlement(int p, int v) {
		if ((_vertices.get(v).getObject() != 0) || //if point already full
				(_vertices.get(v).isBuildable() == false)) { //if not 2 away from other object
			return false;
		}
		if (_firstRound) {
			return true;
		}
		for (Edge e : _players.get(p).getRoads()) {
			if (e.getStartV() == v || e.getEndV() == v) { //if player has road connected
				return true;
			}
		}
		return false;
	}
	
	public void buildSettlement(int p, int v) {
		if (_firstRound) {
			_players.get(p).addSettlement(_vertices.get(v));
			_vertices.get(v).setObject(1);
			_vertices.get(v).setOwner(p);
		}else {
			_players.get(p).addSettlement(_vertices.get(v));
			_vertices.get(v).setObject(1);
			_vertices.get(v).setOwner(p);
			updateLongestRd(p);
		}
	}
	
	public boolean canBuildRoad(int p, int e) {
		if (_edges.get(e).hasRoad()) {//if edge already has road 
			return false;	
		}
		if (_firstRound) {
			if (_vertices.get(_edges.get(e).getStartV()).getOwner() == p || 
					_vertices.get(_edges.get(e).getEndV()).getOwner() == p) {
				return true;
			}
		}
		for (Edge i : _players.get(p).getRoads()) {
			if (i.getStartV() == _edges.get(e).getStartV() || i.getStartV() == _edges.get(e).getEndV() ||
					i.getEndV() == _edges.get(e).getStartV() || i.getEndV() == _edges.get(e).getEndV()) {
				//if new road connected to old road
				return true;
			}
		}
		return false;
	}
	
	public void buildRoad(int p, int e) {
		if (_firstRound) {
			_players.get(p).addRoad(_edges.get(e));
			_edges.get(e).setRoad();
		} else {
			_players.get(p).addRoad(_edges.get(e));
			_edges.get(e).setRoad();
		}
	}
	
	public boolean canBuildCity(int p, int v) {
		if (_vertices.get(v).getObject() != 1 || //if no settlement on vertex
				!_players.get(p).hasSettlement(_vertices.get(v))) { //if settlement belongs to player
				return false;
		}
		return true;
	}
	
	public void buildCity(int p, int v) {
		_players.get(p).addCity(_vertices.get(v));
		_vertices.get(v).setObject(2);
	}
	
	public boolean playDevCard(int p, int cardID) {
		return true;
	}
	
	public boolean makeTrade(int p1, int p2, 
					ArrayList<Integer> cards1, ArrayList<Integer> cards2) {
		boolean b1 = _players.get(p1).removeCards(cards1);
		boolean b2 = _players.get(p2).removeCards(cards2);
		if (!b1 || !b2) {
			return false;
		}
		_players.get(p1).addCards(cards2);
		_players.get(p2).addCards(cards1);
		return true;
	}
	
	public void diceRolled(int roll) {
		if (roll == 7) {
			moveRobber();
		}
		else {
			for (Hex h : _hexes) {
				if (h.getRollNum() == roll && h.getNum() != _robberLoc) {
					for (Integer i : h.getVertices()) {
						int p = _vertices.get(i).getOwner();
						if (p != -1) {
							_players.get(p).addCards(new ArrayList(Arrays.asList(h.getResource())));
							if (_vertices.get(i).getObject() == 2)  { //if city
								_players.get(p).addCards(new ArrayList(Arrays.asList(h.getResource())));
							}
						}
					}
				}
			}
		}
	}
	
	public void updateLongestRd(int p) {
		if (_players.get(p).getnumRds() > _longestRd) {
			if (_longestRd_Owner != -1) {
				_players.get(_longestRd_Owner).updateLongestRd(-2);
			}
			_players.get(p).updateLongestRd(2);
			_longestRd_Owner = p;
		}
	}
	
	public void moveRobber() {
		Random rand = new Random();
		int num = 0;
		while (num != _robberLoc || num == 0) {
			num = rand.nextInt(_hexes.size() + 1);
		}
		_robberLoc = num;
	}
	//TODO: send robber info
	//TODO: getstate method = returns string with # of hexes and each's number
	
	
	
	
	
	
	public void setupTestBoard() {
		_vertices = new ArrayList<Vertex>();
		for (int i = 0; i<13; i++) {
			_vertices.add(new Vertex(i));
		}
		
		_vertices.get(0).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(11), _vertices.get(1))));
		_vertices.get(1).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(0), _vertices.get(2), _vertices.get(12))));
		_vertices.get(2).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(1), _vertices.get(3))));
		_vertices.get(3).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(2), _vertices.get(4))));
		_vertices.get(4).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(3), _vertices.get(5))));
		_vertices.get(5).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(4), _vertices.get(6), _vertices.get(12))));
		_vertices.get(6).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(5), _vertices.get(7))));
		_vertices.get(7).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(6), _vertices.get(8))));
		_vertices.get(8).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(9), _vertices.get(7))));
		_vertices.get(9).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(8), _vertices.get(10), _vertices.get(12))));
		_vertices.get(10).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(9), _vertices.get(11))));
		_vertices.get(11).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(10), _vertices.get(0))));
		_vertices.get(12).setNeighbors(new ArrayList(Arrays.asList(_vertices.get(1), _vertices.get(5), _vertices.get(9))));
		
		_edges = new ArrayList<Edge>();
		_edges.add(new Edge(11, 0));
		_edges.add(new Edge(0, 1));
		_edges.add(new Edge(1, 2));
		_edges.add(new Edge(2, 3));
		_edges.add(new Edge(3, 4));
		_edges.add(new Edge(4, 5));
		_edges.add(new Edge(5, 6));
		_edges.add(new Edge(6, 7));
		_edges.add(new Edge(7, 8));
		_edges.add(new Edge(8, 9));
		_edges.add(new Edge(9, 10));
		_edges.add(new Edge(10, 11));
		_edges.add(new Edge(9, 12));
		_edges.add(new Edge(12, 1));
		_edges.add(new Edge(12, 5));
		
		_hexes = new ArrayList<Hex>();
		_hexes.add(new Hex(0, 3, 0, new ArrayList(Arrays.asList(0,1,12,9,10,11))));
		_hexes.add(new Hex(1, 4, 1, new ArrayList(Arrays.asList(1,2,3,4,5,12))));
		_hexes.add(new Hex(2, 5, 2, new ArrayList(Arrays.asList(12,5,6,7,8,9))));
		
		_players = new ArrayList<Player>();
		_players.add(new Player(0));
		_players.add(new Player(1));
	}
}
