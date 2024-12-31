package org.skedyy.playerRewards.commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.skedyy.playerRewards.database.databaseUtils;
import org.skedyy.playerRewards.utils.globalMessages;
import org.skedyy.playerRewards.utils.globalVars;


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
            if(args.length == 0) {
                //Display help command
                if (sender.hasPermission("playerrewards.help")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',globalVars.pluginPrefix + " " + ChatColor.GREEN + "/playerRewards results " + ChatColor.GOLD + "Show the actual competition leaderboard. "));
                }
            } else if (args.length == 1) {
                switch (args[0]){
                    case "results":
                        if (sender.hasPermission("playerrewards.results")) {
                            var compResults = new competitionResults(globalVars,databaseManager,new globalMessages(globalVars.config));
                            compResults.results(sender,command,label,args);
                        }else{
                        }
                    break;
                    default:
                        if (sender.hasPermission("playerrewards.help")) {
                            sender.sendMessage(globalVars.pluginPrefix + " " + ChatColor.GREEN + "/playerRewards results " + ChatColor.GOLD + "Show the actual competition leaderboard. ");
                        }
                    break;
                }
            }
        }
        return true;
    }
}