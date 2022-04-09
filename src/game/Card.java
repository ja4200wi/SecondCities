package game;

public class Card implements Cloneable{

  boolean coinCard;
  int color; // 0=yellow; 1=blue; 2=white, 3=green, 4=red
  int value; // 0==coinCard

  public Card(int color,int value){
    this.color = color;
    this.value = value;
    this.coinCard = (value<2);
  }

  public int compareTo(Card c) {
    if(value!=c.getValue()) {
      return -1;
    }
    if(color!=c.getColor()) {
      return -1;
    }
    return 1;
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
}
