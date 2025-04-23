
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
    int distanceTravelled;	// arbitrary units
    boolean horseFallen;	// true if fallen
    
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.horseSymbol = horseSymbol;
        
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
    
    
    
    //Other methods of class Horse
    public void fall()
    {
        horseConfidence = horseConfidence - 0.1;
        
       	if (horseConfidence <= 0){
            horseConfidence = 0.1;
        }
        
        horseFallen = true;
    }
    
    public double getConfidence()
    {
        return horseConfidence;
    }
    
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }
    
    public String getName()
    {
        return horseName;
    }
    
    public char getSymbol()
    {
        return horseSymbol;
    }
    
    public void goBackToStart()
    {
        distanceTravelled = 0;
        return;
    }
    
    public boolean hasFallen()
    {
        return horseFallen;
    }

    public void moveForward()
    {
        distanceTravelled = distanceTravelled + 1;
        return;
    }

    public void setConfidence(double newConfidence)
    {
    	if (newConfidence > 1 || newConfidence < 0){
    		System.out.println("Error: Confidence not in range: " + newConfidence);
    	}
    	else{
    		this.horseConfidence = newConfidence;
    	}
        return;
    }
    
    public void setSymbol(char newSymbol)
    {
        this.horseSymbol = newSymbol;
        return;
    }
    
}
