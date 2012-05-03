package catanai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.List;

public class AIPlayer extends Player implements AIConstants {
	private List<DevCard> _devcards;
	private Vertex _goal, _s0, _s1;
	private Heuristic _lastHeuristic;
	private server.Server _server;
	
	public AIPlayer(gamelogic.PublicGameBoard board, String id, server.Server serve) {
		_hand = new ArrayList<Resource>();
		_cities = new HashSet<Vertex>();
		_settlements = new HashSet<Vertex>();
		_roads = new HashSet<Edge>();
		_numCards = 0;
		_numDev = 0;
		_numKnight = 0;
		_longestRoad = false;
		_largestArmy = false;
		_opponents = new HashMap<String, Opponent>();
		_devcards = new ArrayList<DevCard>();
		_goal = null;
		_board = new GameBoard(false);
		_lastHeuristic = null;
		_server = serve;
		_id = id;
		_publicBoard = board;
		_board.getResourceInfo(_publicBoard);
		_board.getRollInfo(_publicBoard);
	}
	
	public void playFirstRound() {
		for (int i = 0; i < 2 * BRICK_SETTLEMENT; i++) draw(Resource.Brick);
		for (int i = 0; i < 2 * SHEEP_SETTLEMENT; i++) draw(Resource.Sheep);
		for (int i = 0; i < 2 * WHEAT_SETTLEMENT; i++) draw(Resource.Wheat);
		for (int i = 0; i < 2 * TIMBER_SETTLEMENT; i++) draw(Resource.Timber);
		for (int i = 0; i < 2 * ORE_SETTLEMENT; i++) draw(Resource.Ore);
		for (int i = 0; i < 2 * BRICK_ROAD; i++) draw(Resource.Brick);
		for (int i = 0; i < 2 * SHEEP_ROAD; i++) draw(Resource.Sheep);
		for (int i = 0; i < 2 * WHEAT_ROAD; i++) draw(Resource.Wheat);
		for (int i = 0; i < 2 * TIMBER_ROAD; i++) draw(Resource.Timber);
		for (int i = 0; i < 2 * ORE_ROAD; i++) draw(Resource.Ore);
		System.out.println("First settlement success: " + Boolean.toString(makeMove(getFirstSettlement()))); // TODO: Debug line
		System.out.println("First road success: " + Boolean.toString(makeMove(getFirstRoad()))); // TODO: Debug line
		System.out.println("Second settlement success: " + Boolean.toString(makeMove(getSecondSettlement()))); // TODO: Debug line
		System.out.println("Second road success: " + Boolean.toString(makeMove(getSecondRoad()))); // TODO: Debug line
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
		Move best = new NoMove();
		Heuristic heur = null;
		for (Heuristic h : moves.keySet()) {
			t = this.valueMove(moves.get(h), _board, LOOKAHEAD_RANGE) * getHeurFact(h);
			if (t > value) {
				value = t;
				best = moves.get(h);
				heur = h;
			}
		}
		if (heur != null) _lastHeuristic = heur;
		return best;
	}
	
	/**
	 * registerMove: Updates the AI's game state with the given move.
	 * @param m: The move to be played on the AI's game board.
	 * @return: Boolean denoting whether the placement was successful.
	 */
	public boolean registerMove(Move m) {
		boolean succ = m.place(_board);
		if (_goal == null || ! _goal.isLegal(this)) setGoal();
		makeMove(getMove());
		return succ;
	}
	
	public boolean registerInitialSettlement(BuildSettlement s) {
		boolean succ = s.placeInitial(_board);
		if (_goal == null || ! _goal.isLegal(this)) setGoal();
		return succ;
	}
	
	public boolean registerInitialRoad(BuildRoad r) {
		boolean succ = r.place(_board);
		if (_goal == null || ! _goal.isLegal(this)) setGoal();
		return succ;
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
	
	public boolean makeMove(Move m) {
		if (! (m instanceof NoMove) && m.make(_publicBoard)) {
			m.place(_board);
			m.broadcast(this);
			return true;
		}
		else return false;
	}
	
	@Override
	public void addOpponent(String id) {
		Opponent opp = new Opponent(_publicBoard, _board, id);
		if (! (_opponents.containsKey(id) || id.equals(this._id))) {
			for (String d : _opponents.keySet()) {
				opp.addOpponent(d);
				_opponents.get(d).addOpponent(id);
			}
			_opponents.put(id, opp);
		}
	}
	
	/**
	 * getFirstSettlement
	 * @return: Returns the move for the first round settlement placement.
	 */
	public BuildSettlement getFirstSettlement() {
		Vertex target = _board.mostValuableLegalVertex(this);
		_s0 = target;
		System.out.println("First settlement at " + target.toString() + "."); // TODO: Debug line
		return new BuildSettlement(this, target);
	}
	
	public BuildRoad getFirstRoad() {
		if (_s0 == null) System.out.println("_s0 is null, program is about to fail."); // TODO: Debug line
		Vertex next = _board.mostValuableLegalVertex(this, _s0.location(), GOAL_RADIUS);
		List<Edge> path = _board.shortestLegalPath(this, _s0, next);
		if (path.size() > 0) {
			System.out.println("First road " + path.get(0).toString()); // TODO: Debug line
			return new BuildRoad(this, path.get(0));
		}
		else return null;
	}
	
	public BuildSettlement getSecondSettlement() {
		Vertex target = _board.mostValuableLegalVertex(this);
		_s1 = target;
		System.out.println("Second settlement at " + target.toString() + "."); // TODO: Debug line
		return new BuildSettlement(this, target);
	}
	
	public BuildRoad getSecondRoad() {
		Vertex next = _board.mostValuableLegalVertex(this, _s1.location(), GOAL_RADIUS);
		List<Edge> path = _board.shortestLegalPath(this, _s1, next);
		if (path.size() > 0) {
			System.out.println("Second road " + path.get(0).toString()); // TODO: Debug line
			return new BuildRoad(this, path.get(0));
		}
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
	
	public Player getPlayer(String id) {
		if (id.equals(this._id)) return this;
		else if (_opponents.containsKey(id)) return _opponents.get(id);
		else return null;
	}

	private void setGoal() {
		Vertex b = null, c;
		double value = 0;
		for (Vertex v : _settlements) {
			c = _board.mostValuableLegalVertex(this, v.location(), GOAL_RADIUS);
			if (c.value() > value) {
				b = c;
				value = c.value();
			}
		}
		_goal = b;
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
					if (! e1.road()) moves.add(new BuildRoad(this, e1));
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
	
	protected Set<Swap> getAllSwaps() {
		// TODO: Implement this.
		return null;
	}
	
	protected Move playFromHeuristic(Heuristic h) {
		switch (h) {
		case AttackLeader:
			Opponent target = getLeader();
			if (_devcards.contains(DevCard.Knight)) return new PlayKnight(this, target.bestTile(this));
			else if (_devcards.contains(DevCard.Monopoly)) return new PlayMonopoly(this, neededResource());
			else if (resForDevCard()) return new BuyDevCard(this);
			else return new NoMove();
		case AttackNeighbor:
			// TODO: Finish this case.
			return new NoMove();
		case Develop:
			if (resForDevCard()) return new BuyDevCard(this);
			else if (_devcards.contains(DevCard.Knight)) return new PlayKnight(this, getLeader().bestTile(this));
			else if (_devcards.contains(DevCard.Monopoly)) return new PlayMonopoly(this, neededResource());
			else if (_devcards.contains(DevCard.YearOfPlenty)) return new PlayYrOfPlenty(this, neededResource());
			else return new NoMove();
		case Expand:
			if (_goal != null && _goal.hasIncRoad(this)) {
				if (resForSettlement()) return new BuildSettlement(this, _goal);
				else if (_devcards.contains(DevCard.Monopoly)) return new PlayMonopoly(this, neededResource());
				else if (_devcards.contains(DevCard.YearOfPlenty)) return new PlayYrOfPlenty(this, neededResource());
				else return new NoMove();
			}
			else {
				Edge e0, e1;
				if (resForRoad()) {
					List<Edge> path = _board.shortestLegalPathFromPlayer(this, _goal);
					if (path != null && path.size() > 0) {
						e0 = path.get(0);
						return new BuildRoad(this, e0);
					}
					else {
						setGoal();
						return new NoMove();
					}
				}
				else if (_devcards.contains(DevCard.RoadBuilding)) {
					List<Edge> path = _board.shortestLegalPathFromPlayer(this, _goal);
					e0 = path.get(0);
					e1 = path.get(1);
					return new PlayRoadBldg(this, e0, e1);
				}
				else if (_devcards.contains(DevCard.YearOfPlenty)) return new PlayYrOfPlenty(this, neededResource());
				else return new NoMove();
			}
		case Urbanize:
			if (resForCity() && _settlements.size() > 0) {
				Vertex b = null;
				double value = 0;
				for (Vertex v : _settlements) {
					if (v.value() > value) {
						b = v;
						value = v.value();
					}
				}
				return new BuildCity(this, b);
			}
			else if (_devcards.contains(DevCard.Monopoly)) return new PlayMonopoly(this, neededResource());
			else if (_devcards.contains(DevCard.YearOfPlenty)) return new PlayYrOfPlenty(this, neededResource());
			else return new NoMove();
		case Spend:
			if (resForCity() && _settlements.size() > 0) {
				Vertex b = null;
				double value = 0;
				for (Vertex v : _settlements) {
					if (v.value() > value) {
						b = v;
						value = v.value();
					}
				}
				return new BuildCity(this, b);
			}
			else if (resForSettlement() && _goal != null && _goal.hasIncRoad(this)) return new BuildSettlement(this, _goal);
			else if (resForDevCard()) return new BuyDevCard(this);
			else if (resForRoad() && _goal != null && ! _goal.hasIncRoad(this)) {
				Edge e2 = _board.shortestLegalPathFromPlayer(this, _goal).get(0);
				return new BuildRoad(this, e2);
			}
			else return new NoMove();
		case Hoard:
			if (_devcards.contains(DevCard.Monopoly)) return new PlayMonopoly(this, neededResource());
			else if (_devcards.contains(DevCard.YearOfPlenty)) return new PlayYrOfPlenty(this, neededResource());
			else return new NoMove();
		default:
			return new NoMove();
		}
		// TODO: Finish this.
	}

	@Override
	protected double valueMove(Move m, GameBoard board, int lookahead) {
		// TODO: Implement this.
		if (m instanceof NoMove) return 0;
		Random rand = new Random();
		return rand.nextDouble();
	}
	
	private double getHeurFact(Heuristic h) {
		// TODO: Implement this.
		return (h == _lastHeuristic)? HEURISTIC_MULT:1;
	}
	
	public Edge getEdgeFromBoard(int i, int j) {
		return _board.getEdgeByInt(i, j);
	}
	
	public Vertex getVertexFromBoard(int v) {
		return _board.getVertexByInt(v);
	}
	
	private Resource neededResource() {
		if (_goal == null) {
			int b = brick(), o = ore(), s = sheep(), t = timber(), w = wheat();
			ArrayList<Integer> cmp = new ArrayList<Integer>(Arrays.asList(b, o, s, t, w));
			int scarce = Collections.min(cmp);
			if (scarce == b) return Resource.Brick;
			else if (scarce == o) return Resource.Ore;
			else if (scarce == s) return Resource.Sheep;
			else if (scarce == t) return Resource.Timber;
			else return Resource.Wheat;
		}
		else if (_goal.hasIncRoad(this)) {
			if (brick() < BRICK_SETTLEMENT) return Resource.Brick;
			else if (ore() < ORE_SETTLEMENT) return Resource.Ore;
			else if (sheep() < SHEEP_SETTLEMENT) return Resource.Sheep;
			else if (timber() < TIMBER_SETTLEMENT) return Resource.Timber;
			else return Resource.Wheat;
		}
		else if (_settlements.size() == SETTLE_CEIL) {
			if (brick() < BRICK_CITY) return Resource.Brick;
			else if (ore() < ORE_CITY) return Resource.Ore;
			else if (sheep() < SHEEP_CITY) return Resource.Sheep;
			else if (timber() < TIMBER_CITY) return Resource.Timber;
			else return Resource.Wheat;
		}
		else {
			if (brick() < BRICK_ROAD) return Resource.Brick;
			else if (ore() < ORE_ROAD) return Resource.Ore;
			else if (sheep() < SHEEP_ROAD) return Resource.Sheep;
			else if (timber() < TIMBER_ROAD) return Resource.Timber;
			else return Resource.Wheat;
		}
	}
	
	public void broadcast(String message) {
		server.ClientPool clients = _server.getClientPool();
		clients.broadcast(message, null);
	}
}
