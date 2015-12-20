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
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import java.util.HashMap;
// *
// * @author Konstantinos.Blatsou
// */
public class ReadTeams {
    private String season;
    private Map<String,String> teams;
    public ReadTeams(String season) {   
        //init season attribute
        this.season=season;                        
             
    }
    //retrurns a hash map (pair of team name 
    public Map<String,String> getTeams() throws IOException{
        //data structure initialization 
        teams=new HashMap<String,String>();
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage page = webClient.getPage("http://www.euroleague.net/competition/teams?seasoncode="+season);

            if (!page.getByXPath("//ul[@class='nav-teams']").isEmpty()){                          
               HtmlElement ulTeams = (HtmlElement) page.getByXPath("//ul[@class='nav-teams']").get(0);             
               //get unorder list
               HtmlUnorderedList teamList= (HtmlUnorderedList)ulTeams;                          
               //iterate through list item <li> elements
               for(int i=0;i<ulTeams.getChildNodes().size();i++){                                  
                   //HtmlListItem liTeam= (HtmlListItem)ulTeams.getChildNodes().get(i);                
                   if(ulTeams.getChildNodes().get(i).getChildNodes().get(1) !=  null){                    
                       //get team code
                       HtmlAnchor teamCodeWeb=(HtmlAnchor)ulTeams.getChildNodes().get(i).getChildNodes().get(1);                                        
                       String teamCode=teamCodeWeb.getAttribute("href").split("=")[1].split("&")[0];
                       //get team name
                       HtmlImage teamNameWeb=(HtmlImage)ulTeams.getChildNodes().get(i).getChildNodes().get(1).getChildNodes().get(0);
                       String teamName=teamNameWeb.getAttribute("alt");                    
                       //put data into data structure
                       teams.put(teamName, teamCode);
                   }                
            }             
         }                  
        }  
        return teams;
    }
    //prints all teams        
    public void printTeams(){
    //iterate through a hashmap
        for (Map.Entry<String,String> entry:teams.entrySet()) {            
            System.out.println();
            System.out.println("Team code  "+entry.getKey());
            System.out.println("Team name  "+entry.getValue()); 
            System.out.println();
        }
    }
    
}
