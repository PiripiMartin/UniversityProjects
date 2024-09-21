
import bagel.*;

import java.util.Properties;

public class Coin {


    private int x;
    private int y;
    private final Image image;
    private boolean pickedup;

    public Coin(int x, int y,Properties gameProps) {
        this.x = x;
        this.y = y;
        this.image = new Image(gameProps.getProperty("gameObjects.coin.image"));
        this.pickedup = false;
    }




    public void draw(){
        image.draw(x,y);
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

    public boolean getIsPickedup() {
        return pickedup;
    }

    public void setPickedup(boolean pickedup) {
        this.pickedup = pickedup;
    }
}
