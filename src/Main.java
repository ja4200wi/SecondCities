import game.Session;
import player.ISPlayer;
import player.LikeHumanPlayer;
import player.Player;
import player.RandomPlayer;
import player.RuleBasedPlayer;
import player.TrulyRandomPlayer;

public class Main {

  public static void main(String[] args){
    testBasicAgents();
    tournamentBasicAgents();
    findExplorationConstant();
    findPlayoutStyle();
  }

  public static void testBasicAgents(){
    TrulyRandomPlayer trulyRandom = new TrulyRandomPlayer();
    RandomPlayer quickRandom = new RandomPlayer();
    RuleBasedPlayer quickRule = new RuleBasedPlayer();
    RuleBasedPlayer quickRule2 = new RuleBasedPlayer(); // if two QuickRule compete
    LikeHumanPlayer likeHuman = new LikeHumanPlayer();
    LikeHumanPlayer likeHuman2 = new LikeHumanPlayer(); //if two likeHumans compete
    Experiment trulyVStruly = new Experiment(trulyRandom,trulyRandom,100000);
    Experiment quickRandomVSquickRandom = new Experiment(quickRandom,quickRandom,100000);
    Experiment quickRuleVSquickRule = new Experiment(quickRule,quickRule2,100000);
    Experiment likeHumanVSlikeHuman = new Experiment(likeHuman,likeHuman2,100000);
    /**
     * The average number of turns and statistics are different to the results in the thesis
     * due to the fact that the gamOver flag was changed for this experiment to allow
     * games taking longer than 100 turns.
     */
    trulyVStruly.doExperiment(false);
    quickRandomVSquickRandom.doExperiment(false);
    quickRuleVSquickRule.doExperiment(false);
    likeHumanVSlikeHuman.doExperiment(false);
  }

  public static void tournamentBasicAgents(){
    TrulyRandomPlayer trulyRandom = new TrulyRandomPlayer();
    RandomPlayer quickRandom = new RandomPlayer();
    RuleBasedPlayer quickRule = new RuleBasedPlayer();
    RuleBasedPlayer quickRule2 = new RuleBasedPlayer(); // if two QuickRule compete
    LikeHumanPlayer likeHuman = new LikeHumanPlayer();
    LikeHumanPlayer likeHuman2 = new LikeHumanPlayer(); //if two likeHumans compete
    Experiment trulyVStruly = new Experiment(trulyRandom,trulyRandom,100000,"TrulyRandom vs TrulyRandom");
    Experiment trulyVSquickRandom = new Experiment(trulyRandom,quickRandom,100000,"Truly Random vs QuickRandom");
    Experiment trulyVSquickRule = new Experiment(trulyRandom,quickRule,100000,"TrulyRandom vs QuickRule");
    Experiment trulyVSlikeHuman = new Experiment(trulyRandom,likeHuman,100000,"TrulyRandom vs LikeHuman");
    Experiment quickRandomVSquickRandom = new Experiment(quickRandom,quickRandom,100000,"QuickRandom vs QuickRandom");
    Experiment quickRandomVSquickRule = new Experiment(quickRandom,quickRule,100000,"QuickRandom vs QuickRule");
    Experiment quickRandomVSlikeHuman = new Experiment(quickRandom,likeHuman,100000,"QuickRandom vs LikeHuman");
    Experiment quickRuleVSquickRule = new Experiment(quickRule,quickRule2,100000,"QuickRule vs QuickRule");
    Experiment quickRuleVSlikeHuman = new Experiment(quickRule,likeHuman,100000,"QuickRule vs LikeHuman");
    Experiment likeHumanVSlikeHuman = new Experiment(likeHuman,likeHuman2,100000,"LikeHuman vs LikeHuman");
    Experiment[] experiments = new Experiment[]{trulyVStruly,trulyVSquickRandom,trulyVSquickRule,
        trulyVSlikeHuman,quickRandomVSquickRandom,quickRandomVSquickRule,quickRandomVSlikeHuman,
        quickRuleVSquickRule,quickRuleVSlikeHuman,likeHumanVSlikeHuman};
    for(int i = 0;i<experiments.length;i++){
      experiments[i].doExperiment(false);
    }
  }

  public static void findExplorationConstant(){
    RuleBasedPlayer quickRule = new RuleBasedPlayer();
    ISPlayer awl0_3 = new ISPlayer(0,250,0.3,0,true);
    ISPlayer awl0_5 = new ISPlayer(0,250,0.5,0,true);
    ISPlayer awl0_7 = new ISPlayer(0,250,0.7,0,true);
    ISPlayer awl1_4 = new ISPlayer(0,250,1.4,0,true);
    ISPlayer rwl0_5 = new ISPlayer(0,250,0.5,2,true);
    ISPlayer rwl1_0 = new ISPlayer(0,250,1.0,2,true);
    ISPlayer rwl2_0 = new ISPlayer(0,250,2.0,2,true);
    ISPlayer rwl3_0 = new ISPlayer(0,250,3.0,2,true);
    ISPlayer sd_25 = new ISPlayer(0,250,25 ,1 ,true);
    ISPlayer sd_50 = new ISPlayer(0,250,50 ,1 ,true);
    ISPlayer sd_75 = new ISPlayer(0,250,75 ,1 ,true);
    ISPlayer sd100 = new ISPlayer(0,250,100,1 ,true);
    Experiment awl1 = new Experiment(awl0_3,quickRule,1000,"AWL with 0.3");
    Experiment awl2 = new Experiment(awl0_5,quickRule,1000,"AWL with 0.5");
    Experiment awl3 = new Experiment(awl0_7,quickRule,1000,"AWL with 0.7");
    Experiment awl4 = new Experiment(awl1_4,quickRule,1000,"AWL with 1.4");
    Experiment rwl1 = new Experiment(rwl0_5,quickRule,1000,"RWL with 0.5");
    Experiment rwl2 = new Experiment(rwl1_0,quickRule,1000,"RWL with 1.0");
    Experiment rwl3 = new Experiment(rwl2_0,quickRule,1000,"RWL with 2.0");
    Experiment rwl4 = new Experiment(rwl3_0,quickRule,1000,"RWL with 3.0");
    Experiment sd_1 = new Experiment(sd_25 ,quickRule,1000,"SD with 25");
    Experiment sd_2 = new Experiment(sd_50 ,quickRule,1000,"SD with 50");
    Experiment sd_3 = new Experiment(sd_75 ,quickRule,1000,"SD with 75");
    Experiment sd_4 = new Experiment(sd100 ,quickRule,1000,"SD with 100");
    Experiment[] experiments = new Experiment[]{awl1,awl2,awl3,awl4,rwl1,rwl2,rwl3,rwl4,sd_1,sd_2,sd_3,sd_4};
    for(int i = 0;i< experiments.length;i++){
      experiments[i].doExperiment(false);
    }
  }

  public static void findPlayoutStyle(){
    RuleBasedPlayer quickRule = new RuleBasedPlayer();
    ISPlayer ps_quickRandom = new ISPlayer(0,250,50,1,true);
    ISPlayer ps_quickRule = new ISPlayer(1,250,50,1,true);
    ISPlayer ps_likeHuman = new ISPlayer(2,250,50,1,true);
    ISPlayer ps_quickRule1sec = new ISPlayer(1,1000,50,1,true);
    ISPlayer ps_likeHuman1sec = new ISPlayer(2,1000,50,1,true);
    Experiment ps1 = new Experiment(ps_quickRandom,quickRule,1000,"Playout Style QuickRandom");
    Experiment ps2 = new Experiment(ps_quickRule,  quickRule,1000,"Playout Style QuickRule");
    Experiment ps3 = new Experiment(ps_likeHuman,  quickRule,1000,"Playout Style LikeHuman");
    Experiment psQuickRuleVSpsLikeHuman = new Experiment(ps_quickRule1sec,ps_likeHuman1sec,1000,"Playout QuickRule versus Playout LikeHuman");
    ps1.doExperiment(false);
    ps2.doExperiment(false);
    ps3.doExperiment(false);
    psQuickRuleVSpsLikeHuman.doExperiment(false);
  }


  /**
   * This class is used to perform experiments.
   */
  public static class Experiment {

    int numberOfSimulations;
    Player p1;
    Player p2;
    String name = "";

    public Experiment(Player p1, Player p2, int numberOfSimulations) {
      this.numberOfSimulations = numberOfSimulations;
      this.p1 = p1;
      this.p2 = p2;
    }

    public Experiment(Player p1, Player p2, int numberOfSimulations,String name) {
      this.numberOfSimulations = numberOfSimulations;
      this.p1 = p1;
      this.p2 = p2;
      this.name = name;
    }



    public double[] doExperiment(boolean print) {
      System.out.println("EXPERIMENT " + this.name);
      long start = System.currentTimeMillis();
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
      for (int i = 0; i < numberOfSimulations / 2; i++) {
        game = new Session(p1, p2);
        int[] results;
        if (print) {
          results = game.playGameWithPrints();
        } else {
          results = game.playGame();
        }
        cumulativeScoreP1 += results[0];
        cumulativeScoreP2 += results[1];
        averageNumberOfTurns += results[2];
        averageNumberOfExpTurns += results[3];
        averageDrawsFromDiscard += results[4];
        averageExpsP1 += results[5];
        averageExpsP2 += results[6];
        if (results[0] > results[1]) {
          winsP1++;
        }
        if (results[1] > results[0]) {
          winsP2++;
        }
        //System.out.println("\nWins P1: " + winsP1 + "\tWins P2: " + winsP2);
      }
      for (int i = 0; i < numberOfSimulations / 2; i++) {
        game = new Session(p2, p1);
        int[] results;
        if (print) {
          results = game.playGameWithPrints();
        } else {
          results = game.playGame();
        }
        cumulativeScoreP1 += results[1];
        cumulativeScoreP2 += results[0];
        averageNumberOfTurns += results[2];
        averageNumberOfExpTurns += results[3];
        averageDrawsFromDiscard += results[4];
        averageExpsP1 += results[6];
        averageExpsP2 += results[5];
        if (results[0] > results[1]) {
          winsP2++;
        }
        if (results[1] > results[0]) {
          winsP1++;
        }
        //System.out.println("\nWins P1 " + winsP1 + "\tWins P2 " + winsP2);
      }
      System.out.println("Time needed --> " + (System.currentTimeMillis()-start));
      averageNumberOfExpTurns /= numberOfSimulations;
      averageNumberOfTurns /= numberOfSimulations;
      averageDrawsFromDiscard /= numberOfSimulations;
      averageExpsP1 /= numberOfSimulations;
      averageExpsP2 /= numberOfSimulations;
      System.out.println("Wins Player 1: " + winsP1 + "\tWins Player 2: " + winsP2 +
          "\nCumulative Score Player 1: " + cumulativeScoreP1 + "\tCumulative Score Player 2: "
          + cumulativeScoreP2 +
          "\nAverage Number of Turns: " + averageNumberOfTurns
          + "\t\t\tAverage Number of Expedition Plays: " + averageNumberOfExpTurns +
          "\nAverage Number of Expeditions started Player 1 " + averageExpsP1
          + "\t\t\tAverage Number of Expeditions started Player 2 " + averageExpsP2 +
          "\nAverage Number of Draws from Discard " + averageDrawsFromDiscard);
      return new double[]{winsP1, winsP2, cumulativeScoreP1, cumulativeScoreP2,
          averageNumberOfTurns, averageNumberOfExpTurns};
    }
  }


}
