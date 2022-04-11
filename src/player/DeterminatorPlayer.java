package player;

import game.Card;
import game.Move;
import game.Session;
import java.util.ArrayList;
import java.util.Stack;
import montecarlo.Node;
import montecarlo.UCT;

public class DeterminatorPlayer extends Player {

  private int iterations;
  boolean heavy;

  public DeterminatorPlayer(int iter){
    this.iterations = (heavy)?iterations/4:iterations;
  }

  @Override
  public Move makeMove(Stack<Card>[] myExp, Stack<Card>[] oppExp, Stack<Card>[] discardPile) {
    Move m = determinization(10,1000,myExp,oppExp,discardPile);
    return m;
  }

  public Move determinization(int numberDeterminizations,int iterationsPerDeterminization,Stack<Card>[] myExp, Stack<Card>[] oppExp, Stack<Card>[] discardPile){
    UCT uct = new UCT();
    Node[] roots = new Node[numberDeterminizations]; //Array with roots of different mct
    for(int i = 0;i<numberDeterminizations;i++){
      roots[i] = uct.mctsSearchReturnTree(createDeterminization(myExp,oppExp,discardPile),iterationsPerDeterminization,heavy);
    }
    Node[] children = roots[0].getChildren().toArray(new Node[0]); //Array with space for all children
    for(int i = 1;i<numberDeterminizations;i++) {
      for(Node child : children){
        updateStats(child,roots[i]);
      }
    }
    Node bestChild = bestChild(children);
    return uct.getMove(new Node(createDeterminization(myExp,oppExp,discardPile)),bestChild);
  }

  public void updateStats(Node aggregate,Node oneDetStat){
    aggregate.setSimulationsPlayed(aggregate.getSimulationsPlayed()+ oneDetStat
        .getSimulationsPlayed());
    aggregate.setSimulationsWon(aggregate.getSimulationsWon()+ aggregate.getSimulationsWon());
  }

  public Node bestChild(Node[] children){
    int max = -1;
    Node bestChild = null;
    for(Node n : children){
      if(n.getSimulationsWon()>max){
        max = n.getSimulationsWon();
        bestChild = n;
      }
    }
    return bestChild;
  }


  public Session createDeterminization(Stack<Card>[] myExp, Stack<Card>[] oppExp, Stack<Card>[] discardPile){
    /*Session determinization;
    ArrayList<Card> observedCards = getObservedCards();
    ArrayList<Card> remainingCards = Card.createCards();
    remainingCards.removeAll(observedCards);
    Stack<Card> newDrawStack = CardDecks.cardArrayToStack(Card.shuffleCards(remainingCards));
    Card[] oppHand = new Card[8];
    for(int i = 0;i<8;i++) {
      oppHand[i] = newDrawStack.pop();
    }
    if(isPlayer1) {
      CardDecks cards = new CardDecks(newDrawStack,discardPile,hand,oppHand,expeditions,oppExpedition);
      determinization = new Session(this,new RandomAIPlayer(false),cards,true);
    } else {
      CardDecks cards = new CardDecks(newDrawStack,discardPile,oppHand,hand,oppExpedition,expeditions);
      determinization = new Session(new RandomAIPlayer(true),this,cards,false);
    }
    return determinization;*/
    return null;
  }

  public ArrayList<Card> getObservedCards(){
    /*ArrayList<Card> observedCards = new ArrayList<>();
    for(Card c : discardPile.getYellow()){
      observedCards.add(c);
    }
    for(Card c : discardPile.getBlue()){
      observedCards.add(c);
    }
    for(Card c : discardPile.getWhite()){
      observedCards.add(c);
    }
    for(Card c : discardPile.getGreen()){
      observedCards.add(c);
    }
    for(Card c : discardPile.getRed()){
      observedCards.add(c);
    }
    for(Card c : hand){
      observedCards.add(c);
    }
    for (int i = 0;i<5;i++) {
      for (Card c : expeditions[i].getExpedition()) {
        observedCards.add(c);
      }
    }
    return observedCards;*/
    return null;
  }

  @Override
  public Move makeMove(Session session) {
    return null;
  }
}
