package test;

import game.Session;
import org.junit.Test;
import player.CheatingMCTSPlayer;
import player.RandomPlayer;
import player.RuleBasedPlayer;

public class LostTest {

  @Test
  public void testSpeed(){
    RuleBasedPlayer rule = new RuleBasedPlayer();
    RuleBasedPlayer rule2 = new RuleBasedPlayer();
    RandomPlayer random = new RandomPlayer();
    RandomPlayer random2 = new RandomPlayer();
    CheatingMCTSPlayer light = new CheatingMCTSPlayer(false);
    int winsP1 = 0;
    for(int i = 0;i<1;i++){
      Session game = new Session(light,random2);
      int[] scores = game.cheatGame();
      if(scores[0]>scores[1]) winsP1++;
    }
    System.out.println(winsP1);

  }


}
