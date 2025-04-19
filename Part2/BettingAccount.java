class BettingAccount{
    int apples;         // apples is the currency of the betting system
    int betAmount;

    public BettingAccount(int startApples){
        apples = startApples;
    }

    public int getBalance(){
        return apples;
    }

    // set the current betting amount to the given value
    // do not allow if the betAmount is greater than the user's apples
    public void makeBet(int betAmount){
        if (apples > betAmount){
            this.betAmount = betAmount;
        }
        else if (apples == betAmount){
            System.out.println("If User loses this bet, they will have no apples remaining");
            this.betAmount = betAmount;
        }
        else{
            System.out.println("User does not have enough apples to make this bet");
        }
        
        return;
    }

    // return the current betting amount
    public int getBet(){
        return betAmount;
    }

    // win the bet, and set betAmount to 0
    public void winBet(){
        apples = apples + betAmount;
        betAmount = 0;
        return;
    }

    // lose the bet, and set betAmount to 0
    // if the user loses more apples than they have, warn them and set their apples to 0
    public void loseBet(){
        apples = apples - betAmount;
        betAmount = 0;

        if (apples <= 0){
            System.out.println("Warning: User has lost all their apples");
            apples = 0;
        }

        return;
    }

    // increase the bet amount by the given value
    public void increaseBet(int increaseBy){
        int tempBet = betAmount + increaseBy;

        if (apples > tempBet){
            betAmount = tempBet;
        }
        else if (apples == tempBet){
            System.out.println("If User loses this bet, they will have no apples remaining");
            betAmount = tempBet;
        }
        else{
            System.out.println("User does not have enough apples to make this bet");
        }

        return;
    }

    // decrease the bet amount by the given value
    public void decreaseBet(int decreaseBy){

        if (betAmount < decreaseBy){
            System.out.println("User cannot decrease bet amount by more than current bet amount");
            return;
        }

        betAmount = betAmount - decreaseBy;
        return;
    }
}