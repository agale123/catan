package catanai;

import java.util.List;
import catanui.BoardObject;
import gamelogic.PublicGameBoard;
import gamelogic.Trade;

public class FulfillTrade extends Move implements AIConstants {
	private Player _rec;
	private int _id;
	private List<Resource> _to, _from;
	private Trade _tb;
	
	public FulfillTrade(Player prop, Player rec, List<Resource> t, List<Resource> f, int id) {
		_mover = prop;
		_to = t;
		_from = f;
		_id = id;
		BoardObject.type to[] = new BoardObject.type[t.size()];
		BoardObject.type from[] = new BoardObject.type[f.size()];
		int i = 0;
		for (Resource r : t) {
			to[i] = RES_CONV.get(r);
			i++;
		}
		i = 0;
		for (Resource r : f) {
			from[i] = RES_CONV.get(r);
			i++;
		}
		_tb = new Trade(from, to, _id, 1);
	}
	
	@Override
	public void broadcast(AIPlayer p, PublicGameBoard board) {
		p.broadcast(_tb);
	}

	@Override
	public void charge() {
		_mover.dropList(_to);
		_rec.dropList(_from);
		_mover.takeList(_from);
		_rec.takeList(_to);
	}

	@Override
	public boolean make(PublicGameBoard board) {
		return board.canTrade(Integer.parseInt(_mover.getID()), Integer.parseInt(_rec.getID()), _tb);
	}

	@Override
	public boolean place(GameBoard board) {
		return true;
	}
	
	public int getID() {
		return _id;
	}
}
