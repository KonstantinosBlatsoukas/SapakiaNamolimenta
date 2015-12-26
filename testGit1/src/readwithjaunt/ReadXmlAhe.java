/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readwithjaunt;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import java.io.IOException;
import org.w3c.dom.Node;
/**
 *
 * @author Konstantinos.Blatsou
 */
public class ReadXmlAhe {
    
       
    //XmlPage xmlPage
    public void parseXmlFile() throws IOException{
        XmlPage xmlPage;
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
             webClient.getOptions().setThrowExceptionOnScriptError(false);
             webClient.getOptions().setJavaScriptEnabled(false);
             XmlPage page = webClient.getPage("http://eftemkt.tcmb.gov.tr/bankasubelistesi/bankaSubeTumListe.xml");                                      
             //total number of banks
             int totalBanks=getBanksInTotal(page);
             //iterate through all banks
             for(int bank=0;bank<totalBanks;bank++){
                 //get current bank 
                 Node currentBank=getCurrentBank(page,bank);
                 //get total subsides/bank
                 int sub=getTotalBankBranches(currentBank);
                 //iterate through banks sub                 
                 for(int subNo=0;subNo<sub;subNo++){
                     //current bank branch                                          
                     Node currentSubBank=currentBank.getChildNodes().item(subNo);                                          
                     for(int bankUnit=0;bankUnit<currentSubBank.getChildNodes().getLength();bankUnit++){                                                                           
                         String tagName=currentSubBank.getChildNodes().item(bankUnit).getNodeName();
                         String tagValue=currentSubBank.getChildNodes().item(bankUnit).getTextContent();                         
                         if(subNo != 0){
                             System.out.println(tagName);
                             //System.out.println(tagName+"#"+tagValue);                                                  
                         }                         
                     }// end of current bank branch                     
                 }                                                                                                      
             }                             
        }
    }
    
    //mehtod that returns total number of banks
    private int getBanksInTotal(XmlPage page){
        return page.getXmlDocument().getChildNodes().item(0).getChildNodes().getLength();
    }    
    //fetch each bank (Node object)
    private Node getCurrentBank(XmlPage page,int bank){
        return page.getXmlDocument().getChildNodes().item(0).getChildNodes().item(bank);                                  
    }    
    //fetch the total number of bank branches
    private int getTotalBankBranches(Node currentBank){
        return currentBank.getChildNodes().getLength();
    }
                             
}
