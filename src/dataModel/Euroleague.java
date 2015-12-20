/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataModel;

import java.util.ArrayList;

/**
 *
 * @author Konstantinos.Blatsou
 */
public class Euroleague implements java.io.Serializable {
    
    private ArrayList<Team> teams;
    
    public Euroleague(ArrayList<Team> teams){
        this.teams=teams;
    }
   
    public ArrayList<Team> getEuroleagueTeams(){
        return teams;
    }
    
}
