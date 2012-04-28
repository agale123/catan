package catanai;

import gamelogic.PublicGameBoard;

public class NoMove extends Move {

	public NoMove() {
		_mover = null;
	}
	
	@Override
	public boolean place(GameBoard board) {
		return true;
	}

	@Override
	public boolean make(PublicGameBoard board) {
		// TODO Auto-generated method stub
		return true;
	}

}
