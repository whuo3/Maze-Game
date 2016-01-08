import java.util.*;

public class Search {
	public static void DFS(Agent ag, Maze mz, Recorder rd) {
		if(ag == null || mz == null) {
			System.out.println("BFS Search fail because of invalid input arguments.");
			return;
		}
		List<Pair> sol = new ArrayList<Pair>(); //Current soluction
		boolean[][] visited = new boolean[mz.getRowLen()][mz.getColLen()];
		dfsHelper(ag.getStart(), ag, mz, visited, sol, rd);
		rd.setCost(ag.getPath().size()); 
		Maze.printMaze(mz, ag);
	}
	private static boolean dfsHelper(Pair cur, Agent ag, Maze mz, boolean[][] visited, List<Pair> sol, Recorder rd) {
		if(mz.checkGoal(cur)) {
			sol.add(cur);
			for(Pair p : sol) {
				ag.getPath().add(p);
			}
			return true;
		}
		rd.addExpand(); //Record expand Nodes
		visited[cur.getY()][cur.getX()] = true;
		Pair up = ag.moveUp(cur, visited);
		Pair down = ag.moveDown(cur, visited);
		Pair left = ag.moveLeft(cur, visited);
		Pair right = ag.moveRight(cur, visited);
		sol.add(cur);
		if(up != null && dfsHelper(up, ag, mz, visited, sol, rd)) {
			return true;
		}
		if(left != null && dfsHelper(left, ag, mz, visited, sol, rd)) {
			return true;
		}
		if(down != null && dfsHelper(down, ag, mz, visited, sol, rd)) {
			return true;
		}
		if(right != null && dfsHelper(right, ag, mz, visited, sol, rd)) {
			return true;
		}
		sol.remove(sol.size() - 1);
		return false;
	}
	public static void BFS(Agent ag, Maze mz, Recorder rd) {
		if(ag == null || mz == null) {
			System.out.println("BFS Search fail because of invalid input arguments.");
			return;
		}
		Pair last = null; //The pair reference which refer to the last step of the game to reach the goal
		Queue<Pair> que = new LinkedList<Pair>();
		boolean[][] visited = new boolean[mz.getRowLen()][mz.getColLen()];
		visited[mz.getStart().getY()][mz.getStart().getX()] = true;
		que.add(mz.getStart());
		while(mz.getGoals().size() != 0) {
			Pair cur = que.poll();
			rd.addExpand(); //Record expand Nodes
			Pair up = ag.moveUp(cur, visited);
			Pair down = ag.moveDown(cur, visited);
			Pair left = ag.moveLeft(cur, visited);
			Pair right = ag.moveRight(cur, visited);
			if(up != null) {
				last = mz.checkGoal(up)? up : last;
				visited[up.getY()][up.getX()] = true;
				up.setParent(cur);
				que.add(up);
			}
			if(down != null) {
				last = mz.checkGoal(down)? down : last;
				visited[down.getY()][down.getX()] = true;
				down.setParent(cur);
				que.add(down);
			}
			if(left != null) {
				last = mz.checkGoal(left)? left : last;
				visited[left.getY()][left.getX()] = true;
				left.setParent(cur);
				que.add(left);
			}
			if(right != null) {
				last = mz.checkGoal(right)? right : last;
				visited[right.getY()][right.getX()] = true;
				right.setParent(cur);
				que.add(right);
			}
		}
		rd.setCost(last.getGx()); //record cost
		//record the optimal path to the agent 
		Pair cur = last;
		List<Pair> path = ag.getPath();
		while(cur != null) {
			path.add(cur);
			cur = cur.getParent();
		}
		Maze.printMaze(mz, ag);
	}
	public static void Gbfs(Agent ag, Maze mz, Recorder rd) {
		//Sanity check..
		if(ag == null || mz == null) {
			System.out.println("Astar Search fail because of invalid input arguments.");
			return;
		}
		char[][] board = mz.getMaze();
		boolean[][] visited = new boolean[mz.getRowLen()][mz.getColLen()];
		PriorityQueue<Pair> pq = new PriorityQueue<Pair>(mz.getRowLen() * mz.getColLen(), new Comparator<Pair>(){
			@Override
			public int compare(Pair a, Pair b) {
				int aVal = a.getHx();
				int bVal = b.getHx();
				if(aVal == bVal) {
					return 0;
				}
				return aVal < bVal? -1 : 1;
			}
		});
		Pair last = null; //The pair reference which refer to the last step of the game to reach the goal
		visited[mz.getStart().getY()][mz.getStart().getX()] = true;
		pq.add(mz.getStart());
		while(mz.getGoals().size() != 0) {
			Pair cur = pq.poll();
			rd.addExpand(); //Record expand Nodes
			if(mz.checkGoal(cur)) {
				last = cur;
			}
			Pair up = ag.moveUp(cur, visited);
			Pair down = ag.moveDown(cur, visited);
			Pair left = ag.moveLeft(cur, visited);
			Pair right = ag.moveRight(cur, visited);
			if(up != null) {
				visited[up.getY()][up.getX()] = true;
				up.setParent(cur);
				pq.add(up);
			}
			if(left != null) {
				visited[left.getY()][left.getX()] = true;
				left.setParent(cur);
				pq.add(left);
			}
			if(down != null) {
				visited[down.getY()][down.getX()] = true;
				down.setParent(cur);
				pq.add(down);
			}
			if(right != null) {
				visited[right.getY()][right.getX()] = true;
				right.setParent(cur);
				pq.add(right);
			}
		}
		rd.setCost(last.getGx()); //Record cost
		//record the optimal path to the agent 
		Pair cur = last;
		List<Pair> path = ag.getPath();
		while(cur != null) {
			path.add(cur);
			cur = cur.getParent();
		}
		Maze.printMaze(mz, ag);
	}

	public static void Astar(Agent ag, Maze mz, Recorder rd) {
		//Sanity check..
		if(ag == null || mz == null) {
			System.out.println("Astar Search fail because of invalid input arguments.");
			return;
		}
		char[][] board = mz.getMaze();
		boolean[][] visited = new boolean[mz.getRowLen()][mz.getColLen()];
		PriorityQueue<Pair> pq = new PriorityQueue<Pair>(mz.getRowLen() * mz.getColLen(), new Comparator<Pair>(){
			@Override
			public int compare(Pair a, Pair b) {
				int aVal = a.getGx() + a.getHx();
				int bVal = b.getGx() + b.getHx();
				if(aVal == bVal) {
					return 0;
				}
				return aVal < bVal? -1 : 1;
			}
		});
		Pair last = null; //The pair reference which refer to the last step of the game to reach the goal
		pq.add(mz.getStart());
		mz.getStart().setPath(new ArrayList<Pair>());
		mz.getStart().getPath().add(mz.getStart());
		visited[mz.getStart().getY()][mz.getStart().getX()] = true;
		while(mz.getGoals().size() != 0) {
			Pair cur = pq.poll();
			rd.addExpand(); //Record expand Nodes
			if(mz.checkGoal(cur)) {
				last = cur;
			}
			Pair up = ag.moveUp(cur, visited);
			Pair down = ag.moveDown(cur, visited);
			Pair left = ag.moveLeft(cur, visited);
			Pair right = ag.moveRight(cur, visited);
			if(up != null) {
				visited[up.getY()][up.getX()] = true;
				up.setParent(cur);
				up.setPath(new ArrayList<Pair>(cur.getPath()));
				up.getPath().add(up);
				pq.add(up);
			}
			if(left != null) {
				visited[left.getY()][left.getX()] = true;
				left.setParent(cur);
				left.setPath(new ArrayList<Pair>(cur.getPath()));
				left.getPath().add(left);
				pq.add(left);
			}
			if(down != null) {
				visited[down.getY()][down.getX()] = true;
				down.setParent(cur);
				down.setPath(new ArrayList<Pair>(cur.getPath()));
				down.getPath().add(down);
				pq.add(down);
			}
			if(right != null) {
				visited[right.getY()][right.getX()] = true;
				right.setParent(cur);
				right.setPath(new ArrayList<Pair>(cur.getPath()));
				right.getPath().add(right);
				pq.add(right);
			}
		}
		rd.setCost(last.getGx()); //record cost
		//record the optimal path to the agent 
		Pair cur = last;
		List<Pair> path = ag.getPath();
		while(cur != null) {
			path.add(cur);
			cur = cur.getParent();
		}
		System.out.println(last.getPath().size());
		Maze.printMaze(mz, ag);
	}
	public static void AstarDir(Agent ag, Maze mz, Recorder rd) {
		//Sanity check..
		if(ag == null || mz == null) {
			System.out.println("Astar Search fail because of invalid input arguments.");
			return;
		}
		char[][] board = mz.getMaze();
		//visited[mz.getRowLen()][mz.getColLen()][robot Direction]
		boolean[][][] visited = new boolean[mz.getRowLen()][mz.getColLen()][5];
		PriorityQueue<Pair> pq = new PriorityQueue<Pair>(mz.getRowLen() * mz.getColLen(), new Comparator<Pair>(){
			@Override
			public int compare(Pair a, Pair b) {
				int aVal = a.getGx() + a.getHx();
				int bVal = b.getGx() + b.getHx();
				if(aVal == bVal) {
					return 0;
				}
				return aVal < bVal? -1 : 1;
			}
		});
		Pair last = null; //The pair reference which refer to the last step of the game to reach the goal
		pq.add(mz.getStart());
		mz.getStart().setPath(new ArrayList<Pair>());
		mz.getStart().getPath().add(mz.getStart());
		visited[mz.getStart().getY()][mz.getStart().getX()][mz.getStart().getDir()] = true;
		while(mz.getGoals().size() != 0) {
			Pair cur = pq.poll();
			rd.addExpand(); //Record expand Nodes
			if(mz.checkGoal(cur)) {
				last = cur;
			}
			Pair up = ag.moveUpDir(cur, visited);
			Pair down = ag.moveDownDir(cur, visited);
			Pair left = ag.moveLeftDir(cur, visited);
			Pair right = ag.moveRightDir(cur, visited);
			if(up != null) {
				visited[up.getY()][up.getX()][Direction.North] = true;
				up.setParent(cur);
				up.setPath(new ArrayList<Pair>(cur.getPath()));
				up.getPath().add(up);
				pq.add(up);
			}
			if(left != null) {
				visited[left.getY()][left.getX()][Direction.West] = true;
				left.setParent(cur);
				left.setPath(new ArrayList<Pair>(cur.getPath()));
				left.getPath().add(left);
				pq.add(left);
			}
			if(down != null) {
				visited[down.getY()][down.getX()][Direction.South] = true;
				down.setParent(cur);
				down.setPath(new ArrayList<Pair>(cur.getPath()));
				down.getPath().add(down);
				pq.add(down);
			}
			if(right != null) {
				visited[right.getY()][right.getX()][Direction.East] = true;
				right.setParent(cur);
				right.setPath(new ArrayList<Pair>(cur.getPath()));
				right.getPath().add(right);
				pq.add(right);
			}
		}
		rd.setCost(last.getGx()); //record cost
		//record the optimal path to the agent 
		Pair cur = last;
		List<Pair> path = ag.getPath();
		while(cur != null) {
			path.add(cur);
			cur = cur.getParent();
		}
		Maze.printMaze(mz, ag);
	}
	//Assuming that there is only one ghost
	public static void AstarGhost(Agent ag, Maze mz, Recorder rd) {
		if(ag == null || mz == null) {
			System.out.println("Astar Search fail because of invalid input arguments.");
			return;
		}
		char[][] board = mz.getMaze();
		//visited[robotY][robotX][GhostY][GhostX][Ghost Direction]
		boolean[][][][][] visited = new boolean[mz.getRowLen()][mz.getColLen()][mz.getRowLen()][mz.getColLen()][5];
		PriorityQueue<Pair> pq = new PriorityQueue<Pair>(mz.getRowLen() * mz.getColLen(), new Comparator<Pair>(){
			@Override
			public int compare(Pair a, Pair b) {
				int aVal = a.getGx() + a.getHx();
				int bVal = b.getGx() + b.getHx();
				if(aVal == bVal) {
					return 0;
				}
				return aVal < bVal? -1 : 1;
			}
		});
		Pair last = null; //The pair reference which refer to the last step of the game to reach the goal
		Pair st = mz.getStart();
		Pair gLoc = st.getGhost().getLoc();
		pq.add(mz.getStart());
		mz.getStart().setPath(new ArrayList<Pair>());
		mz.getStart().getPath().add(mz.getStart());
		visited[st.getY()][st.getX()][gLoc.getY()][gLoc.getX()][gLoc.getDir()] = true;
		while(mz.getGoals().size() != 0) {
			Pair cur = pq.poll();
			rd.addExpand(); //Record expand Nodes
			if(mz.checkGoal(cur)) {
				last = cur;
			}
			Pair up = ag.moveUpGhost(cur, visited);
			Pair down = ag.moveDownGhost(cur, visited);
			Pair left = ag.moveLeftGhost(cur, visited);
			Pair right = ag.moveRightGhost(cur, visited);
			if(up != null) {
				gLoc = up.getGhost().getLoc();
				visited[up.getY()][up.getX()][gLoc.getY()][gLoc.getX()][Direction.North] = true;
				up.setParent(cur);
				up.setPath(new ArrayList<Pair>(cur.getPath()));
				up.getPath().add(up);
				pq.add(up);
			}
			if(left != null) {
				gLoc = left.getGhost().getLoc();
				visited[left.getY()][left.getX()][gLoc.getY()][gLoc.getX()][Direction.West] = true;
				left.setParent(cur);
				left.setPath(new ArrayList<Pair>(cur.getPath()));
				left.getPath().add(left);
				pq.add(left);
			}
			if(down != null) {
				gLoc = down.getGhost().getLoc();
				visited[down.getY()][down.getX()][gLoc.getY()][gLoc.getX()][Direction.South] = true;
				down.setParent(cur);
				down.setPath(new ArrayList<Pair>(cur.getPath()));
				down.getPath().add(down);
				pq.add(down);
			}
			if(right != null) {
				gLoc = right.getGhost().getLoc();
				visited[right.getY()][right.getX()][gLoc.getY()][gLoc.getX()][Direction.East] = true;
				right.setParent(cur);
				right.setPath(new ArrayList<Pair>(cur.getPath()));
				right.getPath().add(right);
				pq.add(right);
			}
		}
		rd.setCost(last.getGx()); //record cost
		//record the optimal path to the agent 
		Pair cur = last;
		List<Pair> path = ag.getPath();
		while(cur != null) {
			path.add(cur);
			cur = cur.getParent();
		}
		for(Pair p : last.getPath()) {
			System.out.println(p.getY() + " " + p.getX());
			System.out.println(p.getGhost().getLoc().getY() + " " + p.getGhost().getLoc().getX());
			System.out.println();
		}
		Maze.printMaze(mz, ag);
	}
	public static void AstarMul(Agent ag, Maze mz, Recorder rd) {
		if(ag == null || mz == null || rd == null) {
			return;
		}
		Pair[] goals = new Pair[mz.getGoals().size()];
		int i = 0;
		HashSet<Pair> hs = mz.getGoals();
		for(Pair p : hs) {
			goals[i++] = p;
		}
		int[][] M = new int[mz.getGoals().size() + 1][mz.getGoals().size() + 1];
		for(int k = 0; k < goals.length; k++) {
			for(int l = k + 1; l < goals.length; l++) {
				M[k][l] = AstarPairToPair(new Pair(goals[k]), new Pair(goals[l]), mz, rd);
			}
		}
		for(int k = 0; k < M.length - 1; k++) {
			M[M[0].length - 1][k] = AstarPairToPair(new Pair(new Pair(ag.getStart())), new Pair(goals[k]), mz, rd);
		}
		HashMap<Pair, Integer> helper = new HashMap<Pair, Integer>();
		for(i = 0; i < goals.length; i++) {
			helper.put(goals[i], i);
		}
		getPermutation(0, goals, new ArrayList<Integer>(), helper, M);
		if(minRecord != null) {
			int g = 0;
			for(int k : minRecord) {
				if(g <= 9) {
					mz.getMaze()[goals[k].getY()][goals[k].getX()] = (char)('0' + g);
				} else if(g <= 35) {
					mz.getMaze()[goals[k].getY()][goals[k].getX()] = (char)('a' + (g - 9));
				} else {
					mz.getMaze()[goals[k].getY()][goals[k].getX()] = (char)('A' + (g - 35));
				}
				g++;
			}
		}
		rd.setCost(min);
		Maze.printMaze(mz, null);
	}
	private static int AstarPairToPair(Pair from, Pair to, Maze mz, Recorder rd) {
		final Pair From = from;
		final Pair To = to;
		char[][] board = mz.getMaze();
		boolean[][] visited = new boolean[mz.getRowLen()][mz.getColLen()];
		PriorityQueue<Pair> pq = new PriorityQueue<Pair>(mz.getRowLen() * mz.getColLen(), new Comparator<Pair>(){
			@Override
			public int compare(Pair a, Pair b) {
				int aVal = a.getGx() + Math.abs(a.getY() - To.getY()) + Math.abs(a.getX() - To.getX());
				int bVal = b.getGx() + Math.abs(b.getY() - To.getY()) + Math.abs(b.getX() - To.getX());
				if(aVal == bVal) {
					return 0;
				}
				return aVal < bVal? -1 : 1;
			}
		});
		Pair last;
		pq.add(from);
		from.setPath(new ArrayList<Pair>());
		from.getPath().add(from);
		visited[from.getY()][from.getX()] = true;
		Agent ag = new Agent(mz); //Fake agent
		while(true) {
			Pair cur = pq.poll();
			rd.addExpand();
			if(cur.equals(to)) {
				last = cur;
				break;
			}
			Pair up = ag.moveUp(cur, visited);
			Pair down = ag.moveDown(cur, visited);
			Pair left = ag.moveLeft(cur, visited);
			Pair right = ag.moveRight(cur, visited);
			if(up != null) {
				visited[up.getY()][up.getX()] = true;
				up.setParent(cur);
				up.setPath(new ArrayList<Pair>(cur.getPath()));
				up.getPath().add(up);
				pq.add(up);
			}
			if(left != null) {
				visited[left.getY()][left.getX()] = true;
				left.setParent(cur);
				left.setPath(new ArrayList<Pair>(cur.getPath()));
				left.getPath().add(left);
				pq.add(left);
			}
			if(down != null) {
				visited[down.getY()][down.getX()] = true;
				down.setParent(cur);
				down.setPath(new ArrayList<Pair>(cur.getPath()));
				down.getPath().add(down);
				pq.add(down);
			}
			if(right != null) {
				visited[right.getY()][right.getX()] = true;
				right.setParent(cur);
				right.setPath(new ArrayList<Pair>(cur.getPath()));
				right.getPath().add(right);
				pq.add(right);
			}
		}
		return last.getPath().size() - 1;
	}
	static int min = Integer.MAX_VALUE;
	static List<Integer> minRecord = null;
	private static void getPermutation(int idx, Pair[] goals, List<Integer> sol, HashMap<Pair, Integer> helper, int[][] M) {
		if(idx == goals.length) {
			int cost = M[M[0].length - 1][sol.get(0)];
			for(int j = 1; j < sol.size(); j++) {
					cost += Math.max(M[sol.get(j)][sol.get(j - 1)], M[sol.get(j - 1)][sol.get(j)]);
			}
			if(cost < min) {
				minRecord = new ArrayList<Integer>(sol);
				min = cost;
			}
			return;
		}
		for(int i = idx; i < goals.length; i++) {
			swap(goals, idx, i);
			sol.add(helper.get(goals[idx]));
			getPermutation(idx + 1, goals, sol, helper, M);
			sol.remove(sol.size() - 1);
			swap(goals, idx, i);
		}
	}
	private static void swap(Pair[] goals, int i, int j) {
		Pair t = goals[i];
		goals[i] = goals[j];
		goals[j] = t;
	}

	public static void AstarMulOptimal(Agent ag, Maze mz, Recorder rd) {
		if(ag == null || mz == null || rd == null) {
			return;
		}
		Pair[] goals = new Pair[mz.getGoals().size()];
		int i = 0;
		Pair st = ag.getStart();
		HashSet<Pair> hs = mz.getGoals();
		HashMap<Pair, Integer> hm = new HashMap<Pair, Integer>();
		for(Pair p : hs) {
			hm.put(p, i);
			goals[i++] = p;
		}
		List<Integer> result = new ArrayList<Integer>();
		Pair cur = st;
		int cost = 0;

		while(hs.size() != 0) {
			int mini = Integer.MAX_VALUE;
			Pair minP = null;
			List<Pair> helper = new ArrayList<Pair>();
			for(Pair p : hs) {
				helper.add(p);
			}
			final Pair t = cur;
			Collections.sort(helper, new Comparator<Pair>(){
				@Override
				public int compare(Pair a, Pair b) {
					int aVal = Math.abs(a.getX() - t.getX()) + Math.abs(a.getY() - t.getY());
					int bVal = Math.abs(b.getX() - t.getX()) + Math.abs(b.getY() - t.getY());
					if(aVal == bVal) {
						return 0;
					}
					return aVal < bVal? -1 : 1;
				}
			});
			for(int k = 0; k < 10 && k < helper.size(); k++) {
				int astarVal = AstarPairToPair(cur, helper.get(k), mz, rd);
				if(astarVal < mini) {
					minP = helper.get(k);
					mini = astarVal;
				}
			}
			result.add(hm.get(minP));
			cur = minP;
			cost += mini;
			hs.remove(minP);
		}
		int g = 0;
		for(int k : result) {
			if(g <= 9) {
				mz.getMaze()[goals[k].getY()][goals[k].getX()] = (char)('0' + g);
			} else if(g <= 35) {
				mz.getMaze()[goals[k].getY()][goals[k].getX()] = (char)('a' + (g - 9));
			} else {
				mz.getMaze()[goals[k].getY()][goals[k].getX()] = (char)('A' + (g - 35));
			}
			g++;
		}
		Maze.printMaze(mz, null);
		rd.setCost(cost);
		System.out.println(cost);
		System.out.println(rd.getExpandNodes());
	}
}
