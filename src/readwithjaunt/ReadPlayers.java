/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readwithjaunt;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import java.io.IOException;
import java.util.HashMap;
/**
 *
 * @author Konstantinos.Blatsou
 */
public class ReadPlayers {                     
    //get players (code, names) of a team 
    public HashMap<String,String> getPlayersCodeNNames(String teamCode, String seasonCode) throws IOException{
        HashMap<String,String> playersCodeNames;
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {                        
                
            playersCodeNames=new HashMap<String,String>();
                    
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage page = webClient.getPage("http://www.euroleague.net/competition/teams/showteam?clubcode="+teamCode+"&seasoncode="+seasonCode);                          
            
            if (!page.getByXPath("//div[@class='wp-module wp-module-teamroster wp-module-6319']").isEmpty()){    
                              
                String playerCode=new String();
                String playerName=new String();
                HtmlElement divPlayers = (HtmlElement) page.getByXPath("//div[@class='wp-module wp-module-teamroster wp-module-6319']").get(0);                                    
                for(int i=0;i<divPlayers.getChildNodes().size();i++){
                    
                    if(divPlayers.getChildNodes().get(i).getLocalName() != null){
                        HtmlDivision player= (HtmlDivision) divPlayers.getChildNodes().get(i);
                        //ensure that tag is about a player
                        if(player.getAttribute("class").equals("item player")){
                            for(int j=0;j<player.getChildNodes().size();j++){                                
                                //get div part
                                if(player.getChildNodes().get(j).getNodeName().equals("div")){
                                    HtmlDivision playerz= (HtmlDivision) player.getChildNodes().get(j);
                                    //get inner div part
                                    if (playerz.getAttribute("class").equals("img")){                                        
                                        for(int k=0;k<playerz.getChildNodes().size();k++){
                                            
                                            if(playerz.getChildNodes().get(k).getNodeName().equals("a")){
                                                HtmlAnchor playerAnc=(HtmlAnchor)playerz.getChildNodes().get(k);
                                                //player code
                                                playerCode=playerAnc.getAttribute("href").split("\\?")[1].split("&")[0].split("=")[1];                                                                                                                                                                                              
                                            }
                                        }
                                    }//end of get player code                                    
                                    //get players name
                                    if(playerz.getAttribute("class").equals("name")){
                                        for(int k=0;k<playerz.getChildNodes().size();k++){                                            
                                             if(playerz.getChildNodes().get(k).getNodeName().equals("a")){
                                                 HtmlAnchor playerAncName=(HtmlAnchor)playerz.getChildNodes().get(k);
                                                 //player name
                                                 playerName=playerAncName.getTextContent();                                    
                                             }
                                        }                                    
                                    }                                    
                                    playersCodeNames.put(playerCode, playerName);
                                }
                            }                            
                        }
                    }
                }                                
            }                  
        }          
        return playersCodeNames;                   
    }
    
    //returs player's statistics for a season 
    public HashMap<String,String> getPlayersMatchesStas(String playerCode, String seasonCode) throws IOException {
        //hashp map that holds palyers statistics
        HashMap<String,String> playersMatchStats;                
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {                        
            
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage page = webClient.getPage("http://www.euroleague.net/competition/players/showplayer?pcode="+playerCode+"&seasoncode="+seasonCode);     
            //initialization of variables
            playersMatchStats=new HashMap<String,String>();
            //fetch regular season matches
            if (!page.getByXPath("//div[@id='"+seasonCode+"_RS']").isEmpty()){                                
                HtmlElement divPlayers = (HtmlElement) page.getByXPath("//div[@id='"+seasonCode+"_RS']").get(0);                
                //get how many games player played
                int numberOfmatches=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().size();                                
                String gameCode=new String();
                String totalTime=new String();
                String totalPoints=new String();
                //iterate through matches                
                for(int i=0;i<numberOfmatches;i++){
                    String tagType=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getNodeName();
                    //chaeck tage type
                    if(tagType.equals("tr")){
                        int count=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().size();
                        for(int k=0;k<count;k++){
                            String nodeName=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().get(k).getNodeName();                            
                            //check if tag is td
                            if(nodeName.equals("td")){                                
                                HtmlTableCell cell=(HtmlTableCell)divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().get(k);                                
                                //get match code
                                if(cell.getAttribute("class").equals("RivalContainer")){
                                    for(int kk=0;kk<cell.getChildNodes().size();kk++){                                                                               
                                       HtmlAnchor playerAncGame=(HtmlAnchor)cell.getChildNodes().get(kk);
                                       gameCode=playerAncGame.getAttribute("href").split("\\?")[1].split("&")[0].split("=")[1];
                                    }
                                }               
                                //get total minutes
                                if(cell.getAttribute("class").equals("PlayerTimePlayedContainer")){
                                    totalTime=cell.getTextContent();
                                }
                                //get total points
                                if(cell.getAttribute("class").equals("PlayerScoreContainer")){
                                    totalPoints=cell.getTextContent();
                                }                                                                                                                
                            }
                        }                        
                    }                    
                    //insert data to hash map ("match code" -> "points , minutes played")
                    playersMatchStats.put(gameCode,totalTime+"::"+totalPoints);     
                }//end for mathces iteration                                                                                           
            }
            //top 16
            if (!page.getByXPath("//div[@id='"+seasonCode+"_TS']").isEmpty()){
                HtmlElement divPlayers = (HtmlElement) page.getByXPath("//div[@id='"+seasonCode+"_TS']").get(0);                
                //get how many games player played
                int numberOfmatches=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().size();                                
                String gameCode=new String();
                String totalTime=new String();
                String totalPoints=new String();
                //iterate through matches                
                for(int i=0;i<numberOfmatches;i++){
                    String tagType=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getNodeName();
                    //chaeck tage type
                    if(tagType.equals("tr")){
                        int count=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().size();
                        for(int k=0;k<count;k++){
                            String nodeName=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().get(k).getNodeName();                            
                            //check if tag is td
                            if(nodeName.equals("td")){                                
                                HtmlTableCell cell=(HtmlTableCell)divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().get(k);                                
                                //get match code
                                if(cell.getAttribute("class").equals("RivalContainer")){
                                    for(int kk=0;kk<cell.getChildNodes().size();kk++){                                                                               
                                       HtmlAnchor playerAncGame=(HtmlAnchor)cell.getChildNodes().get(kk);
                                       gameCode=playerAncGame.getAttribute("href").split("\\?")[1].split("&")[0].split("=")[1];
                                    }
                                }               
                                //get total minutes
                                if(cell.getAttribute("class").equals("PlayerTimePlayedContainer")){
                                    totalTime=cell.getTextContent();
                                }
                                //get total points
                                if(cell.getAttribute("class").equals("PlayerScoreContainer")){
                                    totalPoints=cell.getTextContent();
                                }                                                                                                                
                            }
                        }                        
                    }                    
                    //insert data to hash map ("match code" -> "points , minutes played")
                    playersMatchStats.put(gameCode,totalTime+"::"+totalPoints);     
                }//end for mathces iteration                                                                                                                                                        
            }
            //play off's
            if (!page.getByXPath("//div[@id='"+seasonCode+"_PO']").isEmpty()){
                HtmlElement divPlayers = (HtmlElement) page.getByXPath("//div[@id='"+seasonCode+"_PO']").get(0);                
                //get how many games player played
                int numberOfmatches=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().size();                                
                String gameCode=new String();
                String totalTime=new String();
                String totalPoints=new String();
                //iterate through matches                
                for(int i=0;i<numberOfmatches;i++){
                    String tagType=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getNodeName();
                    //chaeck tage type
                    if(tagType.equals("tr")){
                        int count=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().size();
                        for(int k=0;k<count;k++){
                            String nodeName=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().get(k).getNodeName();                            
                            //check if tag is td
                            if(nodeName.equals("td")){                                
                                HtmlTableCell cell=(HtmlTableCell)divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().get(k);                                
                                //get match code
                                if(cell.getAttribute("class").equals("RivalContainer")){
                                    for(int kk=0;kk<cell.getChildNodes().size();kk++){                                                                               
                                       HtmlAnchor playerAncGame=(HtmlAnchor)cell.getChildNodes().get(kk);
                                       gameCode=playerAncGame.getAttribute("href").split("\\?")[1].split("&")[0].split("=")[1];
                                    }
                                }               
                                //get total minutes
                                if(cell.getAttribute("class").equals("PlayerTimePlayedContainer")){
                                    totalTime=cell.getTextContent();
                                }
                                //get total points
                                if(cell.getAttribute("class").equals("PlayerScoreContainer")){
                                    totalPoints=cell.getTextContent();
                                }                                                                                                                
                            }
                        }                        
                    }                    
                    //insert data to hash map ("match code" -> "points , minutes played")
                    playersMatchStats.put(gameCode,totalTime+"::"+totalPoints);     
                }//end for mathces iteration                         
            }
            //final four            
            if (!page.getByXPath("//div[@id='"+seasonCode+"_FF']").isEmpty()){
                HtmlElement divPlayers = (HtmlElement) page.getByXPath("//div[@id='"+seasonCode+"_FF']").get(0);                
                //get how many games player played
                int numberOfmatches=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().size();                                
                String gameCode=new String();
                String totalTime=new String();
                String totalPoints=new String();
                //iterate through matches                
                for(int i=0;i<numberOfmatches;i++){
                    String tagType=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getNodeName();
                    //chaeck tage type
                    if(tagType.equals("tr")){
                        int count=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().size();
                        for(int k=0;k<count;k++){
                            String nodeName=divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().get(k).getNodeName();                            
                            //check if tag is td
                            if(nodeName.equals("td")){                                
                                HtmlTableCell cell=(HtmlTableCell)divPlayers.getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(3).getChildNodes().get(i).getChildNodes().get(k);                                
                                //get match code
                                if(cell.getAttribute("class").equals("RivalContainer")){
                                    for(int kk=0;kk<cell.getChildNodes().size();kk++){                                                                               
                                       HtmlAnchor playerAncGame=(HtmlAnchor)cell.getChildNodes().get(kk);
                                       gameCode=playerAncGame.getAttribute("href").split("\\?")[1].split("&")[0].split("=")[1];
                                    }
                                }               
                                //get total minutes
                                if(cell.getAttribute("class").equals("PlayerTimePlayedContainer")){
                                    totalTime=cell.getTextContent();
                                }
                                //get total points
                                if(cell.getAttribute("class").equals("PlayerScoreContainer")){
                                    totalPoints=cell.getTextContent();
                                }                                                                                                                
                            }
                        }                        
                    }                    
                    //insert data to hash map ("match code" -> "points , minutes played")
                    playersMatchStats.put(gameCode,totalTime+"::"+totalPoints);     
                }//end for mathces iteration                                       
            }                 
        }        
        return playersMatchStats;
    }
    
    
    
    
}
