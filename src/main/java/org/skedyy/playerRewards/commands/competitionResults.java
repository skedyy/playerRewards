package org.skedyy.playerRewards.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.skedyy.playerRewards.database.databaseUtils;
import org.skedyy.playerRewards.utils.globalMessages;
import org.skedyy.playerRewards.utils.globalVars;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.skedyy.playerRewards.utils.winnersFormat;

public class competitionResults{
    private final org.skedyy.playerRewards.utils.globalVars globalVars;
    private final databaseUtils.DatabaseManager databaseManager;
    private final globalMessages globalMessages;
    public competitionResults(globalVars globalVars, databaseUtils.DatabaseManager databaseManager, globalMessages globalMessages) {
        this.globalVars = globalVars;
        this.databaseManager = databaseManager;
        this.globalMessages = globalMessages;
    }
    public void results (CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            //Init winnersFormat class
            var winnersFormat = new winnersFormat(globalVars);
            //Checking if the time format is not invalid.
            winnersFormat.checkTimeFormat();
            //Getting database results and init multiD array
            var resultsArr = databaseManager.getResults();
            if(!(resultsArr ==null)){
                String[][] winnersArray = {{""},{""}};
                //Looping through array to get all players
                for (int i = 0; i < resultsArr[0].length; ++i) {
                    var index = i+1;
                    var winnerName = Bukkit.getOfflinePlayer(UUID.fromString(resultsArr[1][i])).getName();
                    var winnerTime = resultsArr[0][i];
                    var winnerDays = winnersFormat.convertTimeUnit(Integer.parseInt(winnerTime));
                    switch (index){
                        case 1:
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',globalVars.pluginPrefix+" "+ ChatColor.GOLD+index+"st  "+winnerName+" Has played for "+winnerDays+" "+globalVars.winnersTimeFormat));
                            break;
                        case 2:
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',globalVars.pluginPrefix+" "+ ChatColor.GOLD+index+"nd  "+winnerName+" Has played for "+winnerDays+" "+globalVars.winnersTimeFormat));
                            break;
                        case 3:
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',globalVars.pluginPrefix+" "+ ChatColor.GOLD+index+"rd  "+winnerName+" Has played for "+winnerDays+" "+globalVars.winnersTimeFormat));
                            break;
                        default:
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',globalVars.pluginPrefix+" "+ ChatColor.GOLD+index+"th  "+winnerName+" Has played for "+winnerDays+" "+globalVars.winnersTimeFormat));

                    }
                }
            }else{
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',globalVars.pluginPrefix+globalMessages.databaseEmpty));
            }
        }
    }
}
