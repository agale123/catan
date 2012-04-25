package catanai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class AIPlayer extends Player implements AIConstants {
	private Set<Opponent> _opponents;
	private List<DevCard> _devcards;
	private Vertex _goal, _s0, _s1;
	private GameBoard _board;
	private Heuristic _lastHeuristic;
	
	public AIPlayer() {
		this(false);
	}
	
	public AIPlayer(boolean extended) {
		// TODO: Implement this.
		_opponents = new HashSet<Opponent>();
		_devcards = new ArrayList<DevCard>();
		_goal = null;
		_board = new GameBoard(extended);
		_lastHeuristic = null;
	}
	
	/**
	 * getMove: Returns move to be played by the AI.
	 * This does not register the move on the AI's game board. This must
	 * be done with the registerMove method.
	 */
	@Override
	public Move getMove() {
		// TODO: Implement this.
		Map<Heuristic, Move> moves = getValidMoves();
		return null;
	}
	
	/**
	 * registerMove: Updates the AI's game state with the given move.
	 * @param m: The move to be played on the AI's game board.
	 * @return: Boolean denoting whether the placement was successful.
	 */
	public boolean registerMove(Move m) {
		// TODO: Implement this.
		return false;
	}
	
	public void addOpponent(Opponent opp) {
		_opponents.add(opp);
	}
	
	/**
	 * getFirstSettlement: 
	 * @return
	 */
	public BuildSettlement getFirstSettlement() {
		// TODO: Implement this.
		Vertex target = _board.mostValuableLegalVertex();
		return null;
	}
	
	public BuildRoad getFirstRoad() {
		// TODO: Implement this.
		return null;
	}
	
	public BuildSettlement getSecondSettlement() {
		// TODO: Implement this.
		return null;
	}
	
	public BuildRoad getSecondRoad() {
		// TODO: Implement this.
		return null;
	}

	@Override
	public int getVictoryPoints() {
		int vp = 0;
		for (DevCard c : _devcards) if (DEV_VP_VALUE.containsKey(c)) vp += DEV_VP_VALUE.get(c);
		vp += VP_SETTLEMENT * _settlements.size();
		vp += VP_CITY * _cities.size();
		if (this._largestArmy) vp += VP_LARG_ARMY;
		if (this._longestRoad) vp += VP_LONG_ROAD;
		return vp;
	}

	@Override
	public int longestRoadLength() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private void setGoal() {
		// TODO: Write this.
	}

	@Override
	protected Map<Heuristic, Move> getValidMoves() {
		HashMap<Heuristic, Move> toReturn = new HashMap<Heuristic, Move>();
		for (Heuristic h : Heuristic.values()) toReturn.put(h, playFromHeuristic(h));
		return toReturn;
	}
	
	private Opponent getLeader() {
		int vp = -1;
		Opponent leader = null;
		for (Opponent opp : _opponents) {
			if (opp.getVictoryPoints() > vp) {
				vp = opp.getVictoryPoints();
				leader = opp;
			}
		}
		return leader;
	}
	
	/**
	 * getAllMoves: Generates a set of all legal moves that the AI can play.
	 * @return: A set containing all legal moves for the AI to play.
	 */
	protected Set<Move> getAllMoves() {
		HashSet<Move> moves = new HashSet<Move>();
		moves.add(new NoMove());
		if (resForDevCard()) moves.add(new BuyDevCard(this));
		if (resForRoad()) {
			for (Edge e0 : _roads) {
				for (Edge e1 : e0.neighbors()) {
					if (!e1.road()) moves.add(new BuildRoad(this, e1));
				}
			}
		}
		if (resForSettlement()) {
			HashSet<Vertex> eligible = new HashSet<Vertex>();
			for (Edge e : _roads) {
				for (Vertex v0 : e.ends()) {
					boolean elig = true;
					for (Vertex v1 : v0.neighbors()) {
						if (v1.buildType() != BuildType.None) {
							elig = false;
							break;
						}
					}
					if (elig) eligible.add(v0);
				}
			}
			for (Vertex v : eligible) moves.add(new BuildSettlement(this, v));
		}
		if (resForCity()) for (Vertex v : _settlements) moves.add(new BuildCity(this, v));
		
		return moves;
	}
	
	private Move playFromHeuristic(Heuristic h) {
		Set<Move> allMoves = getAllMoves();
		switch (h) {
		case AttackLeader:
			Opponent target = getLeader();
			break;
		case AttackNeighbor:
			break;
		case Develop:
			break;
		case Expand:
			break;
		case Urbanize:
			break;
		default:
			break;
		}
		// TODO: Finish this.
		return null;
	}
	
	private boolean resForDevCard() {
		return brick() >= BRICK_DEV && 
				ore() >= ORE_DEV && 
				sheep() >= SHEEP_DEV && 
				timber() >= TIMBER_DEV && 
				wheat() >= WHEAT_DEV;
	}
	
	private boolean resForRoad() {
		return brick() >= BRICK_ROAD && 
				ore() >= ORE_ROAD && 
				sheep() >= SHEEP_ROAD && 
				timber() >= TIMBER_ROAD && 
				wheat() >= WHEAT_ROAD;
	}
	
	private boolean resForSettlement() {
		return brick() >= BRICK_SETTLEMENT && 
				ore() >= ORE_SETTLEMENT && 
				sheep() >= SHEEP_SETTLEMENT && 
				timber() >= TIMBER_SETTLEMENT && 
				wheat() >= WHEAT_SETTLEMENT;
	}
	
	private boolean resForCity() {
		return brick() >= BRICK_CITY && 
				ore() >= ORE_CITY && 
				sheep() >= SHEEP_CITY && 
				timber() >= TIMBER_CITY && 
				wheat() >= WHEAT_CITY;
	}
}
