package player;

import game.Card;
import game.Move;
import game.Session;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Jann Winter
 * This class represents a rule-based agent
 */

public class LikeHumanPlayer extends MemoryPlayer{

  private boolean[] playColor = {false, false, false, false, false};
  private boolean[] oppPlayColor = {false, false, false, false, false};


  private ArrayList<Card> cardsOfOpp = new ArrayList<>();
  private Stack<Card>[] oldDiscardPile = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};

  public LikeHumanPlayer(){
    super();
    this.hasMemory = true;
    playColor = new boolean[]{false, false, false, false, false};
  }

  @Override
  public void resetMemory(){
    playColor = new boolean[]{false, false, false, false, false};
    }

  @Override
  public Move makeMove(Card[] myHand, Stack<Card>[] myExp, Stack<Card>[] oppExp,
      Stack<Card>[] discardPile, boolean turn, int turnCounter) {
    int playIndex = -1;
    boolean onExp = true;
    int drawFrom = 0;
    int remainingCards = calcRemainingCards(myExp, oppExp, discardPile);
    //int[] topValuesOnExp = getValuesOfTopCards(myExp); //GET VALUES OF TOP CARDS FOR EXPEDITIONS
    decideColorsToPlay(myHand, myExp, oppExp, discardPile); //CHECK IF I CAN START EXPEDITIONS
    //checkColorsOppPlays(oppExp);//CHECK EXPS OPP PLAYS
    //int[] lowestOfColor = getLowestCards(myHand); //DETERMINE INDEX OF LOWEST CARD OF EACH COLOR
    // EXP PLAYS
      int[] colorsPlayable = getColorsPlayable();
      playIndex = findLowestPlayableCard(myHand, colorsPlayable, myExp);
    //DISC PLAYS
    if (playIndex==-1){
      onExp = false;
      playIndex = findLowestCardNoCC(myHand); //PLACE LOWEST OF ALL CARDS TODO: COINCARDS & OPP CARDS -> USE LOWESTOFCOLOR
    }
    drawFrom = drawUsefulCard(myExp,discardPile);
    if(drawFrom-1==myHand[playIndex].getColor()) drawFrom = 0;
    return new Move(playIndex,onExp,drawFrom);
  }

  /**
   * This method determines the number of remaining cards on the draw stack
   * @param myExp
   * @param oppExp
   * @param discardPile
   * @return
   */
  public static int calcRemainingCards(Stack<Card>[] myExp,Stack<Card>[] oppExp,Stack<Card>[] discardPile){
    int cardsOnBoard = 0;
    for(int i = 0;i<5;i++){
      cardsOnBoard  += myExp[i].size();
      cardsOnBoard  += oppExp[i].size();
      cardsOnBoard  += discardPile[i].size();
    }
    return 44-cardsOnBoard;
  }

  /**
   * This funciton can determine the maximum space allowed between two cards placed on expeditions
   * @param remainingCards
   * @return
   */
  public static int maxDistanceBetweenExpCards(int remainingCards){
    int yIntercept = 10;
    double slope = (double)0;
    double functionValue = yIntercept-slope*remainingCards;
    return (int) functionValue; //UNTERE GAUSSKLAMMER
  }

  /**
   * This method returns the lowest placeable card on the expeditions
   * @param hand
   * @param playableColors
   * @param myExp
   * @return
   */
  public int findLowestPlayableCard(Card[] hand,int[] playableColors,Stack<Card>[] myExp){
    int valueLowestPlayableCard = 100;
    int indexOfLowestPlayableCard = -1;
    for(int i = 0;i<8;i++){
      if(arrayContainsNumber(playableColors,hand[i].getColor()) && //IF COLOR OF CURRENT CARD INSIDE PLAYABLE COLORS
          addCardPossible(myExp[hand[i].getColor()],hand[i],hand[i].getColor()) && //IF CARD HIGHER THAN TOP VALUE ON EXP
          hand[i].getValue()<valueLowestPlayableCard) {            //IF CARD LOWER THAN PREVIOUS FOUND PLAYABLE CARD
        valueLowestPlayableCard = hand[i].getValue();
        indexOfLowestPlayableCard = i;
      }
    }
    return indexOfLowestPlayableCard;
  }

  /**
   * Get the cards on hand of a color
   * @param hand
   * @param color
   * @return
   */
  public ArrayList<Card> getCardsOfColor(Card[] hand,int color){
    ArrayList<Card> cardsOfColor = new ArrayList<>();
    for(Card c : hand){
      if(c.getColor()==color) cardsOfColor.add(c);
    }
    return cardsOfColor;
  }

  /**
   * Get the colors decided to play as expedition
   * @return
   */
  public int[] getColorsPlayable(){
    int countPlayableCards = 0;
    for(int i = 0;i<5;i++) if(playColor[i]) countPlayableCards++; //LOOP COUNTS HOW MANY EXPS I PLAY
    int[] colorsPlayable = new int[countPlayableCards];
    int counterForArray = 0;
    for(int i = 0;i<5;i++) if(playColor[i]) colorsPlayable[counterForArray++] = i; //FILL WITH COLORS I PLAY
    return colorsPlayable;
  }

  /**
   * Check if the discard pile holds a card i could place onto one the started expeditions
   * @param myExp
   * @param discardPile
   * @return
   */
  public int drawUsefulCard(Stack<Card>[] myExp,Stack<Card>[] discardPile){
    Card[] topDiscard = new Card[5];
    for(int i = 0;i<5;i++) {
      topDiscard[i] = getTopCard(discardPile[i]);
    }
    Card[] topExp = new Card[5];
    for(int i = 0;i<5;i++) {
      topExp[i] = getTopCard(myExp[i]);
    }
    for(int i = 0;i<5;i++){
      if(topExp[i]==null && topDiscard[i]!=null && playColor[i]) return i+1;
      if(topExp[i]!=null && topDiscard[i]!=null && (topExp[i].getValue()<topDiscard[i].getValue() || topExp[i].isCoinCard())) return i+1;
    }
    return 0;
  }

  /**
   * Check if expedition to play is available
   * @return
   */
  public boolean playColorAvailable(){
    for(int i=0;i<5;i++){
      if(playColor[i]) return true;
    }
    return false;
  }

  /**
   * Get the lowest cards of each color on hand
   * @param hand
   * @return
   */
  public static  int[] getLowestCards(Card[] hand){
    int[] indicesOfLowestCards = new int[]{-1,-1,-1,-1,-1};
    for(int i = 0;i<5;i++){
      indicesOfLowestCards[i] = findLowestOfColor(i,hand);
    }
    return indicesOfLowestCards;
  }

  /**
   * Set the colors the opponent plays
   * @param oppExp
   */
  public void checkColorsOppPlays(Stack<Card>[] oppExp){
    for(int i = 0;i<5;i++){
      if(!oppExp[i].isEmpty()) {
        oppPlayColor[i] = true;
      }
    }
  }

  /**
   * Get an int array holding the values of the top cards on stacks
   * @param myExp
   * @return
   */
  public static int[] getValuesOfTopCards(Stack<Card>[] myExp){
    int[] topValues = new int[]{0,0,0,0,0};
    for(int i = 0;i<5;i++){
      if(!myExp[i].isEmpty()){
        topValues[i] = myExp[i].peek().getValue();
      } else {
        topValues[i] = 1; //no card on Exp therefore all other cards placeable
      }
    }
    return topValues;
  }

  /**
   * Check if an array holds a number
   * @param array
   * @param number
   * @return
   */
  public static boolean arrayContainsNumber(int[] array,int number){
    for(int i = 0;i< array.length;i++){
      if(array[i]==number) return true;
    }
    return false;
  }

  /**
   * Check what expeditions can be started.
   * @param myHand
   * @param myExp
   * @param oppExp
   * @param discardPile
   */
  public void decideColorsToPlay(Card[] myHand,Stack<Card>[] myExp,Stack<Card>[] oppExp,Stack<Card>[] discardPile){
    double[] expectedPerColor = {0,0,0,0,0};
    int[] pointsOwned = {0,0,0,0,0};
    for(int i = 0;i<5;i++){
      pointsOwned[i] = getPointsOwned(myHand,myExp[i],i);
      int pointsObtainable = getPointsObtainable(pointsOwned[i],oppExp[i],i,discardPile[i]);
      //Assumption that remaining cards are drawn by player with same likelihood
      expectedPerColor[i] = pointsOwned[i] + (double) pointsObtainable/2;
    }
    for(int i = 0;i<5;i++) {
      if(expectedPerColor[i]>=32 && pointsOwned[i]>14) playColor[i] = true; //expectedPerColor[i]>=32 && pointsOwned[i]>14
    }
  }

  //TODO: Idee -> oberste discardPile hinzufügen

  /**
   * Determines points still obtainable, which is sum of all cards of a color minus the cards on expeditions and player's hand.
   * @param pointsOwned
   * @param oppExp
   * @param color
   * @param discardPileOfColor
   * @return
   */
  public int getPointsObtainable(int pointsOwned,Stack<Card> oppExp,int color,Stack<Card> discardPileOfColor){
    int maxPointsObtainable = 54; //2+3+4+5+6+7+8+9+10
    maxPointsObtainable -= pointsOwned;
    for(Card c : oppExp){
      if(c.getColor()==color && !c.isCoinCard()) maxPointsObtainable -= c.getValue();
    }
    return maxPointsObtainable;
  }

  /**
   * Calculate points owned of a color, which includes poiints on hand and expedition
   * @param myHand
   * @param myExp
   * @param color
   * @return
   */
  public int getPointsOwned(Card[] myHand,Stack<Card> myExp,int color){
    int pointsOwned = 0;
    for(Card c : myHand){
      if(c.getColor()==color && !c.isCoinCard()) pointsOwned += c.getValue();
    }
    for(Card c : myExp){
      if(c.getColor()==color && !c.isCoinCard()) pointsOwned += c.getValue();
    }
    return pointsOwned;
  }

  /**
   * Return index of lowest card on hand but is not coin card
   * @param myHand
   * @return
   */
  public static int findLowestCardNoCC(Card[] myHand){
    int indexOfCard = -1;
    for(int i = 0;i<8;i++){
      if(!myHand[i].isCoinCard()) {
        if (indexOfCard != -1) {
          if (myHand[i].getValue() < myHand[indexOfCard].getValue()) {
            indexOfCard = i;
          }
        } else {
          indexOfCard = i;
        }
      }
    }
    if(indexOfCard==-1) {
      return 0;
    }
    return indexOfCard;
  }

  /**
   * Returns index of lowest Card of given color
   * @param color
   * @param myHand
   * @return
   */
  public static int findLowestOfColor(int color,Card[] myHand){
    int indexOfCard = -1;
    for(int i = 0;i<8;i++){
      if(myHand[i].getColor()==color) {
        if(indexOfCard != -1) {
          if(myHand[i].getValue()<myHand[indexOfCard].getValue()){
            indexOfCard = i;
          }
        } else {
          indexOfCard = i;
        }
      }
    }
    return indexOfCard;
  }

  /**
   * Return index of lowest card player wants to discard
   * @param lowestOfColor
   * @param hand
   * @return
   */
  public int findLowestDiscardableCard(int[] lowestOfColor,Card[] hand){
    int valueLowestCardDiscarable = 100;
    int indexOfLowestCardDiscardable = -1;
    for(int i = 0;i<5;i++){
      if(!playColor[i] && lowestOfColor[i]!=-1 &&  hand[lowestOfColor[i]].getValue()<valueLowestCardDiscarable) {
        valueLowestCardDiscarable = hand[lowestOfColor[i]].getValue();
        indexOfLowestCardDiscardable = lowestOfColor[i];
      }
    }
    return indexOfLowestCardDiscardable;
  }

  /**
   * Check if a card selected for placement leaves to much space between previous card on expedition
   * @param card
   * @param topValuesOnExp
   * @param maxDistanceAllowed
   * @return
   */
  public static boolean checkDistanceTopExpAndNew(Card card,int[] topValuesOnExp,int maxDistanceAllowed){
    int color = card.getColor();
    if(card.getValue()<=topValuesOnExp[color]+maxDistanceAllowed) return true;
    return false;
  }

  /**
   * Discard cards no one can use anymore
   * @param myHand
   * @param myExp
   * @param oppExp
   * @param discardPile
   * @param remainingCards
   * @return
   */
  public int dropCardNoOneNeeds(Card[] myHand,Stack<Card>[] myExp,Stack<Card>[] oppExp,Stack<Card>[] discardPile,int remainingCards){
    int cardColor;
    int cardValue;
    int topExpCardValue;
    int topExpCardOppValue;
    //if(remainingCards<30) return -1;
    for(int i = 0;i<8;i++) {
      cardColor = myHand[i].getColor();
      cardValue = myHand[i].getValue();
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

  @Override
  public Move makeMove(Session session) {
    return null;
  }
}
