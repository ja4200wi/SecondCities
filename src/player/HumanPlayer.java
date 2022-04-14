package player;

import game.Card;
import game.Move;
import game.Session;
import java.util.Scanner;
import java.util.Stack;

public class HumanPlayer extends Player {

  public HumanPlayer(){super();}

  @Override
  public Move makeMove(Card[] myHand,Stack<Card>[] myExp,Stack<Card>[] oppExp,Stack<Card>[] discardPile,boolean turn) {
    int cardIndex;
    boolean onExp;
    int drawFrom;
    Scanner scanner = new Scanner(System.in);
    printHand(myHand);
    Player.printGameBoard(myExp,oppExp,discardPile);
    System.out.println("What card do you want to place? [1-8]");
    cardIndex = Integer.parseInt(scanner.nextLine()) - 1;
    System.out.println("Where do you want to place " + myHand[cardIndex]
        + "[E=Expedition, D=DiscardPile]");
    String expOrDisc = scanner.nextLine();
    onExp = (expOrDisc=="E" || expOrDisc=="");
    System.out.println("Where do you want to draw from? [0=drawStack, 1=yellow, 2=blue, 3=white, 4=green, 5=red]");
    drawFrom = scanner.nextInt();
    return new Move(cardIndex,onExp,drawFrom);
  }

  public void printHand(Card[] myHand){
    StringBuilder sb = new StringBuilder("My cards: ");
    for(Card c: myHand){
      sb.append(c);
      sb.append(", ");
    }
    System.out.println(sb);
  }

  @Override
  public Move makeMove(Session session) {
    return null;
  }
}
