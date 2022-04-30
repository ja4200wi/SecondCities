package player;

import game.Card;
import game.Move;
import game.Session;
import montecarlo.InformationIS;
import montecarlo.MonteCarloIS;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Jann Winter
 * This class represents a MCTS player, which has a memory to identify opponent's cards.
 */

public class ISPlayer extends MemoryPlayer{

  private int time; //time given to construct mcts tree
  private int playOutStyle; //true -> use RuleBased for simulation; 0=random;1=rule;2=human
  private boolean reduceBranching; //true -> dont expand all moves
  double explorationConstant;
  int rewardStrategy; //0 =
  private ArrayList<Card> cardsOfOpp = new ArrayList<>();
  private Stack<Card>[] oldDiscardPile = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
  private Stack<Card>[] oldOppExp = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};

  public ISPlayer(int playOutStyle,int time,double explorationConstant,int rewardStrategy,boolean reduceBranching){
    super();
    this.time = time;
    this.playOutStyle = playOutStyle;
    this.explorationConstant = explorationConstant;
    this.rewardStrategy = rewardStrategy;
    this.hasMemory = true;
    this.reduceBranching = reduceBranching;
  }

  @Override
  public Move makeMove(Card[] myHand,Stack<Card>[] myExp, Stack<Card>[] oppExp, Stack<Card>[] discardPile,boolean turn,int turnCounter) {
    detectOpponentsCards(discardPile,oppExp);
    InformationIS information = new InformationIS(myHand,myExp,oppExp,discardPile,turn,this.imP1,turnCounter,cardsOfOpp);
    Move m = MonteCarloIS
        .ismcts(information,time,playOutStyle,explorationConstant,rewardStrategy,reduceBranching);
    memorizeDiscardPile(discardPile,m,myHand);
    memorizeOppExpPile(oppExp);
    return m;
  }

  /**
   * This method adds cards to opponent model if a card was drawn from discard pile (by opponent).
   * @param discardPile
   * @param oppExp
   */
  public void detectOpponentsCards(Stack<Card>[] discardPile,Stack<Card>[] oppExp){
    boolean drawFromDiscardDetected = false;
    int colorOfDraw = -1;
    for(int i = 0;i<5;i++){
      drawFromDiscardDetected = discardPile[i].size()<oldDiscardPile[i].size();
      if(drawFromDiscardDetected) {
        colorOfDraw = i;
        i=10;
      }
    }
    if(drawFromDiscardDetected){
      cardsOfOpp.add(oldDiscardPile[colorOfDraw].peek().clone());
    }
    didOpponentRemove(discardPile,oppExp); //Update known cards
  }

  /**
   * Removes cards from opponent model if opponent placed cards onto game board.
   * @param discardPile
   * @param oppExp
   */
  public void didOpponentRemove(Stack<Card>[] discardPile,Stack<Card>[] oppExp){
    int remove = -1;
    int counter = 0;
    for(Card c : cardsOfOpp){
      int color = c.getColor();
      if(discardPile[color].size()>oldDiscardPile[color].size() && (c.equals(discardPile[color]) || (c.isCoinCard()&&discardPile[color].peek().isCoinCard()))) remove = counter;
      if(oppExp[color].size()>oldOppExp[color].size() && (c.equals(oppExp[color]) || (c.isCoinCard()&&oppExp[color].peek().isCoinCard()))) remove = counter;
      //if(oppExp[color].size()>oldOppExp[color].size()) remove = counter;
      counter++;
    }
    if(remove>=0){
      cardsOfOpp.remove(remove);
    }
  }

  /**
   * Memorize the opponent's expeditions by deep copying the prior
   * @param oppExp
   */
  public void memorizeOppExpPile(Stack<Card>[] oppExp) {
    oldDiscardPile = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(),
        new Stack<Card>(), new Stack<Card>()};
    for (int i = 0; i < 5; i++) {
      for (Card c : oppExp[i]) {
        oldOppExp[i].add(c.clone());
      }
    }
  }

  /**
   * Memorize the discard pile by deep copying the prior and including cards placed in current move
   * @param discardPile
   * @param move
   * @param hand
   */
  public void memorizeDiscardPile(Stack<Card>[] discardPile,Move move,Card[] hand){
    oldDiscardPile = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    for(int i = 0;i<5;i++){
      for(Card c : discardPile[i]) {
        oldDiscardPile[i].add(c.clone());
      }
    }
    if(!move.isOnExp()){
      Card cloneCard = hand[move.getCardIndex()].clone();
      oldDiscardPile[cloneCard.getColor()].add(cloneCard);
    }
    if(move.getDrawFrom()!=0){
      oldDiscardPile[move.getDrawFrom()-1].pop();
    }
  }

  /**
   * This method needs ti be called before a memory player is added to a game.
   */
  public void resetMemory(){
    this.oldDiscardPile = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    this.cardsOfOpp = new ArrayList<>();
    this.oldOppExp = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
  }
  @Override
  public Move makeMove(Session session) {
    return null;
  }
}
