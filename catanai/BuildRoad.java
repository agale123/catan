package catanai;

import java.util.List;
import java.util.Map;

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
		boolean exp = board.isExpanded();
		int nv = (exp)? NUM_VERTICES_EXP:NUM_VERTICES;
		Map<Integer, List<Integer>> x_gr = (exp)? X_GROUPS_EXP:X_GROUPS;
		Map<Integer, List<Integer>> y_gr = (exp)? Y_GROUPS_EXP:Y_GROUPS;
		Map<Integer, List<Integer>> z_gr = (exp)? Z_GROUPS_EXP:Z_GROUPS;
		for (int i = 0; i < nv; i++) {
			if (x_gr.get(e0.x()).contains(i) && y_gr.get(e0.y()).contains(i) && 
					z_gr.get(e0.z()).contains(i)) {
				v0 = i;
				if (v1 != -1) break;
			}
			else if (x_gr.get(e1.x()).contains(i) && y_gr.get(e1.y()).contains(i) &&
					z_gr.get(e1.z()).contains(i)) {
				v1 = i;
				if (v0 != -1) break;
			}
		}
		if (v0 == -1 || v1 == -1) return false;
		gamelogic.CoordPair c0 = board.getCoordsFromInt(v0), c1 = board.getCoordsFromInt(v1);
		return board.canBuildRoad(Integer.parseInt(_mover.getID()), c0.getX(), c0.getY(), c1.getX(), c1.getY());
	}

	@Override
	public void broadcast(AIPlayer p, gamelogic.PublicGameBoard board) {
		BoardCoordinate e0 = null, e1 = null;
		for (Vertex v : _target.ends()) {
			if (e0 == null) e0 = v.location();
			else e1 = v.location();
		}
		int v0 = -1, v1 = -1;
		boolean exp = board.isExpanded();
		int nv = (exp)? NUM_VERTICES_EXP:NUM_VERTICES;
		Map<Integer, List<Integer>> x_gr = (exp)? X_GROUPS_EXP:X_GROUPS;
		Map<Integer, List<Integer>> y_gr = (exp)? Y_GROUPS_EXP:Y_GROUPS;
		Map<Integer, List<Integer>> z_gr = (exp)? Z_GROUPS_EXP:Z_GROUPS;
		for (int i = 0; i < nv; i++) {
			if (x_gr.get(e0.x()).contains(i) && y_gr.get(e0.y()).contains(i) && 
					z_gr.get(e0.z()).contains(i)) {
				v0 = i;
				if (v1 != -1) break;
			}
			else if (x_gr.get(e1.x()).contains(i) && y_gr.get(e1.y()).contains(i) &&
					z_gr.get(e1.z()).contains(i)) {
				v1 = i;
				if (v0 != -1) break;
			}
		}
		if (v0 == -1 || v1 == -1) return;
		gamelogic.CoordPair c0 = board.getCoordsFromInt(v0), c1 = board.getCoordsFromInt(v1);
		int messageInt[] = {c0.getX(), c0.getY(), c1.getX(), c1.getY()};
		String messageComp[] = new String[messageInt.length];
		for (int i = 0; i < messageComp.length; i++) messageComp[i] = Integer.toString(messageInt[i]);
		String message = "3/" + p.getID() + ",";
		for (int i = 0; i < messageComp.length; i++) 
			message += messageComp[i] + ((i == messageComp.length - 1)? "":",");
		p.broadcast(message);
	}

	@Override
	public void charge() {
		_mover.chargeForRoad();
	}
}
