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
    ISPlayer smart = new ISPlayer(false,1000);
    Session game = new Session(smart,random);
    int[] scores = game.playGame();
    System.out.println("Score Player 1:" + scores[0] + "\tScore Player 2:" + scores[1]);
  }

}
