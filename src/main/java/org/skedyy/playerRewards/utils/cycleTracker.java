package org.skedyy.playerRewards.utils;

public class cycleTracker {
    public boolean isCycleValid(int cycleDays, long currentTimestamp, long startTimestamp){
        var cycleMilliseconds = cycleDays*86400000;
        System.out.println("cycleDays: "+cycleDays);
        System.out.println("cycleMilliseconds: "+cycleMilliseconds);
        System.out.println("startTimestamp: "+startTimestamp);
        return currentTimestamp - startTimestamp <= cycleMilliseconds;
    }
}
