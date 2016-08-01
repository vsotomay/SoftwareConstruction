/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.code.graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Pluto
 */
public class VisualizationGeneration {
private File graphvizFile; 
private File xmlFile; 
DocumentBuilder documentBuilder;
   public VisualizationGeneration() throws ParserConfigurationException, TransformerException {
        try {
            File graphvizFile = new File("C:\\graphViz\\graph1.gv");
            if (!graphvizFile.exists()) {
                graphvizFile.createNewFile();

            }
            readXML();
        } catch (IOException io) {

        }
    }

   private void readXML() throws ParserConfigurationException, TransformerException, IOException {

        xmlFile = new File("C:\\XML\\data.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();

        graphvizFile = new File("C:\\graphViz\\graph1.gv");
        
        generateVizCode();
        //    File graphvizFile = new File("C:\\graphViz\\graph1.gv");
//        try {
//            Document document = documentBuilder.parse(xmlFile);
//            document.getDocumentElement().normalize();
//            NodeList list = document.getElementsByTagName("node");
//            FileWriter fw = new FileWriter(graphvizFile);
//            fw.write("digraph{");
//            for (int i = 0; i < list.getLength(); i++) {
//                org.w3c.dom.Node node = list.item(i);
//
//                Element element = (Element) node;
//
//                fw.write("\"" + element.getTextContent() + "\"->\"" + list.item(i).getAttributes().getNamedItem("to").getNodeValue() + "\"; \n");
//                System.out.println("First Name : " + element.getTextContent());
//                System.out.println("******" + list.item(i).getAttributes().getNamedItem("to").getNodeValue());
//
//            }
//            fw.write("}");
//            fw.close();
//        } catch (Exception e) {
//
//        }
    }
       
   public void  generateVizCode(){
        try {
                    Document document = documentBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();
            NodeList list = document.getElementsByTagName("node");
            FileWriter fw = new FileWriter(graphvizFile);
            fw.write("digraph{");
            for (int i = 0; i < list.getLength(); i++) {
                org.w3c.dom.Node node = list.item(i);

                Element element = (Element) node;

                fw.write("\"" + element.getTextContent() + "\"->\"" + list.item(i).getAttributes().getNamedItem("to").getNodeValue() + "\"; \n");
                System.out.println("First Name : " + element.getTextContent());
                System.out.println("******" + list.item(i).getAttributes().getNamedItem("to").getNodeValue());

            }
            fw.write("}");
            fw.close();
            } catch (Exception e) {

        }
    }
}
