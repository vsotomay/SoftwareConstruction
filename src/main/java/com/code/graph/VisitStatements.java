package com.code.graph;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.List;

/**
 *
 * @author T7
 */
/**
     * Visit every method to parse statements from each
     */
    public class VisitStatements extends VoidVisitorAdapter {
        
        private List<Statement> LIST_STATEMENTS;
        private int statment_Number =1;

    
        @Override
        public void visit(MethodDeclaration n, Object arg) {

            //Get every method's name from the parsed .java file
            System.out.println("---------Statements in [" + n.getName() + "] are: ----------------------");

            //every method declaration contains finite number of statments (lines of code)
            BlockStmt method_block=new BlockStmt();
            method_block=n.getBody();
            LIST_STATEMENTS = method_block.getStmts();
    
            for(Statement statement: LIST_STATEMENTS){

                System.out.println("stmt : {{"+ statment_Number + "}} ");
                
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
            System.out.println("---------Method [" + n.getName() + "] Ends------------------------");
   
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
            
             
            //flag
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
