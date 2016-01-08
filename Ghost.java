import java.util.List;
import java.util.ArrayList;

public class Ghost {
	private Pair curLoc;
	private Maze maze;
	public Ghost(Pair curLoc, Maze maze) {
		this.curLoc = curLoc;
		this.maze = maze;
	}
	public Pair getLoc(){
		return curLoc;	
	}
	public int getDir() {
		return curLoc.getDir();
	}
	public Pair nextMove() {
		int x = curLoc.getX();
		int y = curLoc.getY();
		int dir = curLoc.getDir();
		if(dir == Direction.West) {
			if(maze.getMaze()[y][x - 1] == '%') {
				return new Pair(x + 1, y, 0, Direction.East, null, null);
			} else {
				return new Pair(x - 1, y, 0, Direction.West, null, null);
			} 
		} else if(dir == Direction.East) {
			if(maze.getMaze()[y][x + 1] == '%') {
				return new Pair(x - 1, y, 0, Direction.West, null, null);
			} else {
				return new Pair(x + 1, y, 0, Direction.East, null, null);
			} 
		}
		//should not be the case of null
		return null;
	}
}
