class Breed{
    String breedName;
    double baseSpeed;
    double baseStamina;    // = endurance

    public Breed(String breedName, double baseSpeed, double baseStamina){
        this.breedName = breedName;
        this.baseSpeed = baseSpeed;
        this.baseStamina = baseStamina;
    }

    public String getBreed(){
        return breedName;
    }

    public double getSpeed(){
        return baseSpeed;
    }

    public double getStamina(){
        return baseStamina;
    }

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

    // return a string to store the breed information in a CSV file
    public String getBreedStringCSV(){
        return breedName + "," + baseSpeed + "," + baseStamina;
    }
}