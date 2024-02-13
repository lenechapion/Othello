
public class Stone {
	private String State;//黒=● ,白=○ 空=・
	private int x;
	private int y;

	public Stone(int x, int y) {
		this.State = "・";
		this.x = x;
		this.y = y;
	}

	public String getState() {
		return this.State;
	}

	public void setState(String state) {
		this.State = state;
	}

	public int[] getPosition() {
		int[] pos = { this.x, this.y };
		return pos;
	}

}