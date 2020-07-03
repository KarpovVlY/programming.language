package com.company.AbstractSyntaxTree.ConditionStatements;

import com.company.AbstractSyntaxTree.BaseStatements.BlockStatement;

import java.util.ArrayList;

public class ElseStatement extends BlockStatement
{
    public ElseStatement(int tabOrder)
    {
        astTokens = new ArrayList<>();
        complexStatements = new ArrayList<>();
        this.tabOrder = tabOrder;
    }
}
