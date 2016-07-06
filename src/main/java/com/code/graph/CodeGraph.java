package com.code.graph;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import java.io.*;
import java.util.*;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sun.glass.ui.EventLoop;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author Julio M. Corral
 *
 */
public class CodeGraph
{
    
   
    
    
    
    public static void main( String[] args ) throws IOException, ParseException {

        System.out.println("Running--> Parse .java code to identify type of statments: sequencial, iterative, conditional");

       
        // .java input code
         FileInputStream in = new FileInputStream("/Users/juliuz/Desktop/FractionalToBinary.java");
        CompilationUnit cu;
        try {
            // parse .java input code
            cu = JavaParser.parse(in);
            
        } finally {
            in.close();
        }

   
        System.out.println("Visiting Statments");
        System.out.println();

        new VisitStatements().visit(cu, null);
//       System.out.println("ey there "+LIST_STATEMENTS.toString());
 
    }
    
}
