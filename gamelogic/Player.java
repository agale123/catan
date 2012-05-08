package gamelogic;

import java.lang.Integer;
import java.util.*;
import catanui.*;

/**
Represents one of the game's players
*/
public class Player {

	private ArrayList<catanui.BoardObject.type>	_hand;
	private ArrayList<Edge>				_roads;
	private ArrayList<Vertex>			_settlements;
	private ArrayList<Vertex>			_cities;
	private ArrayList<BoardObject.type> 		_ports;
	private int 	_numCards	= 0;
	private int	_numDevCards 	= 0;
	private int	_points	 	= 0;
	private int	_numRds		= 0;
	private int	_devCards	= 0;
	private int	_playerNum;
	private boolean _lostConnection;
	
	public Player(int num) {
		_playerNum = num;
		_hand = new ArrayList<catanui.BoardObject.type>();
		_roads = new ArrayList<Edge>();
		_settlements = new ArrayList<Vertex>();
		_cities = new ArrayList<Vertex>();
		_ports = new ArrayList<BoardObject.type>();
		_lostConnection = false;
	}

	//built a settlement
	public void addSettlement(Vertex v) {
		_settlements.add(v);
		_points++;
		if (v.getPort() != null) {
		    _ports.add(v.getPort());
		}
	}
	
	public void removeSettlement(Vertex v) {
		_settlements.remove(_settlements.indexOf(v));
	}
	
	//if connection to server was lost
	public boolean isLostConnection() {
		return _lostConnection;
	}
	
	public void setLostConnection(boolean b) {
		_lostConnection = b;
	}
	
	//built a road
	public void addRoad(Edge e) {
		_roads.add(e);
		_numRds ++;
	}
	
	//built a city
	public void addCity(Vertex v) {
		_cities.add(v);
		_settlements.remove(v);
		_points ++;
	}
	
	//whether the player has a settlement on a specific vertex
	public boolean hasSettlement(Vertex v) {
		return _settlements.contains(v);
	}
	
	//gets a card added to their hand
	public void addCard(catanui.BoardObject.type c) {
	    _hand.add(c);
	    _numCards ++;
	}
	
	//bought a development card
	public void addDevCard() {
	    _devCards++;
	}
	
	//used one of their development cards
	public void removeDevCard() {
	    _devCards--;
	}
	
	//used or lost a card in their hand
	public boolean removeCard(catanui.BoardObject.type c) {
		boolean b = _hand.remove(c);
		if (!b) {
		    return false;
		}
	    _numCards --;
	    return true;
	}
	
	//got or lost largest road network
	public void updateLongestRd(int x) {
		_points += x;
	}
	
	//got a point
	public void addPoint() {
		_points++;
	}
	
	public ArrayList<Edge> getRoads() { return _roads; }
	public int getnumPoints() { return _points; }
	public int getnumRds() { return _numRds; }
	public ArrayList<Vertex> getSettlements() { return _settlements; }
	public ArrayList<catanui.BoardObject.type> getHand() { return _hand; }
	public ArrayList<BoardObject.type> getPorts() { return _ports; }
	public int getnumDevCards() { return _devCards; }
}
