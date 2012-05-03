package catanai;

public class BuildRoad extends Move implements AIConstants {
	private Edge _target;
	
	public BuildRoad(Player mover, Edge target) {
		_mover = mover;
		_target = target;
		_isPlaced = true;
	}
	
	@Override
	public boolean place(GameBoard board) {
		return board.placeRoad(_mover, _target);
	}

	@Override
	public boolean make(gamelogic.PublicGameBoard board) {
		BoardCoordinate e0 = null, e1 = null;
		for (Vertex v : _target.ends()) {
			if (e0 == null) e0 = v.location();
			else e1 = v.location();
		}
		int v0 = -1, v1 = -1;
		for (int i = 0; i < NUM_VERTICES; i++) {
			if (X_GROUPS.get(e0.x()).contains(i) && Y_GROUPS.get(e0.y()).contains(i) && 
					Z_GROUPS.get(e0.z()).contains(i)) {
				v0 = i;
				if (v1 != -1) break;
			}
			else if (X_GROUPS.get(e1.x()).contains(i) && Y_GROUPS.get(e1.y()).contains(i) &&
					Z_GROUPS.get(e1.z()).contains(i)) {
				v1 = i;
				if (v0 != -1) break;
			}
		}
		if (v0 == -1 || v1 == -1) return false;
		gamelogic.CoordPair c0 = board.getCoordsFromInt(v0), c1 = board.getCoordsFromInt(v1);
		return board.canBuildRoad(Integer.parseInt(_mover.getID()), c0.getX(), c0.getY(), c1.getX(), c1.getY());
	}

	@Override
	public void broadcast(AIPlayer p) {
		// TODO Auto-generated method stub
		
	}
}
