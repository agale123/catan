package catanai;

public class BuildSettlement extends Move {
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

}
