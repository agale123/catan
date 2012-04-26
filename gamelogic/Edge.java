package gamelogic;

public class Edge {

	Vertex _start;
	Vertex _end;
	boolean _hasRoad = false;
	
	public Edge(Vertex s, Vertex e) {
		_start = s;
		_end = e;
	}
	
	public Vertex getStartV() {
		return _start;
	}
	
	public Vertex getEndV() {
		return _end;
	}
	
	public boolean hasRoad() {
		return _hasRoad;
	}
	
	public void setRoad() {
		_hasRoad = true;
	}
}
