package catanai;

import java.util.Map;
import java.util.Set;
import java.util.List;

public abstract class Player {
	protected List<Resource> _hand;
	protected Set<Vertex> _cities;
	protected Set<Vertex> _settlements;
	protected Set<Edge> _roads;
	protected int _numCards, _numDev, _numKnight;
	protected boolean _longestRoad, _largestArmy;
	
	// Returns the move that optimizes the expected number of victory points n cycles ahead.
	public abstract Move getMove();
	public abstract int getVictoryPoints();
	public abstract int longestRoadLength();
	protected abstract Map<Heuristic, Move> getValidMoves();
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
}
