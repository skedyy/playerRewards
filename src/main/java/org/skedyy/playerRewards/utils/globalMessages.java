package org.skedyy.playerRewards.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class globalMessages {
    public final String cycleFinished;
    public final String databaseEmpty;
    public globalMessages(FileConfiguration config){
        this.cycleFinished = config.getString("messages.cycle-finished");
        this.databaseEmpty = config.getString("messages.database-empty");
    }
}
