package game;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import player.MemoryPlayer;
import player.Player;
import player.RandomPlayer;

/**
 * @author Jann Winter
 * This class represents a game of Lost Cities. A game consists of two players,
 * the cards used in the game, a boolean indicating who's turn it is,
 * and a counter of turns made.
 */

public class Session {

  private Player[] players; // players[0] is Player Nr.1; players[1] is Player Nr.2
  private Card[][] playercards; // 2D array holding both players' cards
  private Stack<Card> drawStack; // stack holding all cards in the draw stack
  private Stack<Card>[] discardPile; // array holding the discard piles, indices of array according to int values of the color
  private Stack<Card>[][] expeditions; // 2D array holding both players' expeditions accessed with colors
  private boolean turn;
  private int turnCounter = 0;

  /**
   * This constructor
   * @param p1
   * @param p2
   */
  public Session(Player p1,Player p2){
    initDrawStack();
    discardPile = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    expeditions = new Stack[][]{{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()},
        {new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()}};
    initPlayers(p1,p2);
    playercards = new Card[2][8];
    initPlayerCards();
    turn = true;
  }

  /**
   * Constructor responsible for creation of determinizations
   * @param playerHands used to initalize placers' cards
   * @param drawStack as the remaining draw stack
   * @param discardPile with the discarded cards
   */
  public Session(Card[][] playerHands,Stack<Card> drawStack,Stack<Card>[] discardPile,Stack<Card>[][] expeditions,boolean turn){
    players = new Player[]{new RandomPlayer(),new RandomPlayer()};
    playercards = playerHands;
    this.drawStack = drawStack;
    this.discardPile = discardPile;
    this.expeditions = expeditions;
    this.turn = turn;
  }

  /**
   * This constructor is used for deep copying.
   * @param copy is the Session to copy
   */
  public Session(Session copy){
    players = new Player[]{new RandomPlayer(copy.players[0]),new RandomPlayer(copy.players[1])};
    playercards = new Card[2][5];
    for(int i = 0;i<8;i++){
      playercards[0][i] = copy.playercards[0][i].clone();
      playercards[1][i] = copy.playercards[1][i].clone();
    }
    drawStack = (Stack<Card>) copy.drawStack.clone();
    discardPile = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    expeditions = new Stack[][]{{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()},
        {new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()}};
    for(int i = 0;i<5;i++){
      discardPile[i] = (Stack<Card>) copy.discardPile[i].clone();
      expeditions[0][i] = (Stack<Card>)copy.expeditions[0][i].clone();
      expeditions[1][i] = (Stack<Card>)copy.expeditions[1][i].clone();
    }
    turn = copy.isTurn();
  }

  /**
   * This method starts a game of Lost Cities and calls the players makeMove function to receive
   * their answers until the game is over.
   * @return array holding the players' scores, turncounter, counter for plays on exp,
   * number of draws from discard pile, and both players number of expeditions started.
   */
  public int[] playGame(){
    int countOnExp = 0;
    int drawFromDiscardCount = 0;
    while(!this.isOver()){
      int atTurn = (turn)?0:1;
      int notAtTurn = (turn)?1:0;
      Move made;
      if(players[atTurn].isCheating()){
        made = players[atTurn].makeMove(this);
      } else {
        made = players[atTurn].makeMove(playercards[atTurn],expeditions[atTurn], expeditions[notAtTurn], discardPile,turn,turnCounter);
      }
      if(made.onExp) countOnExp++;
      if(made.drawFrom>0) drawFromDiscardCount++;
      executeMove(made);
    }
    int[] gameEndInformation = new int[7];
    int[] scores = calcPoints();
    gameEndInformation[0] = scores[0];
    gameEndInformation[1] = scores[1];
    gameEndInformation[2] = turnCounter;
    gameEndInformation[3] = countOnExp;
    gameEndInformation[4] = drawFromDiscardCount;
    gameEndInformation[5] = countExpsStarted(true);
    gameEndInformation[6] = countExpsStarted(false);
    return gameEndInformation;
  }

  /**
   * Executes as the playGame method but with additional print commands for each turn.
   * @return similar game statistics
   */
  public int[] playGameWithPrints(){
    int countOnExp = 0;
    while(!this.isOver()){
      int atTurn = (turn)?0:1;
      int notAtTurn = (turn)?1:0;
      Move made;
      if(players[atTurn].isCheating()){
        made = players[atTurn].makeMove(this);
      } else {
        made = players[atTurn].makeMove(playercards[atTurn],expeditions[atTurn], expeditions[notAtTurn], discardPile,turn,turnCounter);
        String onto = (made.onExp)?"Expedition":"Discard Pile";
        String drawFrom ="";
        switch (made.getDrawFrom()){
          case 0: drawFrom="DrawStack";
          break;
          case 1: drawFrom="Yellow Discard Pile";
            break;
          case 2: drawFrom="Blue Discard Pile";
            break;
          case 3: drawFrom="White Discard Pile";
            break;
          case 4: drawFrom="Green Discard Pile";
            break;
          case 5: drawFrom="Red Discard Pile";
            break;
        }
        Card drawn = null;
        if(made.getDrawFrom()!=0){
          drawn = discardPile[made.getDrawFrom()-1].peek().clone();
        }
        System.out.println("\nPlay " + getHandAtTurn()[made.cardIndex] + " onto " + onto + " and draw from " + drawFrom + " " + drawn);
        printHand(getHandAtTurn());
        Player.printGameBoard(expeditions[0],expeditions[1],discardPile,calcPoints());
        printDivider();
      }
      if(made.onExp) countOnExp++;
      executeMove(made);
    }
    int[] gameEndInformation = new int[4];
    int[] scores = calcPoints();
    gameEndInformation[0] = scores[0];
    gameEndInformation[1] = scores[1];
    gameEndInformation[2] = turnCounter;
    gameEndInformation[3] = countOnExp;
    return gameEndInformation;
  }

  /**
   * This method is called from a GUI and alters the way moves are collected from players in the
   * way that the program waits until the GUI calls the method again. Basically, a recursive method.
   */
  public void playGameWithGUI(){
    if(!this.isOver()){
      int atTurn = (turn)?0:1;
      int notAtTurn = (turn)?1:0;
      if(players[atTurn].isCheating()){
        //GUI Interaction
      } else {
        executeMove(players[atTurn].makeMove(playercards[atTurn],expeditions[atTurn], expeditions[notAtTurn], discardPile,turn,turnCounter));
        playGameWithGUI();
      }
    } else {
      System.out.println("Game Over\nScore Player 1 -> " + this.calcPointsPlayer(0) +
          "\nScore Player 2 -> " + this.calcPointsPlayer(1));
    }
  }

  /**
   * This method is the simplest form of game flow execution used for fast simulations during the MCTS loop.
   * @return the scores
   */
  public int[] simGame(){
    while(!this.isOver() && turnCounter<150){
      int atTurn = (turn)?0:1;
      int notAtTurn = (turn)?1:0;
      Move made = players[atTurn].makeMove(playercards[atTurn],expeditions[atTurn],expeditions[notAtTurn],discardPile,turn,turnCounter);
      executeMove(made);
    }
    return calcPoints();
  }
  /**
   * This method turns move into change in game and indicates success.
   * @param move is the move proposed by the player
   * @return whether the move was legal
   */
  public boolean executeMove(Move move){
    turnCounter++;
    int player = (turn)?0:1;
    int cardIndex = move.getCardIndex();
    boolean onExp = move.isOnExp();
    int drawFrom = move.getDrawFrom();
    if(onExp) {
      putOnExpedition(cardIndex,player);
    } else {
      putOnDiscardPile(cardIndex,player);
    }
    if(drawFrom==0){
      drawFromStack(cardIndex,player);
    } else {
      drawFromDiscardPile(drawFrom,cardIndex,player);
    }
    switchTurn();
    return true;//subject to change @TODO
  }

  private boolean putOnExpedition(int indexOfCard,int player){
    Card card = playercards[player][indexOfCard];
    expeditions[player][card.getColor()].add(card);
    return true;//subject to change @TODO
  }

  private boolean putOnDiscardPile(int indexOfCard,int player){
    Card card = playercards[player][indexOfCard];
    discardPile[card.getColor()].add(card);
    return true;//subject to change @TODO
  }

  private boolean drawFromStack(int index,int player){
    Card card = drawStack.pop();
    playercards[player][index] = card;
    return true;//subject to change @TODO
  }

  private boolean drawFromDiscardPile(int drawFrom,int index,int player){
    Card card = discardPile[drawFrom-1].pop();
    playercards[player][index] = card;
    return true;//subject to change @TODO
  }

  /**
   * This method is called to calculate the player's scores.
   */
  public int[] calcPoints(){
    int scoreForPlayer1 = calcPointsPlayer(0);
    int scoreForPlayer2 = calcPointsPlayer(1);
    return new int[]{scoreForPlayer1,scoreForPlayer2};
  }

  /**
   * Method calculates points a player achieved
   * @param player is the number of player in 0 for first and 1 for second
   * @return points player made
   */
  public int calcPointsPlayer(int player){
    int totalScore = 0;
    for(Stack<Card> exp : expeditions[player]){
      totalScore += calcPointsExpedition(exp);
    }
    return totalScore;
  }

  /**
   * Calculates the worth of the expedition
   * @return Points the expedition is worth
   */
  public int calcPointsExpedition(Stack<Card> expedition){
    int result = 0;
    int coincardCount = 1;
    if(expedition.size()!=0) {
      result -= 20;
    } else return 0;
    for(Card c : expedition){
      if(c.isCoinCard()) {
        coincardCount++;
      } else {
        result += c.getValue();
      }
    }
    result *= coincardCount;
    if(expedition.size()>7){
      result += 20;
    }
    return result;
  }

  /**
   * This method counts the number of non-empty expedtions of a player.
   * @param p1 is used to select the player where true is player 1 and
   * @return
   */
  public int countExpsStarted(boolean p1){
    int numberExpStarted = 0;
    if(p1) for(int i = 0;i<5;i++) if(expeditions[0][i].size()>0) numberExpStarted++;
    if(!p1) for(int i = 0;i<5;i++) if(expeditions[1][i].size()>0) numberExpStarted++;
    return numberExpStarted;
  }

  /**
   * This method is used to print an array of cards
   * @param myHand is the array of cards to print
   */
  public void printHand(Card[] myHand){
    StringBuilder sb = new StringBuilder("My cards: ");
    int counter = 1;
    for(Card c: myHand){
      sb.append("\tCard " + counter++);
      sb.append(c);
      sb.append(", \t");
    }
    System.out.println(sb);
  }

  private void printDivider(){
    System.out.println("__________________________________________________________________________");
  }

  /**
   * This method sets the player array and deals starting cards to players.
   * @param p1 is the first player
   * @param p2 is the second player
   */
  private void initPlayers(Player p1,Player p2){
    players = new Player[2];
    p1.setImP1(true);
    p2.setImP1(false);
    if(p1.hasMemory()) {
      MemoryPlayer p1IS = (MemoryPlayer) p1;
      p1IS.resetMemory();
      players[0] = p1IS;
    } else {
      players[0] = p1;
    }
    if(p2.hasMemory()) {
      MemoryPlayer p2IS = (MemoryPlayer) p2;
      p2IS.resetMemory();
      players[1] = p2IS;
    } else {
      players[1] = p2;
    }
  }

  /**
   * This method creates 60 cards, shuffles them and turns it into a stack for this session.
   */
  private void initDrawStack(){
    Card[] wholeSetOfCards = new Card[60];
    int count = 0;
    for(int i = 0;i<3;i++){
      wholeSetOfCards[count++] = new Card(0,i-1);
      wholeSetOfCards[count++] = new Card(1,i-1);
      wholeSetOfCards[count++] = new Card(2,i-1);
      wholeSetOfCards[count++] = new Card(3,i-1);
      wholeSetOfCards[count++] = new Card(4,i-1);
    }
    for(int i = 2;i<11;i++){
      wholeSetOfCards[count++] = new Card(0,i);
      wholeSetOfCards[count++] = new Card(1,i);
      wholeSetOfCards[count++] = new Card(2,i);
      wholeSetOfCards[count++] = new Card(3,i);
      wholeSetOfCards[count++] = new Card(4,i);
    }
    shuffleArray(wholeSetOfCards);
    drawStack = cardArrayToStack(wholeSetOfCards);
  }

  /**
   * Fill players' cards with cards.
   */
  private void initPlayerCards() {
    for(int i = 0;i<8;i++){
      playercards[0][i] = drawStack.pop();
      playercards[1][i] = drawStack.pop();
    }
  }

  /**
   * This method takes an array of Cards and shuffles the order.
   * It implements the Fisher-Yates shuffle
   * @param cardArray Cards, which should be shuffled.
   * @return Returns array wit the same Cards, but in different order.
   */
  private static void shuffleArray(Card[] cardArray) {
    Random rnd = ThreadLocalRandom.current();
    for (int i = cardArray.length - 1; i > 0; i--) {
      int index = rnd.nextInt(i + 1);
      Card c = cardArray[index];
      cardArray[index] = cardArray[i];
      cardArray[i] = c;
    }
  }

  /**
   * This method turn an array of cards into a stack of cards.
   * @param cards Array of cards.
   * @return Stack with cards of the given array.
   */
  private static Stack<Card> cardArrayToStack(Card[] cards){
    Stack<Card> result = new Stack<Card>();
    for(Card c : cards) {
      result.push(c);
    }
    return result;
  }

  /**
   * Print the top cards of an array of stacks
   * @param stacks to print the top cards of
   * @return the string representation
   */
  private String topOfStacksToString(Stack<Card>[] stacks){
    StringBuilder sb = new StringBuilder("{");
    for(Stack<Card> color : stacks){
      sb.append(Player.getTopCard(color));
      sb.append(", ");
    }
    sb.append("\b}");
    return String.valueOf(sb);
  }

  /**
   * This method prints all cards of mutiple stacks
   * @param stacks is the array of stacks of cards
   * @return a string holding all cards
   */
  private String stacksToString(Stack<Card>[] stacks){
    StringBuilder sb = new StringBuilder("{");
    for(Stack<Card> color : stacks){
      for(Card c : color){
        sb.append(c);
        sb.append(", ");
      }
      sb.append("\t");
    }
    sb.append("\b\b}");
    return String.valueOf(sb);
  }

  /**
   * Method uses turn to return current player's cards
   * @return card array
   */
  public Card[] getHandAtTurn(){ return (turn)?playercards[0]:playercards[1]; }

  /**
   * Method uses turn to return current player's expeditions
   * @return array of stack of cards representing the expeditions
   */
  public Stack<Card>[] getExpAtTurn(){ return (turn)?expeditions[0]:expeditions[1]; }

  public void switchTurn(){
    turn = !turn;
  }

  /**
   * This method tells whether the game is finished based on th size of the drawstack and a maximum number of turns.
   * @return true if game is over
   */
  public boolean isOver(){
    if(drawStack.isEmpty()) return true;
    if(turnCounter>100) return true;
    return false;
  }

  public boolean isTurn(){
    return turn;
  }

  public void setPlayer(Player p1,Player p2){
    players[0] = p1;
    players[1] = p2;
  }

  /**
   * Method gives cards of a player.
   * @param player indicating player of interest -> true = player 1
   * @return cards of player
   */
  public Card[] getPlayerHand(boolean player){
    return (player)?playercards[0]:playercards[1];
  }

  public Stack<Card>[] getPlayerExpeditions(boolean player){
    return (player)?expeditions[0]:expeditions[1];
  }

  public int getNumberCardsLeft(){
    return drawStack.size();
  }

  public Stack<Card>[] getDiscardPile() {
    return discardPile;
  }

  public Stack<Card>[][] getExpeditions() {
    return expeditions;
  }


  @Override
  public String toString() {
    int[] scores = calcPoints();
    return "Session{" +
        "\tCards left: " + drawStack.size() +
        "\nExpeditions Player 1 " + stacksToString(expeditions[0]) +
        "\nDiscardPile " + topOfStacksToString(discardPile) +
        "\nExpeditions Player 2 " + stacksToString(expeditions[1]) +
        "\nScore Player 1: " + scores[0] +
        "\tScore Player 2. " + scores[1];
  }

  public int[] getPossibleDrawsInt(){
    ArrayList<Integer> possibleDraws = new ArrayList<>();
    if(!drawStack.isEmpty())possibleDraws.add(0);
    if (!discardPile[0].isEmpty()) {
      possibleDraws.add(1);
    }
    if (!discardPile[1].isEmpty()) {
      possibleDraws.add(2);
    }
    if (!discardPile[2].isEmpty()) {
      possibleDraws.add(3);
    }
    if (!discardPile[3].isEmpty()) {
      possibleDraws.add(4);
    }
    if (!discardPile[4].isEmpty()) {
      possibleDraws.add(5);
    }
    return possibleDraws.stream().mapToInt(Integer::intValue).toArray();
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

  public void setTurnCounter(int turnCounter) {
    this.turnCounter = turnCounter;
  }
}
