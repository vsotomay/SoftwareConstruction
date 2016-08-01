package com.code.graph;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
//import com.code.graph.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author T7
 */
/**
 * Visit every method to parse statements from each
 */
public class VisitStatements extends VoidVisitorAdapter {

    int tempCountIf = 0;   ////////////////////////////////////////   count if 
    

    protected List<Statement> LIST_STATEMENTS;

    protected ArrayList<ArrayList<Integer>> nodeList;
    protected ArrayList<ArrayList<Integer>> edgeList;
    
    protected ArrayList<ArrayList<Integer>> loopToConnectLineNumbers;
    protected ArrayList<ArrayList<Integer>> loopConnectionArray;
    protected ArrayList<ArrayList<Integer>> ifStatementConnectionArray;
   
    int j = 0;

    int k = 0;
    protected int nodeIndexI = 0;
    boolean elseStatementExist;
    boolean statementExist;
    protected int edgeIndex = 0;
    protected int loopIndex = 0;

    BlockStmt method_block;

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        nodeIndexI = 0;
        edgeIndex = 0;
        loopIndex = 0;
    
        nodeList = new ArrayList<ArrayList<Integer>>();
        edgeList = new ArrayList<ArrayList<Integer>>();
        loopToConnectLineNumbers = new ArrayList<ArrayList<Integer>>();
        loopConnectionArray = new ArrayList<ArrayList<Integer>>();
        ifStatementConnectionArray = new ArrayList<ArrayList<Integer>>();
        System.out.println("----------------Statements in [" + n.getName() + "] are: ----------------------");

        method_block = new BlockStmt();
        method_block = n.getBody();

        makeNodesFromStatements(method_block);
        testMethod();  

        System.out.println("---------Method [" + n.getName() + "] Ends------------------------");

        
   
    }
    
    @Override
    public void visit(IfStmt n, Object arg) {

        if (n instanceof IfStmt) {
            
            
           IfStatement ifStatement = new IfStatement(n);
           // ifStatements.processStatement();
 
            
 //////////////////////////////////////////////////////////////////////////////////////////////////           
//            boolean statementAdded = addNewStatementNumToNodeList(n.getBeginLine());
//            int nodeIndexHoldingIfStatementLineNum = nodeIndexI;
//            // System.out.println("nodeIndexHoldingIfStatementLineNum:: "+nodeIndexHoldingIfStatementLineNum);
//            nodeIndexI++;
//            //    System.out.println("IF-STMT at line : ("+n.getBeginLine()+") --> "+n.getCondition());
//            
//            int nextNodeIndexToConnectTest =  nodeIndexI;
//            if (n.getThenStmt() != null) {
//
//
//                BlockStmt blockThenStmt = new BlockStmt();
//                blockThenStmt = (BlockStmt) n.getThenStmt();
//                makeNodesFromStatements(blockThenStmt);
//                
//                
//              if (statementAdded) {
//                  
//                  
//                int nextNodeIndex = findNextNode(nextNodeIndexToConnectTest);
//                boolean edgeExist = false;
//                edgeExist = checkIfEdgeExist(nodeIndexHoldingIfStatementLineNum, nextNodeIndex);
//
//                 if(edgeExist == false){
//                addNewEdge(nodeIndexHoldingIfStatementLineNum, nextNodeIndex);
//                 }
//                }
//                nodeIndexI++;
//
//          //       System.out.println("THEN---STMT at line ("+n.getThenStmt().getBeginLine()+") --> "+n.getThenStmt());
//            }
//            if (n.getElseStmt() != null) {
// 
//                boolean found = false;
//                for (int i = 0; i < nodeList.size(); i++) {    //iteration checks if this node already exists to prevent repetation of same node 
//                    if (nodeList.get(i).contains(n.getElseStmt().getBeginLine())) {
//                        found = true;
//                        break;
//                    }
//                }
//                int nodeIndexHoldingElseStatementBeginLine ;
//                if (found == false) {
//               //     System.out.println("ELSE---STMT at line(" + n.getElseStmt().getBeginLine() + ") --> " + n.getElseStmt());
//
//                    nodeIndexI++;
//                    nodeList.add(new ArrayList<Integer>());
//                    nodeList.get(nodeIndexI).add(n.getElseStmt().getBeginLine());
//                    addEdgeFromLoop(n.getElseStmt().getBeginLine());
//                    nodeIndexHoldingElseStatementBeginLine =nodeIndexI;
//                   addNewEdge(nodeIndexHoldingIfStatementLineNum, nodeIndexHoldingElseStatementBeginLine);   // adding new edge from if statement to else
//                    nodeIndexI++;
//                    nodeList.add(new ArrayList<Integer>());
//                    makeNodesFromElseStatements(n.getElseStmt().getChildrenNodes());
//                }
//
//                if (n.getElseStmt().getChildrenNodes() != null) {
//                    makeNodesFromElseStatements(n.getElseStmt().getChildrenNodes());                    
//             //        System.out.println("ELSE---STMT at getChildrenNodes("+n.getElseStmt().getChildrenNodes() +") --> \n \n");
//               //      System.out.println("ELSE---STMT at getParentNode("+n.getElseStmt().getParentNode()+") --> ");
//                }
//            }     
//            if (n.getElseStmt() == null) {              
//            }
        }
//        
        /////////////////////////////////////////////////////////////////
        //flag visited methods using voidvisitoradapter
        super.visit(n, arg);
    }

    @Override
    public void visit(ForStmt n, Object arg){

        if (n instanceof ForStmt) {
     
            int previousNodeIndex = findPreviousNode(nodeIndexI);

            nodeIndexI++;
            boolean statementAdded = addNewStatementNumToNodeList(n.getBeginLine());

            int nodeIndexHoldingForStatementLineNum = nodeIndexI;
           
            boolean edgeExist = checkIfEdgeExist(previousNodeIndex, nodeIndexI);
            boolean previousNodeEmpty = nodeList.get(previousNodeIndex).isEmpty();
            boolean currntNodeEmpty = nodeList.get(nodeIndexI).isEmpty();
            
            if(edgeExist == false && !previousNodeEmpty && !currntNodeEmpty ){
              //System.out.println("Previous Node Index:"+previousNodeIndex+", nodeIndexI: "+nodeIndexI);
            addNewEdge(previousNodeIndex, nodeIndexI); 
            }
            nodeIndexI++;
            int nextNodeIndexToConnectTest =  nodeIndexI;
            nodeList.add(new ArrayList<Integer>());
            BlockStmt blockStmt = new BlockStmt();
            blockStmt = (BlockStmt) n.getBody();
            nodeIndexI++;
            makeNodesFromStatements(blockStmt);
              if (statementAdded) {                 
                  
                int nextNodeIndex = findNextNode(nextNodeIndexToConnectTest);
                edgeExist = false;
                edgeExist = checkIfEdgeExist(nodeIndexHoldingForStatementLineNum, nextNodeIndex);

                if(edgeExist == false){
                addNewEdge(nodeIndexHoldingForStatementLineNum, nextNodeIndex);
                 }
                //// Connect end of for loop to the beginning line of for loop
                int endLine = n.getEndLine();
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                nodeList.get(nodeIndexI).add(endLine);
                  
                int nodeIndexHoldingLoopEndingLineNum = nodeIndexI;
                   
                edgeExist = checkIfEdgeExist(nodeIndexI, nodeIndexHoldingForStatementLineNum);
                if(edgeExist == false){
                 addNewEdge(nodeIndexI, nodeIndexHoldingForStatementLineNum);
                 }  
                
               
                
                /*******************************************/
             //   LIST_STATEMENTS = method_block.getStmts();
                nodeList.add(new ArrayList<Integer>());   /////////////////****************** might be creating excess nodes
                
                int lineNumAfterEndLine = findNextStatement(endLine);
                System.out.println("\n lineNumAfterEndLine"+ lineNumAfterEndLine);
                if(lineNumAfterEndLine != -1){ 
                    addToLoopConnectionArray(nodeIndexHoldingForStatementLineNum, nodeIndexHoldingLoopEndingLineNum, lineNumAfterEndLine);                       
                }
                 
                 nodeIndexI++;
                }            
           
//            System.out.println("FOR-LOOP at line : (" + n.getBeginLine() + ") --> " + n.getCompare());
//            System.out.println("FOR-LOOP BODY " + n.getBody());
//            System.out.println("FOR-LOOP ENDS at line (" + n.getEndLine() + ")");
        }
        super.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, Object arg) {

        if (n instanceof WhileStmt) {
            nodeIndexI++;
            addNewStatementNumToNodeList(n.getBeginLine());
            BlockStmt blockStmt = new BlockStmt();
            blockStmt = (BlockStmt) n.getBody();
            nodeIndexI++;
            makeNodesFromStatements(blockStmt);
            nodeIndexI++;

            System.out.println("WHILE-LOOP at line : (" + n.getBeginLine() + ") --> " + n.getCondition());
            System.out.println("WHILE-LOOP BODY is --> " + n.getBody());
            System.out.println("WHILE-LOOP ENDS at line (" + n.getEndLine() + ")");
            
        }

        super.visit(n, arg);

    }

    @Override
    public void visit(BreakStmt n, Object arg) {

        if (n instanceof BreakStmt) {
            n.setId("0");
            System.out.println("BREAK! at line : (" + n.getBeginLine() + ") --> ID=" + n.getId());

        }
        super.visit(n, arg);
    }


    
    public void makeNodesFromStatements(BlockStmt method_block) {

        LIST_STATEMENTS = method_block.getStmts();

        nodeList.add(new ArrayList<Integer>());   /////////////////****************** might be creating excess nodes

        for (Statement statement : LIST_STATEMENTS) {

            if (statement instanceof IfStmt) {
                visit((IfStmt) statement, null);
            } else if (statement instanceof ForStmt) {

                visit((ForStmt) statement, null);
            } else if (statement instanceof WhileStmt) {

                visit((WhileStmt) statement, null);
            } else if (statement instanceof BreakStmt) {
                visit((BreakStmt) statement, null);

            } else {
           
                addNewStatementNumToNodeList(statement.getBeginLine());
            }
        }

    }
    
    public void makeNodesFromElseStatements(List<Node> LIST_STATEMENTS) {   // accepts argument as Node object
        nodeList.add(new ArrayList<Integer>());
        //     nodeList.add(new ArrayList<Integer>());
        for (Node statement : LIST_STATEMENTS) {

            if (statement instanceof IfStmt) {

                visit((IfStmt) statement, null);
            } else if (statement instanceof ForStmt) {

                visit((ForStmt) statement, null);
            } else if (statement instanceof WhileStmt) {
                visit((WhileStmt) statement, null);
            } else if (statement instanceof BreakStmt) {
                visit((BreakStmt) statement, null);
            } else {
                addNewElseStatementNumToNodeList(statement.getBeginLine());
            }
        } 
        LIST_STATEMENTS = null;
        nodeIndexI++;
      

    }
 
    public boolean addNewStatementNumToNodeList(int statementNum) {
        statementExist = false;
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).contains(statementNum)) {
                statementExist = true;
                break;
            }
        }
        if (statementExist == false) {
            nodeList.add(new ArrayList<Integer>());
            nodeList.get(nodeIndexI).add(statementNum);
            
            addEdgeFromLoop(statementNum);
            return true;
        }
        
        return false;

    }
    
    public void addNewElseStatementNumToNodeList(int lineNum) {
        elseStatementExist = false;
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).contains(lineNum)) {
                elseStatementExist = true;
                break;
            }
        }
        if (elseStatementExist == false) {
            nodeList.add(new ArrayList<Integer>());
            nodeList.get(nodeIndexI).add(lineNum);
            addEdgeFromLoop(lineNum);
        }
    }
    
    
    public void addEdgeFromLoop(int statementBeginLine){
       if(!loopConnectionArray.isEmpty()){                           /////   checks if any loop starting node left to connect to the second node.
            for (int i = 0; i < loopConnectionArray.size(); i++) {
                if(loopConnectionArray.get(i).get(2) == statementBeginLine){                
             //      System.out.println("\n \n Statement begin line for loop connection:: "+statementBeginLine+"\n \n \n" );
                       
                    int nodeIndexHoldingLoopStatementBeginLine =  loopConnectionArray.get(i).get(0);
                    int nodeIndexHoldingLoopStatementEndLine = loopConnectionArray.get(i).get(1);
                    
                    int nodeIndexHoldingOutOfLoopLineNum = nodeIndexI;
                    System.out.println("\n \n nodeIndexI  : "+nodeIndexI+"\n \n \n" );
                    addNewEdge( nodeIndexHoldingLoopStatementBeginLine,  nodeIndexHoldingOutOfLoopLineNum);                                                    
                //    addNewEdge( nodeIndexHoldingLoopStatementEndLine,  nodeIndexHoldingOutOfLoopLineNum);
                    
                    int previousNodeBeforeLoopEndLineNodeTest = nodeIndexHoldingLoopStatementEndLine-1;
                    int  previousNodeBeforeLoopEndLineNode = findPreviousNode(previousNodeBeforeLoopEndLineNodeTest);
                    addNewEdge(previousNodeBeforeLoopEndLineNode, nodeIndexHoldingLoopStatementEndLine);
                }
            }             
        }
    }
    
    public void addNewEdge(int nodeIndexFrom, int nodeIndexTo){
        
        boolean edgeExist = checkIfEdgeExist(nodeIndexFrom, nodeIndexTo);
        
        if(!edgeExist){
            edgeList.add(new ArrayList<Integer>());
            edgeList.get(edgeIndex).add(nodeIndexFrom);
            edgeList.get(edgeIndex).add(nodeIndexTo);
            edgeIndex++;
        }
    }
    
    public int findPreviousNode(int previousNodeIndexTest){
          //  int  previousNodeIndexTest = nodeIndexI;
            while(nodeList.get(previousNodeIndexTest).isEmpty()){
           previousNodeIndexTest = previousNodeIndexTest-1 ;
            }
              System.out.println(" previousNodeIndex: "+previousNodeIndexTest);
            return  previousNodeIndexTest; 
    }

    public int findNextNode(int nextNodeIndexToConnectTest){
            int  nextNodeIndexTest = nextNodeIndexToConnectTest;
            while(nodeList.get(nextNodeIndexTest).isEmpty()){
               nextNodeIndexTest = nextNodeIndexTest+1 ;
            }
            return  nextNodeIndexTest; 
    }
    
    
    public boolean checkIfEdgeExist(int nodeIndexFrom, int nodeIndexTo){
        boolean edgeExist = false;
        for (int i = 0; i < edgeList.size(); i++) {
            if(edgeList.get(i).get(0) == nodeIndexFrom  && edgeList.get(i).get(1) == nodeIndexTo ){
                edgeExist = true;
                break;
            }
        }
        return edgeExist;
    }
    
    public int findNextStatement(int lineNum){
        List<Statement> allStatementsInMethod;
        allStatementsInMethod = method_block.getStmts();
        for (Statement statement : allStatementsInMethod) {
            int statementLineNo = statement.getBeginLine();
            
           
            if(statementLineNo > lineNum){ 
      //           System.out.println("\n \n statementLineNo statementLineNo statementLineNo:"+statementLineNo);
                return statementLineNo;
            }
        }
        
        return -1;
    }
  
    
    public int findPreviousStatement(int lineNum){
        int previousStatementLineNo = -1;
        for (Statement statement : LIST_STATEMENTS) {
            int statementLineNo = statement.getBeginLine();
            if(statementLineNo < lineNum && (previousStatementLineNo == -1 || statementLineNo>previousStatementLineNo )){ 
                 previousStatementLineNo = statementLineNo;
            }
        }
        
        return previousStatementLineNo;
    }
    
    public void addToLoopConnectionArray(int nodeIndexHoldingLoopBeginningLineNum, int nodeIndexHoldingLoopEndingLineNum, int outOfLoopLineNum){
          loopConnectionArray.add(new ArrayList<Integer>());
          loopConnectionArray.get(loopIndex).add(nodeIndexHoldingLoopBeginningLineNum);  
          loopConnectionArray.get(loopIndex).add(nodeIndexHoldingLoopEndingLineNum);
          loopConnectionArray.get(loopIndex).add(outOfLoopLineNum);
          loopIndex++;
  
         }
    
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
      public void testMethod(){    ///////// temporary Test Method to display test data  ******************************************************
                  j = 0;
          
        /////////************** Testing:::: Iterate through node list and display for testing
        k = 0; 
       ///////// Method displays test data  ************************************************************************
        for (j = 0; j < nodeList.size(); j++) {

            System.out.print("Node Index "+j+": ");
            for (k = 0; k < ((ArrayList) nodeList.get(j)).size(); k++) {

                System.out.print(((ArrayList) nodeList.get(j)).get(k) + "  ");
            }
            System.out.println();
        }
        /////////************** Testing:::: Iterate through node list and display for testing: ends 
        
        
       /////////************** Testing:::: Iterate through edge list, and display for testing
        k = 0;

        for (j = 0; j < edgeList.size(); j++) {

            System.out.print("edge: ");
            for (k = 0; k < ((ArrayList) edgeList.get(j)).size(); k++) {

                System.out.print(((ArrayList) edgeList.get(j)).get(k) + "  ");
            }
            System.out.println();
        }
        /////////************** Testing:::: Iterate through edge list, and display for testing: ends 
        
        
        for (j = 0; j < loopConnectionArray.size(); j++) {

            System.out.print("loopConnectionArray: ");
            for (k = 0; k < ((ArrayList) loopConnectionArray.get(j)).size(); k++) {
          //      if(j==1)
                System.out.print(((ArrayList) loopConnectionArray.get(j)).get(k) + "  ");
               // loopConnectionArray.get(j).clear();
            }
            System.out.println();
            
        }
                   
                   
                   /////////
                   
     //   (ArrayList) loopToConnectLineNumbers.get(1).set(0,null);
                   for (j = 0; j < loopToConnectLineNumbers.size(); j++) {

            System.out.print("loopToConnectLineNumbers : "+j+": ");
            for (k = 0; k < ((ArrayList) loopToConnectLineNumbers.get(j)).size(); k++) {

                System.out.print(( loopToConnectLineNumbers.get(j)).get(k) + "  ");
                loopToConnectLineNumbers.get(j).set(k, 0);
            }
            System.out.println();
            
        }
                   
                     /////////************** Testing:::: Iterate through loopToConnectLineNumbers list, and display for testing: ends 
      }
      
        }    
//     public void checkIfLoopCanConnect(int currentLineNum){
//         int nextLineNum = -1;
//         int nextNodeIndex = -1;
//         int loopToConnectIndexNum = -1;
//         int i;
//         boolean outerMostLoopBreak = false;
//           for (i = 0; i < loopToConnectLineNumbers.size(); i++) {
//               int loopEndLineNum = loopToConnectLineNumbers.get(i).get(2);
//           
//                for (j = 0; j < nodeList.size(); j++) {
//                    if(i != loopIndexToIgonore){
//                    for (k = 0; k < ((ArrayList) nodeList.get(j)).size(); k++) {
//                        int lineNo = nodeList.get(j).get(k) ;
//                        if(lineNo > loopEndLineNum  && (nextLineNum == -1 || lineNo < nextLineNum)){
//                            nextLineNum = lineNo;
//                            nextNodeIndex = j;
//                            outerMostLoopBreak = true;
//                        }
//                    }    
//                }
//                }
//                
//                System.out.println("the i: "+i);
//             loopIndexToIgonore = i;
//              
//              if(outerMostLoopBreak) {
//                  System.out.println("the i2: "+i);
//                  break;
//              }  
//           }  
//           
//          
//           
//           if(nextLineNum != -1 ){
//              int  nodeIndexHoldingLoopBeginningLineNum = loopToConnectLineNumbers.get(i).get(0);
//               
//               addNewEdge(nodeIndexHoldingLoopBeginningLineNum, nextNodeIndex);
//
//           }
//     } 
     
    
         
        //      public void addLoopToConnectLineNo(int nodeIndexHoldingLoopBeginningLineNum, int nodeIndexHoldingLoopEndLine, int loopEndLineNum){
        //          loopToConnectLineNumbers.add(new ArrayList<Integer>());
        //          loopToConnectLineNumbers.get(loopIndex).add(nodeIndexHoldingLoopBeginningLineNum);  
        //          loopToConnectLineNumbers.get(loopIndex).add(nodeIndexHoldingLoopEndLine); 
        //          loopToConnectLineNumbers.get(loopIndex).add(loopEndLineNum); 
        //        //  loopIndex++;
        //         }     
  
