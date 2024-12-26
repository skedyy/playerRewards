package org.skedyy.playerRewards.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.skedyy.playerRewards.Main;
import org.skedyy.playerRewards.database.databaseUtils;
import org.skedyy.playerRewards.utils.globalVars;

import java.io.File;
import java.sql.SQLException;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class playerQuitEvent implements Listener {
    private final databaseUtils.DatabaseManager databaseManager;
    private final org.skedyy.playerRewards.utils.globalVars globalVars;
    public playerQuitEvent(databaseUtils.DatabaseManager databaseManager, globalVars globalVariables) {
        this.databaseManager = databaseManager;
        this.globalVars = globalVariables;
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) throws SQLException {
        var playerUUID = event.getPlayer().getUniqueId().toString();
        var playersFolder = globalVars.playersFolder;
        var currentTimestamp = System.currentTimeMillis();
        if(playersFolder.exists()){
            Bukkit.getScheduler().runTaskAsynchronously(getPlugin(Main.class), () -> {
                try {
                    if(databaseManager.playerExists(playerUUID)&&databaseManager.sessionTimestampExists(playerUUID)){
                        var sessionTimestamp = databaseManager.getSessionTimestamp(playerUUID);
                        var newTimePlayed = currentTimestamp-sessionTimestamp;
                        var timePlayed = databaseManager.getPlayedTime(playerUUID);
                        databaseManager.setPlayedTime(playerUUID,timePlayed+newTimePlayed);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
