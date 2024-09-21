import bagel.Image;

import java.util.Properties;

public class TripEndFlag {
    private int x;
    private int y;
    private  final Properties newGameProperties;
    private final Image image;


    // initialising the trip end flag
    public TripEndFlag(Trip trip,Properties gameProps, Properties messageProps){
        this.newGameProperties = gameProps;
        this.x = trip.getEndX();
        this.y = trip.getStartY()-trip.getyDistance();
        this.image = new Image(newGameProperties.getProperty("gameObjects.tripEndFlag.image"));
    }

    public void moveDown(){

        this.y += Integer.parseInt(newGameProperties.getProperty("gameObjects.taxi.speedY"));


    }

    public void draw(){

        image.draw(x,y);

    }


    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }


}

