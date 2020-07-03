package com.company.AbstractSyntaxTree.PrintStatements;

import com.company.AbstractSyntaxTree.BaseStatements.BaseStatement;

import java.util.ArrayList;

public class PrintStatement extends BaseStatement
{

    public PrintStatement()
    {
        astTokens = new ArrayList<>();
    }

    public ArrayList<Object> getTokens()
    {
        return astTokens;
    }
}
