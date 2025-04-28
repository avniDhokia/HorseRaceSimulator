import java.util.*;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

class TrackPosition{
    int x;
    int y;
    double distance;

    public TrackPosition(int x, int y, double distance){
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public double getDistance(){
        return distance;
    }
}

class Track{
    Queue<TrackPosition> trackPositions = new LinkedList<>();
    int raceLength;
    TrackPosition currentPosition;
    String trackName;

    // constructor for copying tracks
    public Track(Queue<TrackPosition> trackPositions, int raceLength, TrackPosition currentPosition){
        this.trackPositions = trackPositions;
        this.raceLength = raceLength;
        this.currentPosition = currentPosition;

        return;
    }

    // constructor via file io
    public Track(String trackName){
        String fileName = trackName.toLowerCase() + ".txt";
        this.trackName = trackName;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();
            
            while ((line = br.readLine()) != null) {
                String[] brokenLine = line.split(",");
                TrackPosition position = new TrackPosition(Integer.parseInt(brokenLine[0]), Integer.parseInt(brokenLine[1]), Integer.parseInt(brokenLine[2]));
                raceLength = Integer.parseInt(brokenLine[2]);
                trackPositions.add(position);
            }

            currentPosition = trackPositions.peek();
        } catch (IOException e) {
            System.out.println("Error reading horses file.");
        }
    }

    // constructor to build custom track, given type and raceLength
    public Track(String trackName, int raceLength, int laneWidth){
        this.trackName = trackName;
        this.raceLength = raceLength;

        if (trackName.toLowerCase().equals("line")){
            trackName = "Line";
            buildLineTrack(raceLength, laneWidth);
        }
        else if (trackName.toLowerCase().equals("zigzag")){
            trackName = "ZigZag";
            buildZigZagTrack(raceLength, laneWidth);
        }
        else if (trackName.toLowerCase().equals("oval")){
            trackName = "Oval";
            buildOvalTrack(raceLength, laneWidth);
        }
        else{
            System.out.println("Error: unknown Track name " + trackName);
        }

        currentPosition = trackPositions.peek();
        return;
    }


    // copy the track
    public Track copyTrack(){

        // copy track positions
        Queue<TrackPosition> newTrackPositions = new LinkedList<>();
        Queue<TrackPosition> replacementTrackPositions = new LinkedList<>();

        // iterate through the trackPositions
        while (!trackPositions.isEmpty()){
            TrackPosition temp = trackPositions.poll();

            newTrackPositions.add(temp);
            replacementTrackPositions.add(temp);
        }

        trackPositions = replacementTrackPositions;

        return new Track(newTrackPositions, raceLength, new TrackPosition(currentPosition.getX(), currentPosition.getY(), currentPosition.getDistance()));
    }

    // build tracks

    public void buildLineTrack(int raceLength, int laneWidth){
        int y = laneWidth/4; // to get horses near middle of lane

        for (int x = 0; x <= raceLength; x++){
            trackPositions.add(new TrackPosition(x, y, x));
        }
    }

    public void buildZigZagTrack(int raceLength, int width){
        trackPositions = new LinkedList<>();
        boolean drawingDown = false;
        int y = 0;

        trackPositions.add(new TrackPosition(0, 0, 0));

        for (int x = 1; x <= raceLength; x++){
            
            if (drawingDown){
                y = y - 1;
            }
            else{
                y = y + 1;
            }

            if (y%width==0){
                drawingDown = !drawingDown;
            }
            
            double distance = Math.sqrt( Math.pow(x,2) + Math.pow(y,2) );
            trackPositions.add(new TrackPosition(x, y, distance));
        }

        return;
    }

    public void buildOvalTrack(int raceLength, int width){
        int length = (raceLength/2) - width;

        trackPositions = new LinkedList<>();
        int x = 0;
        int y = 0;
        int distance = 0;
        trackPositions.add(new TrackPosition(x, y, distance));
        
        // move forward
        for (int i = 0; i <= length; i++){
            x++;
            distance++;
            trackPositions.add(new TrackPosition(x, y, distance));
        }

        // move down
        for (int i = 0; i <= width; i++){
            y = y+1;
            distance++;
            trackPositions.add(new TrackPosition(x, y, distance));
        }

        // move backwards
        for (int i = 0; i <= length; i++){
            x = x-1;
            distance++;
            trackPositions.add(new TrackPosition(x, y, distance));
        }
            
        // move up
        for (int i = 0; i <= width; i++){
            y=y-1;
            distance++;
            trackPositions.add(new TrackPosition(x, y, distance));
        }
            
        return;
    }


    // return current position
    public TrackPosition getCurrentPosition(){
        return currentPosition;
    }

    // move to next position
    public void moveToNextPosition(){
        currentPosition = trackPositions.poll();
        return;
    }

    // find the next position
    public TrackPosition getNextPosition(){
        return trackPositions.peek();
    }

    // return the raceLength
    public int getRaceLength(){
        return raceLength;
    }

    // return the track name
    public String getTrackName(){
        return trackName;
    }

}