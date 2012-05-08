package catanai;

import java.util.List;
import java.util.Map;

import gamelogic.PublicGameBoard;

public class BuildSettlement extends Move implements AIConstants {
	private Vertex _target;
	
	public BuildSettlement(Player pl, Vertex target) {
		if (target == null) throw new NullPointerException("Ye cannot nullify ye target!");
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
		boolean exp = board.isExpanded();
		int nv = (exp)? NUM_VERTICES_EXP:NUM_VERTICES;
		Map<Integer, List<Integer>> x_gr = (exp)? X_GROUPS_EXP:X_GROUPS;
		Map<Integer, List<Integer>> y_gr = (exp)? Y_GROUPS_EXP:Y_GROUPS;
		Map<Integer, List<Integer>> z_gr = (exp)? Z_GROUPS_EXP:Z_GROUPS;
		for (int i = 0; i < nv; i++) {
			if (x_gr.get(c.x()).contains(i) && y_gr.get(c.y()).contains(i) && 
					z_gr.get(c.z()).contains(i)) {
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
		boolean exp = board.isExpanded();
		int nv = (exp)? NUM_VERTICES_EXP:NUM_VERTICES;
		Map<Integer, List<Integer>> x_gr = (exp)? X_GROUPS_EXP:X_GROUPS;
		Map<Integer, List<Integer>> y_gr = (exp)? Y_GROUPS_EXP:Y_GROUPS;
		Map<Integer, List<Integer>> z_gr = (exp)? Z_GROUPS_EXP:Z_GROUPS;
		for (int i = 0; i < nv; i++) {
			if (x_gr.get(c.x()).contains(i) && y_gr.get(c.y()).contains(i) && 
					z_gr.get(c.z()).contains(i)) {
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
	
	@Override
	public String toString() {
		return "Build settlement at " + _target.toString();
	}

	@Override
	public void charge() {
		_mover.chargeForSettlement();
	}
}
