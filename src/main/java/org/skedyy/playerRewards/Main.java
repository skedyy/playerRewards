package org.skedyy.playerRewards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.skedyy.playerRewards.commands.playerRewards;
import org.skedyy.playerRewards.database.databaseUtils;
import org.skedyy.playerRewards.events.playerJoinEvent;
import org.skedyy.playerRewards.events.playerQuitEvent;
import org.skedyy.playerRewards.utils.globalMessages;
import org.skedyy.playerRewards.utils.globalVars;
import java.io.File;
import java.sql.SQLException;

public final class Main extends JavaPlugin {

    private databaseUtils.DatabaseManager databaseManager;
    private globalVars globalVariables;
    private globalMessages globalMessages;
    @Override
    public void onEnable() {
        // Check for dataFolder
        globalVariables = new globalVars(getServer(),this,getConfig(),databaseManager);
        globalMessages = new globalMessages(getConfig());
        var server = getServer();
        server.getConsoleSender().sendMessage(ChatColor.GOLD + "[playerRewards]"+ChatColor.GREEN+" Starting the rewards!");
        var dataFolder = getDataFolder();
        var database = new File(getDataFolder().getAbsolutePath() + "/database/players.db");
        if (dataFolder.exists()&&database.exists()) {
            server.getConsoleSender().sendMessage(ChatColor.GOLD + "[playerRewards] Loading already existent data");
        } else {
            server.getConsoleSender().sendMessage(ChatColor.GOLD + "[playerRewards]"+ChatColor.GREEN+" Creating new data folder...");
            dataFolder.mkdir();
            var playersFolder = new File(dataFolder+"/players");
            var databaseFolder = new File(dataFolder+"/database");
            playersFolder.mkdir();
            databaseFolder.mkdir();
            saveDefaultConfig();
            try {
                var databaseVersion = getConfig().getDouble("databaseVersion");
                var pluginVersion = getConfig().getDouble("pluginVersion");
                databaseManager = new databaseUtils.DatabaseManager(getDataFolder().getAbsolutePath() + "/database/players.db",databaseVersion,pluginVersion,globalVariables);
            } catch (SQLException e) {
                e.printStackTrace();
                // Disable the plugin if the database connection fails, because we don't want enabled plugin with no functionality.
                server.getConsoleSender().sendMessage(ChatColor.GOLD+"[playerRewards]"+ChatColor.RED+" Error creating the database!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        try {
            var databaseVersion = getConfig().getDouble("databaseVersion");
            var pluginVersion = getConfig().getDouble("pluginVersion");
            databaseManager = new databaseUtils.DatabaseManager(getDataFolder().getAbsolutePath() + "/database/players.db",databaseVersion,pluginVersion,globalVariables);
        } catch (SQLException e) {
            e.printStackTrace();
            // Disable the plugin if the database connection fails, because we don't want enabled plugin with no functionality.
            server.getConsoleSender().sendMessage(ChatColor.GOLD+"[playerRewards]"+ChatColor.RED+" Error connecting to the database!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        //Registering event(s)
        server.getPluginManager().registerEvents(new playerJoinEvent(databaseManager,globalVariables,globalMessages), this);
        server.getPluginManager().registerEvents(new playerQuitEvent(databaseManager,globalVariables), this);
        //Registering command(s)
        this.getCommand("playerRewards").setExecutor(new playerRewards(globalVariables,databaseManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        var server = getServer();
        server.getConsoleSender().sendMessage(ChatColor.GOLD + "[playerRewards] The rewards are gone :(");
        server.getConsoleSender().sendMessage(ChatColor.GOLD+"[playerRewards]"+ChatColor.RED+" Please check the console for errors!");
        try {
            // Close the database connection when the plugin is disabled
            if (databaseManager != null) {
                databaseManager.closeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
