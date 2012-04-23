package Catan;

import java.util.*;

public class PublicGameBoard {
	
	private ArrayList<Vertex>	_points;
	private ArrayList<Hex>		_hexes;
	private ArrayList<Edge>		_edges;
	boolean _firstRound = true;
	private int _longestRd = 4;
	private Player _longestRd_Owner = null;
	private int _largestArmy = 2;
	private Player _largestArmy_Owner = null;
	private int _robberLoc = 1;
	
	public PublicGameBoard(ArrayList<Vertex> points, ArrayList<Hex> hexes, ArrayList<Edge> edges) {
		_points = points;
		_hexes = hexes;
		_edges = edges;
	}
	
	public void setFirstRoundOver() {
		_firstRound = false;
	}
	
	public boolean buildSettlement(Player p, int v) {
		if ((_points.get(v).getObject() != 0) || //if point already full
				(_points.get(v).isBuildable() == false)) { //if not 2 away from other object
			return false;
		}
		if (_firstRound) {
			p.addSettlement(_points.get(v));
			_points.get(v).setObject(1);
			_points.get(v).setOwner(p);
			return true;
		}
		for (Edge e : p.getRoads()) {
			if (e.getStartV() == v || e.getEndV() == v) { //if player has road connected
				p.addSettlement(_points.get(v));
				_points.get(v).setObject(1);
				_points.get(v).setOwner(p);
				updateLongestRd(p);
				return true;
			}
		}
		return false;
	}
	
	public boolean buildRoad(Player p, int e) {
		if (_edges.get(e).hasRoad()) {//if edge already has road 
			return false;	
		}
		if (_firstRound) {
			if (_points.get(_edges.get(e).getStartV()).getOwner() == p || 
					_points.get(_edges.get(e).getEndV()).getOwner() == p) {
				p.addRoad(_edges.get(e));
				_edges.get(e).setRoad();
				return true;
			}
		}
		for (Edge i : p.getRoads()) {
			if (i.getStartV() == _edges.get(e).getStartV() || i.getStartV() == _edges.get(e).getEndV() ||
					i.getEndV() == _edges.get(e).getStartV() || i.getEndV() == _edges.get(e).getEndV()) {
				//if new road connected to old road
				p.addRoad(_edges.get(e));
				_edges.get(e).setRoad();
				return true;
			}
		}
		return false;
	}
	
	public boolean buildCity(Player p, int v) {
		if (_points.get(v).getObject() != 1 || //if no settlement on vertex
			!p.hasSettlement(_points.get(v))) { //if settlement belongs to player
			return false;
		}
		p.addCity(_points.get(v));
		_points.get(v).setObject(2);
		return true;
	}
	
	public boolean playDevCard(Player p, int cardID) {
		return true;
	}
	
	public boolean makeTrade(Player p1, Player p2, 
					ArrayList<Integer> cards1, ArrayList<Integer> cards2) {
		boolean b1 = p1.removeCards(cards1);
		boolean b2 = p2.removeCards(cards2);
		if (!b1 || !b2) {
			return false;
		}
		p1.addCards(cards2);
		p2.addCards(cards1);
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
						Player p = _points.get(i).getOwner();
						if (p != null) {
							p.addCards(new ArrayList(Arrays.asList(h.getResource())));
							if (_points.get(i).getObject() == 2)  { //if city
								p.addCards(new ArrayList(Arrays.asList(h.getResource())));
							}
						}
					}
				}
			}
		}
	}
	
	public void updateLongestRd(Player p) {
		if (p.getnumRds() > _longestRd) {
			if (_longestRd_Owner != null) {
				_longestRd_Owner.updateLongestRd(-2);
			}
			p.updateLongestRd(2);
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
