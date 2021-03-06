package core.competition;

import core.ArcadeMachine;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Diego
 * Date: 04/10/13
 * Time: 16:29
 * This is a Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class AgentExecutorMulti {

    public static void main(String[] args) {

        String game = args[0];
        String level = args[1];
        String playerClassString0 = args[2];
        String playerClassString1 = args[3];
        String action_file = args[4];

        String players = playerClassString0 + " " + playerClassString1;
        String playersSwapped = playerClassString0 + " " + playerClassString1;

        System.out.println("Game: " + game);
        System.out.println("Level: " + level);
        System.out.println("Player 0 Class: " + playerClassString0);
        System.out.println("Player 1 Class: " + playerClassString1);
        System.out.println("Action File: " + action_file );

        int seed = new Random().nextInt();

        /**
         * Play 2 games, reversing the order of the players in the second one.
         */
        double[] gameScore = ArcadeMachine.runOneGame(game, level, false, players, action_file, seed, 0);
        double[] gameScoreSwapped = ArcadeMachine.runOneGame(game, level, false, playersSwapped, action_file + "_swapped", seed, 0);

        //System.out.println(gameScore);
        //System.out.println(gameScoreSwapped);
    }
}
