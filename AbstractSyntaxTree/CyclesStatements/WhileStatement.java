package com.company.AbstractSyntaxTree.CyclesStatements;

import com.company.AbstractSyntaxTree.BaseStatements.BaseStatement;
import com.company.AbstractSyntaxTree.BaseStatements.BlockStatement;
import java.util.ArrayList;

public class WhileStatement extends BlockStatement
{

    public WhileStatement(int tabOrder)
    {
        astTokens = new ArrayList<>();
        complexStatements = new ArrayList<>();
        this.tabOrder = tabOrder;
    }

    @Override
    public void process() throws Exception
    {
        super.process();
    }

    public ArrayList<BaseStatement> getStatements() { return statements; }
}
