import bagel.Font;

import java.util.Properties;

public class Trip {
    private final int endX;
    private final int yDistance;
    private final Properties newGameProperties;
    private final Properties newMessageProperties;
    private final Font tripFont;
    private final int startY;
    private final TripEndFlag newTripEndFlag;
    private int priority;
    private double expectedFee;
    private int rate;





    public Trip(Passenger passenger, Taxi taxi, Properties gameProps, Properties messageProps) {
        this.newGameProperties = gameProps;
        this.newMessageProperties = messageProps;
        this.tripFont = new Font(newGameProperties.getProperty("font"),Integer.parseInt(newGameProperties.getProperty("gameplay.info.fontSize")));
        this.startY = passenger.getY();
        this.endX = passenger.getEndX();


        switch (passenger.getPriority()){
            case 1: this.rate = Integer.parseInt(newGameProperties.getProperty("trip.rate.priority1")); break;
            case 2: this.rate = Integer.parseInt(newGameProperties.getProperty("trip.rate.priority2")); break;
            case 3: this.rate = Integer.parseInt(newGameProperties.getProperty("trip.rate.priority3")); break;
        }
        this.expectedFee = (int) (passenger.getyDistance() * 0.1 + passenger.getPriority()*rate);
        this.yDistance = passenger.getyDistance();

        this.priority = passenger.getPriority();

        // creating an associated trip end flag with the trip
        this.newTripEndFlag = new TripEndFlag(this,gameProps,messageProps);
    }

    // update priority to be called when taxi hits a coin
    public void updateExpectedFee(int newPriority){
        this.priority = newPriority;
        switch (priority){
            case 1: this.rate = Integer.parseInt(newGameProperties.getProperty("trip.rate.priority1")); break;
            case 2: this.rate = Integer.parseInt(newGameProperties.getProperty("trip.rate.priority2")); break;
            case 3: this.rate = Integer.parseInt(newGameProperties.getProperty("trip.rate.priority3")); break;
        }
        this.expectedFee = (int)(this.yDistance * Double.parseDouble(newGameProperties.getProperty("trip.rate.perY")) + this.priority * rate);

    }
    // drawing current trip information
    public void draw(){
        tripFont.drawString(newMessageProperties.getProperty("gamePlay.onGoingTrip.title"),Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.y")));
        tripFont.drawString(newMessageProperties.getProperty("gamePlay.trip.expectedEarning")+ expectedFee,Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.y"))+ 30);
        tripFont.drawString(newMessageProperties.getProperty("gamePlay.trip.priority") + priority,Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.x")),Integer.parseInt(newGameProperties.getProperty("gameplay.tripInfo.y")) + 60);
    }

    public TripEndFlag getTripEndFlag() {
        return newTripEndFlag;
    }

    // getters to make the trip end flag

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public int getEndX() {
        return endX;
    }

    public int getyDistance() {
        return yDistance;
    }

    public int getStartY() {
        return startY;
    }

    public double getExpectedFee() {
        return expectedFee;
    }
}
