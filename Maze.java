import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.*;
class Hx {
	private static int Type = 1; //Set default type to be manhattan
	public final static int Manhattan = 1;
	public final static int ImproveManhattan = 2;
	public final static int Ghost = 3;
	public final static int PenalTrunsOne = 4;
	public final static int PenalTrunsTwo = 5;
	public final static int MultipleGoals = 6;
	public static boolean setType(int ty) {
		if(ty >= 1 && ty <= 6) {
			Type = ty;
			return true;
		}
		return false;
	}
	public static int getType() {
		return Type;
	}
}
class Pair {
	private int x;
	private int y;
	private int gx;
	private int hx;
	private int dir;
	private Pair parent;
	private List<Pair> path;
	private Ghost ghost;
	private HashSet<Pair> goals;
	/******General*******/
	public Pair(Pair cp) {
		this.x = cp.getX();
		this.y = cp.getY();
	}
	public Pair(int x, int y, int gx, int dir, HashSet<Pair> goals, Ghost gho) {
		this.x = x;
		this.y = y;
		this.gx = gx;
		this.goals = goals;
		this.dir = dir;
		if(gho != null) {
			ghost = gho;
		}
		if(goals == null) { 
			return;
		}
		//If goals == null, then the current pair is goal, no need to calculate gx, hx. 
		if(Hx.getType() == Hx.Manhattan) {
			this.hx = calManhattan(x, y);
		} else if(Hx.getType() == Hx.ImproveManhattan) {
			this.hx = (int)(1.1 * calManhattan(x, y));
		} else if(Hx.getType() == Hx.Ghost) {
			this.hx = calGhostHx();
		} else if(Hx.getType() == Hx.PenalTrunsOne) {
			this.hx = penalizeOne();
		} else if(Hx.getType() == Hx.PenalTrunsTwo) {

		} else if(Hx.getType() == Hx.MultipleGoals) {

		}
	}
	public void setPath(List<Pair> path) {
		this.path = path;
	}
	public void setDir(int d) {
		this.dir = d;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setParent(Pair parent) { //Record the previous step
		this.parent = parent;
	}
	public int getDir() {
		return dir;
	}
	public List<Pair> getPath() {
		return path;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getGx() {
		return gx;
	}
	public int getHx() {
		return hx;
	}
	public Pair getParent() {
		return parent;
	}
	public HashSet<Pair> getGoals() {
		return goals;
	}
	public Ghost getGhost() {
		return ghost;
	}
	@Override
	public int hashCode() {
		return y * 101 + x;
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof Pair)) {
			return false;
		}
		Pair t = (Pair)obj;
		return t.getX() == this.x && t.getY() == this.y;
	}
	/******1.1 Single Goal heuristic********/
	private int calManhattan(int x, int y) {
		int manhattan = 0;
		for(Pair p : goals) {
			manhattan += Math.abs(p.getX() - x) + Math.abs(p.getY() - y);
		}
		return manhattan;
	}
	/****1.2 Penalizing turns heuristic*****/
	private int penalizeOne() {
		for(Pair goal : goals) {
			int hor = goal.getX() - x > 0? Direction.East : (goal.getX() - x == 0? dir:Direction.West);
			int ver = goal.getY() - y > 0? Direction.South : (goal.getY() - y == 0? dir:Direction.North);
			return calManhattan(x, y) + Math.max(Direction.numTurns(dir, hor), Direction.numTurns(dir, ver));
		}
		return -1;
	}
	private int penalizeTwo() {
		return -1;
	}
	/****1.3 Pacman with ghost heuristic****/
	private int calGhostHx() {
		return calManhattan(x, y);
	}
	/****2.0 multiple goals*******/

}
public class Maze {
	private Pair start;
	private Ghost ghost;
	private HashSet<Pair> goals;
	private int rowLen; //the length of rows, corrspond to the range of y
	private int colLen;	//the length of columns, corrspond to the range of x
	private char[][] maze;
	//Constructor
	public Maze(String fileName) {
		goals = new HashSet<Pair>();
		maze = readMaze(fileName);
		rowLen = maze.length;
		colLen = maze[0].length;
	}
	//Read the maze from the input
	private char[][] readMaze(String fileName) {
		String line = null;
		List<char[]> helper = new ArrayList<char[]>();
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
                helper.add(line.toCharArray());
            }
		} catch(Exception e) {
			System.exit(1);
		}
		char[][] newMaze = new char[helper.size()][];
		for(int i = 0; i < helper.size(); i++) {
			newMaze[i] = helper.get(i);
		}
		for(int i = 0; i < newMaze.length; i++) {
			for(int j = 0; j < newMaze[0].length; j++) {
				if(newMaze[i][j] == '.') {
					goals.add(new Pair(j, i, 0, -1, null, null));
				} else if(newMaze[i][j] == 'G') {
					ghost = new Ghost(new Pair(j, i, -1, Direction.East, null, null), this);
				}
			}
		}
		for(int i = 0; i < newMaze.length; i++) {
			for(int j = 0; j < newMaze[0].length; j++) {
				if(newMaze[i][j] == 'P') {
					start = new Pair(j, i, 0, Direction.East, goals, ghost); //Initial direction is east
				} 
			}
		}
		return newMaze;
	}
	public Ghost getGhost() {
		return ghost;
	}
	public Pair getStart() {
		return start;
	}
	public boolean checkGoal(Pair t) {
		if(goals.contains(t)) {
			goals.remove(t);
			return true;
		}
		return false;
	}
	public HashSet<Pair> getGoals() {
		return goals;
	}
	public char[][] getMaze() {
		return maze;
	}
	public int getRowLen() {
		return rowLen;
	}
	public int getColLen() {
		return colLen;
	}
	//******************For testing*********************
	public static void printMaze(Maze mz, Agent ag) {
		if(mz == null) {
			return;
		}
		char[][] map = mz.getMaze();
		char[][] copy = new char[map.length][map[0].length];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				copy[i][j] = map[i][j];
			}
		}
		if(ag != null) {
			for(Pair p : ag.getPath()) {
				copy[p.getY()][p.getX()] = '.';
			}
		}
		System.out.println("Result:");
		print(copy);
	}
	public static void print(char[][] map) {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}
}