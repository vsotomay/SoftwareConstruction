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
    private List<Statement> LIST_IF_STATEMENTS;
    private int statment_Number = 1;
    private ArrayList<ArrayList<Integer>> nodeList;
    int j = 0;

    int k = 0;
    private int nodeIndexI = 0;
    BlockStmt method_block;

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        //  nodeList.clear();
        nodeList = new ArrayList<ArrayList<Integer>>();
        //Get every method's name from the parsed .java file
        System.out.println("----------------Statements in [" + n.getName() + "] are: ----------------------");

        //every method declaration contains finite number of statments (lines of code)
        method_block = new BlockStmt();
        method_block = n.getBody();

        makeNodesFromStatements(method_block);
        j = 0;

            /////////************** Testing:::: Iterate through node list and display for testing
        k = 0;
        // display contents of matrix
        for (j = 0; j < nodeList.size(); j++) {
            //     System.out.print("Node Size: "+nodeList.size()+"\n");
            System.out.print("Node: ");
            for (k = 0; k < ((ArrayList) nodeList.get(j)).size(); k++) {

                System.out.print(((ArrayList) nodeList.get(j)).get(k) + "  ");
            }
            System.out.println();
        }

        System.out.println("---------Method [" + n.getName() + "] Ends------------------------");
        /////////************** Testing:::: Iterate through node list and display for testing: ends 
    }

    public void makeNodesFromStatements(BlockStmt method_block) {

        LIST_STATEMENTS = method_block.getStmts();

        // i = 0;
        nodeList.add(new ArrayList<Integer>());
        //     nodeList.add(new ArrayList<Integer>());
        for (Statement statement : LIST_STATEMENTS) {

             //   System.out.println("stmtNum : {{"+ statment_Number + "}} ");
            if (statement instanceof IfStmt) {
                addNewStatementNumToNodeList(statement);
                nodeIndexI++;

                visit((IfStmt) statement, null);
            } else if (statement instanceof ForStmt) {
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                nodeList.get(nodeIndexI).add(statement.getBeginLine());
                visit((ForStmt) statement, null);
            } else if (statement instanceof WhileStmt) {
                nodeIndexI++;

                nodeList.add(new ArrayList<Integer>());
                nodeList.get(nodeIndexI).add(statement.getBeginLine());
                visit((WhileStmt) statement, null);
            } else if (statement instanceof BreakStmt) {

                visit((BreakStmt) statement, null);

            } else {
                addNewStatementNumToNodeList(statement);
//                      nodeList.add(new ArrayList<Integer>());   
//                     nodeList.get(i).add(statement.getBeginLine());
                //        System.out.println("Line "+statement.getBeginLine()+" : "+statement);

            }

            //    statment_Number++;
        }
        //   i = 0;

    }

    public void makeNodesFromIfStatements(BlockStmt method_block) {
        //    LIST_STATEMENTS.clear();
        LIST_IF_STATEMENTS = method_block.getStmts();
        // i = 0;
        nodeList.add(new ArrayList<Integer>());
        //     nodeList.add(new ArrayList<Integer>());
        for (Statement statement : LIST_IF_STATEMENTS) {

             //   System.out.println("stmtNum : {{"+ statment_Number + "}} ");
            if (statement instanceof IfStmt) {
                addNewStatementNumToNodeList(statement);
                nodeIndexI++;
                visit((IfStmt) statement, null);
            } else if (statement instanceof ForStmt) {
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                nodeList.get(nodeIndexI).add(statement.getBeginLine());
                visit((ForStmt) statement, null);
            } else if (statement instanceof WhileStmt) {
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                nodeList.get(nodeIndexI).add(statement.getBeginLine());
               // nodeIndexI++;
                visit((WhileStmt) statement, null);
            } else if (statement instanceof BreakStmt) {
                visit((BreakStmt) statement, null);
            } else {
                addNewStatementNumToNodeList(statement);
            }
            //    statment_Number++;
        }
  //      LIST_STATEMENTS = null;
        nodeIndexI++;
    }

    
    @Override
    public void visit(IfStmt n, Object arg) {

        if (n instanceof IfStmt) {
            //    System.out.println("IF-STMT at line : ("+n.getBeginLine()+") --> "+n.getCondition());
            if (n.getThenStmt() != null) {
                BlockStmt blockThenStmt = new BlockStmt();
                blockThenStmt = (BlockStmt) n.getThenStmt();
                makeNodesFromIfStatements(blockThenStmt);

                //   System.out.println("THEN---STMT at line ("+n.getThenStmt().getBeginLine()+") --> "+n.getThenStmt());
            }
            if (n.getElseStmt() != null) {
                boolean found = false;
                for (int i = 0; i < nodeList.size(); i++) {
                    if (nodeList.get(i).contains(n.getElseStmt().getBeginLine())) {
                        found = true;
                       
                    
                   break;
                }
                }
                if (found == false) {
                    System.out.println("ELSE---STMT at line("+n.getElseStmt().getBeginLine()+") --> "+n.getElseStmt());
                    nodeList.add(new ArrayList<Integer>());
                    nodeIndexI++;
                    nodeList.get(nodeIndexI).add(n.getElseStmt().getBeginLine());
                    nodeIndexI++;
                    nodeList.add(new ArrayList<Integer>());
                    makeNodesFromElseStatements(n.getElseStmt().getChildrenNodes());
                }
                
                
                if (n.getElseStmt().getChildrenNodes() != null){
                     makeNodesFromElseStatements(n.getElseStmt().getChildrenNodes());
                        //  System.out.println("ELSE---STMT at getChildrenNodes("+n.getElseStmt().getChildrenNodes() +") --> ");
                }
               //      BlockStmt blockTElseStmt = (BlockStmt) n.getElseStmt();
                //     makeNodesFromIfStatements(blockTElseStmt);     
                   
            }

        }

        //flag visited methods using voidvisitoradapter
        super.visit(n, arg);

    }

    @Override
    public void visit(ForStmt n, Object arg) {

        if (n instanceof ForStmt) {
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                BlockStmt blockStmt = new BlockStmt();
                blockStmt = (BlockStmt) n.getBody();
                nodeIndexI++;
                makeNodesFromIfStatements(blockStmt);
            System.out.println("FOR-LOOP at line : (" + n.getBeginLine() + ") --> " + n.getCompare());
            System.out.println("FOR-LOOP BODY " + n.getBody());
            System.out.println("FOR-LOOP ENDS at line (" + n.getEndLine() + ")");
        }
        super.visit(n, arg);

    }

    @Override
    public void visit(WhileStmt n, Object arg) {

        if (n instanceof WhileStmt) {
            
                BlockStmt blockStmt = new BlockStmt();
                blockStmt = (BlockStmt) n.getBody();
                nodeIndexI++;
                makeNodesFromIfStatements(blockStmt);
                
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

    public void addNewStatementNumToNodeList(Statement statement) {
        boolean found = false;
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).contains(statement.getBeginLine())) {
                found = true;
                break;
            }
        }
        if (found == false) {
            nodeList.add(new ArrayList<Integer>());
            nodeList.get(nodeIndexI).add(statement.getBeginLine());

        }

    }
    
        public void makeNodesFromElseStatements(List<Node> LIST_STATEMENTS) {
        //    LIST_STATEMENTS.clear();
        // i = 0;
        nodeList.add(new ArrayList<Integer>());
        //     nodeList.add(new ArrayList<Integer>());
        for (Node statement : LIST_STATEMENTS) {
          //  Statement statement = (Statement)node; 
             //   System.out.println("stmtNum : {{"+ statment_Number + "}} ");
            if (statement instanceof IfStmt) {
                addNewElseStatementNumToNodeList(statement);
                nodeIndexI++;
                visit((IfStmt) statement, null);
            } else if (statement instanceof ForStmt) {
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                nodeList.get(nodeIndexI).add(statement.getBeginLine());
                visit((ForStmt) statement, null);
            } else if (statement instanceof WhileStmt) {
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                nodeList.get(nodeIndexI).add(statement.getBeginLine());
           //     nodeIndexI++;
                
                visit((WhileStmt) statement, null);
            } else if (statement instanceof BreakStmt) {

                visit((BreakStmt) statement, null);

            } else {
                addNewElseStatementNumToNodeList(statement);

            }
            //    statment_Number++;
        }
        LIST_STATEMENTS = null;
        nodeIndexI++;

        }
        
        public void addNewElseStatementNumToNodeList(Node statement) {
        boolean found = false;
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).contains(statement.getBeginLine())) {
                found = true;
                break;
            }
        }
        if (found == false) {
            nodeList.add(new ArrayList<Integer>());
            nodeList.get(nodeIndexI).add(statement.getBeginLine());

        }
        }
}
