package test;

import game.Card;
import game.Move;
import game.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import javafx.util.Pair;
import montecarlo.InformationIS;
import montecarlo.MonteCarloIS;
import montecarlo.NodeIS;
import org.junit.Test;
import player.CheatingMCTSPlayer;
import player.DeterminatorPlayer;
import player.ISPlayer;
import player.Player;
import player.RandomPlayer;
import player.RuleBasedPlayer;
import player.RuleBasedPlayerALT;

public class LostTest {

  @Test
  public void testAPI(){
    RandomPlayer random;
    RandomPlayer random2;
    //Session game = new Session(random,random2);
  }

  @Test
  public void testRandomSpeed(){
    RandomPlayer random = new RandomPlayer();
    RandomPlayer random2 = new RandomPlayer();
    RuleBasedPlayer rule = new RuleBasedPlayer();
    RuleBasedPlayer rule2 = new RuleBasedPlayer();
    Session game;
    for(int i = 0;i<100000;i++){
      game = new Session(rule,rule2);
      game.playGame();
    }
  }

  @Test
  public void testMethods(){
    Stack<Card> stack = createTestCards(); //Card deck
    Card[] myHand = new Card[8];
    for(int i = 0;i<8;i++) myHand[i] = stack.pop(); //Fill my Hand
    Stack<Card> example = new Stack<>();
    example.add(new Card(0,5));
    Stack<Card>[] myExp = new Stack[]{example, new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    Stack<Card>[] oppExp = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    Stack<Card>[] discard = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    ArrayList<Card> oppCardsIKnow = new ArrayList<>();
    oppCardsIKnow.add(new Card(0,8));
    //Above create empty game board representations
    InformationIS information = new InformationIS(myHand,myExp,oppExp,discard,true,true,0,oppCardsIKnow);
    Session det = information.createDeterminization();
    //@TODO: Assert.assertEquals(43,det.getCardsLeft());
    NodeIS nodeWithOneChild = new NodeIS();
    nodeWithOneChild.addChild(new Move(0,true,0));
    ArrayList<Move> moves = MonteCarloIS.movesFromDetNotInNodeChildren(nodeWithOneChild,det);
    //@TODO: Assert.assertEquals(15,moves.size()); here again determine exactly Cards
    MonteCarloIS mcIS = new MonteCarloIS();
    Pair<NodeIS,Session> pair;
    pair = mcIS.select(new NodeIS(),det,Math.sqrt(2));
    System.out.println("DEBUG"); //@TODO: Assert
    pair = mcIS.expand(nodeWithOneChild,det);
    System.out.println("DEBUG"); //@TODO: Assert
    ISPlayer smart = new ISPlayer(true,1000,0.7,0);
    RandomPlayer random = new RandomPlayer();
    Session game = new Session(smart,random);
    game.getDiscardPile()[0].add(new Card(0,10));
    game.getDiscardPile()[1].add(new Card(1,10));
    game.getDiscardPile()[2].add(new Card(2,10));
    game.getDiscardPile()[3].add(new Card(3,10));
    game.getDiscardPile()[4].add(new Card(4,10));
    ArrayList<Move> movesAll = MonteCarloIS.getPossibleMoves(game);
    System.out.println(movesAll.size());
  }

  @Test
  public void testConstExploration(){
    RuleBasedPlayer rule = new RuleBasedPlayer();
    ISPlayer dumb = new ISPlayer(false,1000,0.4,0);
    ISPlayer dumb2 = new ISPlayer(false,1000,0.6,0);
    ISPlayer dumb3 = new ISPlayer(false,1000,0.7,0);
    ISPlayer dumb4 = new ISPlayer(false,1000,0.8,0);
    ISPlayer dumb5 = new ISPlayer(false,1000,1,0);
    ISPlayer dumb6 = new ISPlayer(false,1000,1.2,0);
    ISPlayer dumb7 = new ISPlayer(false,1000,1.4,0);
    int winsDumb = 0;
    int winsDumb2 = 0;
    int winsDumb3 = 0;
    int winsDumb4 = 0;
    int winsDumb5 = 0;
    int winsDumb6 = 0;
    int winsDumb7 = 0;
    for(int i = 0;i<100;i++){
      Session game= new Session(dumb,rule);
      int [] scores = game.playGame();
      if(scores[0]>scores[1]) winsDumb++;
      String nameWinner = (scores[0]>scores[1])?"ISMCTS":"Rule";
      System.out.println("Game won by player " + nameWinner);
    }
    for(int i = 0;i<100;i++){
      Session game = new Session(dumb2,rule);
      int [] scores = game.playGame();
      if(scores[0]>scores[1]) winsDumb2++;
      String nameWinner = (scores[0]>scores[1])?"ISMCTS":"Rule";
      System.out.println("Game won by player " + nameWinner);
    }
    for(int i = 0;i<100;i++){
      Session game = new Session(dumb3,rule);
      int [] scores = game.playGame();
      if(scores[0]>scores[1]) winsDumb3++;
      String nameWinner = (scores[0]>scores[1])?"ISMCTS":"Rule";
      System.out.println("Game won by player " + nameWinner);
    }
    for(int i = 0;i<100;i++){
      Session game = new Session(dumb4,rule);
      int [] scores = game.playGame();
      if(scores[0]>scores[1]) winsDumb4++;
      String nameWinner = (scores[0]>scores[1])?"ISMCTS":"Rule";
      System.out.println("Game won by player " + nameWinner);
    }
    for(int i = 0;i<100;i++){
      Session game = new Session(dumb5,rule);
      int [] scores = game.playGame();
      if(scores[0]>scores[1]) winsDumb5++;
      String nameWinner = (scores[0]>scores[1])?"ISMCTS":"Rule";
      System.out.println("Game won by player " + nameWinner);
    }
    for(int i = 0;i<100;i++){
      Session game = new Session(dumb6,rule);
      int [] scores = game.playGame();
      if(scores[0]>scores[1]) winsDumb6++;
      String nameWinner = (scores[0]>scores[1])?"ISMCTS":"Rule";
      System.out.println("Game won by player " + nameWinner);
    }
    for(int i = 0;i<100;i++){
      Session game = new Session(dumb7,rule);
      int [] scores = game.playGame();
      if(scores[0]>scores[1]) winsDumb7++;
      String nameWinner = (scores[0]>scores[1])?"ISMCTS":"Rule";
      System.out.println("Game won by player " + nameWinner);
    }
    System.out.println("Wins Player Const Exploration 0.4: " + winsDumb + "\nWins Player Const Exploration 0.6: " + winsDumb2
    + "\nWins Player Const Exploration 0.7: " + winsDumb3 + "\nWins Player Const Exploration 0.8: " + winsDumb4 +
        "\nWins Player Const Exploration 1: " + winsDumb5 +"\nWins Player Const Exploration 1.2: " + winsDumb6 +
        "\nWins Player Const Exploration 1.4: " + winsDumb7);
  }

  @Test
  public void testLightHeavy(){
    ISPlayer light = new ISPlayer(false,100,0.7,0);
    ISPlayer heavy = new ISPlayer(true,100,0.7,0);
    Session game;
    int winsLight = 0;
    int winsHeavy = 0;
    int turns = 0;
    int onExps = 0;
    for(int i = 0;i<1;i++){
      game = new Session(light,heavy);
      int[] gameEndInfo = game.playGame();
      if(gameEndInfo[0]>gameEndInfo[1]) {
        winsLight++;
      } else {
        winsHeavy++;
      }
      turns += gameEndInfo[2];
      onExps += gameEndInfo[3];
      String nameWinner = (gameEndInfo[0]>gameEndInfo[1])?"Light":"Heavy";
      System.out.println("Game won by player " + nameWinner);
    }
    for(int i = 0;i<0;i++){
      game = new Session(heavy,light);
      int[] gameEndInfo = game.playGame();
      if(gameEndInfo[0]>gameEndInfo[1]) {
        winsHeavy++;
      } else {
        winsLight++;
      }
      turns += gameEndInfo[2];
      onExps += gameEndInfo[3];
      String nameWinner = (gameEndInfo[0]>gameEndInfo[1])?"Heavy":"Light";
      System.out.println("Game won by player " + nameWinner);
    }
    System.out.println("Wins Light: "+ winsLight + "\tWins Heavy: " + winsHeavy + "\tTurns: " +
        turns + "\tTimes played on expedition: " + onExps);
  }

  @Test
  public void testDetPlayer(){
    DeterminatorPlayer determinator = new DeterminatorPlayer(false,15,400);
    ISPlayer dumb = new ISPlayer(false,6000,0.7,0);
    Session game = new Session(determinator,dumb);
    int[] scores = game.playGame();
    System.out.println("Scores ->\tDeterminization: " + scores[0] + "\t:\t" + scores[1] + ":ISMCTS") ;
  }

  @Test
  public void testIterations(){
    ISPlayer light07Reward0 = new ISPlayer(false,1000,0.7,0);
    //ISPlayer light07Reward0 = new ISPlayer(false,8000,0.7,0);
    RuleBasedPlayer rule = new RuleBasedPlayer();
    Session game = new Session(light07Reward0,rule);
    game.playGame();
    System.out.println(game);
  }

  @Test
  public void testRewardStrategy(){
    ISPlayer strategy0 = new ISPlayer(false,1000,0.7,0);
    ISPlayer strategy2 = new ISPlayer(false,1000,0.7,2);
    Session game;
    int winsStrat0 = 0;
    int winsStrat1 = 0;
    for(int i = 0;i<25;i++){
      game = new Session(strategy0,strategy2);
      int [] scores = game.playGame();
      if(scores[0]>scores[1]) { winsStrat0++; }
      else {winsStrat1++;}
      String nameWinner = (scores[0]>scores[1])?"Strategy 0":"Strategy 1";
      System.out.println("Game won by player " + nameWinner);
    }
    for(int i = 0;i<25;i++){
      game = new Session(strategy2,strategy0);
      int [] scores = game.playGame();
      if(scores[0]>scores[1]) { winsStrat1++; }
      else {winsStrat0++;}
      String nameWinner = (scores[0]>scores[1])?"Strategy 1":"Strategy 0";
      System.out.println("Game won by player " + nameWinner);
    }
    System.out.println("Wins Strat0: " + winsStrat0 + "Wins Strat1: " + winsStrat1);
  }

  @Test
  public void experiment(){
    ISPlayer light8sec07reward0 = new ISPlayer(false,100,0.7,0);
    ISPlayer heavy8sec07reward0 = new ISPlayer(true,8000,0.7,0);
    ISPlayer light8sec14reward2 = new ISPlayer(false,500,1.4,2);
    RuleBasedPlayer ruleOld = new RuleBasedPlayer();
    RuleBasedPlayerALT ruleNew = new RuleBasedPlayerALT();
    RuleBasedPlayer rule = new RuleBasedPlayer();
    Experiment experiment = new Experiment(light8sec07reward0,rule,4);
    Experiment experiment2 = new Experiment(light8sec14reward2,rule,2);
    Experiment experiment3 = new Experiment(ruleNew,ruleOld,50000);
    experiment.doExperiment();
    experiment2.doExperiment();
    experiment3.doExperiment();
  }

  @Test
  public void testBasics(){
    Card card1 = new Card(0,0);
    Card card2 = new Card(0,1);
  }

  Stack<Card> createTestCards(){
    ArrayList<Card> cards = InformationIS.createCardDeck();
    Collections.shuffle(cards);
    Stack<Card> cardStack = new Stack<>();
    cardStack.addAll(cards);
    return cardStack;
  }

  public class Experiment{

    int numberOfSimulations;
    Player p1;
    Player p2;

    public Experiment(Player p1, Player p2,int numberOfSimulations){
      this.numberOfSimulations = numberOfSimulations;
      this.p1 = p1;
      this.p2 = p2;
    }

    public int[] doExperiment(){
      Session game;
      int winsP1 = 0;
      int winsP2 = 0;
      int cumulativeScoreP1 = 0;
      int cumulativeScoreP2 = 0;
      int averageNumberOfTurns = 0;
      int averageNumberOfExpTurns = 0;
      for(int i = 0;i<numberOfSimulations/2;i++){
        game = new Session(p1,p2);
        int[] results = game.playGame();
        cumulativeScoreP1 += results[0];
        cumulativeScoreP2 += results[1];
        averageNumberOfTurns += results[2];
        averageNumberOfExpTurns += results[3];
        if(results[0]>results[1]) winsP1++;
        if(results[1]>results[0]) winsP2++;
      }
      for(int i = 0;i<numberOfSimulations/2;i++){
        game = new Session(p2,p1);
        int[] results = game.playGame();
        cumulativeScoreP1 += results[1];
        cumulativeScoreP2 += results[0];
        averageNumberOfTurns += results[2];
        averageNumberOfExpTurns += results[3];
        if(results[0]>results[1]) winsP2++;
        if(results[1]>results[0]) winsP1++;
      }
      averageNumberOfExpTurns /= numberOfSimulations;
      averageNumberOfTurns /= numberOfSimulations;
      System.out.println("Wins Player 1: " + winsP1 + "\tWins Player 2: " + winsP2 +
          "\nCumulative Score Player 1: " + cumulativeScoreP1 + "\tCumulative Score Player 2: " + cumulativeScoreP2 +
          "\nAverage Number of Turns: " + averageNumberOfTurns + "\tAverage Number of Expedition Plays: " +averageNumberOfExpTurns);
      return new int[]{winsP1,winsP2,cumulativeScoreP1,cumulativeScoreP2,averageNumberOfTurns,averageNumberOfExpTurns};
    }
  }

}
