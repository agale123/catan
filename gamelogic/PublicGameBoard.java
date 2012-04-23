package gamelogic;

import java.util.*;

public class PublicGameBoard {
	
	private ArrayList<Vertex>	_points;
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
		_points = points;
		_hexes = hexes;
		_edges = edges;
		_players = players;
	}
	
	public void setFirstRoundOver() {
		_firstRound = false;
	}
	
	public boolean canBuildSettlement(int p, int v) {
		if ((_points.get(v).getObject() != 0) || //if point already full
				(_points.get(v).isBuildable() == false)) { //if not 2 away from other object
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
			_players.get(p).addSettlement(_points.get(v));
			_points.get(v).setObject(1);
			_points.get(v).setOwner(p);
		}else {
			_players.get(p).addSettlement(_points.get(v));
			_points.get(v).setObject(1);
			_points.get(v).setOwner(p);
			updateLongestRd(p);
		}
	}
	
	public boolean canBuildRoad(int p, int e) {
		if (_edges.get(e).hasRoad()) {//if edge already has road 
			return false;	
		}
		if (_firstRound) {
			if (_points.get(_edges.get(e).getStartV()).getOwner() == p || 
					_points.get(_edges.get(e).getEndV()).getOwner() == p) {
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
		if (_points.get(v).getObject() != 1 || //if no settlement on vertex
				!_players.get(p).hasSettlement(_points.get(v))) { //if settlement belongs to player
				return false;
		}
		return true;
	}
	
	public void buildCity(int p, int v) {
		_players.get(p).addCity(_points.get(v));
		_points.get(v).setObject(2);
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
						int p = _points.get(i).getOwner();
						if (p != -1) {
							_players.get(p).addCards(new ArrayList(Arrays.asList(h.getResource())));
							if (_points.get(i).getObject() == 2)  { //if city
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
}
