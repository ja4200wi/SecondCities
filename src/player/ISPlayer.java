package player;

import game.Card;
import game.Move;
import game.Session;
import java.util.ArrayList;
import java.util.Stack;
import montecarlo.InformationIS;
import montecarlo.MonteCarloIS;

public class ISPlayer extends Player{

  private int time;
  private boolean heavyPlayout;
  double explorationConstant;
  int rewardStrategy;
  private ArrayList<Card> cardsOfOpp = new ArrayList<>();
  private Stack<Card>[] oldDiscardPile = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};

  public ISPlayer(boolean heavyPlayout,int time,double explorationConstant,int rewardStrategy){
    super();
    this.time = time;
    this.heavyPlayout = heavyPlayout;
    this.explorationConstant = explorationConstant;
    this.rewardStrategy = rewardStrategy;
  }

  @Override
  public Move makeMove(Card[] myHand,Stack<Card>[] myExp, Stack<Card>[] oppExp, Stack<Card>[] discardPile,boolean turn,int turnCounter) {
    detectOpponentsCards(discardPile);
    InformationIS information = new InformationIS(myHand,myExp,oppExp,discardPile,turn,this.imP1,turnCounter,cardsOfOpp);
    Move m = MonteCarloIS.ismcts(information,time,heavyPlayout,explorationConstant,rewardStrategy);
    memorizeDiscardPile(discardPile,m,myHand);
    return m;
  }

  public void detectOpponentsCards(Stack<Card>[] discardPile){
    cardsOfOpp = new ArrayList<>();
    boolean drawFromDiscardDetected = false;
    int colorOfDraw = -1;
    for(int i = 0;i<5;i++){
      drawFromDiscardDetected = discardPile[i].size()<oldDiscardPile[i].size();
      colorOfDraw = i;
    }
    if(drawFromDiscardDetected){
      cardsOfOpp.add(oldDiscardPile[colorOfDraw].peek().clone());
    }
  }

  public void memorizeDiscardPile(Stack<Card>[] discardPile,Move move,Card[] hand){
    for(int i = 0;i<5;i++){
      oldDiscardPile[i] = (Stack<Card>) discardPile[i].clone();
    }
    if(!move.isOnExp()){
      Card cloneCard = hand[move.getCardIndex()].clone();
      oldDiscardPile[cloneCard.getColor()].add(cloneCard);
    }
    if(move.getDrawFrom()!=0){
      oldDiscardPile[move.getDrawFrom()-1].pop();
    }
  }

  @Override
  public Move makeMove(Session session) {
    return null;
  }
}
