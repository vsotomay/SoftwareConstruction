/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.code.graph;

/**
 *
 * @author Pluto
 */
import java.io.File;

import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class XMLManager {

    public XMLManager() {
        try {
            this.writeXML();
        } catch (ParserConfigurationException parse) {

        } catch (TransformerException transformerException) {

        }
    }

    private void writeXML() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();
        Element element = document.createElement("CodeGraph");
        document.appendChild(element);

        Attr attr = document.createAttribute("id");
        attr.setValue("1");
        element.setAttributeNode(attr);

        String[][] node = new String[5][4];
        List<String> nodes = new ArrayList<String>();
        nodes.add("StmentID:1,2,3;To:4,5");

   //   String[] parts1 = nodes[].split(";");
        //  String keyType = parts1[0];
        // String keyValue = parts1[1];
               // String[][] edge = new String[10][10]; 
//        node[1][2] = "1,2,3";   //node[nodeId][edge to] = "nodeValue, line numbers";
//        node[2][3] = "2";
//        node[3][4] = "4, 5";
//        node[4][5] = "6,7";
        System.out.println("XML:::::");
        System.out.println(Arrays.deepToString(node));

//        for (int i = 0; i < nodes.size(); i++) {
//             for(int j=0; j<node[i].length; j++){
        //         System.out.println("i:"+i+"j:"+j);
        // element.appendChild(this.addNodes("node", "1,2,3", "1", "2", document));
        String nodeName = "node";
        element.appendChild(this.addNodes(nodeName, "4,5", "2", "3", document));
        element.appendChild(this.addNodes(nodeName, "6,7", "3", "4", document));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);

        StreamResult streamresult = new StreamResult(new File("C:\\XML\\data.xml"));
        transformer.transform(source, streamresult);
        
        readXML();
    }

    private Element addNodes(String name, String value, String id, String to, Document document) {
        Text attributetext = document.createTextNode(value);
        Element attributeName = document.createElement(name);
        //  attributeName.setAttribute("id", id);

        Attr attrId = document.createAttribute("id");
        attrId.setValue(id);
        attributeName.setAttributeNode(attrId);
        Attr attrTo = document.createAttribute("to");
        attrTo.setValue(to);
        attributeName.setAttributeNode(attrTo);
        attributeName.appendChild(attributetext);
        return attributeName;
    }

    private void readXML() throws ParserConfigurationException, TransformerException {
        File xmlFile = new File("C:\\XML\\data.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        try {
            Document document = documentBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();
            NodeList list = document.getElementsByTagName("node");

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);

//                if (node.getNodeType() == Node.ELEMENT_NODE) {
//
//                    Element element = (Element) node;
//                    System.out.println("to :***** " + element.getAttribute("to"));
//                  //  System.out.println("First Name : " + element.getElementsByTagName("node1").item(0).getTextContent());
//                    System.out.println("First Name : " + element.getElementsByTagName("node").item(1).getTextContent());
//                  //      System.out.println("attribute : " + element.getElementsByTagName("node").item(1).getNodeValue());
//                        
//                }
                
      //     for(int x=0,size= list.getLength(); x<size; x++) {
            System.out.println("******"+list.item(i).getAttributes().getNamedItem("to").getNodeValue());
     //      }   
//            if (node.hasAttributes()) {
//                    Attr attr = (Attr) node.getAttributes().getNamedItem("to");
//                    if (attr != null) {
//                        String attribute= attr.getValue();                      
//                        System.out.println("attribute: " + attribute);                      
//                    }
//                }             
                
            }
        } catch (Exception e) {

        }
    }
}
