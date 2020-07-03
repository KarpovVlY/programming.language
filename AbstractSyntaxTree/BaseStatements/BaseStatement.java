package com.company.AbstractSyntaxTree.BaseStatements;


import java.util.ArrayList;

public class BaseStatement
{
    protected ArrayList<Object> astTokens;

    public void process() throws Exception { }

    public void addToken(Object token) { astTokens.add(token); }
    public ArrayList<Object> getTokens() { return astTokens; }
}
