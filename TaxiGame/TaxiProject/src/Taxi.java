import java.util.Properties;
import bagel.*;





public class Taxi {

    private  final Properties newGameProperties;
    private  final Properties newMessageProperties;
    private int x;
    private int y;
    private boolean hasPassenger;
    private final Image image;
    private Trip currentTrip;
    private LastTrip lastTrip;
    private Passenger passenger;
    private boolean hasLastTrip;
    private boolean hasCoin;


    public Taxi(int x, int y,Properties gameProps,Properties messageProps) {
        this.x = x;
        this.y = y;
        this.newGameProperties = gameProps;
        this.newMessageProperties = messageProps;
        this.hasPassenger = false;
        this.image = new Image(newGameProperties.getProperty("gameObjects.taxi.image"));
        this.hasLastTrip = false;
        this.hasCoin = false;
    }



    // to add a new trip to the taxi
    public void newTrip(Passenger passenger){
        this.currentTrip = new Trip(passenger,this,newGameProperties,newMessageProperties);
        this.passenger = passenger;

    }
    public void lastTrip(Double penalty,Passenger passenger,Double expectedFee){
        this.lastTrip = new LastTrip(penalty,passenger,expectedFee,newGameProperties,newMessageProperties);
        this.hasLastTrip = true;
    }

    public void draw(){
        //drawing the taxi
        image.draw(x,y);

    }

    public Trip getCurrentTrip() {
        return currentTrip;
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

    public Passenger getPassenger() {
        return passenger;
    }

    public boolean getHasPassenger() {
        return hasPassenger;
    }

    public void setHasPassenger(boolean hasPassenger) {
        this.hasPassenger = hasPassenger;
    }


    public boolean getHasLastTrip() {
        return hasLastTrip;
    }


    public LastTrip getLastTrip() {
        return lastTrip;
    }

    public void setHasLastTrip(boolean hasLastTrip) {
        this.hasLastTrip = hasLastTrip;
    }

    public boolean getHasCoin() {
        return hasCoin;
    }

    public void setHasCoin(boolean hasCoin) {
        this.hasCoin = hasCoin;
    }
}
