package montecarlo;

import game.Session;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Jann Winter
 * This class represents a node in the MCTS
 */
public class Node {

  Session state;
  Node parent;
  HashSet<Node> children;
  int simulationsPlayed;
  int simulationsWon; // rewards
  int possibleChildren = -1;

  public Node(Session game) {
    state = game;
    children = new HashSet<>();
  }

  /**
   * This method is called to increase the score of the node. Usually by one,
   * if simulation played was a win.
   * @param score
   */
  public void incSimWon(int score) {
    simulationsWon += score;
  }

  /**
   * This method increments the number of visits on this node
   */
  public void incSimPlayed(){
    simulationsPlayed++;
  }

  /**
   * This method is called to determine whether the node
   * represents a state where the game is over.
   * @return true if game over
   */
  public boolean isTerminal(){
    return state.isOver();
  }

  /**
   * This method compares the number of children (expanded states from current node) to the amount
   * of states that could be expanded.
   * @return true if all possible states have been expanded
   */
  public boolean isFullyExpanded(){
    if(possibleChildren==-1){
      possibleChildren = state.getPossibleStates().size();
    }
    return (possibleChildren==children.size());
  }

  public static int numberOfNodesBelow(Node n){
    int childrenSize = n.children.size();
    for(Node node : n.children) {
      childrenSize += numberOfNodesBelow(node);
    }
    return childrenSize;
  }

  public boolean allChildrenSimOne(){
    for(Node n : children){
      if(n.getSimulationsPlayed()==0) return false;
    }
    return true;
  }
  /** Set methods */
  public void setState(Session state) {
    this.state = state;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public void setChildren(HashSet<Node> children) {
    this.children = children;
  }

  public void setSimulationsPlayed(int simulationsPlayed) {
    this.simulationsPlayed = simulationsPlayed;
  }

  public void setSimulationsWon(int simulationsWon) {
    this.simulationsWon = simulationsWon;
  }

  /** Get methods */
  public Node getParent() {
    return parent;
  }

  public Session getState() {
    return state;
  }

  public HashSet<Node> getChildren() {
    return children;
  }

  public int getSimulationsPlayed() {
    return simulationsPlayed;
  }

  public int getSimulationsWon() {
    return simulationsWon;
  }

  @Override
  public String toString() {
    return "Node{" +
        "simulationsPlayed=" + simulationsPlayed +
        ", simulationsWon=" + simulationsWon +
        '}';
  }

  public Node getChildWithoutSim() {
    for (Node n : children) {
      if(n.getSimulationsPlayed()==0){
        return  n;
      }
    }
    return null;
  }
}
