package com.code.graph;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import java.io.*;

/**
 * @author T7
 *
 */
public class CodeGraph
{
    public static void main( String[] args ) throws IOException, ParseException {

        System.out.println("Running--> Parse .java code to identify type of statments: sequencial, iterative, conditional");

       
        
         FileInputStream in = new FileInputStream("/Users/juliuz/Desktop/QuickSort.java");
        CompilationUnit cu;
        try {
            // parse .java input code
            cu = JavaParser.parse(in);
            
        } finally {
            in.close();
        }

        System.out.println("Visiting Statments");
        System.out.println();
        
        //Use grammar library and parse .java input code into a list of statements
        new VisitStatements().visit(cu, null);
        
        
        //use list of statements parsed from the grammar and build a CFG with its nodes and edges
        //BuildCFG will output the nodes, directed edges and labels using the XML library
        //BuildCFG graphToBuild = new BuildCFG().construct();
        
        //save the XML file that contains the nodes,directed edges, labels in the project's folder
        //External library graphzip-repl will be used to generate the graph

    }
    
}
