
package com.assignment4.tasks;

import java.util.Arrays;

public class VectorClock {

    private final int[] timestamps;
    public VectorClock(int numOfClients){
        timestamps = new int[numOfClients];
        Arrays.fill(timestamps, 0);
    }
    public void setVectorClock(int processId, int time){
        timestamps[processId] = time;
    }
    public void tick(int processId){
        // increment the clock for the given process id
        timestamps[processId] += 1;
    }
    public int getCurrentTimestamp(int processId){
        return timestamps[processId];
    }
    public void updateClock(VectorClock other){
        // update the clock with the incoming clock value (use for-loop to iterate over values in array)
        for (int i = 0; i < timestamps.length; i++) {
            timestamps[i] = Math.max(timestamps[i], other.getCurrentTimestamp(i));
        }
    }
    public String showClock(){
        return Arrays.toString(timestamps);
    }

}
