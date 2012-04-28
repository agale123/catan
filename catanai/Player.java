package catanai;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

public abstract class Player implements AIConstants {
	protected List<Resource> _hand;
	protected Set<Vertex> _cities;
	protected Set<Vertex> _settlements;
	protected Set<Edge> _roads;
	protected gamelogic.PublicGameBoard _publicBoard;
	protected String _id;
	protected Map<String, Opponent> _opponents;
	protected GameBoard _board;
	protected int _numCards, _numDev, _numKnight;
	protected boolean _longestRoad, _largestArmy;
	
	public abstract Move getMove();
	public abstract int getVictoryPoints();
	protected abstract Map<Heuristic, Move> getValidMoves();
	protected abstract double valueMove(Move m, GameBoard board, int lookahead);
	protected abstract Move playFromHeuristic(Heuristic h);
	public abstract void registerDieRoll(int r);
	public abstract void addOpponent(String id);
	
	public int longestRoadLength() {
		// TODO: Implement this.
		return 0;
	}
	
	public int handSize() {
		return _numCards;
	}
	
	public int numDev() {
		return _numDev;
	}
	
	public int armySize() {
		return _numKnight;
	}
	
	public void addKnight() {
		_numKnight++;
	}
	
	public boolean longestRoad() {
		return _longestRoad;
	}
	
	public boolean _largestArmy() {
		return _largestArmy;
	}
	
	public void giveLongRoad() {
		_longestRoad = true;
	}
	
	public void takeLongRoad() {
		_longestRoad = false;
	}
	
	public void giveLargArmy() {
		_largestArmy = true;
	}
	
	public void takeLargArmy() {
		_largestArmy = false;
	}
	
	public void addSettlement(Vertex v) {
		_settlements.add(v);
	}
	
	public void addCity(Vertex v) {
		_settlements.remove(v);
		_cities.add(v);
	}
	
	public void addRoad(Edge e) {
		_roads.add(e);
	}
	
	public int brick() {
		int i = 0;
		for (Resource r : _hand) if (r == Resource.Brick) i++;
		return i;
	}
	
	public int ore() {
		int i = 0;
		for (Resource r : _hand) if (r == Resource.Ore) i++;
		return i;
	}
	
	public int sheep() {
		int i = 0;
		for (Resource r : _hand) if (r == Resource.Sheep) i++;
		return i;
	}
	
	public int timber() {
		int i = 0;
		for (Resource r : _hand) if (r == Resource.Timber) i++;
		return i;
	}
	
	public int wheat() {
		int i = 0;
		for (Resource r : _hand) if (r == Resource.Wheat) i++;
		return i;
	}
	
	public void draw(Resource res) {
		_hand.add(res);
	}
	
	public int takeAll(Resource res) {
		boolean has;
		int n = -1;
		do {
			has = _hand.remove(res);
			n++;
		}
		while (has);
		return n;
	}
	
	public boolean resForDevCard() {
		return brick() >= BRICK_DEV && 
				ore() >= ORE_DEV && 
				sheep() >= SHEEP_DEV && 
				timber() >= TIMBER_DEV && 
				wheat() >= WHEAT_DEV;
	}
	
	public boolean resForRoad() {
		return brick() >= BRICK_ROAD && 
				ore() >= ORE_ROAD && 
				sheep() >= SHEEP_ROAD && 
				timber() >= TIMBER_ROAD && 
				wheat() >= WHEAT_ROAD;
	}
	
	public boolean resForSettlement() {
		return brick() >= BRICK_SETTLEMENT && 
				ore() >= ORE_SETTLEMENT && 
				sheep() >= SHEEP_SETTLEMENT && 
				timber() >= TIMBER_SETTLEMENT && 
				wheat() >= WHEAT_SETTLEMENT;
	}
	
	public boolean resForCity() {
		return brick() >= BRICK_CITY && 
				ore() >= ORE_CITY && 
				sheep() >= SHEEP_CITY && 
				timber() >= TIMBER_CITY && 
				wheat() >= WHEAT_CITY;
	}
	
	public Set<Vertex> vertOnNetwork() {
		HashSet<Vertex> res = new HashSet<Vertex>();
		for (Edge e : _roads) for (Vertex v : e.ends()) res.add(v);
		return res;
	}
	
	public Collection<Opponent> getOpponents() {
		return _opponents.values();
	}
	
	public String getID() {
		return _id;
	}
}
