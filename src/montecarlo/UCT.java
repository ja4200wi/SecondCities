package montecarlo;

import game.Card;
import game.Session;
import java.util.ArrayList;
import player.RandomPlayer;
import player.RuleBasedPlayer;

public class UCT extends MonteCarloTreeSearch{

  @Override
  public Node treePolicy(Node promisingNode) {
    boolean imP1 = promisingNode.getState().isTurn();
    while(!promisingNode.isTerminal()){
      if(!promisingNode.isFullyExpanded()) {
        //expands all in .isFullyExpanded
      } else if(!promisingNode.allChildrenSimOne()) {
        return expand(promisingNode);
      }
      else {
        promisingNode = bestChild(promisingNode,Math.sqrt(2),imP1);
      }
    }
    return promisingNode;
  }

  public Node expand(Node node) {
    return node.getChildWithoutSim();
  }

  @Override
  public int defaultPolicy(Session game,boolean heavyPlayout) {
    Session simulation = new Session(game);
    int[] scores;
    Card[] hand1 = simulation.getPlayerHand(true);
    Card[] hand2 = simulation.getPlayerHand(false);
    if(heavyPlayout) {
      simulation.setPlayer(new RuleBasedPlayer(hand1),new RuleBasedPlayer(hand2));
      scores = simulation.playGame();
    } else {
      simulation.setPlayer(new RandomPlayer(hand1),new RandomPlayer(hand2));
      scores = simulation.playGame();
    }
    return (scores[0]>scores[1])?1:2;
  }

  @Override
  public void backPropagate(Node node, int reward) {
    while(node!=null) {
      node.incSimPlayed();
      node.incSimWon(reward);
      node = node.getParent();
    }
  }

  @Override
  public Node bestChild(Node root,double constantExploration,boolean imP1) {
    boolean turn = root.getState().isTurn();
    Node bestChild = null;
    double ctValue = -1;
    for(Node n : root.getChildren()) {
      int simWon = (imP1==turn)?n.getSimulationsWon():n.getSimulationsPlayed()-n.getSimulationsWon();
      double newCtValue = (double) simWon/n.simulationsPlayed +
          constantExploration *
              (Math.sqrt((2*Math.log(root.simulationsPlayed))/n.simulationsPlayed));
      if(newCtValue>ctValue) {
        bestChild = n;
        ctValue = newCtValue;
      }
    }
    return bestChild;
  }
}
