package gamelogic;

import java.util.*;
import catanai.*;
import catanui.*;

public class PublicGameBoard {
	
	private ArrayList<Vertex> _vertices;
	private ArrayList<Hex> _hexes;
	private ArrayList<Edge> _edges;
	private ArrayList<Player> _players;
	private ArrayList<catanai.AIPlayer> _ais;
	boolean _firstRound = true;
	private int _longestRd = 4;
	private int _longestRd_Owner = -1;
	private int _largestArmy = 2;
	private int _largestArmy_Owner = -1;
	private server.Server _server;
	private HashMap<CoordPair, Integer> _coordMap;
	private HashMap<Pair, Integer> _edgeMap;
	
	public PublicGameBoard(server.Server server, int numPlayers) {
		_server = server;
		_hexes = new ArrayList<Hex>();
		_players = new ArrayList<Player>();
		_ais = new ArrayList<catanai.AIPlayer>();
		_coordMap = new HashMap<CoordPair, Integer>();
		_edgeMap = new HashMap<Pair, Integer>();
		_vertices = new ArrayList<Vertex>();
		_edges = new ArrayList<Edge>();
		
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
	     ArrayList<Integer> numbers;
	    int numHexes = 0;
	    //if (numPlayers <= 4) {
		colSizes = new ArrayList<Integer>(Arrays.asList(3, 4, 5, 4, 3));
		startY = new ArrayList<Integer>(Arrays.asList(3, 2, 1, 2, 3));
		numHexes = 19;
		numbers = new ArrayList<Integer>(Arrays.asList(11,4,8,12,6,3,6,2,5,11,10,5,10,4,9,2,8,3,6));
	    /*} else if (numPlayers == 5 || numPlayers == 6) {
		colSizes = new ArrayList<Integer>(Arrays.asList(3, 4, 5, 6, 5, 4, 3));
		startY = new ArrayList<Integer>(Arrays.asList(4, 3, 2, 1, 2, 3, 4));
		numHexes = 30;
		numbers = new ArrayList<Integer>(Arrays.asList(11,4,8,12,6,3,6,2,5,11,10,5,10,4,9,2,8,3,6,8,6,3,9,10,4,2,7,11,12,6));
	    } else {
		colSizes = new ArrayList<Integer>(Arrays.asList(3, 4, 5, 6, 7, 6, 5, 4, 3));
		startY = new ArrayList<Integer>(Arrays.asList(5, 4, 3, 2, 1, 2, 3, 4, 5));
		numHexes = 43;
		numbers = new ArrayList<Integer>(Arrays.asList(11,4,8,12,6,3,6,2,5,11,10,5,10,4,9,2,8,3,6,8,6,3,
							    9,10,4,2,7,11,12,6,11,4,8,12,6,3,6,2,5,11,10,5,10));
	    }*/
	    
	    ArrayList<catanui.BoardObject.type> resources = new ArrayList<catanui.BoardObject.type>();
	    catanui.BoardObject.type[] types = {catanui.BoardObject.type.WHEAT, catanui.BoardObject.type.WOOD, 
	    catanui.BoardObject.type.SHEEP,catanui.BoardObject.type.BRICK, catanui.BoardObject.type.ORE};
	    for (int r = 0; r<numHexes; r++) {
			resources.add(types[r%5]);
	    }
	    
	    double currx = -0.5;
	    double curry;
	    int hexCount = 0;
	    for (int i=0; i<colSizes.size(); i++) {
		currx += 2;
		curry = startY.get(i);
		for (int x=0; x<colSizes.get(i); x++) {
		    Hex hex = new Hex(hexCount, currx, curry);
		    int rand = (int) (Math.random() * resources.size());
		    hex.setResource(resources.get(rand));
		    hex.setRollNum(numbers.get(hexCount));
		    resources.remove(rand);
		    _hexes.add(hex);
		    hexCount++;
		    
		    ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		    if(_vertices.contains(new Vertex((int)(currx-1.5), (int)(curry)))) {
			vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx-1.5), (int)(curry)))));
		    } else {
			vertices.add(new Vertex((int)(currx-1.5), (int)(curry)));
		    } if(_vertices.contains(new Vertex((int)(currx-.5), (int)(curry-1)))) {
			vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx-.5), (int)(curry-1)))));
		    } else {
			vertices.add(new Vertex((int)(currx-.5), (int)(curry-1)));
		    } if(_vertices.contains(new Vertex((int)(currx+.5), (int)(curry-1)))) {
			vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx+.5), (int)(curry-1)))));
		    } else {
			vertices.add(new Vertex((int)(currx+.5), (int)(curry-1)));
		    } if(_vertices.contains(new Vertex((int)(currx+1.5), (int)(curry)))) {
			vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx+1.5), (int)(curry)))));
		    } else {
			vertices.add(new Vertex((int)(currx+1.5), (int)(curry)));
		    } if(_vertices.contains(new Vertex((int)(currx+.5), (int)(curry+1)))) {
			vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx+.5), (int)(curry+1)))));
		    } else {
			vertices.add(new Vertex((int)(currx+.5), (int)(curry+1)));
		    } if(_vertices.contains(new Vertex((int)(currx-.5), (int)(curry+1)))) {
			vertices.add(_vertices.get(_coordMap.get(new CoordPair((int)(currx-.5), (int)(curry+1)))));
		    } else {
			vertices.add(new Vertex((int)(currx-.5), (int)(curry+1)));
		    }
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
				_edgeMap.put(new Pair(new CoordPair(edge.getStartV().getX(), edge.getStartV().getY()), new CoordPair(edge.getEndV().getX(), edge.getEndV().getY())), 
						    new Integer(_edges.indexOf(edge)));
				_edgeMap.put(new Pair(new CoordPair(edge.getEndV().getX(), edge.getEndV().getY()), new CoordPair(edge.getStartV().getX(), edge.getStartV().getY())), 
						    new Integer(_edges.indexOf(edge)));
			    }
			} else {
			    Edge edge = new Edge(vertices.get(z), vertices.get(z+1));
			    if (!_edges.contains(edge)) {
				_edges.add(edge);
				_edgeMap.put(new Pair(new CoordPair(edge.getEndV().getX(), edge.getEndV().getY()), new CoordPair(edge.getStartV().getX(), edge.getStartV().getY())), 
						    new Integer(_edges.indexOf(edge)));
				_edgeMap.put(new Pair(new CoordPair(edge.getStartV().getX(), edge.getStartV().getY()), new CoordPair(edge.getEndV().getX(), edge.getEndV().getY())), 
						    new Integer(_edges.indexOf(edge)));
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
	    return true;//should be false
	}
	
	public boolean canBuildSettlement(int p, int vx, int vy) { 
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    
	    
	    /**FIX*/
	    /*for (Integer i : _vertices.get(v).getNeighbors()) {
		    if (_vertices.get(i).getObject() != 0) {
			    return false; //if not 2 away from other object
		    }
	    } */
	    
	    
	    
	    if ((_vertices.get(v).getObject() != 0)) { //if point already full
		return false;
	    }
    
	    if (_firstRound) {
		buildSettlement(p, vx, vy);
		return true;
	    }
	    for (Edge e : _players.get(p).getRoads()) {
		if (e.getStartV() == _vertices.get(v) || e.getEndV() == _vertices.get(v)) { 
		//if player has road connected
		    buildSettlement(p, vx, vy);
		    return true;
		}
	    }
	    return false;
	}
	
	public void buildSettlement(int p, int vx, int vy) {
	    int x = _coordMap.get(new CoordPair(vx, vy));
	    Vertex v = _vertices.get(x);
	    _players.get(p).addSettlement(v);
	    v.setObject(1);
	    v.setOwner(p);
	    System.out.println(v.getOwner());
	    _players.get(p).removeCard(catanui.BoardObject.type.WOOD);
	    _players.get(p).removeCard(catanui.BoardObject.type.BRICK);
	    _players.get(p).removeCard(catanui.BoardObject.type.WHEAT);
	    _players.get(p).removeCard(catanui.BoardObject.type.SHEEP);
	    System.out.println("number of settlements = " + _players.get(p).getSettlements().size());
	}
	
	public boolean canBuyRoad(int p) {
	    if (_players.get(p).getHand().contains(catanui.BoardObject.type.WOOD) && _players.get(p).getHand().contains(catanui.BoardObject.type.BRICK)) {
		return true;
	    }
	    return true; //should be false
	}
	
	/*FIX*/
	public boolean canBuildRoad(int p, int vx1, int vy1, int vx2, int vy2) {
		try {
		    int e = _edgeMap.get(new Pair(new CoordPair(vx1, vy1), new CoordPair(vx2, vy2)));
		} catch(Exception e) {
		    return false;
		} 
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
		return true; // should be false
	}
	
	/**FIX*/
	public void buildRoad(int p, int e) {
		_players.get(p).addRoad(_edges.get(e));
		_edges.get(e).setRoad();
		_players.get(p).removeCard(catanui.BoardObject.type.WOOD);
		_players.get(p).removeCard(catanui.BoardObject.type.BRICK);
	}
	
	public boolean canBuyCity(int p) {
	    int numOre = 0;
	    int numWheat = 0;
	    for (BoardObject.type resource: _players.get(p).getHand()) {
		if (resource == BoardObject.type.ORE) {
		    numOre++;
		} else if (resource == BoardObject.type.WHEAT) {
		    numWheat++;
		}
	    }
	    if (numOre >= 3 && numWheat >= 2) {
		return true;
	    }
	    return true; //should be false
	}
	
	public boolean canBuildCity(int p, int vx, int vy) {
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    if (_vertices.get(v).getObject() != 1 || //if no settlement on vertex
			    !_players.get(p).hasSettlement(_vertices.get(v))) { //if settlement belongs to player
		return false;
	    }
	    buildCity(p, vx, vy);
	    return true;
	}
	
	public void buildCity(int p, int vx, int vy) {
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    _players.get(p).addCity(_vertices.get(v));
	    _vertices.get(v).setObject(2);
	    _players.get(p).removeCard(catanui.BoardObject.type.ORE);
	    _players.get(p).removeCard(catanui.BoardObject.type.ORE);
	    _players.get(p).removeCard(catanui.BoardObject.type.ORE);
	    _players.get(p).removeCard(catanui.BoardObject.type.WHEAT);
	    _players.get(p).removeCard(catanui.BoardObject.type.WHEAT);
	}
	
	public boolean canBuyDev(int p) {
	     if (_players.get(p).getHand().contains(catanui.BoardObject.type.ORE) && 					_players.get(p).getHand().contains(catanui.BoardObject.type.SHEEP) 					&&_players.get(p).getHand().contains(catanui.BoardObject.type.WHEAT)) {
		return true;
	    }
	    return true; //should return false
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
	
	public void updateLongestRd(int p) {
		if (_players.get(p).getnumRds() > _longestRd) {
			if (_longestRd_Owner != -1) {
				_players.get(_longestRd_Owner).updateLongestRd(-2);
			}
			_players.get(p).updateLongestRd(2);
			_longestRd_Owner = p;
		}
	}
	
	public String getState() {
		String toReturn = "";
		for(Hex h : _hexes) {
			toReturn += h.getResource().toString() + ",";
		}
		return toReturn;
	}

	public void addAIPlayer(catanai.AIPlayer play) {
		_ais.add(play);
		_players.add(new Player(0)); /**change the player num*/
	}
}
