package gamelogic;

import java.lang.Integer;
import java.util.*;
import catanui.*;

public class Player {

	private ArrayList<catanui.BoardObject.type>	_hand;
	private ArrayList<Edge>				_roads;
	private ArrayList<Vertex>			_settlements;
	private ArrayList<Vertex>			_cities;
	private ArrayList<BoardObject.type> 			_ports;
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

	public void addSettlement(Vertex v) {
		_settlements.add(v);
		_points++;
		if (v.getPort() != null) {
		    _ports.add(v.getPort());
		}
	}
	
	public boolean isLostConnection() {
		return _lostConnection;
	}
	
	public void setLostConnection(boolean b) {
		_lostConnection = b;
	}
	
	public void addRoad(Edge e) {
		_roads.add(e);
		_numRds ++;
	}
	
	public void addCity(Vertex v) {
		_cities.add(v);
		_settlements.remove(v);
		_points ++;
	}
	
	public boolean hasSettlement(Vertex v) {
		return _settlements.contains(v);
	}
	
	public void addCard(catanui.BoardObject.type c) {
	    _hand.add(c);
	    _numCards ++;
	}
	
	public void addDevCard() {
	    _devCards++;
	}
	
	public void removeDevCard() {
	    _devCards--;
	}
	
	public boolean removeCard(catanui.BoardObject.type c) {
		boolean b = _hand.remove(c);
		if (!b) {
		    return false;
		}
	    _numCards --;
	    return true;
	}
	
	public void updateLongestRd(int x) {
		_points += x;
	}
	
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
