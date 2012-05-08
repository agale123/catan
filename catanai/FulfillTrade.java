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
		_rec = rec;
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
		_tb = new Trade(to, from, _id, 1);
	}
	
	@Override
	public void broadcast(AIPlayer p, PublicGameBoard board) {
		System.out.println("A trade is being fulfilled with ID " + Integer.toString(_id) + "!"); // TODO: Debug line
		this.printResources(); // TODO: Debug line
		p.completeTrade(this);
		int c_index = p.getServerClientPool().getPlayerFromTrade(_id);
		p.broadcastTo(_tb, c_index);
		p.getServerClientPool().removeTrade(_id);
		p.broadcastToElse("17/" + Integer.toString(_id), c_index, -1);
	}

	@Override
	public void charge() {
		if (_mover != null) _mover.takeList(_from);
		if (_rec != null) {
			_rec.dropList(_from);
			_rec.takeList(_to);
		}
	}

	@Override
	public boolean make(PublicGameBoard board) {
		return board.canTrade(Integer.parseInt(_mover.getID()), Integer.parseInt(_rec.getID()), _tb);
	}

	@Override
	public boolean place(GameBoard board) {
		return _rec.hasList(_from) && _mover.hasList(_to);
	}
	
	public Player getRecipient() {
		return _rec;
	}
	
	public int getID() {
		return _id;
	}
	
	public List<Resource> getTo() {
		return _to;
	}
	
	public List<Resource> getFrom() {
		return _from;
	}
	
	public void printResources() {
		System.out.println(_to.toString() + " --> " + _from.toString());
	}
	
	@Override
	public String toString() {
		return _to.toString() + " --> " + _from.toString();
	}
}
