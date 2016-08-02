package com.code.graph;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author T7
 */
/**
 * Visit every method to parse statements from each
 */
public class VisitStatements extends VoidVisitorAdapter {

    protected List<Statement> LIST_STATEMENTS;

    protected ArrayList<ArrayList<Integer>> nodeList;
    protected ArrayList<ArrayList<Integer>> edgeList;

    protected ArrayList<ArrayList<Integer>> loopToConnectLineNumbers;
    protected ArrayList<ArrayList<Integer>> loopConnectionArray;
    protected ArrayList<ArrayList<Integer>> doWhileLoopConnectionArray;
    protected ArrayList<ArrayList<Integer>> ifStatementConnectionArray;

    protected int nodeIndexI = 0;
    boolean elseStatementExist;
    boolean statementExist;
    protected int edgeIndex = 0;
    protected int loopIndex = 0;
    protected int ifIndex = 0;
    XMLGeneration xmlGeneration ;
    BlockStmt method_block;

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        nodeIndexI = 0;
        edgeIndex = 0;
        ifIndex = 0;
        loopIndex = 0;
        nodeList = new ArrayList<ArrayList<Integer>>();
        edgeList = new ArrayList<ArrayList<Integer>>();
        loopToConnectLineNumbers = new ArrayList<ArrayList<Integer>>();
        loopConnectionArray = new ArrayList<ArrayList<Integer>>();
        doWhileLoopConnectionArray = new ArrayList<ArrayList<Integer>>();
        ifStatementConnectionArray = new ArrayList<ArrayList<Integer>>();
        System.out.println("----------------Statements in [" + n.getName() + "] are: ----------------------");
        String methodName = n.getName();
        method_block = new BlockStmt();
        method_block = n.getBody();

        makeNodesFromStatements(method_block);


        xmlGeneration = new XMLGeneration(nodeList, edgeList, methodName);
        try {
            xmlGeneration.writeXML();
        } catch (ParserConfigurationException parse) {

        } catch (TransformerException transformerException) {

        }
              testMethod();

        System.out.println("---------Method [" + n.getName() + "] Ends------------------------");
    }

    @Override
    public void visit(IfStmt n, Object arg) {

        if (n instanceof IfStmt) {

            boolean statementAdded = addNewStatementNumToNodeList(n.getBeginLine());
            int nodeIndexHoldingIfStatementLineNum = nodeIndexI;
            int ifStatementEndLine = -1;
            int nodeIndexHoldingIfStatementEndingLineNum = -1;
            if (statementAdded) {

                //      System.out.println("nodeIndexHoldingIfStatementLineNum:: "+nodeIndexHoldingIfStatementLineNum);
                nodeIndexI++;
                System.out.println("IF-STMT at line : (" + n.getBeginLine() + ") --> " + n.getCondition());
                System.out.println("IF-STMT ends at line : (" + n.getEndLine() + ") --> ");

                int nextNodeIndexToConnectTest = nodeIndexI;
                if (n.getThenStmt() != null) {

                    BlockStmt blockThenStmt = new BlockStmt();
                    blockThenStmt = (BlockStmt) n.getThenStmt();
                    makeNodesFromStatements(blockThenStmt);

                    int nextNodeIndex = findNextNode(nextNodeIndexToConnectTest);
                    boolean edgeExist = false;
                    edgeExist = checkIfEdgeExist(nodeIndexHoldingIfStatementLineNum, nextNodeIndex);

                    if (edgeExist == false) {
                        addNewEdge(nodeIndexHoldingIfStatementLineNum, nextNodeIndex);
                    }
                    ifStatementEndLine = n.getEndLine();
                    nodeIndexI++;
                    addNewStatementNumToNodeList(ifStatementEndLine);
                    //    int lineNumBeforeIfStatementEndLine = findPreviousStatement(ifStatementEndLine);
                    nodeIndexHoldingIfStatementEndingLineNum = nodeIndexI;
                    nodeIndexI++;

                    //       System.out.println("THEN---STMT at line ("+n.getThenStmt().getBeginLine()+") --> "+n.getThenStmt());
                }

                handleElseStatement(n, nodeIndexHoldingIfStatementLineNum);

            }
        }
        //flag visited methods using voidvisitoradapter
        super.visit(n, arg);
    }

    public void handleElseStatement(IfStmt n, int nodeIndexHoldingIfStatementLineNum) {
        //   int ifStatementEndLine = n.getEndLine();

        int nodeIndexHoldingElseStatementBeginLine = -1;
        if (n.getElseStmt() != null) {
            int elseStatementBeginLineNum = n.getElseStmt().getBeginLine();
            boolean found = false;

            for (int i = 0; i < nodeList.size(); i++) {    //iteration checks if this node already exists to prevent repetation of same node
                if (nodeList.get(i).contains(n.getElseStmt().getBeginLine())) {
                    found = true;
                    break;
                }
            }

            if (found == false) {
                //     System.out.println("ELSE---STMT at line(" + n.getElseStmt().getBeginLine() + ") --> " + n.getElseStmt());

                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());

                nodeList.get(nodeIndexI).add(elseStatementBeginLineNum);

                addEdgeFromLoop(n.getElseStmt().getBeginLine());
                nodeIndexHoldingElseStatementBeginLine = nodeIndexI;
                addNewEdge(nodeIndexHoldingIfStatementLineNum, nodeIndexHoldingElseStatementBeginLine);   // adding new edge from if statement to else
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                makeNodesFromElseStatements(n.getElseStmt().getChildrenNodes());
            }

            if (n.getElseStmt().getChildrenNodes() != null) {
                makeNodesFromElseStatements(n.getElseStmt().getChildrenNodes());
                //        System.out.println("ELSE---STMT at getChildrenNodes("+n.getElseStmt().getChildrenNodes() +") --> \n \n");
                //      System.out.println("ELSE---STMT at getParentNode("+n.getElseStmt().getParentNode()+") --> ");
            }

            int elseStatementEndLine = n.getElseStmt().getEndLine();

            int nodeIndexHoldingElseStatementEndingLineNum = -1;
            for (int i = 0; i < nodeList.size(); i++) {    //iteration checks if this node already exists to prevent repetation of same node
                if (nodeList.get(i).contains(n.getElseStmt().getEndLine())) {
                    nodeIndexHoldingElseStatementEndingLineNum = i;
                    break;
                }
            }

            if (nodeIndexHoldingElseStatementEndingLineNum == -1) {
                nodeIndexI++;
                addNewStatementNumToNodeList(elseStatementEndLine);
                nodeIndexHoldingElseStatementEndingLineNum = nodeIndexI;

                nodeIndexI++;
            }
            int lineNumAfterElseStatementEndLine = findNextStatement(elseStatementEndLine);
            int previousLineNumOfElseStatementBeginLineNum = findPreviousStatement(elseStatementBeginLineNum);

            int lineNumAfterElseStatementBeginLine = findNextStatement(elseStatementBeginLineNum);

            addToIfConnectionArray(nodeIndexHoldingIfStatementLineNum, previousLineNumOfElseStatementBeginLineNum, -1, lineNumAfterElseStatementEndLine, nodeIndexHoldingElseStatementEndingLineNum, nodeIndexHoldingElseStatementBeginLine, elseStatementBeginLineNum, lineNumAfterElseStatementBeginLine);
        }

        if (n.getElseStmt() == null) {
            int ifStatementEndLine = n.getEndLine();
            int nodeIndexHoldingIfStatementEndingLineNum = -1;
            int lineNumAfterIfStatementEndLine = findNextStatement(ifStatementEndLine);
            for (int i = 0; i < nodeList.size(); i++) {
                if (nodeList.get(i).contains(ifStatementEndLine)) {
                    nodeIndexHoldingIfStatementEndingLineNum = i;
                    break;
                }
            }
            System.out.println(" \n \n \n  \n \n nodeIndexHoldingIfStatementEndingLineNum::::: " + nodeIndexHoldingIfStatementEndingLineNum);

            if (lineNumAfterIfStatementEndLine != -1 && nodeIndexHoldingIfStatementEndingLineNum != -1) {
                //      System.out.println(" \n \n \n \n \n lineNumAfterIfStatementEndLine: " + lineNumAfterIfStatementEndLine);
                addToIfConnectionArray(nodeIndexHoldingIfStatementLineNum, nodeIndexHoldingIfStatementEndingLineNum, lineNumAfterIfStatementEndLine, -1, -1, -1, -1, -1);
            }
        }

    }

    @Override
    public void visit(ForStmt n, Object arg) {

        if (n instanceof ForStmt) {

            int previousNodeIndex = findPreviousNode(nodeIndexI);
            boolean nodeIndexHoldingLoopStatementEndLine = checkIfNodeIndexHoldingLoopStatementEndLine(previousNodeIndex);
            nodeIndexI++;
            boolean statementAdded = addNewStatementNumToNodeList(n.getBeginLine());

            int nodeIndexHoldingForStatementLineNum = nodeIndexI;

            boolean previousNodeEmpty = nodeList.get(previousNodeIndex).isEmpty();
            boolean currntNodeEmpty = nodeList.get(nodeIndexI).isEmpty();

            if (!previousNodeEmpty && !currntNodeEmpty && !nodeIndexHoldingLoopStatementEndLine) {

                addNewEdge(previousNodeIndex, nodeIndexI);
            }
            nodeIndexI++;
            int nextNodeIndexToConnectTest = nodeIndexI;
            nodeList.add(new ArrayList<Integer>());
            BlockStmt blockStmt = new BlockStmt();
            blockStmt = (BlockStmt) n.getBody();
            nodeIndexI++;
            makeNodesFromStatements(blockStmt);
            if (statementAdded) {

                int nextNodeIndex = findNextNode(nextNodeIndexToConnectTest);

                addNewEdge(nodeIndexHoldingForStatementLineNum, nextNodeIndex);

                //// Connect end of for loop to the beginning line of for loop
                int endLine = n.getEndLine();
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                nodeList.get(nodeIndexI).add(endLine);

                int nodeIndexHoldingLoopEndingLineNum = nodeIndexI;

                addNewEdge(nodeIndexI, nodeIndexHoldingForStatementLineNum);

                nodeList.add(new ArrayList<Integer>());   /////////////////****************** might be creating excess nodes

                int lineNumAfterEndLine = findNextStatement(endLine);

                if (lineNumAfterEndLine != -1) {
                    addToLoopConnectionArray(nodeIndexHoldingForStatementLineNum, nodeIndexHoldingLoopEndingLineNum, lineNumAfterEndLine);
                }

                nodeIndexI++;
            }

        }
        super.visit(n, arg);
    }

    @Override
    public void visit(WhileStmt n, Object arg) {

        if (n instanceof WhileStmt) {
            int previousNodeIndex = findPreviousNode(nodeIndexI);
            boolean nodeIndexHoldingLoopStatementEndLine = checkIfNodeIndexHoldingLoopStatementEndLine(previousNodeIndex);
            nodeIndexI++;
            boolean statementAdded = addNewStatementNumToNodeList(n.getBeginLine());

            int nodeIndexHoldingForStatementLineNum = nodeIndexI;

            boolean previousNodeEmpty = nodeList.get(previousNodeIndex).isEmpty();
            boolean currntNodeEmpty = nodeList.get(nodeIndexI).isEmpty();

            if (!previousNodeEmpty && !currntNodeEmpty && !nodeIndexHoldingLoopStatementEndLine) {

                addNewEdge(previousNodeIndex, nodeIndexI);
            }
            nodeIndexI++;
            int nextNodeIndexToConnectTest = nodeIndexI;
            nodeList.add(new ArrayList<Integer>());
            BlockStmt blockStmt = new BlockStmt();
            blockStmt = (BlockStmt) n.getBody();
            nodeIndexI++;
            makeNodesFromStatements(blockStmt);
            if (statementAdded) {

                int nextNodeIndex = findNextNode(nextNodeIndexToConnectTest);

                addNewEdge(nodeIndexHoldingForStatementLineNum, nextNodeIndex);

                //// Connect end of for loop to the beginning line of for loop
                int endLine = n.getEndLine();
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                nodeList.get(nodeIndexI).add(endLine);

                int nodeIndexHoldingLoopEndingLineNum = nodeIndexI;

                addNewEdge(nodeIndexI, nodeIndexHoldingForStatementLineNum);

                nodeList.add(new ArrayList<Integer>());   /////////////////****************** might be creating excess nodes

                int lineNumAfterEndLine = findNextStatement(endLine);

                if (lineNumAfterEndLine != -1) {
                    addToLoopConnectionArray(nodeIndexHoldingForStatementLineNum, nodeIndexHoldingLoopEndingLineNum, lineNumAfterEndLine);
                }

                nodeIndexI++;
            }

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

    public boolean checkIfNodeIndexHoldingLoopStatementEndLine(int previousNodeIndex) {
        for (int i = 0; i < loopConnectionArray.size(); i++) {
            int nodeIndexHoldingLoopStatementEndLine = loopConnectionArray.get(i).get(1);
            if (nodeIndexHoldingLoopStatementEndLine == previousNodeIndex) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visit(DoStmt n, Object arg) {

        if (n instanceof DoStmt) {

            System.out.println(" \n do while loop body" + n.getBody());
            System.out.println(" \n do while loop begin line " + n.getBeginLine());
            System.out.println(" \n do while loop begin line " + n.getEndLine());
            int previousLineNum = findPreviousLine(n.getBeginLine());
            int previousNodeIndex = findNodeIndexFromLineNum(previousLineNum);
            //int previousNodeIndex = findPreviousNode(nodeIndexI);
            boolean nodeIndexHoldingLoopStatementEndLine = checkIfNodeIndexHoldingLoopStatementEndLine(previousNodeIndex);

            nodeIndexI++;
            boolean statementAdded = addNewStatementNumToNodeList(n.getBeginLine());

            int nodeIndexHoldingForStatementLineNum = nodeIndexI;

            boolean previousNodeEmpty = nodeList.get(previousNodeIndex).isEmpty();
            boolean currntNodeEmpty = nodeList.get(nodeIndexI).isEmpty();

            if (!previousNodeEmpty && !currntNodeEmpty && !nodeIndexHoldingLoopStatementEndLine) {
                //System.out.println("Previous Node Index:"+previousNodeIndex+", nodeIndexI: "+nodeIndexI);
                addNewEdge(previousNodeIndex, nodeIndexI);
            }
            nodeIndexI++;
            int nextNodeIndexToConnectTest = nodeIndexI;
            nodeList.add(new ArrayList<Integer>());
            BlockStmt blockStmt = new BlockStmt();
            blockStmt = (BlockStmt) n.getBody();
            nodeIndexI++;
            makeNodesFromStatements(blockStmt);
            if (statementAdded) {

                int nextNodeIndex = findNextNode(nextNodeIndexToConnectTest);

                addNewEdge(nodeIndexHoldingForStatementLineNum, nextNodeIndex);

                //// Connect end of for loop to the beginning line of for loop
                int endLine = n.getEndLine();
                nodeIndexI++;
                nodeList.add(new ArrayList<Integer>());
                nodeList.get(nodeIndexI).add(endLine);

                int nodeIndexHoldingLoopEndingLineNum = nodeIndexI;
                addNewEdge(nodeIndexI, nodeIndexHoldingForStatementLineNum);
                nodeList.add(new ArrayList<Integer>());   /////////////////****************** might be creating excess nodes
                int lineNumAfterEndLine = findNextStatement(endLine);
                if (lineNumAfterEndLine != -1) {
                    addToDoWhileLoopConnectionArray(nodeIndexHoldingForStatementLineNum, nodeIndexHoldingLoopEndingLineNum, lineNumAfterEndLine);
                }

                nodeIndexI++;
            }

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
                addNewStatementNumToNodeList(statement.getBeginLine());
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
            addEdgeFromIf(statementNum);
            addEdgeFromDoWhileLoop(statementNum);
            return true;
        }

        return false;

    }

    public void addEdgeFromLoop(int statementBeginLine) {
        if (!loopConnectionArray.isEmpty()) {                           /////   checks if any loop starting node left to connect to the second node.
            for (int i = 0; i < loopConnectionArray.size(); i++) {
                int outOfLoopLineNum = loopConnectionArray.get(i).get(2);
                int nodeIndexHoldingLoopStatementBeginLine = loopConnectionArray.get(i).get(0);
                int nodeIndexHoldingLoopStatementEndLine = loopConnectionArray.get(i).get(1);

                int loopStatementEndLine = nodeList.get(nodeIndexHoldingLoopStatementEndLine).get(nodeList.get(nodeIndexHoldingLoopStatementEndLine).size() - 1);   ///// gets last Element Of Node Holding Loop Statement End Line
                int nodeIndexHoldingOutOfLoopLineNum = -1;
                int previousLineBeforeLoopEndLine = -1;
                int previousNodeBeforeLoopEndLineNode = -1;

                outOfLoopLineNum = findNextLine(loopStatementEndLine);
                previousLineBeforeLoopEndLine = findPreviousLine(loopStatementEndLine);;
                nodeIndexHoldingOutOfLoopLineNum = findNodeIndexFromLineNum(outOfLoopLineNum);
                previousNodeBeforeLoopEndLineNode = findNodeIndexFromLineNum(previousLineBeforeLoopEndLine);

                if (nodeIndexHoldingOutOfLoopLineNum != -1) {
                    addNewEdge(nodeIndexHoldingLoopStatementBeginLine, nodeIndexHoldingOutOfLoopLineNum);
                }

                if (previousNodeBeforeLoopEndLineNode != -1) {
                    addNewEdge(previousNodeBeforeLoopEndLineNode, nodeIndexHoldingLoopStatementEndLine);
                }
            }
        }

    }

    public void addEdgeFromDoWhileLoop(int statementBeginLine) {
        if (!doWhileLoopConnectionArray.isEmpty()) {                           /////   checks if any loop starting node left to connect to the second node.
            for (int i = 0; i < doWhileLoopConnectionArray.size(); i++) {
                int outOfLoopLineNum = doWhileLoopConnectionArray.get(i).get(2);
                int nodeIndexHoldingLoopStatementBeginLine = doWhileLoopConnectionArray.get(i).get(0);
                int nodeIndexHoldingLoopStatementEndLine = doWhileLoopConnectionArray.get(i).get(1);

                int loopStatementEndLine = nodeList.get(nodeIndexHoldingLoopStatementEndLine).get(nodeList.get(nodeIndexHoldingLoopStatementEndLine).size() - 1);   ///// gets last Element Of Node Holding Loop Statement End Line
                int nodeIndexHoldingOutOfLoopLineNum = -1;
                int previousLineBeforeLoopEndLine = -1;
                int previousNodeBeforeLoopEndLineNode = -1;

                outOfLoopLineNum = findNextLine(loopStatementEndLine);
                previousLineBeforeLoopEndLine = findPreviousLine(loopStatementEndLine);;
                nodeIndexHoldingOutOfLoopLineNum = findNodeIndexFromLineNum(outOfLoopLineNum);
                previousNodeBeforeLoopEndLineNode = findNodeIndexFromLineNum(previousLineBeforeLoopEndLine);

                if (nodeIndexHoldingLoopStatementEndLine != -1) {
                    addNewEdge(nodeIndexHoldingLoopStatementEndLine, nodeIndexHoldingLoopStatementBeginLine);
                }

                if (previousNodeBeforeLoopEndLineNode != -1) {
                    addNewEdge(previousNodeBeforeLoopEndLineNode, nodeIndexHoldingLoopStatementEndLine);
                }

                if (nodeIndexHoldingOutOfLoopLineNum != -1) {
                    addNewEdge(nodeIndexHoldingLoopStatementEndLine, nodeIndexHoldingOutOfLoopLineNum);
                }
            }
        }

    }

    public void addEdgeFromIf(int statementBeginLine) {
        if (!ifStatementConnectionArray.isEmpty()) {                           /////   checks if any loop starting node left to connect to the second node.
            for (int i = 0; i < ifStatementConnectionArray.size(); i++) {
                int lineNumAfterIfStatementEndLine = ifStatementConnectionArray.get(i).get(2);
                int lineNumAfterElseStatementEndLine = ifStatementConnectionArray.get(i).get(3);

                if (lineNumAfterIfStatementEndLine != -1) {
                    addEdgefromIfWhenNoElse(i);
                }
                if (lineNumAfterElseStatementEndLine != -1) {
                    addEdgefromIfWhenThereIsElse(i);
                }
            }
        }
    }

    public void addEdgefromIfWhenNoElse(int i) {

        int nodeIndexHoldingIfStatementLineNum = ifStatementConnectionArray.get(i).get(0);
        int nodeIndexHoldingIfStatementEndingLineNum = ifStatementConnectionArray.get(i).get(1);
        int ifStatementEndLine = nodeList.get(nodeIndexHoldingIfStatementEndingLineNum).get(nodeList.get(nodeIndexHoldingIfStatementEndingLineNum).size() - 1);   ///// gets last Element Of Node Holding Loop Statement End Line
        int lineNumAfterIfStatementEndLine = findNextLine(ifStatementEndLine);
        int previousLineBeforeIfEndLine = findPreviousLine(ifStatementEndLine);
        int nodeIndexHoldingOutOfIfLineNum = findNodeIndexFromLineNum(lineNumAfterIfStatementEndLine);
        int previousNodeBeforeIfEndLineNode = findNodeIndexFromLineNum(previousLineBeforeIfEndLine);
        if (nodeIndexHoldingOutOfIfLineNum != -1) {
            addNewEdge(nodeIndexHoldingIfStatementEndingLineNum, nodeIndexHoldingOutOfIfLineNum);
            addNewEdge(nodeIndexHoldingIfStatementLineNum, nodeIndexHoldingOutOfIfLineNum);
        }
        if (previousNodeBeforeIfEndLineNode != -1) {
            addNewEdge(previousNodeBeforeIfEndLineNode, nodeIndexHoldingIfStatementEndingLineNum);
        }
    }

    public int findNextLine(int lineNo) {
        int lineNumAfterIfStatementEndLine = -1;
        for (int j = 0; j < nodeList.size(); j++) {     ////// This iteration is to get previous and next lines of if ending line
            for (int k = 0; k < ((ArrayList) nodeList.get(j)).size(); k++) {
                if (nodeList.get(j).get(k) > lineNo && (lineNumAfterIfStatementEndLine == -1 || nodeList.get(j).get(k) < lineNumAfterIfStatementEndLine)) {   //This condition is to get lineNumAfterIfStatementEndLine
                    lineNumAfterIfStatementEndLine = nodeList.get(j).get(k);
                }

            }
        }

        return lineNumAfterIfStatementEndLine;
    }

    public int findPreviousLine(int lineNo) {
        int previousLineBeforeIfEndLine = -1;
        for (int j = 0; j < nodeList.size(); j++) {     ////// This iteration is to get previous and next lines of if ending line
            for (int k = 0; k < ((ArrayList) nodeList.get(j)).size(); k++) {

                if (nodeList.get(j).get(k) < lineNo && (previousLineBeforeIfEndLine == -1 || nodeList.get(j).get(k) > previousLineBeforeIfEndLine)) {  //This condition is to get previousLineBeforeIfEndLine
                    previousLineBeforeIfEndLine = nodeList.get(j).get(k);
                }
            }
        }

        return previousLineBeforeIfEndLine;
    }

    public int findNodeIndexFromLineNum(int lineNum) {
        for (int j = 0; j < nodeList.size(); j++) {
            for (int k = 0; k < ((ArrayList) nodeList.get(j)).size(); k++) {
                if (nodeList.get(j).get(k) == lineNum) {     ////// This condition to get the node-index holding next line  of loop ending line
                    return j;
                }
            }
        }
        return 1;
    }

    public void addEdgefromIfWhenThereIsElse(int i) {

        int nodeIndexHoldingElseStatementEndingLineNum = ifStatementConnectionArray.get(i).get(4);
        int nodeIndexHoldingElseStatementBeginLine = ifStatementConnectionArray.get(i).get(5);
        int elseStatementBeginLineNum = ifStatementConnectionArray.get(i).get(6);

        int elseStatementEndLine = nodeList.get(nodeIndexHoldingElseStatementEndingLineNum).get(nodeList.get(nodeIndexHoldingElseStatementEndingLineNum).size() - 1);   ///// gets last Element Of Node Holding Loop Statement End Line

        int lineNumAfterElseStatementEndLine = findNextLine(elseStatementEndLine);

        int lineNumAfterElseStatementBeginLine = findNextLine(elseStatementBeginLineNum);

        int previousLineNumOfElseStatementBeginLineNum = findPreviousLine(elseStatementBeginLineNum);
        int ifStatementEndLine = previousLineNumOfElseStatementBeginLineNum;

        int previousLineBeforeElseEndLine = findPreviousLine(elseStatementEndLine);

        int previousLineBeforeIfEndLine = findPreviousLine(ifStatementEndLine);

        int nodeIndexHoldingOutOfElseLineNum = findNodeIndexFromLineNum(lineNumAfterElseStatementEndLine);

        int nodeIndexHoldingNextLineOfElseStatementBeginLine = findNodeIndexFromLineNum(lineNumAfterElseStatementBeginLine);

        int previousNodeBeforeElseEndLineNode = findNodeIndexFromLineNum(previousLineBeforeElseEndLine);

        int previousNodeBeforeIfEndLineNode = findNodeIndexFromLineNum(previousLineBeforeIfEndLine);
        int nodeIndexHoldingIfStatementEndingLineNum = findNodeIndexFromLineNum(ifStatementEndLine);

        if (nodeIndexHoldingOutOfElseLineNum != -1) {
            addNewEdge(nodeIndexHoldingIfStatementEndingLineNum, nodeIndexHoldingOutOfElseLineNum);
            addNewEdge(nodeIndexHoldingElseStatementEndingLineNum, nodeIndexHoldingOutOfElseLineNum);
        }

        if (nodeIndexHoldingNextLineOfElseStatementBeginLine != -1) {
            addNewEdge(nodeIndexHoldingElseStatementBeginLine, nodeIndexHoldingNextLineOfElseStatementBeginLine);
        }

        if (previousNodeBeforeElseEndLineNode != -1) {
            addNewEdge(previousNodeBeforeElseEndLineNode, nodeIndexHoldingElseStatementEndingLineNum);
        }
    }

    public void addNewEdge(int nodeIndexFrom, int nodeIndexTo) {

        boolean edgeExist = checkIfEdgeExist(nodeIndexFrom, nodeIndexTo);

        if (!edgeExist) {
            edgeList.add(new ArrayList<Integer>());
            edgeList.get(edgeIndex).add(nodeIndexFrom);
            edgeList.get(edgeIndex).add(nodeIndexTo);
            edgeIndex++;
        }
    }

    public int findPreviousNode(int previousNodeIndexTest) {
        //  int  previousNodeIndexTest = nodeIndexI;
        //        System.out.println(" previousNodeIndex 1: "+previousNodeIndexTest);
        while (nodeList.get(previousNodeIndexTest).isEmpty() && previousNodeIndexTest > 1) {
            previousNodeIndexTest = previousNodeIndexTest - 1;
        }
        //      System.out.println(" previousNodeIndex 2: "+previousNodeIndexTest);
        return previousNodeIndexTest;
    }

    public int findNextNode(int nextNodeIndexToConnectTest) {
        int nextNodeIndexTest = nextNodeIndexToConnectTest;
        while (nodeList.get(nextNodeIndexTest).isEmpty()) {
            nextNodeIndexTest = nextNodeIndexTest + 1;
        }
        return nextNodeIndexTest;
    }

    public boolean checkIfEdgeExist(int nodeIndexFrom, int nodeIndexTo) {
        boolean edgeExist = false;
        for (int i = 0; i < edgeList.size(); i++) {
            if (edgeList.get(i).get(0) == nodeIndexFrom && edgeList.get(i).get(1) == nodeIndexTo) {
                edgeExist = true;
                break;
            }
        }
        return edgeExist;
    }

    public int findNextStatement(int lineNum) {
        List<Statement> allStatementsInMethod;
        allStatementsInMethod = method_block.getStmts();
        for (Statement statement : allStatementsInMethod) {
            int statementLineNo = statement.getBeginLine();

            if (statementLineNo > lineNum) {
                statementLineNo = checkNodeListIfAnyNodeExistInBetween(lineNum, statementLineNo);
                return statementLineNo;
            }
        }

        return -1;
    }

    public int checkNodeListIfAnyNodeExistInBetween(int lineNo, int possibleNextLine) {
        int nextLine = possibleNextLine;
        for (int j = 0; j < nodeList.size(); j++) {

            for (int k = 0; k < ((ArrayList) nodeList.get(j)).size(); k++) {
                int currentLineNo = nodeList.get(j).get(k);
                if (currentLineNo > lineNo && currentLineNo < nextLine) {
                    nextLine = currentLineNo;
                }
            }

        }
        return nextLine;
    }

    public int findPreviousStatement(int lineNum) {
        int previousStatementLineNo = -1;
        for (Statement statement : LIST_STATEMENTS) {
            int statementLineNo = statement.getBeginLine();
            if (statementLineNo < lineNum && (previousStatementLineNo == -1 || statementLineNo > previousStatementLineNo)) {
                previousStatementLineNo = statementLineNo;
            }
        }

        return previousStatementLineNo;
    }

    public void addToLoopConnectionArray(int nodeIndexHoldingLoopBeginningLineNum, int nodeIndexHoldingLoopEndingLineNum, int outOfLoopLineNum) {
        loopConnectionArray.add(new ArrayList<Integer>());
        loopConnectionArray.get(loopIndex).add(nodeIndexHoldingLoopBeginningLineNum);
        loopConnectionArray.get(loopIndex).add(nodeIndexHoldingLoopEndingLineNum);
        loopConnectionArray.get(loopIndex).add(outOfLoopLineNum);
        loopIndex++;

    }

    public void addToDoWhileLoopConnectionArray(int nodeIndexHoldingLoopBeginningLineNum, int nodeIndexHoldingLoopEndingLineNum, int outOfLoopLineNum) {
        loopConnectionArray.add(new ArrayList<Integer>());
        loopConnectionArray.get(loopIndex).add(nodeIndexHoldingLoopBeginningLineNum);
        loopConnectionArray.get(loopIndex).add(nodeIndexHoldingLoopEndingLineNum);
        loopConnectionArray.get(loopIndex).add(outOfLoopLineNum);
        loopIndex++;

    }

    public void addToIfConnectionArray(int nodeIndexHoldingIfStatementLineNum, int nodeIndexHoldingIfStatementEndingLineNum, int lineNumAfterIfStatementEndLine, int lineNumAfterElseStatementEndLine, int nodeIndexHoldingElseStatementEndingLineNum, int nodeIndexHoldingElseStatementBeginLine, int elseStatementBeginLineNum, int lineNumAfterElseStatementBeginLine) {

        ifStatementConnectionArray.add(new ArrayList<Integer>());
        ifStatementConnectionArray.get(ifIndex).add(nodeIndexHoldingIfStatementLineNum);
        ifStatementConnectionArray.get(ifIndex).add(nodeIndexHoldingIfStatementEndingLineNum);  /// If there is a else statement this index contains previousLineNumOfElseStatementBeginLineNum
        ifStatementConnectionArray.get(ifIndex).add(lineNumAfterIfStatementEndLine);
        ifStatementConnectionArray.get(ifIndex).add(lineNumAfterElseStatementEndLine);
        ifStatementConnectionArray.get(ifIndex).add(nodeIndexHoldingElseStatementEndingLineNum);
        ifStatementConnectionArray.get(ifIndex).add(nodeIndexHoldingElseStatementBeginLine);
        ifStatementConnectionArray.get(ifIndex).add(elseStatementBeginLineNum);
        ifStatementConnectionArray.get(ifIndex).add(lineNumAfterElseStatementBeginLine);
        ifIndex++;
    }

}
