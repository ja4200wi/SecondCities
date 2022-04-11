package player;

import domain.players.AbstractPlayer;
import game.Card;
import game.Move;
import game.Session;
import java.util.Stack;

public abstract class Player {

  protected Card[] hand;
  boolean isCheating = false;

  public Player(){
    super();
  }

  public Player(Card[] given){
    hand = given;
  }

  public Player(Player player){
    hand = player.hand.clone();
    isCheating = player.isCheating;
  }

  /**
   * Method called to place players card onto game board.
   * @param index of the card to play
   * @return card to place
   */
  public Card placeCard(int index){
    return hand[index];
  }

  /**
   * Method accepts card drawn and places it onto players hand, where needed.
   * @param card that has been drawn
   * @param index of the empty spot on hand
   */
  public void drawCard(Card card, int index){
    hand[index] = card;
  }

  /**
   * This method will be called to prompt the player to make a move.
   * Specific implementation may vary.
   * @return the move the player choses
   */
  public abstract Move makeMove(Stack<Card>[] myExp,Stack<Card>[] oppExp,Stack<Card>[] discardPile);

  /**
   * This method allows to give the players their starting hand.
   * @param startingHand is the first 8 cards dealt
   */
  public void initHand(Card[] startingHand){
    hand = startingHand;
  }

  /**
   * This method returns the top card of any stack containing cards.
   * @param stackOfCards is the stack of cards to peek from
   * @return the top card of the stack without removing it from stack
   */
  public static Card getTopCard(Stack<Card> stackOfCards){
    if(stackOfCards.empty()) return null;
    return stackOfCards.peek();
  }

  /**
   * Determines whether game.Card can be placed on the Expedition.
   * @param stack is the expedition to place on
   * @param card is the card to place
   * @param color is the color of the expedition
   * @return true if valid move
   */
  public static boolean addCardPossible(Stack<Card> stack, Card card,int color){
    if(card.getColor()!=color) {
      return false;
    } else {
      if(card.isCoinCard()){
        if(stack.size()==0 || stack.peek().isCoinCard()) {
          return true;
        }
      } else if(!card.isCoinCard()){
        if(stack.size()!=0) {
          int newValue = card.getValue();
          int oldValue = stack.peek().getValue();
          if(newValue>oldValue || stack.peek().isCoinCard()) {
            return true;
          } else {
            return false;
          }
        } else if (stack.size()==0) {
          return true;
        }
      }
    }
    return false;
  }

  /** This method prints hand of the player */
  public void printHand(){
    StringBuilder sb = new StringBuilder("My cards: ");
    for(Card c: hand){
      sb.append(c);
      sb.append(", ");
    }
    System.out.println(sb);
  }

  public static void printGameBoard(Stack<Card>[] myExp,Stack<Card>[] oppExp,Stack<Card>[] discardPile){
    StringBuilder sb = new StringBuilder("GameBoard:\nOpponent Expeditions:");
    int count = 0;
    String[] colors ={"Y->","B->","W->","G->","R->"};
    for(Stack<Card> c : oppExp){
      sb.append(colors[count++]);
      sb.append(c);
      sb.append("\t");
    }
    for(Stack<Card> c: discardPile){
      if(!c.isEmpty() && c.peek()!=null) sb.append(c.peek());
    }
    count = 0;
    for(Stack<Card> c : myExp){
      sb.append(colors[count++]);
      sb.append(c);
      sb.append("\t");
    }
  }

  /** Get methods*/
  public Card[] getHand() {
    return hand;
  }

  public boolean isCheating() {
    return isCheating;
  }

  /** Set methods*/
  private void setHand(Card[] hand) {
    this.hand = hand;
  }

  public abstract Move makeMove(Session session);
}
