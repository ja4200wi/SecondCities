package montecarlo;

import game.Card;
import game.Move;
import game.Session;
import javafx.util.Pair;

public abstract class MonteCarloTreeSearch {

  public Move mctsSearch(Session initial,int iterations,boolean heavyPlayout){
    long end;
    long start = System.currentTimeMillis();
    Node root = new Node();
    int player = (initial.isTurn())?1:2;
    while(iterations>0) {
      iterations--;
      Pair<Node,Session>  promisingNode = treePolicy(root,initial);
      int winner = defaultPolicy(promisingNode.getValue(), heavyPlayout);
      int reward = 0;
      if(winner==player) reward=1;
      backPropagate(promisingNode.getKey(),reward);
    }
    Node bestChild = bestChild(root,0, initial.isTurn());
    end = System.currentTimeMillis();
    System.out.println("One Move of MCTS: " + (end-start));
    return bestChild.incomingMove;
  }

  public Move mctsSearchTimed(Session initial,int time,boolean heavyPlayout){
    Node root = new Node();
    int player = (initial.isTurn())?1:2;
    long end;
    long start = System.currentTimeMillis();
    while(System.currentTimeMillis()<start+time) {
      Pair<Node,Session> nodeAndDet = treePolicy(root,initial);
      int winner = defaultPolicy(nodeAndDet.getValue(),heavyPlayout);
      int reward = 0;
      if(winner==player) reward=1;
      backPropagate(nodeAndDet.getKey(), reward);
    }
    Node bestChild = bestChild(root,0,initial.isTurn());
    //end = System.currentTimeMillis();
    //System.out.println("One Move of MCTS: " + (end-start));
    return bestChild.incomingMove;
  }

  public abstract Pair<Node,Session> treePolicy(Node root,Session det);

  public abstract int defaultPolicy(Session game,boolean heavyPlayout);

  public abstract void backPropagate(Node promisingNode,int reward);

  public abstract Node bestChild(Node root,double constantExploration,boolean imP1);

  public Node mctsSearchReturnTree(Session initial,int iterations,boolean heavyPlayout){
    Node root = new Node();
    int player = (initial.isTurn())?1:2;
    while(iterations>0) {
      iterations--;
      Pair<Node,Session>  promisingNode = treePolicy(root,initial);
      int winner = defaultPolicy(promisingNode.getValue(), heavyPlayout);
      int reward = 0;
      if(winner==player) reward=1;
      backPropagate(promisingNode.getKey(),reward);
    }
    Node bestChild = bestChild(root,0, initial.isTurn());
    return root;
  }

}
