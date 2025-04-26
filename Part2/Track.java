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
    public Track(String trackName, int raceLength){
        this.trackName = trackName;
        this.raceLength = raceLength;
        int x = 0;
        int y = 0;
        double distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

        /* template
            while (distance < raceLength){
                trackPositions.add(new TrackPosition(x, y, distance));
                x++;
                y++

                distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            }
        */

        if (trackName.toLowerCase().equals("line")){
            trackName = "Line";
            y=25;
            while (distance < raceLength){
                trackPositions.add(new TrackPosition(x, y, distance));
                x++;
                distance = x;
            }
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