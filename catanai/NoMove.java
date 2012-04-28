package catanai;

public class NoMove extends Move {

	public NoMove() {
		_mover = null;
	}
	
	@Override
	public boolean place(gamelogic.PublicGameBoard pub, GameBoard board) {
		// TODO Auto-generated method stub
		return false;
	}

}
