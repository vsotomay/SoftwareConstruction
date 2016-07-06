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
    
    static List<Statement> LIST_STATEMENTS;
    //count statments from the .java input code
    static int statment_Number =1;
    
    
    
    public static void main( String[] args ) throws IOException, ParseException {

        System.out.println("Running--> Parse .java code to identify type of statments: sequencial, iterative, conditional");

       
        // .java input code
         FileInputStream in = new FileInputStream("/Users/jmcorral2/Desktop/QuickSort.java");
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

//        System.out.println("ey there "+LIST_STATEMENTS.toString());
    }
    

    
    /**
     * Visit every method to parse statements from each
     */
    private static class VisitStatements extends VoidVisitorAdapter {
       

        
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            

//            System.out.println(n.getBody().getChildrenNodes());//Gets contents of method.


//             BlockStmt block = new BlockStmt();

            //Get every method's name from the parsed .java file
            System.out.println("----------------Statements in [" + n.getName() + "] are: ------------------------------------------------------------------");
          
  
            //every method declaration contains finite number of statments (lines of code)
            LIST_STATEMENTS = n.getBody().getStmts();
          
            
            for(Statement statement: LIST_STATEMENTS){

                System.out.println("stmtNum : {{"+ statment_Number + "}} ");
                
                if(statement instanceof IfStmt){
                    
                    visit((IfStmt) statement,null);
                }
                else if(statement instanceof ForStmt){
                    
                    visit((ForStmt) statement,null);
       
                    
                }
                else if(statement instanceof WhileStmt){
                    
                    visit((WhileStmt) statement,null);
                }
                else if(statement instanceof BreakStmt){
                    
                    visit((BreakStmt) statement,null);
                    
                }
                else{
                     System.out.println("Line "+statement.getBeginLine()+" : "+statement);
                    
                }
              
               
                
                statment_Number++;
            }
            System.out.println("---------Method [" + n.getName() + "] Ends------------------------------------------------------------------------------------");
   
        }
          
        
        @Override
        public void visit(IfStmt n, Object arg){
           
            if(n instanceof IfStmt){
                System.out.println("IF-STMT at line : ("+n.getBeginLine()+") --> "+n.getCondition());
                if(n.getThenStmt() != null){
                     System.out.println("THEN---STMT at line ("+n.getThenStmt().getBeginLine()+") --> "+n.getThenStmt());
                }
                if(n.getElseStmt() != null){
                    System.out.println("ELSE---STMT at line("+n.getElseStmt().getBeginLine()+") --> "+n.getElseStmt());
                }
                System.out.println("IF-STMT ENDS at line ("+n.getEndLine()+")");
            }
            
            //flag visited methods using voidvisitoradapter
            super.visit(n, arg); 
            
        }
        
        @Override
        public void visit(ForStmt n, Object arg){
            
             if(n instanceof ForStmt){
                 
                System.out.println("FOR-LOOP at line : ("+n.getBeginLine()+") --> "+n.getCompare());
                System.out.println("FOR-LOOP BODY "+n.getBody());
                System.out.println("FOR-LOOP ENDS at line ("+n.getEndLine()+")");
             }
               super.visit(n, arg);
            
        }
        
         @Override
        public void visit(WhileStmt n, Object arg){
            
            if(n instanceof WhileStmt){
            System.out.println("WHILE-LOOP at line : ("+n.getBeginLine()+") --> "+n.getCondition());
            System.out.println("WHILE-LOOP BODY is --> "+n.getBody());
            System.out.println("WHILE-LOOP ENDS at line ("+n.getEndLine()+")");
            }
            
            super.visit(n, arg);
            
        }
        
         @Override
        public void visit(BreakStmt n, Object arg){
            
            if(n instanceof BreakStmt){
                n.setId("0");
            System.out.println("BREAK! at line : ("+n.getBeginLine()+") --> ID="+n.getId());
           
            }
         
               super.visit(n, arg);
        }
    
    }
    
}
