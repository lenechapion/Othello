import java.util.Random;

public class Player {
	private String[] order = new String[2];

	public Player() {
		;
	}

	//順番を決める
	public void decideOrder() {
		Random rand = new Random();
		int num = rand.nextInt(2);
		if (num == 0) {
			order[0] = "CPU";
			order[1] = "あなた";
		} else {
			order[0] = "あなた";
			order[1] = "CPU";
		}
	}

	//色を指定
	public String color(boolean player) {
		if (player) {
			return "●";
		} else {
			return "○";
		}
	}

	//順番を示す
	public String indicateOrder(boolean player) {
		if (player) {
			return order[0];
		} else {
			return order[1];
		}
	}

}