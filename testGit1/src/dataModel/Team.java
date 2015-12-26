/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Konstantinos.Blatsou
 */
public class Team implements java.io.Serializable{
    private String teamName;
    private String teamCode;
    private String season;
    private ArrayList<Match> matches;
    private ArrayList<Player> teamPlayers;
    
    public Team(String teamName,String teamCode,String season, ArrayList<Match> matches,ArrayList<Player> teamPlayers){
        this.teamCode=teamCode;
        this.teamName=teamName;
        this.season=season;
        this.matches=matches;
        this.teamPlayers=teamPlayers;
    }
    //get team players
    public ArrayList<Player> getTeamPlayers() {
        return teamPlayers;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }
    //get team name
    public String getTeamName(){
        return teamName;
    }
    //get teamCode
    public String getTeamCode(){
        return teamCode;
    }
    //get season
    public String getTeamSeason(){
        return season;
    }
             
}
