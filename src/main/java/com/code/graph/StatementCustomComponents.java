/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.code.graph;

import com.github.javaparser.ast.stmt.Statement;

/**
 *
 * @author juliuz
 */
public class StatementCustomComponents {
    public Statement body;
    public String stmtType;
    public int startLine=0;
    public int endLine=0;
    public int stmtID=0;
    
    
    public StatementCustomComponents(){
        
    }
    
    public StatementCustomComponents(Statement body, String stmtType, int startLine, int endLine, int stmtID){
        this.body=body;
        this.stmtType=stmtType;
        this.startLine=startLine;
        this.endLine=endLine;
        this.stmtID=stmtID;
    }
    
}
