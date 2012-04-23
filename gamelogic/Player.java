package gamelogic;

import java.lang.Integer;
import java.util.*;

public class Player {

	private ArrayList<Integer>	_hand;
	private ArrayList<Edge>	_roads;
	private ArrayList<Vertex>	_settlements;
	private ArrayList<Vertex>	_cities;
	private ArrayList<Integer>  _unplayedDevCards;
	private int _numCards	 	= 0;
	private int	_numDevCards 	= 0;
	private int	_points	 	= 0;
	private int	_numRds			= 0;
	private int	_numKnights		= 0;
	private int	_playerNum;
	
	public Player(int num) {
		_playerNum = num;
		_hand = new ArrayList<Integer>();
		_roads = new ArrayList<Edge>();
		_settlements = new ArrayList<Vertex>();
		_cities = new ArrayList<Vertex>();
		_unplayedDevCards = new ArrayList<Integer>();
	}

	public void addSettlement(Vertex v) {
		_settlements.add(v);
		_points++;
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
	
	public void addCards(ArrayList<Integer> cards) {
		for (Integer i : cards) {
			_hand.add(i);
			_numCards ++;
		}
	}
	
	public boolean removeCards(ArrayList<Integer> cards) {
		for (Integer i : cards) {
			boolean b = _hand.remove(i);
			_numCards --;
			if (!b) {
				return false;
			}
		}
		return true;
	}
	
	public void updateLongestRd(int x) {
		_points += x;
	}
	
	public ArrayList<Edge> getRoads() { return _roads; }
	public int getnumPoints() { return _points; }
	public int getnumRds() { return _numRds; }
	public ArrayList<Vertex> getSettlements() { return _settlements; }
	public ArrayList<Integer> getHand() { return _hand; }
}
