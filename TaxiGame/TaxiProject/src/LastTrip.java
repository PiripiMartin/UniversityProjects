import bagel.Font;

import java.util.Properties;

public class LastTrip {
    private final double expectedFee;
    private final int priority;
    private final double penalty;
    private  final Properties newGameProperties;
    private  final Properties newMessageProperties;
    private final Font tripFont;

    public LastTrip(Double penalty,Passenger passenger, double expectedFee, Properties gameProps, Properties messageProps){
        this.newGameProperties = gameProps;
        this.newMessageProperties = messageProps;
        this.priority = passenger.getPriority();
        this.expectedFee = expectedFee;
        this.penalty = penalty;
        this.tripFont = new Font(newGameProperties.getProperty("font"),Integer.parseInt(newGameProperties.getProperty("gameplay.info.fontSize")));
    }

    // function to draw the last trip information
    public void draw(){
        tripFont.drawString(newMessageProperties.getProperty("gamePlay.completedTrip.title"),Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.y")));
        tripFont.drawString(newMessageProperties.getProperty("gamePlay.trip.expectedEarning")+ expectedFee,Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.y"))+ 30);
        tripFont.drawString(newMessageProperties.getProperty("gamePlay.trip.priority") + priority,Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.y"))+ 60);
        tripFont.drawString(newMessageProperties.getProperty("gamePlay.trip.penalty") + penalty,Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.y")) + 90);
    }





}
