package catanai;

import java.util.Hashtable;
import java.util.HashSet;
import java.util.Stack;

public interface AIConstants {
	// Structure of board components.
	public final int MAX_SIDES = 6;
	public final int NUM_ENDS = 2;
	public final int MAX_ADJ_TILES = 3;
	// Structure of overall board.
	public final int NUM_VERTICES = 54;
	public final int NUM_EDGES = 72;
	public final int NUM_TILES = 19;
	public final int CEIL_X = 5;
	public final int FLOOR_X = 0;
	public final int CEIL_Y = 5;
	public final int FLOOR_Y = 0;
	public final int FLOOR_Z = -2;
	public final int CEIL_Z = 3;
	@SuppressWarnings("serial")
	public final HashSet<BoardCoordinate> VALID_VERTS = new HashSet<BoardCoordinate>() {{
		Stack<BoardCoordinate> toTraverse = new Stack<BoardCoordinate>();
		BoardCoordinate current, next;
		// Start at the origin.
		toTraverse.push(new BoardCoordinate(0, 0, 0));
		while (! toTraverse.isEmpty()) {
			current = toTraverse.pop();
			// Add all valid adjacent vertices to the stack.
			for (int i = 0; i <= 2; i++) {
				next = current.moveIn(i, true);
				if (next != null) toTraverse.push(next);
				else {
					next = current.moveIn(i, false);
					if (next != null) toTraverse.push(next);
				}
			}
			// Add the current vertex to the set of valid ones.
			add(current);
		}
	}};
	// Development cards
	@SuppressWarnings("serial")
	public final Hashtable<DevCard, Integer> DEV_VP_VALUE = new Hashtable<DevCard, Integer>() {{
		put(DevCard.Market, 1);
		put(DevCard.Palace, 2);
		put(DevCard.University, 2);
	}};
	@SuppressWarnings("serial")
	public final Hashtable<DevCard, Double> DEV_FREQ = new Hashtable<DevCard, Double>() {{
		put(DevCard.Knight, 0.3);
		put(DevCard.Market, 0.1);
		put(DevCard.Palace, 0.1);
		put(DevCard.University, 0.1);
		put(DevCard.Monopoly, 0.1);
		put(DevCard.RoadBuilding, 0.15);
		put(DevCard.YearOfPlenty, 0.15);
	}};
	// Die rolls
	public final double DIE_FREQ[] = {0, 0, 1 / 36, 1 / 18, 3 / 36, 1 / 9, 5 / 36, 1 / 6, 5 / 36, 1 / 9, 3 / 36, 1 / 18, 1 / 36};
	// Scoring
	public final int VP_LONG_ROAD = 2;
	public final int VP_LARG_ARMY = 2;
	public final int VP_SETTLEMENT = 1;
	public final int VP_CITY = 2;
	// Resource constants
	@SuppressWarnings("serial")
	public final Hashtable<TileType, Resource> TILE_RES = new Hashtable<TileType, Resource>() {{
		put(TileType.Brick, Resource.Brick);
		put(TileType.Ore, Resource.Ore);
		put(TileType.Sheep, Resource.Sheep);
		put(TileType.Timber, Resource.Timber);
		put(TileType.Wheat, Resource.Wheat);
	}};
	public final int SETT_PAYOUT = 1;
	public final int CITY_PAYOUT = 2;
	public final int BRICK_CITY = 0;
	public final int ORE_CITY = 3;
	public final int SHEEP_CITY = 0;
	public final int TIMBER_CITY = 0;
	public final int WHEAT_CITY = 2;
	public final int BRICK_SETTLEMENT = 1;
	public final int ORE_SETTLEMENT = 0;
	public final int SHEEP_SETTLEMENT = 1;
	public final int TIMBER_SETTLEMENT = 1;
	public final int WHEAT_SETTLEMENT = 1;
	public final int BRICK_ROAD = 1;
	public final int ORE_ROAD = 0;
	public final int SHEEP_ROAD = 0;
	public final int TIMBER_ROAD = 1;
	public final int WHEAT_ROAD = 0;
	public final int BRICK_DEV = 0;
	public final int ORE_DEV = 1;
	public final int SHEEP_DEV = 1;
	public final int TIMBER_DEV = 0;
	public final int WHEAT_DEV = 1;
	// Other AI constants
	public final int LOOKAHEAD_RANGE = 5;
	public final double HEURISTIC_MULT = 1.1;
	public final int GOAL_RADIUS = 4;
}
