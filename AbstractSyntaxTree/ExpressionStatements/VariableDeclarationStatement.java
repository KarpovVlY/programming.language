package com.company.AbstractSyntaxTree.ExpressionStatements;

import com.company.AbstractSyntaxTree.BaseStatements.BaseStatement;
import com.company.Service.Token;

public class VariableDeclarationStatement extends BaseStatement
{
    private ExpressionStatement expressionStatement;



    public VariableDeclarationStatement() { }


    public void setExpressionStatement(Token token) { expressionStatement = new ExpressionStatement(token); }
    public ExpressionStatement getExpressionStatement() { return expressionStatement; }

    @Override
    public void addToken(Object token) { expressionStatement.addToken(token); }
}
