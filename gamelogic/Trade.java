package gamelogic;
import catanui.*;

/**
Represents a proposed trade 
*/
public class Trade implements java.io.Serializable {
	private BoardObject.type[] _ins;
	private BoardObject.type[] _outs;
	private String _backupouts;
	private String _backupins;
	private int _tradeID;
	private int _opcode;
	
	public Trade(BoardObject.type[] i, BoardObject.type[] o, int id, int op) {
		_ins = i;
		_outs = o;
		_tradeID = id;
		_opcode = op;
	}
	
	//convert BoardObject.type ins and outs to string
	public void backup() {
		if(_outs != null && _outs[0] != null) {
			_backupouts = _outs[0].toString();
			for(int i=1; i<_outs.length; i++) {
				_backupouts += "," + _outs[i];
			}
		}
		
		if(_ins != null && _ins[0] != null) {
			_backupins = _ins[0].toString();
			for(int i=1; i<_ins.length; i++) {
				_backupins += "," + _ins[i];
			}
		}
	}
	
	//restore strings into BoardObject.type ins and outs
	public void restore() {
		String[] split = _backupouts.split(",");
		_outs = new BoardObject.type[split.length];
		for(int j=0; j<split.length; j++) {
			for (BoardObject.type c: BoardObject.cardtypes) {
				if (split[j].equalsIgnoreCase(c.toString())) {
					_outs[j] = c;
				}
			}
		}
		
		String[] split2 = _backupins.split(",");
		_ins = new BoardObject.type[split2.length];
		for(int j=0; j<split2.length; j++) {
			for (BoardObject.type c: BoardObject.cardtypes) {
				if (split2[j].equalsIgnoreCase(c.toString())) {
					_ins[j] = c;
				}
			}
		}
	}
	
	public BoardObject.type[] getIns() { return _ins; } //what card(s) player wants to trade
	public BoardObject.type[] getOuts() { return _outs; } //what card(s) player wants
	public int getTradeID() { return _tradeID; }
	public int getOpcode() { return _opcode; }
	
	public boolean isPropose() {
		return (_opcode == 2);
	}
	public boolean isComplete() {
		return (_opcode == 1);
	}
	public boolean isBuild() {
		return (_tradeID < 100);
	}
	public boolean isBuyDev() {
		return (_opcode == 3);
	}
	public boolean isBuyCity() {
		return (_opcode == 4);
	}
	public boolean isBuyRoad() {
		return (_opcode == 5);
	}
	public boolean isBuySettlement() {
		return (_opcode == 6);
	}
	public boolean isPort() {
		return(_opcode == 7);
	} 
	
	//swap the trades ins and outs
	public void swap() {
		BoardObject.type[] temp = _outs;
		_outs = _ins;
		_ins = temp;
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
