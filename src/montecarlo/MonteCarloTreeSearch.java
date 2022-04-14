package montecarlo;

import game.Card;
import game.Move;
import game.Session;

public abstract class MonteCarloTreeSearch {

  public Move mctsSearch(Session initial,int iterations,boolean heavyPlayout){
    long end;
    long start = System.currentTimeMillis();
    Node root = new Node(initial);
    int player = (initial.isTurn())?1:2;
    while(iterations>0) {
      iterations--;
      Node  promisingNode = treePolicy(root);
      int winner = defaultPolicy(promisingNode.getState(),heavyPlayout);
      int reward = 0;
      if(winner==player) reward=1;
      backPropagate(promisingNode,reward);
    }
    Node bestChild = bestChild(root,0, initial.isTurn());
    end = System.currentTimeMillis();
    System.out.println("One Move of MCTS: " + (end-start));
    return getMove(root,bestChild);
  }

  public Move mctsSearchTimed(Session initial,int time,boolean heavyPlayout){
    Node root = new Node(initial);
    int player = (initial.isTurn())?1:2;
    long end;
    long start = System.currentTimeMillis();
    while(System.currentTimeMillis()<start+time) {
      Node  promisingNode = treePolicy(root);
      int winner = defaultPolicy(promisingNode.getState(),heavyPlayout);
      int reward = 0;
      if(winner==player) reward=1;
      backPropagate(promisingNode,reward);
    }
    Node bestChild = bestChild(root,0,initial.isTurn());
    //end = System.currentTimeMillis();
    //System.out.println("One Move of MCTS: " + (end-start));
    return getMove(root,bestChild);
  }

  public abstract Node treePolicy(Node root);

  public abstract int defaultPolicy(Session game,boolean heavyPlayout);

  public abstract void backPropagate(Node promisingNode,int reward);

  public Move getMove(Node initial,Node bestChild){
    initial.getState().calcPoints();
    bestChild.getState().calcPoints();
    int playerNumber = (initial.getState().isTurn())?1:2;
    int index = -1;
    boolean onExp = false;
    int drawFrom = -1;
    Card[] currentHand = initial.getState().getHandAtTurn();
    Card[] nextHand = (initial.getState().isTurn())?bestChild.getState().getPlayerHand(true):bestChild.getState().getPlayerHand(false);
    for(int i = 0;i<8;i++){
      if(!currentHand[i].equals(nextHand[i])) {
        index = i;
        i=9;
      }
    }
    if(playerNumber==1 && (bestChild.getState().calcPointsPlayer(0)!=initial.getState().calcPointsPlayer(0))) onExp=true;
    if(playerNumber==2 && (bestChild.getState().calcPointsPlayer(1)!=initial.getState().calcPointsPlayer(1))) onExp=true;
    if(initial.getState().getCardsLeft()>bestChild.getState().getCardsLeft()) {
      drawFrom = 0;
    } else {
      if(index==-1){
        System.out.println("STOP");
      }
      drawFrom = nextHand[index].getColor()+1;
    }
    return new Move(index,onExp,drawFrom);
  }

  public abstract Node bestChild(Node root,double constantExploration,boolean imP1);

  public Node mctsSearchReturnTree(Session initial,int iterations,boolean heavyPlayout){
    Node root = new Node(initial);
    int player = (initial.isTurn())?1:2;
    while(iterations>0) {
      iterations--;
      Node  promisingNode = treePolicy(root);
      int winner = defaultPolicy(promisingNode.getState(),heavyPlayout);
      int reward = 0;
      if(winner==player) reward=1;
      backPropagate(promisingNode,reward);
    }
    return root;
  }

}
