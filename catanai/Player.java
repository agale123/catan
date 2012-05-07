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
		boolean succ = _settlements.remove(v);
		if (succ) _cities.add(v);
	}
	
	public void addRoad(Edge e) {
		_roads.add(e);
	}
	
	public void collectFromVertex(Vertex v) {
		if (v == null || v.controller() != this || v.buildType() != BuildType.Settlement) return;
		for (Tile t : v.tiles()) if (TILE_RES.containsKey(t.resource())) draw(TILE_RES.get(t.resource()));
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
	
	public void dropList(List<Resource> res) {
		for (Resource r : res) _hand.remove(r);
	}
	
	public void takeList(List<Resource> res) {
		_hand.addAll(res);
	}
	
	public boolean hasList(List<Resource> res) {
		int b = 0, o = 0, s = 0, t = 0, w = 0;
		for (Resource r : res) {
			if (r == Resource.Brick) b++;
			else if (r == Resource.Ore) o++;
			else if (r == Resource.Sheep) s++;
			else if (r == Resource.Timber) t++;
			else if (r == Resource.Wheat) w++;
		}
		return b <= brick() && o <= ore() && s <= sheep() && t <= timber() && w <= wheat();
	}
	
	public boolean resForDevCard() {
		return brick() >= BRICK_DEV && 
				ore() >= ORE_DEV && 
				sheep() >= SHEEP_DEV && 
				timber() >= TIMBER_DEV && 
				wheat() >= WHEAT_DEV;
	}
	
	public boolean chargeForDevCard() {
		if (! resForDevCard()) return false;
		for (int i = 0; i < BRICK_DEV; i++) _hand.remove(Resource.Brick);
		for (int i = 0; i < ORE_DEV; i++) _hand.remove(Resource.Ore);
		for (int i = 0; i < SHEEP_DEV; i++) _hand.remove(Resource.Sheep);
		for (int i = 0; i < TIMBER_DEV; i++) _hand.remove(Resource.Timber);
		for (int i = 0; i < WHEAT_DEV; i++) _hand.remove(Resource.Wheat);
		return true;
	}
	
	public boolean resForRoad() {
		return brick() >= BRICK_ROAD && 
				ore() >= ORE_ROAD && 
				sheep() >= SHEEP_ROAD && 
				timber() >= TIMBER_ROAD && 
				wheat() >= WHEAT_ROAD;
	}
	
	public boolean chargeForRoad() {
		if (! resForRoad()) return false;
		for (int i = 0; i < BRICK_ROAD; i++) _hand.remove(Resource.Brick);
		for (int i = 0; i < ORE_ROAD; i++) _hand.remove(Resource.Ore);
		for (int i = 0; i < SHEEP_ROAD; i++) _hand.remove(Resource.Sheep);
		for (int i = 0; i < TIMBER_ROAD; i++) _hand.remove(Resource.Timber);
		for (int i = 0; i < WHEAT_ROAD; i++) _hand.remove(Resource.Wheat);
		return true;
	}
	
	public boolean resForSettlement() {
		return brick() >= BRICK_SETTLEMENT && 
				ore() >= ORE_SETTLEMENT && 
				sheep() >= SHEEP_SETTLEMENT && 
				timber() >= TIMBER_SETTLEMENT && 
				wheat() >= WHEAT_SETTLEMENT;
	}
	
	public boolean chargeForSettlement() {
		if (! resForSettlement()) return false;
		for (int i = 0; i < BRICK_SETTLEMENT; i++) _hand.remove(Resource.Brick);
		for (int i = 0; i < ORE_SETTLEMENT; i++) _hand.remove(Resource.Ore);
		for (int i = 0; i < SHEEP_SETTLEMENT; i++) _hand.remove(Resource.Sheep);
		for (int i = 0; i < TIMBER_SETTLEMENT; i++) _hand.remove(Resource.Timber);
		for (int i = 0; i < WHEAT_SETTLEMENT; i++) _hand.remove(Resource.Wheat);
		return true;
	}
	
	public boolean resForCity() {
		return brick() >= BRICK_CITY && 
				ore() >= ORE_CITY && 
				sheep() >= SHEEP_CITY && 
				timber() >= TIMBER_CITY && 
				wheat() >= WHEAT_CITY;
	}
	
	public boolean chargeForCity() {
		if (! resForCity()) return false;
		for (int i = 0; i < BRICK_CITY; i++) _hand.remove(Resource.Brick);
		for (int i = 0; i < ORE_CITY; i++) _hand.remove(Resource.Ore);
		for (int i = 0; i < SHEEP_CITY; i++) _hand.remove(Resource.Sheep);
		for (int i = 0; i < TIMBER_CITY; i++) _hand.remove(Resource.Timber);
		for (int i = 0; i < WHEAT_CITY; i++) _hand.remove(Resource.Wheat);
		return true;
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
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof Player) && (this._id.equals(((Player) other).getID()));
	}
	
	public void printResources() {
		System.out.println("(b, o, s, t, w) = (" + Integer.toString(brick()) + ", " + Integer.toString(ore()) + ", " +
				Integer.toString(sheep()) + ", " + Integer.toString(timber()) + ", " + Integer.toString(wheat()) + ")");
	}
}
