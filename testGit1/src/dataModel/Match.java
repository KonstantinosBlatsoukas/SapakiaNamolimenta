/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataModel;

/**
 *
 * @author Konstantinos.Blatsou
 */
public class Match implements java.io.Serializable{
    private String homeTeam;
    private String awayTeam;
    private String finalScore;
    private String matchCode;
    
    public Match(String homeTeam,String awayTeam,String finalScore,String matchCode) {
        this.homeTeam=homeTeam;
        this.awayTeam=awayTeam;        
        this.finalScore=finalScore;
        this.matchCode=matchCode;
    }
    //get match
    public String getMatch(){
        return homeTeam+"-"+awayTeam+" "+finalScore+" ";
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public String getMatchCode() {
        return matchCode;
    }
                    
}
