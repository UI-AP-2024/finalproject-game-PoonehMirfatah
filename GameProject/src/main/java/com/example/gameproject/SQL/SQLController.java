package com.example.gameproject.SQL;

import Controllers.PlayerController;
import Models.Player;


import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLController {

    public static int getMaxID() throws SQLException {
        String sqlCmd="Select MAX(ID) from player";
        SQLConnection sql=new SQLConnection();
        ResultSet rs=sql.executeQuery(sqlCmd);
        if(rs.next()){
            return rs.getInt(1);
        }else {
            return 0;
        }
    }
    public static void insertPlayer(Player player) throws Exception {
        SQLConnection sql = new SQLConnection();
        String SQLcom = String.format("INSERT INTO player (ID, UserName, Password, Level, Diamonds) VALUES (%s, '%s','%s',%s, %s)", player.getID(),player.getUserName(),player.getPassword(),player.getLevel(),player.getDiamonds());
//        for(Spell spell:player.getBackPack()){
//        String SQLcom2 = String.format("INSERT INTO backpacks (ID,SpellName) VALUES (%s,'%s')",player.getID(),spell.toString());
//            sql.executeSQL(SQLcom2);
//        }
        sql.executeSQL(SQLcom);

    }

    public static void loadPlayer(String userName,String password) throws SQLException {
        String sqlCmd = String.format("Select * from player WHERE UserName='%s' AND Password='%s'",userName,password);
        SQLConnection sql = new SQLConnection();
        ResultSet rs = sql.executeQuery(sqlCmd);
        if (rs.next()) {
            Player player=new Player(rs.getString("UserName"),rs.getString("Password")) ;
            player.setID(Integer.parseInt(rs.getString("ID")));
            player.setLevel(Integer.parseInt(rs.getString("Level")));
            player.setDiamonds(Integer.parseInt(rs.getString("Diamonds")));
            PlayerController.getInstance().player=player;
            System.out.println(PlayerController.getInstance().player.getUserName());
        }
    }
    public static void insertSpell(String spellName, int count) throws Exception {
        int ID=PlayerController.getInstance().player.getID();
        String SQLcom = String.format("INSERT INTO spells (PlayerID, SpellName,Count) VALUES (%s, '%s',%s)", ID, spellName, count);
        SQLConnection sql = new SQLConnection();
        sql.executeSQL(SQLcom);
    }
    public static void deletePlayerSpells(int ID) throws Exception {
        String SQLcom = String.format("DELETE FROM spells WHERE PlayerID=%s", ID);
        SQLConnection sql = new SQLConnection();
        sql.executeSQL(SQLcom);
    }
}
