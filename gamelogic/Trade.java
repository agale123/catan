package gamelogic;
import catanui.*;

public class Trade implements java.io.Serializable {
	private BoardObject.type[] _ins;
	private BoardObject.type[] _outs;
	private int _tradeID;
	private int _opcode;
	
	public Trade(BoardObject.type[] i, BoardObject.type[] o, int id, int op) {
		_ins = i;
		_outs = o;
		_tradeID = id;
		_opcode = op;
	}
	
	public BoardObject.type[] getIns() {
		return _ins;
	}
	
	public BoardObject.type[] getOuts() {
		return _outs;
	}
	
	public int getTradeID() {
		return _tradeID;
	}
	
	public int getOpcode() {
		return _opcode;
	}
	
	public void swap() {
		BoardObject.type[] temp = _ins;
		_ins = _outs;
		_outs = temp;
	}
	
	public String toString() {
		String toReturn = "Trade id: " + _tradeID + " opcode: " + _opcode + " ins: ";
		for(int i=0; i<_ins.length; i++) {
			toReturn += _ins[i] + " ";
		}
		toReturn += "outs: ";
		for(int i=0; i<_outs.length; i++) {
			toReturn += _outs[i] + " ";
		}
		return toReturn;
	}
	
	public boolean equals(Object o) {
		Trade p = (Trade) o;
		return p.getIns().equals(_ins) && p.getOuts().equals(_outs) && p.getTradeID() == _tradeID && p.getOpcode() == _opcode;
	}

	public int hashCode() {
		return (int)((_ins.hashCode() + _outs.hashCode())/2);
	}
}