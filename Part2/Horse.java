import java.io.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.Random;

/**
 * Write a description of class Horse here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Horse
{
    //Fields of class Horse
    char horseSymbol;
    String horseName;
    double horseConfidence;
    double distanceTravelled;	// arbitrary units
    boolean horseFallen;	// true if fallen


    // additional attributes for GUI implementation

    Track track;
    Breed breed;
    String colour;
    double odds;

    double stamina;
    double staminaCounter = 0; // this keeps track of when to decrease speed due to stamina
    double speed;

    // equipment
    String saddle = "";
    String horseshoes = "";
    String bridle = "";
    String blanket = "";
    String hat = "";

    // weather and track conditions
    boolean sunny = false;
    boolean rainy = false;
    boolean foggy = false;

    // track condition optins:      dry | wet | icy | muddy
    String trackCondition = null;

    // tracking horse performance
    long startTime = 0;
    long endTime = 0;

      
    //Constructors of class Horse

    public Horse(char horseSymbol, String horseName, double horseConfidence){
        this.horseSymbol = horseSymbol;
        this.breed = breed;
        this.speed = breed.getSpeed();
        this.stamina = breed.getStamina();
        this.colour = colour;
        
        // ensure horseName is valid
        if (horseName == null || horseName.length() == 0){
            System.out.println("Error: Name is empty or invalid. Name set to 'empty'");
            this.horseName = "empty";
        }
        else{
            this.horseName = horseName;
        }

        // ensure horseConfidence is valid
        if (0<horseConfidence && horseConfidence<1){
            this.horseConfidence = horseConfidence;
        }
        else{
            System.out.println("Error: Confidence not in range: " + horseConfidence + ". Horse cannot be initialised. Setting confidence to 0.1");
            this.horseConfidence = 0.1;
        }

        this.distanceTravelled = 0;
        this.horseFallen = false;

        return;
    }

    public Horse(char horseSymbol, String horseName, double horseConfidence, String colour, Breed breed){
        this.horseSymbol = horseSymbol;
        this.breed = breed;
        this.speed = breed.getSpeed();
        this.stamina = breed.getStamina();
        this.colour = colour;
        
        // ensure horseName is valid
        if (horseName == null || horseName.length() == 0){
            System.out.println("Error: Name is empty or invalid. Name set to 'empty'");
            this.horseName = "empty";
        }
        else{
            this.horseName = horseName;
        }

        // ensure horseConfidence is valid
        if (0<horseConfidence && horseConfidence<1){
            this.horseConfidence = horseConfidence;
        }
        else{
            System.out.println("Error: Confidence not in range: " + horseConfidence + ". Horse cannot be initialised. Setting confidence to 0.1");
            this.horseConfidence = 0.1;
        }

        horseConfidence = this.horseConfidence;

        this.distanceTravelled = 0;
        this.horseFallen = false;

        return;
    }

    // read a Horse from a file, given a String read from a CSV (formatted in a specific way)
    public Horse(String line) throws IOException{

        /* Structure of one line of Horse file:
            name,confidence,representation,colour,breedName,breedSpeed,breedStamina
        */

        if (line == null){
            System.out.println("Error: Horse file is empty");
            return;
        }

        String[] brokenLine = line.split(",");

        horseName = brokenLine[0].replace("_"," ");
        horseConfidence = Double.parseDouble( brokenLine[1] );
        horseSymbol = brokenLine[2].charAt(0);
        colour = brokenLine[3];
        breed = new Breed(brokenLine[4], Double.parseDouble(brokenLine[5]), Double.parseDouble(brokenLine[6]));

        return;
    }

    // write the Horse to a file, given a PrintWriter
    public void storeHorseCSV(PrintWriter pw){

        /* Structure of one line of Horse file:
            name,confidence,representation,colour,breedName,breedSpeed,breedStamina
        */

        String tempHorseName = horseName.replace(" ", "_");

        pw.println(tempHorseName + "," + horseConfidence + "," + horseSymbol + "," + colour + "," + breed.getBreedStringCSV());
        return;
    }

    public void writeRaceToHistory(Date date, String result, double averageSpeed){

        Boolean writeHeader = false;
        String tempHorseName = horseName.replace(" ", "_");

        File file = new File(tempHorseName + "_history.csv");
        if (!file.exists()){
            writeHeader = true;
        }

        try (PrintWriter pw = new PrintWriter( new FileOutputStream(file, true) )){
            if (writeHeader){
                pw.println("Date,Result,Average Speed,Finish Time,Horse Fell?,Confidence");
            }

            pw.println(date + "," + result + "," + averageSpeed + "," + (startTime-endTime) + "," + horseFallen + "," + horseConfidence);
        }
        catch (IOException e){
            System.out.println("Error: couldn't write race to horse history file");
        }
    }

    // update confidence after winning a race
    public void winRace(){
        horseConfidence = horseConfidence + 0.05;

        if (horseConfidence >= 1){
            horseConfidence = 0.95;
        }

        return;
    }

    // update confidence after losing a race
    public void loseRace(){
        horseConfidence = horseConfidence - 0.05;

        if (horseConfidence <= 0){
            horseConfidence = 0.05;
        }

        return;
    }


    // return a copy of the horse
    public Horse copyHorse(){
        Horse horse = new Horse(horseSymbol, horseName, horseConfidence, colour, breed);
        horse.setSaddle(saddle);
        horse.setHorseshoes(horseshoes);
        horse.setBridle(bridle);
        horse.setBlanket(blanket);
        horse.setHat(hat);
        return horse;
    }

    
    // methods for horse functioning
    
    public void fall(){
        horseFallen = true;
        
        if (horseConfidence > 0.4){
            horseConfidence = horseConfidence - 0.2;
        }
        else{
            horseConfidence = 0.2;
        }
    }

    public void goBackToStart(){
        distanceTravelled = 0;
        return;
    }
    
    public boolean hasFallen(){
        return horseFallen;
    }

    // allow equipment and track conditions to have an effect
    public void prepareForRace(){
        speed = breed.getSpeed();
        stamina = breed.getStamina();
        startTime = 0;
        endTime = 0;


        // go through weather and track conditions and adjust the stats
        if (sunny){
            speed = speed + 1;
            stamina = stamina + 1;
            horseConfidence = horseConfidence + 0.08;
        }

        if (rainy){
            speed = speed - 1;
            stamina = stamina + 1;
            horseConfidence = horseConfidence + 0.05;
        }

        if (foggy){
            speed = speed - 2;
            stamina = stamina + 2;
            horseConfidence = horseConfidence - 0.05;
        }

        if (trackCondition != null){
            if (trackCondition.equals("dry")){
                speed = speed + 2;
                stamina = stamina + 2;
            }
            else if (trackCondition.equals("wet")){
                speed = speed - 1;
                stamina = stamina - 1;
            }
            else if (trackCondition.equals("icy")){
                speed = speed - 2;
                stamina = stamina + 2;
                horseConfidence = horseConfidence + 0.08;
            }
            else if (trackCondition.equals("muddy")){
                speed = speed - 3;
                stamina = stamina - 2;
                horseConfidence = horseConfidence + 0.06;
            }
        }



        // go through each equipment and adjust the stats

        // saddle
        if (!saddle.equals("")){

            if (saddle.equals("Small saddle")){
                speed = speed-1;
                stamina = stamina-1;
            }
            else if (saddle.equals("Medium saddle")){
                speed = speed-2;
                stamina = stamina+2;
            }
            else if (saddle.equals("Large saddle")){
                speed = speed-3;
                stamina = stamina+3;
            }
        }

        // horseshoes
        if (!horseshoes.equals("")){
            
            if (horseshoes.equals("Rusty horseshoes")){
                speed = speed + 1;
                stamina = stamina - 1;
            }
            else if (horseshoes.equals("Magic horseshoes")){
                speed = speed + 3;
                stamina = stamina - 2;
            }
        }
        
        // bridle
        if (!bridle.equals("")){
            
            if (bridle.equals("Basic bridle")){
                horseConfidence = horseConfidence + 0.07;
                speed = speed - 1;
                stamina = stamina - 1;
            }
            else if (bridle.equals("Racing bridle")){
                horseConfidence = horseConfidence + 0.1;
                speed = speed - 2;
                stamina = stamina - 2;
            }
        }

        // blanket
        if (!blanket.equals("")){
            
            if (blanket.equals("Torn blanket")){
                horseConfidence = horseConfidence - 0.05;
                speed = speed - 1;
                stamina = stamina - 1;
            }
            else if (blanket.equals("Cosy blanket")){
                speed = speed - 3;
                stamina = stamina - 2;
                horseConfidence = horseConfidence + 0.1;
            }
        }

        // hat
        if (!hat.equals("")){
            
            if (hat.equals("Oversized hat")){
                horseConfidence = horseConfidence - 0.03;
            }
            else if (hat.equals("Tight hat")){
                horseConfidence = horseConfidence - 0.05;
            }
            else if (hat.equals("Racing helmet")){
                horseConfidence = horseConfidence + 0.1;
            }
        }

        // ensure speed, stamina and confidence are in range
        if (speed < 0){
            speed = 2;
        }
        
        if (stamina < 5){
            stamina = 5;
        }

        if (horseConfidence <= 0){
            horseConfidence = 0.05;
        }
        else if (horseConfidence >= 1){
            horseConfidence = 0.95;
        }

        return;
    }

    public void moveForwardCLI(){
        distanceTravelled = distanceTravelled + 1;
        return;
    }

    public void moveForward(){

        // decrease speed based on stamina
        if (staminaCounter >= stamina){
            staminaCounter = 0;
            speed = speed - 0.1;
        }

        Random random = new Random();
        int randInt = random.nextInt(6);
        randInt = randInt - 6;
        
        // move forward at the correct speed
        distanceTravelled = distanceTravelled + speed + (horseConfidence * 10) + randInt;
        staminaCounter = staminaCounter + speed + (horseConfidence * 10) + randInt;

        // maybe fall
        double fallLimit = (Math.pow(horseConfidence, 3) / 50);
        double fallChance = Math.random();

        if (fallChance < fallLimit){
            horseFallen = true;
        }


        // check if horse has moved enough to get to next track position
        TrackPosition tempPos = track.getNextPosition();
        while (distanceTravelled > tempPos.getDistance()){
            // move to next position
            track.moveToNextPosition();
            tempPos = track.getNextPosition();
        }

        if (distanceTravelled == tempPos.getDistance()){
            track.moveToNextPosition();
        }

        return;
    }


    // setters

    public void setTrack(Track track){
        this.track = track;
        distanceTravelled = 0;
        horseFallen = false;
        return;
    }

    public void setConfidence(double newConfidence){
    	if (newConfidence > 1 || newConfidence < 0){
    		System.out.println("Error: Confidence not in range: " + newConfidence);
    	}
    	else{
    		this.horseConfidence = newConfidence;
    	}
        return;
    }

    public void setConditions(boolean sunny, boolean rainy, boolean foggy, String trackCondition){
        this.sunny = sunny;
        this.rainy = rainy;
        this.foggy = foggy;
        this.trackCondition = trackCondition;
        return;
    }

    public void setSymbol(char newSymbol){
        this.horseSymbol = newSymbol;
        return;
    }

    public void setName(String newName){
        this.horseName = newName;
        
        return;
    }

    public void setSaddle(String saddle) {
        this.saddle = saddle;
        return;
    }

    public void setHorseshoes(String horseshoes) {
        this.horseshoes = horseshoes;
        return;
    }

    public void setBridle(String bridle) {
        this.bridle = bridle;
        return;
    }

    public void setBlanket(String blanket) {
        this.blanket = blanket;
        return;
    }

    public void setHat(String hat) {
        this.hat = hat;
        return;
    }

    public void setOdds(double odds){
        this.odds = odds;
        return;
    }

    public void setEndTime(long endTime){
        this.endTime = endTime;
        return;
    }

    public void setStartTime(long startTime){
        this.startTime = startTime;
        return;
    }


    // getters
        
    public double getConfidence(){
        return horseConfidence;
    }

    public String getColour(){
        return colour;
    }

    
    public double getDistanceTravelled(){
        return distanceTravelled;
    }
    
    public String getName(){
        return horseName;
    }
    
    public char getSymbol(){
        return horseSymbol;
    }

    public double getFinishTime(){        
        
        return (endTime - startTime)/1000.0;
    }

    public Breed getBreed(){
        return breed;
    }
    
    public double getOdds(){
        return odds;
    }

    public double getSpeed(){
        return speed;
    }

    public double getStamina(){
        return stamina;
    }
    
    public TrackPosition getPosition(){
        return track.getCurrentPosition();
    }
}
