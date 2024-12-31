package org.skedyy.playerRewards.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.skedyy.playerRewards.database.databaseUtils;
import org.skedyy.playerRewards.utils.cycleTracker;
import org.skedyy.playerRewards.utils.globalMessages;
import org.skedyy.playerRewards.utils.globalVars;
import org.skedyy.playerRewards.utils.chatUtils;
import java.sql.SQLException;

public class playerJoinEvent implements Listener {
    private final databaseUtils.DatabaseManager databaseManager;
    private final globalVars globalVars;
    private final globalMessages globalMessages;
    public playerJoinEvent(databaseUtils.DatabaseManager databaseManager, globalVars globalVariables, globalMessages globalMessages) {
        this.databaseManager = databaseManager;
        this.globalVars = globalVariables;
        this.globalMessages = globalMessages;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        var cycleTracker = new cycleTracker();
        var chatUtils = new chatUtils();
        var plugin = globalVars.plugin;
        var server = globalVars.server;
        var config = globalVars.config;
        var playersFolder = globalVars.playersFolder;
        var pluginPrefix = globalVars.pluginPrefix;
        var player = event.getPlayer();
        var sessionTimestamp = System.currentTimeMillis();
        var daysCycle = config.getInt("daysCycle");
        var currentTimestamp = System.currentTimeMillis();
        var startTimestamp = databaseManager.getCycleStart();
        var cycle = cycleTracker.isCycleValid(daysCycle, currentTimestamp, startTimestamp);
        if(cycle){
            if(playersFolder.exists()){
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        databaseManager.addPlayer(player);
                        databaseManager.setSessionTimestamp(player, sessionTimestamp);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        }else{
            chatUtils.sendClickableCommand(player,ChatColor.GOLD+pluginPrefix+" "+globalMessages.cycleFinished,"playerrewards results");
        }
    }
}
