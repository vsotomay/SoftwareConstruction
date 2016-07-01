package com.code.graph;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import java.io.*;
import java.util.*;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sun.glass.ui.EventLoop;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author
 *
 */
public class CodeGraph
{
    public static void main( String[] args ) throws IOException, ParseException {

        String status = "Status: Testing method visitor.";
        System.out.println(status);

        // creates an input stream for the file to be parsed
      
         FileInputStream in = new FileInputStream("/Users/jmcorral2/Desktop/FractionalToBinary.java");
        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
            
        } finally {
            in.close();
        }

        /* prints the resulting compilation unit to default system output
        // System.out.println("Printing out contents of class");
        // System.out.println(cu.toString()); */

        System.out.println("Visiting Methods in FractionalToBinary.java");
        new MethodVisitorTest().visit(cu, null);

    }

  
   
    
    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodVisitorTest extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this
            // CompilationUnit, including inner class methods
//            System.out.println(n.getName());//Gets method names.
//            System.out.println(n.getBody().getChildrenNodes());//Gets contents of method.

            List<Statement> statements;
            List<IfStmt> current_ifs;
            List<ForStmt> current_fors;
            
           

            //Test to see if we can get contents of method and set them in a block.
            System.out.println("----------------Statements are: -------------------");
            
            BlockStmt block = new BlockStmt();
            IfStmt conditional_IF = new IfStmt();
            ForStmt iterative_FOR = new ForStmt();
            
            block = n.getBody();
            
            
            statements = block.getStmts();
            
            int x =1;
            for(Statement statement: statements){
                System.out.println("("+ x + ") ");
                System.out.println(statement);
                
                x++;
            }
            System.out.println();
             System.out.println("------------------------------------------------");
            //Ok so, in Java blocks are executed as a single statement. That's why if you run this, you can see the statement separated by commas and a block is seen as a statement.
            //What's a block in java? https://docs.oracle.com/javase/tutorial/java/nutsandbolts/expressions.html

            super.visit(n, arg);//Makes a note of the methods visited by marking them through VoidVisitorAdapter


        }
        
        @Override
        public void visit(IfStmt n, Object org){
            
            
            System.out.println("CURRENT if CONDITIONS found--> "+n.getCondition());
//            System.out.println("ALL IF data "+n.getData());
            super.visit(n,org);
            
        }
        
        @Override
        public void visit(ForStmt n, Object org){
            
            
            System.out.println("CONDITION in the FOR--> "+n.getCompare());
            System.out.println("BODY inside each FORLOOP"+n.getBody());
            super.visit(n,org);
            
        }
        
         @Override
        public void visit(WhileStmt n, Object org){
            
            
            System.out.println("CONDITION in the WHILE"+n.getCondition());
            System.out.println("BODY inside each WHILELOOP "+n.getBody());
            super.visit(n,org);
            
        }
        
    }
    
    
   

}
