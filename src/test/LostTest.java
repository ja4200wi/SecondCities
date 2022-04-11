package test;

import game.Session;
import org.junit.Test;
import player.CheatingMCTSPlayer;
import player.RandomPlayer;
import player.RuleBasedPlayer;

public class LostTest {

  @Test
  public void testAPI(){
    RandomPlayer random;
    RandomPlayer random2;
    //Session game = new Session(random,random2);
  }

  @Test
  public void testSpeed(){
    RuleBasedPlayer rule = new RuleBasedPlayer();
    RuleBasedPlayer rule2 = new RuleBasedPlayer();
    RandomPlayer random = new RandomPlayer();
    RandomPlayer random2 = new RandomPlayer();
    CheatingMCTSPlayer light = new CheatingMCTSPlayer(false,3000);
    CheatingMCTSPlayer heavy = new CheatingMCTSPlayer(true,3000);
    int winsP1 = 0;
    for(int i = 0;i<10;i++){
      Session game = new Session(light,heavy);
      game.cheatGame();
      int[] scores = game.cheatGame();
      if(scores[0]>scores[1]) winsP1++;
      System.out.println(scores[0] + " : " + scores[1]);
      System.out.println(game);
    }
    System.out.println(winsP1);

  }

}
