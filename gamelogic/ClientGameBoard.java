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
	private HashMap<CoordPair, Integer> _coordMap;
	client.Client _client;
	private int _playerNum;
	public catanui.ChatBar _chatBar;
	public catanui.SideBar _sideBar;
	public catanui.MapPanel _mapPanel;
	public String _name;
	private ArrayList<Trade> _currTrades;
	private HashMap<CoordPair, Pair> _currVertexState;
	private HashMap<Pair, Integer> _currEdgeState;
	
	public ClientGameBoard(int numPlayers, client.Client client, int playerNum, String name) {
		_client = client;
		_hexes = new ArrayList<Hex>();
		_players = new ArrayList<Player>();
		_coordMap = new HashMap<CoordPair, Integer>();
		_playerNum = playerNum;
		_name = name;
		_currVertexState = new HashMap<CoordPair, Pair>();
		_currEdgeState = new HashMap<Pair, Integer>();
	}
	
	public void setFirstRoundOver() {
		_firstRound = false;
	}
	
	public void updateGUI(catanui.SideBar.Exchanger e) {
	    //_sideBar.switchOutB(); in buildSettlement
	}
	
	public void writeBuySettlement(catanui.SideBar.Exchanger e) {
		_client.sendRequest(e);
		//if true:
		//updategui shouldve been called
	}

	public void writeBuildSettlement(int vx, int vy) {
		_client.sendRequest(2, Integer.toString(_playerNum) + "," + 
			Integer.toString(vx) + "," + Integer.toString(vy));
	}
	
	public void buildSettlement(int p, int vx, int vy) {
	    _currVertexState.put(new CoordPair(vx, vy), new Pair(catanui.BoardObject.type.SETTLEMENT, p));
	}
	
	public void writeBuyRoad(catanui.SideBar.Exchanger e) {
		
		//if true:
		//updategui shouldve been called
	}
	
	public void writeBuildRoad(int e) {
		_client.sendRequest(1, Integer.toString(_playerNum) + "," + Integer.toString(e));
	}
	
	public void buildRoad(int p, int vx1, int vy1, int vx2, int vy2) {
	    _currEdgeState.put(new Pair(new CoordPair(vx1, vy1), new CoordPair(vx2, vy2)), new Integer(p));
	}
	
	public void writeBuyCity(catanui.SideBar.Exchanger e) {
		
		//if true:
		//updategui shouldve been called
	}
	
	public void writeBuildCity(int vx, int vy) {
		_client.sendRequest(3, Integer.toString(_playerNum) + "," + 
			Double.toString(vx) + "," + Double.toString(vy));
	}
	
	public void buildCity(int p, int vx, int vy) {
	    _currVertexState.put(new CoordPair(vx, vy), new Pair(catanui.BoardObject.type.CITY, p));
	}
	
	public void writeDoTrade(catanui.SideBar.Exchanger e, catanui.BoardObject.type c1, catanui.BoardObject.type c2) {
		
		//if true:
		//updategui shouldve been called
	}
	
	public void writeDoTrade(catanui.SideBar.Exchanger e, catanui.BoardObject.type c1, catanui.BoardObject.type c2, 
						catanui.BoardObject.type c3, catanui.BoardObject.type c4) {
		/*_client.sendRequest(4, Integer.toString(p1) + "," + Integer.toString(p2) + "," + c1.toString() + "," + 
		    c2.toString() + "," + c3.toString() + "," + c4.toString());
		    
		_sideBar.switchOutB();*/
	}
	
	public boolean makeTrade(int p1, int p2, catanui.BoardObject.type c1, catanui.BoardObject.type c2, catanui.BoardObject.type c3, catanui.BoardObject.type c4) {
		return false;
	}
	
	public void diceRolled(int roll) {
	    for (Hex h : _hexes) {
		if (h.getRollNum() == roll) {
		    for (Vertex vertex : h.getVertices()) {
			int p = vertex.getOwner();
			if (p == _playerNum) {
			    _sideBar.addCard(h.getResource());
			    if (vertex.getObject() == 2)  { //if city
				_sideBar.addCard(h.getResource());
				_chatBar.addLine(_name + "received 2 " + h.getResource());
			    }else {
				_chatBar.addLine(_name + "received 1 " + h.getResource());
			    }
			}
		    }
		}
	    }
	}
	
	public void updateLongestRd() {
		if (_players.get(_playerNum).getnumRds() > _longestRd) {
			if (_longestRd_Owner != -1) {
				_players.get(_longestRd_Owner).updateLongestRd(-2);
			}
			_players.get(_playerNum).updateLongestRd(2);
			_longestRd_Owner = _playerNum;
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
	
}
