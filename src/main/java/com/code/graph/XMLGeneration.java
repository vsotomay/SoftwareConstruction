/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.code.graph;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang3.StringUtils;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 * @author T7
 */
public class XMLGeneration {

    private ArrayList<ArrayList<Integer>> nodeList;
    private ArrayList<ArrayList<Integer>> edgeList;
    String methodName;

    public XMLGeneration(ArrayList<ArrayList<Integer>> nodeList, ArrayList<ArrayList<Integer>> edgeList, String methodName) {
        this.nodeList = nodeList;
        this.edgeList = edgeList;
        this.methodName = methodName;
    }


  
    protected void writeXML() throws ParserConfigurationException, TransformerException {
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


        String nodeName = "node";
        for (int edgeI = 0; edgeI < edgeList.size(); edgeI++) {

        int nodeIndexFrom = edgeList.get(edgeI).get(0);
        int nodeIndexTo = edgeList.get(edgeI).get(1);
System.out.println("nodeIndexFrom:: "+nodeIndexFrom);
System.out.println("nodeIndexTo:: "+nodeIndexTo);
        String nodeFrom = StringUtils.join(", ", nodeList.get(nodeIndexFrom));
        nodeFrom = removeUnwantedCharacter(nodeFrom);
        String nodeTo = StringUtils.join(", ", nodeList.get(nodeIndexTo));
        nodeTo = removeUnwantedCharacter(nodeTo);
//System.out.println("\n \n \n \n \n  joined ---------------------------"+joined+"\n \n \n \n \n");
     
        element.appendChild(this.addXMLNodes(nodeName, nodeFrom, nodeTo, document));       
       //     System.out.println(nodeList.get(nodeIndexFrom));
    }


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);

        StreamResult streamresult = new StreamResult(new File("C:\\XML\\"+methodName+".xml"));
        transformer.transform(source, streamresult);

        
        VisualizationGeneration visualizationGeneration = new VisualizationGeneration(methodName);
    }

    private Element addXMLNodes(String name, String value, String to, Document document) {
        Text attributetext = document.createTextNode(value);
        Element attributeName = document.createElement(name);
        //  attributeName.setAttribute("id", id);

        Attr attrTo = document.createAttribute("to");
        attrTo.setValue(to);
        attributeName.setAttributeNode(attrTo);
        attributeName.appendChild(attributetext);
        return attributeName;
    }
    
    private String removeUnwantedCharacter(String nodeFrom){
        String nodeFrom1 = nodeFrom.substring(3);
        return nodeFrom1.substring(0, nodeFrom1.length()-1);
    }

}
