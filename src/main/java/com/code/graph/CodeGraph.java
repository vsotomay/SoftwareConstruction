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
        System.out.println();
        new MethodVisitorTest().visit(cu, null);
        

    }

   
    private static class MethodGenericTest extends GenericVisitorAdapter{
        
      //whuuuut? genericvisitoradapat might store blocks in a list by defaul... 'might'"
       
    }
   
    
    /**
     * visiting methods using VoidVisitorAdapter parent class.
     */
    private static class MethodVisitorTest extends VoidVisitorAdapter {

        
//        public static void getAll_Methods(MethodDeclaration n, Object arg){
//             BlockStmt list_of_methods = new BlockStmt();
//             
//        }
        
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            
//            System.out.println(n.getName());//Gets method names.
//            System.out.println(n.getBody().getChildrenNodes());//Gets contents of method.

            List<Statement> statements;
             BlockStmt block = new BlockStmt();
            
            
//             
//             List<IfStmt> if_statements_list;
//            List<ForStmt> for_statements_list;
//            IfStmt instance_IF = new IfStmt();
//            ForStmt instance_FOR = new ForStmt();

            //Test to see if we can get contents of method and set them in a block.
            System.out.println("----------------Statements in [" + n.getName() + "] are: ------------------------------------------------------------------");
          
        
            
            
            block = n.getBody();
            
            
            statements = block.getStmts();
            
            
            int x =1;
            for(Statement statement: statements){

                System.out.println("{"+ x + "} ");
                
//                if(statement instanceof IfStmt)
//                {
//                    System.out.println("This is an if_stmt!!->  "+statement.getChildrenNodes());
//                }
                System.out.println(statement.getChildrenNodes());
                
                x++;
            }
            System.out.println();
            
           
             
            super.visit(n, arg);

        }
        

        
        @Override
        public void visit(IfStmt n, Object org){
           
            if(n instanceof IfStmt){
                System.out.println("IF-STMT at line ("+n.getBeginLine()+") --> "+n.getCondition());
                if(n.getThenStmt() != null){
                     System.out.println("THEN---STMT at line ("+n.getThenStmt().getBeginLine()+") --> "+n.getThenStmt());
                }
                if(n.getElseStmt() != null){
                    System.out.println("ELSE---STMT at line("+n.getElseStmt().getBeginLine()+") --> "+n.getElseStmt());
                }
                System.out.println("IF-STMT ENDS at line ("+n.getEndLine()+")");
            }
            super.visit(n,org);
            
        }
        
        @Override
        public void visit(ForStmt n, Object org){
            
             if(n instanceof ForStmt){
                 
                System.out.println("FOR-LOOP is at line--> ("+n.getBeginLine()+") --> "+n.getCompare());
                System.out.println("FOR-LOOP BODY "+n.getBody());
                System.out.println("FOR-LOOP ENDS at line ("+n.getEndLine()+")");
             }
            
            super.visit(n,org);
            
        }
        
         @Override
        public void visit(WhileStmt n, Object org){
            
            if(n instanceof WhileStmt){
            System.out.println("WHILE-LOOP is --> ("+n.getBeginLine()+") --> "+n.getCondition());
            System.out.println("WHILE-LOOP BODY is --> "+n.getBody());
            System.out.println("WHILE-LOOP ENDS at line ("+n.getEndLine()+")");
            }
            
            super.visit(n,org);
            
        }
        
         @Override
        public void visit(BreakStmt n, Object org){
            
            if(n instanceof BreakStmt){
            System.out.println("BREAK! is --> ("+n.getBeginLine()+") --> ID="+n.getId());
           
            }
            
            super.visit(n,org);
            
        }
        
        
        
        
             //ignore this funciton for now, it might be usfule to read every single word in the code
        public static void process(Node node){
            System.out.println("Trigger process funtion!!!!!");
            //this loop get a node, (a node is also know as a statment)
            for(Node child : node.getChildrenNodes()){
                System.out.println("My child is-----> "+child);
                process(child);
            }
            
            System.out.println("BEGIN:  "+node.getBeginLine());
             System.out.println("END:   "+node.getEndLine());
              System.out.println("PARENT NODE:   "+node.getParentNode());
              System.out.println("-----");
        }

        
    }

}
