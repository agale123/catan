package catanai;

import gamelogic.PublicGameBoard;

public class BuyDevCard extends Move {
	public BuyDevCard(Player mover) {
		_mover = mover;
		_isPlaced = false;
	}
	@Override
	public boolean place(GameBoard board) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean make(PublicGameBoard board) {
		// TODO Auto-generated method stub
		return false;
	}

}
