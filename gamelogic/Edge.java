package gamelogic;

public class Edge {

	Vertex _start;
	Vertex _end;
	boolean _hasRoad = false;
	
	public Edge(Vertex s, Vertex e) {
		_start = s;
		_end = e;
	}
	
	public boolean equals(Object o) {
		Edge e = (Edge) o;
		return (e.getStartV().equals(_start) && e.getEndV().equals(_end)) || (e.getEndV().equals(_end) && e.getStartV().equals(_start));
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
