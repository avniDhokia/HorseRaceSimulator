class Bet{
    int amount;
    Horse horse = null;

    // constructor
    public Bet(int amount, Horse horse){
        this.amount = amount;
        this.horse = horse;
        return;
    }


    // setters

    public void setAmount(int amount){
        this.amount = amount;
        return;
    }

    public void setHorse(Horse horse){
        this.horse = horse;
        return;
    }


    // getters

    public int getAmount(){
        return this.amount;
    }

    public Horse getHorse(){
        return this.horse;
    }
}