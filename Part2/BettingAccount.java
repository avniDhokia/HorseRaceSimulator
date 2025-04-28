import java.util.ArrayList;

class BettingAccount{
    boolean active = false;
    String username;
    int apples;         // apples is the currency of the betting system
    Bet bet = new Bet(0, null);
    ArrayList<Bet> betHistory = new ArrayList<Bet>();

    // attributes to store previous bet
    Bet previousBet = new Bet(0, null);
    String previousResult = null;

    // constructor by start balance
    public BettingAccount(int startApples){
        apples = startApples;
    }

    // constructor by reading from string
    public BettingAccount(String line){
        String[] brokenLine = line.split(",");

        username = brokenLine[0];
        apples = Integer.parseInt(brokenLine[1]);
    }

    // constructor by username and start apples
    public BettingAccount(String username, int startApples){
        this.username = username;
        apples = 0;

        return;
    }


    public int getBalance(){
        return apples;
    }

    public String getUsername(){
        return username;
    }

    public boolean isActive(){
        return active;
    }

    public boolean setActive(boolean active){
        this.active = active;
        return active;
    }

    public void setBalance(int balance){
        apples = balance;
        return;
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

    // get whether previous bet won or lost
    public String getPreviousResult(){
        return previousResult;
    }


    // win the bet, and set betAmount to 0
    public void winBet(){
        previousBet = new Bet(bet);
        previousResult = "Won";

        betHistory.add(bet);
        apples = apples + (bet.getAmount() * (int)bet.getHorse().getOdds());
        bet.setAmount(0);
        bet.setHorse(null);
        return;
    }

    // get previous bet
    public Bet getPreviousBet(){
        return previousBet;
    }


    // lose the bet, and set betAmount to 0
    // if the user loses more apples than they have, warn them and set their apples to 0
    public void loseBet(){
        previousResult = "Lost";
        betHistory.add(bet);
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
    public void decreaseBet(int decreaseBy){
        if (bet.getAmount() < decreaseBy){
            System.out.println("User cannot decrease bet amount by more than current bet amount");
            return;
        }

        bet.setAmount(bet.getAmount() - decreaseBy);
        return;
    }
}