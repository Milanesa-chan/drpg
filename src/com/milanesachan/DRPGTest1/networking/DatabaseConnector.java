package com.milanesachan.DRPGTest1.networking;

import com.milanesachan.DRPGTest1.commons.exceptions.ServerNotFoundException;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;

public class DatabaseConnector {
    private static DatabaseConnector dcInstance;
    private String serverUrl = "jdbc:mysql://localhost:3306/drpg_test1?useSSL=false";
    private String serverUser = "root";
    private String serverPassword = "LaragonRunsThisServer";

    private DatabaseConnector(){}

    public static DatabaseConnector getInstance(){
        if(dcInstance == null){
            dcInstance = new DatabaseConnector();
        }
        return dcInstance;
    }

    public Connection getDatabaseConnection() throws SQLException {
        try {
            return DriverManager.getConnection(serverUrl, serverUser, serverPassword);
        } catch (SQLException e) {
            System.out.println("There was an error connecting to the database.");
            throw e;
        }
    }

    public boolean isGuildInDatabase(long serverID) throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con!=null){
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM `guilds` WHERE `UID`="+serverID);
            return result.next();
        }else{
            throw new SQLException();
        }
    }

    public String getGuildName(long serverID) throws ServerNotFoundException, SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        String name;

        if(con!=null){
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM `guilds` WHERE `UID`="+serverID);
            if(result.next()){
                name = result.getString("Name");
                return name;
            }else{
                throw new ServerNotFoundException();
            }
        }else{
            throw new SQLException();
        }
    }

    public boolean isCharacterInDatabase(long userID) throws SQLException {
        Connection con = DatabaseConnector.getInstance().getDatabaseConnection();
        if(con!=null){
            Statement stmt = con.createStatement();
            ResultSet result = stmt.executeQuery("SELECT * FROM `characters` WHERE `UID`="+userID);
            return result.next();
        }else{
            throw new SQLException();
        }
    }
}
