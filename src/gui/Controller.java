package gui;

import game.Session;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import player.HumanPlayer;
import player.ISPlayer;
import player.Player;
import player.RandomPlayer;
import player.RuleBasedPlayer;

public class Controller {

  Session game;

  ObservableList<String> ais = FXCollections.observableArrayList("Random","Rule-Based","ISMCTS");

  @FXML
  private ChoiceBox<String> AIselection1;

  @FXML
  private ChoiceBox<String> AIselection2;

  @FXML
  private Button player1AI;

  @FXML
  private Button player1Human;

  @FXML
  private TextField player1Name;

  @FXML
  private Button player2AI;

  @FXML
  private Button player2Human;

  @FXML
  private TextField player2Name;

  @FXML
  private Button startGameButton;

  public Session getGame() {
    return game;
  }

  @FXML
  private void initialize(){
    AIselection1.setItems(ais);
    AIselection2.setItems(ais);
  }

  @FXML
  void chooseAI1(){
    if(!player1Human.isDisabled()) {
      player1AI.setDisable(false);
      AIselection1.setDisable(false);
      player1Human.setDisable(true);
    } else {
      player1AI.setDisable(false);
      AIselection1.setDisable(false);
      player1Human.setDisable(false);
    }
  }

  @FXML
  void chooseAI2(){
    if(!player2Human.isDisabled()) {
      player2AI.setDisable(false);
      AIselection2.setDisable(false);
      player2Human.setDisable(true);
    } else {
      player2AI.setDisable(false);
      AIselection2.setDisable(false);
      player2Human.setDisable(false);
    }
  }

  @FXML
  void chooseHuman1() {
    if(!player1AI.isDisabled()) {
      player1Human.setDisable(false);
      player1AI.setDisable(true);
      AIselection1.setDisable(true);
    } else {
      player1Human.setDisable(false);
      player1AI.setDisable(false);
      AIselection1.setDisable(false);
    }
  }

  @FXML
  void chooseHuman2() {
    if(!player2AI.isDisabled()) {
      player2Human.setDisable(false);
      player2AI.setDisable(true);
      AIselection2.setDisable(true);
    } else {
      player2Human.setDisable(false);
      player2AI.setDisable(false);
      AIselection2.setDisable(false);
    }
  }


  public void startGame(javafx.event.ActionEvent actionEvent) throws IOException {
    boolean p1isAI = player1Human.isDisabled();
    boolean p2isAI = player2Human.isDisabled();
    String p1name = player1Name.getText();
    String p2name = player2Name.getText();
    Player one = createPlayer(p1isAI,p1name,1);
    Player two = createPlayer(p2isAI,p2name,2);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("gameBoard.fxml"));
    Parent root = loader.load();

    GameController gc = loader.getController();
    game = new Session(one,two);
    gc.setGame(game);

    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.setTitle("Lost Cities");
    stage.show();
    game.playGameWithGUI();
  }

  Player createPlayer(boolean isAI,String name,int p) {
    boolean isP1 = (p==1)? true:false;
    if(isAI) {
      String typeOfAI = (p==1)? AIselection1.getValue(): AIselection2.getValue();
      switch(typeOfAI) {
        case "Random":
          return new RandomPlayer();
        case "Rule-Based":
          return new RuleBasedPlayer();
        case "ISMCTS":
          return new ISPlayer(true,6000,0.7,0);
      }
    }
    else {
      return new HumanPlayer();
    }
    return null;
  }
}
