package de.smoodi.projectchip.sql;


import de.smoodi.projectchip.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

public class SQLBridge {

    private Main ref_main;
    private Connection con;
    public SQLBridge(String host, String port, String dbname, String username, String pw){

        //We start a conneciton
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection( "jdbc:mysql://" + host + ":" + port + "/" + dbname, username, pw);
            con.setAutoCommit(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

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

        con.close();
    }


}
