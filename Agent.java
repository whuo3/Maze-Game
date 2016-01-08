import java.util.List;
import java.util.ArrayList;
class Direction {
	public final static int North = 1;
	public final static int East = 2;
	public final static int South = 3;
	public final static int West = 4;
	private Direction() {}
	public static int numTurns(int now, int next) {
		int res = Math.abs(now - next);
		return res > 2? res % 2 : res;
	}
}
class CostMode {
	private static int Type = 1;
	public final static int Def = 1;//default Cost Mode
	public final static int One = 2;//forward movement has cost 2 and any turn has cost 1
	public final static int Two = 3;//forward movement has cost 1 and any turn has cost 2
	private CostMode() {}
	public static boolean setType(int ty) {
		if(ty >= 1 && ty <= 3) {
			Type = ty;
			return true;
		}
		return false;
	}
	public static int getType() {
		return Type;
	}
}
public class Agent {
	private Pair start;
	private Maze maze;
	private List<Pair> path;
	public Agent(Maze maze) {
		this.start = maze.getStart();
		this.maze = maze;
		path = new ArrayList<Pair>();
	}
	public Pair getStart() {
		return start;	
	}
	public List<Pair> getPath() {
		return path;
	}
	public boolean isValidLoc(int x, int y) {
		return x >= 0 && x < maze.getColLen() && y >=0 && y < maze.getRowLen();
	}
	public boolean isBarrier(int x, int y) {
		return maze.getMaze()[y][x] == '%';
	}
	private int calCost(Pair curLoc, int nextDir) {
		if(CostMode.getType() == CostMode.Def) {
			return curLoc.getGx() + 1;
		} else if(CostMode.getType() == CostMode.One) {
			return curLoc.getGx() + 2 + Direction.numTurns(curLoc.getDir(), nextDir);
		} else if(CostMode.getType() == CostMode.Two) {
			return curLoc.getGx() + 1 + Direction.numTurns(curLoc.getDir(), nextDir) * 2;
		} else {
			return 0;
		}
	}
	//If the move to go up is valid, then return the new pair, otherwise return null
	public Pair moveUp(Pair curLoc, boolean[][] visited) {
		int x = curLoc.getX();
		int y = curLoc.getY();
		if(isValidLoc(x, y - 1) && !isBarrier(x, y - 1) && !visited[y - 1][x]) {
			int cost = calCost(curLoc, Direction.North);
			return new Pair(x, y - 1, cost, Direction.North, curLoc.getGoals(), null);
		} else {
			return null;
		}
	}
	//If the move to go down is valid, then return the new pair, otherwise return null
	public Pair moveDown(Pair curLoc, boolean[][] visited) {
		int x = curLoc.getX();
		int y = curLoc.getY();
		if(isValidLoc(x, y + 1) && !isBarrier(x, y + 1) && !visited[y + 1][x]) {
			int cost = calCost(curLoc, Direction.South);
			return new Pair(x, y + 1, cost, Direction.South, curLoc.getGoals(), null); 
		} else {
			return null;
		}
	}
	//If the move to go left is valid, then return the new pair, otherwise return null
	public Pair moveLeft(Pair curLoc, boolean[][] visited) {
		int x = curLoc.getX();
		int y = curLoc.getY();
		if(isValidLoc(x - 1, y) && !isBarrier(x - 1, y) && !visited[y][x - 1]) {
			int cost = calCost(curLoc, Direction.West);
			return new Pair(x - 1, y, cost, Direction.West, curLoc.getGoals(), null);
		} else {
			return null;
		}
	} 
	//If the move to go right is valid, then return the new pair, otherwise return null
	public Pair moveRight(Pair curLoc, boolean[][] visited) {	
		int x = curLoc.getX();
		int y = curLoc.getY();
		if(isValidLoc(x + 1, y) && !isBarrier(x + 1, y) && !visited[y][x + 1]) {
			int cost = calCost(curLoc, Direction.East);
			return new Pair(x + 1, y, cost, Direction.East, curLoc.getGoals(), null); 
		} else {
			return null;
		}
	}
	//******************************Direction Move***********************************
	public Pair moveUpDir(Pair curLoc, boolean[][][] visited) {
		int x = curLoc.getX();
		int y = curLoc.getY();
		if(isValidLoc(x, y - 1) && !isBarrier(x, y - 1) && !visited[y - 1][x][Direction.North]) {
			int cost = calCost(curLoc, Direction.North);
			return new Pair(x, y - 1, cost, Direction.North, curLoc.getGoals(), null);
		} else {
			return null;
		}
	}
	//If the move to go down is valid, then return the new pair, otherwise return null
	public Pair moveDownDir(Pair curLoc, boolean[][][] visited) {
		int x = curLoc.getX();
		int y = curLoc.getY();
		if(isValidLoc(x, y + 1) && !isBarrier(x, y + 1) && !visited[y + 1][x][Direction.South]) {
			int cost = calCost(curLoc, Direction.South);
			return new Pair(x, y + 1, cost, Direction.South, curLoc.getGoals(), null); 
		} else {
			return null;
		}
	}
	//If the move to go left is valid, then return the new pair, otherwise return null
	public Pair moveLeftDir(Pair curLoc, boolean[][][] visited) {
		int x = curLoc.getX();
		int y = curLoc.getY();
		if(isValidLoc(x - 1, y) && !isBarrier(x - 1, y) && !visited[y][x - 1][Direction.West]) {
			int cost = calCost(curLoc, Direction.West);
			return new Pair(x - 1, y, cost, Direction.West, curLoc.getGoals(), null);
		} else {
			return null;
		}
	} 
	//If the move to go right is valid, then return the new pair, otherwise return null
	public Pair moveRightDir(Pair curLoc, boolean[][][] visited) {	
		int x = curLoc.getX();
		int y = curLoc.getY();
		if(isValidLoc(x + 1, y) && !isBarrier(x + 1, y) && !visited[y][x + 1][Direction.East]) {
			int cost = calCost(curLoc, Direction.East);
			return new Pair(x + 1, y, cost, Direction.East, curLoc.getGoals(), null); 
		} else {
			return null;
		}
	}
	//******************************Ghost Move***********************************
	public boolean willDie(int x, int y, int dir, Pair ghostLoc) {
		if(ghostLoc == null) {
			return true;
		}
		int gx = ghostLoc.getX();
		int gy = ghostLoc.getY();
		int gdir = ghostLoc.getDir();
		if(x == gx && y == gy) {
			return true;
		} else if(y == gy && Math.abs(gdir - dir) == 2 && Math.abs(x - gx) == 1) {
			return true;
		}
		return false;
	}
	public Pair moveUpGhost(Pair curLoc, boolean[][][][][] visited) {
		int x = curLoc.getX();
		int y = curLoc.getY();
		Ghost ghost = curLoc.getGhost();
		Pair gNextMove = ghost.nextMove();
		if(isValidLoc(x, y - 1) && 
		   !isBarrier(x, y - 1) && 
		   !willDie(x, y - 1, Direction.North, gNextMove) && 
		   !visited[y - 1][x][gNextMove.getY()][gNextMove.getX()][Direction.North]) {
			int cost = calCost(curLoc, Direction.North);
			return new Pair(x, y - 1, cost, Direction.North, curLoc.getGoals(), new Ghost(gNextMove, maze));
		} else {
			return null;
		}
	}
	//If the move to go down is valid, then return the new pair, otherwise return null
	public Pair moveDownGhost(Pair curLoc, boolean[][][][][] visited) {
		int x = curLoc.getX();
		int y = curLoc.getY();
		Ghost ghost = curLoc.getGhost();
		Pair gNextMove = ghost.nextMove();
		if(isValidLoc(x, y + 1) && 
		   !isBarrier(x, y + 1) && 
		   !willDie(x, y + 1, Direction.South, gNextMove) && 
		   !visited[y + 1][x][gNextMove.getY()][gNextMove.getX()][Direction.South]) {
			int cost = calCost(curLoc, Direction.South);
			return new Pair(x, y + 1, cost, Direction.South, curLoc.getGoals(), new Ghost(gNextMove, maze)); 
		} else {
			return null;
		}
	}
	//If the move to go left is valid, then return the new pair, otherwise return null
	public Pair moveLeftGhost(Pair curLoc, boolean[][][][][] visited) {
		int x = curLoc.getX();
		int y = curLoc.getY();
		Ghost ghost = curLoc.getGhost();
		Pair gNextMove = ghost.nextMove();
		if(isValidLoc(x - 1, y) && 
			!isBarrier(x - 1, y) && 
			!willDie(x - 1, y, Direction.West, gNextMove) && 
			!visited[y][x - 1][gNextMove.getY()][gNextMove.getX()][Direction.West]) {
			int cost = calCost(curLoc, Direction.West);
			return new Pair(x - 1, y, cost, Direction.West, curLoc.getGoals(), new Ghost(gNextMove, maze));
		} else {
			return null;
		}
	} 
	//If the move to go right is valid, then return the new pair, otherwise return null
	public Pair moveRightGhost(Pair curLoc, boolean[][][][][] visited) {	
		int x = curLoc.getX();
		int y = curLoc.getY();
		Ghost ghost = curLoc.getGhost();
		Pair gNextMove = ghost.nextMove();
		if(isValidLoc(x + 1, y) && 
		   !isBarrier(x + 1, y) && 
		   !willDie(x + 1, y, Direction.South, gNextMove) && 
		   !visited[y][x + 1][gNextMove.getY()][gNextMove.getX()][Direction.East]) {
			int cost = calCost(curLoc, Direction.East);
			return new Pair(x + 1, y, cost, Direction.East, curLoc.getGoals(), new Ghost(gNextMove, maze)); 
		} else {
			return null;
		}
	}
}