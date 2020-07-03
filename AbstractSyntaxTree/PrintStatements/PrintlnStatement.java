package com.company.AbstractSyntaxTree.PrintStatements;

import com.company.AbstractSyntaxTree.BaseStatements.BaseStatement;

import java.util.ArrayList;

public class PrintlnStatement extends BaseStatement
{

    public PrintlnStatement()
    {
        astTokens = new ArrayList<>();
    }

    public ArrayList<Object> getTokens()
    {
        return astTokens;
    }
}
