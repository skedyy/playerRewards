package org.skedyy.playerRewards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.skedyy.playerRewards.commands.competitionResults;
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
        // Create globalVars and globalMessagesclasses
        globalVariables = new globalVars(getServer(),this,getConfig(),databaseManager,Bukkit.getPluginManager());
        globalMessages = new globalMessages(getConfig());
        var server = getServer();
        server.getConsoleSender().sendMessage(ChatColor.GOLD + "[playerRewards]"+ChatColor.GREEN+" Starting the rewards!");

        //Checks for existent data
        var dataFolder = getDataFolder();
        var database = new File(getDataFolder().getAbsolutePath() + "/database/players.db");
        if (dataFolder.exists()&&database.exists()) {
            server.getConsoleSender().sendMessage(ChatColor.GOLD + "[playerRewards] Loading already existent data");
        }

        //If data doesn't exists
        else {
            server.getConsoleSender().sendMessage(ChatColor.GOLD + "[playerRewards]"+ChatColor.GREEN+" Creating new data folder...");
            try{
                dataFolder.mkdir();
                var playersFolder = new File(dataFolder+"/players");
                var databaseFolder = new File(dataFolder+"/database");
                playersFolder.mkdir();
                databaseFolder.mkdir();
            }
            catch (SecurityException e) {
                e.printStackTrace();
                server.getConsoleSender().sendMessage(ChatColor.GOLD+"[playerRewards]"+ChatColor.RED+" Error creating the folders, check for permissions!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
            saveDefaultConfig();
        }

        //Database creation/connection
        try {
            var databaseVersion = getConfig().getDouble("databaseVersion");
            var pluginVersion = getConfig().getDouble("pluginVersion");
            databaseManager = new databaseUtils.DatabaseManager(getDataFolder().getAbsolutePath() + "/database/players.db",databaseVersion,pluginVersion,globalVariables);
        }
        catch (SQLException e) {
            e.printStackTrace();
            server.getConsoleSender().sendMessage(ChatColor.GOLD+"[playerRewards]"+ChatColor.RED+" Error connecting to the database!");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        //Registering event(s)
        server.getPluginManager().registerEvents(new playerJoinEvent(databaseManager,globalVariables,globalMessages), this);
        server.getPluginManager().registerEvents(new playerQuitEvent(databaseManager,globalVariables), this);

        //Registering command(s)
        this.getCommand("playerrewards").setExecutor(new playerRewards(globalVariables,databaseManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        var server = getServer();
        server.getConsoleSender().sendMessage(ChatColor.GOLD + "[playerRewards] The rewards are gone :(");
        server.getConsoleSender().sendMessage(ChatColor.GOLD + "[playerRewards]" + ChatColor.RED + " Please check the console for errors!");
        try {
            // Close the database connection when the plugin is disabled
            if (databaseManager != null) {
                databaseManager.closeConnection();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
