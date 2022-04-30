package game;

/**
 * @author Jann Winter
 * This class is used to represent a Card. Cards have 5 different colors and can be either
 * a Card with numbers from 2 to 10 or a CoinCard.
 */

public class Card implements Cloneable{

  boolean coinCard;
  int color; // 0 = yellow; 1 = blue; 2 = white, 3 = green, 4 = red
  int value; // -1,0,1 = coinCard

  /**
   * Constructor for creating a new Card.
   * @param color  int value for the color 0=yellow,1=blue,2=white,3=green,4=red
   * @param value Number on the Card. For CoinCards between -1 and 1
   */
  public Card(int color,int value){
    this.color = color;
    this.value = value;
    this.coinCard = (value<2);
  }

  /**
   * This method is used for String representation of colors and values of the card.
   * @return string indidcating its color and value
   */
  public String getShort(){
    String s = "";
    if(this.getValue()>1) s += this.getColorChar() + " " + this.getValue();
    if(this.getValue()<2) s += this.getColorChar() + " " + "CC";
    return s;
  }

  /**
   * This method is used to simply get the value of a card as a String
   * @return a STring holding the value of a card
   */
  public String getShortWithoutColor(){
    String s = "";
    if(this.getValue()>1) s += + this.getValue();
    if(this.getValue()<2) s += "CC";
    return s;
  }

  /**
   * This method returns the start letter of the color of a card
   * @return
   */
  public char getColorChar(){
    switch (color) {
      case 0:
        return 'Y';
      case 1:
        return 'B';
      case 2:
        return 'W';
      case 3:
        return 'G';
      case 4:
        return 'R';
    }
    return 'X';
  }

  /** Get methods */
  public int getColor() {
    return color;
  }

  public int getValue() {
    return value;
  }

  public boolean isCoinCard() {
    return coinCard;
  }

  @Override
  public Card clone(){
    return new Card(color,value);
  }

  @Override
  public String toString() {
    String colorName = "";
    switch(this.color) {
      case 0:
        colorName = "Y";
        break;
      case 1:
        colorName = "B";
        break;
      case 2:
        colorName = "W";
        break;
      case 3:
        colorName = "G";
        break;
      case 4:
        colorName = "R";
        break;
    }
    if(coinCard) return  "{" + "CC" + "," + colorName + '}';
    return "{" + colorName + "," + value + '}';
  }

  @Override
  public boolean equals(Object o) {
    Card c = (Card) o;
    if(value!=c.getValue()) {
      return false;
    }
    if(color!=c.getColor()) {
      return false;
    }
    return true;
  }
}
