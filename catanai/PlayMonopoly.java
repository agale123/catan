package catanai;

import gamelogic.PublicGameBoard;

public class PlayMonopoly extends Move {
	private Resource _r;
	
	public PlayMonopoly(Player p, Resource res) {
		_mover = p;
		_isPlaced = false;
		_r = res;
	}
	@Override
	public boolean place(GameBoard board) {
		int amt = 0;
		for (Opponent opp : _mover.getOpponents()) amt += opp.takeAll(_r);
		for (int i = 0; i < amt; i++) _mover.draw(_r);
		return true;
	}
	@Override
	public boolean make(PublicGameBoard board) {
		// TODO Auto-generated method stub
		return false;
	}

}
