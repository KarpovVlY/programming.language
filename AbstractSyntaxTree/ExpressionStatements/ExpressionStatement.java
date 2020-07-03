package com.company.AbstractSyntaxTree.ExpressionStatements;

import com.company.AbstractSyntaxTree.BaseStatements.BaseStatement;
import com.company.Service.Token;

import java.util.ArrayList;

public class ExpressionStatement extends BaseStatement
{
    private Token expressionID;

    public ExpressionStatement(Token expressionID)
    {
        this.expressionID = expressionID;
        astTokens = new ArrayList<>();
    }

    public Token getID() { return expressionID; }
}
