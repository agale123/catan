package gamelogic;

public class Pair {
    
    Object _a;
    Object _b;
    
    public Pair(Object a, Object b) {
		_a = a;
		_b = b;
    }
    
    public Object getA() { return _a;}
    public Object getB() { return _b; }
    public void setA(Object o) {
		_a = o;
    }
    public void setB(Object o) {
		_b = o;
    }

    public String toString() {
	return _a+" "+_b;
    }

	public boolean equals(Object o) {
		Pair p = (Pair) o;
		return p.getA().equals(_a) && p.getB().equals(_b);
	}

	public int hashCode() {
		return (int)((_a.hashCode() + _b.hashCode())/2);
	}

}
