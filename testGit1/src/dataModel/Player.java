/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataModel;

import java.util.HashMap;

/**
 *
 * @author Konstantinos.Blatsou
 */

public class Player implements java.io.Serializable{
    
    private String playerName;
    private String playerCode;
    private HashMap<String,String> matchCodeNStats;
    
    public Player(String playerName, String playerCode, HashMap<String, String> matchCodeNStats) {
        this.playerName = playerName;
        this.playerCode = playerCode;
        this.matchCodeNStats = matchCodeNStats;
    }
    
    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerCode() {
        return playerCode;
    }

    public HashMap<String, String> getMatchCodeNStats() {
        return matchCodeNStats;
    }

    
    
}
