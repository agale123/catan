package gamelogic;

import java.util.*;
import java.lang.*;
import catanui.*;


public class ClientGameBoard {

	private ArrayList<Hex> _hexes;
	private ArrayList<Player> _players;
	boolean _firstRound = true;
	private int _longestRd = 4;
	private int _longestRd_Owner = -1;
	client.Client _client;
	public int _playerNum;
	public catanui.ChatBar _chatBar;
	public catanui.SideBar _sideBar;
	public catanui.MapPanel _mapPanel;
	public String _name;
	private ArrayList<Trade> _currTrades;
	private HashMap<CoordPair, Pair> _currVertexState;
	private HashMap<Pair, Integer> _currEdgeState;
	private int _numPlayers;
	private HashMap<CoordPair, Integer> _coordMap;
	private ArrayList<Vertex> _vertices;
	private int[] _points;
	private int[] _numRoads;
	
	public ClientGameBoard(int numPlayers, client.Client client, int playerNum, String name, String[] resources) {
		_client = client;
		_hexes = new ArrayList<Hex>();
		_players = new ArrayList<Player>();
		_playerNum = playerNum;
		_name = name;
		_currVertexState = new HashMap<CoordPair, Pair>();
		_currEdgeState = new HashMap<Pair, Integer>();
		_numPlayers = numPlayers;
		_coordMap = new HashMap<CoordPair, Integer>();
		_vertices = new ArrayList<Vertex>();
		_points = new int[numPlayers];
		_numRoads = new int[numPlayers];
		for (int i = 0; i<numPlayers; i++) {
		    _points[i] = 0;
		    _numRoads[i] = 0;
		}
		
		setUpBoard(numPlayers, resources);
	}
	
	public void setUpBoard(int numPlayers, String[] resources) {
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
	    
	    double currx = -0.5;
	    double curry;
	    int hexCount = 0;
	    for (int i=0; i<colSizes.size(); i++) {
		currx += 2;
		curry = startY.get(i);
		for (int x=0; x<colSizes.get(i); x++) {
		    Hex hex = new Hex(hexCount, currx, curry);
		    hex.setResource(catanui.BoardObject.type.valueOf(resources[hexCount]));
		    hex.setRollNum(numbers.get(hexCount));
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
		    }
		    curry += 2;
		}
	    }
	}
	
	public void updateGUI(Pair pair, boolean b) {
		if(((Pair) pair.getA()).getB().getClass().equals(Integer.class)) {
			_sideBar.signalNewTrade(pair);
	    } else {
			_sideBar.activateExchanger((Integer)(pair.getB()), b);
	    }
	}
	

	public void writeBuySettlement(Pair pair) {
		_client.sendRequest(pair);
	}

	public void writeBuildSettlement(int vx, int vy) {
		_client.sendRequest(2, Integer.toString(_playerNum) + "," + 
			Integer.toString(vx) + "," + Integer.toString(vy));
	}
	
	public void buildSettlement(int p, int vx, int vy) {
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    _vertices.get(v).setOwner(p);
	    _vertices.get(v).setObject(1);
	    _points[p]++;
	    if (_points[p] >= 6 && p == _playerNum) {
		_chatBar.addLine(_name + " has won the game!");
		sendLine(_name + " has won the game!");
	    }
	    _currVertexState.put(new CoordPair(vx, vy), new Pair(catanui.BoardObject.type.SETTLEMENT, p));
	    _mapPanel.updateVertexContents(_currVertexState);
	}
	
	public void writeBuyRoad(Pair pair) {
		_client.sendRequest(pair);
	}

	public void writeBuildRoad(int vx1, int vy1, int vx2, int vy2) {
		_client.sendRequest(1, Integer.toString(_playerNum) + "," + Integer.toString(vx1) + "," + Integer.toString(vy1) + "," + Integer.toString(vx2) + "," + Integer.toString(vy2));
	}
	
	public void buildRoad(int p, int vx1, int vy1, int vx2, int vy2) {
	    _numRoads[p]++;
	    updateLongestRoad(p);
	    _currEdgeState.put(new Pair(new CoordPair(vx1, vy1), new CoordPair(vx2, vy2)), new Integer(p));
	    _mapPanel.updateEdgeContents(_currEdgeState);
	}
	
	public void writeBuyCity(Pair pair) {
		_client.sendRequest(pair);
	}
	
	public void writeBuildCity(int vx, int vy) {
		_client.sendRequest(3, Integer.toString(_playerNum) + "," + 
			Integer.toString(vx) + "," + Integer.toString(vy));
	}
	
	public void buildCity(int p, int vx, int vy) {
	    int v = _coordMap.get(new CoordPair(vx, vy));
	    _vertices.get(v).setOwner(p);
	    _vertices.get(v).setObject(2);
	    _points[p]++;
	    if (_points[p] >= 6 && p == _playerNum) {
		_chatBar.addLine(_name + " has won the game!");
		sendLine(_name + " has won the game!");
	    }
	    _currVertexState.put(new CoordPair(vx, vy), new Pair(catanui.BoardObject.type.CITY, p));
	    _mapPanel.updateVertexContents(_currVertexState);
	}
	
	public void writeBuyDev(Pair pair) {
	    _client.sendRequest(pair);
	}
	
	public void useDevCard() {
		_client.sendRequest(17, "usedev");
	}
	
	public void writeProposeTrade(Pair pair) { //((ins, outs), id)
	//if already seen id, overwrite it
		_client.sendRequest(new Pair(pair, new Integer(2)));
	}
	
	public void writeDoTrade(Pair pair) {
		_client.sendRequest(new Pair(pair, new Integer(1)));
	}
	
	public boolean makeTrade(int p1, int p2, catanui.BoardObject.type c1, catanui.BoardObject.type c2, catanui.BoardObject.type c3, catanui.BoardObject.type c4) {
		return false;
	}
	
	public void diceRolled(int roll) {
		_chatBar.addLine("The dice roll was " + roll);
		
	    for (Hex h : _hexes) {
		if (h.getRollNum() == roll) {
		    for (Vertex vertex : h.getVertices()) {
			int p = vertex.getOwner();
			if (p == _playerNum) {
			    _sideBar.addCard(h.getResource());
			    if (vertex.getObject() == 2)  { //if city
				_sideBar.addCard(h.getResource());
				_chatBar.addLine(_name + " received two " + h.getResource());
				sendLine(_name + " received two " + h.getResource());
			    }else {
				_chatBar.addLine(_name + " received one " + h.getResource());
				sendLine(_name + " received one " + h.getResource());
			    }
			}
		    }
		}
	    }
	}
	
	public void updateLongestRoad(int p) {
	    if (_numRoads[p] > _longestRd) {
		if (_longestRd_Owner != -1) {
			_points[_longestRd_Owner] -= 2;
		}
		_points[p] += 2;
		_longestRd = _numRoads[p];
		_longestRd_Owner = p;
		if (p == _playerNum) {
		    _chatBar.addLine(_name + " now has the Largest Road Network!");
		    sendLine(_name + " now has the Largest Road Network!");
		}
		if (_points[p] >= 6 && p == _playerNum) {
		    sendLine(_name + " has won the game!");
		    _chatBar.addLine(_name + " has won the game!");
		}
	    }
	}
	
	public void sendLine(String s) {
	    _client.sendRequest(10, s);
	}
	
	public void receiveLine(String s) {
	    _chatBar.addLine(s);
	}
	
	public class Trade {
	    public Trade() {
	    
	    }
	}
	
	public HashMap<Pair, Pair> getHexInfo() {
	    HashMap<Pair, Pair> map = new HashMap<Pair, Pair>();
	    for (Hex h: _hexes) {
		map.put(new Pair(h.getX(), h.getY()), new Pair(h.getResource(), h.getRollNum()));
	    }
	    return map;
	}
	public int getNumRings() {
	    return 3;
	}
	public Pair getStartPoint() {
	    Pair start = new Pair(_hexes.get(9).getX(), _hexes.get(9).getY());
	    return start;
	}
	
	public void exit() {
	
	}
	
}
