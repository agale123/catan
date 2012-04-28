package catanai;

public class PlayRoadBldg extends Move {
	private Edge _e0, _e1;
	
	public PlayRoadBldg(Player p, Edge e0, Edge e1) {
		_mover = p;
		_isPlaced = false;
		_e0 = e0;
		_e1 = e1;
	}
	@Override
	public boolean place(GameBoard board) {
		// TODO Auto-generated method stub
		boolean s0, s1;
		if (s0 = board.placeRoad(_mover, _e0)) {
			_mover.addRoad(_e0);
		}
		if (s1 = board.placeRoad(_mover, _e1)) {
			_mover.addRoad(_e1);
		}
		return s0 && s1;
	}

}
