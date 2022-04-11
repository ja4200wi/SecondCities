package montecarlo;

import game.Move;
import java.util.List;

public class NodeIS {

  List<NodeIS> children;
  NodeIS parent;
  int visitCount;
  int availabilityCount;
  int totalReward;
  Move incomingAction;

  public NodeIS(){

  }

}
