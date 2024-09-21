import bagel.*;
import java.util.Properties;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2024
 * Please enter your name below
 * Piripi Martin
 */
public class ShadowTaxi extends AbstractGame {

    private final Properties GAME_PROPS;
    private final Properties MESSAGE_PROPS;
    private final GameState newGameState;
    private  Renders newRenders;
    private static final int HOME = 0;
    private static final int INFORMATION = 1;
    private static final int PLAYING = 2;
    private static final int END = 3;
    private GameScore finalGameScore;
    private Boolean win;



    public ShadowTaxi(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                messageProps.getProperty("home.title"));
        this.GAME_PROPS = gameProps;
        this.MESSAGE_PROPS = messageProps;
        this.newRenders = new Renders(gameProps,messageProps);
        this.newGameState = new GameState(HOME);
    }

    /**
     * Render the relevant screens and game objects based on the keyboard input
     * given by the user and the status of the game play.
     * @param input The current mouse/keyboard input.
     */

    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
        // this is given as an example, you may move/delete this line as you wish

        switch (this.newGameState.getState()){


            case HOME:

                if (input.wasPressed(Keys.ENTER)) { // if pressed enter on the home screen start info screen
                    // set state as info and break
                    newGameState.setState(INFORMATION);
                    break;
                }
                // otherwise render the home screen
                this.newRenders.renderHomeScreen();

                break;

            case INFORMATION:

                if (input.wasPressed(Keys.ENTER)) { // if pressed enter on the info screen start playing
                    // set state as playing and break
                    newGameState.setState(PLAYING);
                    break;
                }
                // otherwise render the information screen
                this.newRenders.renderInformationScreen(input);
                break;

            case PLAYING:
                // if the player has won
                if(newRenders.getNewEntityLoader().getNewGameScore().getTotalPay() >= 500){
                    newGameState.setState(END);
                    finalGameScore = newRenders.getNewEntityLoader().getNewGameScore();
                    win = true;

                    break;
                }

                // if the player has lost
                if(newRenders.getNewEntityLoader().getNewGameScore().getFramesRemaining() <= 0){
                    newGameState.setState(END);
                    finalGameScore = newRenders.getNewEntityLoader().getNewGameScore();
                    win = false;
                    break;
                }
                this.newRenders.renderPlayingScreen(input);
                break;

            case END:

                if (input.wasPressed(Keys.SPACE)) { // if pressed enter on the info screen start playing
                    // set state as playing and break
                    newGameState.setState(HOME);
                    resetGame();
                    break;
                }
                this.newRenders.renderEndScreen(input,win,finalGameScore);

        }


    }

    public void resetGame(){
        newRenders = new Renders(GAME_PROPS,MESSAGE_PROPS);
    }

    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowTaxi game = new ShadowTaxi(game_props, message_props);



        game.run();

    }
}
