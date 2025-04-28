import java.util.Scanner;

class RaceExample{
    public static void main(String[] a){

        // enter race length
        int raceLength = inputInt("Enter length of race (a whole number): ");
        while (raceLength <= 5){
            System.out.println("The minimum length is 5.");
            raceLength = inputInt("Enter length of race (a whole number): ");
        }

        // enter number of lanes
        int numLanes = inputInt("Enter number of lanes: ");

        Race race = new Race(raceLength, numLanes);

        // for lane n, enter horse details
        for (int lane = 1; lane <= numLanes; lane++){
            System.out.println("\n\nLANE " + lane + ":");
            String name = inputString("Enter horse's name (if you don't want a horse in this lane, type 'NONE'): ");
            name = name.toUpperCase();

            if (name.equals("NONE")){
                race.addHorse(null, lane);
            }
            else{
                char symbol = inputChar("Enter horse's character symbol: ");
                double confidence = inputDouble("Enter horse's confidence (a decimal number between 0 and 1): ");

                while (confidence >= 1 || confidence <= 0){
                    System.out.println("Confidence must be >0 and <1.");
                    confidence = inputDouble("Enter hors's confidence (a decimal number between 0 and 1): ");
                }

                race.addHorse(new Horse(symbol, name, confidence), lane);
            }
        }

        race.startRace();
        return;
    }

    public static boolean isInt(String test){
        
        for (int i = 0; i < test.length(); i++){
            char c = test.charAt(i);
            if (c > '9' || c < '0'){
                return false;
            }
        }

        return true;
    }

    public static boolean isDouble(String input){
        boolean dotFound = false;

        for (int i = 0; i < input.length(); i++){
            char c = input.charAt(i);

            if (c == '.'){

                // we've found multiple .'s
                if (dotFound){
                    return false;
                }
                // we've found our first .
                else{
                    dotFound = true;
                }

            }

            // we've found a non-. and a non-digit character
            else if (c > '9' || c < '0'){
                return false;
            }
        }

        return true;
    }

    public static double inputDouble(String message){
        String input = inputString(message);

        while (! (isDouble(input))){
            System.out.println("Please enter a decimal number.");
            input = inputString(message);
        }

        return Double.parseDouble(input);
    }

    public static int inputInt(String message){
        String input = inputString(message);

        while (! (isInt(input))){
            System.out.println("Please enter an integer.");
            input = inputString(message);
        }

        return Integer.parseInt(input);
    }

    public static String inputString(String message){
        Scanner scanner = new Scanner(System.in);
        String input = "";

        System.out.println(message);
        input = scanner.nextLine();

        while (input.length() == 0){
            System.out.println("Please enter something.");
            input = scanner.nextLine();
        }

        return input;
    }

    public static char inputChar(String message){
        String input = inputString(message);

        while (input.codePointCount(0, input.length()) != 1){
            System.out.println("Please enter a single character.");
            input = inputString(message);
        }

        char c = input.charAt(0);

        return c;
    }
}