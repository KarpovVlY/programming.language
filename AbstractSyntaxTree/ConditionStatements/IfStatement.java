package com.company.AbstractSyntaxTree.ConditionStatements;

import com.company.AbstractSyntaxTree.BaseStatements.BaseStatement;
import com.company.AbstractSyntaxTree.BaseStatements.BlockStatement;
import java.util.ArrayList;

public class IfStatement extends BlockStatement
{

    private ArrayList<BaseStatement> positiveStatements;
    private ArrayList<BaseStatement> negativeStatements;


    public IfStatement(int tabOrder)
    {
        astTokens = new ArrayList<>();
        complexStatements = new ArrayList<>();
        this.tabOrder = tabOrder;
    }


    @Override
    public void process() throws Exception
    {
        super.process();

        if(positiveStatements == null)
        {
            positiveStatements = statements;
            complexStatements = new ArrayList<>();
        }
        else
        {
            negativeStatements = statements;
            complexStatements = null;
        }

        statements = null;

    }

    public ArrayList<BaseStatement> getPositiveStatements() { return positiveStatements; }
    public ArrayList<BaseStatement> getNegativeStatements() { return negativeStatements; }
}
