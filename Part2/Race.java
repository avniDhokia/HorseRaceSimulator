import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McRaceface
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private ArrayList<Horse> horses = new ArrayList<>();
    

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance, int numLanes)
    {
        // initialise instance variables
        raceLength = distance;
        
        /*
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
        */

        return;
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber > 0){
            horses.add(laneNumber-1, theHorse);
        }
        else{
            System.out.println("Error: Cannot add horse to lane " + laneNumber);
        }
        
        return;        
    }

    /**
     * Start the race with GUI
     */
    public void startRaceGUI()   // acts as main method
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
             
        for (Horse h : horses){
            if (! (h==null)){
                h.goBackToStart();
            }
        }
        
                      
        while (!finished)
        {
            for (Horse h : horses){
                if (! (h==null) ){
                    moveHorse(h);
                }
        	}

                        
            //print the race positions
            printRace();
            
            //if any of the three horses has won the race is finished
            for (Horse h : horses){
                if (! (h==null)){
                    if (raceWonBy(h)){
                        finished = true;
                    }
                }
        	}

            // check if all horses have fallen
            boolean allFallen = true;
            for (Horse h : horses){
                if (! (h==null)){
                    if (! h.hasFallen()){
                        allFallen = false;
                    }
                }
        	}

            if (allFallen){
                finished = true;
            }

            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }
        
        // show winning horse
        Horse winner = null;
        for (Horse h : horses){
            if (! (h==null)){
                if (raceWonBy(h)){
                    winner = h;
                }
            }

        }

        if (winner==null){
        	System.out.println("No winners");
        }
        else{
            System.out.println("");
        	System.out.println("And the winner is... " + winner.getName());
        }

    	return;
    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()   // acts as main method
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
             
        for (Horse h : horses){
            if (! (h==null)){
                h.goBackToStart();
            }
        }
        
                      
        while (!finished)
        {
            for (Horse h : horses){
                if (! (h==null) ){
                    moveHorse(h);
                }
        	}

                        
            //print the race positions
            printRace();
            
            //if any of the three horses has won the race is finished
            for (Horse h : horses){
                if (! (h==null)){
                    if (raceWonBy(h)){
                        finished = true;
                    }
                }
        	}

            // check if all horses have fallen
            boolean allFallen = true;
            for (Horse h : horses){
                if (! (h==null)){
                    if (! h.hasFallen()){
                        allFallen = false;
                    }
                }
        	}

            if (allFallen){
                finished = true;
            }

            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }
        
        // show winning horse
        Horse winner = null;
        for (Horse h : horses){
            if (! (h==null)){
                if (raceWonBy(h)){
                    winner = h;
                }
            }

        }

        if (winner==null){
        	System.out.println("No winners");
        }
        else{
            System.out.println("");
        	System.out.println("And the winner is... " + winner.getName());
        }

    	return;
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        //System.out.print('\u000C');  //clear the terminal window
        System.out.print("\033[H\033[2J");
        System.out.flush();

        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();

        for (Horse h : horses){

            if (! (h==null)){
                printLane(h);
            }
            else{
                printLane();
            }
            
            System.out.println();
        }


        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - 1 - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            //System.out.print('\u2322');
            System.out.print('\u274C');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }

        if (spacesAfter >= 0){
            System.out.print("\033[" + (spacesBefore + 4) + "G");
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');

        // print the horse's name and confidence
        System.out.print(" " + theHorse.getName() + " (Current confidence " + theHorse.getConfidence() + ")");
    }
        
    private void printLane()
    {
        //calculate how many spaces are needed
        int spacesBefore = raceLength + 1;
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        multiplePrint(' ',spacesBefore);
        
        //print the | for the end of the track
        System.out.print('|');
    }
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
