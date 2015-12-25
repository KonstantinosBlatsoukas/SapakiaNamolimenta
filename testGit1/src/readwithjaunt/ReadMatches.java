/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readwithjaunt;
import java.io.IOException;
import java.util.Map;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import java.util.HashMap;
/**
 *
 * @author Konstantinos.Blatsou
 */
public class ReadMatches {
    
    private String season;
    private String gameCode;
    private Map<String,String> matchMap;
    
    public ReadMatches(String gameCode,String season){
        this.season=season;
        this.gameCode=gameCode;
        matchMap=new HashMap<String,String>();
    }
    
    public Map<String,String> getCodeMatches() throws IOException{
        //data structure initialization 
        //teams=new HashMap<String,String>();
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {                        
    
         webClient.getOptions().setThrowExceptionOnScriptError(false);
         webClient.getOptions().setJavaScriptEnabled(false);
         HtmlPage page = webClient.getPage("http://www.euroleague.net/competition/teams/showteam?clubcode="+gameCode+"&seasoncode="+season);
             
         if (!page.getByXPath("//div[@class='TeamPhaseGamesMainContainer']").isEmpty()){    
              
            HtmlElement ulTeams = (HtmlElement) page.getByXPath("//div[@class='TeamPhaseGamesMainContainer']").get(0);                                    
            //Table with matches
            HtmlTableBody teamTable=(HtmlTableBody)ulTeams.getChildNodes().get(5).getChildNodes().get(1).getChildNodes().get(1).getChildNodes().get(1);
            
            
            //iterate through mathces
            int mathcesCount=teamTable.getChildNodes().size();
            for(int i=0;i<mathcesCount;i++){                                                  
                //get specific data for each match
                String codeGame=new String();
                String matchDetails=new String();
                String opponent=new String();
                String FT=new String();
                for(int j=1;j<teamTable.getChildNodes().get(i).getChildNodes().size()-1;j++){                    
                    
                    if(!teamTable.getChildNodes().get(i).getChildNodes().isEmpty()){                        
                        HtmlTableDataCell matchElement=(HtmlTableDataCell)teamTable.getChildNodes().get(i).getChildNodes().get(j);                        
                        //fetch inside td tag
                        if(matchElement.getChildNodes().size()>0){
                            for(int k=0;k<matchElement.getChildNodes().size();k++){
                                                            
                                //get match code
                                if( matchElement.getChildNodes().get(k).getNodeName().equals("a")&& j == 3){                                    
                                   HtmlAnchor matchGameCode=(HtmlAnchor)matchElement.getChildNodes().get(k);
                                   //game code
                                   String gameCode=matchGameCode.getAttribute("href").split("\\?")[1].split("&")[0].split("=")[1];                                   
                                   codeGame=gameCode;
                                                                      
                                   //first iteration you get home or away match
                                   //at second iteration you get the name opponent                                   
                                   for(int kk=0;kk<matchElement.getChildNodes().get(k).getChildNodes().size();kk++){
                                                                              
                                       if(matchElement.getChildNodes().get(k).getChildNodes().get(kk).getNodeName().equals("span")) {                                           
                                           HtmlSpan matchGameOpponent=(HtmlSpan)matchElement.getChildNodes().get(k).getChildNodes().get(kk);
                                           //get opponent                                            
                                           if ( kk == 1 ){
                                               opponent=opponent+matchGameOpponent.getTextContent()+" "; 
                                           }
                                           else if( kk == 3 ){
                                               opponent=opponent+matchGameOpponent.getTextContent(); 
                                           }                                           
                                       }
                                   }                                   
                                   
                                }
                                
                                //get final score
                                if( matchElement.getChildNodes().get(k).getNodeName().equals("a")&& j == 4){                                    
                                   HtmlAnchor matchGameCode=(HtmlAnchor)matchElement.getChildNodes().get(k);
                                   //game score//                                   
                                   for(int kk=0;kk<matchElement.getChildNodes().get(k).getChildNodes().size();kk++){
                                       if(matchElement.getChildNodes().get(k).getChildNodes().get(kk).getNodeName().equals("span")) {                                           
                                           HtmlSpan matchGameOpponent=(HtmlSpan)matchElement.getChildNodes().get(k).getChildNodes().get(kk);
                                           HtmlAnchor firstRes=(HtmlAnchor)matchGameOpponent.getParentNode();
                                           FT=firstRes.getTextContent();                                                                                                         
                                           
                                       }
                                   }                                   
                                }
                                                                                                
                            }                                                                        
                        }                                                
                    } 
                }                             
                matchMap.put(codeGame, opponent+" : "+FT);
            }             
                        
         }                  
        }  
        return matchMap;
    }
        
}
