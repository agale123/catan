package catanai;

import gamelogic.PublicGameBoard;

public class Swap extends Move {
	Resource _f, _t;
	int _r;
	
	public Swap(Player p, Resource from, Resource to, int ratio) {
		_mover = p;
		_isPlaced = false;
		_f = from;
		_t = to;
		_r = ratio;
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
