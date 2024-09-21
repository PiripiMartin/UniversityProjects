import bagel.*;

import java.util.Properties;

public class Passenger {
    private  final Properties newGameProperties;
    private final Image image;
    private final Font passengerFont;
    private final double expectedEarnings;
    private int x;
    private int y;
    private int priority;
    private int endX;
    private  int endY;
    private int yDistance;
    private boolean isPickedUp;
    private boolean isDroppedOff;
    private boolean allreadyCompletedATrip;
    private boolean coinBoosted;
    private int rate;





    public Passenger(int x, int y, int priority, int endX, int yDistance,Properties gameProps) {
        this.x = x;
        this.y = y;
        this.priority = priority;
        this.endX = endX;
        this.yDistance = yDistance;
        this.newGameProperties = gameProps;
        this.image = new Image(newGameProperties.getProperty("gameObjects.passenger.image"));
        this.isPickedUp = false;
        this.isDroppedOff = false;
        this.allreadyCompletedATrip = false;
        this.coinBoosted = false;
        this.passengerFont = new Font(newGameProperties.getProperty("font"),
                Integer.parseInt(newGameProperties.getProperty("gameObjects.passenger.fontSize")));

        // defining the expected earnings of the player
        switch (priority){
            case 1: this.rate = Integer.parseInt(newGameProperties.getProperty("trip.rate.priority1")); break;
            case 2: this.rate = Integer.parseInt(newGameProperties.getProperty("trip.rate.priority2")); break;
            case 3: this.rate = Integer.parseInt(newGameProperties.getProperty("trip.rate.priority3")); break;
        }
        this.expectedEarnings = (int) (yDistance* 0.1 + priority*rate);


    }

    // function to move passenger to taxi one by one
    public void moveToTaxi(int taxiX, int taxiY){
        // if passenger is to the left
        if(x - taxiX <= -1){
            x+=Integer.parseInt(newGameProperties.getProperty("gameObjects.passenger.walkSpeedX"));
        }
        // if passenger is to the right
        if(x - taxiX >= 1){
            x-=Integer.parseInt(newGameProperties.getProperty("gameObjects.passenger.walkSpeedX"));
        }
        // if passenger is above
        if(y - taxiY <= -1){
            y +=Integer.parseInt(newGameProperties.getProperty("gameObjects.passenger.walkSpeedY"));
        }
        if (y - taxiY >= 1) {
            y -= Integer.parseInt(newGameProperties.getProperty("gameObjects.passenger.walkSpeedY"));
        }

    }
    // function to move the passenger to the end flag
    public void moveToEnd(){
        // if the passenger has not allready reached the end
        if(!allreadyCompletedATrip) {
            // if passenger is to the left
            if (x - endX <= -1) {
                x += Integer.parseInt(newGameProperties.getProperty("gameObjects.passenger.walkSpeedX"));
            }
            // if passenger is to the right
            else if (x - endX >= 1) {
                x -= Integer.parseInt(newGameProperties.getProperty("gameObjects.passenger.walkSpeedX"));
            }
            // if passenger is above
            else if (y - endY <= -1) {
                y += Integer.parseInt(newGameProperties.getProperty("gameObjects.passenger.walkSpeedY"));
            }
            // if passenger is below
            else if (y - endY >= 1) {
                y -= Integer.parseInt(newGameProperties.getProperty("gameObjects.passenger.walkSpeedY"));
            } else {
                allreadyCompletedATrip = true;
            }
        }

    }


    // drawing the passenger
    public void draw(){
        image.draw(x,y);
        // draw the passengers expected score and prioirity if it hasn't been dropped off
        if(!isDroppedOff){
            passengerFont.drawString(String.valueOf(priority),x-30,y);
            passengerFont.drawString(String.valueOf(expectedEarnings),x-100,y);
        }
    }



    // getters and setters
    public void setDroppedOff(boolean droppedOff) {
        isDroppedOff = droppedOff;
    }

    public boolean getIsDroppedOff() {
        return isDroppedOff;
    }


    public void setEndY(int endY) {

        this.endY = endY;
    }

    public int getEndY() {
        return endY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getyDistance() {
        return yDistance;
    }

    public void setyDistance(int yDistance) {
        this.yDistance = yDistance;
    }

    public boolean getPickedUp() {
        return isPickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }

    public boolean getAllreadyCompletedATrip() {
        return allreadyCompletedATrip;
    }

    public void setAllreadyCompletedATrip(boolean allreadyCompletedATrip) {
        this.allreadyCompletedATrip = allreadyCompletedATrip;
    }

    public boolean isCoinBoosted() {
        return coinBoosted;
    }

    public void setCoinBoosted(boolean coinBoosted) {
        this.coinBoosted = coinBoosted;
    }
}
