package player;

import game.Card;
import game.Move;
import game.Session;
import java.util.ArrayList;
import java.util.Stack;

public class RuleBasedPlayer extends Player {

  /**
   * playColor tells for each color if agents pursues to play this expedition
   * {YELLOW,BLUE,WHITE,GREEN,RED}
   */
  private boolean[] playColor = {false, false, false, false, false};

  public RuleBasedPlayer(){}

  public RuleBasedPlayer(Card[] given){super(given);}


  @Override
  public Move makeMove(Stack<Card>[] myExp, Stack<Card>[] oppExp, Stack<Card>[] discardPile) {
    int index = -1;
    boolean onExp;
    int drawFrom = -1;
    /**Placing*/
    index = obviousPlacement(myExp);
    onExp = true;
    if (index == -1) {
      index = startExpedition(myExp);
      onExp = true;
      //if(index!=-1) System.out.println("Start Expedition Rule");
    }
    /*if (index == -1) {
      index = dropCardNoOneNeeds(myExp,oppExp,discardPile);
      onExp = false;
      //if(index!=-1) System.out.println("Drop game.Card No One Needs Rule");
    }*/
    if (index == -1) {
      int[] indicesCardsOpponentWants = dontGiveOpponentCardNeeded(oppExp);
      boolean placed = false;
      int maxTry = 16;
      while (!placed && maxTry > 0) {
        index = (int) (Math.random() * 8);
        Card c = hand[index];
        int color = c.getColor();
        if (Player.addCardPossible(myExp[color],c,color)) {
          index = makeSureLowestCardPlayed(index,myExp);
          //System.out.println("Random game.Card on Expedition");
          onExp = true;
          placed = true;
        } else {
          if (!isInArray(index, indicesCardsOpponentWants)) {
            onExp = false;
            placed = true;
          } else {
            //System.out.println("Don't Discard Rule");
            maxTry--;
          }
        }
      }
    }
    /**Drawing*/
    drawFrom = drawUsefulCard(myExp,discardPile);
    //if(drawFrom!=-1) System.out.println("Draw Useful game.Card Rule");
    if(drawFrom==-1) drawFrom = 0;
    return new Move(index,onExp,drawFrom);
  }

  @Override
  public Move makeMove(Session session) {
    return null;
  }

  public int startExpedition(Stack<Card>[] myExp){
    decideColorsToPlay();
    for(int i = 0;i<5;i++){
      if(playColor[i]){
        if(Player.getTopCard(myExp[i])==null) return findLowestOfColor(i);
      }
    }
    return -1;
  }

  public void decideColorsToPlay(){
    int[] pointsPerColor = {0,0,0,0,0};
    for(Card c : hand){
      if(!c.isCoinCard()) pointsPerColor[c.getColor()] += c.getValue();
    }
    for(int i = 0;i<5;i++) {
      if(pointsPerColor[i]>=20) playColor[i] = true;
    }
  }

  public int dropCardNoOneNeeds(Stack<Card>[] myExp,Stack<Card>[] oppExp,Stack<Card>[] discardPile){
    int cardColor;
    int cardValue;
    int topExpCardValue;
    int topExpCardOppValue;
    if(getNumberOfCardsLeft(myExp,oppExp,discardPile)<30) return -1;
    for(int i = 0;i<8;i++) {
      cardColor = hand[i].getColor();
      cardValue = hand[i].getValue();
      if (Player.getTopCard(myExp[cardColor]) != null
          && Player.getTopCard(oppExp[cardColor]) != null) {
        topExpCardValue = Player.getTopCard(myExp[cardColor]).getValue();
        topExpCardOppValue = Player.getTopCard(myExp[cardColor]).getValue();
        if (cardValue < topExpCardValue && cardValue < topExpCardOppValue
            && topExpCardValue<11 && topExpCardOppValue<11)
          return i;
      }
    }
    return -1;
  }

  public int findLowestOfColor(int color){
    int indexOfCard = -1;
    for(int i = 0;i<8;i++){
      if(hand[i].getColor()==color) {
        if(indexOfCard != -1) {
          if(hand[i].getValue()<hand[indexOfCard].getValue() || hand[i].isCoinCard()){
            indexOfCard = i;
          }
        } else {
          indexOfCard = i;
        }
      }
    }
    return indexOfCard;
  }

  public int obviousPlacement(Stack<Card>[] myExp){
    Card[] topExpCards = new Card[5];
    int count = 0;
    for(Stack<Card> exp : myExp){
      topExpCards[count++] = Player.getTopCard(exp);
    }
    for(int i = 0;i<8;i++){
      if(topExpCards[hand[i].getColor()]!=null) {
        if (hand[i].getValue() == topExpCards[hand[i].getColor()].getValue() + 1) {
          return i;
        }
      }
    }
    return -1;
  }

  public int drawUsefulCard(Stack<Card>[] myExp,Stack<Card>[] discardPile){
    Card[] topDiscard = new Card[5];
    for(int i = 0;i<5;i++) {
      topDiscard[i] = Player.getTopCard(discardPile[i]);
    }
    Card[] topExp = new Card[5];
    for(int i = 0;i<5;i++) {
      topExp[i] = Player.getTopCard(myExp[i]);
    }
    if(topExp[0]!=null && topDiscard[0]!=null && topExp[0].getValue()<topDiscard[0].getValue() && !topDiscard[0].isCoinCard()) return 1;
    if(topExp[1]!=null && topDiscard[1]!=null && topExp[1].getValue()<topDiscard[1].getValue() && !topDiscard[1].isCoinCard()) return 2;
    if(topExp[2]!=null && topDiscard[2]!=null && topExp[2].getValue()<topDiscard[2].getValue() && !topDiscard[2].isCoinCard()) return 3;
    if(topExp[3]!=null && topDiscard[3]!=null && topExp[3].getValue()<topDiscard[3].getValue() && !topDiscard[3].isCoinCard()) return 4;
    if(topExp[4]!=null && topDiscard[4]!=null && topExp[4].getValue()<topDiscard[4].getValue() && !topDiscard[4].isCoinCard()) return 5;
    return -1;
  }

  public int getNumberOfCardsLeft(Stack<Card>[] myExp,Stack<Card>[] oppExp,Stack<Card>[]discardPile){
    int totalCards = 54;
    for(Stack<Card> e : myExp) totalCards -= e.size();
    for(Stack<Card> e : oppExp) totalCards -= e.size();
    for(Stack<Card> d : discardPile) totalCards -= d.size();
    return  totalCards;
  }

  public int[] dontGiveOpponentCardNeeded(Stack<Card>[] oppExp){
    ArrayList<Integer> indices = new ArrayList<>();
    for(int i = 0;i<8;i++){
      Card c = Player.getTopCard(oppExp[hand[i].getColor()]);
      if(c==null) indices.add(i);
      if(c!=null && hand[i].getValue()>c.getValue()) indices.add(i);
    }
    return indices.stream().mapToInt(Integer::intValue).toArray();
  }

  public static boolean isInArray(int number,int[] array){
    for(int i : array){
      if(number==i) return true;
    }
    return false;
  }

  public int makeSureLowestCardPlayed(int index,Stack<Card>[] myExp){
    Card chosen = hand[index];
    if(chosen.isCoinCard()) return index;
    for(int i = 0;i<8;i++){
      Card current = hand[i];
      if(!current.isCoinCard()) {
        if (current.getColor() == chosen.getColor() && current.getValue()< chosen.getValue()
            && Player.addCardPossible(myExp[chosen.getColor()],current, chosen.getColor())) {
          index = i;
          chosen = hand[index];
        }
      }
    }
    return index;
  }
}