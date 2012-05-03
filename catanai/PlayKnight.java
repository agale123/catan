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
	public boolean place(GameBoard board) {
		_mover.addKnight();
		board.moveRobber(_t);
		return true;
	}
	@Override
	public boolean make(PublicGameBoard board) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void broadcast(AIPlayer p, gamelogic.PublicGameBoard board) {
		// TODO Auto-generated method stub
		
	}

}
