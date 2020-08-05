/*
 * ResearchList.java
 *
 * Created on March 1, 2010, 3:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package config;

import GUI.DOMUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ebock
 */
public class ResearchList {
    private Document document;
    private String fileName;
    private String email;
    
    /** Creates a new instance of ResearchList */
    public ResearchList() {
        fileName = "ResearchList.xml";
    }
    
    public void buildDefaultFile(){
        String rootName = "PROTOCOL";
        Element root;
        Element institute;
        Element protocol;
        Element category;
        
        document = DOMUtilities.createDOM(rootName);
        root = document.getDocumentElement();
        
        institute = DOMUtilities.insertTask(document, "CHILDRENS", root);

        
        institute = DOMUtilities.insertTask(document, "COLLAB", root);
        
        
        institute = DOMUtilities.insertTask(document, "MARQUETTE", root);
        protocol = DOMUtilities.insertElement(document, institute, "PROJNAME", "pedaling");
        DOMUtilities.insertAttribute(document, protocol, "EMAIL", "");
        
        institute = DOMUtilities.insertTask(document, "MCW", root);
        DOMUtilities.insertAttribute(document, institute, "NAME", "MCW");
        protocol = DOMUtilities.insertElement(document, institute, "PROJNAME", "cloze");
        DOMUtilities.insertAttribute(document, protocol, "EMAIL", "");
        protocol = DOMUtilities.insertElement(document, institute, "PROJNAME", "lexdec");
        DOMUtilities.insertAttribute(document, protocol, "EMAIL", "wgraves@mcw.edu");
        protocol = DOMUtilities.insertElement(document, institute, "PROJNAME", "oddmeg");
        DOMUtilities.insertAttribute(document, protocol, "EMAIL", "");
        protocol = DOMUtilities.insertElement(document, institute, "PROJNAME", "restingState");
        DOMUtilities.insertAttribute(document, protocol, "EMAIL", "");
        protocol = DOMUtilities.insertElement(document, institute, "PROJNAME", "senment");
        DOMUtilities.insertAttribute(document, protocol, "EMAIL", "");
        protocol = DOMUtilities.insertElement(document, institute, "PROJNAME", "visual_connectome");
        DOMUtilities.insertAttribute(document, protocol, "EMAIL", "");
        
        institute = DOMUtilities.insertTask(document, "UW", root);
        DOMUtilities.insertAttribute(document, institute, "NAME", "UW");
        DOMUtilities.writeXmlToFile(fileName, document);
    }
    
    public NodeList getInstitutions(){
        document = DOMUtilities.parse(fileName);
        NodeList nodes = document.getElementsByTagName("NAME");
        return (nodes);
        
    }
    
    public String getEmail(String institute, String project){
        email = "";
        document = DOMUtilities.parse(fileName);
        NodeList nodes = document.getElementsByTagName("PROJNAME");
        int len = nodes.getLength();
        for (int i=0; i<len; i++){
            Node tempNode = nodes.item(i);
            if (tempNode.getNodeName().equals(project)){
                email = tempNode.getAttributes().getNamedItem("EMAIL").toString();
                break;
            }
        }
        return(email);
    }
    
    public void sendEmail(){
        
    }
}
