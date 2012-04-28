package catanai;

public class BuildCity extends Move {
	private Vertex _target;
	
	public BuildCity(Player pl, Vertex target) {
		_mover = pl;
		_target = target;
	}
	
	@Override
	public boolean place(gamelogic.PublicGameBoard pub, GameBoard board) {
		// TODO Auto-generated method stub
		return false;
	}
}
