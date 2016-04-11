package core.player;

import core.game.Game;
import core.game.StateObservation;
import core.game.StateObservationMulti;
import ontology.Types;
import tools.ElapsedCpuTimer;
import tools.KeyHandler;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Raluca on 07-Apr-16.
 */
public abstract class Player implements Cloneable {

    /**
     * Variable to store player id.
     * Default 0 for single player games.
     */
    private int playerID = 0;

    private double score = 0.0;

    private Types.WINNER winState = Types.WINNER.NO_WINNER;

    /**
     * Disqualified flag, moved from Game class to individual players,
     * as there may be more than 1 in a game; variable still in Game
     * class for single player games to keep back-compatibility
     */
    protected boolean is_disqualified;

    /**
     * File where the actions played in a given game are stored.
     */
    private String actionFile;

    /**
     * Writer for the actions file.
     */
    private BufferedWriter writer;

    /**
     * Set this variable to FALSE to avoid logging the actions to a file.
     */
    private static final boolean SHOULD_LOG = true;

    /**
     * Last action executed by this agent.
     */
    private Types.ACTIONS lastAction = null;

    /**
     * List of actions to be dumped.
     */
    private ArrayList<Types.ACTIONS> allActions;

    /**
     * Random seed of the game.
     */
    private int randomSeed;

    /**
     * Picks an action. This function is called every game step to request an
     * action from the player. The action returned must be contained in the
     * actions accessible from stateObs.getAvailableActions(), or no action
     * will be applied.
     * Single Player method.
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    public abstract Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer);


    /**
     * Picks an action. This function is called every game step to request an
     * action from the player. The action returned must be contained in the
     * actions accessible from stateObs.getAvailableActions(), or no action
     * will be applied.
     * Multi player method.
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    public abstract Types.ACTIONS act(StateObservationMulti stateObs, ElapsedCpuTimer elapsedTimer);

    /**
     * Function called when the game is over. This method must finish before CompetitionParameters.TEAR_DOWN_TIME,
     *  or the agent will be DISQUALIFIED
     * @param stateObservation the game state at the end of the game
     * @param elapsedCpuTimer timer when this method is meant to finish.
     */
    public void result(StateObservation stateObservation, ElapsedCpuTimer elapsedCpuTimer)
    {
    }


    /**
     * This function sets up the controller to save the actions executed in a given game.
     * @param actionFile file to save the actions to.
     * @param randomSeed Seed for the sampleRandom generator of the game to be played.
     */
    public void setup(String actionFile, int randomSeed) {
        this.actionFile = actionFile;
        this.randomSeed = randomSeed;

        if(this.actionFile!=null && SHOULD_LOG)
        {
            allActions = new ArrayList<>();
        }
    }

    /**
     * Closes the agent, writing actions to file.
     */
    final public void teardown(Game played) {
        try {
            if((this.actionFile != null && !actionFile.equals("")) && SHOULD_LOG) {
                writer = new BufferedWriter(new FileWriter(new File(this.actionFile)));
                writer.write(randomSeed +
                        " " + (played.getWinner() == Types.WINNER.PLAYER_WINS ? 1 : 0) +
                        " " + played.getScore() + " " + played.getGameTick() + "\n");

                for(Types.ACTIONS act : allActions)
                    writer.write(act.toString() + "\n");

                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs a single action
     * @param action the action to log.
     */
    final public void logAction(Types.ACTIONS action) {

        lastAction = action;
        if(this.actionFile!=null && SHOULD_LOG)
        {
            allActions.add(action);
        }

    }

    /**
     * Gets the last action executed by this controller.
     * @return the last action
     */
    public Types.ACTIONS getLastAction()
    {
        return lastAction;
    }

    /**
     * Get this player's ID.
     * @return player ID.
     */
    public int getPlayerID() {
        return playerID;
    }


    /**
     * Checks whether this player is disqualified.
     * @return true if disqualified, false otherwise.
     */
    public boolean is_disqualified() {
        return is_disqualified;
    }

    /**
     * Sets the disqualified flag.
     */
    public void disqualify(boolean is_disqualified) { this.is_disqualified = is_disqualified; }

    /**
     * Gets the score of this player.
     * @return score.
     */
    public double getScore() { return score; }

    /**
     * Sets the score of this player to a new value.
     * @param s - new score.
     */
    public void setScore(double s) { score = s; }

    /**
     * Adds a value to the current score of this player.
     * @param s - value to add to the score.
     */
    public void addScore (double s) { score += s; }

    /**
     * Gets the win state of this player.
     * @return - win state, value of Types.WINNER
     */
    public Types.WINNER getWinState() { return winState; }

    /**
     * Sets the win state of this player.
     * @param w - new win state.
     */
    public void setWinState(Types.WINNER w) { winState = w; }

    /**
     * Method to copy the player object into a new one.
     * @return new Player object with the same attribute values.
     */
    public Player copy() {
        try {
            return (Player) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the player the control to draw something on the screen.
     * It can be used for debug purposes.
     * @param g Graphics device to draw to.
     */
    public void draw(Graphics2D g)
    {
        //Overwrite this method in your controller to draw on the screen.
        //This method should be left empty in this class.
    }
}