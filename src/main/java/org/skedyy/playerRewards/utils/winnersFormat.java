package org.skedyy.playerRewards.utils;

import org.bukkit.ChatColor;
import org.skedyy.playerRewards.Main;

import java.util.concurrent.TimeUnit;

public class winnersFormat {
    private final globalVars globalVars;
    public winnersFormat(globalVars globalVars) {
        this.globalVars = globalVars;
    }
    public void checkTimeFormat() {
        var timeFormat = globalVars.winnersTimeFormat;
        if(timeFormat==null){
            globalVars.server.getConsoleSender().sendMessage(ChatColor.GOLD+"[playerRewards] "+ChatColor.RED+"Check the config, winnersTimeFormat is invalid!");
            globalVars.pluginManager.disablePlugin(globalVars.plugin);
        }
    }

    public long convertTimeUnit(int millis){
        var timeFormat = globalVars.winnersTimeFormat;
        globalVars.server.getConsoleSender().sendMessage(ChatColor.GOLD+"[playerRewards] timeFormat: "+timeFormat);
        return switch (timeFormat) {
            case "hours" -> TimeUnit.HOURS.convert(millis, TimeUnit.MILLISECONDS);
            case "days" -> TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS);
            case "minutes" -> TimeUnit.MINUTES.convert(millis,TimeUnit.MILLISECONDS);
            case null -> 1;
            default -> -1;

        };
    }
}
