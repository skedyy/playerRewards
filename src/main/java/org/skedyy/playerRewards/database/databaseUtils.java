package org.skedyy.playerRewards.database;

import org.bukkit.entity.Player;
import org.skedyy.playerRewards.utils.globalVars;
import java.sql.*;
import java.util.ArrayList;

public class databaseUtils {
    public static class DatabaseManager {
        private final Connection connection;
        private final globalVars globalVars;
        public DatabaseManager(String path,Double databaseVersion,Double pluginVersion, globalVars globalVariables) throws SQLException {
            this.globalVars = globalVariables;
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                        "uuid TEXT PRIMARY KEY, " +
                        "playedTime INT NOT NULL DEFAULT 0,"+
                        "sessionTimestamp INT NOT NULL DEFAULT 0);");
                statement.execute("CREATE TABLE IF NOT EXISTS playerRewards_metadata (" +
                        "databaseVersion FLOAT NOT NULL DEFAULT "+databaseVersion+","+
                        "createdAt INT NOT NULL DEFAULT "+System.currentTimeMillis()+","+
                        "pluginVersion FLOAT NOT NULL DEFAULT "+pluginVersion+","+
                        "cycleStart INT NOT NULL DEFAULT "+System.currentTimeMillis()+")");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try{
                var prepstatement = connection.prepareStatement("SELECT * FROM playerRewards_metadata");
                var resultSet = prepstatement.executeQuery();
                if (!resultSet.next()) {
                    resultSet.close();
                    var preparedstatement = connection.prepareStatement("INSERT INTO playerRewards_metadata (databaseVersion, createdAt, pluginVersion, cycleStart) VALUES ("+databaseVersion+","+System.currentTimeMillis()+","+pluginVersion+","+System.currentTimeMillis()+");");
                    preparedstatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public void addPlayer(Player player) throws SQLException {
            if (!playerExists(player)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (uuid) VALUES (?)")) {
                    preparedStatement.setString(1, player.getUniqueId().toString());
                    preparedStatement.executeUpdate();
                }catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void setCycleStart() throws SQLException {
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE playerRewards_metadata SET cycleStart = ?")) {
                    preparedStatement.setLong(1, System.currentTimeMillis());
                    preparedStatement.executeUpdate();
                }catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        public Long getCycleStart() throws SQLException {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT cycleStart FROM playerRewards_metadata")) {
                var resultSet = preparedStatement.executeQuery();
                var cycleStart = resultSet.getLong(1);
                resultSet.close();
                return cycleStart;
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public void setPlayedTime(Player player,Long time) throws SQLException {
            if (playerExists(player)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET playedTime = ? WHERE uuid = ?")) {
                    preparedStatement.setLong(1,time);
                    preparedStatement.setString(2, player.getUniqueId().toString());
                    preparedStatement.executeUpdate();
                }catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public void setPlayedTime(String UUID,Long time) throws SQLException {
            if (playerExists(UUID)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET playedTime = ? WHERE uuid = ?")) {
                    preparedStatement.setLong(1,time);
                    preparedStatement.setString(2, UUID);
                    preparedStatement.executeUpdate();
                }catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public Long getPlayedTime(Player player) throws SQLException {
            if (playerExists(player)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT playedTime FROM players WHERE uuid = ?")) {
                    preparedStatement.setString(1, player.getUniqueId().toString());
                    var resultSet = preparedStatement.executeQuery();
                    var playedTime = resultSet.getLong("playedTime");
                    resultSet.close();
                    return playedTime;

                }catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }
        public Long getPlayedTime(String UUID) throws SQLException {
            if (playerExists(UUID)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT playedTime FROM players WHERE uuid = ?")) {
                    preparedStatement.setString(1, UUID);
                    var resultSet = preparedStatement.executeQuery();
                    var playedTime = resultSet.getLong("playedTime");
                    resultSet.close();
                    return playedTime;
                }catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }
        public void setSessionTimestamp(Player player,Long timestamp) throws SQLException {
            if (playerExists(player)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET sessionTimestamp = ? WHERE uuid = ?")) {
                    preparedStatement.setLong(1,timestamp);
                    preparedStatement.setString(2, player.getUniqueId().toString());
                    preparedStatement.executeUpdate();
                }catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        public boolean playerExists(Player player) throws SQLException {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
                preparedStatement.setString(1, player.getUniqueId().toString());
                return preparedStatement.executeQuery().next();
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public boolean playerExists(String UUID) throws SQLException {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
                preparedStatement.setString(1, UUID);
                return preparedStatement.executeQuery().next();
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public boolean sessionTimestampExists(Player player) throws SQLException {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT sessionTimestamp FROM players WHERE uuid = ?")) {
                preparedStatement.setString(1, player.getUniqueId().toString());
                return preparedStatement.executeQuery().next();
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean sessionTimestampExists(String UUID) throws SQLException {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT sessionTimestamp FROM players WHERE uuid = ?")) {
                preparedStatement.setString(1, UUID);
                return preparedStatement.executeQuery().next();
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        public Integer getSessionTimestamp(Player player) throws SQLException {
            if (playerExists(player)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT sessionTimestamp FROM players WHERE uuid = ?")) {
                    preparedStatement.setString(1, player.getUniqueId().toString());
                    var resultSet = preparedStatement.executeQuery();
                    var sesssionTimestamp = resultSet.getInt("sessionTimestamp");
                    resultSet.close();
                    return sesssionTimestamp;

                }catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }
        public Long getSessionTimestamp(String UUID) throws SQLException {
            if (playerExists(UUID)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT sessionTimestamp FROM players WHERE uuid = ?")) {
                    preparedStatement.setString(1, UUID);
                    var resultSet = preparedStatement.executeQuery();
                    var sesssionTimestamp = resultSet.getLong("sessionTimestamp");
                    resultSet.close();
                    return sesssionTimestamp;

                }catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }
        public String[][] getResults() {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players ORDER BY playedTime DESC LIMIT ?");
                preparedStatement.setInt(1, globalVars.competitionWinnners );
                var resultSet = preparedStatement.executeQuery();
                ArrayList<String> results = new ArrayList<>();
                ArrayList<String> uuids = new ArrayList<>();
                while (resultSet.next()) {
                    results.add(resultSet.getString(1));
                    uuids.add(resultSet.getString(2));
                }
                var arrResults = results.toArray(String[]::new);
                var arrUuids = uuids.toArray(String[]::new);
                String[][] resultArray = {{""},{""}};
                for (int i = 0; i < arrResults.length; i++) {
                    resultArray[1][i] = arrResults[i];
                }
                for (int i = 0; i < arrUuids.length; i++) {
                    resultArray[0][i] = arrUuids[i];
                }
                if(arrResults.length >=1 || arrUuids.length >= 1){
                    return resultArray;
                }

            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
        public void closeConnection() throws SQLException {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }
}
