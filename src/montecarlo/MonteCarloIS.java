package montecarlo;

import game.Move;
import game.Session;
import javafx.util.Pair;
import player.RandomPlayer;

public class MonteCarloIS {

  public Move ismcts(InformationIS initialIS,int iterations){
    NodeIS root = createRootNode(initialIS);
    Pair<NodeIS,Session> nodeAndDet;
    int player = (initialIS.isTurn())?1:2;
    while(iterations>0){
      Session determinization= initialIS.chooseDeterminization();
      nodeAndDet = select(root,determinization);
      if(actionsFromDetNode(nodeAndDet.getKey(),determinization).size()!=0) {
        nodeAndDet = expand(nodeAndDet.getKey(),determinization);
      }
      int winner = simulate(nodeAndDet.getValue());
      int reward = (winner==player)?1:0;
      backPropagate(reward,nodeAndDet.getKey());
    }
    return bestMove(root); //TODO: return action with highest visit count
  }

  public Pair<NodeIS,Session> select(NodeIS node,Session determinization){
    while(!determinization.isOver() && actionsFromDetNode(node,determinization).size()!=0){
      node = c; //TODO: probably should be a deep copy
      determinization = f(determinization,node.getIncomingAction()); //TODO: probably should be a deep copy
    }
    return new Pair<>(node,determinization);
  }

  public Pair<NodeIS,Session> expand(NodeIS node,Session determinization){
    Move move = chooseRandomAction(actionsFromDetNode(node,determinization));
    NodeIS child = new NodeIS(move);
    node.addChild(child);
    node = child; //TODO: probably should be a deep copy
    determinization = resultingMove(determinization,move);
    return new Pair<>(node,determinization);
  }

  public int simulate(Session determinization){
    RandomPlayer random = new RandomPlayer();
    RandomPlayer random2 = new RandomPlayer();
    determinization.setPlayer(random,random2);
    int[] scores = determinization.playGame();
    return (scores[0]>scores[1])?1:2;
  }

  public void backPropagate(int reward,NodeIS node){
    while(node!=null){
      node.incVisitCount();
      node.incTotalReward(reward);
      node.incAvailabilityCount();
      for(NodeIS n : node.getSiblings()){
        n.incAvailabilityCount();
      }
      node = node.getParent();
    }
  }

}
