import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {
	private static Scanner scanner = new Scanner(System.in);
	private static final String results_file = "results.txt"; // 対戦成績を保存するファイル

	public static void main(String[] args) {
		Title();

		while (true) {
			int user = oneselfUser();
			switch (user) {
			case 1:
				playGame();
				break;
			case 2:
				showResults();
				break;
			case 3:
				System.out.println("***終了します***");
				return;
			default:
				break;
			}

			System.out.println();
			System.out.println("***続けますか？***");
			System.out.println("***１：はい　２：いいえ***");
			int end = scanner.nextInt();
			if (end == 1) {
				Title();
			}
			if (end == 2) {
				System.out.println("***お疲れさまでした！***");
				break;
			}
		}
	}

	public static void Title() {
		System.out.println("***１：始める　２：前回の勝敗　３：終わる***");
	}

	private static int oneselfUser() {//選択肢

		while (true) {
			if (scanner.hasNextInt()) {
				int number = scanner.nextInt();
				if (number <= 0 || number >= 4) {
					System.out.println("***エラー：入力できるのは「１～３」です***");
					continue;
				} else {
					return number;
				}
			}
		}
	}

	public static void playGame() {
		boolean player = true; //先攻：true
		Board board = new Board(8, 8);
		Player play = new Player();

		board.prepare(); //盤面の初期設定
		play.decideOrder();//先攻後攻
		System.out.print("***先攻と後攻を決定します***");
		for (int i = 0; i < 3; i++) {
			try {
				System.out.print(".");
				Thread.sleep(3000);
			} catch (Exception e) {
			}
		}
		System.out.println();
		System.out.println();

		System.out.println("***先攻・後攻を決定しました***");
		System.out
				.println("***先攻(●)・" + play.indicateOrder(player) + "、後攻(○)・" + play.indicateOrder(!player) + "です***");
		System.out.println();
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}
		System.out.println("***それでは対戦開始です。***");
		System.out.println();
		board.feature();
		System.out.println("----------------------------------");
		System.out.println();
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}

		while (true) {
			List<int[]> mine = board.getCoordinate(player);
			List<int[]> not_mine = board.getCoordinate(!player);

			if (mine.size() == 0 & not_mine.size() == 0) {
				break;
			}

			String st = play.indicateOrder(player); //順番
			System.out.println("*** " + st + "の番です。(石： " + play.color(player) + ")***");
			System.out.println();

			int x;
			int y;

			if (mine.size() == 0) {//置くところがなければパス
				board.feature();
				System.out.println("***石を置く場所がありません。パスします。***");
				System.out.println();
				System.out.println("----------------------------------");
				System.out.println();

				player = !player;//交代
			} else {

				while (true) {

					if (st == "CPU") {
						Random rand = new Random();
						int id;
						if (mine.size() > 0) {
							id = rand.nextInt(mine.size());
						} else {
							id = rand.nextInt(1);
						}

						int[] pos = mine.get(id);
						x = pos[0];
						y = pos[1];
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
						}
					} else {
						System.out.print("***石を置く場所を入力してください (例 2b)***");
						System.out.println();

						String input = scanner.next();
						//１文字入力NGチェック文
						if(input.length() != 2) {
							System.out.println("***２文字で入力してください***");
							continue;
						}
						char x_input = input.charAt(1);
						char y_input = input.charAt(0);
						int[] input_coordinate = board.receive(x_input, y_input);
						x = input_coordinate[0];
						y = input_coordinate[1];
					}

					int judge = board.turnover(x, y, player, mine);
					if (judge == 1) {
						break;
					}
				}
				//石を置けたら盤を表示
				board.feature();
				System.out.println("----------------------------------");
				System.out.println();

				player = !player;
			}

		}
		//ゲーム終了、勝敗を宣言
		System.out.println("***対戦終了***");
		board.feature();
		player = true;
		int black = board.countBlack();
		int white = board.countWhite();
		String result;
		System.out.println("***●：" + black + "個***");
		System.out.println("***○：" + white + "個***");
		if (black > white) {
			System.out.println("***"+play.indicateOrder(player) + "(●)の勝ち！***");
			result = "win";
		} else if (white > black) {
			System.out.println("***"+play.indicateOrder(!player) + "(○)の勝ち！***");
			result = "lose";
		} else {
			System.out.println("引き分け！");
			result = "draw";
		}
		saveResultsToFile(result);

	}

	private static void saveResultsToFile(String result) {// 前回の勝敗結果を読み込み

		int win = 0, lose = 0, draw = 0;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(results_file), "UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("Win: ")) {
					win = Integer.parseInt(line.substring(6));
				} else if (line.startsWith("Lose: ")) {
					lose = Integer.parseInt(line.substring(8));
				} else if (line.startsWith("Draw: ")) {
					draw = Integer.parseInt(line.substring(7));
				}
			}
		} catch (Exception e) {
			;
		}

		// 成績を更新
		switch (result) {
		case "win":
			win++;
			break;
		case "lose":
			lose++;
			break;
		case "draw":
			draw++;
			break;
		}

		// 更新した戦績をファイルに保存
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(results_file),"UTF-8"))) {
			bw.write("Win: " + win + "\n");
			bw.write("Lose: " + lose + "\n");
			bw.write("Draw: " + draw);
			System.out.println("***勝敗記録を保存しました***");
		} catch (Exception e) {
			System.out.println("***記録の保存に失敗しました***");
			e.printStackTrace();
		}
	}

	private static void showResults() {
		// 成績をファイルから読み込んで表示
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(results_file), "UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			System.out.println("***記録の読み込みに失敗しました***");
			e.printStackTrace();
		}
	}
}