package gamelogic;
import catanui.*;

public class Trade implements java.io.Serializable {
	private BoardObject.type[] _ins;
	private BoardObject.type[] _outs;
	private String _backupouts;
	private int _tradeID;
	private int _opcode;
	public int myint = 3;
	public String me;
	
	public Trade(BoardObject.type[] i, BoardObject.type[] o, int id, int op) {
		_ins = i;
		_outs = o;
		_tradeID = id;
		_opcode = op;
	}
	
	public void backup() {
		if(_outs != null) {
			_backupouts = _outs[0].toString();
			for(int i=1; i<_outs.length; i++) {
				_backupouts += "," + _outs[i];
			}
		}
	}
	
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
		toReturn += "myint : "+myint + "\nme: "+me;
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
