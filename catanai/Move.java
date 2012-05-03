package catanai;

public abstract class Move {
	protected Player _mover;
	protected boolean _isPlaced;
	
	public abstract boolean place(GameBoard board);
	public abstract boolean make(gamelogic.PublicGameBoard board);
	public abstract void broadcast(AIPlayer p, gamelogic.PublicGameBoard board);
	
	public Player getMover() {
		return _mover;
	}
	public boolean isPlaced() {
		return _isPlaced;
	}
}
