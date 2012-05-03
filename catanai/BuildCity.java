package catanai;

import gamelogic.PublicGameBoard;

public class BuildCity extends Move implements AIConstants {
	private Vertex _target;
	
	public BuildCity(Player pl, Vertex target) {
		_mover = pl;
		_target = target;
	}
	
	@Override
	public boolean place(GameBoard board) {
		// TODO Auto-generated method stub
		if (board.placeCity(_mover, _target)) {
			_mover.addCity(_target);
			return true;
		}
		else return false;
	}

	@Override
	public boolean make(PublicGameBoard board) {
		BoardCoordinate c = _target.location();
		int v = -1;
		for (int i = 0; i < NUM_VERTICES; i++) {
			if (X_GROUPS.get(c.x()).contains(i) && Y_GROUPS.get(c.y()).contains(i) && 
					Z_GROUPS.get(c.z()).contains(i)) {
				v = i;
				break;
			}
		}
		if (v == -1) return false;
		gamelogic.CoordPair coords = board.getCoordsFromInt(v);
		return board.canBuildCity(Integer.parseInt(_mover.getID()), coords.getX(), coords.getY());
	}

	@Override
	public void broadcast(AIPlayer p, gamelogic.PublicGameBoard board) {
		// TODO Auto-generated method stub
		
	}
}
