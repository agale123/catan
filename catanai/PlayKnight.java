package catanai;

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

}
