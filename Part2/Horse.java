import java.io.*;

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
    Breed breed;
    String colour;
    String saddle;
    String horseshoes;
    String bridle;
    String blankets;
    String hats;    
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence, String colour, Breed breed){
        this.horseSymbol = horseSymbol;
        this.breed = breed;
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

    // read a Horse from a file, given a BufferedReader
    public Horse(BufferedReader br) throws IOException{

        /* Structure of one line of Horse file:
            name,confidence,representation,colour,breedName,breedSpeed,breedStamina
        */

        String line = br.readLine();
        String[] brokenLine = line.split(",");

        horseName = brokenLine[0];
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

        pw.println(horseName + "," + horseConfidence + "," + horseSymbol + "," + colour + "," + breed.getBreedStringCSV());
        return;
    }

    //Other methods of class Horse
    public void fall(){
        horseFallen = true;
    }
    
    public double getConfidence(){
        return horseConfidence;
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
    
    public void goBackToStart(){
        distanceTravelled = 0;
        return;
    }
    
    public boolean hasFallen(){
        return horseFallen;
    }

    public void moveForward(){
        distanceTravelled = distanceTravelled + 1;
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
    
    public void setSymbol(char newSymbol){
        this.horseSymbol = newSymbol;
        return;
    }
    
}
