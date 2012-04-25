package catanai;

import java.util.Map;
import java.util.Set;

public class Opponent extends Player implements AIConstants {
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
	public int longestRoadLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Map<Heuristic, Move> getValidMoves() {
		// TODO Auto-generated method stub
		return null;
	}
}
