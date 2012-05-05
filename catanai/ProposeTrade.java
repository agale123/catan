package catanai;

import java.util.List;
import server.Server;
import gamelogic.PublicGameBoard;
import gamelogic.Trade;
import catanui.BoardObject;

public class ProposeTrade extends Move implements AIConstants {
	private int _id;
	private List<Resource> _to, _from;
	private gamelogic.Trade _tb;
	
	public ProposeTrade(Player p, List<Resource> t, List<Resource> f, Server s) {
		_mover = p;
		_to = t;
		_from = f;
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
		_id = s.getClientPool().nextTradeID(Integer.parseInt(p.getID()));
		_tb = new Trade(to, from, _id, 2);
	}
	
	@Override
	public void broadcast(AIPlayer p, PublicGameBoard board) {
		p.broadcast(_tb);
	}

	@Override
	public void charge() {
		return;
	}

	@Override
	public boolean make(PublicGameBoard board) {
		return _mover.hasList(_to);
	}

	@Override
	public boolean place(GameBoard board) {
		return true;
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
	
	public FulfillTrade fulfill(Player p) {
		return new FulfillTrade(_mover, p, _to, _from, _id);
	}
}
