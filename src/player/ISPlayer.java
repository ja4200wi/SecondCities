package player;

import game.Card;
import game.Move;
import game.Session;
import java.util.Stack;
import montecarlo.InformationIS;
import montecarlo.MonteCarloIS;

public class ISPlayer extends Player{

  private int iteration;
  private boolean heavyPlayout;

  public ISPlayer(boolean heavyPlayout,int iteration){
    super();
    this.iteration = iteration;
    this.heavyPlayout = heavyPlayout;
  }

  @Override
  public Move makeMove(Card[] myHand,Stack<Card>[] myExp, Stack<Card>[] oppExp, Stack<Card>[] discardPile,boolean turn) {
    InformationIS information = new InformationIS(myHand,myExp,oppExp,discardPile,turn,this.imP1);
    Move m = MonteCarloIS.ismcts(information,iteration,heavyPlayout);
    return m;
  }

  @Override
  public Move makeMove(Session session) {
    return null;
  }
}
