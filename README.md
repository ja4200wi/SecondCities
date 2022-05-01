# SecondCities

This repository holds all code to my bachelor thesis project.
There are two main tasks one can perform using this program:
1. Execute code using the Main class in the src folder
2. Start the JavaFX application and play Lost Cities using a preliminary GUI

---

### 1. Execute code

The easiest way to use the code is downloading the project as a .zip and loading it into your favorite IDE such as Eclipse or IntelliJ. 

Make sure to include javafx to used libraries.

The project is divided into 4 packages: game, gui, montecarlo, and player
The **game** package holds 3 classes: Card, Move, and Session
- Card and Move serve as objects during a game and allow comparisons creation and hold the information needed in the game in a minimalistic way.
- Session is the class representing one game and mainly provides the functionality of starting/playing a game of Lost Cities. To start a game two players are needed to initalize a valid instance of Session.

The **gui** package hold 3 classes and 2 fxml files: Controller, GameController, Main, gameBoard.fxml, and sample.fxml
- The Main class is executed to start the JavaFX application and opens a game menu controlled by the class Controller.
- The Controller is responsible for interactivity in the sample.fxml which represents the menu. Here a game can be started by clicking the corresponding buttons to choose the players. The relevant AI implementing MCTS needs to be selected after the selection of an AI as an opponent. The correct alternative is "ISMCTS-LikeHuman".
- The GameController is started when a game is opened in the menu window and is responsible for communication between GUI and the game.

The **montecarlo** package consists of 3 classes: InformationIS, MonteCarloIS, and NodeIS
- InformationIS collects all relevant informatio  a player can observe from a game and is used to create determinizations.
- MonteCarloIS implements the Monte Carlo Tree Search (MCTS) algorithm.
- NodeIS represents the nodes used for the MCTS algortihm and the tree built.

The **player** package holds 8 classes: *Player*, *MemoryPlayer*, TrulyRandomPlayer, RandomPlayer, RuleBasedPlayer, LikeHumanPlayer, ISPlayer, and HumanPlayer
- The Player and MemoryPlayer are two abstract classes responsible for two important methods all players need to implement: makeMove, and for MemoryPlayer resetMemory
- The other classes are different implementations of the makeMove method which returns moves to the game


---
### 2. Start the JavFX application

> **Refer to the HOW_TO_USE.pdf for information on how to use and communicate with the user interface.**

To start the JavaFX application use the SecondCities.jar and make sure to add important JavaFX modules javafx.base,javafx.controls,javafx.fxml. 
The necessary files can be downloaded  at https://gluonhq.com/products/javafx/. 
Then to start the application unzip the JavaFX SDK downloaded and place it next to the .jar.
Go to the directory holding these files:
  
   ->SecondCities.jar
   
   ->javafx-sdk-18.0.1
    
And using ```java --module-path "javafx-sdk-18.0.1/lib" --add-modules javafx.controls,javafx.fxml,javafx.base -jar SecondCities.jar``` should start the application.

Otherwise, you can download the corresponding installer, which installs LostCities as an application to your system. The download links can be found on https://ja4200wi.de/
    
   
