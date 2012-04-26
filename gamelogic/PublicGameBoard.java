package gamelogic;

import java.util.*;

public class PublicGameBoard {
	
	private ArrayList<Vertex> _vertices;
	private ArrayList<Hex> _hexes;
	private ArrayList<Edge> _edges;
	private ArrayList<Player> _players;
	boolean _firstRound = true;
	private int _longestRd = 4;
	private int _longestRd_Owner = -1;
	private int _largestArmy = 2;
	private int _largestArmy_Owner = -1;
	private server.Server _server;
	private HashMap<CoordPair, Integer> _coordMap;
	private HashMap<CoordPair, Pair> _currVertexState;
	private HashMap<Pair, Integer> _currEdgeState;
	
	public PublicGameBoard(server.Server server, int numPlayers) {
		_server = server;
		_hexes = new ArrayList<Hex>();
		_players = new ArrayList<Player>();
		_coordMap = new HashMap<CoordPair, Integer>();
		_currVertexState = new HashMap<CoordPair, Pair>();
		_currEdgeState = new HashMap<Pair, Integer>();
		
		for (int i = 0; i<numPlayers; i++) {
		    _players.add(new Player(i));
		}
		setUpBoard(numPlayers);
	}
	
	public PublicGameBoard(server.Server s, Object a, Object b, Object c, Object d) {
	    new PublicGameBoard(null, 4);
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
		    Hex hex = new Hex(hexCount, currx, curry);
		    _hexes.add(hex);
		    hexCount++;
		    
		    ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		    vertices.add(new Vertex((int)(currx-1), (int)(curry)));
		    vertices.add(new Vertex((int)(currx-.5), (int)(curry-1)));
		    vertices.add(new Vertex((int)(currx+.5), (int)(curry-1)));
		    vertices.add(new Vertex((int)(currx+1), (int)(curry)));
		    vertices.add(new Vertex((int)(currx+.5), (int)(curry+1)));
		    vertices.add(new Vertex((int)(currx-.5), (int)(curry+1)));
		    hex.setVertices(vertices);
		    
		    for (int z=0; z<(vertices.size()); z++) {
			if (!_vertices.contains(vertices.get(z))) {
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
	
	public boolean canBuySettlement(int p) {
	    if (_players.get(p).getHand().contains(catanui.BoardObject.type.WOOD) && _players.get(p).getHand().contains(catanui.BoardObject.type.BRICK) &&
	    _players.get(p).getHand().contains(catanui.BoardObject.type.SHEEP) && _players.get(p).getHand().contains(catanui.BoardObject.type.WHEAT)) {
		return true;
	    }
	    return false;
	}
	
	public boolean canBuildSettlement(int p, int vx, int vy) { 
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    
	    
	    /*FIX*/
	    for (Integer i : _vertices.get(v).getNeighbors()) {
		    if (_vertices.get(i).getObject() != 0) {
			    return false; //if not 2 away from other object
		    }
	    } 
	    
	    
	    
	    if ((_vertices.get(v).getObject() != 0)) { //if point already full
		return false;
	    }
    
	    if (_firstRound) {
		    return true;
	    }
	    for (Edge e : _players.get(p).getRoads()) {
		if (e.getStartV() == _vertices.get(v) || e.getEndV() == _vertices.get(v)) { 
		//if player has road connected
		    return true;
		}
	    }
	    return false;
	}
	
	public HashMap<CoordPair, Pair> buildSettlement(int p, int vx, int vy) {
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
	    _players.get(p).removeCard(catanui.BoardObject.type.WOOD);
	    _players.get(p).removeCard(catanui.BoardObject.type.BRICK);
	    _players.get(p).removeCard(catanui.BoardObject.type.WHEAT);
	    _players.get(p).removeCard(catanui.BoardObject.type.SHEEP);
	    
	    _currVertexState.put(new CoordPair(vx, vy), new Pair(catanui.BoardObject.type.SETTLEMENT, p));
	    return _currVertexState;
	}
	
	public boolean canBuyRoad(int p) {
	    if (_players.get(p).getHand().contains(catanui.BoardObject.type.WOOD) && _players.get(p).getHand().contains(catanui.BoardObject.type.BRICK)) {
		return true;
	    }
	    return false;
	}
	
	/*FIX*/
	public boolean canBuildRoad(int p, int e) {
		if (_edges.get(e).hasRoad()) {//if edge already has road 
			return false;	
		}
		if (_firstRound) {
			if (_edges.get(e).getStartV().getOwner() == p || 
					_edges.get(e).getEndV().getOwner() == p) {
				buildRoad(p, e);
				return true;
			}
		}
		for (Edge i : _players.get(p).getRoads()) {
			if (i.getStartV() == _edges.get(e).getStartV() || i.getStartV() == _edges.get(e).getEndV() ||
					i.getEndV() == _edges.get(e).getStartV() || i.getEndV() == _edges.get(e).getEndV()) {
				//if new road connected to old road
				buildRoad(p, e);
				return true;
			}
		}
		return false;
	}
	
	/*FIX*/
	public HashMap<Pair, Integer> buildRoad(int p, int e) {
		if (_firstRound) {
			_players.get(p).addRoad(_edges.get(e));
			_edges.get(e).setRoad();
		} else {
			_players.get(p).addRoad(_edges.get(e));
			_edges.get(e).setRoad();
		}
		_players.get(p).removeCard(catanui.BoardObject.type.WOOD);
		_players.get(p).removeCard(catanui.BoardObject.type.BRICK);
		
	    _currEdgeState.put(new Pair(new CoordPair(_edges.get(e).getStartV().getX(), _edges.get(e).getStartV().getY()), 
		new CoordPair(_edges.get(e).getEndV().getX(), _edges.get(e).getEndV().getY())), new Integer(p));
	    return _currEdgeState;
	}
	
	public boolean canBuyCity(int p) {
	    return true;
	}
	
	public boolean canBuildCity(int p, int vx, int vy) {
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    if (_vertices.get(v).getObject() != 1 || //if no settlement on vertex
			    !_players.get(p).hasSettlement(_vertices.get(v))) { //if settlement belongs to player
		return false;
	    }
	    return true;
	}
	
	public HashMap<CoordPair, Pair> buildCity(int p, int vx, int vy) {
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    _players.get(p).addCity(_vertices.get(v));
	    _vertices.get(v).setObject(2);
	    _players.get(p).removeCard(catanui.BoardObject.type.ORE);
	    _players.get(p).removeCard(catanui.BoardObject.type.ORE);
	    _players.get(p).removeCard(catanui.BoardObject.type.ORE);
	    _players.get(p).removeCard(catanui.BoardObject.type.WHEAT);
	    _players.get(p).removeCard(catanui.BoardObject.type.WHEAT);
	    
	    _currVertexState.put(new CoordPair(vx, vy), new Pair(catanui.BoardObject.type.CITY, p));
	    return _currVertexState;
	}
	
	public boolean playDevCard(int p, int cardID) {
		return true;
	}
	
	public boolean makeTrade(int p1, int p2, catanui.BoardObject.type c1, catanui.BoardObject.type c2, 
						catanui.BoardObject.type c3, catanui.BoardObject.type c4) {
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
		    //moveRobber();
	    }
	    else {
		for (Hex h : _hexes) {
		    if (h.getRollNum() == roll) {
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
	
	public void updateLongestRd(int p) {
		if (_players.get(p).getnumRds() > _longestRd) {
			if (_longestRd_Owner != -1) {
				_players.get(_longestRd_Owner).updateLongestRd(-2);
			}
			_players.get(p).updateLongestRd(2);
			_longestRd_Owner = p;
		}
	}
	
	//TODO: getstate method = returns string with # of hexes and each's number

}