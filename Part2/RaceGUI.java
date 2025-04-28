import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;

import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import java.io.*;


class RaceSwingWorker extends SwingWorker<Void,Horse[]>{
    private Horse[] racingHorses;
    private JPanel[] lanes;
    private Track selectedTrack;
    private long startTime;
    private long endTime;
    private String trackCondition;
    private JPanel workingPanel;
    private JPanel racePanel;
    private JLabel startRaceLabel;
    private JPanel startRacePanel;
    private JButton startRace;

    public RaceSwingWorker(Horse[] racingHorses, Track selectedTrack, String trackCondition, JPanel[] lanes, JPanel workingPanel, JPanel racePanel, JLabel startRaceLabel, JPanel startRacePanel, JButton startRace) {
        this.racingHorses = racingHorses;
        this.selectedTrack = selectedTrack;
        this.workingPanel = workingPanel;
        this.racePanel = racePanel;
        this.startRaceLabel = startRaceLabel;
        this.startRacePanel = startRacePanel;
        this.startRace = startRace;
        this.trackCondition = trackCondition;
        this.lanes = lanes;
    }

    @Override
    protected Void doInBackground() throws Exception {
        System.out.println("Race started!");
        
        Boolean finished = false;

        startTime = System.currentTimeMillis(); 

        // do race
        while (!finished){

            // move each horse forward
            moveHorsesForward();

            // check if any horse has won
            finished = checkFinishedAndFallen();

            // display horses
            publish(racingHorses);

            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }
        
        return null;
    }

    protected void process(java.util.List<Horse[]> chunks) {

        for (Horse[] racingHorses : chunks) {
            
            racePanel.removeAll();

            if (selectedTrack.getTrackName().equals("Oval")){

                lanes[0].removeAll();

                for (Horse h : racingHorses){
                    if (h != null){
                        
                        JLabel horseLabel = new JLabel("" + h.getSymbol());
                        horseLabel.setFont(new Font("Serif", Font.BOLD, 24));
                        horseLabel.setSize(50,50);

                        Color foregroundColour;

                        if (h.getColour().equals("White")){
                            foregroundColour = new Color(0,0,0);
                        }
                        else if (h.getColour().equals("Brown")){
                            foregroundColour = new Color(150,87,10);

                            // if track is brown too, make the horse black
                            if (trackCondition.equals("muddy")){
                                foregroundColour = new Color(255,255,255);
                            }
                        }
                        else if (h.getColour().equals("Cream")){
                            foregroundColour = new Color(226,191,61);

                            // if track is cream too, make the horse white
                            if (trackCondition.equals("dry")){
                                foregroundColour = new Color(0,0,0);
                            }
                        }
                        else if (h.getColour().equals("Rainbow")){
                            Random rand = new Random();
                            float red = rand.nextFloat();
                            float green = rand.nextFloat();
                            float blue = rand.nextFloat();

                            foregroundColour = new Color(red, green, blue);
                        }
                        else{
                            foregroundColour = new Color(255,255,255);
                        }

                        horseLabel.setForeground(foregroundColour);


                        // if the horse has fallen, display a X instead of the horse's symbol
                        if (h.hasFallen()){
                            horseLabel.setText("X");
                        }

                        lanes[0].add(horseLabel);
                        lanes[0].revalidate();
                        lanes[0].repaint();

                        TrackPosition pos = h.getPosition();
                        horseLabel.setLocation(pos.getX(), pos.getY());
                    }
                }

                racePanel.add(lanes[0]);
                racePanel.revalidate();
                racePanel.repaint();
            }
            else{
                for (int lane = 0; lane < racingHorses.length; lane++){
                    Horse h = racingHorses[lane];
                    if (h != null){
                        lanes[lane].removeAll();

                        JLabel horseLabel = new JLabel("" + h.getSymbol());
                        horseLabel.setFont(new Font("Serif", Font.BOLD, 24));
                        horseLabel.setSize(50,50);

                        Color foregroundColour;

                        if (h.getColour().equals("White")){
                            foregroundColour = new Color(0,0,0);
                        }
                        else if (h.getColour().equals("Brown")){
                            foregroundColour = new Color(150,87,10);

                            // if track is brown too, make the horse black
                            if (trackCondition.equals("muddy")){
                                foregroundColour = new Color(255,255,255);
                            }
                        }
                        else if (h.getColour().equals("Cream")){
                            foregroundColour = new Color(226,191,61);

                            // if track is cream too, make the horse white
                            if (trackCondition.equals("dry")){
                                foregroundColour = new Color(0,0,0);
                            }
                        }
                        else if (h.getColour().equals("Rainbow")){
                            Random rand = new Random();
                            float red = rand.nextFloat();
                            float green = rand.nextFloat();
                            float blue = rand.nextFloat();

                            foregroundColour = new Color(red, green, blue);
                        }
                        else{
                            foregroundColour = new Color(255,255,255);
                        }

                        horseLabel.setForeground(foregroundColour);


                        // if the horse has fallen, display a X instead of the horse's symbol
                        if (h.hasFallen()){
                            horseLabel.setText("X");
                        }

                        JLabel finishLine = new JLabel("|");
                        finishLine.setFont(new Font("Serif", Font.BOLD, 48));
                        finishLine.setSize(50,50);
                        
                        //racePanel.add(horseLabel);
                        lanes[lane].add(horseLabel);
                        lanes[lane].add(finishLine);
                        lanes[lane].revalidate();
                        lanes[lane].repaint();

                        TrackPosition pos = h.getPosition();

                        horseLabel.setLocation(pos.getX(), pos.getY());
                        finishLine.setLocation(selectedTrack.getRaceLength()-10, pos.getY());


                        /*gbc.gridx = 0;
                        gbc.gridy = lane;*/
                        racePanel.add(lanes[lane]);
                        racePanel.revalidate();
                        racePanel.repaint();
                        //racePanel.add(lanes[lane]);
                    }
                    else{
                        racePanel.add(lanes[lane]);
                        racePanel.revalidate();
                        racePanel.repaint();
                    }
                }
            }

            racePanel.revalidate();
            racePanel.repaint();
        }
    }

    protected void done() {
        System.out.println("Race finished!");

        // show winning horse and set startTime for all horses
        Horse winner = null;
        for (Horse h : racingHorses){
            if (! (h==null)){
                h.setStartTime(startTime);
                h.setEndTime(endTime);
                if (h.getDistanceTravelled() >= selectedTrack.getRaceLength()){
                    winner = h;
                }
            }
        }

        JLabel winningLabel = new JLabel();

        if (winner==null){
            winningLabel.setText("No winners");
        }
        else{
            winningLabel.setText("And the winner is... " + winner.getName());
        }


        // refresh panel again to show winner
        workingPanel.add(winningLabel, BorderLayout.SOUTH);
        
        racePanel.revalidate();
        racePanel.repaint();

        startRacePanel.remove(startRace);
        startRacePanel.revalidate();
        startRacePanel.repaint();
        
        startRaceLabel.setText("The race has finished!");
    }

    private void moveHorsesForward(){
        for (Horse h : racingHorses){
            if (h!=null && !h.hasFallen()){
                h.moveForward();
            }
        }

        return;
    }

    private boolean checkFinishedAndFallen(){
        boolean finished = false;
        endTime = System.currentTimeMillis();

        for (Horse h : racingHorses){
            if (! (h==null)){
                if (h.getDistanceTravelled() == selectedTrack.getRaceLength()){
                    h.setEndTime(endTime);
                    finished = true;
                }
            }
        }

        boolean allFallen = true;
        for (Horse h : racingHorses){
            if (! (h==null)){
                if (! h.hasFallen()){
                    allFallen = false;
                }
            }
        }

        if (allFallen){
            System.out.println("All horses have fallen");
            finished = true;
        }
        return finished;
    }
}


class RaceGUI{

    // final
    final static String NAME_BASE = "Horse Race Simulator";
    final static int MAX_LANES = 8;

    static int numHorses = 0;
    static int numLanes = 3;    
    static int numRaces = 7;
    static int currentRaceNum = 1;
    static int numUsers = 5;
    static int initialApples = 30;

    // colours
    static Color background = new Color(164, 228, 252);
    static Color navColour = new Color(108, 163, 252);
    static Color lightest = new Color(196,245,252);

    static ArrayList<BettingAccount> allBettingAccounts = new ArrayList<BettingAccount>();
    static ArrayList<BettingAccount> bettingAccounts = new ArrayList<BettingAccount>();
    static HashMap<String, JFrame> frames;

    static String[][] conditions;   // eg:     SUNNY    RAINY    FOGGY    TRACK
                                    //       {{"sunny", null, "foggy", "wet"}}
  
    static Track selectedTrack;
    static Horse[] racingHorses = new Horse[numLanes];
    static ArrayList<Horse> allHorses = new ArrayList<Horse>();
    static ArrayList<Breed> allBreeds = new ArrayList<Breed>();
    static Horse winningHorse;

    // constructor
    public RaceGUI(){
        return;
    }


    // 'main' method
    public static void runGUI(){
        // read all horses to allHorses
        try (BufferedReader br = new BufferedReader(new FileReader("horses.csv"))) {
            String line = br.readLine();
            
            while ((line = br.readLine()) != null) {
                allHorses.add(new Horse(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading horses file.");
        }

        // read all breeds to allBreeds
        try (BufferedReader br = new BufferedReader(new FileReader("breeds.csv"))) {
            String line = br.readLine();
            
            while ((line = br.readLine()) != null) {
                allBreeds.add(new Breed(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading breeds file.");
        }

        // read all betting accoutns to allBettingAccounts
        try (BufferedReader br = new BufferedReader(new FileReader("bettingAccounts.csv"))) {
            String line = br.readLine();
            
            while ((line = br.readLine()) != null) {
                allBettingAccounts.add(new BettingAccount(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading betting accounts file.");
        }

        // create all initial screens
        
        // JFrame createHorseScreen = createJFrame(NAME_BASE + " - Create Horse");  // this screen should only be created/developed when needed
        
        frames = new HashMap<String, JFrame>();       // format: ["screenName":JFrame]
        frames.put("homeScreen", createJFrame(NAME_BASE + " - Home"));
        frames.put("viewAnalyticsScreen", createJFrame(NAME_BASE + " - Analytics"));
        frames.put("raceDesignScreen", createJFrame(NAME_BASE + " - Race Setup"));
        frames.put("horseSelectionScreen", createJFrame(NAME_BASE + " - Horse Setup"));
        frames.put("bettingSetupScreen", createJFrame(NAME_BASE + " - Betting Setup"));
        frames.put("preRaceBettingScreen", createJFrame(NAME_BASE + " - Pre-Race Betting"));
        frames.put("raceScreen", createJFrame(NAME_BASE + " - Race"));
        frames.put("postRaceDisplayScreen", createJFrame(NAME_BASE + " - Post-Race Display"));
        frames.put("postWeekBettingDisplayScreen", createJFrame(NAME_BASE + " - Final Betting Display"));

        // develop screens for specialised functions
        developHomeScreen(frames);
        developViewAnalyticsScreen(frames);
        developRaceDesignScreen(frames);
        developHorseSelectionScreen(frames);
        developBettingSetupScreen(frames);
        developPreRaceBettingScreen(frames);
        developRaceScreen(frames);
        developPostRaceDisplayScreen(frames);
        developPostWeekBettingisplayScreen(frames);

        // make homeScreen visible
        frames.get("homeScreen").setVisible(true);

        return;
    }

    public static void main(String[] a){
        runGUI();
        
        return;
    }

    // develop screens
    public static void refreshAllScreens(HashMap<String, JFrame> frames){
        frames.forEach((name, frame) -> {
            frame.revalidate();
            frame.repaint();
        });

        return;
    }

    public static void developHomeScreen(HashMap<String, JFrame> frames){//JFrame homeScreen, JFrame viewAnalyticsScreen, JFrame raceDesignScreen){
        // homeScreen nav
        JButton homeToAnalyticsButton = new JButton("View Analytics");
        homeToAnalyticsButton.addActionListener(e -> {
            switchFrames(frames.get("homeScreen"), frames.get("viewAnalyticsScreen"));
        });

        JButton homeToRaceDesignButton = new JButton("Setup Races");
        homeToRaceDesignButton.addActionListener(e->{
            switchFrames(frames.get("homeScreen"), frames.get("raceDesignScreen"));
        });

        JPanel homeNavPanel = createNavigationPanel(new JButton[]{homeToAnalyticsButton, homeToRaceDesignButton});
        frames.get("homeScreen").add(homeNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new BorderLayout());
        workingPanel.setBackground(background);

        JPanel introPanel = new JPanel();
        introPanel.setBackground(background);

        JPanel infoPanel = new JPanel(new GridLayout(2,2));//new GridBagLayout());
        infoPanel.setBackground(background);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);


        // into
        JEditorPane introEditorPane = new JEditorPane();
        introEditorPane.setBackground(background);
        
        introEditorPane.setContentType("text/html");
        introEditorPane.setEditable(false);
        introEditorPane.setText("<h1>Welcome to the Horse Race Simulator!</h1>");
    
        introPanel.add(introEditorPane);


        // race design info
        JEditorPane raceEditorPane = new JEditorPane();
        raceEditorPane.setBorder(new EmptyBorder(25,25,25,25));
        raceEditorPane.setBackground(lightest);
        raceEditorPane.setContentType("text/html");
        raceEditorPane.setEditable(false);
        raceEditorPane.setText("<h1>Racing</h1><hr>You'll be able to design <strong>thrilling weather conditions</strong> on <strong>ultimate race tracks</strong>,"+
                               "with <strong>incredible horses</strong> galloping to win. </p>\n<p> Watch as <strong>horses speed along the tracks</strong> and even <em>slow down near the end of long races due to their limited stamina</em>!");
        
        //gbc.gridx = 0;
        //gbc.gridy = 0;
        infoPanel.add(raceEditorPane);//, gbc);


        // horse selection info
        JEditorPane horseEditorPane = new JEditorPane();
        horseEditorPane.setBorder(new EmptyBorder(25,25,25,25));
        horseEditorPane.setBackground(lightest);
        horseEditorPane.setContentType("text/html");
        horseEditorPane.setEditable(false);
        horseEditorPane.setText("<h1>Horses</h1><hr><p>Choose from a variety of <strong>top horses</strong> in all the best breeds. "+
                                "Choose a <em>lightning fast American Quater Horse</em> or a <em>well-paced Arabian or Appaloosa Horse</em> (or even create your own new horse!).</p>"+
                                "<p>Choose from a range of <strong>epic equipment</strong> to <strong>boost your horse's stats!</strong></p>");
        
        //gbc.gridx = 1;
        //gbc.gridy = 0;
        infoPanel.add(horseEditorPane);//, gbc);


        // betting info
        JEditorPane bettingEditorPane = new JEditorPane();
        bettingEditorPane.setBorder(new EmptyBorder(25,25,25,25));
        bettingEditorPane.setBackground(lightest);
        bettingEditorPane.setContentType("text/html");
        bettingEditorPane.setEditable(false);
        bettingEditorPane.setText("<h1>Betting</h1><hr><p>Take part in an <strong>epic betting competition</strong>!</p><p>Bet to <strong>rise to the top with your favourite horse</strong>, or <strong>risk it all on a mighty underdog</strong>.</p>");
        
        //gbc.gridx = 0;
        //gbc.gridy = 1;
        infoPanel.add(bettingEditorPane);//, gbc);


        // analytics info
        JEditorPane analyticsEditorPane = new JEditorPane();
        analyticsEditorPane.setBorder(new EmptyBorder(25,25,25,25));
        analyticsEditorPane.setBackground(lightest);
        analyticsEditorPane.setContentType("text/html");
        analyticsEditorPane.setEditable(false);
        analyticsEditorPane.setText("<h1>Analytics</h1><hr><p>Check out all the <strong>horse's past races and performances</strong>, and see the <strong>fastest race times</strong> on all the tracks! View <strong>track best times</strong> by viewing the track stats!</p>"+
                                    "<p>Will your favourite horse reach the top of the leaderboard?</p>");
        
        //gbc.gridx = 1;
        //gbc.gridy = 1;
        infoPanel.add(analyticsEditorPane);//, gbc);


        workingPanel.add(introPanel, BorderLayout.NORTH);
        workingPanel.add(infoPanel);

        frames.get("homeScreen").add(workingPanel, BorderLayout.CENTER);


        return;// frames.get("homeScreen");
    }

    public static void developViewAnalyticsScreen(HashMap<String, JFrame> frames){//JFrame viewAnalyticsScreen, JFrame homeScreen){
        // viewAnalyticsScreen nav
        JButton goHome = new JButton("Home");
        goHome.addActionListener(e -> {
            switchFrames(frames.get("viewAnalyticsScreen"), frames.get("homeScreen"));
        });
        JPanel analyticsNavPanel = createNavigationPanel(new JButton[]{goHome});
        frames.get("viewAnalyticsScreen").add(analyticsNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridBagLayout());
        workingPanel.setBackground(background);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20,20,20,20);

        /* panel layout:

            track history info      track history combo / track history buttons
            horse history info      horse history combo / horse history button
            bet history info        bet history button

        */

        // track history
        JEditorPane trackInfoPane = new JEditorPane();
        trackInfoPane.setBorder(new EmptyBorder(25,25,25,25));
        trackInfoPane.setBackground(lightest);
        trackInfoPane.setContentType("text/html");
        trackInfoPane.setEditable(false);
        trackInfoPane.setText("track info");
        trackInfoPane.setBorder(new EmptyBorder(20, 20, 20, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        workingPanel.add(trackInfoPane, gbc);

        

        JPanel trackHistorySelectionPanel = new JPanel(new GridLayout(0,1));
        String[] trackNames = {"Line", "Oval", "ZigZag"};
        JComboBox<String> trackHistoryCombo = new JComboBox<>(trackNames);
        trackHistorySelectionPanel.add(trackHistoryCombo);

        JButton trackHistoryButton = new JButton("View Track History");
        trackHistoryButton.addActionListener(e->{

            String selectedTrackName = (String)trackHistoryCombo.getSelectedItem();
            JFrame trackHistoryFrame = createJFrame(NAME_BASE + " - " + selectedTrackName + " History");

            int[] numLinesAndColumns = countFileLines(selectedTrackName + "_history.csv");

            developHistoryScreen(trackHistoryFrame, selectedTrackName, numLinesAndColumns[0], numLinesAndColumns[1]);
            trackHistoryFrame.setVisible(true);
        });
        trackHistorySelectionPanel.add(trackHistoryButton);

        gbc.gridx = 1;
        gbc.gridy = 0;
        workingPanel.add(trackHistorySelectionPanel, gbc);


        // horse history
        JEditorPane horseInfoPane = new JEditorPane();
        horseInfoPane.setBorder(new EmptyBorder(25,25,25,25));
        horseInfoPane.setBackground(lightest);
        horseInfoPane.setContentType("text/html");
        horseInfoPane.setEditable(false);
        horseInfoPane.setText("track info");
        horseInfoPane.setBorder(new EmptyBorder(20, 20, 20, 20));

        gbc.gridx = 0;
        gbc.gridy = 1;
        workingPanel.add(horseInfoPane, gbc);

        String horseNamesArray[] = new String[allHorses.size()];
        for (int i = 0; i < allHorses.size(); i++){
            horseNamesArray[i] = allHorses.get(i).getName();
        }

        JPanel horseHistorySelectionPanel = new JPanel(new GridLayout(0,1));
        JComboBox<String> horseHistoryCombo = new JComboBox<>(horseNamesArray);
        horseHistorySelectionPanel.add(horseHistoryCombo);

        JButton horseHistoryButton = new JButton("View Horse History");
        horseHistoryButton.addActionListener(e->{

            String selectedHorseName = (String)horseHistoryCombo.getSelectedItem();
            JFrame horseHistoryFrame = createJFrame(NAME_BASE + " - " + selectedHorseName + " History");

            selectedHorseName = selectedHorseName.replace(" ", "_");

            int[] numLinesAndColumns = countFileLines(selectedHorseName + "_history.csv");

            developHistoryScreen(horseHistoryFrame, selectedHorseName, numLinesAndColumns[0], numLinesAndColumns[1]);
            horseHistoryFrame.setVisible(true);
        });
        horseHistorySelectionPanel.add(horseHistoryButton);

        gbc.gridx = 1;
        gbc.gridy = 1;
        workingPanel.add(horseHistorySelectionPanel, gbc);


        // bet history
        JEditorPane betInfoPane = new JEditorPane();
        betInfoPane.setBorder(new EmptyBorder(25,25,25,25));
        betInfoPane.setBackground(lightest);
        betInfoPane.setContentType("text/html");
        betInfoPane.setEditable(false);
        betInfoPane.setText("track info");
        betInfoPane.setBorder(new EmptyBorder(20, 20, 20, 20));

        gbc.gridx = 0;
        gbc.gridy = 2;
        workingPanel.add(betInfoPane, gbc);

        String betUsernamesArray[] = new String[allBettingAccounts.size()];
        for (int i = 0; i < allBettingAccounts.size(); i++){
            betUsernamesArray[i] = allBettingAccounts.get(i).getUsername();
        }

        JPanel betHistorySelectionPanel = new JPanel(new GridLayout(0,1));
        JComboBox<String> betHistoryCombo = new JComboBox<>(betUsernamesArray);
        betHistorySelectionPanel.add(betHistoryCombo);

        JButton betHistoryButton = new JButton("View User Betting History");
        betHistoryButton.addActionListener(e->{

            String selectedUsername = (String)betHistoryCombo.getSelectedItem();
            JFrame betHistoryFrame = createJFrame(NAME_BASE + " - " + selectedUsername + " History");

            selectedUsername = selectedUsername.replace(" ", "_");

            int[] numLinesAndColumns = countFileLines(selectedUsername + "_history.csv");

            developHistoryScreen(betHistoryFrame, selectedUsername, numLinesAndColumns[0], numLinesAndColumns[1]);
            betHistoryFrame.setVisible(true);
        });
        betHistorySelectionPanel.add(betHistoryButton);

        gbc.gridx = 1;
        gbc.gridy = 2;
        workingPanel.add(betHistorySelectionPanel, gbc);


        
        frames.get("viewAnalyticsScreen").add(workingPanel);

        return;// frames.get("viewAnalyticsScreen");
    }

    // helper used by: developViewAnalyticsScreen()
    public static int[] countFileLines(String fileName){
        int numLines = 0;
        int numColumns = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();
            while (line != null) {
                numLines++;
                numColumns = line.split(",").length;
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error reading " + fileName + " file.");
        }

        int[] results = {numLines, numColumns};

        return results;
    }

    // helper used by: developViewAnalyticsScreen()
    public static void developHistoryScreen(JFrame frame, String name, int numLines, int numColumns){
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e->{
            frame.dispose();
        });

        JPanel historyNavPanel = createNavigationPanel(new JButton[]{
            cancel
        });
        frame.add(historyNavPanel, BorderLayout.PAGE_START);

        frame.setBackground(background);
        JScrollPane scrollPane;

        File trackFile = new File(name + "_history.csv");
        Object[][] data = new Object[numLines][numColumns];
        String[] columnNames = {"history"};

        if (!trackFile.exists()){
            System.out.println("File " + name + "_history.csv" + " does not exist.");
            JTextPane errorPane = new JTextPane();

            name = name.replace("_", " ");

            errorPane.setText("There's no data for " + name + " yet! Try doing a race with " + name + " and then check back.");
            errorPane.setEditable(false);
            errorPane.setBackground(lightest);
            frame.add(errorPane);
        }
        else{
            try (BufferedReader br = new BufferedReader(new FileReader(trackFile))){

                int lineNum = 0;
                String line = br.readLine();
                if (line!=null){
                    columnNames = line.split(",");
                }

                line = br.readLine();            

                while (line != null) {
                    
                    Object[] brokenLine = line.split(",");
                    data[lineNum] = brokenLine;

                    lineNum++;
                    line = br.readLine();
                }
                    
                JTable infoTable = new JTable(data, columnNames);
                scrollPane = new JScrollPane(infoTable);
                infoTable.setFillsViewportHeight(true);
                frame.add(scrollPane);
            }
            catch (IOException e) {
                System.out.println("Error reading " + name + " file.");
            }
        }

        return;
    }

    // helper used by: developViewAnalyticsScreen()
    public static void developHorseHistoryScreen(JFrame horseHistoryFrame, String horseName){


        return;
    }

    public static void developRaceDesignScreen(HashMap<String, JFrame> frames){//JFrame raceDesignScreen, JFrame homeScreen, JFrame horseSelectionScreen){
        JTextField raceNumInput = new JTextField(5);
        // raceDesignScreen nav
        JButton goHome = new JButton("Home");
        goHome.addActionListener(e->{
            switchFrames(frames.get("raceDesignScreen"), frames.get("homeScreen"));
        });

        int raceLength = 0;

        JSlider numLanesSlider = new JSlider(1, MAX_LANES, numLanes);


        // make slider for raceLength
        JSlider raceLengthSlider = new JSlider(100, 940, 940);
        raceLengthSlider.setMajorTickSpacing(200);
        raceLengthSlider.setMinorTickSpacing(50);
        raceLengthSlider.setBackground(lightest);
        raceLengthSlider.setPaintTicks(true);
        raceLengthSlider.setPaintLabels(true);
        JLabel raceLengthSliderLabel = new JLabel("Select length of track:");


        String[] trackOptions = {"Line", "Oval", "ZigZag"};
        JComboBox<String> selectTrack = new JComboBox<>(trackOptions);
        selectTrack.addActionListener(e-> {
            if (selectTrack.getSelectedItem().equals("Line") || selectTrack.getSelectedItem().equals("ZigZag")){
                raceLengthSlider.setVisible(true);
                raceLengthSliderLabel.setVisible(true);
            }
            else{
                raceLengthSlider.setVisible(false);
                raceLengthSliderLabel.setVisible(false);
            }
        });




        JButton selectHorses = new JButton("Select Horses");
        selectHorses.addActionListener(e->{
            // check at least 1 race has been selected
            if (raceNumInput.getText().length() != 0 && isInt(raceNumInput.getText()) && Integer.parseInt(raceNumInput.getText()) > 0){

                if (Integer.parseInt(raceNumInput.getText()) > 28){
                    JLabel alert = new JLabel("Warning: Maximum number of races is 28.");
                    JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                numRaces = Integer.parseInt(raceNumInput.getText());

                for (int raceIndex = 0; raceIndex < numRaces; raceIndex++){
                    for (int i = 0; i < 4; i++){
                        if (conditions[raceIndex][i] == null){
                            conditions[raceIndex][i] = "x";
                        }
                    }
                }

                int panelHeight = 646;
                int borderHeight = 20;
                int laneHeight;
                
                if (((String)selectTrack.getSelectedItem()).equals("Oval")){
                    laneHeight = panelHeight - (2*borderHeight);
                    selectedTrack = new Track((String)selectTrack.getSelectedItem(), 3000, laneHeight);
                }
                else{
                    laneHeight = (panelHeight / numLanesSlider.getValue()) - (2*borderHeight);
                    selectedTrack = new Track((String)selectTrack.getSelectedItem(), raceLengthSlider.getValue(), laneHeight);
                }

                JFrame newFrame = createJFrame(NAME_BASE + " - Horse Setup");
                frames.put("horseSelectionScreen", newFrame);
                developHorseSelectionScreen(frames);
                /*frames.get("horseSelectionScreen").revalidate();
                frames.get("horseSelectionScreen").repaint();*/
                switchFrames(frames.get("raceDesignScreen"), frames.get("horseSelectionScreen"));
            }
            else{
                JLabel alert = new JLabel("Invalid number of races selected.");
                JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel raceDesignNavPanel = createNavigationPanel(new JButton[]{goHome, selectHorses});
        frames.get("raceDesignScreen").add(raceDesignNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));
        workingPanel.setBackground(background);
        workingPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // instructions + information
        JEditorPane info = new JEditorPane();
        info.setBorder(new EmptyBorder(25,25,25,25));
        info.setBackground(lightest);
        info.setContentType("text/html");
        info.setEditable(false);
        info.setText("<p>Choose <strong>track type</strong>, <strong>number of lanes</strong> and <strong>number of races</strong>. Select <strong>weather and track conditions</strong> for each race.</p>\n\n" +
                     "<p><strong>Dry tracks</strong> allow optimal performance. <strong>Wet</strong> and <strong>Icy tracks</strong> slow down horses and make them more likely to fall. <strong>Muddy tracks</strong> slow down horses, and decrease their stamina.</p>\n\n" +
                     "<p><strong>Sunny</strong> weather allows optimal performance. Horses run slower in <strong>Rainy</strong> and <strong>Foggy</strong> weather. <strong>Foggy</strong> weather also decreases horses' confidence.</p>");

        info.setBorder(new EmptyBorder(20, 20, 20, 20));
        workingPanel.add(info);

        

        // trackPanel: select track type, select number of lanes, select number of races
        JPanel trackPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);

        trackPanel.setBorder(new EmptyBorder(10, 50, 5, 50));
        trackPanel.setBackground(lightest);

        gbc.gridx = 0;
        gbc.gridy = 0;
        trackPanel.add(new JLabel("Select track:"), gbc);

        /*String[] trackOptions = {"Line", "Oval", "8", "Other"};    added earlier
        JComboBox<String> selectTrack = new JComboBox<>(trackOptions);*/

        gbc.gridx = 1;
        gbc.gridy = 0;
        trackPanel.add(selectTrack, gbc);        

        gbc.gridx = 0;
        gbc.gridy = 1;
        trackPanel.add(raceLengthSliderLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        trackPanel.add(raceLengthSlider, gbc);

        // make slider for number of lanes
        //JSlider numLanesSlider = new JSlider(1, MAX_LANES, numLanes); added earlier
        numLanesSlider.setBackground(lightest);
        numLanesSlider.setMajorTickSpacing(2);
        numLanesSlider.setMinorTickSpacing(1);
        numLanesSlider.setPaintTicks(true);
        numLanesSlider.setPaintLabels(true);

        numLanesSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                numLanes = numLanesSlider.getValue();
                racingHorses = new Horse[numLanes];
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        trackPanel.add(new JLabel("Select number of lanes:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        trackPanel.add(numLanesSlider, gbc);        

        JPanel conditionsPanel = new JPanel(new GridLayout(0,1));

        // setup tabs for conditions of each race
        gbc.gridx = 0;
        gbc.gridy = 3;
        trackPanel.add(new JLabel("Select Number of Races:"), gbc);
        //JTextField raceNumInput = new JTextField(5); above to allow button validation

        gbc.gridx = 1;
        gbc.gridy = 3;
        trackPanel.add(raceNumInput, gbc);

        workingPanel.add(trackPanel);

        raceNumInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                processInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            String input = raceNumInput.getText();
                if (input.length() > 0){
                    processInput();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                processInput();
            }

            private void processInput() {
                String input = raceNumInput.getText();

                if (isInt(input)){
                    if (Integer.parseInt(input) > 28){
                        JLabel alert = new JLabel("Warning: Maximum number of races is 28.");
                        JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // create a mini tab menu so you can access and edit each race's conditions
                    JTabbedPane raceTabPanel = new JTabbedPane();
                    conditionsPanel.removeAll();
                    
                    int race = Integer.parseInt(input);
                    numRaces = race;

                    conditions = new String[race][4];

                    for (int i = 1; i <= race; i++){
                        final int RACE_INDEX = i;

                        JPanel racePanel = new  JPanel();
                        //racePanel.setLayout(new BoxLayout(racePanel, BoxLayout.Y_AXIS));
                        racePanel.setLayout(new GridLayout(0,1));
                        racePanel.setBackground(lightest);
                        racePanel.add(new JLabel("Race " + RACE_INDEX + " Conditions"));
                        raceTabPanel.addTab("Race " + RACE_INDEX, racePanel);

                        // track conditions
                        JRadioButton dryButton = new JRadioButton("Dry Track");
                        dryButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                conditions[RACE_INDEX-1][3] = "dry";
                            }
                        });

                        JRadioButton wetButton = new JRadioButton("Wet Track");
                        wetButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                conditions[RACE_INDEX-1][3] = "wet";
                            }
                        });

                        JRadioButton icyButton = new JRadioButton("Icy Track");
                        icyButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                conditions[RACE_INDEX-1][3] = "icy";
                            }
                        });

                        JRadioButton muddyButton = new JRadioButton("Muddy Track");
                        muddyButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                conditions[RACE_INDEX-1][3] = "muddy";
                            }
                        });

                        ButtonGroup conditionButtons = new ButtonGroup();
                        


                        conditionButtons.add(dryButton);
                        dryButton.setSelected(true);
                        conditions[RACE_INDEX-1][3] = "dry";

                        conditionButtons.add(wetButton);
                        conditionButtons.add(icyButton);
                        conditionButtons.add(muddyButton);

                        JPanel trackConditionsPanel = new JPanel();
                        trackConditionsPanel.setBackground(lightest);
                        trackConditionsPanel.add(new Label("Track Conditions:"));

                        trackConditionsPanel.add(dryButton);
                        trackConditionsPanel.add(wetButton);
                        trackConditionsPanel.add(icyButton);
                        trackConditionsPanel.add(muddyButton);

                        for (Component c : trackConditionsPanel.getComponents()){
                            c.setBackground(lightest);
                        }

                        racePanel.add(trackConditionsPanel);


                        // weather conditions
                        JCheckBox sunny = new JCheckBox("Sunny");
                        sunny.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                if (sunny.isSelected()){
                                    conditions[RACE_INDEX-1][0] = "sunny";
                                }
                                else{
                                    conditions[RACE_INDEX-1][0] = "x";
                                }
                            }
                        });

                        JCheckBox rainy = new JCheckBox("Rainy");
                        rainy.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                if (rainy.isSelected()){
                                    conditions[RACE_INDEX-1][1] = "rainy";
                                }
                                else{
                                    conditions[RACE_INDEX-1][1] = "x";
                                }
                            }
                        });

                        JCheckBox foggy = new JCheckBox("Foggy");
                        foggy.addItemListener(new ItemListener() {
                            @Override
                            public void itemStateChanged(ItemEvent e) {
                                if (foggy.isSelected()){
                                    conditions[RACE_INDEX-1][2] = "foggy";
                                }
                                else{
                                    conditions[RACE_INDEX-1][2] = "x";
                                }
                            }
                        });

                        JPanel weatherConditionsPanel = new JPanel();
                        weatherConditionsPanel.setBackground(lightest);
                        weatherConditionsPanel.add(new Label("Weather Conditions:"));
                        weatherConditionsPanel.add(sunny);
                        weatherConditionsPanel.add(rainy);
                        weatherConditionsPanel.add(foggy);

                        for (Component c : weatherConditionsPanel.getComponents()){
                            c.setBackground(lightest);
                        }

                        racePanel.setBackground(lightest);
                        racePanel.add(weatherConditionsPanel);
                    }
                    raceTabPanel.setBackground(lightest);
                    conditionsPanel.add(raceTabPanel);
                    conditionsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
                                        
                    workingPanel.add(conditionsPanel);
                    workingPanel.revalidate();
                    workingPanel.repaint();
                    frames.get("raceDesignScreen").add(workingPanel);
                }
            }
        });


        conditionsPanel.setBackground(lightest);
        workingPanel.add(conditionsPanel);


        frames.get("raceDesignScreen").add(workingPanel);
        return ;//frames.get("raceDesignScreen");
    }
    
    public static void developHorseSelectionScreen(HashMap<String, JFrame> frames){//JFrame horseSelectionScreen, JFrame raceDesignScreen, JFrame bettingSetupScreen){
        // horseSelectionScreen nav
        JButton createHorse = new JButton("Create Custom Horse");
        createHorse.addActionListener(e->{
            JFrame newFrame = createJFrame(NAME_BASE + " - Create Horse");
            frames.put("createHorseScreen", newFrame);
            developHorseCreationScreen(frames);
            frames.get("createHorseScreen").setVisible(true);
        });

        JButton toBetting = new JButton("Setup Betting");
        toBetting.addActionListener(e->{

            // check how many horses are racing
            numHorses = 0;
            for (int i = 0; i < racingHorses.length; i++){
                if (racingHorses[i] != null){
                    numHorses++;
                }
            }

            if (numHorses > 0){
                JFrame newFrame = createJFrame(NAME_BASE + " - Setup Betting");
                frames.put("bettingSetupScreen", newFrame);
                developBettingSetupScreen(frames);
                /*frames.get("bettingSetupScreen").revalidate();
                frames.get("bettingSetupScreen").repaint();*/
                switchFrames(frames.get("horseSelectionScreen"), frames.get("bettingSetupScreen"));
            }
            else{
                JOptionPane.showMessageDialog(null, "There must be at least 1 horse added to the race.");
            }


        });
        JPanel horseSelectionNavPanel = createNavigationPanel(new JButton[]{
            addSwitchButton("Back to Race Setup", frames.get("horseSelectionScreen"), frames.get("raceDesignScreen")),
            createHorse,
            toBetting
        });

        frames.get("horseSelectionScreen").add(horseSelectionNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));
        workingPanel.setBackground(background);
        workingPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel infoAndCustomisationPanel = new JPanel();
        infoAndCustomisationPanel.setBackground(background);
        infoAndCustomisationPanel.setLayout(new GridLayout(1,0));    

        StyleContext sc = new StyleContext();
        DefaultStyledDocument doc = new DefaultStyledDocument(sc);
        JTextPane statsPane = new JTextPane(doc);
        statsPane.setPreferredSize(new Dimension(300, 100));
        statsPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        final Style mainStyle = sc.addStyle("MainStyle", defaultStyle);

        final Style advantageStyle = sc.addStyle("Adv", null);
        StyleConstants.setForeground(advantageStyle, Color.green);

        final Style disadvantageStyle = sc.addStyle("DisAdv", null);
        StyleConstants.setForeground(disadvantageStyle, Color.red);

        String text = "Customise each lane horse via the tabs. You can also select for no horse to race in any lane.\n" +
                      "For each horse, you can choose equipment and accessories. These can affect the horse's performance.\n\n" +
                      "Some equipment have different effects, but typically:\n" +  //53
                      "Saddle : -speed, +stamina\n" +//25
                      "Horseshoes : +speed, -stamina\n" +//29
                      "Bridle : +confidence, -speed, -stamina\n" +//38
                      "Blanket : +confidence, -speed, -stamina\n" +//39
                      "Hat : -confidence" +//17
                      "\n\nRemember to click 'Add Horse to Lane n' to save any changes you make to the horses.\n" +
                      "You need to add at least 1 horse to be able to race.";

        try {
            doc.setLogicalStyle(0, mainStyle);
            doc.insertString(0, text, null);

            doc.setCharacterAttributes(258, 6, disadvantageStyle, false);
            doc.setCharacterAttributes(266, 8, advantageStyle, false);
            doc.setCharacterAttributes(288, 6, advantageStyle, false);
            doc.setCharacterAttributes(296, 8, disadvantageStyle, false);
            doc.setCharacterAttributes(314, 11, advantageStyle, false);
            doc.setCharacterAttributes(327, 6, disadvantageStyle, false);
            doc.setCharacterAttributes(335, 8, disadvantageStyle, false);
            doc.setCharacterAttributes(354, 11, advantageStyle, false);
            doc.setCharacterAttributes(367, 6, disadvantageStyle, false);
            doc.setCharacterAttributes(375, 8, disadvantageStyle, false);
            doc.setCharacterAttributes(390, 11, disadvantageStyle, false);
        } catch (BadLocationException e) {
        }

        statsPane.setEditable(false);
        statsPane.setBackground(lightest);
        infoAndCustomisationPanel.add(statsPane);

        // do dynamic tab thing to allow customising each horse
        JPanel customisationPanel = new JPanel(new GridLayout(0,1));
        customisationPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JPanel tablePanel = new JPanel();

        // make horseNames array for users to select horse to race
        String horseNamesArray[] = new String[allHorses.size()];
        for (int i = 0; i < allHorses.size(); i++){
            horseNamesArray[i] = allHorses.get(i).getName();
        }

        // create a mini tab menu so you can access and edit each horse's equipment
        JTabbedPane horseTabPanel = new JTabbedPane();
        for (int i = 1; i <= numLanes; i++){
            JPanel horsePanel = new  JPanel();

            horsePanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 0, 5, 0);

            horseTabPanel.addTab("Lane " + i, horsePanel);
            horsePanel.setBackground(lightest);
            horseTabPanel.setBackground(lightest);

            // select horse
            JComboBox<String> selectHorseFromAll = new JComboBox<>(horseNamesArray);


            // equipment comboBoxes
            String saddleOptions[] = {"No saddle", "Small saddle", "Medium saddle", "Large saddle"};
            JComboBox<String> saddleCombo = new JComboBox<>(saddleOptions);
            horsePanel.add(saddleCombo);

            String horseshoesOptions[] = {"No horseshoes", "Rusty horseshoes", "Magic horseshoes"};
            JComboBox<String> horseshoesCombo = new JComboBox<>(horseshoesOptions);
            horsePanel.add(horseshoesCombo);

            String bridleOptions[] = {"No bridle", "Basic bridle", "Racing Bridle"};
            JComboBox<String> bridleCombo = new JComboBox<>(bridleOptions);
            horsePanel.add(bridleCombo);

            String blanketOptions[] = {"No blanket", "Torn blanket", "Cosy blanket"};
            JComboBox<String> blanketCombo = new JComboBox<>(blanketOptions);
            horsePanel.add(blanketCombo);

            String hatOptions[] = {"No hat", "Oversized hat", "Tight hat", "Racing helmet"};
            JComboBox<String> hatCombo = new JComboBox<>(hatOptions);
            horsePanel.add(hatCombo);

            gbc.gridx = 0;
            gbc.gridy = 0;
            horsePanel.add(new JLabel("Select horse:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            horsePanel.add(selectHorseFromAll, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            horsePanel.add(new JLabel("Select saddle:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            horsePanel.add(saddleCombo, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            horsePanel.add(new JLabel("Select horseshoes: "), gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            horsePanel.add(horseshoesCombo, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            horsePanel.add(new JLabel("Select bridle: "), gbc);

            gbc.gridx = 1;
            gbc.gridy = 3;
            horsePanel.add(bridleCombo, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            horsePanel.add(new JLabel("Select blanket: "), gbc);

            gbc.gridx = 1;
            gbc.gridy = 4;
            horsePanel.add(blanketCombo, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            horsePanel.add(new JLabel("Select hat: "), gbc);

            gbc.gridx = 1;
            gbc.gridy = 5;
            horsePanel.add(hatCombo, gbc);

            for (Component c : horsePanel.getComponents()){
                if (c instanceof JLabel){
                    c.setBackground(lightest);
                }
            }


            // confirm button to add horse with selected equipment
            JButton confirm = new JButton("Add Horse to Lane " + i);
            final int INDEX = i;
            confirm.addActionListener(e->{
                String horseName = (String)selectHorseFromAll.getSelectedItem();

                for (int j = 0; j < racingHorses.length; j++){
                    if (racingHorses[j] != null && racingHorses[j].getName().equals(horseName) && j != INDEX-1){
                        JLabel alert = new JLabel("Horse already selected for lane " + (j+1) + ".");
                        JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }

                allHorses.forEach(horse->{
                    if (horse.getName().equals(horseName)){
                        // set equipment for horse
                        horse.setSaddle((String)saddleCombo.getSelectedItem());
                        horse.setHorseshoes((String)horseshoesCombo.getSelectedItem());
                        horse.setBridle((String)bridleCombo.getSelectedItem());
                        horse.setBlanket((String)blanketCombo.getSelectedItem());
                        horse.setHat((String)hatCombo.getSelectedItem());

                        racingHorses[INDEX-1] = horse;
                    }
                });

                refreshLaneInfoTable(tablePanel);
                workingPanel.add(tablePanel);
                
            });

            gbc.gridx = 1;
            gbc.gridy = 6;
            horsePanel.add(confirm, gbc);
        }

        customisationPanel.setBackground(background);
        customisationPanel.add(horseTabPanel);

        infoAndCustomisationPanel.add(customisationPanel);

        workingPanel.add(infoAndCustomisationPanel);
        
        // show table with which horse (if any) is in a lane
        // JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        tablePanel.setBackground(background);
        refreshLaneInfoTable(tablePanel);
        workingPanel.add(tablePanel);

        

        frames.get("horseSelectionScreen").add(workingPanel);
        return ;//frames.get("horseSelectionScreen");
    }

    // helper used by: developHorseSelectionScreen()
    private static void refreshLaneInfoTable(JPanel tablePanel){
        tablePanel.removeAll();

        // display each horse's odds in a table
        String[] columnNames = {"Lane", "Horse Name", "Symbol", "Breed", "Speed", "Stamina", "Win Ratio", "Confidence"};

        Object[][] laneData = new Object[numLanes][6];

        for (int lane = 1; lane <= numLanes; lane++){
            Object[] temp;
            if (racingHorses[lane-1] != null){
                racingHorses[lane-1].prepareForRace();
                temp = new Object[]{lane, racingHorses[lane-1].getName(), racingHorses[lane-1].getSymbol(), racingHorses[lane-1].getBreed().getBreed(),
                    racingHorses[lane-1].getSpeed(), racingHorses[lane-1].getStamina(), racingHorses[lane-1].getWinRatio(), racingHorses[lane-1].getConfidence()};
            }
            else{
                temp = new Object[]{lane, " ", " ", " ", " ", " "};
            }
            
            laneData[lane-1]=temp;
        }

        JTable laneInfoTable = new JTable(laneData, columnNames);
        //laneInfoTable.setFillsViewportHeight(true);

        DefaultTableModel nonEditable = new DefaultTableModel(laneData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        laneInfoTable.setModel(nonEditable);

        JScrollPane scrollPane = new JScrollPane(laneInfoTable);

        tablePanel.add(laneInfoTable.getTableHeader(), BorderLayout.PAGE_START);
        tablePanel.add(laneInfoTable, BorderLayout.CENTER);

        tablePanel.revalidate();
        tablePanel.repaint();   

        return;
    }
    
    public static void developHorseCreationScreen(HashMap<String, JFrame> frames){//JFrame createHorseScreen){
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e->{
            frames.get("createHorseScreen").dispose();
        });

        JPanel horseCreationNavPanel = createNavigationPanel(new JButton[]{
            cancel
        });
        frames.get("createHorseScreen").add(horseCreationNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));
        workingPanel.setBackground(background);
        workingPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(lightest);
        infoPanel.setLayout(new GridLayout(1,0));   // left: info, right: breed table

        JEditorPane infoPane = new JEditorPane();
        infoPane.setBorder(new EmptyBorder(25,25,25,25));
        infoPane.setBackground(lightest);
        infoPane.setContentType("text/html");
        infoPane.setEditable(false);
        infoPane.setText("<h1>Create your own horse!</h1>"+
                         "<p>First choose the horse's <strong>name</strong>, <strong>confidence</strong> (must be between 0 and 1 exclusive), and a <strong>character symbol</strong> to represent the horse.</p>"+
                         "<p>Then select the horse's <strong>breed</strong> and <strong>coat colour</strong>.</p>"+
                         "<p>The horse's breed will determine the horse's <strong>speed</strong> and <strong>stamina</strong>. Details about each breed are shown on the right!</p>");


        infoPanel.add(infoPane);

        // make breed info table
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(lightest);
        tablePanel.setLayout(new GridLayout(0,1));
        tablePanel.setBorder(new EmptyBorder(20, 20, 100, 20));

        String[] columnNames = {"Breed", "Speed", "Stamina"};

        Object[][] breedData = new Object[allBreeds.size()][5];

        for (int i = 0; i < allBreeds.size(); i++){
            Breed breed = allBreeds.get(i);
            Object[] temp = new Object[]{breed.getBreed(), breed.getSpeed(), breed.getStamina()};
            breedData[i]=temp;
        }

        JTable breedInfoTable = new JTable(breedData, columnNames);

        DefaultTableModel nonEditable = new DefaultTableModel(breedData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        breedInfoTable.setModel(nonEditable);

        breedInfoTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(breedInfoTable);

        tablePanel.add(breedInfoTable.getTableHeader(), BorderLayout.PAGE_START);
        tablePanel.add(breedInfoTable, BorderLayout.CENTER);

        infoPanel.add(tablePanel);

        JPanel createHorsePanel = new JPanel();
        createHorsePanel.setBackground(lightest);
        createHorsePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        
        JLabel confidenceLabel = new JLabel("Confidence (0 < x < 1):");
        JTextField confidenceField = new JTextField(15);
        
        JLabel repLabel = new JLabel("Symbol (a character or emoji):");
        JTextField repField = new JTextField(15);

        String[] breedOptions = new String[allBreeds.size()];
        for (int i = 0; i < allBreeds.size(); i++){
            breedOptions[i] = allBreeds.get(i).getBreed();
        }
        JComboBox<String> breedComboBox = new JComboBox<>(breedOptions);

        String[] colourOptions = {"White", "Brown", "Black", "Cream", "Rainbow"};
        JComboBox<String> colourComboBox = new JComboBox<>(colourOptions);

        JButton create = new JButton("Create!");
        create.addActionListener(e->{

            // make sure all text fields have input
            if (nameField.getText().length() == 0 || confidenceField.getText().length() == 0 || repField.getText().length() == 0){
                JLabel alert = new JLabel("All fields must be filled in.");
                JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);

                return;
            }

            // validate input for confidence
            if (!(isDouble(confidenceField.getText()) && Double.parseDouble(confidenceField.getText()) > 0 && Double.parseDouble(confidenceField.getText()) < 1)){
                JLabel alert = new JLabel("Confidence must be a number, and be between 0 and 1 exclusive.");
                JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);

                return;
            }

            // validate input for symbol
            if (repField.getText().length() != 1){
                JLabel alert = new JLabel("Symbol must be a single character.");
                JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);

                return;
            }

            // make sure symbol is unique
            if (!isSymbolUnique(repField.getText().charAt(0))){
                JLabel alert = new JLabel("Symbol must be unique. Your symbol has already been assigned to another horse.");
                JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);

                return;
            }

            String name = nameField.getText();
            double confidence = Double.parseDouble(confidenceField.getText());
            String symbol = repField.getText();
            String breed = (String)breedComboBox.getSelectedItem();
            String colour = (String)colourComboBox.getSelectedItem();

            Breed selectedBreed = null;
            Breed[] selection = {null};

            allBreeds.forEach(knownBreed ->{
                if (knownBreed.getBreed().equals(breed)){
                    selection[0] = knownBreed;
                }
            });

            if (selection[0] == null){
                selectedBreed = new Breed("Default Breed", 10, 10);
            }
            else{
                selectedBreed = selection[0];
            }

            // create new horse
            Horse newHorse = new Horse(symbol.charAt(0), name, confidence, colour, selectedBreed);
            allHorses.add(newHorse);
            System.out.println("Added new horse!");

            writeAllHorsesToFile();


            
            // refresh horseSelectionScreen
            frames.get("horseSelectionScreen").dispose();
            JFrame newFrame = createJFrame(NAME_BASE + " - Horse Setup");
            frames.put("horseSelectionScreen", newFrame);
            developHorseSelectionScreen(frames);
            //frames.get("horseSelectionScreen").revalidate();
            //frames.get("horseSelectionScreen").repaint();

            switchFrames(frames.get("createHorseScreen"), frames.get("horseSelectionScreen"));

            // close createHorseScreen
            frames.get("createHorseScreen").dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        createHorsePanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        createHorsePanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        createHorsePanel.add(confidenceLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        createHorsePanel.add(confidenceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        createHorsePanel.add(repLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        createHorsePanel.add(repField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        createHorsePanel.add(new JLabel("Choose breed"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        createHorsePanel.add(breedComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        createHorsePanel.add(new JLabel("Choose coat colour"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        createHorsePanel.add(colourComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        createHorsePanel.add(create, gbc);

        workingPanel.add(infoPanel);
        workingPanel.add(createHorsePanel);

        frames.get("createHorseScreen").add(workingPanel, BorderLayout.CENTER);

        return;// frames.get("createHorseScreen");
    }

    // helper used by: developHorseCreationScreen()
    public static boolean isDouble(String input){
        boolean dotFound = false;

        if (input.length()==0){
            return false;
        }

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

    // helper used by: developHorseCreationScreen()
    public static boolean isSymbolUnique(char c){
        for (Horse h : allHorses){
            if (h.getSymbol() == c){
                return false;
            }
        }
        return true;
    }

    public static void developBettingSetupScreen(HashMap<String, JFrame> frames){//JFrame bettingSetupScreen, JFrame horseSelectionScreen, JFrame preRaceBettingScreen){
        // bettingSetupScreen nav
        JTextField applesNumInput = new JTextField(5);

        JButton createBettingAccount = new JButton("Create Betting Account");
        createBettingAccount.addActionListener(e->{
            JFrame newFrame = createJFrame(NAME_BASE + " - Create Betting Account");
            frames.put("bettingAccountCreationScreen", newFrame);
            developBettingAccountCreationScreen(frames);
            frames.get("bettingAccountCreationScreen").setVisible(true);
        });

        JButton startBetting = new JButton("Start Betting");
        startBetting.addActionListener(e->{

            bettingAccounts = new ArrayList<BettingAccount>();
            boolean canProceed = false;

            // process which betting accounts are active and ensure at least 1 account is playing
            for (BettingAccount bettingAccount : allBettingAccounts){
                if (bettingAccount.isActive()){
                    bettingAccounts.add(bettingAccount);
                    canProceed = true;
                }
            }

            if (canProceed){
                numUsers = bettingAccounts.size();

                // process input for initial number of apples
                if (applesNumInput.getText().length() > 0){
                    String input = applesNumInput.getText();

                    if (!isInt(input)){
                        JLabel alert = new JLabel("Number of apples must be a whole number.");
                        JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (Integer.parseInt(input) < 1){
                        JLabel alert = new JLabel("Number of apples must be at least 1.");
                        JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    initialApples = Integer.parseInt(input);
                }
                else{
                    initialApples = 30;
                }

                // set all accounts with 0 balance to initialApples
                for (BettingAccount account : bettingAccounts){
                    if (account.getBalance() == 0){
                        account.setBalance(initialApples);
                    }
                }

                JFrame newFrame = createJFrame(NAME_BASE + " - Pre-Race Betting");
                frames.put("preRaceBettingScreen", newFrame);
                developPreRaceBettingScreen(frames);
                /*frames.get("preRaceBettingScreen").revalidate();
                frames.get("preRaceBettingScreen").repaint();*/
                switchFrames(frames.get("bettingSetupScreen"), frames.get("preRaceBettingScreen"));

            }
            else{
                JLabel alert = new JLabel("There must be at least 1 player!");
                JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }


            // reset bettingAccounts ArrayList
            /*bettingAccounts = new ArrayList<BettingAccount>();
            for (int i = 0; i < numUsers; i++){
                bettingAccounts.add(new BettingAccount(initialApples));
            }*/


            
        });

        JPanel bettingSetupNavPanel = createNavigationPanel(new JButton[]{
            addSwitchButton("Back to Horse Setup", frames.get("bettingSetupScreen"), frames.get("horseSelectionScreen")),
            createBettingAccount,
            startBetting
        });
        frames.get("bettingSetupScreen").add(bettingSetupNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));
        workingPanel.setBackground(background);
        workingPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // instructions + information
        JEditorPane info = new JEditorPane();
        info.setBorder(new EmptyBorder(25,25,25,25));
        info.setBackground(lightest);
        info.setContentType("text/html");
        info.setEditable(false);
        info.setText("<p>Here, you can <strong>choose how many users will be betting</strong>, and you can <strong>choose how many apples (the betting currency)</strong> will be given to each user to start with.\n" +
                     "The default number of apples per user is 30.\n\n" +
                     "Before each race, you'll be able to <strong>see each horse and its odds</strong>, as well as the <strong>total amount bet on each horse</strong>.</p>" +
                     "<p>Each user can <strong>place a bet</strong> on whichever horse they would like. <em>If a user doesn't want to bet, simply select any horse and set the bet amount to 0 apples.</em></p>");

        info.setBorder(new EmptyBorder(20, 20, 20, 20));
        workingPanel.add(info);


        // select which users are playing
        JPanel userNumPanel = new JPanel(new GridLayout(0,1));
        userNumPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        userNumPanel.setBackground(lightest);
        userNumPanel.add(new JLabel("Which users will be betting for these races?"));

        JPanel optionsPanel = new JPanel(new FlowLayout());
        optionsPanel.setBackground(lightest);

        String betUsernamesArray[] = new String[allBettingAccounts.size()];
        for (int i = 0; i < allBettingAccounts.size(); i++){
            JCheckBox user = new JCheckBox(allBettingAccounts.get(i).getUsername());
            user.setBackground(lightest);
            final int INDEX = i;
            user.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (user.isSelected()){
                        allBettingAccounts.get(INDEX).setActive(true);
                    }
                    else{
                        allBettingAccounts.get(INDEX).setActive(false);
                    }
                }
            });
            optionsPanel.add(user);
        }

        userNumPanel.add(optionsPanel);
        
        workingPanel.add(userNumPanel);

        // select number of start apples
        JPanel applesNumPanel = new JPanel();
        applesNumPanel.setBackground(lightest);
        applesNumPanel.add(new JLabel("How many apples should users with 0 apples get?"));

        //JTextField applesNumInput = new JTextField(5); added earlier
        applesNumPanel.add(applesNumInput);
        workingPanel.add(applesNumPanel);
        
        frames.get("bettingSetupScreen").add(workingPanel);


        return;// frames.get("bettingSetupScreen");
    }

    public static void developBettingAccountCreationScreen(HashMap<String, JFrame> frames){//JFrame createHorseScreen){
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e->{
            frames.get("bettingAccountCreationScreen").dispose();
        });

        JPanel bettingAccountCreationNavPanel = createNavigationPanel(new JButton[]{
            cancel
        });
        frames.get("bettingAccountCreationScreen").add(bettingAccountCreationNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));
        workingPanel.setBackground(background);

        JLabel usernameLabel = new JLabel("What is your username?");
        workingPanel.add(usernameLabel);

        JTextField usernameField = new JTextField(20);
        workingPanel.add(usernameField);

        JButton create = new JButton("Create Betting Account!");
        create.addActionListener(e->{
            String username = usernameField.getText();

            // make username has been input
            if (username.length() == 0){
                JLabel alert = new JLabel("Username field must be filled in.");
                JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);

                return;
            }

            // make sure username is unique
            if (!isUsernameUnique(username)){
                JLabel alert = new JLabel("Username must be unique. Your username has already been assigned to another bettingAccount.");
                JOptionPane.showMessageDialog(null, alert, "Warning", JOptionPane.WARNING_MESSAGE);

                return;
            }

            // create new betting account
            BettingAccount newAccount = new BettingAccount(username, initialApples);
            allBettingAccounts.add(newAccount);
            System.out.println("Added new betting account!");

            writeBettingAccountsToFile();

            
            // refresh bettingSetupScreen
            frames.get("bettingSetupScreen").dispose();
            JFrame newFrame = createJFrame(NAME_BASE + " - Betting Setup");
            frames.put("bettingSetupScreen", newFrame);
            developBettingSetupScreen(frames);

            switchFrames(frames.get("bettingAccountCreationScreen"), frames.get("bettingSetupScreen"));
            frames.get("bettingAccountCreationScreen").dispose();

        });

        workingPanel.add(create);
        workingPanel.setBorder(new EmptyBorder(200,200,200,200));
        frames.get("bettingAccountCreationScreen").add(workingPanel, BorderLayout.CENTER);

        return;
    }

    public static boolean isUsernameUnique(String s){
        for (BettingAccount account : allBettingAccounts){
            if (account.getUsername().equals(s)){
                return false;
            }
        }
        return true;
    }

    public static void developPreRaceBettingScreen(HashMap<String, JFrame> frames){//JFrame preRaceBettingScreen, JFrame raceScreen){

        // preRaceBettingScreen nav
        JButton startRace = new JButton("Start Race!");
        startRace.addActionListener(e->{

            // prepare all horses
            for (Horse h : racingHorses){
                if (h != null){
                    h.prepareForRace();
                }
            }

            JFrame newFrame = createJFrame(NAME_BASE + " - Race");
            frames.put("raceScreen", newFrame);
            developRaceScreen(frames);
            switchFrames(frames.get("preRaceBettingScreen"), frames.get("raceScreen"));
        });
        JPanel preRaceBettingNavPanel;
        
        if (currentRaceNum == 1){
            preRaceBettingNavPanel = createNavigationPanel(new JButton[]{
                addSwitchButton("Back to Betting Setup", frames.get("preRaceBettingScreen"), frames.get("bettingSetupScreen")),
                startRace
            });
        }
        else{
            preRaceBettingNavPanel = createNavigationPanel(new JButton[]{
                startRace
            });
        }

        frames.get("preRaceBettingScreen").add(preRaceBettingNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));
        workingPanel.setBackground(background);

        // display each horse's odds in a table
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(background);
        tablePanel.setLayout(new BorderLayout());

        tablePanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        refreshHorseInfoTable(tablePanel);
        workingPanel.add(tablePanel);

        // create a tab menu so you can access and edit each user's bet
        JTabbedPane userTabs = new JTabbedPane();
        JPanel bettingPanel = new JPanel();
        bettingPanel.setBackground(background);
        for (int i = 1; i <= numUsers; i++){
            JPanel bettingTabPanel = new  JPanel(new GridLayout(0,1));

            if (bettingAccounts.size() > 0){
                BettingAccount account = bettingAccounts.get(i-1);
                
                /* create slider to set bets
                JSlider betSlider = new JSlider(0, account.getBalance(), 0);
                betSlider.setMajorTickSpacing(10);
                betSlider.setMinorTickSpacing(5);
                betSlider.setPaintTicks(true);
                betSlider.setPaintLabels(true);*/

                // select the bet amount
                JPanel betAmountPanel = new JPanel();
                betAmountPanel.setBackground(lightest);
                betAmountPanel.add(new JLabel("How many apples would you like to bet?"));
                JTextField betInput = new JTextField(5);

                JButton confirm = new JButton("Confirm Bet");

                ArrayList<String> horseNames = new ArrayList<String>();
                for (Horse horse : racingHorses){
                    if (horse != null){
                        horseNames.add(horse.getName());
                    }
                }
                String[] horseOptions = new String[horseNames.size()];
                for (int j = 0; j < horseNames.size(); j++){
                    horseOptions[j] = horseNames.get(j);
                }
                
                JComboBox<String> selectBettingHorse = new JComboBox<String>(horseOptions);

                confirm.addActionListener(e -> {
                    String betAmountString = betInput.getText();

                    if (betAmountString.length() > 0 && isInt(betAmountString)){
                        int betAmount = Integer.parseInt(betAmountString);
                        String selectedHorseName = (String)selectBettingHorse.getSelectedItem();

                        // iterate through the horse list to find the selected horse
                        Horse selectedHorse = null;
                        for (Horse horse : racingHorses) {
                            if (horse!=null && horse.getName().equals(selectedHorseName)) {
                                selectedHorse = horse;
                                break;
                            }
                        }

                        // bet is greater than balance
                        if (betAmount > account.getBalance()){
                            JLabel alert = new JLabel("You cannot bet more apples than you have.");
                            JOptionPane.showMessageDialog(bettingTabPanel, alert, "Warning", JOptionPane.WARNING_MESSAGE);
                            
                            account.makeBet(betAmount, selectedHorse);
                        }
                        // bet is valid
                        else{
                            if (betAmount == account.getBalance()){
                                JLabel alert = new JLabel("Warning: you are betting all of your apples.");
                                JOptionPane.showMessageDialog(bettingTabPanel, alert, "Warning", JOptionPane.WARNING_MESSAGE);
                            }

                            account.makeBet(betAmount, selectedHorse);
                        }

                        // refresh horse info table
                        refreshHorseInfoTable(tablePanel);
                    }
                    else{
                        JLabel alert = new JLabel("Invalid bet.");
                        JOptionPane.showMessageDialog(bettingTabPanel, alert, "Warning", JOptionPane.WARNING_MESSAGE);
                    }


                });

                bettingTabPanel.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);
                
                bettingPanel.add(bettingTabPanel);
                bettingTabPanel.setBackground(lightest);
                userTabs.addTab(account.getUsername(), bettingTabPanel);

                for (Component c : bettingTabPanel.getComponents()){
                    if (c instanceof JLabel){
                        c.setBackground(lightest);
                    }
                }

                gbc.gridx = 0;
                gbc.gridy = 0;
                bettingTabPanel.add(new JLabel("Current Balance: " + account.getBalance()), gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                bettingTabPanel.add(new JLabel("Select Horse:"), gbc);

                gbc.gridx = 1;
                gbc.gridy = 1;
                bettingTabPanel.add(selectBettingHorse, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                bettingTabPanel.add(new JLabel("Select Bet Amount:"), gbc);

                gbc.gridx = 1;
                gbc.gridy = 2;
                bettingTabPanel.add(betInput, gbc);

                gbc.gridx = 0;
                gbc.gridy = 3;
                bettingTabPanel.add(confirm, gbc);
            }
        }

        bettingPanel.add(userTabs);
        workingPanel.add(bettingPanel);

        frames.get("preRaceBettingScreen").add(workingPanel);

        return;// frames.get("preRaceBettingScreen");
    }

    // helper used by: developPreRaceBettingScreen()
    private static void refreshHorseInfoTable(JPanel tablePanel){
        tablePanel.removeAll();

        // display each horse's odds in a table
        String[] columnNames = {"Lane", "Horse Name", "Symbol", "Speed", "Stamina", "Confidence", "Win Ratio", "Total Apples Bet", "Betting Odds"};

        Object[][] horseData = new Object[racingHorses.length][5];

        // get total amount that has been bet
        int[] totalApples = {0};
        bettingAccounts.forEach(account -> {
            // make sure both account and account.getBetHorse() don't return null
            if (!(account==null)){
                totalApples[0] = totalApples[0] + account.getBetAmount();
            }
        });

        if (racingHorses != null && numHorses>0){
            for (int i = 0; i < racingHorses.length; i++){
                Horse horse = racingHorses[i];
                if (! (horse==null)){
                    int betAmount = 0;
                    double horseOdds = 0.0;
                    
                    // calculate how much has been bet on this horse
                    for (BettingAccount account : bettingAccounts){
                        // make sure both account and account.getBetHorse() don't return null
                        if ( (!(account==null)) && (!(account.getBetHorse()==null)) && account.getBetHorse().equals(horse)){
                            betAmount = betAmount + account.getBetAmount();
                            
                        }
                    }
                    
                    horseOdds = calculateHorseOdds(horse, betAmount, totalApples[0]);
                    String oddsString = "1 bet -> ~" + (int)horseOdds + " apple";

                    if ((int)horseOdds != 1){
                        oddsString = oddsString + "s";
                    }

                    Object[] temp = new Object[]{(i+1), horse.getName(), horse.getSymbol(), horse.getSpeed(), horse.getStamina(), horse.getConfidence(), horse.getWinRatio(), betAmount, oddsString};
                    horseData[i]=temp;
                }
                else{
                    Object[] temp = new Object[]{(i+1), " ", " ", " ", " "};
                    horseData[i]=temp;
                }
            }
        }

        JTable horseInfoTable = new JTable(horseData, columnNames);

        DefaultTableModel nonEditable = new DefaultTableModel(horseData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        horseInfoTable.setModel(nonEditable);

        horseInfoTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(horseInfoTable);

        tablePanel.add(horseInfoTable.getTableHeader(), BorderLayout.PAGE_START);
        tablePanel.add(horseInfoTable, BorderLayout.CENTER);

        tablePanel.revalidate();
        tablePanel.repaint();   
    }

    // helper used by: developPreRaceBettingScreen()
    private static double calculateHorseOdds(Horse horse, int betAmount, int totalAmountToBet){
        Breed breed = horse.getBreed();
        double odds;
        double betProportion = 0.1;

        if (totalAmountToBet > 0 && betAmount > 0){
            // what proportion of the total start apples has been bet on this horse?
            betProportion = (double)betAmount / (double)totalAmountToBet;
            if (betProportion > 0.5){
                betProportion = 0.5;
            }
        }
        
        odds = (1/((double)(breed.getSpeed() + breed.getStamina() -35))) * ((1/(3*betProportion)) - 0.5) * 500 * horse.getConfidence();

        if (odds < 1){
            odds = 1;
        }
        
        return odds;
    }

    public static void developRaceScreen(HashMap<String, JFrame> frames){//JFrame raceScreen, JFrame postRaceDisplayScreen){
        // raceScreen nav
        boolean[] raceFinished = {false};

        JButton toPostRace = new JButton("Finish Race");
        toPostRace.addActionListener(e->{
            if (raceFinished[0]){
                
                // get winner
                for (Horse h : racingHorses){
                    if (! (h==null)){
                        if (h.getDistanceTravelled() >= selectedTrack.getRaceLength()){
                            winningHorse = h;
                            h.winRace();
                        }
                        else{
                            h.loseRace();
                        }
                    }
                }

                // write all horses to file to update confidence
                writeAllHorsesToFile();

                JFrame newFrame = createJFrame(NAME_BASE + "Post-Race Display");
                frames.put("postRaceDisplayScreen", newFrame);
                developPostRaceDisplayScreen(frames);
                /*frames.get("postRaceDisplayScreen").revalidate();
                frames.get("postRaceDisplayScreen").repaint();*/
                switchFrames(frames.get("raceScreen"), frames.get("postRaceDisplayScreen"));
            }
            else{
                JOptionPane.showMessageDialog(null, "The race hasn't finished!");
            }
        });

        JPanel raceNavPanel = createNavigationPanel(new JButton[]{
            toPostRace
        });
        frames.get("raceScreen").add(raceNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new BorderLayout());
        workingPanel.setBackground(background);
        workingPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        JLabel startRaceLabel = new JLabel("Ready to start...");
        JPanel startRacePanel = new JPanel(new GridLayout(1,0));
        startRacePanel.setBackground(background);


        JButton startRace = new JButton("Start Race!");
        startRace.addActionListener(e->{
            startRaceLabel.setText("The race has begun!");

            JPanel racePanel = new JPanel();       
            racePanel.setMaximumSize(new Dimension((selectedTrack.getRaceLength()+50), 500));     
            racePanel.setLayout(new GridLayout(0,1));
            workingPanel.add(racePanel, BorderLayout.CENTER);

            // give each horse a panel and a track, and set the conditions
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);


            JPanel [] lanes;

            // single lane for oval
            if (selectedTrack.getTrackName().equals("Oval")){
                lanes = new JPanel[1];
            }
            // multiple lanes for line and zigzag
            else{
                lanes = new JPanel[racingHorses.length];
            }
        
            // create lanes
            for (int i = 0; i < lanes.length; i++){
                Horse h = racingHorses[i];

                // add lane to lanes array
                lanes[i] = new JPanel();
                lanes[i].setMaximumSize(new Dimension(selectedTrack.getRaceLength()+50, -1));
                lanes[i].setLayout(null);

                // set different colour border and backgrounds depending on the weather
                if (conditions[currentRaceNum-1][3].equals("wet")){
                    lanes[i].setBackground(new Color(12, 214, 110));
                    lanes[i].setBorder(new LineBorder(new Color(22, 221, 25), 20));
                }
                else if (conditions[currentRaceNum-1][3].equals("icy")){
                    lanes[i].setBackground(new Color(159, 249, 249));
                    lanes[i].setBorder(new LineBorder(new Color(10, 221, 104), 20));
                }
                else if (conditions[currentRaceNum-1][3].equals("muddy")){
                    lanes[i].setBackground(new Color(163, 82, 6));
                    lanes[i].setBorder(new LineBorder(new Color(22, 221, 25), 20));
                }
                else{
                    lanes[i].setBackground(new Color(232, 224, 2));
                    lanes[i].setBorder(new LineBorder(new Color(22, 221, 25), 20));
                }

                racePanel.add(lanes[i]);
            }

            for (Horse h : racingHorses){
                // get horse ready for race
                if (h!=null){
                    h.setTrack(selectedTrack.copyTrack());
                    h.setConditions(conditions[currentRaceNum-1][0].equals("sunny"),
                        conditions[currentRaceNum-1][1].equals("rainy"), conditions[currentRaceNum-1][2].equals("foggy"), conditions[currentRaceNum-1][3]);
                    //h.setTrack(offsetTracks[i]);
                }
            }

            RaceSwingWorker raceSwingWorker = new RaceSwingWorker(racingHorses, selectedTrack, conditions[currentRaceNum-1][3], lanes, workingPanel, racePanel, startRaceLabel, startRacePanel, startRace);
            raceSwingWorker.execute();

            raceFinished[0] = true;

            /*startRacePanel.remove(startRace);
            startRacePanel.revalidate();
            startRacePanel.repaint();
            
            startRaceLabel.setText("The race has finished!");*/
        });

        startRacePanel.add(startRaceLabel);
        startRacePanel.add(startRace);
        
        workingPanel.add(startRacePanel, BorderLayout.NORTH);

        frames.get("raceScreen").add(workingPanel);

        return;// frames.get("raceScreen");
    }

    public static void developPostRaceDisplayScreen(HashMap<String, JFrame> frames){//JFrame postRaceDisplayScreen, JFrame preRaceBettingScreen, JFrame postWeekBettingDisplayScreen){
        // postRaceDisplayScreen nav
        JButton nextRace = new JButton("Next Race");
        nextRace.addActionListener(e->{
            currentRaceNum = currentRaceNum + 1;
            writeBetsToFile();
            JFrame newFrame = createJFrame(NAME_BASE + "Pre-Race Betting");
            frames.put("preRaceBettingScreen", newFrame);
            developPreRaceBettingScreen(frames);
            /*frames.get("preRaceBettingScreen").revalidate();
            frames.get("preRaceBettingScreen").repaint();*/
            switchFrames(frames.get("postRaceDisplayScreen"), frames.get("preRaceBettingScreen"));
        });

        JButton finishAllRaces = new JButton("Finish All Races");
        finishAllRaces.addActionListener(e->{
            writeBetsToFile();
            switchFrames(frames.get("postRaceDisplayScreen"), frames.get("preRaceBettingScreen"));
        });

        JPanel postRaceNavPanel;

        // if all races are done, don't add the nextRace button
        if (currentRaceNum < numRaces){
            postRaceNavPanel = createNavigationPanel(new JButton[]{
                addSwitchButton("Finish All Races", frames.get("postRaceDisplayScreen"), frames.get("postWeekBettingDisplayScreen")),
                nextRace
            });
        }
        else{
            postRaceNavPanel = createNavigationPanel(new JButton[]{
                addSwitchButton("Finish All Races", frames.get("postRaceDisplayScreen"), frames.get("postWeekBettingDisplayScreen"))
            });
        }

        frames.get("postRaceDisplayScreen").add(postRaceNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));
        workingPanel.setBackground(background);

        // panel for table of horse data
        JPanel horseTablePanel = new JPanel();
        horseTablePanel.setLayout(new BorderLayout());
        horseTablePanel.setBackground(background);
        horseTablePanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        
        String[] horseTableColumnNames = {"Lane", "Horse Name", "Symbol", "Previous Confidence", "Confidence", "Change in Confidence", "Total Apples Bet", "Betting Odds", "Finish Time", "Distance Travelled", "Average Speed", "Result"};

        Object[][] horseData = new Object[numHorses][8];

        int laneIndex = 1;
        int[] laneIndexArray = {laneIndex};

        // get total amount that has been bet
        int[] totalApples = {0};
        bettingAccounts.forEach(account -> {
            // make sure both account and account.getBetHorse() don't return null
            if (!(account==null)){
                totalApples[0] = totalApples[0] + account.getBalance();
            }
        });

        if (racingHorses != null){

            if (selectedTrack != null){
                // save track & race details
                Boolean writeHeader = false;
                File file = new File(selectedTrack.getTrackName() + "_history.csv");
                if (!file.exists()){
                    writeHeader = true;
                }

                try (PrintWriter pw = new PrintWriter( new FileOutputStream(file, true) )){
                    if (writeHeader){
                        pw.println("Date,Track Conditions,Sunny?,Rainy?,Foggy?,Finish Time,% Horses Fallen");
                    }
                    double totalFallen = 0;
                    for (Horse h : racingHorses){
                        if (h != null){
                            if (h.hasFallen()){
                                totalFallen++;
                            }
                        }
                    }

                    double percHorsesFallen = (totalFallen / (double)numHorses) / 100;

                    // if all horses fell
                    if (totalFallen == (double)numHorses){
                        pw.println(new Date() + "," + conditions[currentRaceNum-1][3] + "," + conditions[currentRaceNum-1][0] + "," + conditions[currentRaceNum-1][1] + "," + conditions[currentRaceNum-1][2] + "," + "No Horses Finished" + "," + (percHorsesFallen+"%"));
                    }
                    // not all horses fell (so there was a winner)
                    else{
                        if (winningHorse != null){
                            pw.println(new Date() + "," + conditions[currentRaceNum-1][3] + "," + conditions[currentRaceNum-1][0] + "," + conditions[currentRaceNum-1][1] + "," + conditions[currentRaceNum-1][2] + "," + winningHorse.getFinishTime() + "," + percHorsesFallen);
                        }
                        else{
                            pw.println(new Date() + "," + conditions[currentRaceNum-1][3] + "," + conditions[currentRaceNum-1][0] + "," + conditions[currentRaceNum-1][1] + "," + conditions[currentRaceNum-1][2] + "," + "N/A" + "," + percHorsesFallen);
                        }
                    }
                    
                }
                catch (IOException e){
                    System.out.println("Error: couldn't write race to track history file");
                }
            }


            for (Horse horse : racingHorses){
                if (! (horse==null)){
                    int[] betAmount = {0};
                    horse.setOdds(0);
                    
                    // calculate how much has been bet on each horse
                    bettingAccounts.forEach(account -> {
                        // make sure both account and account.getBetHorse() don't return null
                        if ( (!(account==null)) && (!(account.getBetHorse()==null)) && account.getBetHorse().equals(horse)){
                            betAmount[0] = betAmount[0] + account.getBetAmount();
                        }
                    });
                    
                    horse.setOdds(calculateHorseOdds(horse, betAmount[0], totalApples[0]));
                    String oddsString = "1 -> ~" + (int)horse.getOdds();

                    double averageSpeed;

                    String result;
                    if (horse.hasFallen()){
                        result = "Fell";
                    }
                    else if (horse.equals(winningHorse)){
                        result = "Won";
                    }
                    else{
                        result = "Lost";
                    }

                    String finishTimeStr = horse.getFinishTime() + "s";

                    if (!(horse.getDistanceTravelled() >= selectedTrack.getRaceLength())){
                        finishTimeStr = "Did not finish";
                    }

                    Object[] temp = new Object[]{laneIndexArray[0], horse.getName(), horse.getSymbol(), horse.getPreviousConfidence(), horse.getConfidence(), (horse.getConfidence() - horse.getPreviousConfidence()), betAmount[0], oddsString, finishTimeStr, ((int)horse.getDistanceTravelled() + " pxl"), ((int)(horse.getDistanceTravelled()/horse.getFinishTime()) + " pxl / sec"), result};
                    horseData[laneIndexArray[0]-1]=temp;
                    laneIndexArray[0]++;

                    // write to horse history file
                    horse.writeRaceToHistory(new Date(), result, (horse.getFinishTime()/selectedTrack.getRaceLength()));
                }
            }
        }


        JTable horseInfoTable = new JTable(horseData, horseTableColumnNames);
        horseInfoTable.setFillsViewportHeight(true);
        
        DefaultTableModel nonEditable = new DefaultTableModel(horseData, horseTableColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        horseInfoTable.setModel(nonEditable);

        JScrollPane horseScrollPane = new JScrollPane(horseInfoTable);

        horseTablePanel.add(horseInfoTable.getTableHeader(), BorderLayout.PAGE_START);
        horseTablePanel.add(horseInfoTable, BorderLayout.CENTER);
        
        workingPanel.add(horseTablePanel);

        // panel for table of account data
        JPanel accountTablePanel = new JPanel();
        accountTablePanel.setBackground(background);
        accountTablePanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        
        String[] accountTableColumnNames = {"User", "Horse", "Apples", "Result", "Feedback"};

        Object[][] accountData = new Object[numUsers][4];

        int userIndex = 1;
        int[] userIndexArray = {userIndex};

        bettingAccounts.forEach(account -> {

            if (account.getBetAmount() > 0){

                String horseName = account.getBetHorse().getName();
                
                String result;
                String feedback;
                if (account.getBetHorse()==(winningHorse)){
                    result = "Won";
                    feedback = "Bet on horses with higher odds for bigger payouts.";
                    account.winBet();
                }
                else{
                    result = "Lost";
                    feedback = "Bet on horses with lower odds for better chance of success.";
                    account.loseBet();
                }

                Object[] temp = new Object[]{userIndexArray[0], horseName, account.getBalance(), result, feedback};
                accountData[userIndexArray[0]-1]=temp;
                userIndexArray[0]++;
            }
            // user didn't bet
            else{
                Object[] temp = new Object[]{userIndexArray[0], "No Horse", account.getBalance(), "Did not bet", "Bet on horses for a chance to win."};
                accountData[userIndexArray[0]-1]=temp;
                userIndexArray[0]++;
            }
        });

        JTable accountInfoTable = new JTable(accountData, accountTableColumnNames);
        accountInfoTable.setFillsViewportHeight(true);

        nonEditable = new DefaultTableModel(accountData, accountTableColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        accountInfoTable.setModel(nonEditable);

        JScrollPane accountScrollPane = new JScrollPane(accountInfoTable);

        accountTablePanel.setLayout(new BorderLayout());
        accountTablePanel.add(accountInfoTable.getTableHeader(), BorderLayout.PAGE_START);
        accountTablePanel.add(accountInfoTable, BorderLayout.CENTER);
        
        workingPanel.add(accountTablePanel);

        frames.get("postRaceDisplayScreen").add(workingPanel);
        return;// frames.get("postRaceDisplayScreen");
    }

    public static void developPostWeekBettingisplayScreen(HashMap<String, JFrame> frames){//JFrame postWeekBettingDisplayScreen, JFrame homeScreen){
        // PostWeekBettingDisplayScreen nav
        JButton toHome = new JButton("Home");
        toHome.addActionListener(e->{

            // write betting accounts to file
            writeBettingAccountsToFile();

            for (BettingAccount account : allBettingAccounts){
                account.setActive(false);
            }
            
            // reset variables
            numHorses = 0;
            numLanes = 3;    
            numRaces = 7;
            currentRaceNum = 1;
            numUsers = 5;
            initialApples = 30;

            bettingAccounts = new ArrayList<BettingAccount>();
            selectedTrack = null;
            racingHorses = new Horse[numLanes];
            winningHorse = null;

            // reset all screens
            frames.put("homeScreen", createJFrame(NAME_BASE + " - Home"));
            frames.put("viewAnalyticsScreen", createJFrame(NAME_BASE + " - Analytics"));
            frames.put("raceDesignScreen", createJFrame(NAME_BASE + " - Race Setup"));
            frames.put("horseSelectionScreen", createJFrame(NAME_BASE + " - Horse Setup"));
            frames.put("bettingSetupScreen", createJFrame(NAME_BASE + " - Betting Setup"));
            frames.put("preRaceBettingScreen", createJFrame(NAME_BASE + " - Pre-Race Betting"));
            frames.put("raceScreen", createJFrame(NAME_BASE + " - Race"));
            frames.put("postRaceDisplayScreen", createJFrame(NAME_BASE + " - Post-Race Display"));
            //frames.put("postWeekBettingDisplayScreen", createJFrame(NAME_BASE + " - Final Betting Display"));

            // develop screens for specialised functions
            developHomeScreen(frames);
            developViewAnalyticsScreen(frames);
            developRaceDesignScreen(frames);
            developHorseSelectionScreen(frames);
            developBettingSetupScreen(frames);
            developPreRaceBettingScreen(frames);
            developRaceScreen(frames);
            developPostRaceDisplayScreen(frames);
            //developPostWeekBettingisplayScreen(frames);

            switchFrames(frames.get("postWeekBettingDisplayScreen"), frames.get("homeScreen"));
        });
        JPanel postWeekNavPanel = createNavigationPanel(new JButton[]{
            toHome
        });
        frames.get("postWeekBettingDisplayScreen").add(postWeekNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel();
        workingPanel.setBackground(background);

        JEditorPane pane = new JEditorPane();
        pane.setBackground(lightest);
        
        pane.setContentType("text/html");
        pane.setEditable(false);
        pane.setText("<h1>Thanks for playing!</h1>"+
                     "<ul><li>Play again!</li>"+
                     "<li>Check out the best times on each track</li>"+
                     "<li>See the horses and their previous performances</li></ul>"+
                     "Just head back '<strong>Home</strong>' and select the option you want.</p>");
    
        workingPanel.add(pane);


        frames.get("postWeekBettingDisplayScreen").add(workingPanel);

        return;// frames.get("postWeekBettingDisplayScreen");
    }

    // generic methods

    // create a generic JFrame
    public static JFrame createJFrame(String name){
        int defaultWindowX = 1000;
        int defaultWindowY = 750;

        JFrame frame = new JFrame(name);
        frame.setLocation(0,0);
        frame.setBackground(background);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(defaultWindowX, defaultWindowY);
        frame.setResizable(false);
        //frame.getContentPane().setBackground(new Color(164, 252, 249));

        return frame;
    }

    // switch from one frame to another
    public static void switchFrames(JFrame frameOff, JFrame frameOn){
        
        try{
            Point locationOff = frameOff.getLocation();
            Dimension size = frameOff.getSize();
            
            // reset size and location to match previous frame
            frameOn.setSize(size);
            frameOn.setLocation((int)locationOff.getX(), (int)locationOff.getY());

            frameOn.setVisible(true);
            frameOff.setVisible(false);
        }
        catch (IllegalComponentStateException e){
            e.printStackTrace();
        }       

        return;
    }

    // add a button to a frame, which switches from that frame to another frame
    public static JButton addSwitchButton(String buttonName, JFrame currentFrame, JFrame newFrame){
        JButton button = new JButton(buttonName);
        button.addActionListener(e -> switchFrames(currentFrame, newFrame));
        button.setSize(button.getPreferredSize());
        currentFrame.add(button);

        return button;
    }

    // add a button to a panel on a frame, which switches from that frame to another frame
    public static JButton addSwitchButton(String buttonName, JFrame currentFrame, JPanel placementPanel, JFrame newFrame){
        JButton button = new JButton(buttonName);
        button.addActionListener(e -> switchFrames(currentFrame, newFrame));
        button.setSize(button.getPreferredSize());
        placementPanel.add(button);

        return button;
    }

    // add a button to a frame, which opens a second frame
    public static JButton addMiniWindowButton(String buttonName, JFrame currentFrame, JFrame newFrame){
        JButton button = new JButton(buttonName);
        button.addActionListener(e -> newFrame.setVisible(true));
        button.setSize(button.getPreferredSize());
        currentFrame.add(button);

        return button;
    }

    // create a navigation panel on a screen
    private static JPanel createNavigationPanel(JButton[] buttons) {
        JPanel navPanel = new JPanel();
        navPanel.setBackground(navColour);
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        for (JButton button : buttons) {
            navPanel.add(button);
        }

        return navPanel;
    }

    // create a cancel button to close a window
    private static JButton createCancelButton(JFrame screen) {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> screen.dispose());
        screen.add(cancelButton);
        return cancelButton;
    }


    // general helper methods

    private static boolean isInt(String test){
        for (int i = 0; i < test.length(); i++){
            char c = test.charAt(i);
            if (c<'0' || c>'9'){
                return false;
            }
        }
        return true;
    }

    private static void writeAllHorsesToFile(){
        try (PrintWriter pw = new PrintWriter(new FileWriter("horses.csv"))){
            pw.println("horseName,horseConfidence,horseSymbol,colour,number of races, number of wins,breed,speed,stamina");

            // write all horses to CSV
            allHorses.forEach(horse -> {
                horse.storeHorseCSV(pw);
            });

            pw.close();
        }
        catch (IOException ioEx){
            System.out.println("Error: Could not write horses to file.");
        }
    }

    private static void writeBettingAccountsToFile(){
        Boolean writeHeader = true;

        File file = new File("bettingAccounts.csv");

        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file))){

            pw.println("Username,Balance");

            // write all betting accounts to CSV
            allBettingAccounts.forEach(account -> {
                Bet previousBet = account.getPreviousBet();
                pw.println(account.getUsername() + "," + account.getBalance());
            });

            pw.close();
        }
        catch (IOException ioEx){
            System.out.println("Error: Could not write bets to file.");
        }
    }

    private static void writeBetsToFile(){

        for (BettingAccount account : bettingAccounts){
            
            Boolean writeHeader = true;
            String tempName = account.getUsername().replace(" ", "_");
            File file = new File(tempName + "_history.csv");

            if (file.exists()){
                writeHeader = false;
            }

            try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, true))){

                if (writeHeader){
                    pw.println("Date,Username,Bet Amount,Horse,Result,Balance after Bet");
                }

                Bet previousBet = account.getPreviousBet();
                String prevBetAmount;
                String prevBetHorse;

                if (previousBet==null){
                    prevBetAmount = "no previous bet";
                    prevBetHorse = "no previous bet";
                }
                else{
                    prevBetAmount = previousBet.getAmount() + "";
                    if (previousBet.getHorse()!=null){
                        prevBetHorse = previousBet.getHorse().getName();
                    }
                    else{
                        prevBetHorse = "no horse";
                    }
                }

                String previousResult;

                if (account.getPreviousResult() == null){
                    previousResult = "N/A";
                }
                else{
                    previousResult = account.getPreviousResult();
                }

                pw.println(new Date() + "," + account.getUsername() + "," + prevBetAmount + "," + prevBetHorse + "," + previousResult + "," + account.getBalance());
            }
            catch (IOException ioEx){
                System.out.println("Error: Could not write bets to file.");
            }
        }
    }

}