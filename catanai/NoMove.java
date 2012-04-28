package catanai;

public class NoMove extends Move {

	public NoMove() {
		_mover = null;
	}
	
	@Override
	public boolean place(GameBoard board) {
		return false;
	}

}
