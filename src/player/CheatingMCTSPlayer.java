package player;

import game.Card;
import game.Move;
import game.Session;
import java.util.Stack;
import montecarlo.UCT;

public class CheatingMCTSPlayer extends Player {

  boolean heavy;

  public CheatingMCTSPlayer(boolean heavy){
    super();
    this.heavy = heavy;
    this.isCheating = true;
  }

  public Move makeMove(Session game){
    UCT uct = new UCT();
    if(heavy) return uct.mctsSearch(game,10,true);
    return uct.mctsSearch(game,10000,false);
  }

  @Override
  public Move makeMove(Stack<Card>[] myExp, Stack<Card>[] oppExp, Stack<Card>[] discardPile) {
    return null;
  }
}
