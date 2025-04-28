# Horse Race Simulator
A Horse Race simulator with both CLI and GUI implementations.

- [Features](#features)
- [Dependencies](#dependencies)
- [Installation](#installation)
- [Usage](#usage)


## Features
### Race Customisation
- Select type of track, and length of track (only for some track types)
- Select number of lanes
- Select number of races
- For each day, select both track and weather conditions. These have an effect on visuals and horse performance.

### Horse Customisation
- For each lane, select the horse (or no horse). Alternatively you can create your own custom horse from a selection of pre-set breeds. Each breed has different default values for speed and stamina.
- For each horse, equip it with equipment and accessories including saddles, horseshoes, bridles, blankets and hats. These all affect horse's speed, stamina, confidence and performances.
- Visualise each lane's horse in a table, and view the horses' stats.

### Betting
- Allow multiple players to bet with their own (fake) money.
- Each player can bet at the start of each race, while viewing each horse's odds in real time. The odds are also affected by any bets placed.

### Racing
- Race the horses! The visuals of the track change depending on the track conditions selected earlier.
- Horses might fall over, where they'll be displayed as 'X' rather than their symbol.
- After each race, each player will see whether they won or lost, and can see their balance update ready for the next race.


## Installation
Clone the repository. Ensure all the following files are present and in the same folder:
   - `Bet.java`
   - `BettingAccount.java`
   - `Breed.java`
   - `Horse.java`
   - `Race.java`
   - `Track.java`
   - `Race.java`
   - `RaceGUI.java`
   - `RunRaceGUI.java`
   - `RunRaceCLI.java`

Also ensure that you have this file for better functioning:
   - `breeds.csv`
     You can add your own custom breeds to this file by using it like a spreadsheet - it is not possible to create new breeds through the GUI.

## Dependencies
To run this project (either the CLI or GUI implementation), you must ensure you have Java installed.


## Usage
You can either use the GUI implementation or the CLI implementation.

1. Compile the code. Open the terminal and set the current directory to the folder where the .java files are kept. Run:

   ```bash
   javac RunRaceCLI.java
   ```
   
   if you want to use the CLI or
   
   ```bash
   javac RunRaceGUI.java
   ```

   if you want to use the GUI

3. Run the compiled code:

   ```bash
   java RunRaceCLI
   ```

   if you want to use the CLI. This invokes the method `startRace`.

   Alternatively run:
   
   ```bash
   java RunRaceGUI
   ```

   if you want to use the GUI. This invokes the method `startRaceGUI`.

