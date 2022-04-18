import game.Session;
import player.CheatingMCTSPlayer;
import player.HumanPlayer;
import player.ISPlayer;
import player.RandomPlayer;
import player.RuleBasedPlayer;

public class Main {

  public static void main(String[] args){
    HumanPlayer jann = new HumanPlayer();
    HumanPlayer gegner = new HumanPlayer();
    RuleBasedPlayer rule = new RuleBasedPlayer();
    RuleBasedPlayer rule2 = new RuleBasedPlayer();
    RandomPlayer random = new RandomPlayer();
    CheatingMCTSPlayer cheater = new CheatingMCTSPlayer(false,4000);
    ISPlayer smart = new ISPlayer(true,8000,0.7,0);
    Session game = new Session(random,smart);
    int[] scores = game.playGame();
    System.out.println("Score Player 1:" + scores[0] + "\tScore Player 2:" + scores[1]);
    //countWins();
  }

  public static void countWins(){
    String wins = "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Light\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy\n"
        + "Game won by player Heavy";
    String[] array = wins.split("\n");
    System.out.println(array.length);
    int countWinsHeavy = 0;
    int countWinsLight = 0;
    for(String s : array){
      if(s.equals("Game won by player Heavy")){
        countWinsHeavy++;
      }
      if(s.equals("Game won by player Light")){
        countWinsLight++;
      }
    }
    System.out.println("Heavy" + countWinsHeavy + "Light" + countWinsLight);
  }

}
