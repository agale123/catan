package catanai;

public class BuildRoad extends Move {
	private Edge _target;
	
	public BuildRoad(Player mover, Edge target) {
		_mover = mover;
		_target = target;
		_isPlaced = true;
	}
	
	@Override
	public boolean place(GameBoard board) {
		// TODO Auto-generated method stub
		return board.placeRoad(_mover, _target);
	}
}
