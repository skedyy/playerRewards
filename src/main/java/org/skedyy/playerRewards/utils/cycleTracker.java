package org.skedyy.playerRewards.utils;

public class cycleTracker {
    public boolean isCycleValid(int cycleDays, long currentTimestamp, long startTimestamp){
        var cycleMilliseconds = cycleDays*86400000;
        return currentTimestamp - startTimestamp <= cycleMilliseconds;
    }
}
