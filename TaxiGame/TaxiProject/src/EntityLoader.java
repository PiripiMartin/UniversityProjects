import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import bagel.*;



public class EntityLoader {

    private  final Properties newGameProperties;
    private  final Properties newMessageProperties;




    private List<Taxi> taxis = new ArrayList<>();
    private List<Passenger> passengers= new ArrayList<>();
    private List<Coin> coins = new ArrayList<>();
    private GameScore newGameScore;

    public Properties getNewGameProperties() {
        return newGameProperties;
    }





    // function to draw all the entities
    public void EntityDraw(){

        for(Passenger passenger: passengers){
            // draw the passenger if it is not picked up
            if(!passenger.getPickedUp()) {
                passenger.draw();

            }

        }
        for(Coin coin: coins){
            if(!coin.getIsPickedup()) {
                coin.draw();
            }
        }
        for(Taxi taxi: taxis){
           taxi.draw();
        }



        if(taxis.get(0).getHasPassenger() || taxis.get(0).getHasLastTrip()){
            taxis.get(0).getCurrentTrip().getTripEndFlag().draw();
        }

    }

    public void PassengerWalkingAway(){
        for(Passenger passenger: passengers){


            // if passenger is dropped off go to the end flag location
            if(passenger.getIsDroppedOff()){
                passenger.moveToEnd();
            }


        }

    }

    // function to move all the entities when arrow pressed
    public void EntityMove(){
        for(Passenger passenger: passengers){
            // if the passenger is picked up stay still


            passenger.setY(passenger.getY() + Integer.parseInt(newGameProperties.getProperty("gameObjects.taxi.speedY")));
        }
        for(Coin coin: coins){
            coin.setY(coin.getY()+Integer.parseInt(newGameProperties.getProperty("gameObjects.taxi.speedY")));
        }
        // function to move flag when you have a passenger and move upwards
        if(taxis.get(0).getHasPassenger() || taxis.get(0).getHasLastTrip()){
            taxis.get(0).getCurrentTrip().getTripEndFlag().moveDown();
        }
        // function to move the flag down after dropping off passenger as passenger goes to the flag
        if(taxis.get(0).getHasLastTrip() && !taxis.get(0).getHasPassenger()){
            taxis.get(0).getPassenger().setEndY(taxis.get(0).getPassenger().getEndY() + 5);

        }
    }


    //function to move the taxi left
    public void TaxiLeft(){
            taxis.get(0).setX(taxis.get(0).getX()-Integer.parseInt(newGameProperties.getProperty("gameObjects.taxi.speedX")));
    }

    //function to move the taxi right
    public void TaxiRight(){
        for(Taxi taxi: taxis){
            taxi.setX(taxi.getX()+Integer.parseInt(newGameProperties.getProperty("gameObjects.taxi.speedX")));
        }
    }

    public void checkForCoin(){
        // if taxi has a coin increment the frames remaining
        if(taxis.get(0).getHasCoin()){
            newGameScore.setCoinFrames(newGameScore.getCoinFrames()+1);
            if(newGameScore.getCoinFrames() > 500){
                this.taxis.get(0).setHasCoin(false);
            }
            else{
                newGameScore.drawCoinFrames();
            }
            //newGameScore.drawCoinFrames();
        }
        for(Coin coin: coins){
            // as long as the coin has not allready been picked up
            if(!coin.getIsPickedup()) {
                double distance = Math.sqrt(Math.pow(coin.getX() - taxis.get(0).getX(), 2) + Math.pow(coin.getY() - taxis.get(0).getY(), 2));
                double range = Double.parseDouble(newGameProperties.getProperty("gameObjects.taxi.radius")) + Double.parseDouble(newGameProperties.getProperty("gameObjects.coin.radius"));
                if (distance <= range) {

                    // if taxi doesnt allready have the coin effect
                    if (!taxis.get(0).getHasCoin() && taxis.get(0).getHasPassenger() && !taxis.get(0).getPassenger().isCoinBoosted()) {
                        // if the priority is not allready one
                        if (taxis.get(0).getCurrentTrip().getPriority() > 1) {
                            // set the new priority to itself -1 and update it
                            taxis.get(0).getCurrentTrip().updateExpectedFee(taxis.get(0).getCurrentTrip().getPriority() - 1);
                        }
                    }
                    taxis.get(0).setHasCoin(true);
                    coin.setPickedup(true);
                    newGameScore.setCoinFrames(0);
                }
            }
        }
        // if taxi does not allready have the coin affect
    }

    // function to check for passenger pickup avaliability
    public void checkForPickup(){
        // only check if taxi doesn't have a passanger
        if(!taxis.get(0).getHasPassenger()) {
            for (Passenger passenger : passengers) {
                // if close enough to get in taxi
                double distance = Math.sqrt(Math.pow(passenger.getX() - taxis.get(0).getX(), 2) + Math.pow(passenger.getY() - taxis.get(0).getY(), 2));
                //making sure the passenger has not just been dropped off
                if(!passenger.getIsDroppedOff()) {
                    if (distance <= 1) {

                        // if the passenger is not allready affected by a coin and the coin affect is applied
                        if(taxis.get(0).getHasCoin() && !passenger.isCoinBoosted() && passenger.getPriority()>1){
                            passenger.setPriority(passenger.getPriority()-1);
                        }



                        passenger.setPickedUp(true);
                        taxis.get(0).setHasPassenger(true);
                        taxis.get(0).newTrip(passenger);
                    }
                    // if close enough to move to taxi
                    if ((distance <= 100) && (distance > 1)) {
                        passenger.moveToTaxi(taxis.get(0).getX(), taxis.get(0).getY());
                    }
                }



            }
        }
    }

    // if taxi is above the trip end flag
    public void checkForDropoff(){
        if(taxis.get(0).getHasPassenger()) {
            // if taxi is close enough to the flag to not incur a penalty:
            if (Math.sqrt(Math.pow(taxis.get(0).getX() - taxis.get(0).getCurrentTrip().getTripEndFlag().getX(), 2) +
                    Math.pow(taxis.get(0).getY() - taxis.get(0).getCurrentTrip().getTripEndFlag().getY(), 2)) <
                    Integer.parseInt(newGameProperties.getProperty("gameObjects.tripEndFlag.radius"))) {
                // add on the expected trip score to the total score
                newGameScore.setTotalPay(newGameScore.getTotalPay() + taxis.get(0).getCurrentTrip().getExpectedFee());


                // remove the passenger from the car
                removePassengerFromCar(0.0);


            }


            // if taxi is to be punished:
            else if (taxis.get(0).getY() < taxis.get(0).getCurrentTrip().getTripEndFlag().getY()) {
                // add on the reduced expected trip score

                int pixelDifference = taxis.get(0).getCurrentTrip().getTripEndFlag().getY() - taxis.get(0).getY();
                double penalty = pixelDifference * Double.parseDouble(newGameProperties.getProperty("trip.penalty.perY"));
                double pay = taxis.get(0).getCurrentTrip().getExpectedFee() - penalty;
                if (pay < 0) {
                    pay = 0;
                }
                newGameScore.setTotalPay(newGameScore.getTotalPay() + pay);

                // remove the passenger from the car
                removePassengerFromCar(penalty);


            }
        }

    }
    public void removePassengerFromCar(Double penalty){
        // assign the last trip to taxi
        taxis.get(0).lastTrip(penalty,taxis.get(0).getPassenger(),taxis.get(0).getCurrentTrip().getExpectedFee());
        taxis.get(0).setHasLastTrip(true);
        taxis.get(0).setHasPassenger(false);
        // remove the current passenger from the car
        taxis.get(0).getPassenger().setX(taxis.get(0).getX());
        taxis.get(0).getPassenger().setY(taxis.get(0).getY());
        taxis.get(0).getPassenger().setDroppedOff(true);
        taxis.get(0).getPassenger().setPickedUp(false);
        taxis.get(0).getPassenger().setEndY(taxis.get(0).getCurrentTrip().getTripEndFlag().getY());

    }


    public List<Taxi> getTaxis() {
        return taxis;
    }

    public GameScore getNewGameScore() {
        return newGameScore;
    }


    // constructor creating the inital list of taxis and passengers.
    public EntityLoader(String filePath,Properties gameProps,Properties messageProps) {
        this.newGameProperties = gameProps;
        this.newMessageProperties = messageProps;
        this.newGameScore = new GameScore(newGameProperties,newMessageProperties);



        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String typeEntity = values[0];
                switch (typeEntity) {
                    case "TAXI":
                        int taxiX = Integer.parseInt(values[1]);
                        int taxiY = Integer.parseInt(values[2]);
                        this.taxis.add(new Taxi(taxiX, taxiY,gameProps,messageProps));
                        break;

                    case "PASSENGER":
                        int passengerX = Integer.parseInt(values[1]);
                        int passengerY = Integer.parseInt(values[2]);
                        int priority = Integer.parseInt(values[3]);
                        int endX = Integer.parseInt(values[4]);
                        int yDistance = Integer.parseInt(values[5]);
                        this.passengers.add(new Passenger(passengerX, passengerY, priority, endX, yDistance,gameProps));
                        break;

                    case "COIN":
                        // adding the coin to the list
                        int coinX = Integer.parseInt(values[1]);
                        int coinY = Integer.parseInt(values[2]);
                        this.coins.add(new Coin(coinX,coinY,gameProps));

                }
            }
        } catch (IOException e) {
            System.out.println("entity error");
        }

}
}








