package gamelogic;

import java.util.*;
import java.lang.*;
import catanui.*;


public class ClientGameBoard {

	private final int POINTS_TO_WIN = 5;
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
	    ArrayList<Integer> colSizes = null;
	    ArrayList<Integer> startY = null;
	    ArrayList<Integer> numbers = null;
	    int numHexes = 0;
	    if (numPlayers <= 4) {
		colSizes = new ArrayList<Integer>(Arrays.asList(3, 4, 5, 4, 3));
		startY = new ArrayList<Integer>(Arrays.asList(3, 2, 1, 2, 3));
		numHexes = 19;
		numbers = new ArrayList<Integer>(Arrays.asList(8,4,11,10,11,3,12,5,9,2,6,9,2,4,5,10,6,3,8));
	    } else if (numPlayers == 5 || numPlayers == 6) {
		colSizes = new ArrayList<Integer>(Arrays.asList(4,5,6,7,6,5,4));
		startY = new ArrayList<Integer>(Arrays.asList(4,3,2,1,2,3,4));
		numHexes = 37;
		numbers = new ArrayList<Integer>(Arrays.asList(11,4,8,12,6,3,6,2,5,11,10,5,10,4,9,2,8,3,6,8,6,3,9,10,4,2,7,11,12,6,3,4,5,6,7,8,9));
	    }
	    
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
	
	public void updateGUI(Trade t, boolean b) {
		if(t.isPropose()) {
			_sideBar.signalNewTrade(t);
	    } else {
			
			_sideBar.activateExchanger(t.getTradeID(), b);
	    }
	}

	public void writeBuySettlement(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 6);
	    _client.sendRequest(t);
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
	    if (_points[p] >= POINTS_TO_WIN && p == _playerNum) {
		_chatBar.addLine(_name + " has won the game!");
		sendWin(_name + " has won the game!");
	    }
	    _currVertexState.put(new CoordPair(vx, vy), new Pair(catanui.BoardObject.type.SETTLEMENT, p));
	    _mapPanel.updateVertexContents(_currVertexState);
	}
	
	public void writeBuyRoad(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 5);
	    _client.sendRequest(t);
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
	
	public void writeBuyCity(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 4);
	    _client.sendRequest(t);
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
	    if (_points[p] >= POINTS_TO_WIN && p == _playerNum) {
		_chatBar.addLine(_name + " has won the game!");
		sendWin(_name + " has won the game!");
	    }
	    _currVertexState.put(new CoordPair(vx, vy), new Pair(catanui.BoardObject.type.CITY, p));
	    _mapPanel.updateVertexContents(_currVertexState);
	}
	
	public void writeBuyDev(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 3);
	    _client.sendRequest(t);
	}
	
	public void useDevCard() {
		_client.sendRequest(17, "usedev");
		sendLine(_name + " has played a Development Card.");
	}
	
	public void writeProposeTrade(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) { //((ins, outs), id)
		Trade t = new Trade(ins, outs, id, 2);
		_client.sendRequest(t);
	}
	
	public void writeDoTrade(catanui.BoardObject.type[] ins, catanui.BoardObject.type[] outs, int id) {
		Trade t = new Trade(ins, outs, id, 1);
		_client.sendRequest(t);
	}
	
	public void diceRolled(int roll) {
		//_chatBar.addLine("The dice roll was " + roll);
		_mapPanel.updateRoll(roll);
		
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
		if (_points[p] >= POINTS_TO_WIN && p == _playerNum) {
		    _chatBar.addLine(_name + " has won the game!");
		    sendWin(_name + " has won the game!");
		}
	    }
	}
	
	public void addPoint() {
	    _points[_playerNum]++;
	    _chatBar.addLine("You have received a point! You now have " + _points[_playerNum] + " points.");
	    if (_points[_playerNum] >= POINTS_TO_WIN) {
		    _chatBar.addLine(_name + " has won the game!");
		    sendWin(_name + " has won the game!");
		}
	}
	
	public void addCard(BoardObject.type card) {
	System.out.println("client game board");
	    _sideBar.addCard(card);
	    _chatBar.addLine("You received a " + card);
	}
	
	public void loseStolenCards(BoardObject.type card) {
	    _sideBar.removeAllCards(card);
	    _chatBar.addLine("All of your " + card + " were stolen!");
	}
	
	public void getStolenCards(int num, BoardObject.type card) {
	    for (int i=0; i<num; i++) {
		_sideBar.addCard(card);
	    }
	    _chatBar.addLine("You stole " + num + " " + card + " from the other players.");
	}
	
	public void sendLine(String s) {
	    _client.sendRequest(10, s);
	}
	
	public void sendWin(String s) {
	    _client.sendRequest(9, s);
	}
	
	public void receiveLine(String s) {
	    _chatBar.addLine(s);
	}
	
	
	public HashMap<Pair, Pair> getHexInfo() {
	    HashMap<Pair, Pair> map = new HashMap<Pair, Pair>();
	    for (Hex h: _hexes) {
		map.put(new Pair(h.getX(), h.getY()), new Pair(h.getResource(), h.getRollNum()));
	    }
	    return map;
	}
	public int getNumRings() {
	if (_numPlayers < 4) {
	    return 3;
	} else {
	    return 4;
	}
	}
	public Pair getStartPoint() {
	    if (_numPlayers < 4) {
		Pair start = new Pair(_hexes.get(9).getX(), _hexes.get(9).getY());
		return start;
	    } else {
		Pair start2 = new Pair(_hexes.get(18).getX(), _hexes.get(18).getY());
		return start2;
	    }
	}
	
	public void exit() {
	
	}
	
}
