package gui;

import game.Card;
import game.Move;
import game.Session;
import java.util.Stack;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import player.Player;

/**
 * @author Jann Winter
 * This class is responsible for the whole GUI while playing a game of Lost Cities.
 */
public class GameController {

  Session game;
  int selectedIndex = -1;
  boolean cardPlaced;
  Move move = new Move();

  @FXML
  private Label remainingCards;

  @FXML
  private VBox expYellow1;

  @FXML
  private VBox expYellow2;

  @FXML
  private VBox expBlue1;

  @FXML
  private VBox expBlue2;

  @FXML
  private VBox expWhite1;

  @FXML
  private VBox expWhite2;

  @FXML
  private VBox expGreen1;

  @FXML
  private VBox expGreen2;

  @FXML
  private VBox expRed1;

  @FXML
  private VBox expRed2;

  @FXML
  private Button discardYellowButton1;

  @FXML
  private Button discardBlueButton2;

  @FXML
  private Button discardWhiteButton3;

  @FXML
  private Button discardGreenButton4;

  @FXML
  private Button discardRedButton5;

  @FXML
  private Label player1Label;

  @FXML
  private Label player1Score;

  @FXML
  private Label player2Label;

  @FXML
  private Label player2Score;

  @FXML
  private Button drawDeck;

  @FXML
  private Button p1Card1;

  @FXML
  private Button p1Card2;

  @FXML
  private Button p1Card4;

  @FXML
  private Button p1Card7;

  @FXML
  private Button p1Card6;

  @FXML
  private Button p1Card5;

  @FXML
  private Button p1Card3;

  @FXML
  private Button p1Card8;

  @FXML
  private Button p2Card1;

  @FXML
  private Button p2Card2;

  @FXML
  private Button p2Card4;

  @FXML
  private Button p2Card7;

  @FXML
  private Button p2Card6;

  @FXML
  private Button p2Card5;

  @FXML
  private Button p2Card3;

  @FXML
  private Button p2Card8;

  @FXML
  private Button expButton;

  /**
   * This method is called when the player presses a button representing a card.
   * It sets the card selected index or if already selected it recerts this.
   * @param actionEvent is the event triggering the method
   */
  @FXML
  void selectCard(javafx.event.ActionEvent actionEvent){
    if(selectedIndex==-1) {
      Button b = (Button) actionEvent.getSource();
      selectedIndex = Integer.parseInt(b.getId().substring(b.getId().length() - 1));
      move.setCardIndex(selectedIndex - 1);
    } else {
      selectedIndex = -1;
    }
    updateScreen();
  }

  /**
   * This method is called when the user presses the button to place a card on his expedition.
   * It checks whether the player has selected a card and then initiates the placement.
   * @param actionEvent is the event triggering the method
   */
  @FXML
  void putOnExpedition(javafx.event.ActionEvent actionEvent){
    if(selectedIndex!=-1){
      int player = (game.isTurn())?0:1;
      Card card = game.getHandAtTurn()[move.getCardIndex()];
      Stack<Card> expedition = game.getExpeditions()[player][card.getColor()];
      if(Player.addCardPossible(expedition,card,card.getColor())){
        move.setOnExp(true);
        cardPlaced=true;
        updateScreen();
      } else {
        Alert alarm = new Alert(AlertType.INFORMATION);
        alarm.setContentText("Card can't be placed onto expedition!");
        alarm.show();
      }
    } else {
      Alert alarm = new Alert(AlertType.INFORMATION);
      alarm.setContentText("No Card is selected!");
      alarm.show();
    }
  }

  /**
   * This method is called when the user presses the discard piles. The method first checks whether
   * the player has selected a card, then based on if a card was placed this method will initiate
   * a draw from the discardPile selected or initiate a placement of the selected card on the
   * correct discard pile.
   * @param actionEvent is the event triggering the method
   */
  @FXML
  void putOnDiscardPile(javafx.event.ActionEvent actionEvent){
    if(selectedIndex==-1) {
      Alert alarm = new Alert(AlertType.INFORMATION);
      alarm.setContentText("No Card is selected!");
      alarm.show();
    }
    if(cardPlaced){
      int colorPlaced = game.getHandAtTurn()[move.getCardIndex()].getColor();
      Button b = (Button) actionEvent.getSource();
      int colorWantToDraw = Integer.parseInt(b.getId().substring(b.getId().length()-1));
      if(colorPlaced+1==colorWantToDraw && !move.isOnExp()) {
        Alert alarm = new Alert(AlertType.INFORMATION);
        alarm.setContentText("Can't draw card placed in same turn!");
        alarm.show();
      } else {
        if(game.getDiscardPile()[colorWantToDraw-1].isEmpty()){
          Alert alarm = new Alert(AlertType.INFORMATION);
          alarm.setContentText("Discard pile empty!");
          alarm.show();
        }
        move.setDrawFrom(colorWantToDraw);
        game.executeMove(move);
        resetMove();
        game.playGameWithGUI();
      }
    } else {
      move.setOnExp(false);
      cardPlaced = true;
    }
    updateScreen();
  }

  /**
   * This method is called when the user wants do draw a card from the draw stack.
   * It checks whether the player has already selected a card to place and has also chosen
   * where to place it.
   * @param actionEvent is the event triggering the method
   */
  @FXML
  void drawFromDeck(javafx.event.ActionEvent actionEvent){
     if(move.getCardIndex()!=-1 && cardPlaced) {
       move.setDrawFrom(0);
       game.executeMove(move);
       resetMove();
       game.playGameWithGUI();
    } else {
      Alert alarm = new Alert(AlertType.INFORMATION);
      alarm.setContentText("No Card was placed!");
      alarm.show();
    }
    updateScreen();
  }

  /**
   * This method is called each time a player has finished his move and
   * the values for the current move should be reset to default
   */
  public void resetMove(){
    move.setCardIndex(-1);
    move.setDrawFrom(-1);
    move.setOnExp(false);
    cardPlaced=false;
    selectedIndex = -1;
  }

  /**
   * This method is a setter and uses information given to set labels of players and their scores.
   * @param game is the game session handled by this controller
   */
  public void setGame(Session game) {
    this.game = game;
  }
  /**
   * This method is called to give the player information about the current game state.
   */
  public void updateScreen(){
    Card[]hand1 = game.getPlayerHand(true);
    Card[]hand2 = game.getPlayerHand(false);
    Button [] p1Buttons = {p1Card1,p1Card2,p1Card3,p1Card4,p1Card5,p1Card6,p1Card7,p1Card8};
    Button [] p2Buttons = {p2Card1,p2Card2,p2Card3,p2Card4,p2Card5,p2Card6,p2Card7,p2Card8};
    Button [] discardButtons = {discardYellowButton1,discardBlueButton2,discardWhiteButton3,discardGreenButton4,discardRedButton5};

    /** Update Expeditions */
    expYellow1.getChildren().clear();
    expYellow2.getChildren().clear();
    expBlue1.getChildren().clear();
    expBlue2.getChildren().clear();
    expWhite1.getChildren().clear();
    expWhite2.getChildren().clear();
    expGreen1.getChildren().clear();
    expGreen2.getChildren().clear();
    expRed1.getChildren().clear();
    expRed2.getChildren().clear();
    for(int i = 0;i<5;i++){
      for(Card c : game.getExpeditions()[0][i]) {
        Label label = new Label(c.getShort());
        label.setStyle("-fx-pref-height: 20;-fx-pref-width: 125;-fx-font-weight: bold;-fx-font-size: 14");
        switch (i) {
          case 0:
            expYellow1.getChildren().add(label);
            break;
          case 1:
            expBlue1.getChildren().add(label);
            break;
          case 2:
            expWhite1.getChildren().add(label);
            break;
          case 3:
            expGreen1.getChildren().add(label);
            break;
          case 4:
            expRed1.getChildren().add(label);
            break;
        }
      }
      for(Card c : game.getExpeditions()[1][i]) {
        Label label = new Label(c.getShort());
        label.setStyle("-fx-pref-height: 20;-fx-pref-width: 125;-fx-font-weight: bold;-fx-font-size: 14");
        switch (i) {
          case 0:
            expYellow2.getChildren().add(label);
            break;
          case 1:
            expBlue2.getChildren().add(label);
            break;
          case 2:
            expWhite2.getChildren().add(label);
            break;
          case 3:
            expGreen2.getChildren().add(label);
            break;
          case 4:
            expRed2.getChildren().add(label);
            break;
        }
      }
    }

    /** Update player scores */
    player1Score.setText("Score: " + game.calcPointsPlayer(0));
    player2Score.setText("Score: " + game.calcPointsPlayer(1));

    /** Update cards on hand */
    for(int i = 0;i<8;i++){
      String p1Button = (hand1[i]==null)?"":hand1[i].getShort();
      String p2Button = (hand2[i]==null)?"":hand2[i].getShort();
      int p1Color = (hand1[i]==null)?0:hand1[i].getColor();
      int p2Color = (hand2[i]==null)?0:hand2[i].getColor();

      setButtonText(p1Buttons[i],p1Button);
      setButtonColor(p1Buttons[i],p1Color);
      setButtonText(p2Buttons[i],p2Button);
      setButtonColor(p2Buttons[i],p2Color);
    }

    if(selectedIndex!=-1) {
      if(game.isTurn()){
        setButtonText(p1Buttons[selectedIndex-1],"");
        setButtonColor(p1Buttons[selectedIndex-1],-1);
      } else {
        setButtonText(p2Buttons[selectedIndex-1],"");
        setButtonColor(p2Buttons[selectedIndex-1],-1);
      }
    }

    /** Update discard piles*/
    String yellowDiscard = (game.getDiscardPile()[0].isEmpty())?"":game.getDiscardPile()[0].peek().getShortWithoutColor() + "";
    String blueDiscard = (game.getDiscardPile()[1].isEmpty())?"":game.getDiscardPile()[1].peek().getShortWithoutColor() + "";
    String whiteDiscard = (game.getDiscardPile()[2].isEmpty())?"":game.getDiscardPile()[2].peek().getShortWithoutColor() + "";
    String greenDiscard = (game.getDiscardPile()[3].isEmpty())?"":game.getDiscardPile()[3].peek().getShortWithoutColor() + "";
    String redDiscard = (game.getDiscardPile()[4].isEmpty())?"":game.getDiscardPile()[4].peek().getShortWithoutColor() + "";
    discardButtons[0].setText(yellowDiscard);
    discardButtons[1].setText(blueDiscard);
    discardButtons[2].setText(whiteDiscard);
    discardButtons[3].setText(greenDiscard);
    discardButtons[4].setText(redDiscard);

    /** Only show cards of player at turn*/
    if(game.isTurn()){
      for(Button b : p2Buttons){
        b.setDisable(true);
        b.setVisible(false);
      }
      for(Button b : p1Buttons){
        b.setDisable(false);
        b.setVisible(true);
      }
    } else {
      for(Button b : p1Buttons) {
        b.setDisable(true);
        b.setVisible(false);
      }
      for(Button b : p2Buttons) {
        b.setDisable(false);
        b.setVisible(true);
      }
    }
    /** Make sure AI's cards are never shown. Might be redundant */
    remainingCards.setText("Remaining Cards: " + game.getNumberCardsLeft());

  }

  /**
   * This method sets the color of a button to desired color
   * @param b is the button to color
   * @param color is the desired color
   */
  public void setButtonColor(Button b,int color){
    switch (color) {
      case 0:
        b.setStyle("-fx-background-color:  #f1c40f;");
        break;
      case 1:
        b.setStyle("-fx-background-color:  #2980b9;");
        break;
      case 2:
        b.setStyle("-fx-background-color: #ffffff;");
        b.setStyle("-fx-text-fill: #000000");
        break;
      case 3:
        b.setStyle("-fx-background-color: #27ae60;");
        break;
      case 4:
        b.setStyle("-fx-background-color:  #c0392b;");
        break;
      default:
        b.setStyle("-fx-background-color: #000000");
    }
  }

  /**
   * This method sets the color of a button to desired text
   * @param b is the button to give new text
   * @param s is the new text
   */
  public void setButtonText(Button b,String s){
    b.setText(s);
  }
}
