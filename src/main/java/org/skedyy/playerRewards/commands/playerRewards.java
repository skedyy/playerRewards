package org.skedyy.playerRewards.commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.skedyy.playerRewards.database.databaseUtils;
import org.skedyy.playerRewards.utils.globalVars;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.UUID.fromString;

public class playerRewards implements CommandExecutor {
    private final globalVars globalVars;
    private final databaseUtils.DatabaseManager databaseManager;
    public playerRewards(globalVars globalVars, databaseUtils.DatabaseManager databaseManager) {
        this.globalVars = globalVars;
        this.databaseManager = databaseManager;
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if(args.length == 0){
                //Display help command
                if(sender.hasPermission("playerRewards.help")){
                    sender.sendMessage(globalVars.pluginPrefix+" "+ ChatColor.GREEN+"/playerRewards results "+ChatColor.GOLD+"Show the actual competition leaderboard. ");
                }
            }else{
                switch (args[0]){
                    //Display results
                    case "results":
                        if(sender.hasPermission("playerRewards.results")){
                            try {
                                var resultsArr = databaseManager.getResults();
                                String[] winnersArray;
                                for (int i = 0; i < resultsArr.length; ++i) {
                                    for (int j = 0; j < resultsArr[i].length; ++j) {
                                        System.out.println(resultsArr[i][j]);
                                        var winnerName = Bukkit.getOfflinePlayer(UUID.fromString(resultsArr[1][j])).getName();
                                        var winnerTime = resultsArr[0][j];
                                        var winnerDays = TimeUnit.MILLISECONDS.toDays(Integer.parseInt(winnerTime));
                                        sender.sendMessage(globalVars.pluginPrefix+" "+ ChatColor.GOLD+j+"st  "+winnerName+" Has played for "+winnerDays+" days.");
                                    }
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        break;
                }
            }
        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}