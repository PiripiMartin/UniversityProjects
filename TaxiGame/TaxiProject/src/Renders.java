import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
public class Renders {

    //import helper classes
    private final MiscUtils newMiscUtils;
    private final IOUtils newIOUtils;
    private  final Properties newGameProperties;
    private  final Properties newMessageProperties;
    private final Player newPlayer;
    private final EntityLoader newEntityLoader;


    // definitions for home screen
    private final Image backgroundImage;  //  home screen
    private final Font titleFont;   // home screen font
    private final Font instructionFont;


    //definitions for information screen

    private final Image informationImage;
    private final Font informationFont;


    // definitions for the playing screen

    private final Image playingBackground1;
    private final Image playingBackground2;
    private Point background1Position;
    private Point background2Position;

    // definitions for the end screen
    private final Image endBackgroundImage;
    private final Font gameEndScoresFont;
    private final Font gameEndStatusFont;
    private boolean addedName;





    public Renders(Properties gameProps, Properties messageProps) {
        // define helper classes
        this.newMiscUtils = new MiscUtils();
        this.newIOUtils = new IOUtils();
        this.newGameProperties = gameProps;
        this.newMessageProperties = messageProps;
        this.newPlayer = new Player("");
        this.newEntityLoader = new EntityLoader(gameProps.getProperty("gamePlay.objectsFile"),gameProps,messageProps);



        // all definitions for home screen
        this.backgroundImage = new Image(newGameProperties.getProperty("backgroundImage.home"));
        this.titleFont = new Font(newGameProperties.getProperty("font"), Integer.parseInt(newGameProperties.getProperty("home.title.fontSize")));
        this.instructionFont = new Font(newGameProperties.getProperty("font"),Integer.parseInt(newGameProperties.getProperty("home.instruction.fontSize")));

        // all definitions for information screen
        this.informationImage = new Image(newGameProperties.getProperty("backgroundImage.playerInfo"));
        this.informationFont = new Font(newGameProperties.getProperty("font"), Integer.parseInt(newGameProperties.getProperty("playerInfo.fontSize")));

        // all definitions for playing screen
        this.playingBackground1 = new Image(newGameProperties.getProperty("backgroundImage"));
        this.playingBackground2 = new Image(newGameProperties.getProperty("backgroundImage"));

        this.background1Position = new Point(512,384);
        this.background2Position = new Point(512,-384);

        // all definitions for end screen
        this.endBackgroundImage = new Image(newGameProperties.getProperty("backgroundImage.gameEnd"));
        this.gameEndScoresFont =  new Font(newGameProperties.getProperty("font"), Integer.parseInt(newGameProperties.getProperty("gameEnd.scores.fontSize")));
        this.gameEndStatusFont =  new Font(newGameProperties.getProperty("font"), Integer.parseInt(newGameProperties.getProperty("gameEnd.status.fontSize")));
        this.addedName = false;





    }
    public void renderHomeScreen() {


        double titleTextWidth = titleFont.getWidth(newMessageProperties.getProperty("home.title")); // getting the width to correctly center title
        double instructionTextWidth = instructionFont.getWidth(newMessageProperties.getProperty("home.instruction")); // getting the width to correctly center instruction
        // drawing the green background
        backgroundImage.draw((double)Window.getWidth()/2,(double)Window.getHeight()/2);
        // Drawing the title
        titleFont.drawString(newMessageProperties.getProperty("home.title"),(Window.getWidth()-titleTextWidth)/2,Integer.parseInt(newGameProperties.getProperty("home.title.y")));
        // drawing the instruction
        instructionFont.drawString(newMessageProperties.getProperty("home.instruction"),(Window.getWidth()-instructionTextWidth)/2,Integer.parseInt(newGameProperties.getProperty("home.instruction.y")));


    }

    public void renderInformationScreen(Input input) {

        DrawOptions options = new DrawOptions();

        // if player wants to delete one of the letters
        if((input.wasPressed(Keys.BACKSPACE)||input.wasPressed(Keys.DELETE))&& !Objects.equals(newPlayer.getPlayerName(), "")){
            newPlayer.setPlayerName(newPlayer.getPlayerName().substring(0, newPlayer.getPlayerName().length() - 1));
        }

        // if the user presses key change the player name
        if(newMiscUtils.getKeyPress(input) != null){
            newPlayer.setPlayerName(newPlayer.getPlayerName()+newMiscUtils.getKeyPress(input));
        }
        informationImage.draw((double) Window.getWidth()/2, (double) Window.getHeight() /2);
        // getting the width to correctly center enter name prompt
        double enterNameWidth = informationFont.getWidth(newMessageProperties.getProperty("playerInfo.playerName"));
        // Drawing ENTER YOUR NAME
        informationFont.drawString(newMessageProperties.getProperty("playerInfo.playerName"),(Window.getWidth()-enterNameWidth)/2,200);
        //rendering the player name
        informationFont.drawString(newPlayer.getPlayerName(),(Window.getWidth()-informationFont.getWidth(newPlayer.getPlayerName()))/2,Integer.parseInt(newGameProperties.getProperty("playerInfo.playerNameInput.y")),options.setBlendColour(Colour.BLACK));
        // instruction message
        informationFont.drawString(newMessageProperties.getProperty("playerInfo.start"),(Window.getWidth()-informationFont.getWidth(newMessageProperties.getProperty("playerInfo.start")))/2,Integer.parseInt(newGameProperties.getProperty("playerInfo.start.y")));

    }


    public void renderPlayingScreen(Input input) {



        // backround image resetting
        if(background1Position.y >= 1152){
            background1Position = new Point (background2Position.x,background2Position.y - Window.getHeight());

        }
        if(background2Position.y >= 1152){
            background2Position = new Point (background1Position.x,background1Position.y - Window.getHeight());
        }


        // placing the backrounds in their positions
        playingBackground1.draw(background1Position.x,background1Position.y);
        playingBackground2.draw(background2Position.x, background2Position.y);

        // Simulation of movement
        if(input.isDown(Keys.UP)){
            newEntityLoader.EntityMove();
            background1Position = new Point(background1Position.x,background1Position.y + Integer.parseInt(newGameProperties.getProperty("gameObjects.taxi.speedY")));
            background2Position = new Point(background2Position.x,background2Position.y + Integer.parseInt(newGameProperties.getProperty("gameObjects.taxi.speedY")));
        }
        if(input.isDown(Keys.LEFT)){
            newEntityLoader.TaxiLeft();
        }
        if(input.isDown(Keys.RIGHT)){
            newEntityLoader.TaxiRight();
        }
        // if car is still check if you can pick up passengers, or if it's time to drop off passengers
        if(!input.isDown(Keys.UP) && !input.isDown(Keys.LEFT)&&!input.isDown(Keys.RIGHT)){
            newEntityLoader.checkForPickup();
            newEntityLoader.checkForDropoff();

        }
        newEntityLoader.checkForCoin();
        // drawing the pay/target/framesremaining
        newEntityLoader.getNewGameScore().draw();
        //updating the frames remaining
        newEntityLoader.getNewGameScore().setFramesRemaining(newEntityLoader.getNewGameScore().getFramesRemaining()-1);


        // drawing the current trip
        if(newEntityLoader.getTaxis().get(0).getHasPassenger()){
           newEntityLoader.getTaxis().get(0).getCurrentTrip().draw();
        }
        else if(newEntityLoader.getTaxis().get(0).getHasLastTrip()){
            newEntityLoader.getTaxis().get(0).getLastTrip().draw();
        }

        // placing the entities in their positions
        newEntityLoader.EntityDraw();

        newEntityLoader.PassengerWalkingAway();
    }





    public void renderEndScreen(Input input, Boolean outcome, GameScore gameScore){

        // Only add the score to the list of highscores once
        if(!addedName) {
            newIOUtils.writeLineToFile(newGameProperties.getProperty("gameEnd.scoresFile"), newPlayer.getPlayerName() + "," + gameScore.getTotalPay());
            addedName = true;
        }
        endBackgroundImage.draw((double)Window.getWidth()/2,(double)Window.getHeight()/2);

        // if the player won, display the win message
        if(outcome){
            double outcomeTextWidth = gameEndStatusFont.getWidth(newMessageProperties.getProperty("gameEnd.won"));
            gameEndStatusFont.drawString(newMessageProperties.getProperty("gameEnd.won"),(Window.getWidth()-outcomeTextWidth)/2,Integer.parseInt(newGameProperties.getProperty("gameEnd.status.y")));

        }
        // if the player lost, display the lose message
        else{
            double outcomeTextWidth = gameEndStatusFont.getWidth(newMessageProperties.getProperty("gameEnd.lost"));
            gameEndStatusFont.drawString(newMessageProperties.getProperty("gameEnd.lost"),(Window.getWidth()-outcomeTextWidth)/2,Integer.parseInt(newGameProperties.getProperty("gameEnd.status.y")));
        }

        // drawing "top 5 scores"
        double topFiveWidth = gameEndScoresFont.getWidth(newMessageProperties.getProperty("gameEnd.highestScores"));
        gameEndScoresFont.drawString(newMessageProperties.getProperty("gameEnd.highestScores"), (Window.getWidth() - topFiveWidth)/ 2,Integer.parseInt(newGameProperties.getProperty("gameEnd.scores.y")));


        // creating and sorting the list of scores
        String[][] scores = newIOUtils.readCommaSeparatedFile(newGameProperties.getProperty("gameEnd.scoresFile"));
        System.out.println(scores[0][0]);
        Arrays.sort(scores, (a, b) -> {
            double scoreA = Double.parseDouble(a[1].trim());
            double scoreB = Double.parseDouble(b[1].trim());
            return Double.compare(scoreB, scoreA); // Sorting in descending order
        });
        // displaying the scores
        for (int i = 0; i < Math.min(scores.length, 5); i++) {
            String playerName = scores[i][0];
            String score = String.format("%.2f", Double.parseDouble(scores[i][1]));
            String scoreLine = playerName + " - " + score;
            double scoreLineWidth = gameEndScoresFont.getWidth(scoreLine);
            gameEndScoresFont.drawString(scoreLine, (Window.getWidth() - scoreLineWidth) / 2, Integer.parseInt(newGameProperties.getProperty("gameEnd.scores.y")) + 40 * (i + 1));
        }

    }

    // getter for the entity loader
    public EntityLoader getNewEntityLoader() {
        return newEntityLoader;
    }
}
