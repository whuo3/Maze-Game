public class Recorder {
	private int expandNodes;
	private int	cost;
	public void setCost(int cost) {
		this.cost = cost;
	}
	public void setExpand(int exp) {
		this.expandNodes = exp;
	}
	public void addExpand() {
		expandNodes++;
	}
	public int getCost() {
		return cost;
	}
	public int getExpandNodes() {
		return expandNodes;
	}
}