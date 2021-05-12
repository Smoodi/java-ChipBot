package de.smoodi.projectchip.sql;


import de.smoodi.projectchip.Main;
import de.smoodi.projectchip.mc.sql.UserProfile;
import de.smoodi.projectchip.util.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;

public class SQLBridge {

    private final String host;
    private final String username;
    private final String port;
    private final String dbname;
    private final String pw;

    private Main ref_main;
    private Connection con;
    public SQLBridge(String host, String port, String dbname, String username, String pw){

        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.username = username;
        this.pw = pw;
        establishConnection();

    }

    public void addGIF(String filename, String entry_name, String entry_source, String[] entry_alts) throws SQLException, FileNotFoundException {
        PreparedStatement s = con.prepareStatement("INSERT INTO `approved_gifs`" +
                        " (`name`, `source`, `raw_data`)" +
                        " VALUES (?, ?, ?)");
        s.setString(1, entry_name);
        s.setString(2, entry_source);

        File file = new File(filename);
        if (!file.canRead())
            return;

        FileInputStream inputStream;
        inputStream = new FileInputStream(file);
        int iLength =  (int)file.length();

        s.setBlob(3, inputStream);

        s.execute();

        s.close();
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        con.close();
    }


    /**
     * Returns if the user has been recorded to be on the server before.
     * @param discordId
     * @return true if yes, false otherwise.
     * @throws SQLException
     */
    public boolean existsUserEntry(long discordId) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM certifiedUsers WHERE DiscordID = \""+ String.valueOf(discordId) + "\";");
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

    public MemberProfile getMemberInformation(long discordId) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("SELECT * FROM certifiedUsers WHERE DiscordID="+String.valueOf(discordId)+";");
        if(stm.execute()) {
            ResultSet r = stm.getResultSet();
            if(!r.next()) {
                r.close();
                stm.close();
                return null;
            }
            MemberProfile up = new MemberProfile(r.getLong("DiscordID"), r.getBoolean("IsCertified"), r.getString("FirstJoinName"), r.getTimestamp("FirstJoinTime"), r.getBoolean("IsBanned"));
            r.close();
            stm.close();
            return up;
        }
        else return null;
    }

    /**
     * Sets the certification status of a discord member. This needs to manually kept in sync-with any other changes made on this value.
     * Please note that this section will be overriden by ongoing ROCs. If a member is found to have an ongoing ROC it's certification value might return true,
     * yet not be certified upon join.
     * @param discordId the users discord id
     * @param certified
     * @return True if successful, false otherwise
     * @throws SQLException
     */
    public boolean setCertification(long discordId, boolean certified) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("UPDATE certifiedUsers SET IsCertified = '" + (certified ? 1 : 0 ) +
                "'  WHERE DiscordID = '"+String.valueOf(discordId)+"';");
        boolean success = stm.execute();
        stm.close();
        return success;
    }

    /**
     * Sets the banned status of a discord member. This needs to manually kept in sync-with any other changes made on this value.
     * @param discordId the users discord id
     * @param banned
     * @return True if successful, false otherwise
     * @throws SQLException
     */
    public boolean setBanned(long discordId, boolean banned) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("UPDATE certifiedUsers SET IsBanned = '" + (banned ? 1 : 0 ) +
                "'  WHERE DiscordID = '"+String.valueOf(discordId)+"';");
        boolean success = stm.execute();
        stm.close();
        return success;
    }

    /**
     * Adds a user entry. It's default values will be set to non-certified, not banned and it's current timestamp.
     * @param discordId
     * @param joinName
     * @return True if successful, false otherwise.
     * @throws SQLException
     */
    public boolean addUserEntry(long discordId, String joinName) throws SQLException {
        fixClosedTimeout();
        PreparedStatement stm = con.prepareStatement("INSERT INTO `certifiedUsers` (`DiscordID`, `FirstJoinName`) VALUES ('" +
                String.valueOf(discordId) + "', '" + joinName + "');");
        boolean success = stm.execute();
        stm.close();
        return success;
    }

    /**
     * Internal method used to reconnect to the database in case of a timeout or early connection closing.
     * @throws SQLException
     */
    private void fixClosedTimeout() throws SQLException {
        if(con == null) {
            establishConnection();
            return;
        }

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
    public void establishConnection() {

        //We start a conneciton
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname + "?useSSL=false", username, pw);
            con.setAutoCommit(false);

            System.out.println("Successfully (re-)connected to the DB...");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
