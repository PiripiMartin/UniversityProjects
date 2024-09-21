import bagel.Font;

import java.util.Properties;

public class GameScore {

    private final Font gameScoreFont;
    private  final Properties newGameProperties;
    private  final Properties newMessageProperties;
    private int framesRemaining;
    private final double targetScore;
    private double totalPay;
    private int coinFrames;

    public GameScore(Properties gameProps, Properties messageProps){

        this.newGameProperties = gameProps;
        this.newMessageProperties = messageProps;
        this.gameScoreFont = new Font(newGameProperties.getProperty("font"),Integer.parseInt(newGameProperties.getProperty("gameplay.info.fontSize")));
        this.framesRemaining = Integer.parseInt(newGameProperties.getProperty("gamePlay.maxFrames"));
        this.targetScore = Double.parseDouble(newGameProperties.getProperty("gamePlay.target"));
        this.totalPay = 0;

    }

    public void draw(){

        // drawing the earnings, target and remaining frames.
        gameScoreFont.drawString(newMessageProperties.getProperty("gamePlay.earnings") + totalPay,Double.parseDouble(newGameProperties.getProperty("gameplay.earnings.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.earnings.y")));
        gameScoreFont.drawString(newMessageProperties.getProperty("gamePlay.target") + targetScore,Double.parseDouble(newGameProperties.getProperty("gameplay.target.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.target.y")));
        gameScoreFont.drawString(newMessageProperties.getProperty("gamePlay.remFrames") + framesRemaining,Integer.parseInt(newGameProperties.getProperty("gameplay.maxFrames.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.maxFrames.y")));
    }
    // draing the coin frames remaining
    public void drawCoinFrames(){
       gameScoreFont.drawString(String.valueOf(coinFrames),Integer.parseInt(newGameProperties.getProperty("gameplay.coin.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.coin.y")));
    }


    public int getFramesRemaining() {
        return framesRemaining;
    }

    public void setFramesRemaining(int framesRemaining) {
        this.framesRemaining = framesRemaining;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public int getCoinFrames() {
        return coinFrames;
    }

    public void setCoinFrames(int coinFrames) {
        this.coinFrames = coinFrames;
    }
}
