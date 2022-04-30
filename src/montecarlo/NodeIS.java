package montecarlo;

import game.Card;
import game.Move;
import game.Session;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Jann Winter
 * This class represents a node in the MCTS  tree
 */

public class NodeIS {

  ArrayList<NodeIS> children;
  NodeIS parent;
  int visitCount;
  int availabilityCount;
  int totalReward;
  Move incomingAction;

  /**
   * Constructor for root node creation often.
   */
  public NodeIS(){
    children = new ArrayList<NodeIS>();
    parent = null;
    visitCount = 0;
    availabilityCount = 0;
    totalReward = 0;
    incomingAction = null;
  }

  /**
   * Create a list of possible children accessible from a determinization
   * @param determinization
   * @return
   */
  public ArrayList<NodeIS> getChildrenConformWith(Session determinization){
    ArrayList<NodeIS> conform = new ArrayList<>();
    ArrayList<Move> availableMoves = getPossibleMoves(determinization);
    for(NodeIS child : children){
      if(availableMoves.contains(child.getIncomingAction())){ //@TODO: This method might need comparable interface
        conform.add(child);
      }
    }
    return conform;
  }

  /**
   * This constructor creates a node with an incomingAction
   * @param move
   */
  public NodeIS(Move move){
    children = new ArrayList<NodeIS>();
    parent = null;
    visitCount = 0;
    availabilityCount = 0;
    totalReward = 0;
    incomingAction = move;
  }

  /**
   * This method returns all nodes accessible from the parent node
   * @return
   */
  public ArrayList<NodeIS> getSiblings(){
    if(parent==null) return null;
    return this.parent.children;
  }

  /**
   * This method appends a child to the current node and sets the incoming action
   * @param move
   * @return
   */
  public NodeIS addChild(Move move){
    NodeIS child = new NodeIS(move);
    this.children.add(child);
    child.parent = this;
    return child;
  }

  public void addChild(NodeIS child){
    children.add(child);
  }

  public NodeIS getParent() {
    return parent;
  }

  public void incTotalReward(int reward){
    totalReward += reward;
  }

  public void incAvailabilityCount(){
    availabilityCount++;
  }

  public void incVisitCount(){
    visitCount++;
  }

  public Move getIncomingAction() {
    return incomingAction;
  }

  /**
   * Get all possible moves of player in session
   * @param game
   * @return the list of moves possible
   */
  public ArrayList<Move> getPossibleMoves(Session game){
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
   * Get the list of moves containing all children's incoming moves
   * @return
   */
  public ArrayList<Move> getMovesFromChildren(){
    ArrayList<Move> moves = new ArrayList<>();
    for(NodeIS node : children){
      moves.add(node.getIncomingAction());
    }
    return moves;
  }

  /**
   * Method determines the best child of current node, which is the most visited one
   * @return the best child
   */
  public NodeIS getBestChild() {
    int maxVisits = -1;
    NodeIS bestChild = children.get(0);
    for(NodeIS node: children){
      if(node.visitCount>maxVisits){
        maxVisits = node.visitCount;
        bestChild = node;
      }
    }
    return bestChild;
  }

  @Override
  public String toString(){
    return "Size Children: " + this.children.size() + ";\tVisitCount: " + this.visitCount +
        ";\tIncoming Action: " + incomingAction;
  }
}
