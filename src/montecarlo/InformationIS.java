package montecarlo;

import game.Card;
import game.Session;
import java.util.ArrayList;
import java.util.Stack;

public class InformationIS {

  ArrayList<Card> observedCards;
  boolean turn;

  public InformationIS(Card[] hand,Stack<Card>[] myExp,Stack<Card>[] oppExp,Stack<Card>[] discardPile){
    observedCards = new ArrayList<>();
    for(Stack<Card> stack : myExp){
      observedCards.addAll(stack);
    }
    for(Stack<Card> stack : oppExp){
      observedCards.addAll(stack);
    }
    for(Stack<Card> stack : discardPile){
      observedCards.addAll(stack);
    }
    for(int i = 0;i<8;i++){
      observedCards.add(hand[i]);
    }
  }

  public Session chooseDeterminization(){

  }

}
