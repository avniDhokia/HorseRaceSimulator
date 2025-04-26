class Breed{
    String breedName;
    double baseSpeed;
    double baseStamina;    // = endurance

    // constructor
    public Breed(String breedName, double baseSpeed, double baseStamina){
        this.breedName = breedName;
        this.baseSpeed = baseSpeed;
        this.baseStamina = baseStamina;
    }

    // constructor by reading a breed from a CSV file
    public Breed(String line){
        String[] brokenLine = line.split(",");
        breedName = brokenLine[0];
        baseSpeed = Double.parseDouble(brokenLine[1]);
        baseStamina = Double.parseDouble(brokenLine[2]);

        return;
    }


    // return a string to store the breed information in a CSV file
    public String getBreedStringCSV(){
        return breedName + "," + baseSpeed + "," + baseStamina;
    }


    // getters

    public String getBreed(){
        return breedName;
    }

    public double getSpeed(){
        return baseSpeed;
    }

    public double getStamina(){
        return baseStamina;
    }


    // setters

    public void setBreedName(String breedName){
        this.breedName = breedName;
        return;
    }

    public void setSpeed(double speed){
        this.baseSpeed = speed;
        return;
    }

    public void setStamina(double stamina){
        this.baseStamina = stamina;
        return;
    }

}