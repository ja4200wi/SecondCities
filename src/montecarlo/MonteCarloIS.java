package montecarlo;

import game.Card;
import game.Move;
import game.Session;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;
import javafx.util.Pair;
import player.LikeHumanPlayer;
import player.Player;
import player.RandomPlayer;
import player.RuleBasedPlayer;

/**
 * @author Jann Winter
 * This class performs the SO-MCTS.
 */

public class MonteCarloIS {

  /**
   * This method starts the MCTS loop
   * @param initialIS is the information set used
   * @param time sets the time constraint stopping the loop
   * @param playOutStyle indicates what agents to use for simulations, 0=QuickRandom,1=QuickRule,2=LikeHuman
   * @param explorationConstant sets the exploration constant used in the UCB formula
   * @param rewardStrategy indicates the reward strategy, 0=AWL,1=SD,2=RWL
   * @param reduceBranching indicates if branching factor should be reduced
   * @return the best move resulting form the search
   */

  public static Move ismcts(InformationIS initialIS,int time,int playOutStyle,double explorationConstant,int rewardStrategy,boolean reduceBranching){
    int counterIterations = 0;
    long start = System.currentTimeMillis();
    NodeIS root = new NodeIS();
    Pair<NodeIS, Session> nodeAndDet = null;
    int player = (initialIS.isImP1())?1:2;
    while(System.currentTimeMillis()-start<time){//System.currentTimeMillis()-start<time
      Session determinization= initialIS.createDeterminization();
      nodeAndDet = select(root,determinization,explorationConstant);
      if(movesFromDetNotInNodeChildren(nodeAndDet.getKey(),determinization).size()!=0) {
        nodeAndDet = expand(nodeAndDet.getKey(),determinization);
      }
      int reward = simulate(nodeAndDet.getValue(),playOutStyle,rewardStrategy,player);
      backPropagate(reward,nodeAndDet.getKey());
      counterIterations++;
    }
    //System.out.println("Iterations made " + counterIterations);
    NodeIS bestChild = root.getBestChild();
    return bestChild.getIncomingAction(); //TODO: return action with highest visit count
  }

  public static Pair<NodeIS, Session> select(NodeIS node,Session determinization,double explorationConstant){
    while(!determinization.isOver() && movesFromDetNotInNodeChildren(node,determinization).size()==0){
      node = UCB(node,determinization,explorationConstant);
      determinization.executeMove(node.incomingAction);//TODO: probably should be a deep copy
    }
    return new Pair<>(node,determinization);
  }

  /**
   * Method to get best child available with given determinization.
   * @param node Current node in tree
   * @param determinization is the current determinization
   * @param explorationConstant constant
   * @return child with highest UCT value
   */
  public static NodeIS UCB(NodeIS node,Session determinization,double explorationConstant){
    double max = Double.NEGATIVE_INFINITY;
    for(NodeIS child : node.getChildrenConformWith(determinization)){//only children conform with determinization
      double ucbValue = (double) child.totalReward/child.visitCount +
          explorationConstant*Math.sqrt(Math.log(child.availabilityCount)/child.visitCount);
      if(ucbValue>max){
        max = ucbValue;
        node = child;
      }
    }
    return node;
  }

  /**
   * Expansion phase during SO-MCTS
   * @param node is the current node
   * @param determinization is the current session/determinization used
   * @return new pair with node and determinization
   */
  public static Pair<NodeIS,Session> expand(NodeIS node,Session determinization){
    ArrayList<Move> possible = movesFromDetNotInNodeChildren(node,determinization);
    Random random = new Random();
    Move move = possible.get(random.nextInt(possible.size())); //Select random move from compatible moves
    node = node.addChild(move);
    determinization.executeMove(move); // implements move to make and applies change to det
    return new Pair<>(node,determinization);
  }

  /**
   * Simulation phase returning reward.
   * @param determinization to start simulation from
   * @param playOutStyle sets the agents for simulation
   * @param rewardStrategy sets the style of reward returned
   * @param player is used to backpropagate the correct reward based on who is the MCTS player
   * @return a reward for this node
   */
  public static int simulate(Session determinization,int playOutStyle,int rewardStrategy,int player){
    Player one = null;
    Player two = null;
    if(playOutStyle==0){
      one = new RandomPlayer();
      two = new RandomPlayer();
    } if(playOutStyle==1){
      one = new RuleBasedPlayer();
      two = new RuleBasedPlayer();
    } if(playOutStyle==2){
      one = new LikeHumanPlayer();
      two = new LikeHumanPlayer();
    }
    determinization.setPlayer(one,two);
    int[] scores = determinization.simGame();
    if(rewardStrategy==0) {
      int winner = (scores[0]>scores[1])?1:2;
      return (winner==player)?1:0;
    }
    if(rewardStrategy==1){
      if(player==1) return scores[0]-scores[1];
      return scores[1]-scores[0];
    }
    if(rewardStrategy==2){
      int winner = (scores[0]>scores[1])?1:2;
      return (winner==player)?1:-1;
    }
    return 0;
  }

  /**
   * This method upates the search tree's statistics
   * @param reward is the reward generated in the simulation phase
   * @param node is the node where the reward was generated
   * @return true because finished
   */
  public static boolean backPropagate(int reward,NodeIS node){
    while(node!=null){
      node.incVisitCount();
      node.incTotalReward(reward);
      node.incAvailabilityCount();
      if(node.parent==null) return true;
      for(NodeIS n : node.getSiblings()){
        n.incAvailabilityCount();
      }
      node = node.getParent();
    }
    return true;
  }

  /**
   * This method is used to get the moves avaialble in a determinization which are not yet in the list of children of node
   * @param node
   * @param determinization
   * @return a list of moves not expanded in the current node
   */
  public static ArrayList<Move> movesFromDetNotInNodeChildren(NodeIS node, Session determinization){
    HashSet<Move> allActions = new HashSet<>();//if reduceBranching was set true then use advanced version of getPossiblemoves
      allActions.addAll(getPossibleMovesReduce(determinization));
      //allActions = getPossibleMoves(determinization);
    allActions.removeAll(node.getMovesFromChildren());
    ArrayList<Move> moves = new ArrayList<>();
    moves.addAll(allActions);
    return  moves;
  }

  /**
   * this method generates all moves possible for the current player in a given determinization
   * @param game is the current session
   * @return a list of all possible moves
   */
  public static ArrayList<Move> getPossibleMoves(Session game){
    ArrayList<Move> moves = new ArrayList<>();
    //Determine where draws are possible
    int[] possibleDraws = game.getPossibleDrawsInt();
    int length = possibleDraws.length;
    Card[] hand = game.getHandAtTurn();//Get hand of player at turn
    Stack<Card>[] expeditions = game.getPlayerExpeditions(game.isTurn());
    boolean expMovePossible = false;
    for(int i = 0;i<8;i++) {
      int color = hand[i].getColor();
      expMovePossible = Session.addCardPossible(expeditions[color],hand[i],color);
      //If put on discardPile only allow other colors from discard pile
      for(int j = 0;j<length;j++){
        if(expMovePossible){
          moves.add(new Move(i,true,possibleDraws[j]));
        }
        if(color+1!=possibleDraws[j]){
          moves.add(new Move(i,false,possibleDraws[j]));
        }
      }
    }
    return moves;
  }

  /**
   * This method reduces the amount of possible moves for reduction of the branching factor
   * @param game is the current determinization
   * @return a reduced list of moves
   */
  public static ArrayList<Move> getPossibleMovesReduce(Session game){
    ArrayList<Move> allMoves = getPossibleMoves(game);
    ArrayList<Move> onExpMoves = getExpMoves(allMoves);
    ArrayList<Move> onDiscMoves = getDiscMoves(allMoves);
    Card[] hand = game.getHandAtTurn();
    int[] topValuesOfExp = getValuesOfTopCards(game.getExpAtTurn());
    ArrayList<Move> answer = new ArrayList<>();
    answer.addAll(reduceExpMoves(onExpMoves,hand,topValuesOfExp));//Difference between reduce methods is reduce exp makes sure the lowest card on hand is looked at which can still be put on expedition
    answer.addAll(reduceDiscMoves(onDiscMoves,hand));
    answer.addAll(getCoinCardMoves(allMoves,game.getHandAtTurn()));
    return answer;
  }

  /**
   * This method returns the values of the top cards of stacks
   * @param myExp is the array of stacks holding cards
   * @return an array representing the values of top cards
   */
  public static int[] getValuesOfTopCards(Stack<Card>[] myExp){
    int[] topValues = new int[]{0,0,0,0,0};
    for(int i = 0;i<5;i++){
      if(!myExp[i].isEmpty()){
        topValues[i] = myExp[i].peek().getValue();
      } else {
        topValues[i] = -10; //no card on Exp therefore all other cards placeable
      }
    }
    return topValues;
  }

  /**
   * Retrieve all moves placing coin cards.
   * @param allMoves list of moves
   * @param hand player's hand
   * @return moves placing coin cards
   */
  public static ArrayList<Move> getCoinCardMoves(ArrayList<Move> allMoves,Card[] hand){
    ArrayList<Move> answer = new ArrayList<>();
    int numberOfCoinCards = 0;
    for(int i = 0;i<8;i++){
      if(hand[i].isCoinCard()) numberOfCoinCards++;
    }
    int[] indicesCoinCards = new int[numberOfCoinCards];
    int counter = 0;
    for(int i = 0;i<8;i++){
      if(hand[i].isCoinCard()) indicesCoinCards[counter++] = i;
    }
    for(Move m : allMoves){
      if(arrayContainsNumber(indicesCoinCards,m.getCardIndex())) answer.add(m);
    }
    return answer;
  }

  /**
   * This method indicates if an array contains a number
   * @param array
   * @param number
   * @return true if yes
   */
  public static boolean arrayContainsNumber(int[] array,int number){
    for(int i = 0;i< array.length;i++){
      if(array[i]==number) return true;
    }
    return false;
  }

  /**
   * Method reduces the number of moves given the placements onto expeditions
   * @param moves
   * @param hand
   * @param topValuesOfExp
   * @return
   */
  public static ArrayList<Move> reduceExpMoves(ArrayList<Move> moves,Card[] hand,int[] topValuesOfExp){
    ArrayList<Move>[] differentColorExp = new ArrayList[]{new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList()};
    int[] colorOfCardsOnHand = new int[8];
    for(int i = 0;i<8;i++){
      colorOfCardsOnHand[i] = hand[i].getColor();
    }
    for(Move m : moves){
      int colorOfCardToPlace = colorOfCardsOnHand[m.getCardIndex()];
      differentColorExp[colorOfCardToPlace].add(m);
    }
    ArrayList<Move> summarize = new ArrayList<>();
    for(int i = 0;i<5;i++){
      //differentColorExp[i] = keepLowestCardOfExpMoves(i,differentColorExp[i],colorOfCardsOnHand,hand);
      summarize.addAll(keepLowestCardOfExpMoves(i,differentColorExp[i],hand,topValuesOfExp[i]));
    }
    return summarize;
  }

  /**
   * this method reduces the number of moves given the placements onto the discard piles
   * @param moves
   * @param hand
   * @return
   */
  public static ArrayList<Move> reduceDiscMoves(ArrayList<Move> moves,Card[] hand){
    ArrayList<Move>[] differentColorExp = new ArrayList[]{new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList(),new ArrayList()};
    int[] colorOfCardsOnHand = new int[8];
    for(int i = 0;i<8;i++){
      colorOfCardsOnHand[i] = hand[i].getColor();
    }
    for(Move m : moves){
      int colorOfCardToPlace = colorOfCardsOnHand[m.getCardIndex()];
      differentColorExp[colorOfCardToPlace].add(m);
    }
    ArrayList<Move> summarize = new ArrayList<>();
    for(int i = 0;i<5;i++){
      //differentColorExp[i] = keepLowestCardOfExpMoves(i,differentColorExp[i],colorOfCardsOnHand,hand);
      summarize.addAll(keepLowestCardOfExpMoves(i,differentColorExp[i],hand,-10)); //-10 just here because with discard cards it is not needed to check if they are placeable
    }
    return summarize;
  }

  /**
   * This method returns the lowest card placement moves possible given the top card's value o the expedition
   * @param color
   * @param onExpOfColor
   * @param hand
   * @param topValueOnExp
   * @return
   */
  public static ArrayList<Move> keepLowestCardOfExpMoves(int color,ArrayList<Move> onExpOfColor,Card[] hand,int topValueOnExp){
    int indexOfLowestCardOfColor = -1;
    int valueOfLowestCardOfColor = 100;
    int counter = 0;
    for(Card c : hand){
      if(c.getColor()==color && c.getValue()<valueOfLowestCardOfColor && !c.isCoinCard() && c.getValue()>topValueOnExp) {
        indexOfLowestCardOfColor = counter;
        valueOfLowestCardOfColor = c.getValue();
      }
      counter++;
    }
    return filterCardIndexFromMoves(onExpOfColor,indexOfLowestCardOfColor);
  }

  /**
   * Filter the moves with the placement of cardIndex
   * @param onExpOfColor
   * @param cardIndex
   * @return
   */
  public static ArrayList<Move> filterCardIndexFromMoves(ArrayList<Move> onExpOfColor,int cardIndex){
    ArrayList<Move> movesWithIndex = new ArrayList<>();
    for(Move m : onExpOfColor){
      if(m.getCardIndex()==cardIndex) movesWithIndex.add(m);
    }
    return movesWithIndex;
  }

  /**
   * Filters list retaining moves with placements onto the expeditions
   * @param allMoves
   * @return
   */
  public static ArrayList<Move> getExpMoves(ArrayList<Move> allMoves){
    ArrayList<Move> expMoves = new ArrayList<>();
    for(Move m : allMoves){
      if(m.isOnExp()) expMoves.add(m);
    }
    return  expMoves;
  }

  /**
   * Filters list retaining moves with placements onto the discard piles
   * @param allMoves
   * @return
   */
  public static ArrayList<Move> getDiscMoves(ArrayList<Move> allMoves){
    ArrayList<Move> expMoves = new ArrayList<>();
    for(Move m : allMoves){
      if(!m.isOnExp()) expMoves.add(m);
    }
    return  expMoves;
  }

}