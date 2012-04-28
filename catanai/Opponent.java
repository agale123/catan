package catanai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Opponent extends Player implements AIConstants {
	public Opponent(gamelogic.PublicGameBoard pub, GameBoard board) {
		_publicBoard = pub;
		_board = board;
		_id = -1;
		_hand = new ArrayList<Resource>();
		_cities = new HashSet<Vertex>();
		_settlements = new HashSet<Vertex>();
		_roads = new HashSet<Edge>();
		_numCards = 0;
		_numDev = 0;
		_numKnight = 0;
		_longestRoad = false;
		_largestArmy = false;
	}
	
	@Override
	public Move getMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getVictoryPoints() {
		int vp = 0;
		vp += VP_SETTLEMENT * _settlements.size();
		vp += VP_CITY * _cities.size();
		double exp_vp = 0;
		// TODO: Improve this to implement Bayesian rationality.
		for (DevCard c : DEV_VP_VALUE.keySet()) exp_vp += DEV_VP_VALUE.get(c) * DEV_FREQ.get(c);
		vp += exp_vp * _numDev;
		if (this._largestArmy) vp += VP_LARG_ARMY;
		if (this._longestRoad) vp += VP_LONG_ROAD;
		return vp;
	}

	@Override
	protected Map<Heuristic, Move> getValidMoves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected double valueMove(Move m, int lookahead) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
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
	}

	@Override
	protected Move playFromHeuristic(Heuristic h) {
		// TODO Auto-generated method stub
		return null;
	}
}
