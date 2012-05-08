package catanai;

import java.util.List;
import java.util.Map;

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
		return board.canBuildCity(Integer.parseInt(_mover.getID()), coords.getX(), coords.getY());
	}

	@Override
	public void broadcast(AIPlayer p, gamelogic.PublicGameBoard board) {
		String message = "5/" + p.getID() + ",";
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
	public void charge() {
		_mover.chargeForCity();
	}
}
