class BettingAccount{
    int apples;         // apples is the currency of the betting system
    Bet bet = new Bet(0, null);

    public BettingAccount(int startApples){
        apples = startApples;
    }

    public int getBalance(){
        return apples;
    }

    // set the current betting amount to the given value
    // do not allow if the betAmount is greater than the user's apples
    public void makeBet(int betAmount, Horse betHorse){
        if (apples > betAmount){
            bet = new Bet(betAmount, betHorse);
        }
        else if (apples == betAmount){
            System.out.println("If User loses this bet, they will have no apples remaining");
            bet = new Bet(betAmount, betHorse);
        }
        else{
            System.out.println("User does not have enough apples to make this bet");
        }
        
        return;
    }

    // return the Bet object
    public Bet getBet(){
        return bet;
    }

    // return the current betting amount
    public int getBetAmount(){
        return bet.getAmount();
    }

    // return the horse that the user is betting on
    public Horse getBetHorse(){
        return bet.getHorse();
    }

    // win the bet, and set betAmount to 0
    public void winBet(){
        apples = apples + (bet.getAmount() * (int)bet.getHorse().getOdds());
        bet.setAmount(0);
        bet.setHorse(null);
        return;
    }

    // lose the bet, and set betAmount to 0
    // if the user loses more apples than they have, warn them and set their apples to 0
    public void loseBet(){
        apples = apples - bet.getAmount();
        bet.setAmount(0);
        bet.setHorse(null);

        if (apples <= 0){
            System.out.println("Warning: User has lost all their apples");
            apples = 0;
        }

        return;
    }

    // increase the bet amount by the given value
    public void increaseBet(int increaseBy){
        int tempBet = bet.getAmount() + increaseBy;

        if (apples > tempBet){
            bet.setAmount(tempBet);
        }
        else if (apples == tempBet){
            System.out.println("If User loses this bet, they will have no apples remaining");
            bet.setAmount(tempBet);
        }
        else{
            System.out.println("User does not have enough apples to make this bet");
        }

        return;
    }

    // decrease the bet amount by the given value
    public void decreaseBet(int decreaseBy)
    {
        if (bet.getAmount() < decreaseBy){
            System.out.println("User cannot decrease bet amount by more than current bet amount");
            return;
        }

        bet.setAmount(bet.getAmount() - decreaseBy);
        return;
    }
}