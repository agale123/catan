package catanai;

public class BuildCity extends Move {
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
}
