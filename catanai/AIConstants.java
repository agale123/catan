package catanai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Stack;
import catanui.BoardObject;

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
	public final int DIM_X = 0;
	public final int DIM_Y = 1;
	public final int DIM_Z = 2;
	@SuppressWarnings("serial")
	public static final HashSet<BoardCoordinate> VALID_VERTS = new HashSet<BoardCoordinate>() {{
		Stack<BoardCoordinate> toTraverse = new Stack<BoardCoordinate>();
		BoardCoordinate current, next;
		// Start at the origin.
		toTraverse.push(BoardCoordinate.ORIGIN);
		while (this.size() <= NUM_VERTICES && ! toTraverse.isEmpty()) {
			current = toTraverse.pop();
			// Add all valid adjacent vertices to the stack.
			for (int i = DIM_X; i <= DIM_Z; i++) {
				next = current.moveIn(i, true);
				if (next != null && ! this.contains(next)) toTraverse.push(next);
				else {
					next = current.moveIn(i, false);
					if (next != null && ! this.contains(next)) toTraverse.push(next);
				}
			}
			// Add the current vertex to the set of valid ones.
			add(current);
		}
	}};
	@SuppressWarnings("serial")
	public static final Hashtable<Integer, ArrayList<Integer>> X_GROUPS = new Hashtable<Integer, ArrayList<Integer>>() {{
		put(0, new ArrayList<Integer>(Arrays.asList(25, 26, 27, 38, 39, 47, 48)));
		put(1, new ArrayList<Integer>(Arrays.asList(14, 15, 16, 28, 29, 40, 41, 49, 50)));
		put(2, new ArrayList<Integer>(Arrays.asList(1, 2, 3, 17, 18, 30, 31, 42, 43, 51, 52)));
		put(3, new ArrayList<Integer>(Arrays.asList(0, 5, 4, 7, 19, 20, 32, 33, 44, 45, 53)));
		put(4, new ArrayList<Integer>(Arrays.asList(6, 9, 8, 11, 21, 22, 34, 35, 46)));
		put(5, new ArrayList<Integer>(Arrays.asList(10, 13, 12, 24, 23, 37, 36)));
	}};
	@SuppressWarnings("serial")
	public static final Hashtable<Integer, ArrayList<Integer>> Y_GROUPS = new Hashtable<Integer, ArrayList<Integer>>() {{
		put(0, new ArrayList<Integer>(Arrays.asList(0, 1, 2, 14, 15, 25, 26)));
		put(1, new ArrayList<Integer>(Arrays.asList(6, 5, 4, 3, 17, 16, 28, 27, 38)));
		put(2, new ArrayList<Integer>(Arrays.asList(10, 9, 8, 7, 19, 18, 30, 29, 40, 39, 47)));
		put(3, new ArrayList<Integer>(Arrays.asList(13, 12, 14, 21, 20, 32, 31, 42, 41, 49, 48)));
		put(4, new ArrayList<Integer>(Arrays.asList(24, 23, 22, 34, 33, 44, 43, 51, 50)));
		put(5, new ArrayList<Integer>(Arrays.asList(37, 36, 35, 46, 45, 53, 52)));
	}};
	@SuppressWarnings("serial")
	public static final Hashtable<Integer, ArrayList<Integer>> Z_GROUPS = new Hashtable<Integer, ArrayList<Integer>>() {{
		put(-2, new ArrayList<Integer>(Arrays.asList(1, 0, 5, 6, 9, 10, 13)));
		put(-1, new ArrayList<Integer>(Arrays.asList(2, 3, 4, 7, 8, 11, 12, 24, 14)));
		put(0, new ArrayList<Integer>(Arrays.asList(25, 15, 16, 17, 18, 19, 20, 21, 22, 23, 37)));
		put(1, new ArrayList<Integer>(Arrays.asList(26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36)));
		put(2, new ArrayList<Integer>(Arrays.asList(38, 39, 40, 41, 42, 43, 44, 45, 46)));
		put(3, new ArrayList<Integer>(Arrays.asList(47, 48, 49, 50, 51, 52, 53)));
	}};
	// Development cards
	@SuppressWarnings("serial")
	public static final Hashtable<DevCard, Integer> DEV_VP_VALUE = new Hashtable<DevCard, Integer>() {{
		put(DevCard.Market, 1);
		put(DevCard.Palace, 2);
		put(DevCard.University, 2);
	}};
	@SuppressWarnings("serial")
	public static final Hashtable<DevCard, Double> DEV_FREQ = new Hashtable<DevCard, Double>() {{
		put(DevCard.Knight, 0.3);
		put(DevCard.Market, 0.1);
		put(DevCard.Palace, 0.1);
		put(DevCard.University, 0.1);
		put(DevCard.Monopoly, 0.1);
		put(DevCard.RoadBuilding, 0.15);
		put(DevCard.YearOfPlenty, 0.15);
	}};
	// Die rolls
	public final double DIE_FREQ[] = {0, 0, 1.0 / 36.0, 1.0 / 18.0, 3.0 / 36.0, 1.0 / 9.0, 5.0 / 36.0, 1.0 / 6.0, 5.0 / 36.0, 1.0 / 9.0, 3.0 / 36.0, 1.0 / 18.0, 1.0 / 36.0};
	// Scoring
	public final int VP_LONG_ROAD = 2;
	public final int VP_LARG_ARMY = 2;
	public final int VP_SETTLEMENT = 1;
	public final int VP_CITY = 2;
	// Resource constants
	@SuppressWarnings("serial")
	public static final Hashtable<TileType, Resource> TILE_RES = new Hashtable<TileType, Resource>() {{
		put(TileType.Brick, Resource.Brick);
		put(TileType.Ore, Resource.Ore);
		put(TileType.Sheep, Resource.Sheep);
		put(TileType.Timber, Resource.Timber);
		put(TileType.Wheat, Resource.Wheat);
	}};
	@SuppressWarnings("serial")
	public static final Hashtable<Resource, BoardObject.type> RES_CONV = new Hashtable<Resource, BoardObject.type>() {{
		put(Resource.Brick, BoardObject.type.BRICK);
		put(Resource.Wheat, BoardObject.type.WHEAT);
		put(Resource.Ore, BoardObject.type.ORE);
		put(Resource.Sheep, BoardObject.type.SHEEP);
		put(Resource.Timber, BoardObject.type.WOOD);
	}};
	@SuppressWarnings("serial")
	public static final Hashtable<BoardObject.type, Resource> RES_C_REV = new Hashtable<BoardObject.type, Resource>() {{
		put(BoardObject.type.BRICK, Resource.Brick);
		put(BoardObject.type.WHEAT, Resource.Wheat);
		put(BoardObject.type.ORE, Resource.Ore);
		put(BoardObject.type.SHEEP, Resource.Sheep);
		put(BoardObject.type.WOOD, Resource.Timber);
	}};
	public final int SETT_PAYOUT = 1;
	public final int CITY_PAYOUT = 2;
	public final int YOP_PAYOUT = 2;
	public final int DEF_SWAP_RATIO = 4;
	public final int UNK_SWAP_RATIO = 3;
	public final int SP_SWAP_RATIO = 2;
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
	public final int LOOKAHEAD_RANGE = 0;
	public final double HEURISTIC_MULT = 1.1;
	public final int GOAL_RADIUS = 4;
	public final int MAX_PATH_LENGTH = 6;
	public final int SETTLE_CEIL = 5;
}
