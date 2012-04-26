package gamelogic;

import java.util.*;
import java.lang.*;


public class ClientGameBoard {

	private ArrayList<Vertex> _vertices;
	private ArrayList<Hex> _hexes;
	private ArrayList<Edge> _edges;
	private ArrayList<Player> _players;
	boolean _firstRound = true;
	private int _longestRd = 4;
	private int _longestRd_Owner = -1;
	private int _largestArmy = 2;
	private int _largestArmy_Owner = -1;
	private int _robberLoc = 1;
	private HashMap<CoordPair, Integer> _coordMap;
	client.Client _client;
	
	public ClientGameBoard(int numPlayer, client.Client client) {
		_client = client;
		_hexes = new ArrayList<Hex>();
		_players = new ArrayList<Player>();
		_coordMap = new HashMap<CoordPair, Integer>();
		
		for (int i = 0; i<numPlayers; i++) {
		    _players.add(new Player(i));
		}
		setUpBoard(numPlayers);
	}
	
	public void setUpBoard(int numPlayers) {
	    //make hexes
	    ArrayList<Integer> colSizes;
	    ArrayList<Integer> startY;
	    if (numPlayers <= 4) {
		colSizes = new ArrayList<Integer>(Arrays.asList(3, 4, 5, 4, 3));
		startY = new ArrayList<Integer>(Arrays.asList(3, 2, 1, 2, 3));
	    } else if (numPlayers == 5 || numPlayers == 6) {
		colSizes = new ArrayList<Integer>(Arrays.asList(3, 4, 5, 6, 5, 4, 3));
		startY = new ArrayList<Integer>(Arrays.asList(4, 3, 2, 1, 2, 3, 4));
	    } else {
		colSizes = new ArrayList<Integer>(Arrays.asList(3, 4, 5, 6, 7, 6, 5, 4, 3));
		startY = new ArrayList<Integer>(Arrays.asList(5, 4, 3, 2, 1, 2, 3, 4, 5));
	    }
	    double currx = -0.5;
	    double curry;
	    int hexCount = 0;
	    for (int i=0; i<colSizes.size(); i++) {
		currx += 2;
		curry = startY.get(i);
		for (int x=0; x<colSizes.get(i); x++) {
		    Hex hex = new Hex(hexCount, currx, curry)l
		    _hexes.add(hex);
		    hexCount++;
		    
		    ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		    vertices.add(new Vertex(currx-1, curry));
		    vertices.add(new Vertex(currx-.5, curry-1));
		    vertices.add(new Vertex(currx+.5, curry-1));
		    vertices.add(new Vertex(currx+1, curry));
		    vertices.add(new Vertex(currx+.5, curry+1));
		    vertices.add(new Vertex(currx-.5, curry+1));
		    hex.setVertices(vertices);
		    
		    for (int z=0; z<(vertices.size()); z++) {
			if (!_vertices.contains(vertices.get(z)) {
			    _vertices.add(vertices.get(z));
			    _coordMap.put(new CoordPair(vertices.get(z).getX(), vertices.get(z).getY()), 
						    new Integer(_vertices.indexOf(vertices.get(z))));
			}
			if (z == 5) {
			    Edge edge = new Edge(vertices.get(z), vertices.get(0));
			    if (!_edges.contains(edge)) {
				_edges.add(edge);
			    }
			} else {
			    Edge edge = new Edge(vertices.get(z), vertices.get(z+1));
			    if (!_edges.contains(edge)) {
				_edges.add(edge);
			    }
			}
		    } 
		    curry += 2;
		}
	    }
	}
	
	public void setFirstRoundOver() {
		_firstRound = false;
	}

	public void writeBuldSettlement(int p, double vx, double vy) {
		_client.sendRequest(2, Integer.toString(p) + "," + 
			Double.toString(vx) + "," + Double.toString(vy));
	}
	
	public void buildSettlement(int p, double vx, double vy) {
	    int x = _coordMap.get(new CoordPair(vx, vy));
	    Vertex v = _vertices.get(x);
	    if (_firstRound) {
		    _players.get(p).addSettlement(v);
		    v.setObject(1);
		    v.setOwner(p);
	    }else {
		    _players.get(p).addSettlement(v);
		    v.setObject(1);
		    v.setOwner(p);
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
	
	public void writeBuildCity(int p, double vx, double vy) {
		_client.sendRequest(3, Integer.toString(p) + "," + 
			Double.toString(vx) + "," + Double.toString(vy));
	}
	
	public void buildCity(int p, double vx, double vy) {
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    _players.get(p).addCity(_vertices.get(v));
	    _vertices.get(v).setObject(2);
	}
	
	public void writeMakeTrade(int p1, int p2, int c1, int c2, int c3, int c4) {
		_client.sendRequest(4, Integer.toString(p1) + "," + 
		    Integer.toString(p2) + "," + Integer.toString(c1) + "," + 
		    Integer.toString(c2) + "," + Integer.toString(c3) + "," + Integer.toString(c4));
	}
	
	public boolean makeTrade(int p1, int p2, int c1, int c2, int c3, int c4) {
		boolean b1 = _players.get(p1).removeCard(c1);
		boolean b2 = _players.get(p1).removeCard(c2);
		boolean b3 = _players.get(p2).removeCard(c3);
		boolean b4 = _players.get(p2).removeCard(c4);
		if (!b1 || !b2 || !b3 || !b4) {
		    return false;
		}
		_players.get(p1).addCard(c3);
		_players.get(p1).addCard(c4);
		_players.get(p2).addCard(c1);
		_players.get(p2).addCard(c2);
		return true;
	}
	
	public void diceRolled(int roll) {
	    if (roll == 7) {
		moveRobber();
	    }
	    else {
		for (Hex h : _hexes) {
		    if (h.getRollNum() == roll && h.getNum() != _robberLoc) {
			for (Vertex vertex : h.getVertices()) {
			    int p = vertex.getOwner();
			    if (p != -1) {
				_players.get(p).addCard(h.getResource());
				if (vertex.getObject() == 2)  { //if city
				    _players.get(p).addCard(h.getResource());
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
