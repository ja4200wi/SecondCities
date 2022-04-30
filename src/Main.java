import game.Session;
import player.ISPlayer;
import player.LikeHumanPlayer;
import player.Player;
import player.RandomPlayer;
import player.RuleBasedPlayer;
import player.TrulyRandomPlayer;

public class Main {

  public static void main(String[] args){
    ISPlayer rewardStrategy0 = new ISPlayer(0,250,1.5,2,false);
    ISPlayer rewardStrategy1 = new ISPlayer(0,250,2,2,false);
    ISPlayer rewardStrategy2 = new ISPlayer(0,250,3,2,false);
    ISPlayer playOut0 = new ISPlayer(0,250,0.6,0,false);
    ISPlayer playOut1 = new ISPlayer(1,250,0.6,0,false);
    ISPlayer playOut2 = new ISPlayer(2,250,0.6,0,false);
    ISPlayer mostPotential = new ISPlayer(1,10000,50,1,false);
    ISPlayer mixBestPotential = new ISPlayer(2,10000,50,1,true);
    RuleBasedPlayer rule = new RuleBasedPlayer();
    LikeHumanPlayer likeHuman = new LikeHumanPlayer();
    TrulyRandomPlayer random = new TrulyRandomPlayer();
    TrulyRandomPlayer random2 = new TrulyRandomPlayer();
    RandomPlayer quickRandom = new RandomPlayer();
    RandomPlayer quickRandom2 = new RandomPlayer();
    RuleBasedPlayer rule2 = new RuleBasedPlayer();
    LikeHumanPlayer likeHuman2 = new LikeHumanPlayer();
    Session game;
    int winsP1 = 0;
    int winsP2 = 0;
    int[] scores;
    double averageTurns = 0;
    double averageExp = 0;
    double averageDrawsFromDiscard = 0;
    double averageExpsP1 = 0;
    double averageExpsP2 = 0;
    int numSim = 100000;
    long begin = System.currentTimeMillis();
    for(int i = 0;i<numSim;i++){
      game = new Session(likeHuman,rule);
      scores = game.playGame();
      if(scores[0]>scores[1]) winsP1++;
      if(scores[1]>scores[0]) winsP2++;
      averageTurns += scores[2];
      averageExp += scores[3];
      averageDrawsFromDiscard += scores[4];
      averageExpsP1 += scores[5];
      averageExpsP2 += scores[6];
    }
    long end = System.currentTimeMillis();
    System.out.println("Wins P1 " + winsP1 + "\twins P2 " + winsP2 + "\tAVG Turns " + (averageTurns/numSim)
        + "\tAVG Exp " + (averageExp/numSim) + "\tAVG draws from discard pile " + (averageDrawsFromDiscard/numSim)
        +  "\tTime for 100.000 games " + (end-begin) + "\tAVG Expeditions started P1 " + (averageExpsP1/numSim)
        + "\tAVG Expeditions started P2 " + (averageExpsP2/numSim));
    Experiment experiment = new Experiment(mixBestPotential,mostPotential,2);
    Experiment experiment2 = new Experiment(playOut1,rule,1000);
    Experiment experiment3 = new Experiment(playOut2,rule,1000);
    //experiment.doExperiment(false);
    //experiment2.doExperiment(false);
    //experiment3.doExperiment(false);
  }

  public static class Experiment{

    int numberOfSimulations;
    Player p1;
    Player p2;

    public Experiment(Player p1, Player p2,int numberOfSimulations){
      this.numberOfSimulations = numberOfSimulations;
      this.p1 = p1;
      this.p2 = p2;
    }

    public double[] doExperiment(boolean print){
      Session game;
      int winsP1 = 0;
      int winsP2 = 0;
      int cumulativeScoreP1 = 0;
      int cumulativeScoreP2 = 0;
      double averageNumberOfTurns = 0;
      double averageNumberOfExpTurns = 0;
      double averageDrawsFromDiscard = 0;
      double averageExpsP1 = 0;
      double averageExpsP2 = 0;
      for(int i = 0;i<numberOfSimulations/2;i++){
        game = new Session(p1,p2);
        int[] results;
        if(print){
          results = game.playGameWithPrints();
        } else{
          results = game.playGame();
        }
        cumulativeScoreP1 += results[0];
        cumulativeScoreP2 += results[1];
        averageNumberOfTurns += results[2];
        averageNumberOfExpTurns += results[3];
        averageDrawsFromDiscard += results[4];
        averageExpsP1 += results[5];
        averageExpsP2 += results[6];
        if(results[0]>results[1]) winsP1++;
        if(results[1]>results[0]) winsP2++;
        System.out.println("\nWins P1: " + winsP1 + "\tWins P2: " + winsP2);
      }
      for(int i = 0;i<numberOfSimulations/2;i++){
        game = new Session(p2,p1);
        int[] results;
        if(print){
          results = game.playGameWithPrints();
        } else{
          results = game.playGame();
        }
        cumulativeScoreP1 += results[1];
        cumulativeScoreP2 += results[0];
        averageNumberOfTurns += results[2];
        averageNumberOfExpTurns += results[3];
        averageDrawsFromDiscard += results[4];
        averageExpsP1 += results[6];
        averageExpsP2 += results[5];
        if(results[0]>results[1]) winsP2++;
        if(results[1]>results[0]) winsP1++;
        System.out.println("\nWins P1 " + winsP1 + "\tWins P2 " + winsP2);
      }
      averageNumberOfExpTurns /= numberOfSimulations;
      averageNumberOfTurns /= numberOfSimulations;
      averageDrawsFromDiscard /= numberOfSimulations;
      averageExpsP1 /= numberOfSimulations;
      averageExpsP2 /= numberOfSimulations;
      System.out.println("Wins Player 1: " + winsP1 + "\tWins Player 2: " + winsP2 +
          "\nCumulative Score Player 1: " + cumulativeScoreP1 + "\tCumulative Score Player 2: " + cumulativeScoreP2 +
          "\nAverage Number of Turns: " + averageNumberOfTurns + "\tAverage Number of Expedition Plays: " + averageNumberOfExpTurns +
          "\tAverage Number of Expeditions started Player 1 " + averageExpsP1 + "\tAverage Number of Expeditions started Player 2 " + averageExpsP2 +
          "\nAverage Number of Draws from Discard " + averageDrawsFromDiscard);
      return new double[]{winsP1,winsP2,cumulativeScoreP1,cumulativeScoreP2,averageNumberOfTurns,averageNumberOfExpTurns};
    }
  }


}
