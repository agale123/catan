package catanai;

import gamelogic.PublicGameBoard;

public class BuildSettlement extends Move implements AIConstants {
	private Vertex _target;
	
	public BuildSettlement(Player pl, Vertex target) {
		_mover = pl;
		_target = target;
		_isPlaced = true;
	}
	
	@Override
	public boolean place(GameBoard board) {
		if (board.placeSettlement(_mover, _target)) {
			_mover.addSettlement(_target);
			return true;
		}
		else return false;
	}
	
	public boolean placeInitial(GameBoard board) {
		if (board.placeInitialSettlement(_mover, _target)) {
			_mover.addSettlement(_target);
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
		if (v == -1) {
			System.out.println("Could not locate proper vertex index!"); // TODO: Debug line
			return false;
		}
		gamelogic.CoordPair coords = board.getCoordsFromInt(v);
		return board.canBuildSettlement(Integer.parseInt(_mover.getID()), coords.getX(), coords.getY());
	}

	@Override
	public void broadcast(AIPlayer p, gamelogic.PublicGameBoard board) {
		String message = "4/" + p.getID() + ",";
		BoardCoordinate c = _target.location();
		int v = -1;
		for (int i = 0; i < NUM_VERTICES; i++) {
			if (X_GROUPS.get(c.x()).contains(i) && Y_GROUPS.get(c.y()).contains(i) && 
					Z_GROUPS.get(c.z()).contains(i)) {
				v = i;
				break;
			}
		}
		if (v == -1) {
			System.out.println("Could not locate proper vertex index!"); // TODO: Debug line
			return;
		}
		gamelogic.CoordPair coords = board.getCoordsFromInt(v);
		message += Integer.toString(coords.getX()) + "," + Integer.toString(coords.getY());
		p.broadcast(message);
	}

}
