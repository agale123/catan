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
	private boolean _exp;
	private List<DevCard> _devcards;
	private Vertex _goal, _s0, _s1;
	private Heuristic _lastHeuristic;
	private server.Server _server;
	private Map<Integer, FulfillTrade> _trades;
	private List<ProposeTrade> _pendingTrades;
	
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
		_exp = false;
		_lastHeuristic = null;
		_server = serve;
		_trades = new HashMap<Integer, FulfillTrade>();
		_pendingTrades = new ArrayList<ProposeTrade>(MAX_PEND_TRADE);
		_id = id;
		_publicBoard = board;
		_board.getResourceInfo(_publicBoard, _exp);
		_board.getRollInfo(_publicBoard, _exp);
	}
	
	public AIPlayer(gamelogic.PublicGameBoard board, String id, server.Server serve, boolean exp) {
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
		_board = new GameBoard(exp);
		_exp = exp;
		_lastHeuristic = null;
		_server = serve;
		_trades = new HashMap<Integer, FulfillTrade>();
		_pendingTrades = new ArrayList<ProposeTrade>(MAX_PEND_TRADE);
		_id = id;
		_publicBoard = board;
		_board.getResourceInfo(_publicBoard, _exp);
		_board.getRollInfo(_publicBoard, _exp);
	}
	
	public void playFirstRound() {
		introduceDelay(PLAY_DELAY);
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
		makeMove(getFirstSettlement());
		makeMove(getFirstRoad());
		makeMove(getSecondSettlement());
		makeMove(getSecondRoad());
		collectFromVertex(_s1);
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
		if (m.getMover() == this) return false;
		if (m instanceof ProposeTrade) return registerTrade((ProposeTrade) m);
		boolean succ = m.place(_board);
		if (succ) m.charge();
		if (_goal == null || ! isFeasibleGoal(_goal)) setGoal();
		makeMove(getMove());
		return succ;
	}
	
	public boolean registerTrade(ProposeTrade tr) {
		if (_trades.containsKey(tr.getID())) return false;
		_trades.put(tr.getID(), tr.fulfill(this));
		return true;
	}
	
	public void completeTrade(FulfillTrade tr) {
		System.out.println("completeTrade is being called"); // TODO: Debug line
		_trades.remove(tr.getID());
		if (tr.getMover() == this || tr.getRecipient() == this) {
			for (ProposeTrade pr : _pendingTrades) {
				if (pr.getID() == tr.getID()) {
					_pendingTrades.remove(pr);
					break;
				}
			}
		}
	}
	
	public void addPendingTrade(ProposeTrade tr) {
		if (_pendingTrades.size() >= MAX_PEND_TRADE) return;
		_pendingTrades.add(tr);
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
		this.printResources(); // TODO: Debug line
		makeMove(getMove());
	}
	
	public boolean makeMove(Move m) {
		if (m instanceof ProposeTrade) {
			System.out.println("AI is proposing a trade..."); // TODO: Debug line
			System.out.println(m.toString()); // TODO: Debug line
			this.printResources();
		}
		if (m.make(_publicBoard)) {
			m.place(_board);
			m.charge();
			m.broadcast(this, _publicBoard);
			
			int aiPoints = 0;
			aiPoints += _settlements.size();
			aiPoints += _cities.size() * 2;
			if(_longestRoad) {
				aiPoints += 2;
			}
			if(aiPoints >= _publicBoard.getPointsToWin()) {
				_server.sendWin(Integer.parseInt(_id));
			}
			
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
		Vertex next = _board.mostValuableLegalVertex(this, _s0.location(), GOAL_RADIUS);
		List<Edge> path = _board.shortestLegalPath(this, _s0, next);
		if (path.size() > 0) return new BuildRoad(this, path.get(0));
		else {
			System.out.println("getFirstRoad is returning null!"); // TODO: Debug line
			return null;
		}
	}
	
	public BuildSettlement getSecondSettlement() {
		double value = -9999, t_val = 0, mult = 0;
		Vertex target = null;
		for (Vertex v : _board.getVertexSet()) {
			if (! v.isLegal(this)) continue;
			t_val = v.value();
			mult = v.numResourceOver(_s0);
			if (mult == 0) mult = 0.5;
			t_val *= mult;
			if (t_val > value) {
				target = v;
				value = t_val;
			}
		}
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
	
	public Player getPlayer(String id) {
		if (id.equals(this._id)) return this;
		else if (_opponents.containsKey(id)) return _opponents.get(id);
		else {
			System.out.println("Player not found for ID " + id + "."); // TODO: Debug line
			return null;
		}
	}

	private void setGoal() {
		Vertex b = null, c;
		double value = 0;
		for (Vertex v : _settlements) {
			c = _board.mostValuableLegalVertex(this, v.location(), GOAL_RADIUS);
			if (c == null) continue;
			if (c.value() > value && isFeasibleGoal(c)) {
				b = c;
				value = c.value();
			}
		}
		_goal = b;
	}
	
	public boolean isFeasibleGoal(Vertex v) {
		if (v == null) return true;
		List<Edge> path = _board.shortestLegalPathFromPlayer(this, v);
		if (path == null || ! v.isLegal(this)) return false;
		return path.size() <= MAX_PATH_LENGTH;
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
	
	protected Move playFromHeuristic(Heuristic h) {
		if (_goal == null || ! isFeasibleGoal(_goal)) setGoal();
		FulfillTrade f;
		ProposeTrade p;
		switch (h) {
		case AttackLeader:
			Opponent target = getLeader();
			if (_devcards.contains(DevCard.Knight)) return new PlayKnight(this, target.bestTile(this));
			else if (_devcards.contains(DevCard.Monopoly)) return new PlayMonopoly(this, neededResource());
			else if (resForDevCard()) return new BuyDevCard(this);
			else if ((f = canTradeFor(SpendType.DevCard)) != null) return f;
			else if ((p = makeTradeFor(SpendType.DevCard)) != null) return p;
			else return new NoMove();
		case AttackNeighbor:
			// TODO: Finish this case.
			return new NoMove();
		case Develop:
			if (resForDevCard()) return new BuyDevCard(this);
			else if (_devcards.contains(DevCard.Knight)) return new PlayKnight(this, getLeader().bestTile(this));
			else if (_devcards.contains(DevCard.Monopoly)) return new PlayMonopoly(this, neededResource());
			else if (_devcards.contains(DevCard.YearOfPlenty)) return new PlayYrOfPlenty(this, neededResource());
			else if ((f = canTradeFor(SpendType.DevCard)) != null) return f;
			else if ((p = makeTradeFor(SpendType.DevCard)) != null) return p;
			else return new NoMove();
		case Expand:
			if (_goal == null) return new NoMove();
			if (_goal.hasIncRoad(this)) {
				if (resForSettlement()) return new BuildSettlement(this, _goal);
				else if (_devcards.contains(DevCard.Monopoly)) return new PlayMonopoly(this, neededResource());
				else if (_devcards.contains(DevCard.YearOfPlenty)) return new PlayYrOfPlenty(this, neededResource());
				else if ((f = canTradeFor(SpendType.Settlement)) != null) return f;
				else if ((p = makeTradeFor(SpendType.Settlement)) != null) return p;
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
				else if ((f = canTradeFor(SpendType.Road)) != null) return f;
				else if ((p = makeTradeFor(SpendType.Road)) != null) return p;
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
			else if (_settlements.size() > 0) {
				if ((f = canTradeFor(SpendType.City)) != null) return f;
				else if ((p = makeTradeFor(SpendType.City)) != null) return p;
			}
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
				List<Edge> sPath = _board.shortestLegalPathFromPlayer(this, _goal);
				if (sPath != null && sPath.size() > 0) {
					Edge e2 = sPath.get(0);
					return new BuildRoad(this, e2);
				}
				else {
					setGoal();
					return new NoMove();
				}
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
		else if (m instanceof BuyDevCard) return 1;
		else if (m instanceof ProposeTrade || m instanceof FulfillTrade) return 2;
		else if (m instanceof BuildRoad) return 3;
		else if (m instanceof BuildSettlement) return 4;
		else if (m instanceof BuildCity) return 5;
		else return 0;
	}
	
	private double getHeurFact(Heuristic h) {
		// TODO: Implement this.
		return (h == _lastHeuristic)? HEURISTIC_MULT:1;
	}
	
	public Edge getEdgeFromBoard(int i, int j) {
		return (_exp)? _board.getEdgeByIntExp(i, j):_board.getEdgeByInt(i, j);
	}
	
	public Vertex getVertexFromBoard(int v) {
		return (_exp)? _board.getVertexByIntExp(v):_board.getVertexByInt(v);
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
	
	private Map<Resource, Integer> neededResources(SpendType t) {
		HashMap<Resource, Integer> ret = new HashMap<Resource, Integer>();
		switch (t) {
		case Settlement:
			ret.put(Resource.Brick, BRICK_SETTLEMENT - brick());
			ret.put(Resource.Ore, ORE_SETTLEMENT - ore());
			ret.put(Resource.Sheep, SHEEP_SETTLEMENT - sheep());
			ret.put(Resource.Timber, TIMBER_SETTLEMENT - timber());
			ret.put(Resource.Wheat, WHEAT_SETTLEMENT - wheat());
			break;
		case City:
			ret.put(Resource.Brick, BRICK_CITY - brick());
			ret.put(Resource.Ore, ORE_CITY - ore());
			ret.put(Resource.Sheep, SHEEP_CITY - sheep());
			ret.put(Resource.Timber, TIMBER_CITY - timber());
			ret.put(Resource.Wheat, WHEAT_CITY - wheat());
			break;
		case Road:
			ret.put(Resource.Brick, BRICK_ROAD - brick());
			ret.put(Resource.Ore, ORE_ROAD - ore());
			ret.put(Resource.Sheep, SHEEP_ROAD - sheep());
			ret.put(Resource.Timber, TIMBER_ROAD - timber());
			ret.put(Resource.Wheat, WHEAT_ROAD - wheat());
			break;
		case DevCard:
			ret.put(Resource.Brick, BRICK_DEV - brick());
			ret.put(Resource.Ore, ORE_DEV - ore());
			ret.put(Resource.Sheep, SHEEP_DEV - sheep());
			ret.put(Resource.Timber, TIMBER_DEV - timber());
			ret.put(Resource.Wheat, WHEAT_DEV - wheat());
			break;
		default:
			return null;
		}
		return ret;
	}
	
	private FulfillTrade canTradeFor(SpendType t) {
		Map<Resource, Integer> res = neededResources(t);
		if (res == null) return null;
		FulfillTrade ret = null, tr;
		int suited = 999999, st;
		List<Resource> to, from;
		int to_i, from_i, needed, net_gain;
		l0: for (int id : _trades.keySet()) {
			tr = _trades.get(id);
			st = 0;
			to = tr.getTo();
			from = tr.getFrom();
			for (Resource r : res.keySet()) {
				to_i = numResInList(r, to);
				from_i = numResInList(r, from);
				needed = res.get(r);
				net_gain = from_i - to_i;
				if (net_gain < 0 && ! hasNumRes(r, net_gain * -1)) continue l0;
				if (net_gain < 0 && needed > net_gain) continue l0;
				st += Math.abs(net_gain - needed);
			}
			if (st < suited) {
				ret = tr;
				suited = st;
			}
		}
		return ret;
	}
	
	private ProposeTrade makeTradeFor(SpendType t) {
		if (_pendingTrades.size() == MAX_PEND_TRADE) return null;
		Map<Resource, Integer> res = neededResources(t);
		if (res == null) return null;
		HashSet<Resource> excess = new HashSet<Resource>();
		HashSet<Resource> scarce = new HashSet<Resource>();
		for (Resource r : res.keySet()) {
			if (res.get(r) > 0) scarce.add(r);
			else if (res.get(r) < 0) excess.add(r);
		}
		Resource most_excess = null, most_scarce = null;
		for (Resource r : excess)
			if (most_excess == null || res.get(r) < res.get(most_excess))
				most_excess = r;
		for (Resource r : scarce)
			if (most_scarce == null || res.get(r) > res.get(most_scarce))
				most_scarce = r;
		if (most_excess == null || most_scarce == null) return null;
		List<Resource> to = Arrays.asList(most_excess);
		List<Resource> from = Arrays.asList(most_scarce);
		for (ProposeTrade tr : _pendingTrades)
			if (sameRes(to, tr.getTo()) && sameRes(from, tr.getFrom())) return null;
		return new ProposeTrade(this, to, from, _server);
	}
	
	private boolean hasNumRes(Resource r, int n) {
		ArrayList<Resource> list = new ArrayList<Resource>(n);
		for (int i = 0; i < n; i++) list.add(r);
		return this.hasList(list);
	}
	
	private boolean sameRes(List<Resource> a, List<Resource> b) {
		for (Resource r : Resource.values())
			if (numResInList(r, a) != numResInList(r, b)) return false;
		return true;
	}

	private int numResInList(Resource r, List<Resource> set) {
		int ret = 0;
		for (Resource s : set) if (r == s) ret++;
		return ret;
	}
	
	public server.ClientPool getServerClientPool() {
		return _server.getClientPool();
	}
	
	public void broadcast(Object message) {
		server.ClientPool clients = _server.getClientPool();
		clients.broadcast(message, null);
	}
	
	public void broadcastTo(Object message, int id) {
		server.ClientPool clients = _server.getClientPool();
		clients.broadcastTo(message, id);
	}
	
	public void broadcastToElse(Object message, int id0, int id1) {
		server.ClientPool clients = _server.getClientPool();
		clients.broadcastToElse(message, id0, id1);
	}
	
	private void introduceDelay(long ms) {
		long now = System.currentTimeMillis(), curr;
		do {curr = System.currentTimeMillis();}
		while (curr - now < ms);
	}
}