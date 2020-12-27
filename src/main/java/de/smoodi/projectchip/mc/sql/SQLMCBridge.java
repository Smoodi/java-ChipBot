package de.smoodi.projectchip.mc.sql;


import de.smoodi.projectchip.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.UUID;


/**
 * SQL MC Connector
 */
public class SQLMCBridge {

    private Main ref_main;
    private Connection con;
    private String host;
    private String port;
    private String dbname;
    private String username;
    private String pw;

    public SQLMCBridge(String host, String port, String dbname, String username, String pw) {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.username = username;
        this.pw = pw;
        establishConnection();
    }

    /**
     * Returns true if the discord User has a Minecraft Account linked. Otherwise, false.
     * @param discordId
     * @return
     * @throws SQLException
     */
    public boolean isDiscordLinked(long discordId) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM whitelisted_players WHERE DiscordID = \""+ String.valueOf(discordId) + "\";");
        if(!stm.execute()) {
            stm.close();
            return false;
        }
        boolean linked = false;
        ResultSet rs = stm.getResultSet();
        linked = rs.next();
        rs.close();
        stm.close();
        return linked;
    }

    /**
     * Returns a full entry profile about a requested user. Can be null of not linked.
     * @param uuid the minecraft uuid to check for
     * @return A UserProfile
     * @throws SQLException
     */
    public UserProfile getEntryInformation(String uuid) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM whitelisted_players WHERE UUID=\""+uuid+"\"");
        if(stm.execute()) {
            ResultSet r = stm.getResultSet();
            if(!r.next()) {
                r.close();
                stm.close();
                return null;
            }
            UserProfile up = new UserProfile(UUID.fromString(uuid), r.getLong("DiscordID"), r.getBoolean("IsWhitelisted"), r.getString("Nickname"), r.getTimestamp("TimeAdded"), r.getBoolean("Banned"));
            r.close();
            stm.close();
            return up;
        }
        else return null;
    }

    /**
     * Returns a full entry profile about a requested user. Can be null if not linked.
     * @param discordId the Discord Id to check for
     * @return
     * @throws SQLException
     */
    public UserProfile getEntryInformation(long discordId) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM whitelisted_players WHERE DiscordID="+String.valueOf(discordId)+";");
        if(stm.execute()) {
            ResultSet r = stm.getResultSet();
            if(!r.next()) {
                r.close();
                stm.close();
                return null;
            }
            UserProfile up = new UserProfile(UUID.fromString(r.getString("UUID")), r.getLong("DiscordID"), r.getBoolean("IsWhitelisted"), r.getString("Nickname"), r.getTimestamp("TimeAdded"), r.getBoolean("Banned"));
            r.close();
            stm.close();
            return up;
        }
        else return null;
    }

    /**
     * Adds a minecraft account link to the database.
     * @param mc_uuid The Minecraft Account UUID that's supposed to be linked.
     * @param discordId The DiscordId that's suppoesd to be linked.
     * @return True, if successful. Otherwise, false.
     * @throws SQLException
     */
    public boolean addMinecraftAccount(UUID mc_uuid, long discordId) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("INSERT INTO `whitelisted_players` (`UUID`, `DiscordID`, `IsWhitelisted`) VALUES ('" +
                mc_uuid.toString() + "', '" + String.valueOf(discordId) + "', '1');");
        boolean success = stm.execute();
        stm.close();
        return success;
    }

    /**
     * Updates the whitelist status of a user based on their discord id. This means a user's linked minecraft account can be (un-)whitelisted.
     * @param discordId the discord id to check for
     * @param whitelisted true = whitelisted, false = not whitelisted.
     * @return True if successful. Otherwise, false.
     * @throws SQLException
     */
    public boolean updateWhitelistedStatus (long discordId, boolean whitelisted) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("UPDATE whitelisted_players SET IsWhitelisted = '" + (whitelisted ? 1 : 0 ) +
                "'  WHERE DiscordID = '"+String.valueOf(discordId)+"';");
        boolean success = stm.execute();
        stm.close();
        return success;
    }

    /**
     * Updates a linked minecraft account. This fails if there is no previously linked entry.
     * It sets the discord user's new minecraft account.
     * @param discordId the discord user's id
     * @param mc_uuid the new minecraft uuid
     * @return True if successful. Otherwise, false.
     * @throws SQLException
     */
    public boolean updateMinecraftAccount (long discordId, UUID mc_uuid) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("UPDATE whitelisted_players SET UUID = '" + mc_uuid.toString() + "' WHERE discordId = '" + String.valueOf(discordId) + "';");
        boolean success = stm.execute();
        stm.close();
        return success;
    }

    /**
     * Removes an entry / link from the database. This will fail if no entry exists.
     * @param discordId The discord user to remove the entry for.
     * @return True if sucessful. Otherwise, false.
     * @throws SQLException
     */
    public boolean deleteEntry(long discordId) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("DELETE FROM `whitelisted_players` WHERE `whitelisted_players`.`DiscordID` = '" + String.valueOf(discordId) + "'" );
        boolean success = stm.execute();
        stm.close();
        return success;
    }

    /**
     * Internal method used to reconnect to the database in case of a timeout or early connection closing.
     * @throws SQLException
     */
    private void fixClosedTimeout() throws SQLException {
        if(!con.isClosed()) {
            if(con.isValid(2)) {}//nothing
            else {
                con.close();
                establishConnection();
            }
        } else { con.close(); establishConnection(); }
    }

    /**
     * Establishes a new connection to the database.
     */
    private void establishConnection() {

        //We start a conneciton
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname, username, pw);
            con.setAutoCommit(false);

            System.out.println("Successfully (re-)connected to the DB...");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
