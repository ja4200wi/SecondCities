package player;

import game.Card;
import game.Move;
import game.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import montecarlo.InformationIS;
import montecarlo.Node;
import montecarlo.UCT;

public class DeterminatorPlayer extends Player {

  private int timePerDet;
  int numOfDet;
  boolean heavy;
  InformationIS information = null;

  public DeterminatorPlayer(boolean heavyPlayout,int numDet,int timePerDet){
    super();
    this.timePerDet = timePerDet;
    this.numOfDet = numDet;
    this.heavy =heavyPlayout;
  }

  @Override
  public Move makeMove(Card[] myHand,Stack<Card>[] myExp, Stack<Card>[] oppExp, Stack<Card>[] discardPile,boolean turn,int turnCounter) {
    information = new InformationIS(myHand,myExp,oppExp,discardPile,turn,this.imP1,turnCounter,null);
    Move m = determinization();
    return m;
  }

  public Move determinization(){
    UCT uct = new UCT();
    Node[] roots = new Node[numOfDet]; //Array with roots of different mct
    for(int i = 0;i<numOfDet;i++){
      roots[i] = uct.mctsSearchReturnTree(information.createDeterminization(), timePerDet,heavy); //TODO
    }
    Node[] children = roots[0].getChildren().toArray(new Node[0]); //Array with space for all children
    for(int i = 1;i<numOfDet;i++) {
      for(Node child : children){
        updateStats(child,roots[i]);
      }
    }
    Node bestChild = bestChild(children);
    return null;//uct.getMove(new Node(information.createDeterminization()),bestChild);
  }

  public void updateStats(Node aggregate,Node oneDetStat){
    aggregate.setSimulationsPlayed(aggregate.getSimulationsPlayed()+ oneDetStat
        .getSimulationsPlayed());
    aggregate.setSimulationsWon(aggregate.getSimulationsWon()+ aggregate.getSimulationsWon());
  }

  public Node bestChild(Node[] children){
    int max = -1;
    Node bestChild = null;
    for(Node n : children){
      if(n.getSimulationsWon()>max){
        max = n.getSimulationsWon();
        bestChild = n;
      }
    }
    return bestChild;
  }

  @Override
  public Move makeMove(Session session) {
    return null;
  }
}
