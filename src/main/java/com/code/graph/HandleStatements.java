/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.code.graph;


import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author
 */
public class HandleStatements {
    
    

    HandleStatements() {
        
    }
    
    public void printOutStmt(LinkedList<Statement> LIST_STATEMENTS) 
    {
        
        LinkedList<Statement> handleList =(LinkedList<Statement>) LIST_STATEMENTS;
        int tempCounter=1;
        for(Statement statement_item : handleList)
        {
            System.out.println("{"+tempCounter+"}");
            System.out.println("Line "+statement_item.getBeginLine()+" : "+statement_item);
            
            tempCounter++;
        }
        
 
    }
    

    
    
    


   public void innerStmts(LinkedList<Statement> LIST_STATEMENTS) {
       
        for(Statement current_stmt : LIST_STATEMENTS)
        {
           
           
            if(current_stmt instanceof ForStmt)
            {
                
                System.out.println("NODE for:: "+ ((ForStmt)current_stmt).getCompare());
                 Statement checkStmt = ((ForStmt) current_stmt).getBody(); // conditional and iterative con be a block or expression (expression if dont have curly brackets)
                 

                
                if(checkStmt instanceof ExpressionStmt){//In case if does not have brackets
                forHandler((ExpressionStmt) checkStmt);
                }

                else if (checkStmt instanceof ForStmt){
                    BlockStmt newNestedBlock = (BlockStmt) ((ForStmt) checkStmt).getBody();
                    forHandler(newNestedBlock);
                }

                else {
                    BlockStmt newBlock = (BlockStmt) ((ForStmt) current_stmt).getBody();
                    forHandler(newBlock);
                }

                
                

                
         
                
            }
            
            
            
            if(current_stmt instanceof WhileStmt)
            {
             System.out.println("Create NODE (iterWhile): "+ ((WhileStmt)current_stmt).getCondition());
                
                
            }
            
            
            if(current_stmt instanceof IfStmt)
            {
             System.out.println("Create NODE (cond): "+ ((IfStmt)current_stmt).getCondition());   
            }
            
           
            
            
        }

    }
        
    private void forHandler(ExpressionStmt expressionFor)
    {
        System.out.println("ForLoop as a expresssion w/o curly");
        System.out.println(expressionFor.getExpression());
        System.out.println();
    }

    private void forHandler(BlockStmt forBlock){
//        System.out.println("ForLoop as a block w/curly");
        List<Statement> statements = forBlock.getStmts();
        System.out.println(forBlock.getBeginLine());
        
        
        
        for(Statement innerStmts : statements)
        {
            if(innerStmts instanceof ForStmt)
            {
              System.out.println("found inner foorlop-->  "+ innerStmts);  
              BlockStmt newNestedBlock = (BlockStmt) ((ForStmt) innerStmts).getBody();
                   System.out.println("found statements "+newNestedBlock.getStmts());
                   innerStmts((LinkedList<Statement>) statements);
            }
        }
        
//        printOutStmt((LinkedList<Statement>) statements);
        System.out.println();

        
    }
        
    
    
  
    
    
        
//    
//     public List<Statement> sendList(List<Statement> LIST_STATEMENTS) {
//        
//        handleList=LIST_STATEMENTS;
//        
//        //          for(Statement statement_item : buu){
////            System.out.println("Line "+statement_item.getBeginLine()+" : "+statement_item);
////        }
//        
//        
//        return handleList;
//    }



 

}
