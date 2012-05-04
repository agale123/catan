package catanai;

import java.util.List;
import gamelogic.PublicGameBoard;

public class Trade extends Move {
	private Player _receive;
	private List<Resource> _to, _from;
	
	public Trade(Player p, Player c, List<Resource> t, List<Resource> f) {
		_mover = p;
		_receive = c;
		_to = t;
		_from = f;
	}
	
	public boolean propose() {
		// TODO: Implement this
		return false;
	}
	
	@Override
	public void broadcast(AIPlayer p, PublicGameBoard board) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void charge() {
		return;
	}

	@Override
	public boolean make(PublicGameBoard board) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean place(GameBoard board) {
		// TODO Auto-generated method stub
		int br_t = 0, sp_t = 0, tm_t = 0, or_t = 0, wh_t = 0;
		int br_f = 0, sp_f = 0, tm_f = 0, or_f = 0, wh_f = 0;
		for (Resource r : _to) {
			switch (r) {
			case Brick:
				br_t++;
				break;
			case Sheep:
				sp_t++;
				break;
			case Timber:
				tm_t++;
				break;
			case Ore:
				or_t++;
				break;
			case Wheat:
				wh_t++;
				break;
			default:
				break;
			}
		}
		for (Resource r : _from) {
			switch (r) {
			case Brick:
				br_f++;
				break;
			case Sheep:
				sp_f++;
				break;
			case Timber:
				tm_f++;
				break;
			case Ore:
				or_f++;
				break;
			case Wheat:
				wh_f++;
				break;
			default:
				break;
			}
		}
		if (br_t > _mover.brick() || sp_t > _mover.sheep() || tm_t > _mover.timber() ||
				or_t > _mover.ore() || wh_t > _mover.wheat()) return false;
		if (br_f > _receive.brick() || sp_f > _receive.sheep() || tm_f > _receive.timber() ||
				or_f > _receive.ore() || wh_f > _receive.wheat()) return false;
		_mover.dropList(_to);
		_receive.dropList(_from);
		_mover.takeList(_from);
		_receive.takeList(_to);
		return true;
	}

}
