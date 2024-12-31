package org.skedyy.playerRewards.utils;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.skedyy.playerRewards.Main;
import org.skedyy.playerRewards.database.databaseUtils;
import java.io.File;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class globalVars {
    public final Server server;
    public final Main plugin;
    public final FileConfiguration config;
    public final File dataFolder;
    public final File playersFolder;
    public final String pluginPrefix;
    public final String pluginVersion;
    public final Integer daysCycle;
    public final Integer competitionWinnners;
    public final String winnersTimeFormat;
    public final PluginManager pluginManager;
    public globalVars(Server server, Main plugin, FileConfiguration config, databaseUtils.DatabaseManager databaseManager, PluginManager pluginManager) {
        this.server = server;
        this.plugin = plugin;
        this.config = config;
        this.dataFolder = plugin.getDataFolder();
        this.playersFolder = new File(this.dataFolder+"/players");
        this.pluginPrefix = config.getString("prefix");
        this.pluginVersion = config.getString("pluginVersion");
        this.daysCycle = config.getInt("daysCycle");
        this.competitionWinnners = config.getInt("competitionWinners");
        this.winnersTimeFormat = config.getString("winners-TimeFormat");
        this.pluginManager = pluginManager;
    }
}
