package catanai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

public class AIPlayer extends Player implements AIConstants {
	private Map<String, Opponent> _opponents;
	private List<DevCard> _devcards;
	private Vertex _goal, _s0, _s1;
	private GameBoard _board;
	private Heuristic _lastHeuristic;
	
	public AIPlayer() {
		this(false);
	}
	
	public AIPlayer(boolean extended) {
		_opponents = new HashMap<String, Opponent>();
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
		Map<Heuristic, Move> moves = getValidMoves();
		double value = -1, t;
		Move best = null;
		for (Heuristic h : moves.keySet()) {
			t = this.valueMove(moves.get(h), LOOKAHEAD_RANGE) * ((h == _lastHeuristic)? HEURISTIC_MULT:1);
			if (t > value) {
				value = t;
				best = moves.get(h);
			}
		}
		return best;
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
	
	public void registerDieRoll(int r) {
		if (r <= 0 || r > DIE_FREQ.length || DIE_FREQ[r - 1] == 0) return;
		for (Vertex v : _settlements) {
			for (Tile t : v.tiles()) {
				if (t.roll() == r && TILE_RES.containsKey(t.resource())) {
					for (int i = 0; i < SETT_PAYOUT; i++) _hand.add(TILE_RES.get(t.resource()));
				}
			}
		}
		for (Vertex v : _cities) {
			for (Tile t : v.tiles()) {
				if (t.roll() == r && TILE_RES.containsKey(t.resource())) {
					for (int i = 0; i < CITY_PAYOUT; i++) _hand.add(TILE_RES.get(t.resource()));
				}
			}
		}
		for (Opponent opp : _opponents.values()) opp.registerDieRoll(r);
	}
	
	public void addOpponent(String id, Opponent opp) {
		_opponents.put(id, opp);
	}
	
	/**
	 * getFirstSettlement
	 * @return: Returns the move for the first round settlement placement.
	 */
	public BuildSettlement getFirstSettlement() {
		Vertex target = _board.mostValuableLegalVertex(this);
		_s0 = target;
		return new BuildSettlement(this, target);
	}
	
	public BuildRoad getFirstRoad() {
		Vertex next = _board.mostValuableLegalVertex(this, _s0.location(), GOAL_RADIUS);
		List<Edge> path = _board.shortestLegalPath(this, _s0, next);
		if (path.size() > 0) return new BuildRoad(this, path.get(0));
		else return null;
	}
	
	public BuildSettlement getSecondSettlement() {
		Vertex target = _board.mostValuableLegalVertex(this);
		_s1 = target;
		return new BuildSettlement(this, target);
	}
	
	public BuildRoad getSecondRoad() {
		Vertex next = _board.mostValuableLegalVertex(this, _s1.location(), GOAL_RADIUS);
		List<Edge> path = _board.shortestLegalPath(this, _s1, next);
		if (path.size() > 0) return new BuildRoad(this, path.get(0));
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
	
	public Opponent getOpponent(String id) {
		if (_opponents.containsKey(id)) return _opponents.get(id);
		else return null;
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
		for (Opponent opp : _opponents.values()) {
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
		case Spend:
			break;
		default:
			break;
		}
		// TODO: Finish this.
		return null;
	}

	@Override
	protected double valueMove(Move m, int lookahead) {
		// TODO: Auto-generated method stub
		return 0;
	}
}
