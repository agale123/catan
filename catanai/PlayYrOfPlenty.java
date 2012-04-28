package catanai;

import gamelogic.PublicGameBoard;

public class PlayYrOfPlenty extends Move {
	private Resource _r;
	
	public PlayYrOfPlenty(Player p, Resource res) {
		_mover = p;
		_isPlaced = false;
		_r = res;
	}
	@Override
	public boolean place(PublicGameBoard pub, GameBoard board) {
		// TODO Auto-generated method stub
		return false;
	}

}
