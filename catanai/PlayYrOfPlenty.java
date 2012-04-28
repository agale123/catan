package catanai;

public class PlayYrOfPlenty extends Move implements AIConstants {
	private Resource _r;
	
	public PlayYrOfPlenty(Player p, Resource res) {
		_mover = p;
		_isPlaced = false;
		_r = res;
	}
	@Override
	public boolean place(GameBoard board) {
		for (int i = 0; i < YOP_PAYOUT; i++) _mover.draw(_r);
		return true;
	}

}
