package catanai;

public abstract class Move {
	protected Player _mover;
	protected boolean _isPlaced;
	
	public abstract boolean place(GameBoard board);
	public Player getMover() {
		return _mover;
	}
	public boolean isPlaced() {
		return _isPlaced;
	}
}
