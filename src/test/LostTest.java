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
import player.ISPlayer;
import player.Player;
import player.RandomPlayer;
import player.RuleBasedPlayer;

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
    ISPlayer smart = new ISPlayer(0,1000,0.7,0,true);
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
  public void avgChildren(){
    String numChilds = "Reduce true 17\n"
        + "Reduce false 40\n"
        + "Reduce true 35\n"
        + "Reduce false 38\n"
        + "Reduce true 26\n"
        + "Reduce false 40\n"
        + "Reduce true 31\n"
        + "Reduce false 40\n"
        + "Reduce true 27\n"
        + "Reduce false 34\n"
        + "Reduce true 25\n"
        + "Reduce false 35\n"
        + "Reduce true 37\n"
        + "Reduce false 34\n"
        + "Reduce true 32\n"
        + "Reduce false 51\n"
        + "Reduce true 36\n"
        + "Reduce false 44\n"
        + "Reduce true 31\n"
        + "Reduce false 54\n"
        + "Reduce true 45\n"
        + "Reduce false 56\n"
        + "Reduce true 39\n"
        + "Reduce false 53\n"
        + "Reduce true 43\n"
        + "Reduce false 65\n"
        + "Reduce true 55\n"
        + "Reduce false 71\n"
        + "Reduce true 65\n"
        + "Reduce false 75\n"
        + "Reduce true 73\n"
        + "Reduce false 68\n"
        + "Reduce true 64\n"
        + "Reduce false 62\n"
        + "Reduce true 67\n"
        + "Reduce false 61\n"
        + "Reduce true 47\n"
        + "Reduce false 54\n"
        + "Reduce true 38\n"
        + "Reduce false 37\n"
        + "Reduce true 42\n"
        + "Reduce false 45\n"
        + "Reduce true 48\n"
        + "Reduce false 51\n"
        + "Reduce true 36\n"
        + "Reduce false 41\n"
        + "Reduce true 32\n"
        + "Reduce false 51\n"
        + "Reduce true 32\n"
        + "Reduce false 35\n"
        + "Reduce true 32\n"
        + "Reduce false 52\n"
        + "Reduce true 46\n"
        + "Reduce false 54\n"
        + "Reduce true 41\n"
        + "Reduce false 54\n"
        + "Reduce true 45\n"
        + "Reduce false 59\n"
        + "Reduce true 37\n"
        + "Reduce false 47\n"
        + "Reduce true 36\n"
        + "Reduce false 43\n"
        + "Reduce true 30\n"
        + "Reduce false 43\n"
        + "Reduce true 48\n"
        + "Reduce false 49\n"
        + "Reduce true 38\n"
        + "Reduce false 52\n"
        + "Reduce true 41\n"
        + "Reduce false 50\n"
        + "Reduce true 42\n"
        + "Reduce false 48\n"
        + "Reduce true 49\n"
        + "Reduce false 60\n"
        + "Reduce true 57\n"
        + "Reduce false 48\n"
        + "Reduce true 55\n"
        + "Reduce false 47\n"
        + "Reduce true 52\n"
        + "Reduce false 53\n"
        + "Reduce true 49\n"
        + "Reduce false 46\n"
        + "Reduce true 49\n"
        + "Reduce false 47\n"
        + "Reduce true 41\n"
        + "Reduce false 46\n"
        + "Reduce true 36\n"
        + "Reduce false 45\n"
        + "Reduce true 44\n"
        + "Reduce false 54\n"
        + "Reduce true 44\n"
        + "Reduce false 51\n"
        + "Reduce true 41\n"
        + "Reduce false 52\n"
        + "Reduce true 50\n"
        + "Reduce false 46\n"
        + "Reduce true 52\n"
        + "Reduce false 46\n"
        + "Reduce true 54\n"
        + "Reduce false 55";
    String[] split = numChilds.split("\n");
    ArrayList<String> reduce = new ArrayList<>();
    ArrayList<String> noReduce = new ArrayList<>();
    int numChildReduce = 0;
    int numChildNoReduce = 0;
    for(String s : split){
      if(s.contains("true")) reduce.add(s.substring(11).trim());
      if(s.contains("false")) noReduce.add(s.substring(12).trim());
    }
    for(String s : reduce){
      numChildReduce += Integer.parseInt(s);
    }
    for(String s : noReduce){
      numChildNoReduce += Integer.parseInt(s);
    }
    System.out.println("Reduce Childs " + numChildReduce + "\t\t\tNoReduce " + numChildNoReduce);
  }

  @Test
  public void testLightHeavy(){
    ISPlayer light = new ISPlayer(0,100,0.7,0,true);
    ISPlayer heavy = new ISPlayer(1,100,0.7,0,true);
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
  public void testIterations(){
    //ISPlayer light07Reward0 = new ISPlayer(false,1000,0.7,0);
    //ISPlayer light07Reward0 = new ISPlayer(false,8000,0.7,0);
    RuleBasedPlayer rule = new RuleBasedPlayer();
    //Session game = new Session(light07Reward0,rule);
    //game.playGame();
    //System.out.println(game);
  }

  @Test
  public void testRewardStrategy(){
    ISPlayer strategy0 = new ISPlayer(0,1000,0.7,0,true);
    ISPlayer strategy2 = new ISPlayer(0,1000,0.7,2,true);
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
