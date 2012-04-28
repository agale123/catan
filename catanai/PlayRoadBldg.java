package catanai;

import gamelogic.PublicGameBoard;

public class PlayRoadBldg extends Move {
	private Edge _e0, _e1;
	
	public PlayRoadBldg(Player p, Edge e0, Edge e1) {
		_mover = p;
		_isPlaced = false;
		_e0 = e0;
		_e1 = e1;
	}
	@Override
	public boolean place(PublicGameBoard pub, GameBoard board) {
		// TODO Auto-generated method stub
		return false;
	}

}
