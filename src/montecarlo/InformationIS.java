package montecarlo;

import game.Card;
import game.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * @author Jann Winter
 * This class represents an information set. It holds all information a player can observe during a game.
 */

public class InformationIS {

  ArrayList<Card> observedCards;
  Card[] myHand;
  Stack<Card>[] myExp;
  Stack<Card>[] oppExp;
  Stack<Card>[] discardPile;
  boolean turn;
  boolean imP1;
  int turnCounter;
  ArrayList<Card> oppCardsObserved;//@TODO

  /**
   * This constructor is called to create an instance with all attributes
   * @param hand is the player's cards
   * @param myExp is the player's expeditions
   * @param oppExp is the opponent's expeditions
   * @param discardPile is the discard pile
   * @param turn is the boolean indicating who's turn it is
   * @param imP1 is a boolean telling if current player is Nr1 or Nr2
   * @param turnCounter is the counter of turns
   * @param oppCards holds cards known to be on opponent's hand
   */
  public InformationIS(Card[] hand,Stack<Card>[] myExp,Stack<Card>[] oppExp,Stack<Card>[] discardPile,
      boolean turn,boolean imP1,int turnCounter,ArrayList<Card> oppCards){
    myHand = clone(hand);
    this.myExp = clone(myExp);
    this.oppExp = clone(oppExp);
    this.discardPile = clone(discardPile);
    this.turn = turn;
    this.imP1 = imP1;
    observedCards = new ArrayList<>();
    for(Stack<Card> stack : myExp){ observedCards.addAll(stack); }
    for(Stack<Card> stack : oppExp){ observedCards.addAll(stack); }
    for(Stack<Card> stack : discardPile){ observedCards.addAll(stack); }
    for(int i = 0;i<8;i++){ observedCards.add(myHand[i]); }
    this.turnCounter = turnCounter;
    oppCardsObserved = oppCards;
  }

  /**
   * This method uses the information to create a conform determinization.
   * @return a valid session
   */
  public Session createDeterminization(){
    Session determinization;
    ArrayList<Card> observedCards = this.observedCards;
    observedCards.addAll(oppCardsObserved);
    ArrayList<Card> remainingCardsHS = createCardDeck(); //Set of game cards used to play -> as HashSet to increase speed of removeAll
    remainingCardsHS.removeAll(observedCards);
    remainingCardsHS.removeAll(oppCardsObserved);//Keep cards not observed yet
    Stack<Card> newDrawStack = new Stack<>();
    newDrawStack.addAll(remainingCardsHS);
    Collections.shuffle(newDrawStack);
    Card[] oppHand = new Card[8];
    for(int i = 0;i<oppCardsObserved.size() && i<8;i++){
      oppHand[i] = oppCardsObserved.get(i);
    }
    if(!newDrawStack.isEmpty()) {
      for (int i = oppCardsObserved.size(); i < 8; i++) {
        oppHand[i] = newDrawStack.pop();
      }
    }
    Card[][] hands;
    Stack<Card>[][] expeditions;
    if(imP1) {
      hands = new Card[][]{clone(myHand), clone(oppHand)};
      expeditions = new Stack[][]{clone(myExp), clone(oppExp)};
    } else {
      hands = new Card[][]{clone(oppHand), clone(myHand)};
      expeditions = new Stack[][]{clone(oppExp), clone(myExp)};
    }
    determinization = new Session(hands,newDrawStack,clone(discardPile),expeditions,turn);
    determinization.setTurnCounter(turnCounter);
    return determinization;
  }

  /**
   * This method creates the 60 game cards
   * @return
   */
  public static ArrayList<Card> createCardDeck(){
    ArrayList<Card> wholeSetOfCards = new ArrayList<>();
    for(int i = 0;i<3;i++){
      wholeSetOfCards.add(new Card(0,i-1));
      wholeSetOfCards.add(new Card(1,i-1));
      wholeSetOfCards.add(new Card(2,i-1));
      wholeSetOfCards.add(new Card(3,i-1));
      wholeSetOfCards.add(new Card(4,i-1));
    }
    for(int i = 2;i<11;i++){
      wholeSetOfCards.add(new Card(0,i));
      wholeSetOfCards.add(new Card(1,i));
      wholeSetOfCards.add(new Card(2,i));
      wholeSetOfCards.add(new Card(3,i));
      wholeSetOfCards.add(new Card(4,i));
    }
    return wholeSetOfCards;
  }

  public Card[] clone(Card[] cards){
    int size = cards.length;
    Card[] clone = new Card[size];
    for(int i = 0;i<size;i++){
      clone[i] = cards[i].clone();
    }
    return clone;
  }

  /**
   * This method is used to deep copy an array of stacks holding cards
   * @param stacks array to copy
   * @return deep copied array
   */
  public Stack<Card>[] clone(Stack<Card>[] stacks){
    int size = stacks.length;
    Stack<Card>[] clone = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    for(int i = 0;i<size;i++) {
      for (Card c : stacks[i]) {
        clone[i].add(c.clone());
      }
    }
    return clone;
  }

  /** Get methods **/
  public boolean isTurn() {
    return turn;
  }

  public boolean isImP1(){
    return imP1;
  }
}
