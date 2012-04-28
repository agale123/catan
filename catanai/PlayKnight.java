package catanai;

import gamelogic.PublicGameBoard;

public class PlayKnight extends Move {
	private Tile _t;
	
	public PlayKnight(Player p, Tile target) {
		_mover = p;
		_isPlaced = false;
		_t = target;
	}
	@Override
	public boolean place(PublicGameBoard pub, GameBoard board) {
		// TODO Auto-generated method stub
		return false;
	}

}
