package player;

import game.Card;
import game.Move;
import game.Session;
import java.util.Stack;
import montecarlo.UCT;

public class CheatingMCTSPlayer extends Player {

  boolean heavy;
  int resource;

  public CheatingMCTSPlayer(boolean heavy,int resource){
    super();
    this.heavy = heavy;
    this.isCheating = true;
    //this.resource = (heavy)?resource/4:resource;
    this.resource = resource;
  }

  public Move makeMove(Session game){
    UCT uct = new UCT();
    if(heavy) return uct.mctsSearchTimed(game,resource,true);
    return uct.mctsSearchTimed(game,resource,false);
  }

  @Override
  public Move makeMove(Card[] myHand,Stack<Card>[] myExp, Stack<Card>[] oppExp, Stack<Card>[] discardPile,boolean turn,int turnCounter) {
    return null;
  }
}
