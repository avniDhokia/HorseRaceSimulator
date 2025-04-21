import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.*;

class JTest{

    final static String NAME_BASE = "Horse Race Simulator";
    static final int MAX_LANES = 10;

    static int numHorses = 0;
    static int numLanes = 3;    
    static int numRaces = 7;
    static int numUsers = 5;
    static int initialApples = 100;

    static ArrayList<BettingAccount> bettingAccounts = new ArrayList<BettingAccount>();
    static ArrayList<Horse> racingHorses;
    static Horse winningHorse;

    public static void main(String[] a){

        // create all initial screens
        
        // JFrame createHorseScreen = createJFrame(NAME_BASE + " - Create Horse");  // this screen should only be created/developed when needed
        
        HashMap<String, JFrame> frames = new HashMap<String, JFrame>();       // format: ["screenName":JFrame]
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
        // frames.put("homeScreen", developHomeScreen(frames));
        // frames.put("viewAnalyticsScreen", developViewAnalyticsScreen(frames));
        // frames.put("raceDesignScreen", developRaceDesignScreen(frames));
        // frames.put("horseSelectionScreen", developHorseSelectionScreen(frames));
        // frames.put("bettingSetupScreen", developBettingSetupScreen(frames));
        // frames.put("preRaceBettingScreen", developPreRaceBettingScreen(frames));
        // frames.put("raceScreen", developRaceScreen(frames));
        // frames.put("postRaceDisplayScreen", developPostRaceDisplayScreen(frames));
        // frames.put("postWeekBettingDisplayScreen", developPostWeekBettingisplayScreen(frames));
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

    // develop screens

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

        return;// frames.get("viewAnalyticsScreen");
    }

    public static void developRaceDesignScreen(HashMap<String, JFrame> frames){//JFrame raceDesignScreen, JFrame homeScreen, JFrame horseSelectionScreen){
        // raceDesignScreen nav
        JButton goHome = new JButton("Home");
        goHome.addActionListener(e->{
            switchFrames(frames.get("raceDesignScreen"), frames.get("homeScreen"));
        });

        JButton selectHorses = new JButton("SelectHorses");
        selectHorses.addActionListener(e->{
            JFrame newFrame = createJFrame(NAME_BASE + " - Horse Setup");
            frames.put("horseSelectionScreen", newFrame);
            developHorseSelectionScreen(frames);
            switchFrames(frames.get("raceDesignScreen"), frames.get("horseSelectionScreen"));
        });

        JPanel raceDesignNavPanel = createNavigationPanel(new JButton[]{goHome, selectHorses});
        frames.get("raceDesignScreen").add(raceDesignNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));
        workingPanel.setBorder(new EmptyBorder(100, 100, 100, 100));

        // instructions + information
        JTextArea info = new JTextArea();
        info.setText("Choose track type, number of lanes and number of races. Select weather and track conditions for each race.\n\n" +
                "Conditions Information:\n" +
                "Dry tracks allow optimal performance. Wet and Icy tracks slow down horses and make them more likely to fall. Muddy tracks slow down horses, and decrease their stamina.\n\n" +
                "Sunny weather allows optimal performance. Horses run slower in Rainy and Foggy weather. Foggy weather also decreases horses' confidence.\n");
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setEditable(false);
        info.setBorder(new EmptyBorder(20, 20, 20, 20));
        workingPanel.add(info);

        // trackPanel: select track type, select number of lanes, select number of days
        JPanel trackPanel = new JPanel(new GridLayout(0,1));
        trackPanel.setBorder(new EmptyBorder(10, 50, 5, 50));
        trackPanel.add(new JLabel("Select Track:"));
        String[] trackOptions = {"Line", "Oval", "8", "Other"};
        JComboBox<String> selectTrack = new JComboBox<>(trackOptions);
        trackPanel.add(selectTrack);        

        // make slider for number of lanes
        JSlider numLanesSlider = new JSlider(0, MAX_LANES, numLanes);
        numLanesSlider.setMajorTickSpacing(2);
        numLanesSlider.setMinorTickSpacing(1);
        numLanesSlider.setPaintTicks(true);
        numLanesSlider.setPaintLabels(true);

        numLanesSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                numLanes = numLanesSlider.getValue();
            }
        });

        trackPanel.add(new JLabel("Select number of lanes:"));
        trackPanel.add(numLanesSlider);

        workingPanel.add(trackPanel);

        JPanel conditionsPanel = new JPanel(new GridLayout(0,1));

        // setup tabs for conditions of each day
        trackPanel.add(new JLabel("Select Number of Days:"));
        JTextField dayNumInput = new JTextField(5);
        trackPanel.add(dayNumInput);

        dayNumInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                processInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            String input = dayNumInput.getText();
                if (input.length() > 0){
                    processInput();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                processInput();
            }

            private boolean isInt(String test){
                for (int i = 0; i < test.length(); i++){
                    char c = test.charAt(i);
                    if (c<'0' || c>'9'){
                        return false;
                    }
                }
                return true;
            }

            private void processInput() {
                String input = dayNumInput.getText();

                if (isInt(input)){
                    // create a mini tab menu so you can access and edit each day's conditions
                    JTabbedPane dayTabPanel = new JTabbedPane();
                    conditionsPanel.removeAll();
                    
                    int day = Integer.parseInt(input);

                    for (int i = 1; i <= day; i++){
                        JPanel dayPanel = new  JPanel();
                        //dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
                        dayPanel.setLayout(new GridLayout(0,1));
                        dayPanel.add(new JLabel("Day " + i + " Conditions"));
                        dayTabPanel.addTab("Day " + i, dayPanel);

                        // track conditions
                        JRadioButton dryButton = new JRadioButton("Dry Track");
                        JRadioButton wetButton = new JRadioButton("Wet Track");
                        JRadioButton icyButton = new JRadioButton("Icy Track");
                        JRadioButton muddyButton = new JRadioButton("Muddy Track");

                        ButtonGroup conditionButtons = new ButtonGroup();
                        conditionButtons.add(dryButton);
                        dryButton.setSelected(true);
                        conditionButtons.add(wetButton);
                        conditionButtons.add(icyButton);
                        conditionButtons.add(muddyButton);

                        JPanel trackConditionsPanel = new JPanel();
                        trackConditionsPanel.add(new Label("Track Conditions:"));

                        trackConditionsPanel.add(dryButton);
                        trackConditionsPanel.add(wetButton);
                        trackConditionsPanel.add(icyButton);
                        trackConditionsPanel.add(muddyButton);
                        dayPanel.add(trackConditionsPanel);


                        // weather conditions
                        JCheckBox sunny = new JCheckBox("Sunny");
                        JCheckBox rainy = new JCheckBox("Rainy");
                        JCheckBox foggy = new JCheckBox("Foggy");

                        JPanel weatherConditionsPanel = new JPanel();
                        weatherConditionsPanel.add(new Label("Weather Conditions:"));
                        weatherConditionsPanel.add(sunny);
                        weatherConditionsPanel.add(rainy);
                        weatherConditionsPanel.add(foggy);
                        
                        dayPanel.add(weatherConditionsPanel);
                    }
                    conditionsPanel.add(dayTabPanel);
                    conditionsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
                                        
                    workingPanel.add(conditionsPanel);
                    workingPanel.revalidate();
                    workingPanel.repaint();
                    frames.get("raceDesignScreen").add(workingPanel);
                }
            }
        });

        workingPanel.add(conditionsPanel);

        frames.get("raceDesignScreen").add(workingPanel);
        return ;//frames.get("raceDesignScreen");
    }
    
    public static void developHorseSelectionScreen(HashMap<String, JFrame> frames){//JFrame horseSelectionScreen, JFrame raceDesignScreen, JFrame bettingSetupScreen){
        // horseSelectionScreen nav
        JPanel horseSelectionNavPanel = createNavigationPanel(new JButton[]{
            addSwitchButton("Back to Race Setup", frames.get("horseSelectionScreen"), frames.get("raceDesignScreen")),
            //addMiniWindowButton("Create Horse", horseSelectionScreen, createHorseScreen),
            addSwitchButton("Setup Betting", frames.get("horseSelectionScreen"), frames.get("bettingSetupScreen"))
        });
        // remmeber mini create screen

        frames.get("horseSelectionScreen").add(horseSelectionNavPanel, BorderLayout.PAGE_START);

        JPanel selectionPanel = new JPanel(new GridLayout(0,1));
        selectionPanel.setBorder(new EmptyBorder(100,100,100,100));

        // instructions + information
        JTextArea info = new JTextArea();
        info.setText("Customise each lane horse via the tabs. You can also select for no horse to race in any lanes.\n\n" +
                     "For each horse, you can choose equipment and accessories. These can affect the horse's performance.\n\n" +
                     "Larger saddles slow down horses. Bridles, blankets and hats increase a horse's confidence, but can make it more likely the horse will fall. Horseshoes help horses run faster.");
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setEditable(false);
        info.setBorder(new EmptyBorder(20, 20, 20, 20));
        selectionPanel.add(info);

        // do dynamic tab thing to allow customising each horse
        JPanel customisationPanel = new JPanel(new GridLayout(0,1));

        // create a mini tab menu so you can access and edit each horse's equipment
        JTabbedPane horseTabPanel = new JTabbedPane();
        for (int i = 1; i <= numLanes; i++){
            JPanel horsePanel = new  JPanel();
            horsePanel.setLayout(new GridLayout(0,1));
            horsePanel.add(new JLabel("Lane 1 Horse Customisation"));
            horseTabPanel.addTab("Lane " + i, horsePanel);

            customisationPanel.add(horseTabPanel);
        }

        // update racingHorses arrayList
        // test arraylist
        racingHorses = new ArrayList<Horse>();
        racingHorses.add(new Horse('A', "TestHorse 1", 0.5, "Brown", new Breed("Breed 1", 10, 10)));
        racingHorses.add(new Horse('B', "TestHorse 2", 0.6, "Black", new Breed("Breed 2", 12, 8)));
        racingHorses.add(new Horse('C', "TestHorse 3", 0.7, "White", new Breed("Breed 3", 8, 12)));
        racingHorses.add(new Horse('D', "TestHorse 4", 0.8, "Cream", new Breed("Breed 1", 9, 11)));
        racingHorses.add(new Horse('E', "TestHorse 5", 0.9, "Rainbow", new Breed("Breed 2", 11, 9)));
        //
        numHorses=5;

        selectionPanel.add(customisationPanel);

        frames.get("horseSelectionScreen").add(selectionPanel);
        return ;//frames.get("horseSelectionScreen");
    }
    
    public static void developHorseCreationScreen(HashMap<String, JFrame> frames){//JFrame createHorseScreen){
        // createHorseScreen nav
        JPanel horseCreationNavPanel = createNavigationPanel(new JButton[]{
            createCancelButton(frames.get("createHorseScreen"))
        });
        frames.get("createHorseScreen").add(horseCreationNavPanel, BorderLayout.PAGE_START);

        JPanel createHorsePanel = new JPanel();
        createHorsePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        
        JLabel confidenceLabel = new JLabel("Confidence (0 < x < 1):");
        JTextField confidenceField = new JTextField(15);
        
        JLabel repLabel = new JLabel("Symbol (a character or emoji):");
        JTextField repField = new JTextField(15);

        String[] breedOptions = {"Breed 1", "Breed 2", "Breed 3"};
        JComboBox<String> breedComboBox = new JComboBox<>(breedOptions);

        String[] colourOptions = {"White", "Brown", "Black", "Cream", "Rainbow"};
        JComboBox<String> colourComboBox = new JComboBox<>(colourOptions);

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

        frames.get("createHorseScreen").add(createHorsePanel, BorderLayout.CENTER);

        return;// frames.get("createHorseScreen");
    }

    public static void developBettingSetupScreen(HashMap<String, JFrame> frames){//JFrame bettingSetupScreen, JFrame horseSelectionScreen, JFrame preRaceBettingScreen){
        // bettingSetupScreen nav
        JButton startBetting = new JButton("Start Betting");
        startBetting.addActionListener(e->{

            // reset bettingAccounts ArrayList
            bettingAccounts = new ArrayList<BettingAccount>();

            JFrame newFrame = createJFrame(NAME_BASE + " - Pre-Race Betting");
            frames.put("preRaceBettingScreen", newFrame);
            developPreRaceBettingScreen(frames);
            switchFrames(frames.get("bettingSetupScreen"), frames.get("preRaceBettingScreen"));
        });

        JPanel bettingSetupNavPanel = createNavigationPanel(new JButton[]{
            addSwitchButton("Back to Horse Setup", frames.get("bettingSetupScreen"), frames.get("horseSelectionScreen")),
            startBetting
        });
        frames.get("bettingSetupScreen").add(bettingSetupNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));

        // instructions + information
        JTextArea info = new JTextArea();
        info.setText("Here, you can choose how many users will be betting, and you can choose how many apples (the betting currency) will be given to each user to start with.\n" +
                "Before each race, you'll be able to see each horse and its odds, as well as the total amount bet on each horse.\n" +
                "Each user can place their own bet on whichever horse they would like. If a user doesn't want to bet, simply select any horse and set the bet amount to 0 apples.");
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setEditable(false);
    
        workingPanel.add(info);

        // select the number of users
        JPanel userNumPanel = new JPanel();
        userNumPanel.add(new JLabel("How many users are there?"));
        JTextField userNumInput = new JTextField(5);
        
        userNumInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                processInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            String input = userNumInput.getText();
                if (input.length() > 0){
                    processInput();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                processInput();
            }

            private boolean isInt(String test){
                for (int i = 0; i < test.length(); i++){
                    char c = test.charAt(i);
                    if (c<'0' || c>'9'){
                        return false;
                    }
                }
                return true;
            }

            private void processInput() {
                String input = userNumInput.getText();

                if (isInt(input)){
                    numUsers = Integer.parseInt(input);          
                }
            }
        });

        userNumPanel.add(userNumInput);
        workingPanel.add(userNumPanel);

        // select number of start apples
        JPanel applesNumPanel = new JPanel();
        applesNumPanel.add(new JLabel("How many apples should each user start with?"));
        JTextField applesNumInput = new JTextField(5);
        
        applesNumInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                processInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            String input = applesNumInput.getText();
                if (input.length() > 0){
                    processInput();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                processInput();
            }

            private boolean isInt(String test){
                for (int i = 0; i < test.length(); i++){
                    char c = test.charAt(i);
                    if (c<'0' || c>'9'){
                        return false;
                    }
                }
                return true;
            }

            private void processInput() {
                String input = applesNumInput.getText();

                if (isInt(input)){
                    initialApples = Integer.parseInt(input);
                }
            }
        });

        applesNumPanel.add(applesNumInput);
        workingPanel.add(applesNumPanel);
        
        frames.get("bettingSetupScreen").add(workingPanel);


        return;// frames.get("bettingSetupScreen");
    }

    public static void developPreRaceBettingScreen(HashMap<String, JFrame> frames){//JFrame preRaceBettingScreen, JFrame raceScreen){

        // preRaceBettingScreen nav
        JPanel preRaceBettingNavPanel = createNavigationPanel(new JButton[]{
            addSwitchButton("Start Race", frames.get("preRaceBettingScreen"), frames.get("raceScreen"))
        });
        frames.get("preRaceBettingScreen").add(preRaceBettingNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));

        // display each horse's odds in a table
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

        tablePanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        refreshHorseInfoTable(tablePanel);
        workingPanel.add(tablePanel);

        // create a tab menu so you can access and edit each user's bet
        JTabbedPane userTabs = new JTabbedPane();
        JPanel bettingPanel = new JPanel();

        for (int i = 1; i <= numUsers; i++){
            JPanel bettingTabPanel = new  JPanel(new GridLayout(0,1));
            BettingAccount account = new BettingAccount(initialApples);
            bettingAccounts.add(i-1, account);

            // create slider to set bets
            JSlider betSlider = new JSlider(0, account.getBalance(), 0);
            betSlider.setMajorTickSpacing(10);
            betSlider.setMinorTickSpacing(5);
            betSlider.setPaintTicks(true);
            betSlider.setPaintLabels(true);

            JButton confirm = new JButton("Confirm Bet");

            String[] horseOptions = new String[numHorses];
            for (int j = 0; j < numHorses; j++){
                horseOptions[j] = racingHorses.get(j).getName();
            }
            
            JComboBox<String> selectBettingHorse = new JComboBox<String>(horseOptions);

            confirm.addActionListener(e -> {
                int betAmount = betSlider.getValue();
                String selectedHorseName = (String)selectBettingHorse.getSelectedItem();

                // iterate through the horse list to find the selected horse
                Horse selectedHorse = null;
                for (Horse horse : racingHorses) {
                    if (horse.getName().equals(selectedHorseName)) {
                        selectedHorse = horse;
                        break;
                    }
                }

                account.makeBet(betAmount, selectedHorse);

                // refresh horse info table
                refreshHorseInfoTable(tablePanel);
            });


            bettingTabPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            
            bettingPanel.add(bettingTabPanel);
            userTabs.addTab("User " + i, bettingTabPanel);


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
            bettingTabPanel.add(betSlider, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            bettingTabPanel.add(confirm, gbc);
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
        String[] columnNames = {"Lane", "Horse Name", "Symbol", "Total Apples Bet", "Betting Odds"};

        Object[][] horseData = new Object[numHorses][5];

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

        racingHorses.forEach(horse -> {

            if (! (horse==null)){
                int[] betAmount = {0};
                double[] horseOdds = {0};
                
                // calculate how much has been bet on each horse
                bettingAccounts.forEach(account -> {
                    // make sure both account and account.getBetHorse() don't return null
                    if ( (!(account==null)) && (!(account.getBetHorse()==null)) && account.getBetHorse().equals(horse)){
                        betAmount[0] = betAmount[0] + account.getBetAmount();
                        
                    }
                });
                
                horseOdds[0] = calculateHorseOdds(horse, betAmount[0], totalApples[0]);
                String oddsString = "1 bet -> ~" + (int)horseOdds[0] + " apple";

                if ((int)horseOdds[0] != 1){
                    oddsString = oddsString + "s";
                }

                Object[] temp = new Object[]{laneIndexArray[0], horse.getName(), horse.getSymbol(), betAmount[0], oddsString};
                horseData[laneIndexArray[0]-1]=temp;
                laneIndexArray[0]++;
            }
        });

        JTable horseInfoTable = new JTable(horseData, columnNames);
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

        double chanceOfSuccess = breed.getSpeed() * breed.getStamina() * (1 + horse.getConfidence()) * 0.01;

        if (totalAmountToBet > 0 && betAmount > 0){
            // what proportion of the total start apples has been bet on this horse?
            double betProportion = (double)betAmount / (double)totalAmountToBet;

            odds = 1 / ((betProportion * chanceOfSuccess)+0.05);
        }
        else{
            odds = 1;
        }
        
        return odds;
    }

    public static void developRaceScreen(HashMap<String, JFrame> frames){//JFrame raceScreen, JFrame postRaceDisplayScreen){
        // raceScreen nav
        JButton toPostRace = new JButton("Finish Race");
        toPostRace.addActionListener(e->{

            // choose random number from 1 to numHorses
            Random random = new Random();
            int randomIndex = random.nextInt(numHorses);
            winningHorse = racingHorses.get(randomIndex);

            JFrame newFrame = createJFrame(NAME_BASE + "Post-Race Display");
            frames.put("postRaceDisplayScreen", newFrame);
            developPostRaceDisplayScreen(frames);
            switchFrames(frames.get("raceScreen"), frames.get("postRaceDisplayScreen"));
        });
        JPanel raceNavPanel = createNavigationPanel(new JButton[]{
            toPostRace
        });
        frames.get("raceScreen").add(raceNavPanel, BorderLayout.PAGE_START);



        return;// frames.get("raceScreen");
    }

    public static void developPostRaceDisplayScreen(HashMap<String, JFrame> frames){//JFrame postRaceDisplayScreen, JFrame preRaceBettingScreen, JFrame postWeekBettingDisplayScreen){
        // postRaceDisplayScreen nav
        JButton nextRace = new JButton("Next Race");
        nextRace.addActionListener(e->{
            JFrame newFrame = createJFrame(NAME_BASE + "Pre-Race Betting");
            frames.put("preRaceBettingScreen", newFrame);
            developPreRaceBettingScreen(frames);
            switchFrames(frames.get("postRaceDisplayScreen"), frames.get("preRaceBettingScreen"));
        });
        JPanel postRaceNavPanel = createNavigationPanel(new JButton[]{
            nextRace,
            addSwitchButton("Finish All Races", frames.get("postRaceDisplayScreen"), frames.get("postWeekBettingDisplayScreen"))
        });
        frames.get("postRaceDisplayScreen").add(postRaceNavPanel, BorderLayout.PAGE_START);

        JPanel workingPanel = new JPanel(new GridLayout(0,1));

        // panel for table of horse data
        JPanel horseTablePanel = new JPanel();
        horseTablePanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        
        String[] horseTableColumnNames = {"Lane", "Horse Name", "Symbol", "Total Apples Bet", "Betting Odds", "Result"};

        Object[][] horseData = new Object[numHorses][7];

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

        racingHorses.forEach(horse -> {

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
                String oddsString = "1 bet -> ~" + (int)horse.getOdds() + " apple";

                if ((int)horse.getOdds() != 1){
                    oddsString = oddsString + "s";
                }

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

                Object[] temp = new Object[]{laneIndexArray[0], horse.getName(), horse.getSymbol(), betAmount[0], oddsString, result};
                horseData[laneIndexArray[0]-1]=temp;
                laneIndexArray[0]++;
            }
        });

        JTable horseInfoTable = new JTable(horseData, horseTableColumnNames);
        horseInfoTable.setFillsViewportHeight(true);
        JScrollPane horseScrollPane = new JScrollPane(horseInfoTable);

        horseTablePanel.add(horseInfoTable.getTableHeader(), BorderLayout.PAGE_START);
        horseTablePanel.add(horseInfoTable, BorderLayout.CENTER);
        
        workingPanel.add(horseTablePanel);

        // panel for table of account data
        JPanel accountTablePanel = new JPanel();
        accountTablePanel.setBorder(new EmptyBorder(50, 100, 50, 100));
        
        String[] accountTableColumnNames = {"User", "Horse", "Apples", "Result"};

        Object[][] accountData = new Object[numUsers][4];

        int userIndex = 1;
        int[] userIndexArray = {userIndex};

        bettingAccounts.forEach(account -> {

            if (account.getBetAmount() > 0){

                String horseName = account.getBetHorse().getName();
                
                String result;
                if (account.getBetHorse().equals(winningHorse)){
                    result = "Won";
                    account.winBet();
                }
                else{
                    result = "Lost";
                    account.loseBet();
                }

                Object[] temp = new Object[]{userIndexArray[0], horseName, account.getBalance(), result};
                accountData[userIndexArray[0]-1]=temp;
                userIndexArray[0]++;
            }
            // user didn't bet
            else{
                Object[] temp = new Object[]{userIndexArray[0], "No Horse", account.getBalance(), "Did not bet"};
                accountData[userIndexArray[0]-1]=temp;
                userIndexArray[0]++;
            }
        });

        JTable accountInfoTable = new JTable(accountData, accountTableColumnNames);
        accountInfoTable.setFillsViewportHeight(true);
        JScrollPane accountScrollPane = new JScrollPane(accountInfoTable);

        accountTablePanel.add(accountInfoTable.getTableHeader(), BorderLayout.PAGE_START);
        accountTablePanel.add(accountInfoTable, BorderLayout.CENTER);
        
        workingPanel.add(accountTablePanel);

        frames.get("postRaceDisplayScreen").add(workingPanel);
        return;// frames.get("postRaceDisplayScreen");
    }

    public static void developPostWeekBettingisplayScreen(HashMap<String, JFrame> frames){//JFrame postWeekBettingDisplayScreen, JFrame homeScreen){
        // PostWeekBettingDisplayScreen nav
        JPanel postWeekNavPanel = createNavigationPanel(new JButton[]{
            addSwitchButton("Home", frames.get("postWeekBettingDisplayScreen"), frames.get("homeScreen"))
        });
        frames.get("postWeekBettingDisplayScreen").add(postWeekNavPanel, BorderLayout.PAGE_START);

        return;// frames.get("postWeekBettingDisplayScreen");
    }

    // generic methods

    // create a generic JFrame
    public static JFrame createJFrame(String name){
        int defaultWindowX = 900;
        int defaultWindowY = 600;

        JFrame frame = new JFrame(name);
        frame.setLocation(0,0);
        frame.setBackground(new Color(0, 0, 0));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(defaultWindowX, defaultWindowY);
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
        navPanel.setBackground(new Color(168, 252, 116));
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        for (JButton button : buttons) {
            navPanel.add(button);
        }

        return navPanel;
    }

    // create a cancel button to close a window
    private static JButton createCancelButton(JFrame screen) {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> screen.setVisible(false));
        screen.add(cancelButton);
        return cancelButton;
    }
}