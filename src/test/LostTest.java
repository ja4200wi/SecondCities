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
import player.ISPlayer;
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
  public void testMethods(){
    Stack<Card> stack = createTestCards(); //Card deck
    Card[] myHand = new Card[8];
    for(int i = 0;i<8;i++) myHand[i] = stack.pop(); //Fill my Hand
    Stack<Card> example = new Stack<>();
    example.add(new Card(0,5));
    Stack<Card>[] myExp = new Stack[]{example, new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    Stack<Card>[] oppExp = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    Stack<Card>[] discard = new Stack[]{new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>(), new Stack<Card>()};
    //Above create empty game board representations
    InformationIS information = new InformationIS(myHand,myExp,oppExp,discard,true,true);
    Session det = information.createDeterminization();
    //@TODO: Assert.assertEquals(43,det.getCardsLeft());
    NodeIS nodeWithOneChild = new NodeIS();
    nodeWithOneChild.addChild(new Move(0,true,0));
    ArrayList<Move> moves = MonteCarloIS.movesFromDetNotInNodeChildren(nodeWithOneChild,det);
    //@TODO: Assert.assertEquals(15,moves.size()); here again determine exactly Cards
    MonteCarloIS mcIS = new MonteCarloIS();
    Pair<NodeIS,Session> pair;
    pair = mcIS.select(new NodeIS(),det);
    System.out.println("DEBUG"); //@TODO: Assert
    pair = mcIS.expand(nodeWithOneChild,det);
    System.out.println("DEBUG"); //@TODO: Assert
  }

  @Test
  public void testSpeed(){
    RuleBasedPlayer rule = new RuleBasedPlayer();
    RuleBasedPlayer rule2 = new RuleBasedPlayer();
    RandomPlayer random = new RandomPlayer();
    RandomPlayer random2 = new RandomPlayer();
    CheatingMCTSPlayer light = new CheatingMCTSPlayer(false,3000);
    CheatingMCTSPlayer heavy = new CheatingMCTSPlayer(true,3000);
    ISPlayer dumb = new ISPlayer(false,1000);
    ISPlayer smart = new ISPlayer(true,1000);
    int winsP1 = 0;
    for(int i = 0;i<10;i++){
      Session game = new Session(smart,random);
      int [] scores = game.playGame();
      if(scores[0]>scores[1]) winsP1++;
      System.out.println(game);
    }
    System.out.println("Wins Player 1: " + winsP1);
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

}
