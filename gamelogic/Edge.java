package gamelogic;

public class Edge {

	int _start;
	int _end;
	boolean _hasRoad = false;
	
	public Edge(int s, int e) {
		_start = s;
		_end = e;
	}
	
	public int getStartV() {
		return _start;
	}
	
	public int getEndV() {
		return _end;
	}
	
	public boolean hasRoad() {
		return _hasRoad;
	}
	
	public void setRoad() {
		_hasRoad = true;
	}
}
