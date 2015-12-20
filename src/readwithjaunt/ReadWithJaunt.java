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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map; 
/**
 *
 * @author Konstantinos.Blatsou
 */
public class ReadWithJaunt {
    //variable that stores team name and team code
    public static Map<String,String> readWteams;
    public static Map<String,String> readWMatches;
    public static Map<String,String> readWPlayers;    
    public static ArrayList<Team> teams;
    public static ArrayList<Match> matches;    
    
    public static void main (String[] args) throws IOException {
        //select season and initialize teams/season
        ReadTeams readWebTeams=new ReadTeams("E2015");            
        //initialize  Euroleague                           
        Euroleague euro=new Euroleague(initTeams(readWebTeams));        
        //serialize Euroleague object (e.g. save data object to disk)
        serializeEuroleagueObject(euro);         
        
        //Euroleague euro=deSerializeEuroleagueObject();
        //iterate through all teams
        for (int currentTeam = 0; currentTeam < euro.getEuroleagueTeams().size(); currentTeam++) {            
            //print current team
            System.out.println();            
            System.out.println(euro.getEuroleagueTeams().get(currentTeam).getTeamName());            
            Team team=euro.getEuroleagueTeams().get(currentTeam);
            
            //print team matches
            StringBuilder sb=new StringBuilder();
            
            for(int teamIt=0;teamIt<team.getMatches().size();teamIt++){
                String matchCode=team.getMatches().get(teamIt).getMatchCode();
                sb.append(team.getMatches().get(teamIt).getMatch()+"\t\t");                           
            }       
            System.out.format("%s", sb);
            
            ArrayList<Player> players=team.getTeamPlayers();                 
            for(int currentPlayer=0;currentPlayer<players.size();currentPlayer++){ 
                    //System.out.println( players.get(currentPlayer).getPlayerName());
                    //iterate through team matches
                    for(int teamIt=0;teamIt<team.getMatches().size();teamIt++){
                    String matchCode=team.getMatches().get(teamIt).getMatchCode();
                    String match=team.getMatches().get(teamIt).getMatch();
                    
                    //System.out.println(match);
                    //check if player played at the specific match
                        if(players.get(currentPlayer).getMatchCodeNStats().containsKey(matchCode)){                          
                          //System.out.println( "Player stats "+players.get(currentPlayer).getMatchCodeNStats().get(matchCode));
                        }
                        else{
                            //System.out.println( "Not played");
                        }
                    } //end matches iteration    
            }                                              
        }
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
        for (Map.Entry<String,String> entry:readWteams.entrySet()) {                                                
            //get team name
            String teamName = entry.getKey();
            //get team code
            String teamCode = entry.getValue();
            //get season code
            String seasonCode = "E2015";
            //get team matches
            //match per team/season (team code/ season code)            
            ReadMatches matches=new ReadMatches(teamCode,"E2015");
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
                       
        }
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
        for (Map.Entry<String,String> entry:readWMatches.entrySet()) {
            
            if( isMatchPlayed( entry.getKey(), entry.getValue()) ){                                
                String matchCode = entry.getKey();
                String finalScore = entry.getValue().split(":")[1].trim();                
                //check if match is home or away
                String awayTeam;
                String homeTeam;
                if(entry.getValue().substring(0, 2).equals("at")){
                    //away                    
                    homeTeam = entry.getValue().split(":")[0].substring(3, entry.getValue().split(":")[0].length());
                    awayTeam = teamName;
                }
                else{
                    //home
                    awayTeam = entry.getValue().split(":")[0].substring(3, entry.getValue().split(":")[0].length());
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
    

