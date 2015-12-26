/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readwithjaunt;
import dataModel.Euroleague;
import dataModel.Match;
import dataModel.Player;
import dataModel.Team;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map; 
/**
 *
 * @author Konstantinos.Blatsou
 */
public class ReadWithJaunt {
    //variable that stores team name and team code
    public static Map<String,String> readWteams;
    public static ArrayList<ArrayList<String>> readWMatches;
    public static Map<String,String> readWPlayers;    
    public static ArrayList<Team> teams;
    public static ArrayList<Match> matches;    
    
    public static void main (String[] args) throws IOException {
        //data update and Euroleague object serialization
        dataUpdate("E2015");
        //Euroleague object deserialization and write data to files                                             
        writeData();                        
    }    
    //write data to files and deserialization 
    public static void writeData() throws FileNotFoundException, UnsupportedEncodingException{
        Euroleague euro=deSerializeEuroleagueObject();
        //iterate through all teams and write results into files
        writeDataToFiles(euro);
    }
    //data update and object serilization
    public static void dataUpdate(String season) throws IOException{
        System.out.println("updating...");
        //select season and initialize teams/season
        ReadTeams readWebTeams=new ReadTeams(season);            
        //initialize  Euroleague                           
        Euroleague euro=new Euroleague(initTeams(readWebTeams));        
        //serialize Euroleague object (e.g. save data object to disk)
        serializeEuroleagueObject(euro);    
    }
    //write processed data to files
    public static void writeDataToFiles(Euroleague euro) throws FileNotFoundException, UnsupportedEncodingException{
        for (int currentTeam = 0; currentTeam < euro.getEuroleagueTeams().size(); currentTeam++) {                        
            PrintWriter writer = new PrintWriter("C:\\Euro\\"+euro.getEuroleagueTeams().get(currentTeam).getTeamName()+".txt", "UTF-8");            
            Team team=euro.getEuroleagueTeams().get(currentTeam);                        
            //get players of the team
            ArrayList<Player> players=team.getTeamPlayers();     
            //get mathces of the team
            ArrayList<Match> teamMatches=team.getMatches();            
            //find max players name length
            int maxPlrLen=maxPlayerLen(players);                                                        
            //get team matches in one line                        
            StringBuilder teamMathcesString=getTeamMatches(teamMatches,getEmptyStr(maxPlrLen));    
            //print all team matches
            writer.println(teamMathcesString);
            //iterate through team players
            for(int currentPlayer=0;currentPlayer<players.size();currentPlayer++){                                                                     
                writer.println(getPlrStsPerGame(teamMatches.size(),currentPlayer, teamMatches, players,maxPlrLen));
            }            
            writer.close();
        }
        System.out.println("Data saved in C:\\Euro\\ !");
    }                
    //get player stats for all mathces in one lines
    public static StringBuilder getPlrStsPerGame(int numOfMatches, int currentPlayer, ArrayList<Match> mathces, ArrayList<Player> players, int maxPlrLen){                                                
        
        StringBuilder playerStats=new StringBuilder();
        //check players name length against the maximum ones length
        if(players.get(currentPlayer).getPlayerName().length()<maxPlrLen){
            int gapMax=maxPlrLen-players.get(currentPlayer).getPlayerName().length();                        
            playerStats.append(players.get(currentPlayer).getPlayerName()+getEmptyStr(gapMax));            
        }
        else{
            playerStats.append(players.get(currentPlayer).getPlayerName());
        }                
        //iterate through team matches    
        for(int teamIt=0;teamIt< numOfMatches;teamIt++){
            String matchCode=mathces.get(teamIt).getMatchCode();                        
            //check if player played at the specific match
            if(players.get(currentPlayer).getMatchCodeNStats().containsKey(matchCode)){                                                                          
                playerStats.append(players.get(currentPlayer).getMatchCodeNStats().get(matchCode).split("::")[0]+"  ");
                playerStats.append(players.get(currentPlayer).getMatchCodeNStats().get(matchCode).split("::")[1]);                
                playerStats.append(getEmptyStr(mathces.get(teamIt).getMatch().length()-players.get(currentPlayer).getMatchCodeNStats().get(matchCode).length()));
            }
            else{
                playerStats.append("Not played");                            
                playerStats.append(getEmptyStr(mathces.get(teamIt).getMatch().length()-new String("Not played").length()));
            }            
            playerStats.append(" ");                                       
        } //end matches iteration                                        
        return playerStats;
    }    
    //return team matches in one line 
    public static StringBuilder getTeamMatches(ArrayList<Match> teamMatches, StringBuilder gap){                
        StringBuilder teamMatchesS=new StringBuilder();        
        teamMatchesS.append(gap);
        for(int teamIt=0;teamIt<teamMatches.size();teamIt++){                
                teamMatchesS.append(teamMatches.get(teamIt).getMatch()+" ");    
                
        }               
        return teamMatchesS;
    }
    //return empty string 
    public static StringBuilder getEmptyStr(int len){                
        StringBuilder teamMatches=new StringBuilder();
        for(int teamIt=0;teamIt<len;teamIt++){                
            teamMatches.append(" ");                           
        }                                
        return teamMatches;
    }
    //method that returns the length of the longest player name
    public static int maxPlayerLen(ArrayList<Player> players){        
        ArrayList<Integer> plLen=new ArrayList<Integer>();
        //iterate through all players
        for(int curPlayer=0;curPlayer<players.size();curPlayer++){
            int plrLen=players.get(curPlayer).getPlayerName().length();                        
            plLen.add(plrLen);            
        }
        //get max players length name           
        return Collections.max(plLen);
    }                    
    //fetch data from disk (e.g. Euroleague)
    public static Euroleague deSerializeEuroleagueObject(){
      Euroleague e = null;
      try{
         FileInputStream fileIn = new FileInputStream("C:\\myObject\\euroleague.ser");
         ObjectInputStream in = new ObjectInputStream(fileIn);
         e = (Euroleague) in.readObject();
         in.close();
         fileIn.close();
      }catch(IOException i){
         i.printStackTrace();
         return null;
      }catch(ClassNotFoundException c){
         System.out.println("Employee class not found");
         c.printStackTrace();
         return null;
      }
      return e;
     }
    //save data to disk (e.g. serialize object to disk)
    public static void serializeEuroleagueObject(Euroleague leaugue){
        try {
            File yourFile = new File("C:\\myObject\\euroleague.ser");
            if (!yourFile.exists()) {
                yourFile.getParentFile().mkdirs();
                yourFile.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(yourFile, false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(leaugue);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in C:\\myObject\\euroleague.ser");
        } catch(IOException i) {
            System.out.println("Error occurred while serializing the object on the system!");
            i.printStackTrace();
        }
    }
    //initializes all euroleuge teams    
    public static ArrayList<Team> initTeams(ReadTeams readTeam) throws IOException{        
        
        readWteams=new HashMap<String,String>(); 
        readWteams=readTeam.getTeams();
        
        teams=new ArrayList<Team>();
        //iterate through all season teams
        int updateProgress=0;
        System.out.println();
        System.out.println("Wait for 24 dashes in order update to complete:");
        for (Map.Entry<String,String> entry:readWteams.entrySet()) {                                                
            //get team name
            String teamName = entry.getKey();
            //get team code
            String teamCode = entry.getValue();
            //get season code
            String seasonCode = "E2015";
            //get team matches
            //match per team/season (team code/ season code)            
            ReadMatches matches=new ReadMatches(teamCode,seasonCode);                                   
            ArrayList<Match> teamMatches=initMatchesPerTeams(teamName,matches);
            //get team players                                    
            ArrayList<Player> teamPlayers=initPlayersPerTeam(teamCode,"E2015");            
            //set team object
            //@param1 : team name
            //@param2 : team code
            //@param3 : season code
            //@param4 : matches that team played
            //@param5 : current team players
            teams.add(new Team(teamName, teamCode, seasonCode, teamMatches, teamPlayers) );
            updateProgress+=4;
            System.out.format("#");
                       
        }
        System.out.println();
        System.out.println("Update completed!");
        System.out.println();
        return teams; 
    }    
    //method that returns team players    
    public static ArrayList<Player> initPlayersPerTeam(String teamCode, String seasonCode) throws IOException{
            ReadPlayers p=new ReadPlayers();
            HashMap<String,String> readWPlayers=new HashMap<String,String>();
            readWPlayers=p.getPlayersCodeNNames(teamCode, seasonCode);            
            //array lis that holds team players           
            ArrayList<Player> players=new ArrayList<Player>();
            //iterate through all team players (for a given season and team code)
            for (Map.Entry<String,String> entryp:readWPlayers.entrySet()) {
                //init and create a player object
                Player player=new Player(entryp.getValue(), entryp.getKey(),p.getPlayersMatchesStas(entryp.getKey(), seasonCode));                        
                //insert player
                players.add(player);                        
            }
        return players;        
    }        
    //method that returns an array list with matches per team
    public static ArrayList<Match> initMatchesPerTeams(String teamName, ReadMatches matches) throws IOException{
        //get matches for a specific team
        readWMatches=matches.getCodeMatches();
        //initialize match object and matches array list
        ArrayList<Match> matchList=new ArrayList<Match>();
        
        for(int i=readWMatches.size()-1;i>=0;i--){
            if( isMatchPlayed( readWMatches.get(i).get(0), readWMatches.get(i).get(1)) ){                                
                String matchCode = readWMatches.get(i).get(0);
                String finalScore = readWMatches.get(i).get(1).split(":")[1].trim();                
                //check if match is home or away
                String awayTeam;
                String homeTeam;
                if(readWMatches.get(i).get(1).substring(0, 2).equals("at")){
                    //away                    
                    homeTeam = readWMatches.get(i).get(1).split(":")[0].substring(3, readWMatches.get(i).get(1).split(":")[0].length());
                    awayTeam = teamName;
                }
                else{
                    //home
                    awayTeam = readWMatches.get(i).get(1).split(":")[0].substring(3, readWMatches.get(i).get(1).split(":")[0].length());
                    homeTeam = teamName;
                }
                //add the specific mathcn to list                
                matchList.add(new Match(homeTeam, awayTeam, finalScore, matchCode));                
            }//end of match check                            
        }                                                                     
        return matchList;
    }    
    //method that check if a specific match is played or not
    public static boolean isMatchPlayed(String matchCode, String teamScore ){
        return (!matchCode.isEmpty() && !teamScore.isEmpty() && !teamScore.split(":")[1].trim().isEmpty());        
    }                 
}
    

