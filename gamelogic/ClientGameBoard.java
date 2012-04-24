package gamelogic;

import java.util.*;
import java.lang.*;


public class ClientGameBoard {

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
	client.Client _client;
	
	public ClientGameBoard(ArrayList<Vertex> points, ArrayList<Hex> hexes, ArrayList<Edge> edges, ArrayList<Player> players, client.Client client) {
		_client = client;
		_vertices = points;
		_hexes = hexes;
		_edges = edges;
		_players = players;
	}
	
	public void setFirstRoundOver() {
		_firstRound = false;
	}

	public void writeBuldSettlement(int p, int v) {
		_client.sendRequest(2, Integer.toString(p) + "," + Integer.toString(v));
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
	
	public void writeBuildRoad(int p, int e) {
		_client.sendRequest(1, Integer.toString(p) + "," + Integer.toString(e));
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
	
	public void writeBuildCity(int p, int v) {
		_client.sendRequest(3, Integer.toString(p) + "," + Integer.toString(v));
	}
	
	public void buildCity(int p, int v) {
		_players.get(p).addCity(_vertices.get(v));
		_vertices.get(v).setObject(2);
	}
	
	public void writeMakeTrade(int p1, int p2, int c1, int c2, int c3, int c4) {
		_client.sendRequest(4, Integer.toString(p1) + "," + Integer.toString(p2) + "," + Integer.toString(c1) + "," + 
					    Integer.toString(c2) + "," + Integer.toString(c3) + "," + Integer.toString(c4));
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
		System.out.println("Rolled: " + roll);
		if (roll != 7) {
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
	
	public void moveRobber(int hexnum) {
		_robberLoc = hexnum;
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
}
